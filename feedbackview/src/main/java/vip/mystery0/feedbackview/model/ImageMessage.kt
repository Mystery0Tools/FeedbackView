package vip.mystery0.feedbackview.model

class ImageMessage(id: Long, type: MessageType) : BaseMessage(id, type, Type.IMAGE) {
    var imageUrl: String? = null
}