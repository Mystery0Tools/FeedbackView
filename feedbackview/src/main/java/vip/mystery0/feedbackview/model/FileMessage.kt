package vip.mystery0.feedbackview.model

import android.net.Uri
import vip.mystery0.feedbackview.helper.FeedbackViewHelper
import vip.mystery0.tools.utils.sha256
import java.io.File

class FileMessage(type: MessageType, state: Boolean) : BaseMessage(type, Type.FILE, state) {
    var localFile: File? = null
    var fileUrl: String? = null
    var progress: Int = 0

    fun getLocalFileFromName(): File {
        val fileName = fileUrl!!.sha256()
        val file = File(LOCAL_DIR, fileName)
        if (file.exists()) {
            localFile = file
            progress = 100
        }
        return file
    }

    companion object {
        private val LOCAL_DIR = File(FeedbackViewHelper.instance.context!!.externalCacheDir, "file")

        fun getLocalFileFromUri(uri: Uri): File {
            val fileName = uri.toString().sha256()
            val file = File(LOCAL_DIR, fileName)
            return file
        }
    }
}