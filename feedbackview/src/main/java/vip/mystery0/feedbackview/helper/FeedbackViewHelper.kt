package vip.mystery0.feedbackview.helper

import android.content.Context
import android.content.Intent
import android.net.Uri
import vip.mystery0.feedbackview.listener.DoDownloadListener
import vip.mystery0.feedbackview.listener.DoSelectListener
import vip.mystery0.feedbackview.listener.DoUploadListener
import vip.mystery0.feedbackview.listener.MessageSendListener
import vip.mystery0.feedbackview.model.*
import vip.mystery0.feedbackview.ui.activity.FeedbackActivity
import vip.mystery0.feedbackview.ui.activity.postAdd
import vip.mystery0.feedbackview.ui.activity.postUpdate
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
        val instance by lazy { FeedbackViewHelper() }

        fun init(context: Context) {
            ToolsClient.initWithContext(context)
            instance.context = context
        }
    }

    fun start() {
        InternalHelper.prepare()
        val intent = Intent(context, FeedbackActivity::class.java)
        context?.startActivity(intent)
    }

    fun add(baseMessage: BaseMessage, clearInput: Boolean = false) {
        EventBusMessageBean(baseMessage, clearInput).postAdd()
        if (baseMessage is ImageMessage) {
            when (baseMessage.messageType) {
                MessageType.RECEIVE -> InternalHelper.downloadHandler!!.addDownloadInfo(DownloadInfo(baseMessage, baseMessage.imageUrl!!))
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
        val file = ImageMessage.getLocalFileFromUri(uri)
        FileTools.instance.cloneUriToFile(context!!, uri, file)
        val imageMessage = ImageMessage(MessageType.SEND, false)
        imageMessage.localFile = file
        add(imageMessage)
    }

    /**
     * 自定义文件选择的Uri接收
     */
    fun receiveFileUri(uri: Uri) {
        val file = ImageMessage.getLocalFileFromUri(uri)
        FileTools.instance.cloneUriToFile(context!!, uri, file)
        val fileMessage = FileMessage(MessageType.SEND, false)
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