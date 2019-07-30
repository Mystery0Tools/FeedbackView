package vip.mystery0.feedbackview.viewModel

import androidx.lifecycle.ViewModel
import vip.mystery0.feedbackview.model.BaseMessage

class FeedbackViewModel : ViewModel() {
    val map: HashMap<String, BaseMessage> by lazy { HashMap<String,BaseMessage>() }
}