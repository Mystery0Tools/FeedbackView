package vip.mystery0.feedbackview.model

class ImageMessage(id: Long, type: MessageType, state: Boolean) : BaseMessage(id, type, Type.IMAGE, state) {
    var imageUrl: String? = null
}