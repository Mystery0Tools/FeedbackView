package vip.mystery0.feedbackview.listener

import java.io.File

interface DoUploadListener {
    fun doUpload(file: File): String
}