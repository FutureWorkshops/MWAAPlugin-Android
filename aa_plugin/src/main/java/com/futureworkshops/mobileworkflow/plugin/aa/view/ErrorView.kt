package com.futureworkshops.mobileworkflow.plugin.aa.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import com.futureworkshops.mobileworkflow.plugin.aa.R

internal class ErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0,
    tintColor: Int?
) : FrameLayout(context, attrs, defStyleRes) {

    private val view: View = View.inflate(context, R.layout.error_view, this)
    private val retryButton: Button = view.findViewById(R.id.retryButton)

    init {
        tintColor?.apply { retryButton.setBackgroundColor(tintColor) }
    }

    fun setRetryFun(function:() -> Unit) {
        retryButton.setOnClickListener { function() }
    }
}