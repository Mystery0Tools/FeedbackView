package vip.mystery0.feedbackview.helper

import android.os.HandlerThread
import vip.mystery0.feedbackview.model.DownloadInfo
import vip.mystery0.feedbackview.model.UploadInfo
import vip.mystery0.feedbackview.thread.DownloadHandler
import vip.mystery0.feedbackview.thread.UploadHandler

object InternalHelper {
    private var uploadThread: HandlerThread? = null
    private var downloadThread: HandlerThread? = null
    var uploadHandler: UploadHandler? = null
        private set
    var downloadHandler: DownloadHandler? = null
        private set

    fun prepare() {
        if (uploadThread == null)
            uploadThread = HandlerThread("upload")
        if (downloadThread == null)
            downloadThread = HandlerThread("download")
        uploadThread?.start()
        downloadThread?.start()
        uploadHandler = UploadHandler(uploadThread!!.looper)
        downloadHandler = DownloadHandler(downloadThread!!.looper)
    }

    fun uploadFileDone(uploadInfo: UploadInfo) {
        val message = uploadInfo.imageMessage
        message.imageUrl = uploadInfo.remoteUrl
        message.progress = 100
        message.state = true
        FeedbackViewHelper.instance.update(uploadInfo.key, message, false)
    }

    fun downloadFileDone(downloadInfo: DownloadInfo) {
        val message = downloadInfo.imageMessage
        message.localFile = downloadInfo.localFile
        message.progress = 100
        message.state = true
        FeedbackViewHelper.instance.update(downloadInfo.key, message, false)
    }

    fun updateProgress(uploadInfo: UploadInfo) {
        val message = uploadInfo.imageMessage
        FeedbackViewHelper.instance.update(uploadInfo.key, message, false)
    }

    fun updateProgress(downloadInfo: DownloadInfo) {
        val message = downloadInfo.imageMessage
        FeedbackViewHelper.instance.update(downloadInfo.key, message, false)
    }

    fun close() {
        uploadThread?.quitSafely()
        downloadThread?.quitSafely()
        uploadThread = null
        downloadThread = null
    }
}