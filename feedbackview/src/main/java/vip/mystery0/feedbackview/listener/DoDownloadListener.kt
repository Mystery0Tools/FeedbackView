package vip.mystery0.feedbackview.listener

import vip.mystery0.feedbackview.model.DownloadInfo
import java.io.File

interface DoDownloadListener {
    fun doDownload(url: String, localFile: File, info: DownloadInfo)
}