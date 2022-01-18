/*
 * Copyright (c) 2021 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.aa.step

import android.os.Parcelable
import com.futureworkshops.mobileworkflow.model.configuration.NavigationItem
import com.futureworkshops.mobileworkflow.model.configuration.StepLink
import com.futureworkshops.mobileworkflow.model.step.PluginStep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AAPluginStep(
    @SerializedName("id") override val id: String,
    @SerializedName("type") override val type: String,
    @SerializedName("title") override val title: String,
    @SerializedName("links") override val links: List<StepLink>,
    @SerializedName("navigationItems") override val navigationItems: List<NavigationItem>,
    @SerializedName("optional") val optional: Boolean,
    @SerializedName("licenseURL") val licenseURL: String = "",
    @SerializedName("mode") val mode: String = ""
) : PluginStep(), Parcelable