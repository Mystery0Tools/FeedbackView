package vip.mystery0.feedbackview.listener

import vip.mystery0.feedbackview.model.BaseMessage

interface MessageSendListener {
    fun onSend(key: String, baseMessage: BaseMessage)
}