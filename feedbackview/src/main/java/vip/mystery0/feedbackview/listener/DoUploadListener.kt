package vip.mystery0.feedbackview.listener

import vip.mystery0.feedbackview.model.UploadInfo
import java.io.File

interface DoUploadListener {
    fun doUpload(file: File, info: UploadInfo): String
}