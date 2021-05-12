package com.futureworkshops.mobileworkflow.plugin.aa.view

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FingerInfo (
    val id: Int,
    val type: String = TYPE_FINGERPRINT,
    val position: String = "",
    val positionName: String = "",
    val order: Int,
    val rawWSQData: String = "",
    val rawPNGData: String = "",
    val imageByteArray: ByteArray,
    val width: Int = 0,
    val height: Int = 0,
    val minutiae: Int = 0,
    val quality: Int = 0
): Parcelable {

    companion object {
        const val TYPE_FINGERPRINT = "Fingerprint"
    }
}