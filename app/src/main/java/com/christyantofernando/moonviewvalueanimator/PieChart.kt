package com.christyantofernando.moonviewvalueanimator

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import kotlin.math.min

class PieChart(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    companion object {
        private const val DEFAULT_MOON_COLOR = Color.YELLOW
        private const val DEFAULT_MOON_STROKE_COLOR = Color.DKGRAY

        private const val DESIRED_MOON_DIAMETER = 80
    }

    // Colors
    private var moonBackgroundColor = DEFAULT_MOON_COLOR
        set(@ColorInt color) {
            field = color
            invalidate()
        }
    private var moonStrokeColor = DEFAULT_MOON_STROKE_COLOR
        set(@ColorInt color) {
            field = color
            invalidate()
        }

    // Paint
    private val moonBackgroundPaint = Paint()
    private val moonStrokePaint = Paint()

    private var actualDiameter = DESIRED_MOON_DIAMETER

    private var currSweepAngle = 0f

    init {
        moonBackgroundPaint.apply {
            style = Paint.Style.FILL
            color = moonBackgroundColor
        }
        moonStrokePaint.apply {
            style = Paint.Style.STROKE
            color = moonStrokeColor
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        var actualWidth: Int = DESIRED_MOON_DIAMETER
        var actualHeight: Int = DESIRED_MOON_DIAMETER

        when (widthMode) {
            MeasureSpec.EXACTLY -> actualWidth = widthSize
            MeasureSpec.AT_MOST -> actualWidth = Math.min(DESIRED_MOON_DIAMETER, widthSize)
            MeasureSpec.UNSPECIFIED -> actualWidth = DESIRED_MOON_DIAMETER
        }
        when (heightMode) {
            MeasureSpec.EXACTLY -> actualHeight = heightSize
            MeasureSpec.AT_MOST -> actualHeight = Math.min(DESIRED_MOON_DIAMETER, heightSize)
            MeasureSpec.UNSPECIFIED -> actualHeight = DESIRED_MOON_DIAMETER
        }

        val actualDiameter = min(actualWidth, actualHeight)
        setMeasuredDimension(actualDiameter, actualDiameter)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val contentWidth = w - paddingLeft - paddingRight
        val contentHeight = h - paddingTop - paddingBottom
        actualDiameter = min(contentWidth, contentHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawHalfMoon(canvas)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val animator = ValueAnimator.ofFloat(currSweepAngle, 360f)
        animator.duration = 1000
        animator.addUpdateListener {
            currSweepAngle = it.animatedValue as Float
            invalidate()
        }
        animator.start()
    }

    private fun drawHalfMoon(canvas: Canvas){
        canvas.drawArc(RectF(0f, 0f, actualDiameter/1f, actualDiameter/1f), -90f, currSweepAngle, true, moonBackgroundPaint)
        canvas.drawArc(RectF(0f, 0f, actualDiameter/1f, actualDiameter/1f), -90f, currSweepAngle, true, moonStrokePaint)

    }
}