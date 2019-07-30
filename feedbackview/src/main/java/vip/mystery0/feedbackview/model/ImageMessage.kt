package vip.mystery0.feedbackview.model

class ImageMessage(type: MessageType, state: Boolean) : BaseMessage(type, Type.IMAGE, state) {
    var imageUrl: String? = null
}