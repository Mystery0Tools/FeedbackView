package vip.mystery0.feedbackview.thread

import android.os.Handler
import android.os.Looper
import android.os.Message
import vip.mystery0.feedbackview.helper.FeedbackViewHelper
import vip.mystery0.feedbackview.helper.InternalHelper
import vip.mystery0.feedbackview.model.UploadInfo

class UploadHandler(looper: Looper) : Handler(looper) {
    companion object {
        const val UPLOAD_TYPE = 31415926
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        if (msg.what != UPLOAD_TYPE) {
            return
        }
        val info = msg.obj as UploadInfo
        try {
            val url = FeedbackViewHelper.instance.doUploadListener?.doUpload(info.localFile)
            info.remoteUrl = url
        } catch (e: Exception) {
            e.printStackTrace()
            info.error = e.message
        }
        InternalHelper.uploadFileDone(info)
    }

    fun addUploadInfo(uploadInfo: UploadInfo) {
        val message = Message.obtain()
        message.what = UPLOAD_TYPE
        message.obj = uploadInfo
        sendMessage(message)
    }

    fun updateProgress(uploadInfo: UploadInfo){
        InternalHelper.updateProgress(uploadInfo)
    }
}
