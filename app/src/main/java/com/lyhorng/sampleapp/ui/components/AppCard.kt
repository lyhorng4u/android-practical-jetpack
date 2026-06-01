package com.lyhorng.sampleapp.ui.components

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.lyhorng.sampleapp.R

class AppCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    enum class CardElevation {
        NONE, LOW, MEDIUM, HIGH
    }

    enum class CardType {
        FILLED, OUTLINED, ELEVATED
    }

    private var cardType: CardType = CardType.FILLED
    private var cardElevation: CardElevation = CardElevation.MEDIUM
    private var cornerRadius: Float = 0f

    init {
        // Set default background to transparent to avoid flickering
        setBackgroundColor(Color.TRANSPARENT)

        context.obtainStyledAttributes(attrs, R.styleable.AppCard).apply {
            try {
                cardType = when (getInt(R.styleable.AppCard_cardType, 0)) {
                    1 -> CardType.OUTLINED
                    2 -> CardType.ELEVATED
                    else -> CardType.FILLED
                }

                cardElevation = when (getInt(R.styleable.AppCard_cardElevation, 1)) {
                    0 -> CardElevation.NONE
                    2 -> CardElevation.HIGH
                    else -> CardElevation.MEDIUM
                }

                cornerRadius = getDimension(R.styleable.AppCard_cardCornerRadius, dp(12).toFloat())
            } finally {
                recycle()
            }
        }

        applyCardStyle()
    }

    private fun applyCardStyle() {
        val background = GradientDrawable()
        background.shape = GradientDrawable.RECTANGLE
        background.cornerRadius = cornerRadius

        when (cardType) {
            CardType.FILLED -> {
                background.setColor(getColor(R.color.surface))
                background.setStroke(0, Color.TRANSPARENT)
                setBackgroundDrawable(background)
                cardElevation = CardElevation.NONE
                applyElevation()
            }

            CardType.OUTLINED -> {
                background.setColor(Color.TRANSPARENT)
                background.setStroke(dp(1), getColor(R.color.border))
                setBackgroundDrawable(background)
                cardElevation = CardElevation.NONE
                applyElevation()
            }

            CardType.ELEVATED -> {
                background.setColor(getColor(R.color.surface))
                background.setStroke(0, Color.TRANSPARENT)
                setBackgroundDrawable(background)
                applyElevation()
            }
        }
    }

    private fun applyElevation() {
        val elevationPx = when (cardElevation) {
            CardElevation.NONE -> 0f
            CardElevation.LOW -> dp(2).toFloat()
            CardElevation.MEDIUM -> dp(4).toFloat()
            CardElevation.HIGH -> dp(8).toFloat()
        }

        elevation = elevationPx
    }

    fun setCardType(type: CardType) {
        cardType = type
        applyCardStyle()
    }

    fun setElevation(level: CardElevation) {
        cardElevation = level
        applyElevation()
    }

    fun setCornerRadius(radius: Float) {
        cornerRadius = radius
        applyCardStyle()
    }

    private fun getColor(colorRes: Int): Int {
        return ContextCompat.getColor(context, colorRes)
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }
}