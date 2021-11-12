/*
 * Copyright (c) 2021 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.aa.view

import com.futureworkshops.mobileworkflow.StepIdentifier
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStep
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStepConfiguration
import com.futureworkshops.mobileworkflow.model.WorkflowServiceResponse
import com.futureworkshops.mobileworkflow.model.result.StepResult
import com.futureworkshops.mobileworkflow.services.MobileWorkflowServices
import com.futureworkshops.mobileworkflow.steps.DataTitle
import com.futureworkshops.mobileworkflow.steps.Step

internal data class UIAAPluginStep(
    override val title: String,
    override var isOptional: Boolean = false,
    override val id: StepIdentifier = StepIdentifier(),
    override val uuid: String,
    private val nextButtonText: String = "Next",
    private val licenseURL: String = "",
    private val mode: String = ""
) : Step, DataTitle {

    override fun createView(
        stepResult: StepResult?,
        mobileWorkflowServices: MobileWorkflowServices,
        workflowServiceResponse: WorkflowServiceResponse,
        selectedWorkflowId: String
    ): FragmentStep {
        return UIAAPluginView(
            FragmentStepConfiguration(
            id = id,
            isOptional = isOptional,
            title = mobileWorkflowServices.localizationService.getTranslation(title),
            text = null,
            nextButtonText = mobileWorkflowServices.localizationService.getTranslation(nextButtonText),
            mobileWorkflowServices = mobileWorkflowServices),
            licenseURL = mobileWorkflowServices.urlTaskBuilder.urlHelper.resolveUrl(
                workflowServiceResponse.server, licenseURL, workflowServiceResponse.session)?: "",
            mode = mode,
            tintColor = workflowServiceResponse.tintColor
        )
    }

    override fun copyWithNewTitle(title: String): Step = this
}