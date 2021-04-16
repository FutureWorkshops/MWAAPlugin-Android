/*
 * Copyright (c) 2021 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.aa.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.futureworkshops.mobileworkflow.SurveyTheme
import com.futureworkshops.mobileworkflow.backend.views.main_parts.StyleablePart
import com.futureworkshops.mobileworkflow.plugin.aa.R

class UIAAPart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleRes), StyleablePart {

    val view: View = View.inflate(context, R.layout.aa_plugin_step, this)

    init {
        createViews()
    }

    private fun createViews() {

    }

    override fun style(surveyTheme: SurveyTheme) {
    }



}