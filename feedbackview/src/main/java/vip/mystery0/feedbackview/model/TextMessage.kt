package vip.mystery0.feedbackview.model

import java.io.Serializable

class TextMessage(type: MessageType, state: Boolean, var text: String) : BaseMessage(type, Type.TEXT, state), Serializable {
    companion object {
        fun send(text: String): TextMessage = TextMessage(MessageType.SEND, false, text)
        fun receive(text: String): TextMessage = TextMessage(MessageType.RECEIVE, false, text)
    }

    fun copyFrom(baseMessage: TextMessage) {
        super.copyFrom(baseMessage)
        this.text = baseMessage.text
    }
}