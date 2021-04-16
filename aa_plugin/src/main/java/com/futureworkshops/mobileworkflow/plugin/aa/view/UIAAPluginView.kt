/*
 * Copyright (c) 2021 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.aa.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.aatechintl.aaprintscanner.ActivityHandler
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStep
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStepConfiguration
import com.futureworkshops.mobileworkflow.data.filehandler.IMWFileHandler
import com.futureworkshops.mobileworkflow.data.network.task.URLIAsyncTask
import com.futureworkshops.mobileworkflow.data.network.task.URLMethod
import com.futureworkshops.mobileworkflow.model.result.FragmentStepResult
import com.futureworkshops.mobileworkflow.plugin.aa.model.AAAnswer
import com.futureworkshops.mobileworkflow.plugin.aa.model.LicenseResponse
import com.futureworkshops.mobileworkflow.plugin.aa.model.PreLicense
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.IOException
import java.io.OutputStream

class AAFileInteractor(private val appContext: Context?, private val fileHandler: IMWFileHandler) {
    fun getImagePathsFromFingerInfo(fingerInfoList: List<FingerInfo>): List<String> {
        val imagePathList = mutableListOf<String>()
        appContext?.let { safeContext ->
            val outputDirectory = fileHandler.getOutputDirectory(safeContext)
            fingerInfoList.forEach {
                val file = fileHandler.createFile(outputDirectory.absolutePath, "jpg")
                val fos = fileHandler.createFileOutputStream(file.absolutePath)
                fileHandler.writeOutputStream(fos, it.imageByteArray)
                fileHandler.flushOutputStream(fos)
                fileHandler.flushOutputStream(fos)

                imagePathList.add(file.absolutePath)
            }
        }
        return imagePathList
    }


    companion object {
        var shared: AAFileInteractor? = null
    }
}

internal class UIAAPluginView(
    private val fragmentStepConfiguration: FragmentStepConfiguration,
    val licenseURL: String,
    val mode: String,
    val tintColor: Int?
) : FragmentStep(fragmentStepConfiguration)  {

    private lateinit var outputDirectory: File
    private val results: MutableList<String> = mutableListOf()
    private var fingerId: Int = 0
    private var fingerPrintPdfPath: String = ""

    private lateinit var aAPart: UIAAPart

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

        if (result.resultCode == MobileWorkflowAAActivity.RC_AA_FINGER_CAPTURE) {
            val resultsArrayList = result.data?.getStringArrayListExtra(MobileWorkflowAAActivity.FINGER_INFO_LIST_EXTRA)
            resultsArrayList?.let { processResults(it) }
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            header.onBack()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let { outputDirectory = fragmentStepConfiguration.mobileWorkflowServices.fileHandler.getOutputDirectory(it) }
        AAFileInteractor.shared = AAFileInteractor(activity?.applicationContext, fragmentStepConfiguration.mobileWorkflowServices.fileHandler)
    }

    override fun createResults(): FragmentStepResult<AAAnswer> {
        return FragmentStepResult(
            identifier = id.id,
            answer = AAAnswer(
                fingerPrintPdfPath = fingerPrintPdfPath
            )
        )
    }

    override fun isValidInput(): Boolean {
        var isValid = true
        if (results.size < 10) {
            isValid = false
        } else {
            results.forEach {
                if (it.isEmpty()) {
                    isValid = false
                }
            }
        }

        return isValid
    }

    private fun startFingerprintCapture() {
        activity?.let {
            setUpLicenseData(it)
        }
    }


    private fun setUpLicenseData(activity: Activity) {
        showLoading()
        val preLicense = getPreLicense(activity)
        getLicenseFromPreLicense(preLicense)
    }

    private fun getLicenseFromPreLicense(
        preLicense: PreLicense
    ) {
        val task = URLIAsyncTask<PreLicense, LicenseResponse>(
            url = licenseURL,
            method = URLMethod.POST,
            body = preLicense,
            headers = mapOf("Content-Type" to "application/json"),
            responseType = object : TypeToken<LicenseResponse>() {}.type)


        val executable =
            fragmentStepConfiguration.mobileWorkflowServices.serviceContainer
                .performSingle<URLIAsyncTask<PreLicense, LicenseResponse>, LicenseResponse>(
                    task
                )

        executable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { licenseResponse ->
                    context?.let { safeContext ->
                        val intent = MobileWorkflowAAActivity.getIntent(safeContext, fingerId, mode, licenseResponse.license)
                        startForResult.launch(intent)
                    }
                }, {
                    showError()
                })
    }

    private fun removeLicense(context: Context) = ActivityHandler.voidLicense(context)

    private fun getPreLicense(activity: Activity): PreLicense {
        removeLicense(activity.baseContext)
        val preLicenseData = ActivityHandler.getPreLicenseData(activity)
        return PreLicense(Base64.encodeToString(preLicenseData.data, Base64.NO_WRAP))
    }

    private fun createPDFFromFingerList(fingerImagePathList: MutableList<String>) {
        val pdfFile = fragmentStepConfiguration.mobileWorkflowServices.fileHandler.createFile(outputDirectory.absolutePath, "pdf")

        Completable
            .fromAction {
                var bitmap: Bitmap
                val document = PdfDocument()
                val height = 1500
                val width = 1050
                var reqH: Int
                var reqW: Int
                reqW = width

                for (fingerImagePath in fingerImagePathList) {
                    bitmap = BitmapFactory.decodeFile(fingerImagePath)
                    reqH = width * bitmap.height / bitmap.width
                    if (reqH < height) {
                        bitmap = Bitmap.createScaledBitmap(bitmap, reqW, reqH, true)
                    } else {
                        reqH = height
                        reqW = height * bitmap.width / bitmap.height
                        bitmap = Bitmap.createScaledBitmap(bitmap, reqW, reqH, true)
                    }
                    val pageInfo = PdfDocument.PageInfo.Builder(reqW, reqH, 1).create()
                    val page = document.startPage(pageInfo)
                    val canvas: Canvas = page.canvas
                    canvas.drawBitmap(bitmap, 0f, 0f, null)
                    document.finishPage(page)
                }

                val fos: OutputStream
                try {
                    fos = fragmentStepConfiguration.mobileWorkflowServices.fileHandler.createFileOutputStream(pdfFile.absolutePath)
                    document.writeTo(fos)
                    document.close()
                    fos.close()
                    fingerPrintPdfPath = pdfFile.absolutePath
                } catch (e: IOException) {

                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                footer.canContinue = isValidInput()
                footer.onContinue()
            }
    }


    private fun processResults(fingerInfoArrayList: ArrayList<String>) {
        try {
            results.addAll(fingerInfoArrayList.toMutableList())
            createPDFFromFingerList(results)
        } catch (e: Exception) {
            showError()
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun showLoading() {
        context?.let {
            content.clear()
            content.add(LoadingView(requireContext(), tintColor = tintColor))
        }
    }

    private fun showError() {
        context?.let {
            content.clear()
            val errorView = ErrorView(context = requireContext(), tintColor = tintColor).apply {
                setRetryFun { startFingerprintCapture() }
            }
            content.add(errorView)
        }
    }

    override fun setupViews() {
        super.setupViews()

        context?.let { safeContext ->
            footer.visibility = View.GONE
            aAPart = UIAAPart(safeContext)
            aAPart.style(surveyTheme)
            content.add(aAPart)
        }

        initFingerCapture()
    }

    private fun initFingerCapture() {
        val permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    startFingerprintCapture()
                }
            }
        if (!hasCameraPermission()) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            startFingerprintCapture()
        }
    }
}