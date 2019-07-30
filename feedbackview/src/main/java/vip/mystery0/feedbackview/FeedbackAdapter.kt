package vip.mystery0.feedbackview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vip.mystery0.feedbackview.databinding.ItemFeedbackTextMessageBinding
import vip.mystery0.feedbackview.messageBubble.MessageBubble
import vip.mystery0.feedbackview.messageBubble.MessageBubbleShapeDrawable
import vip.mystery0.feedbackview.model.BaseMessage
import vip.mystery0.feedbackview.model.MessageType
import vip.mystery0.feedbackview.model.TextMessage
import vip.mystery0.feedbackview.model.Type
import vip.mystery0.feedbackview.utils.dip

class FeedbackAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val bubbleShapeStart = MessageBubble(
        backgroundColor = 0x8800FF00.toInt(),
        strokeColor = 0xFFCFCFCF.toInt(),
        strokeWidth = context.dip(1F),
        arrowWidth = context.dip(6F),
        arrowMarginTop = context.dip(1F),
        arrowHeight = context.dip(12F),
        cornerRadius = context.dip(10F)
    )

    private val glide = Glide.with(context)

    var messageList = ArrayList<BaseMessage>()
        private set

    override fun getItemViewType(position: Int): Int = messageList[position].type.code

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            Type.TEXT.code -> {
                val binding = DataBindingUtil.inflate<ItemFeedbackTextMessageBinding>(layoutInflater, R.layout.item_feedback_text_message, parent, false)
                TextViewHolder(binding.root)
            }
            Type.IMAGE.code -> {
                TextViewHolder(
                    layoutInflater.inflate(
                        R.layout.item_feedback_image_message,
                        parent,
                        false
                    )
                )
            }
            else -> throw Exception("类型错误")
        }
    }

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        when (holder) {
            is TextViewHolder -> {
                val binding = DataBindingUtil.getBinding<ItemFeedbackTextMessageBinding>(holder.itemView)!!
                val textMessage = message as TextMessage
                binding.textMessage = textMessage
                when (textMessage.messageType) {
                    MessageType.SEND -> {
                        if (textMessage.drawable == null)
                            textMessage.drawable = MessageBubbleShapeDrawable.bubbleStateListDrawable(context, bubbleShapeStart.clone(), true)
                        binding.sendLayout.visibility = View.VISIBLE
                        binding.receiveLayout.visibility = View.GONE
                        binding.sendTextView.background = textMessage.drawable
                        binding.sendProgressBar.visibility = if (textMessage.state) View.GONE else View.VISIBLE
                    }
                    MessageType.RECEIVE -> {
                        if (textMessage.drawable == null)
                            textMessage.drawable = MessageBubbleShapeDrawable.bubbleStateListDrawable(context, bubbleShapeStart.clone(), false)
                        binding.sendLayout.visibility = View.GONE
                        binding.receiveLayout.visibility = View.VISIBLE
                        binding.receiveTextView.background = textMessage.drawable
                        binding.receiveProgressBar.visibility = if (textMessage.state) View.GONE else View.VISIBLE
                    }
                }
            }
        }
    }

    class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}