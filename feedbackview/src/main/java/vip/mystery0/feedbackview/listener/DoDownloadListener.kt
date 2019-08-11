package vip.mystery0.feedbackview.listener

import java.io.File

interface DoDownloadListener {
    fun doDownload(url: String, localFile: File)
}