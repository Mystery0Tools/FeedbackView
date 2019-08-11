package vip.mystery0.feedbackview.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_feedback.*
import vip.mystery0.feedbackview.helper.FeedbackViewHelper
import vip.mystery0.feedbackview.R
import vip.mystery0.feedbackview.model.BaseMessage
import vip.mystery0.feedbackview.model.TextMessage
import vip.mystery0.feedbackview.ui.SoftKeyboardStateWatcher
import vip.mystery0.feedbackview.utils.Pair3
import vip.mystery0.feedbackview.utils.sha256
import vip.mystery0.feedbackview.viewModel.FeedbackViewModel
import vip.mystery0.feedbackview.viewModel.MessageViewModel
import java.util.*

class FeedbackActivity : AppCompatActivity() {
    private val feedbackViewModel: FeedbackViewModel by lazy { ViewModelProviders.of(this)[FeedbackViewModel::class.java] }
    private lateinit var watcher: SoftKeyboardStateWatcher
    private val listener = object : SoftKeyboardStateWatcher.SoftKeyboardStateListener {
        override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
            feedbackView.scrollToBottom()
        }

        override fun onSoftKeyboardClosed() {
            feedbackView.scrollToBottom()
        }
    }

    private val addMessageObserver = Observer<Pair3<String, BaseMessage, Boolean>> {
        feedbackViewModel.map[it.first] = it.second
        feedbackView.addMessage(it.second, it.third)
        feedbackView.scrollToBottom()
    }

    private val updateMessageObserver = Observer<Pair3<String, BaseMessage, Boolean>> {
        val save = feedbackViewModel.map[it.first] ?: return@Observer
        save.copyFrom(it.second)
        feedbackView.updateMessage(save, it.third)
        feedbackViewModel.map.remove(it.first)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        MessageViewModel.addMessage.observeForever(addMessageObserver)
        MessageViewModel.updateMessage.observeForever(updateMessageObserver)
        watcher = SoftKeyboardStateWatcher(feedbackView, this)
        watcher.addSoftKeyboardStateListener(listener)
        feedbackView.onSendListener {
            val key = Calendar.getInstance().time.toString().sha256()
            val message = TextMessage.send(it)
            MessageViewModel.addMessage.postValue(Pair3(key, message, true))
            FeedbackViewHelper.instance.messageSendListener?.onSend(key, message)
        }
    }

    override fun onDestroy() {
        MessageViewModel.addMessage.removeObserver(addMessageObserver)
        MessageViewModel.updateMessage.removeObserver(updateMessageObserver)
        watcher.removeSoftKeyboardStateListener(listener)
        super.onDestroy()
    }
}
