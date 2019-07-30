package vip.mystery0.feedbackview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vip.mystery0.feedbackview.databinding.ItemFeedbackImageMessageBinding
import vip.mystery0.feedbackview.databinding.ItemFeedbackSystemMessageBinding
import vip.mystery0.feedbackview.databinding.ItemFeedbackTextMessageBinding
import vip.mystery0.feedbackview.model.*
import vip.mystery0.feedbackview.utils.changeLayoutParams
import vip.mystery0.feedbackview.utils.getScreenWidth
import kotlin.math.roundToInt

class FeedbackAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val maxWidth: Int = (context.getScreenWidth() * 0.7).roundToInt()

    private val glide = Glide.with(context)

    var messageList = ArrayList<BaseMessage>()
        private set

    override fun getItemViewType(position: Int): Int = messageList[position].type.code

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            Type.SYSTEM.code -> {
                val binding = DataBindingUtil.inflate<ItemFeedbackSystemMessageBinding>(layoutInflater, R.layout.item_feedback_system_message, parent, false)
                SystemViewHolder(binding.root)
            }
            Type.TEXT.code -> {
                val binding = DataBindingUtil.inflate<ItemFeedbackTextMessageBinding>(layoutInflater, R.layout.item_feedback_text_message, parent, false)
                TextViewHolder(binding.root)
            }
            Type.IMAGE.code -> {
                val binding = DataBindingUtil.inflate<ItemFeedbackImageMessageBinding>(layoutInflater, R.layout.item_feedback_image_message, parent, false)
                ImageViewHolder(binding.root)
            }
            else -> throw Exception("类型错误")
        }
    }

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        when (holder) {
            is SystemViewHolder -> {
                val systemMessage = message as SystemMessage
                val binding = DataBindingUtil.getBinding<ItemFeedbackSystemMessageBinding>(holder.itemView)!!
                binding.systemMessage = systemMessage
            }
            is TextViewHolder -> {
                val textMessage = message as TextMessage
                val binding = DataBindingUtil.getBinding<ItemFeedbackTextMessageBinding>(holder.itemView)!!
                binding.textMessage = textMessage
                binding.receiveTextView.maxWidth = maxWidth
                binding.sendTextView.maxWidth = maxWidth
                when (textMessage.messageType) {
                    MessageType.SEND -> {
                        binding.sendLayout.visibility = View.VISIBLE
                        binding.receiveLayout.visibility = View.GONE
                        binding.sendProgressBar.visibility = if (textMessage.state) View.GONE else View.VISIBLE
                    }
                    MessageType.RECEIVE -> {
                        binding.sendLayout.visibility = View.GONE
                        binding.receiveLayout.visibility = View.VISIBLE
                        binding.receiveProgressBar.visibility = if (textMessage.state) View.GONE else View.VISIBLE
                    }
                    else -> {
                    }
                }
            }
            is ImageViewHolder -> {
                val imageMessage = message as ImageMessage
                val binding = DataBindingUtil.getBinding<ItemFeedbackImageMessageBinding>(holder.itemView)!!
                binding.receiveImageView.maxWidth = maxWidth
                binding.sendImageView.maxWidth = maxWidth
                when (imageMessage.messageType) {
                    MessageType.SEND -> {
                        binding.sendLayout.visibility = View.VISIBLE
                        binding.receiveLayout.visibility = View.GONE
                        binding.sendProgressBar.visibility = if (imageMessage.state) View.GONE else View.VISIBLE
                        glide.load(imageMessage.imageUrl).into(binding.sendImageView)
                    }
                    MessageType.RECEIVE -> {
                        binding.sendLayout.visibility = View.GONE
                        binding.receiveLayout.visibility = View.VISIBLE
                        binding.receiveProgressBar.visibility = if (imageMessage.state) View.GONE else View.VISIBLE
                        glide.load(imageMessage.imageUrl).into(binding.receiveImageView)
                    }
                    else -> {
                    }
                }
            }
        }
    }

    class SystemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}