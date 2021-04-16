package com.futureworkshops.mobileworkflow.plugin.aa.view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.futureworkshops.mobileworkflow.plugin.aa.R

internal class LoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0,
    tintColor: Int?
) : FrameLayout(context, attrs, defStyleRes) {

    private val view: View = View.inflate(context, R.layout.loading_view, this)

    init {
        tintColor?.apply {
            val progressBar = view.findViewById<ProgressBar>(R.id.loadingView)
            progressBar.indeterminateTintList =
                ColorStateList.valueOf(this)
        }
    }
}