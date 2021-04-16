/*
 * Copyright (c) 2021 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.aa.model

import com.futureworkshops.mobileworkflow.model.result.FileAnswerResult
import com.futureworkshops.mobileworkflow.model.result.FileInformation
import com.futureworkshops.mobileworkflow.model.result.PropertyResultComponent
import com.futureworkshops.mobileworkflow.model.result.ResultComponent
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AAAnswer (
    private val fingerPrintPdfPath: String
) : FileAnswerResult {
    override fun getResultValue(): String? = fingerPrintPdfPath

    override fun getComponent(component: String): ResultComponent? {
        return when (component) {
            "fingerprints" -> PropertyResultComponent("file://$fingerPrintPdfPath")
            else -> null
        }
    }

    override val files: List<FileInformation>
        get() = listOf(
            FileInformation("fingerprints", fingerPrintPdfPath, "application/pdf")
        )

    override fun getUploadRepresentation(): Any? = this
}