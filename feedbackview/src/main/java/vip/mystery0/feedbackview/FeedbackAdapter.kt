package vip.mystery0.feedbackview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vip.mystery0.feedbackview.databinding.ItemFeedbackTextMessageBinding
import vip.mystery0.feedbackview.model.BaseMessage
import vip.mystery0.feedbackview.model.MessageType
import vip.mystery0.feedbackview.model.TextMessage
import vip.mystery0.feedbackview.model.Type

class FeedbackAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val glide = Glide.with(context)

    var messageList = ObservableArrayList<BaseMessage>()
        private set

    private val itemsChangeCallback = ListChangedCallback()

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
                        binding.sendTextView.visibility = View.VISIBLE
                        binding.receiveTextView.visibility = View.GONE
                    }
                    MessageType.RECEIVE -> {
                        binding.sendTextView.visibility = View.GONE
                        binding.receiveTextView.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.messageList.addOnListChangedCallback(itemsChangeCallback)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.messageList.removeOnListChangedCallback(itemsChangeCallback)
    }

    //region 处理数据集变化
    fun onChanged(newItems: ObservableArrayList<BaseMessage>) {
        resetItems(newItems)
        notifyDataSetChanged()
    }

    fun onItemRangeChanged(newItems: ObservableArrayList<BaseMessage>, positionStart: Int, itemCount: Int) {
        resetItems(newItems)
        notifyItemRangeChanged(positionStart, itemCount)
    }

    fun onItemRangeInserted(newItems: ObservableArrayList<BaseMessage>, positionStart: Int, itemCount: Int) {
        resetItems(newItems)
        notifyItemRangeInserted(positionStart, itemCount)
    }

    fun onItemRangeMoved(newItems: ObservableArrayList<BaseMessage>) {
        resetItems(newItems)
        notifyDataSetChanged()
    }

    fun onItemRangeRemoved(newItems: ObservableArrayList<BaseMessage>, positionStart: Int, itemCount: Int) {
        resetItems(newItems)
        notifyItemRangeRemoved(positionStart, itemCount)
    }

    private fun resetItems(newItems: ObservableArrayList<BaseMessage>) {
        this.messageList = newItems
    }

    inner class ListChangedCallback : ObservableList.OnListChangedCallback<ObservableArrayList<BaseMessage>>() {
        override fun onChanged(newItems: ObservableArrayList<BaseMessage>) {
            this@FeedbackAdapter.onChanged(newItems)
        }

        override fun onItemRangeChanged(newItems: ObservableArrayList<BaseMessage>, i: Int, itemCount: Int) {
            this@FeedbackAdapter.onItemRangeChanged(newItems, i, itemCount)
        }

        override fun onItemRangeInserted(newItems: ObservableArrayList<BaseMessage>, i: Int, itemCount: Int) {
            this@FeedbackAdapter.onItemRangeInserted(newItems, i, itemCount)
        }

        override fun onItemRangeMoved(newItems: ObservableArrayList<BaseMessage>, i: Int, i1: Int, itemCount: Int) {
            this@FeedbackAdapter.onItemRangeMoved(newItems)
        }

        override fun onItemRangeRemoved(sender: ObservableArrayList<BaseMessage>, positionStart: Int, itemCount: Int) {
            this@FeedbackAdapter.onItemRangeRemoved(sender, positionStart, itemCount)
        }
    }
}