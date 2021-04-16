package com.futureworkshops.mobileworkflow.plugin.aa.view

import android.content.Context
import android.util.Base64
import com.aatechintl.aaprintscanner.ActivityHandler


object FingerPrintMapper {

    /* From presentation model to domain model */

    fun fromCaptureResultToFingerResult(
        context: Context,
        captureResult: ActivityHandler.CaptureResult,
        provider: FingerPrintCaptureProvider
    ): FingerInfo {
        val positionName = captureResult.fingerType.toString()
        val wsqImage = captureResult.image.image
        val bitmapImage = captureResult.displayImage.image
        val encodedBase64ImgWSQ = wsqImage?.let { Base64.encodeToString(it, Base64.NO_WRAP) } ?: ""
        val encodedBase64ImgPng = bitmapImage?.let { Base64.encodeToString(it, Base64.NO_WRAP) } ?: ""

        //checkQualityRequirentments(captureResult)

        return FingerInfo(
            id = captureResult.fingerType.ordinal,
            position = positionName.replace("\\s".toRegex(), ""),
            positionName = provider.getFingerPositionNameFromFingerType(context, captureResult.fingerType),
            order = provider.getOrderFromFingerType(captureResult.fingerType),
            rawWSQData = encodedBase64ImgWSQ,
            rawPNGData = encodedBase64ImgPng,
            imageByteArray = bitmapImage,
            width = captureResult.image.width,
            height = captureResult.image.height,
            minutiae = captureResult.mntCount,
            quality = captureResult.aaQuality
        )
    }

    /*private fun checkQualityRequirentments(captureResult: ActivityHandler.CaptureResult): Unit =
        when {
            captureResult.mntCount <= 0 -> {
                throw FingerMinutiaeCountException()
            }
            captureResult.aaQuality <= 0 -> {
                throw FingerPhotoPoorQualityException()
            }
            else -> {
            }
        }
*/
}