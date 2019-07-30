package vip.mystery0.feedbackview.model

import android.graphics.drawable.StateListDrawable
import java.io.Serializable

abstract class BaseMessage(
    var id: Long,
    val messageType: MessageType,
    val type: Type,
    var state: Boolean
) :Serializable{
    var drawable: StateListDrawable? = null

    fun copyFrom(baseMessage: BaseMessage) {
        this.id = baseMessage.id
        this.state = baseMessage.state
        this.drawable = baseMessage.drawable
    }
}

enum class MessageType {
    SEND, RECEIVE
}

enum class Type(val code: Int) {
    TEXT(1),
    IMAGE(2),
    FILE(3);

    override fun toString(): String = code.toString()
}