package vip.mystery0.feedbackview.messageBubble

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.shapes.Shape
import androidx.annotation.ColorInt

class MessageBubble(
    var arrowDirection: Direction = Direction.START,
    @ColorInt var backgroundColor: Int,
    @ColorInt var strokeColor: Int,
    var strokeWidth: Float,
    var cornerRadius: Float,
    var arrowWidth: Float,
    var arrowHeight: Float,
    var arrowMarginTop: Float
) : Shape() {
    //The path to fill canvas.
    private var pathBackground = Path()
    //The path to draw stroke.
    private var pathStroke = Path()
    //RectF for reusing.
    private var rectF = RectF()
    //Stroke offset.
    private var strokeOffset = strokeWidth / 2
    //he height of upper area with no arrow.
    private val mUpperHeightNA = cornerRadius + arrowMarginTop + strokeOffset
    //The height of upper area with half a arrow.
    private val mUpperHeightHA = mUpperHeightNA + (arrowHeight / 2)
    //The height of upper area with a full arrow.
    private val mUpperHeightFA = mUpperHeightNA + arrowHeight

    override fun draw(canvas: Canvas, paint: Paint) {
        if (arrowDirection == Direction.END)
            canvas.scale(-1F, 1F, width / 2, height / 2)
        canvas.save()
        drawBackground(canvas, paint)
        drawStroke(canvas, paint)
        canvas.restore()
    }

    private fun drawBackground(canvas: Canvas, paint: Paint) {
        paint.color = backgroundColor
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.isDither = true
        canvas.drawPath(pathBackground, paint)
    }

    private fun drawStroke(canvas: Canvas, paint: Paint) {
        paint.color = strokeColor
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = strokeWidth
        canvas.drawPath(pathStroke, paint)
    }

    override fun onResize(width: Float, height: Float) {
        resizeBackgroundPath(width, height)
        resizeStrokePath(width, height)
    }

    private fun resizeBackgroundPath(width: Float, height: Float) {
        val cornerRadius = cornerRadius
        val arrowWidth = arrowWidth
        pathBackground.reset()
        // Draw arrow.
        pathBackground.moveTo(arrowWidth, mUpperHeightFA)
        pathBackground.lineTo(0F, mUpperHeightHA)
        pathBackground.lineTo(arrowWidth, mUpperHeightNA)
        pathBackground.lineTo(arrowWidth, cornerRadius)

        // Upper left corner and the upper line.
        rectF.set(arrowWidth, 0F, arrowWidth + cornerRadius, cornerRadius)
        pathBackground.arcTo(rectF, 180F, 90F)
        pathBackground.lineTo(width - cornerRadius, 0F)

        // Upper right corner and the right line.
        rectF.set(width - cornerRadius, 0F, width, cornerRadius)
        pathBackground.arcTo(rectF, 270F, 90F)
        pathBackground.lineTo(width, height - cornerRadius)

        // Bottom right corner and the bottom line.
        rectF.set(width - cornerRadius, height - cornerRadius, width, height)
        pathBackground.arcTo(rectF, 0F, 90F)
        pathBackground.lineTo((arrowWidth + cornerRadius), height)

        // Bottom left corner.
        rectF.set(arrowWidth, height - cornerRadius, arrowWidth + cornerRadius, height)
        pathBackground.arcTo(rectF, 90F, 90F)

        pathBackground.close()
    }

    private fun resizeStrokePath(width: Float, height: Float) {
        val strokeOffset = strokeOffset
        val cornerRadius = cornerRadius
        val arrowWidth = arrowWidth

        pathStroke.reset()

        // Arrow and the upper left line.
        pathStroke.moveTo(arrowWidth + strokeOffset, mUpperHeightFA)
        pathStroke.lineTo(strokeOffset, mUpperHeightHA)
        pathStroke.lineTo(arrowWidth + strokeOffset, mUpperHeightNA)
        pathStroke.lineTo(arrowWidth + strokeOffset, cornerRadius)

        // Upper left corner and the upper line.
        rectF.set(arrowWidth + strokeOffset, strokeOffset, arrowWidth + cornerRadius - strokeOffset, cornerRadius - strokeOffset)
        pathStroke.arcTo(rectF, 180F, 90F)
        pathStroke.lineTo(width - cornerRadius, strokeOffset)

        // Upper right corner and the right line.
        rectF.set(width - cornerRadius + strokeOffset, strokeOffset, width - strokeOffset, cornerRadius - strokeOffset)
        pathStroke.arcTo(rectF, 270F, 90F)
        pathStroke.lineTo(width - strokeOffset, height - cornerRadius)

        // Bottom right corner and the bottom line.
        rectF.set(width - cornerRadius + strokeOffset, height - cornerRadius + strokeOffset, width - strokeOffset, height - strokeOffset)
        pathStroke.arcTo(rectF, 0F, 90F)
        pathStroke.lineTo((arrowWidth + cornerRadius), height - strokeOffset)

        // Bottom left corner.
        rectF.set(arrowWidth + strokeOffset, height - cornerRadius + strokeOffset, arrowWidth + cornerRadius - strokeOffset, height - strokeOffset)
        pathStroke.arcTo(rectF, 90F, 90F)

        pathStroke.close()
    }

    override fun clone(): MessageBubble = super.clone() as MessageBubble

    enum class Direction {
        START, END
    }
}