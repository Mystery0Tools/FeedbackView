package vip.mystery0.feedbackview.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jeremyliao.liveeventbus.core.Observable
import kotlinx.android.synthetic.main.activity_feedback.*
import vip.mystery0.feedbackview.R
import vip.mystery0.feedbackview.helper.FeedbackViewHelper
import vip.mystery0.feedbackview.listener.DoSelectListener
import vip.mystery0.feedbackview.model.EVENT_BUS_MESSAGE_ADD
import vip.mystery0.feedbackview.model.EVENT_BUS_MESSAGE_UPDATE
import vip.mystery0.feedbackview.model.EventBusMessageBean
import vip.mystery0.feedbackview.model.TextMessage
import vip.mystery0.feedbackview.ui.SoftKeyboardStateWatcher

fun eventBusMessageAdd(): Observable<EventBusMessageBean> = LiveEventBus.get(EVENT_BUS_MESSAGE_ADD, EventBusMessageBean::class.java)
fun eventBusMessageUpdate(): Observable<EventBusMessageBean> = LiveEventBus.get(EVENT_BUS_MESSAGE_UPDATE, EventBusMessageBean::class.java)
fun EventBusMessageBean.postAdd() = eventBusMessageAdd().post(this)
fun EventBusMessageBean.postUpdate() = eventBusMessageUpdate().post(this)

class FeedbackActivity : AppCompatActivity() {
    companion object {
        private const val IMAGE_SELECT_CODE = 11
        private const val FILE_SELECT_CODE = 22
    }

    private lateinit var watcher: SoftKeyboardStateWatcher
    private val listener = object : SoftKeyboardStateWatcher.SoftKeyboardStateListener {
        override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
            feedbackView.scrollToBottom()
        }

        override fun onSoftKeyboardClosed() {
            feedbackView.scrollToBottom()
        }
    }

    private val addMessageObserver = Observer<EventBusMessageBean> {
        feedbackView.addMessage(it.message, it.clearInput)
        feedbackView.scrollToBottom()
    }

    private val updateMessageObserver = Observer<EventBusMessageBean> {
        feedbackView.updateMessage(it.message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        eventBusMessageAdd().observe(this, addMessageObserver)
        eventBusMessageUpdate().observe(this, updateMessageObserver)
        watcher = SoftKeyboardStateWatcher(feedbackView)
        watcher.addSoftKeyboardStateListener(listener)
        FeedbackViewHelper.instance.doSelectListener = object : DoSelectListener {
            override fun selectImage() {
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                startActivityForResult(intent, IMAGE_SELECT_CODE)
            }

            override fun selectFile() {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "*/*"
                startActivityForResult(intent, FILE_SELECT_CODE)
            }
        }
        feedbackView.onSendListener {
            val message = TextMessage.send(it)
            EventBusMessageBean(message, true).postAdd()
            FeedbackViewHelper.instance.messageSendListener?.onSend( message)
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
        watcher.removeSoftKeyboardStateListener(listener)
        super.onDestroy()
    }
}
