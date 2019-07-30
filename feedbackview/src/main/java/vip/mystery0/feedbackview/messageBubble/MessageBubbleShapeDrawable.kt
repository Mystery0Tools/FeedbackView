package vip.mystery0.feedbackview.messageBubble

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable

class MessageBubbleShapeDrawable : ShapeDrawable {
    /**
     * Sets padding for the shape.
     *
     * @param left padding for the left side (in pixels)
     * @param top padding for the top (in pixels)
     * @param right padding for the right side (in pixels)
     * @param bottom padding for the bottom (in pixels)
     */
    constructor(shape: MessageBubble, left: Int, top: Int, right: Int, bottom: Int) : super(shape) {
        setPadding(left, top, right, bottom)
    }

    /**
     * Sets padding for the shape.
     *
     * @param padding padding for the all sides (in pixels)
     * @param autoPadding auto padding for the arrow direction
     */
    constructor(shape: MessageBubble, padding: Int, autoPadding: Boolean) : super(shape) {
        if (autoPadding) {
            val fixedPadding = (shape.arrowWidth + shape.strokeWidth + padding).toInt()
            if (shape.arrowDirection == MessageBubble.Direction.START) {
                setPadding(fixedPadding, padding, padding, padding)
            } else {
                setPadding(padding, padding, fixedPadding, padding)
            }
        } else {
            setPadding(padding, padding, padding, padding)
        }
    }

    /**
     * Sets padding for this shape, defined by a Rect object. Define the padding
     * in the Rect object as: left, top, right, bottom.
     */
    constructor(shape: MessageBubble, padding: Rect) : super(shape) {
        setPadding(padding)
    }

    companion object {
        private fun Context.dip(value: Float): Int = (value * resources.displayMetrics.density).toInt()

        fun bubbleStateListDrawable(context: Context, shape: MessageBubble, isSender: Boolean): StateListDrawable {
            val darkColor = Color.argb(
                Color.alpha(shape.backgroundColor),
                (Color.red(shape.backgroundColor) * 0.9).toInt(),
                (Color.green(shape.backgroundColor) * 0.9).toInt(),
                (Color.blue(shape.backgroundColor) * 0.9).toInt()
            )

            shape.arrowDirection = if (isSender) MessageBubble.Direction.END else MessageBubble.Direction.START

            val drawable = MessageBubbleShapeDrawable(shape, context.dip(6F), true)
            val darkShape = shape.clone().apply { backgroundColor = darkColor }
            val darkDrawable = MessageBubbleShapeDrawable(darkShape, context.dip(6F), true)

            return StateListDrawable().apply {
                addState(intArrayOf(android.R.attr.state_pressed), darkDrawable)
                addState(intArrayOf(android.R.attr.state_selected), darkDrawable)
                addState(intArrayOf(), drawable)
            }
        }
    }
}