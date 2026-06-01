package com.lyhorng.sampleapp.ui.components

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class QrScannerFrameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#99000000")
        style = Paint.Style.FILL
    }

    private val clearPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private val cornerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        strokeWidth = 8f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#33FFFFFF")
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 42f
        textAlign = Paint.Align.CENTER
    }

    private var frameRect = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val saveLayer = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), maskPaint)

        val frameSize = min(width * 0.72f, 320.dp())
        val left = (width - frameSize) / 2f
        val top = height * 0.28f
        val right = left + frameSize
        val bottom = top + frameSize

        frameRect.set(left, top, right, bottom)

        canvas.drawRoundRect(frameRect, 24.dp(), 24.dp(), clearPaint)

        canvas.restoreToCount(saveLayer)

        drawFrame(canvas)
        drawText(canvas)
    }

    private fun drawFrame(canvas: Canvas) {
        val cornerLength = 52.dp()
        val radius = 24.dp()

        canvas.drawRoundRect(frameRect, radius, radius, borderPaint)

        val l = frameRect.left
        val t = frameRect.top
        val r = frameRect.right
        val b = frameRect.bottom

        // top-left
        canvas.drawLine(l, t + cornerLength, l, t + radius, cornerPaint)
        canvas.drawLine(l + radius, t, l + cornerLength, t, cornerPaint)

        // top-right
        canvas.drawLine(r - cornerLength, t, r - radius, t, cornerPaint)
        canvas.drawLine(r, t + radius, r, t + cornerLength, cornerPaint)

        // bottom-left
        canvas.drawLine(l, b - cornerLength, l, b - radius, cornerPaint)
        canvas.drawLine(l + radius, b, l + cornerLength, b, cornerPaint)

        // bottom-right
        canvas.drawLine(r - cornerLength, b, r - radius, b, cornerPaint)
        canvas.drawLine(r, b - cornerLength, r, b - radius, cornerPaint)
    }

    private fun drawText(canvas: Canvas) {
        canvas.drawText(
            "Place QR code inside the frame",
            width / 2f,
            frameRect.bottom + 48.dp(),
            textPaint
        )
    }

    fun getFrameRect(): RectF {
        return frameRect
    }

    private fun Int.dp(): Float {
        return this * resources.displayMetrics.density
    }
}