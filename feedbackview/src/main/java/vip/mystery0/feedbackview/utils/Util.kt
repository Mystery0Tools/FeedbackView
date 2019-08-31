package vip.mystery0.feedbackview.utils

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import vip.mystery0.feedbackview.helper.FeedbackViewHelper
import vip.mystery0.tools.utils.sha256
import java.io.File

fun View.changeLayoutParams(action: (ViewGroup.LayoutParams) -> ViewGroup.LayoutParams) {
    this.layoutParams = action(layoutParams)
}

private val LOCAL_DIR = File(FeedbackViewHelper.instance.context!!.externalCacheDir, "file")

fun String.getLocalFileFromName(): File {
    val fileName = this.sha256()
    return File(LOCAL_DIR, fileName)
}

fun Uri.getLocalFileFromUri(): File {
    val fileName = this.toString().sha256()
    return File(LOCAL_DIR, fileName)
}