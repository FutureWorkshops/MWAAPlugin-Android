/*
 * Copyright (c) 2021 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.aa

import com.futureworkshops.mobileworkflow.domain.DeserializeStep

import com.futureworkshops.mobileworkflow.plugin.aa.step.AAPluginStep
import com.futureworkshops.mobileworkflow.plugin.aa.view.UIAAPluginStep
import com.futureworkshops.mobileworkflow.steps.Step

internal class AAPlugin: DeserializeStep<AAPluginStep>(
    type = "com.aatechintl.FingerprintScanner",
    classT = AAPluginStep::class.java) {

    override fun createUIStep(step: AAPluginStep): Step =
        UIAAPluginStep(
            title = step.title,
                id = step.id,
            licenseURL = step.licenseURL,
            mode = step.mode
        )
}