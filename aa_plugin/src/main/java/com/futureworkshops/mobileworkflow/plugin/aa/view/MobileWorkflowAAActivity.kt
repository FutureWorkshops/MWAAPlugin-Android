/*
 * Copyright (c) 2021 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.aa.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import com.aatechintl.aaprintscanner.ActivityHandler
import com.futureworkshops.mobileworkflow.extensions.decodeBase64ToByteArray
import java.util.*

open class MobileWorkflowAAActivity : AppCompatActivity() {

    private lateinit var handler: ActivityHandler
    private var initialised = false
    private var subjectId: String? = ""
    private val provider: FingerPrintCaptureProvider = FingerPrintCaptureProvider()
    private var fingerId: Int = 0
    private var mode: String = ""
    private var license: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fingerId = intent?.getIntExtra(FINER_ID_EXTRA, 0) ?: 0
        mode = intent?.getStringExtra(MODE_EXTRA) ?: ""
        license = intent?.getStringExtra(LICENSE_EXTRA) ?: ""


        keepScreenAsOpen()
        startFingerCapture(license)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onPause() {
        if (initialised) {
            handler.onPause()
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (initialised) {
            handler.onResume()
        }
    }

    override fun onDestroy() {
        removeLicense(this)
        if (initialised) {
            handler.onDestroy()
        }

        super.onDestroy()
    }

    private fun keepScreenAsOpen() {
        // keep screen on on that activity
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun startFingerCapture(
        license: String
    ) {
        removeLicense(baseContext)
        val licenseData = ActivityHandler.LicenseData()
        licenseData.data = license.decodeBase64ToByteArray()
        ActivityHandler.setLicenseData(this, licenseData)
        setUpInit(this)
        startCapture()
    }

    private fun setUpInit(activity: Activity) {
        val callback = object : ActivityHandler.ExternalCallback {
            override fun onCaptureProcessed(id: String) {
                processResults(activity, id)
            }

            override fun onCaptureSaved(id: String) {
                processResults(activity, id)
            }

            override fun onCaptureCanceled() {
                //showError()
            }

            override fun getRecaptureFingers(results: Array<ActivityHandler.CaptureResult>): BooleanArray? {
                return getRecaptures(results)
            }
        }

        val params = getConfiguredParams()
        provider.populateTextStrings(
            context = activity,
            textStrings = params.textStrings,
            isRightToLeft = isRightToLeftEnabled()
        )
        subjectId = provider.generateSubjectId()
        handler = ActivityHandler(activity, callback, params)
    }

    private fun getConfiguredParams(): ActivityHandler.Params {
        val params = ActivityHandler.Params()
        params.externalRecaptureOn = false
        params.overlayRotationOn = false
        params.cancelConfirmOn = false
        params.displayText = ""
        params.devModeOn = false
        return params
    }

    private fun getRecaptures(results: Array<ActivityHandler.CaptureResult>?): BooleanArray? {
        if (results == null) return null
        val recaptureFingers = BooleanArray(results.size)
        var fingerDuplicated = false
        results.forEachIndexed { index, result ->
            //Errors in capture process; recapture
            recaptureFingers[index] =
                (result.image == null || result.image.image == null || result.mntCount <= 0)
            // Check if there are any finger duplicated
            if (result.fingerError == ActivityHandler.CaptureResult.FingerErrorType.SequentialError) {
                fingerDuplicated = true
            }
        }

        if (fingerDuplicated) {
            //extCallback.onCaptureError(FingerDuplicatedException())
        }
        return recaptureFingers
    }

    private fun isRightToLeftEnabled(): Boolean =
        TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL


    private fun processResults(context: Context, id: String) {
        try {
            onFingerCaptureSuccess(getCaptureResults(context, id))
        } catch (e: Exception) {
            //showError()
        }
    }

    private fun getCaptureResults(context: Context, id: String): List<FingerInfo> {
        return handler.getCaptureResults(id).map {
            FingerPrintMapper.fromCaptureResultToFingerResult(context, it, provider)
        }
    }

    private fun removeLicense(context: Context) = ActivityHandler.voidLicense(context)

    private fun startCapture() {
        initialised = true
        val profile = if (mode == "singleFinger") {
            initCaptureProfile(ScanType.ONE_TIMES_10_FINGERS)
        } else {
            initCaptureProfile(ScanType.ONE_FOUR_FINGERS)
        }
        try {
            handler.reset()
            handler.startCapture(profile)
        } catch (e: Exception) {
        }
    }

    private fun onFingerCaptureSuccess(fingerInfoList: List<FingerInfo>) {

        val intent = Intent()
        AAFileInteractor.shared?.getImagePathsFromFingerInfo(fingerInfoList)?.let {
            intent.putStringArrayListExtra(FINGER_INFO_LIST_EXTRA, ArrayList(it))
        }
        setResult(RC_AA_FINGER_CAPTURE, intent)

        finish()
    }

    private fun initCaptureProfile(
        scanType: ScanType,
        fingerId: Int = 0
    ): ActivityHandler.CaptureProfile {
        return ActivityHandler.CaptureProfile().apply {
            captures = when (scanType) {
                ScanType.SINGLE_FINGER -> getSingleCapture(fingerId)
                ScanType.ONE_FOUR_FINGERS -> get1414Captures()
                ScanType.ONE_TIMES_10_FINGERS -> get1times10Captures()
            }
            subjectId = this@MobileWorkflowAAActivity.subjectId
            saveWidth = REQUIRED_WIDTH
            saveHeight = REQUIRED_HEIGHT
            saveType = ActivityHandler.CaptureImage.Type.WSQ
            saveWsqMaxSize = REQUIRED_MAX_SIZE
        }
    }

    private fun getSingleCapture(fingerId: Int): Array<ActivityHandler.CaptureProfile.Capture> {
        return arrayOf(ActivityHandler.CaptureProfile.Capture().apply {
            fingers = arrayOf(provider.getFingerTypeFromOrdinalId(fingerId))
            captureType = ActivityHandler.CaptureProfile.CaptureType.OneFinger
        })
    }

    private fun get1414Captures(): Array<ActivityHandler.CaptureProfile.Capture> {
        return Array(ScanType.ONE_FOUR_FINGERS.captures) { index ->
            ActivityHandler.CaptureProfile.Capture().apply {
                when (index) {
                    0 -> {
                        fingers = arrayOf(ActivityHandler.CaptureProfile.FingerType.LT)
                        captureType = ActivityHandler.CaptureProfile.CaptureType.OneFinger
                    }
                    1 -> {
                        fingers = arrayOf(
                            ActivityHandler.CaptureProfile.FingerType.LI,
                            ActivityHandler.CaptureProfile.FingerType.LM,
                            ActivityHandler.CaptureProfile.FingerType.LR,
                            ActivityHandler.CaptureProfile.FingerType.LL
                        )
                        captureType = ActivityHandler.CaptureProfile.CaptureType.FourFinger
                    }
                    2 -> {
                        fingers = arrayOf(ActivityHandler.CaptureProfile.FingerType.RT)
                        captureType = ActivityHandler.CaptureProfile.CaptureType.OneFinger
                    }
                    3 -> {
                        fingers = arrayOf(
                            ActivityHandler.CaptureProfile.FingerType.RI,
                            ActivityHandler.CaptureProfile.FingerType.RM,
                            ActivityHandler.CaptureProfile.FingerType.RR,
                            ActivityHandler.CaptureProfile.FingerType.RL
                        )
                        captureType = ActivityHandler.CaptureProfile.CaptureType.FourFinger
                    }
                }
            }
        }
    }

    private fun get1times10Captures(): Array<ActivityHandler.CaptureProfile.Capture> {
        return Array(ScanType.ONE_TIMES_10_FINGERS.captures) { index ->
            ActivityHandler.CaptureProfile.Capture().apply {
                when (index) {
                    0 -> {
                        fingers = arrayOf(ActivityHandler.CaptureProfile.FingerType.LT)
                        captureType = ActivityHandler.CaptureProfile.CaptureType.OneFinger
                    }
                    1 -> {
                        fingers = arrayOf(ActivityHandler.CaptureProfile.FingerType.LI)
                        captureType = ActivityHandler.CaptureProfile.CaptureType.OneFinger
                    }
                    2 -> {
                        fingers = arrayOf(ActivityHandler.CaptureProfile.FingerType.LM)
                        captureType = ActivityHandler.CaptureProfile.CaptureType.OneFinger
                    }
                    3 -> {
                        fingers = arrayOf(ActivityHandler.CaptureProfile.FingerType.LR)
                        captureType = ActivityHandler.CaptureProfile.CaptureType.OneFinger
                    }
                    4 -> {
                        fingers = arrayOf(ActivityHandler.CaptureProfile.FingerType.LL)
                        captureType = ActivityHandler.CaptureProfile.CaptureType.OneFinger
                    }
                    5 -> {
                        fingers = arrayOf(ActivityHandler.CaptureProfile.FingerType.RT)
                        captureType = ActivityHandler.CaptureProfile.CaptureType.OneFinger
                    }
                    6 -> {
                        fingers = arrayOf(ActivityHandler.CaptureProfile.FingerType.RI)
                        captureType = ActivityHandler.CaptureProfile.CaptureType.OneFinger
                    }
                    7 -> {
                        fingers = arrayOf(ActivityHandler.CaptureProfile.FingerType.RM)
                        captureType = ActivityHandler.CaptureProfile.CaptureType.OneFinger
                    }
                    8 -> {
                        fingers = arrayOf(ActivityHandler.CaptureProfile.FingerType.RR)
                        captureType = ActivityHandler.CaptureProfile.CaptureType.OneFinger
                    }
                    9 -> {
                        fingers = arrayOf(ActivityHandler.CaptureProfile.FingerType.RL)
                        captureType = ActivityHandler.CaptureProfile.CaptureType.OneFinger
                    }
                }
            }
        }
    }

    enum class ScanType(val captures: Int) {
        SINGLE_FINGER(1),
        ONE_FOUR_FINGERS(4),
        ONE_TIMES_10_FINGERS(10)
    }

    companion object {

        private const val REQUIRED_WIDTH = 400
        private const val REQUIRED_HEIGHT = 500
        private const val REQUIRED_MAX_SIZE = 40000

        const val RC_AA_FINGER_CAPTURE = 10004

        private const val FINER_ID_EXTRA = "FINER_ID_EXTRA"
        private const val MODE_EXTRA = "MODE_EXTRA"
        private const val LICENSE_EXTRA = "LICENSE_EXTRA"

        const val FINGER_INFO_LIST_EXTRA = "FINGER_INFO_LIST_EXTRA"



        fun getIntent(context: Context, fingerId: Int, mode: String, license: String): Intent {
            val intent = Intent(context, MobileWorkflowAAActivity::class.java)
            intent.putExtra(FINER_ID_EXTRA, fingerId)
            intent.putExtra(MODE_EXTRA, mode)
            intent.putExtra(LICENSE_EXTRA, license)
            return intent
        }
    }
}