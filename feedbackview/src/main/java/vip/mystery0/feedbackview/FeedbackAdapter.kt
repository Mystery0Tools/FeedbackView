package vip.mystery0.feedbackview

import android.content.Context
import android.util.Log
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
import vip.mystery0.feedbackview.utils.getScreenWidth
import kotlin.math.roundToInt

class FeedbackAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = "FeedbackAdapter"
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
                        //判断发送是否完成
                        if (imageMessage.state) {
                            //发送步骤完成
                            binding.sendProgressBar.visibility = View.GONE
                            if (imageMessage.error != null) {
                                //出现错误
                                Log.w(TAG, imageMessage.error!!)
                                //显示错误的图标
                            } else {
                                //未出现错误
                                glide.load(imageMessage.localFile).into(binding.sendImageView)
                            }
                        } else {
                            //发送步骤未完成
                            binding.sendProgressBar.visibility = View.VISIBLE
                            binding.sendProgressBar.progress = imageMessage.progress
                        }
                    }
                    MessageType.RECEIVE -> {
                        binding.sendLayout.visibility = View.GONE
                        binding.receiveLayout.visibility = View.VISIBLE
                        //判断接收是否完成
                        if (imageMessage.state) {
                            //接收步骤完成
                            binding.receiveProgressBar.visibility = View.GONE
                            if (imageMessage.error != null) {
                                //出现错误
                                Log.w(TAG, imageMessage.error!!)
                                //显示错误的图标
                            } else {
                                //未出现错误
                                glide.load(imageMessage.localFile).into(binding.sendImageView)
                            }
                        } else {
                            //接收步骤未完成
                            binding.receiveProgressBar.visibility = View.VISIBLE
                            binding.receiveProgressBar.progress = imageMessage.progress
                        }
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