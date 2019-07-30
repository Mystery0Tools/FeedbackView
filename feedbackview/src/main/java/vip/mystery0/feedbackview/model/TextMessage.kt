package vip.mystery0.feedbackview.model

import java.io.Serializable

class TextMessage(id: Long, type: MessageType, state: Boolean, var text: String) : BaseMessage(id, type, Type.TEXT, state), Serializable {
    companion object {
        fun send(text: String): TextMessage = TextMessage(-1L, MessageType.SEND, false, text)
        fun receive(text: String): TextMessage = TextMessage(-1L, MessageType.RECEIVE, false, text)
    }
}