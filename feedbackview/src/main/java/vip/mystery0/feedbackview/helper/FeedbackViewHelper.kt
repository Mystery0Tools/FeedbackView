package vip.mystery0.feedbackview.helper

import android.content.Context
import android.content.Intent
import vip.mystery0.feedbackview.listener.DoDownloadListener
import vip.mystery0.feedbackview.listener.DoUploadListener
import vip.mystery0.feedbackview.listener.MessageSendListener
import vip.mystery0.feedbackview.model.*
import vip.mystery0.feedbackview.ui.activity.FeedbackActivity
import vip.mystery0.feedbackview.utils.Pair3
import vip.mystery0.feedbackview.utils.sha256
import vip.mystery0.feedbackview.viewModel.MessageViewModel
import java.util.*

class FeedbackViewHelper private constructor() {
    var context: Context? = null
        private set

    var messageSendListener: MessageSendListener? = null
    var doUploadListener: DoUploadListener? = null
    var doDownloadListener: DoDownloadListener? = null

    companion object {
        val instance by lazy { FeedbackViewHelper() }

        fun init(context: Context) {
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

    fun update(key: String, baseMessage: BaseMessage, clearInput: Boolean = false) {
        MessageViewModel.updateMessage.postValue(Pair3(key, baseMessage, clearInput))
    }

    fun clear() {
        InternalHelper.close()
        context = null
    }
}