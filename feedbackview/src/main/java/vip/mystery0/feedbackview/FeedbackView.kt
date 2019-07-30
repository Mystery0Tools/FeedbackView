package vip.mystery0.feedbackview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vip.mystery0.feedbackview.model.BaseMessage

class FeedbackView : LinearLayout {
    private val TAG = "FeedbackView"
    private val inflate: View = View.inflate(context, R.layout.layout_feedback_view, this)
    private val recyclerView = inflate.findViewById<RecyclerView>(R.id.recyclerView)
    private val annexButton = inflate.findViewById<ImageView>(R.id.annexButton)
    private val sendButton = inflate.findViewById<ImageView>(R.id.sendButton)
    private val editText = inflate.findViewById<EditText>(R.id.inputEditText)
    private val adapter = FeedbackAdapter(context)

    private var sendListener: ((String) -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        annexButton.setOnClickListener {
            Log.i(TAG, "点击了附件按钮")
        }
        sendButton.setOnClickListener {
            doSend()
        }
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND)
                doSend()
            true
        }
    }

    fun onSendListener(listener: (String) -> Unit) {
        sendListener = listener
    }

    private fun doSend() {
        val message = editText.text.toString()
        if (message.isEmpty())
            return
        if (sendListener == null) {
            Log.w(TAG, "发送监听为空！")
            Log.w(TAG, "发送的消息为：$message")
            return
        }
        sendListener!!(message)
    }

    @Synchronized
    fun lastMessageId(): Long = adapter.messageList.lastOrNull()?.id ?: -1

    fun addAllMessage(messageList: List<BaseMessage>, clearInput: Boolean = false) {
        val startIndex = adapter.messageList.size
        adapter.messageList.addAll(messageList)
        adapter.notifyItemRangeInserted(startIndex, messageList.size)
        if (clearInput)
            editText.text = null
    }

    fun addMessage(message: BaseMessage, clearInput: Boolean = true) {
        adapter.messageList.add(message)
        adapter.notifyItemInserted(adapter.messageList.lastIndex)
        if (clearInput)
            editText.text = null
    }

    fun updateMessage(message: BaseMessage, clearInput: Boolean = false) {
        val msg = adapter.messageList.find { message.id == it.id }
        if (msg == null) {
            Log.w(TAG, "消息不存在")
            return
        }
        msg.copyFrom(message)
        adapter.notifyItemChanged(adapter.messageList.indexOf(msg))
        if (clearInput)
            editText.text = null
    }

    fun scrollToTop() {
        recyclerView.scrollToPosition(0)
    }

    fun scrollToBottom() {
        recyclerView.scrollToPosition(adapter.itemCount - 1)
    }
}
