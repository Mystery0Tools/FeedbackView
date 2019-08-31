package vip.mystery0.feedbackview.model

import java.io.File

data class UploadInfo(
    var baseMessage: BaseMessage,
    var localFile: File,
    var remoteUrl: String? = null
) {
    var progress = 0
}

data class DownloadInfo(
    var baseMessage: BaseMessage,
    var remoteUrl: String,
    var localFile: File? = null
){
    var progress = 0
}