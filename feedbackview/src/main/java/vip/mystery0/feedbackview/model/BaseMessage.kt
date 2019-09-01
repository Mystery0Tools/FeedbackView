package vip.mystery0.feedbackview.model

import vip.mystery0.tools.utils.sha1
import java.io.Serializable
import java.util.*

abstract class BaseMessage(
    val messageType: MessageType,
    val type: Type,
    var state: Boolean,
    var time: Long = Calendar.getInstance().timeInMillis
) : Serializable {
    var id = time.toString().sha1()
    var extraData: Any? = null
    var error: String? = null

    fun generateId() {
        id = time.toString().sha1()
    }

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