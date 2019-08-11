package vip.mystery0.feedbackview.utils

import android.view.View
import android.view.ViewGroup

fun View.changeLayoutParams(action: (ViewGroup.LayoutParams) -> Unit) {
    val params = layoutParams
    action(params)
    this.layoutParams = params
}