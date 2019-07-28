package vip.mystery0.feedbackview.model

class TextMessage(id: Long, type: MessageType, val text: String) : BaseMessage(id, type, Type.TEXT)