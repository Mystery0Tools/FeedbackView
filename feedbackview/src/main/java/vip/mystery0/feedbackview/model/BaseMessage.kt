package vip.mystery0.feedbackview.model

abstract class BaseMessage(
    val id: Long,
    val messageType: MessageType,
    val type: Type
)

enum class MessageType {
    SEND, RECEIVE
}

enum class Type(val code: Int) {
    TEXT(1),
    IMAGE(2),
    FILE(3);

    override fun toString(): String = code.toString()


}