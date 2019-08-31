package vip.mystery0.feedbackview.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import vip.mystery0.feedbackview.R
import vip.mystery0.tools.getTColor
import kotlin.math.roundToInt


class CirclePercentView : View {
    private var progress = 0
    private var radius = 48F
    //圆心坐标
    private var centerX = 0F
    private var centerY = 0F
    //总体大小
    private var mHeight = 0
    private var mWidth = 0
    //要画的弧度
    private var mEndAngle = 0F

    private val circlePaint = Paint()
    private val sectorPaint = Paint()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        circlePaint.isAntiAlias = true
        circlePaint.color = Color.WHITE
        circlePaint.style = Paint.Style.STROKE
        circlePaint.strokeWidth = 2F
        sectorPaint.isAntiAlias = true
        sectorPaint.color = Color.WHITE
        sectorPaint.style = Paint.Style.FILL
        setBackgroundColor(context.getTColor(R.color.progressColor))
    }

    fun updateProgress(progress: Int) {
        this.progress = progress
        if (progress <= 0)
            this.progress = 0
        if (progress >= 100)
            this.progress = 100
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //获取测量模式
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        //获取测量大小
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            centerX = (widthSize / 2).toFloat()
            centerY = (heightSize / 2).toFloat()
            mWidth = widthSize
            mHeight = heightSize
        }
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            mWidth = (radius * 2).roundToInt()
            mHeight = (radius * 2).roundToInt()
            centerX = radius
            centerY = radius
        }
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas) {
        mEndAngle = progress * 3.6F
        canvas.drawCircle(centerX, centerY, radius + 20, circlePaint)
        canvas.drawCircle(centerX, centerY, radius + 10, circlePaint)
        //绘制圆环
        canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius, 270F, mEndAngle, true, sectorPaint)
    }
}