package com.lyhorng.sampleapp.ui.components

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.lyhorng.sampleapp.R

class AppButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatButton(context, attrs) {

    enum class Variant {
        PRIMARY, SECONDARY, OUTLINE, DANGER, GHOST
    }

    enum class Size {
        SMALL, MEDIUM, LARGE
    }

    enum class Rounded {
        SMALL, MEDIUM, FULL
    }

    private var originalText: String = ""

    private var variant: Variant = Variant.PRIMARY
    private var size: Size = Size.MEDIUM
    private var rounded: Rounded = Rounded.MEDIUM
    private var loading: Boolean = false

    init {
        isAllCaps = false
        gravity = Gravity.CENTER
        includeFontPadding = false

        context.obtainStyledAttributes(attrs, R.styleable.AppButton).apply {

            originalText = getString(R.styleable.AppButton_buttonText) ?: text.toString()
            text = originalText

            variant = when (getInt(R.styleable.AppButton_buttonVariant, 0)) {
                1 -> Variant.SECONDARY
                2 -> Variant.OUTLINE
                3 -> Variant.DANGER
                4 -> Variant.GHOST
                else -> Variant.PRIMARY
            }

            size = when (getInt(R.styleable.AppButton_buttonSize, 1)) {
                0 -> Size.SMALL
                2 -> Size.LARGE
                else -> Size.MEDIUM
            }

            rounded = when (getInt(R.styleable.AppButton_buttonRounded, 1)) {
                0 -> Rounded.SMALL
                2 -> Rounded.FULL
                else -> Rounded.MEDIUM
            }

            val leftIcon = getDrawable(R.styleable.AppButton_leftIcon)
            val rightIcon = getDrawable(R.styleable.AppButton_rightIcon)

            setCompoundDrawablesWithIntrinsicBounds(leftIcon, null, rightIcon, null)
            compoundDrawablePadding = dp(8)

            loading = getBoolean(R.styleable.AppButton_isLoading, false)

            recycle()
        }

        applySize()
        applyStyle()
        setLoading(loading)
    }

    private fun applySize() {
        when (size) {
            Size.SMALL -> {
                minHeight = dp(40)
                height = dp(40)
                textSize = 13f
                setPadding(dp(14), 0, dp(14), 0)
            }

            Size.MEDIUM -> {
                minHeight = dp(48)
                height = dp(48)
                textSize = 15f
                setPadding(dp(18), 0, dp(18), 0)
            }

            Size.LARGE -> {
                minHeight = dp(56)
                height = dp(56)
                textSize = 16f
                setPadding(dp(22), 0, dp(22), 0)
            }
        }
    }

    private fun applyStyle() {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.cornerRadius = getCornerRadius()

        when (variant) {
            Variant.PRIMARY -> {
                drawable.setColor(getColor(R.color.primary))
                setTextColor(Color.WHITE)
            }

            Variant.SECONDARY -> {
                drawable.setColor(getColor(R.color.input_background))
                setTextColor(getColor(R.color.text_primary))
            }

            Variant.OUTLINE -> {
                drawable.setColor(Color.TRANSPARENT)
                drawable.setStroke(dp(1), getColor(R.color.primary))
                setTextColor(getColor(R.color.primary))
            }

            Variant.DANGER -> {
                drawable.setColor(getColor(R.color.error))
                setTextColor(Color.WHITE)
            }

            Variant.GHOST -> {
                drawable.setColor(Color.TRANSPARENT)
                setTextColor(getColor(R.color.primary))
            }
        }

        background = drawable
    }

    private fun getCornerRadius(): Float {
        return when (rounded) {
            Rounded.SMALL -> dp(8).toFloat()
            Rounded.MEDIUM -> dp(14).toFloat()
            Rounded.FULL -> dp(100).toFloat()
        }
    }

    fun setLoading(isLoading: Boolean) {
        loading = isLoading
        isEnabled = !isLoading
        text = if (isLoading) "Loading..." else originalText
    }

    fun setButtonText(value: String) {
        originalText = value
        text = value
    }

    fun setVariant(value: Variant) {
        variant = value
        applyStyle()
    }

    fun setSize(value: Size) {
        size = value
        applySize()
    }

    fun setRounded(value: Rounded) {
        rounded = value
        applyStyle()
    }

    private fun getColor(colorRes: Int): Int {
        return ContextCompat.getColor(context, colorRes)
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }
}