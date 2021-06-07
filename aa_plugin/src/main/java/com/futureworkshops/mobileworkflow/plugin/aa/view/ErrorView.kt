package com.futureworkshops.mobileworkflow.plugin.aa.view

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.FrameLayout
import com.futureworkshops.mobileworkflow.plugin.aa.databinding.ErrorViewBinding
import com.futureworkshops.mobileworkflow.ui.part.BindingPart

internal class ErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0,
    tintColor: Int?
) : FrameLayout(context, attrs, defStyleRes), BindingPart<ErrorViewBinding> {

    constructor(context: Context, attrs: AttributeSet? = null, defStyleRes: Int = 0):
            this(context, attrs, defStyleRes, null)

    override val view = ErrorViewBinding.inflate(getLayoutInflater(context), this, true)
    private val retryButton: Button
        get() = view.retryButton

    init {
        tintColor?.apply { retryButton.setBackgroundColor(tintColor) }
    }

    fun setRetryFun(function:() -> Unit) {
        retryButton.setOnClickListener { function() }
    }
}