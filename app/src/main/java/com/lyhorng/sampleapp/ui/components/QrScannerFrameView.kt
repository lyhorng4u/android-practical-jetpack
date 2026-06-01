package com.lyhorng.sampleapp.ui.components

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import kotlin.math.min

class QrScannerFrameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#B0000000")   // Darker overlay like ABA
        style = Paint.Style.FILL
    }

    private val clearPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private val cornerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        strokeWidth = 5.5f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#44FFFFFF")
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }

    private val scanningLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        strokeWidth = 3.5f
        style = Paint.Style.STROKE
    }

    private val scanningGlowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#44FFFFFF")
        style = Paint.Style.FILL
    }

    private val frameRect = RectF()
    private var scanningLineY = 0f
    private var animator: ValueAnimator? = null

    init {
        startScanningAnimation()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()

        val saveCount = canvas.saveLayer(0f, 0f, w, h, null)

        // Dark mask
        canvas.drawRect(0f, 0f, w, h, maskPaint)

        // Frame calculation (closer to ABA positioning)
        val frameSize = min(w * 0.75f, 340f.dp())
        val left = (w - frameSize) / 2f
        val top = h * 0.23f
        val right = left + frameSize
        val bottom = top + frameSize

        frameRect.set(left, top, right, bottom)

        // Clear the center
        canvas.drawRoundRect(frameRect, 32f.dp(), 32f.dp(), clearPaint)

        canvas.restoreToCount(saveCount)

        // Subtle inner border
        canvas.drawRoundRect(frameRect, 32f.dp(), 32f.dp(), borderPaint)

        // Draw white corners
        drawAbaStyleCorners(canvas)

        // Scanning line
        drawScanningLine(canvas)
    }

    private fun drawAbaStyleCorners(canvas: Canvas) {
        val l = frameRect.left
        val t = frameRect.top
        val r = frameRect.right
        val b = frameRect.bottom
        val len = 62f.dp()
        val radius = 32f.dp()

        cornerPaint.strokeWidth = 5.5f

        // Top Left
        canvas.drawLine(l, t + radius, l, t + len, cornerPaint)
        canvas.drawLine(l + radius, t, l + len, t, cornerPaint)

        // Top Right
        canvas.drawLine(r - len, t, r - radius, t, cornerPaint)
        canvas.drawLine(r, t + radius, r, t + len, cornerPaint)

        // Bottom Left
        canvas.drawLine(l, b - len, l, b - radius, cornerPaint)
        canvas.drawLine(l + radius, b, l + len, b, cornerPaint)

        // Bottom Right
        canvas.drawLine(r - len, b, r - radius, b, cornerPaint)
        canvas.drawLine(r, b - len, r, b - radius, cornerPaint)
    }

    private fun drawScanningLine(canvas: Canvas) {
        if (scanningLineY < frameRect.top || scanningLineY > frameRect.bottom) return

        val left = frameRect.left + 14.dp()
        val right = frameRect.right - 14.dp()

        // Soft glow
        canvas.drawRect(left, scanningLineY - 14.dp(), right, scanningLineY + 14.dp(), scanningGlowPaint)

        // Main line
        canvas.drawLine(left, scanningLineY, right, scanningLineY, scanningLinePaint)
    }

    private fun startScanningAnimation() {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 2200
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()

            addUpdateListener {
                val progress = it.animatedValue as Float
                scanningLineY = frameRect.top + frameRect.height() * progress
                invalidate()
            }
            start()
        }
    }

    fun getFrameRect(): RectF = frameRect

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }

    private fun Int.dp(): Float = this * resources.displayMetrics.density
    private fun Float.dp(): Float = this * resources.displayMetrics.density
}