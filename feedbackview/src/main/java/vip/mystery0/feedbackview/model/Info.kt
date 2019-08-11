package vip.mystery0.feedbackview.model

import java.io.File

data class UploadInfo(
    val key: String,
    var imageMessage: ImageMessage,
    var localFile: File,
    var remoteUrl: String? = null,
    var error: String? = null
)

data class DownloadInfo(
    val key: String,
    var imageMessage: ImageMessage,
    var remoteUrl: String,
    var localFile: File? = null,
    var error: String? = null
)