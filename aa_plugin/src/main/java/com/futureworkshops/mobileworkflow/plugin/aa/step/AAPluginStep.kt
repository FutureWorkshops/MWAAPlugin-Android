/*
 * Copyright (c) 2021 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.aa.step

import android.os.Parcelable
import com.futureworkshops.mobileworkflow.model.step.PluginStep
import kotlinx.parcelize.Parcelize

@Parcelize
data class AAPluginStep(
    override val type: String,
    override val title: String,
    val optional: Boolean,
    val licenseURL: String = "",
    val mode: String = ""
) : PluginStep(), Parcelable