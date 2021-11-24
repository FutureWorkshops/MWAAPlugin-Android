/*
 * Copyright (c) 2021 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.aa.view


import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStep
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStepConfiguration
import com.futureworkshops.mobileworkflow.model.WorkflowServiceResponse
import com.futureworkshops.mobileworkflow.model.result.AnswerResult
import com.futureworkshops.mobileworkflow.services.ServiceBox
import com.futureworkshops.mobileworkflow.steps.Step

internal data class UIAAPluginStep(
    val title: String,
    override var isOptional: Boolean = false,
    override val id: String,

    private val nextButtonText: String = "Next",
    private val licenseURL: String = "",
    private val mode: String = ""
) : Step {

    override fun createView(
        stepResult: AnswerResult?,
        services: ServiceBox,
        workflowServiceResponse: WorkflowServiceResponse,
        selectedWorkflowId: String
    ): FragmentStep {
        return UIAAPluginView(
            FragmentStepConfiguration(
            isOptional = isOptional,
            title = services.localizationService.getTranslation(title),
            text = null,
            nextButtonText = services.localizationService.getTranslation(nextButtonText),
            services = services),
            licenseURL = services.urlTaskBuilder.urlHelper.resolveUrl(
                workflowServiceResponse.server, licenseURL, workflowServiceResponse.session)?: "",
            mode = mode,
            tintColor = workflowServiceResponse.tintColor
        )
    }
}