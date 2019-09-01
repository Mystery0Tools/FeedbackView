package vip.mystery0.feedbackview.model

import java.io.File

class ImageMessage(type: MessageType, state: Boolean) : BaseMessage(type, Type.IMAGE, state) {
    var localFile: File? = null
    var imageUrl: String? = null
    var progress: Int = 0
}