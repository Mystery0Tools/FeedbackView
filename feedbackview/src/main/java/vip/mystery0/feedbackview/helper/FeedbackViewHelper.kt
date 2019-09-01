package vip.mystery0.feedbackview.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import vip.mystery0.feedbackview.listener.DoDownloadListener
import vip.mystery0.feedbackview.listener.DoSelectListener
import vip.mystery0.feedbackview.listener.DoUploadListener
import vip.mystery0.feedbackview.listener.MessageSendListener
import vip.mystery0.feedbackview.model.*
import vip.mystery0.feedbackview.ui.activity.FeedbackActivity
import vip.mystery0.feedbackview.ui.activity.postAdd
import vip.mystery0.feedbackview.ui.activity.postUpdate
import vip.mystery0.feedbackview.utils.getLocalFileFromUri
import vip.mystery0.tools.ToolsClient
import vip.mystery0.tools.utils.FileTools

class FeedbackViewHelper private constructor() {
    var context: Context? = null
        private set

    var messageSendListener: MessageSendListener? = null
    var doUploadListener: DoUploadListener? = null
    var doDownloadListener: DoDownloadListener? = null
    var doSelectListener: DoSelectListener? = null

    companion object {
        const val INTENT_FEED_BACK_ACTIVITY = 111
        val instance by lazy { FeedbackViewHelper() }

        fun init(context: Context) {
            ToolsClient.initWithContext(context)
            instance.context = context
        }
    }

    fun start(activity: Activity) {
        InternalHelper.prepare()
        val intent = Intent(activity, FeedbackActivity::class.java)
        activity.startActivityForResult(intent, INTENT_FEED_BACK_ACTIVITY)
    }

    fun add(baseMessage: BaseMessage, clearInput: Boolean = false) {
        EventBusMessageBean(baseMessage, clearInput).postAdd()
        when (baseMessage) {
            is ImageMessage -> when (baseMessage.messageType) {
                MessageType.RECEIVE -> InternalHelper.downloadHandler!!.addDownloadInfo(DownloadInfo(baseMessage, baseMessage.imageUrl!!))
                MessageType.SEND -> InternalHelper.uploadHandler!!.addUploadInfo((UploadInfo(baseMessage, baseMessage.localFile!!)))
                else -> {
                }
            }
            is FileMessage -> when (baseMessage.messageType) {
                MessageType.RECEIVE -> InternalHelper.downloadHandler!!.addDownloadInfo(DownloadInfo(baseMessage, baseMessage.fileUrl!!))
                MessageType.SEND -> InternalHelper.uploadHandler!!.addUploadInfo((UploadInfo(baseMessage, baseMessage.localFile!!)))
                else -> {
                }
            }
        }
    }

    fun update(baseMessage: BaseMessage) {
        EventBusMessageBean(baseMessage, false).postUpdate()
    }

    /**
     * 自定义图片选择的Uri接收
     */
    fun receiveImageUri(uri: Uri) {
        val file = uri.getLocalFileFromUri()
        FileTools.instance.cloneUriToFile(context!!, uri, file)
        val imageMessage = ImageMessage(MessageType.SEND, false)
        imageMessage.localFile = file
        add(imageMessage)
    }

    /**
     * 自定义文件选择的Uri接收
     */
    fun receiveFileUri(uri: Uri) {
        val file = uri.getLocalFileFromUri()
        FileTools.instance.cloneUriToFile(context!!, uri, file)
        val fileMessage = FileMessage(MessageType.SEND, false)
        uri.let { returnUri ->
            context!!.contentResolver.query(returnUri, null, null, null, null)
        }?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            cursor.moveToFirst()
            fileMessage.fileTitle = cursor.getString(nameIndex)
            fileMessage.fileSize = cursor.getLong(sizeIndex)
        }
        fileMessage.localFile = file
        add(fileMessage)
    }

    fun updateProgress(uploadInfo: UploadInfo) {
        val message = uploadInfo.baseMessage
        when (message) {
            is ImageMessage -> message.progress = uploadInfo.progress
            is FileMessage -> message.progress = uploadInfo.progress
        }
        update(message)
    }

    fun updateProgress(downloadInfo: DownloadInfo) {
        val message = downloadInfo.baseMessage
        when (message) {
            is ImageMessage -> message.progress = downloadInfo.progress
            is FileMessage -> message.progress = downloadInfo.progress
        }
        update(message)
    }

    fun clear() {
        InternalHelper.close()
        context = null
    }
}