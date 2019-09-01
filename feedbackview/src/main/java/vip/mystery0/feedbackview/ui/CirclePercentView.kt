package vip.mystery0.feedbackview.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import vip.mystery0.feedbackview.R
import vip.mystery0.tools.getTColor

class CirclePercentView : View {
    private var progress = 0
    private var radius = 0
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
    private val backgroundPaint = Paint()

    private var drawBackground = true

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initStyle(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initStyle(context, attrs)
    }

    init {
        circlePaint.isAntiAlias = true
        circlePaint.color = context.getTColor(R.color.progressColor)
        circlePaint.style = Paint.Style.STROKE
        circlePaint.strokeWidth = 2F
        sectorPaint.isAntiAlias = true
        sectorPaint.color = context.getTColor(R.color.progressColor)
        sectorPaint.style = Paint.Style.FILL
        backgroundPaint.isAntiAlias = true
        backgroundPaint.color = context.getTColor(R.color.backgroundColor)
        backgroundPaint.style = Paint.Style.FILL
    }

    private fun initStyle(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentView)
        radius = typedArray.getDimensionPixelSize(R.styleable.CirclePercentView_radius, 36)
        drawBackground = typedArray.getBoolean(R.styleable.CirclePercentView_drawBackground, true)
        typedArray.recycle()
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
            mWidth = radius * 2
            mHeight = radius * 2
            centerX = radius.toFloat()
            centerY = radius.toFloat()
        }
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas) {
        mEndAngle = progress * 3.6F
        if (drawBackground)
            canvas.drawColor(context.getTColor(R.color.backgroundColor))
        else
            canvas.drawCircle(centerX, centerY, radius + 30F, backgroundPaint)
        canvas.drawCircle(centerX, centerY, radius + 20F, circlePaint)
        canvas.drawCircle(centerX, centerY, radius + 10F, circlePaint)
        //绘制圆环
        canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius, 270F, mEndAngle, true, sectorPaint)
        //绘制背景
    }
}