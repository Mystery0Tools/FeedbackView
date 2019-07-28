package vip.mystery0.feedbackview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vip.mystery0.feedbackview.model.MessageType
import vip.mystery0.feedbackview.model.TextMessage

class FeedbackView : LinearLayout {
    private val TAG = "FeedbackView"
    private val inflate: View = View.inflate(context, R.layout.layout_feedback_view, this)
    private val recyclerView = inflate.findViewById<RecyclerView>(R.id.recyclerView)
    private val emojiButton = inflate.findViewById<ImageView>(R.id.emojiButton)
    private val annexButton = inflate.findViewById<ImageView>(R.id.annexButton)
    private val editText = inflate.findViewById<EditText>(R.id.inputEditText)
    private val adapter = FeedbackAdapter(context)
    private var isSend = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        emojiButton.setOnClickListener {
            Log.i(TAG, "点击表情按钮")
        }
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                isSend = if (p0 != null && p0.isNotEmpty()) {
                    annexButton.setImageResource(R.drawable.ic_send)
                    true
                } else {
                    annexButton.setImageResource(R.drawable.ic_annex)
                    false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        annexButton.setOnClickListener {
            if (isSend) {
                adapter.messageList.add(TextMessage(1, MessageType.SEND, editText.text.toString()))
            } else {
                Log.i(TAG, "显示附件面板")
            }
        }
    }
}
