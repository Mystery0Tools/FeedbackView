package vip.mystery0.feedbackview.model

import vip.mystery0.feedbackview.helper.FeedbackViewHelper
import vip.mystery0.feedbackview.utils.sha256
import java.io.File

class ImageMessage(type: MessageType, state: Boolean) : BaseMessage(type, Type.IMAGE, state) {
    var localFile: File? = null
    var imageUrl: String? = null
    var progress: Int = 0

    fun getLocalFileFromName(): File {
        val fileName = imageUrl!!.sha256()
        val file = File(LOCAL_DIR, fileName)
        if (file.exists()) {
            localFile = file
            progress = 100
        }
        return file
    }

    companion object {
        private val LOCAL_DIR = File(FeedbackViewHelper.instance.context!!.externalCacheDir, "image")
    }
}