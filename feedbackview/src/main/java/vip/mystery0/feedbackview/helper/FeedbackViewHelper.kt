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
import vip.mystery0.feedbackview.viewModel.MessageViewModel
import vip.mystery0.tools.ToolsClient
import vip.mystery0.tools.model.Pair3
import vip.mystery0.tools.utils.FileTools
import vip.mystery0.tools.utils.sha256
import java.util.*

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

    fun add(baseMessage: BaseMessage, clearInput: Boolean = false): String {
        val key = Calendar.getInstance().time.toString().sha256()
        MessageViewModel.addMessage.postValue(Pair3(key, baseMessage, clearInput))
        if (baseMessage is ImageMessage) {
            when (baseMessage.messageType) {
                MessageType.RECEIVE -> InternalHelper.downloadHandler!!.addDownloadInfo(DownloadInfo(key, baseMessage, baseMessage.imageUrl!!))
                MessageType.SEND -> InternalHelper.uploadHandler!!.addUploadInfo((UploadInfo(key, baseMessage, baseMessage.localFile!!)))
                else -> {
                }
            }
        }
        return key
    }

    fun update(key: String, baseMessage: BaseMessage, noUpdate: Boolean = false) {
        MessageViewModel.updateMessage.postValue(Pair3(key, baseMessage, noUpdate))
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
        update(uploadInfo.key, message, false)
    }

    fun updateProgress(downloadInfo: DownloadInfo) {
        val message = downloadInfo.baseMessage
        when (message) {
            is ImageMessage -> message.progress = downloadInfo.progress
            is FileMessage -> message.progress = downloadInfo.progress
        }
        update(downloadInfo.key, message, false)
    }

    fun clear() {
        InternalHelper.close()
        context = null
    }
}