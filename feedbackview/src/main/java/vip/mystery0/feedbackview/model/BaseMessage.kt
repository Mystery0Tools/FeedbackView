package vip.mystery0.feedbackview.model

import java.io.Serializable
import java.util.*

abstract class BaseMessage(
    val messageType: MessageType,
    val type: Type,
    var state: Boolean,
    val time: Long = Calendar.getInstance().timeInMillis
) : Serializable {
    open fun copyFrom(baseMessage: BaseMessage) {
        this.state = baseMessage.state
    }
}

enum class MessageType {
    SEND, RECEIVE, SYSTEM
}

enum class Type(val code: Int) {
    SYSTEM(0),
    TEXT(1),
    IMAGE(2),
    FILE(3);

    override fun toString(): String = code.toString()
}