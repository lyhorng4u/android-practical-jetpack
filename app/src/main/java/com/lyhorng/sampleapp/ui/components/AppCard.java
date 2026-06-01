package com.lyhorng.sampleapp.ui.components;

import android.content.Context;
import android.util.AttributeSet;

import kotlin.jvm.JvmOverloads;

class AppCard @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    enum class CardElevation {
        LOW, MEDIUM, HIGH
    }


}
