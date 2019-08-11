package vip.mystery0.feedbackview.ui

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import vip.mystery0.tools.utils.dp2px
import java.util.*

class SoftKeyboardStateWatcher(
    private val activityRootView: View
) : ViewTreeObserver.OnGlobalLayoutListener {
    private val listeners = LinkedList<SoftKeyboardStateListener>()
    private var isSoftKeyboardOpened = false
    /**
     * Default value is zero `0`.
     *
     * @return last saved keyboard height in px
     */
    private var lastSoftKeyboardHeightInPx: Int = 0

    interface SoftKeyboardStateListener {
        fun onSoftKeyboardOpened(keyboardHeightInPx: Int)
        fun onSoftKeyboardClosed()
    }

    init {
        activityRootView.viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        //下面这种方式则对软键盘没有设置要求
        val r = Rect()
        //r will be populated with the coordinates of your view that area still visible.
        activityRootView.getWindowVisibleDisplayFrame(r)
        val heightDiff = activityRootView.rootView.height - (r.bottom - r.top)
        if (!isSoftKeyboardOpened && heightDiff > dp2px(200f)) { // if more than 100 pixels, its probably a keyboard...
            isSoftKeyboardOpened = true
            notifyOnSoftKeyboardOpened(heightDiff)
        } else if (isSoftKeyboardOpened && heightDiff < dp2px(200f)) {
            isSoftKeyboardOpened = false
            notifyOnSoftKeyboardClosed()
        }
    }

    fun addSoftKeyboardStateListener(listener: SoftKeyboardStateListener) {
        listeners.add(listener)
    }

    fun removeSoftKeyboardStateListener(listener: SoftKeyboardStateListener) {
        listeners.remove(listener)
    }

    private fun notifyOnSoftKeyboardOpened(keyboardHeightInPx: Int) {
        this.lastSoftKeyboardHeightInPx = keyboardHeightInPx
        for (listener in listeners) {
            listener.onSoftKeyboardOpened(keyboardHeightInPx)
        }
    }

    private fun notifyOnSoftKeyboardClosed() {
        for (listener in listeners) {
            listener.onSoftKeyboardClosed()
        }
    }
}