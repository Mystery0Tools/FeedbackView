package vip.mystery0.feedbackview

import android.content.Context
import android.content.Intent
import vip.mystery0.feedbackview.listener.MessageSendListener
import vip.mystery0.feedbackview.model.BaseMessage
import vip.mystery0.feedbackview.ui.activity.FeedbackActivity
import vip.mystery0.feedbackview.utils.Pair3
import vip.mystery0.feedbackview.utils.sha256
import vip.mystery0.feedbackview.viewModel.MessageViewModel
import java.util.*

class FeedbackViewHelper private constructor() {
    var context: Context? = null
        private set

    var messageSendListener: MessageSendListener? = null

    companion object {
        val instance by lazy { FeedbackViewHelper() }

        fun init(context: Context) {
            instance.context = context
        }
    }

    fun start() {
        val intent = Intent(context, FeedbackActivity::class.java)
        context?.startActivity(intent)
    }

    /**
     * 添加消息，后续消息的状态通过广播进行更改
     */
    fun add(baseMessage: BaseMessage, clearInput: Boolean = false): String {
        val key = Calendar.getInstance().time.toString().sha256()
        MessageViewModel.addMessage.postValue(Pair3(key, baseMessage, clearInput))
        return key
    }

    fun update(key: String, baseMessage: BaseMessage, clearInput: Boolean = false) {
        MessageViewModel.updateMessage.postValue(Pair3(key, baseMessage, clearInput))
    }

    fun clear() {
        context = null
    }
}