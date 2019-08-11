package vip.mystery0.feedbackview.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_feedback.*
import vip.mystery0.feedbackview.R
import vip.mystery0.feedbackview.helper.FeedbackViewHelper
import vip.mystery0.feedbackview.listener.DoSelectListener
import vip.mystery0.feedbackview.model.BaseMessage
import vip.mystery0.feedbackview.model.TextMessage
import vip.mystery0.feedbackview.ui.SoftKeyboardStateWatcher
import vip.mystery0.feedbackview.viewModel.FeedbackViewModel
import vip.mystery0.feedbackview.viewModel.MessageViewModel
import vip.mystery0.tools.model.Pair3
import vip.mystery0.tools.utils.sha256
import java.util.*

class FeedbackActivity : AppCompatActivity() {
    companion object {
        private const val IMAGE_SELECT_CODE = 11
        private const val FILE_SELECT_CODE = 22
    }

    private val feedbackViewModel: FeedbackViewModel by lazy { ViewModelProvider(this)[FeedbackViewModel::class.java] }
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
        feedbackView.updateMessage(save)
        if (it.third)
            feedbackViewModel.map.remove(it.first)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        MessageViewModel.addMessage.observeForever(addMessageObserver)
        MessageViewModel.updateMessage.observeForever(updateMessageObserver)
        watcher = SoftKeyboardStateWatcher(feedbackView)
        watcher.addSoftKeyboardStateListener(listener)
        FeedbackViewHelper.instance.doSelectListener = object : DoSelectListener {
            override fun selectImage() {
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                startActivityForResult(intent, IMAGE_SELECT_CODE)
            }

            override fun selectFile() {
            }
        }
        feedbackView.onSendListener {
            val key = Calendar.getInstance().time.toString().sha256()
            val message = TextMessage.send(it)
            MessageViewModel.addMessage.postValue(Pair3(key, message, true))
            FeedbackViewHelper.instance.messageSendListener?.onSend(key, message)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_SELECT_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    FeedbackViewHelper.instance.receiveImageUri(data?.data!!)
                }
            }
            FILE_SELECT_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    FeedbackViewHelper.instance.receiveFileUri(data?.data!!)
                }
            }
        }
    }

    override fun onDestroy() {
        MessageViewModel.addMessage.removeObserver(addMessageObserver)
        MessageViewModel.updateMessage.removeObserver(updateMessageObserver)
        watcher.removeSoftKeyboardStateListener(listener)
        super.onDestroy()
    }
}
