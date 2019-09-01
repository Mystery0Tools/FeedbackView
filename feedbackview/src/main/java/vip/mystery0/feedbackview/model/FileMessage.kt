package vip.mystery0.feedbackview.model

import java.io.File

class FileMessage(type: MessageType, state: Boolean) : BaseMessage(type, Type.FILE, state) {
    var localFile: File? = null
    var fileUrl: String? = null
    var progress: Int = 0
    var fileTitle: String? = null
    var fileSize = 0L
}