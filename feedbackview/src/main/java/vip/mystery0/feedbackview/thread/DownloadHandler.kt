package vip.mystery0.feedbackview.thread

import android.os.Handler
import android.os.Looper
import android.os.Message
import vip.mystery0.feedbackview.helper.FeedbackViewHelper
import vip.mystery0.feedbackview.helper.InternalHelper
import vip.mystery0.feedbackview.model.DownloadInfo

class DownloadHandler(looper: Looper) : Handler(looper) {
    companion object {
        const val UPLOAD_TYPE = 31415927
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        if (msg.what != UPLOAD_TYPE) {
            return
        }
        val info = msg.obj as DownloadInfo
        try {
            val local = info.imageMessage.getLocalFileFromName()
            FeedbackViewHelper.instance.doDownloadListener?.doDownload(info.remoteUrl, local, info)
            info.localFile = local
        } catch (e: Exception) {
            e.printStackTrace()
            info.error = e.message
        }
        InternalHelper.downloadFileDone(info)
    }

    fun addDownloadInfo(downloadInfo: DownloadInfo) {
        val message = Message.obtain()
        message.what = UPLOAD_TYPE
        message.obj = downloadInfo
        sendMessage(message)
    }
}
