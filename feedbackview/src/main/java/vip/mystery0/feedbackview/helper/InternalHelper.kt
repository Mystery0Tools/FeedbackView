package vip.mystery0.feedbackview.helper

import android.os.HandlerThread
import vip.mystery0.feedbackview.model.DownloadInfo
import vip.mystery0.feedbackview.model.FileMessage
import vip.mystery0.feedbackview.model.ImageMessage
import vip.mystery0.feedbackview.model.UploadInfo
import vip.mystery0.feedbackview.thread.DownloadHandler
import vip.mystery0.feedbackview.thread.UploadHandler

object InternalHelper {
    private val TAG = "InternalHelper"
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
        val message = uploadInfo.baseMessage
        when (message) {
            is ImageMessage -> {
                message.imageUrl = uploadInfo.remoteUrl
                message.progress = 100
            }
            is FileMessage -> {
                message.fileUrl = uploadInfo.remoteUrl
                message.progress = 100
            }
        }
        message.state = true
        FeedbackViewHelper.instance.update(uploadInfo.key, message, true)
    }

    fun downloadFileDone(downloadInfo: DownloadInfo) {
        val message = downloadInfo.baseMessage
        when (message) {
            is ImageMessage -> {
                message.localFile = downloadInfo.localFile
                message.progress = 100
            }
            is FileMessage -> {
                message.localFile = downloadInfo.localFile
                message.progress = 100
            }
        }
        message.state = true
        FeedbackViewHelper.instance.update(downloadInfo.key, message, true)
    }

    fun close() {
        uploadThread?.quitSafely()
        downloadThread?.quitSafely()
        uploadThread = null
        downloadThread = null
    }
}