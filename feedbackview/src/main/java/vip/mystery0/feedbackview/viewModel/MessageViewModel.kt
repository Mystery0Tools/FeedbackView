package vip.mystery0.feedbackview.viewModel

import androidx.lifecycle.MutableLiveData
import vip.mystery0.feedbackview.model.BaseMessage
import vip.mystery0.feedbackview.model.UploadInfo
import vip.mystery0.tools.model.Pair3

object MessageViewModel {
    val addMessage: MutableLiveData<Pair3<String, BaseMessage, Boolean>> by lazy { MutableLiveData<Pair3<String, BaseMessage, Boolean>>() }
    val updateMessage: MutableLiveData<Pair3<String, BaseMessage, Boolean>> by lazy { MutableLiveData<Pair3<String, BaseMessage, Boolean>>() }
    val uploadMessage: MutableLiveData<UploadInfo> by lazy { MutableLiveData<UploadInfo>() }
}