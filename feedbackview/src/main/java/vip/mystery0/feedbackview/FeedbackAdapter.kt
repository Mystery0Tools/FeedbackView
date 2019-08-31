package vip.mystery0.feedbackview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import vip.mystery0.feedbackview.databinding.ItemFeedbackFileMessageBinding
import vip.mystery0.feedbackview.databinding.ItemFeedbackImageMessageBinding
import vip.mystery0.feedbackview.databinding.ItemFeedbackSystemMessageBinding
import vip.mystery0.feedbackview.databinding.ItemFeedbackTextMessageBinding
import vip.mystery0.feedbackview.model.*
import vip.mystery0.feedbackview.utils.changeLayoutParams
import vip.mystery0.tools.getTColor
import vip.mystery0.tools.utils.getScreenWidth
import kotlin.math.roundToInt

class FeedbackAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = "FeedbackAdapter"
    private val maxWidth: Int = (getScreenWidth() * 0.7).roundToInt()

    private val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)

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
            Type.FILE.code -> {
                val binding = DataBindingUtil.inflate<ItemFeedbackFileMessageBinding>(layoutInflater, R.layout.item_feedback_file_message, parent, false)
                FileViewHolder(binding.root)
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
                when (imageMessage.messageType) {
                    MessageType.SEND -> {
                        binding.imageView.changeLayoutParams {
                            val params = it as ConstraintLayout.LayoutParams
                            params.horizontalBias = 1F
                            params
                        }
                        binding.imageView.setBackgroundColor(context.getTColor(R.color.sendColor))
                        binding.guideLineStart.setGuidelinePercent(0.3F)
                        binding.guideLineEnd.setGuidelinePercent(1F)
                        when {
                            imageMessage.error != null -> {
                                //出现错误
                                Log.w(TAG, imageMessage.error!!)
                                //显示错误的图标
                                binding.imageView.setImageResource(R.drawable.ic_image_load_failed)
                                binding.circlePercent.visibility = View.GONE
                            }
                            imageMessage.state -> {
                                //发送步骤完成
                                binding.circlePercent.updateProgress(100)
                                binding.circlePercent.visibility = View.GONE
                                glide.applyDefaultRequestOptions(requestOptions).load(imageMessage.localFile).into(binding.imageView)
                            }
                            !imageMessage.state -> {
                                //发送步骤未完成
                                binding.circlePercent.visibility = View.VISIBLE
                                binding.circlePercent.updateProgress(imageMessage.progress)
                                glide.applyDefaultRequestOptions(requestOptions).load(imageMessage.localFile).into(binding.imageView)
                            }
                        }
                    }
                    MessageType.RECEIVE -> {
                        binding.imageView.changeLayoutParams {
                            val params = it as ConstraintLayout.LayoutParams
                            params.horizontalBias = 0F
                            params
                        }
                        binding.imageView.setBackgroundColor(context.getTColor(R.color.receiveColor))
                        binding.guideLineStart.setGuidelinePercent(0F)
                        binding.guideLineEnd.setGuidelinePercent(0.7F)
                        when {
                            imageMessage.error != null -> {
                                //出现错误
                                Log.w(TAG, imageMessage.error!!)
                                //显示错误的图标
                                binding.imageView.setImageResource(R.drawable.ic_image_load_failed)
                                binding.circlePercent.visibility = View.GONE
                            }
                            imageMessage.state -> {
                                //发送步骤完成
                                binding.circlePercent.updateProgress(100)
                                binding.circlePercent.visibility = View.GONE
                                glide.applyDefaultRequestOptions(requestOptions).load(imageMessage.localFile).into(binding.imageView)
                            }
                            !imageMessage.state -> {
                                //发送步骤未完成
                                binding.circlePercent.visibility = View.VISIBLE
                                binding.circlePercent.updateProgress(imageMessage.progress)
                                glide.applyDefaultRequestOptions(requestOptions).load(imageMessage.localFile).into(binding.imageView)
                            }
                        }
                    }
                    else -> {
                    }
                }
            }
            is FileViewHolder -> {
                val fileMessage = message as FileMessage
                Log.i(TAG, "update: ${fileMessage.progress}")
                val binding = DataBindingUtil.getBinding<ItemFeedbackFileMessageBinding>(holder.itemView)!!
                when (fileMessage.messageType) {
                    MessageType.SEND -> {
                        binding.sendLayout.visibility = View.VISIBLE
                        binding.receiveLayout.visibility = View.GONE
                        //判断发送是否完成
                        if (fileMessage.state) {
                            //发送步骤完成
                            binding.sendProgressBar.visibility = View.GONE
                            if (fileMessage.error != null) {
                                //出现错误
                                Log.w(TAG, fileMessage.error!!)
                                //显示错误的图标
                            }
                        } else {
                            //发送步骤未完成
                            binding.sendProgressBar.visibility = View.VISIBLE
                            binding.sendProgressBar.progress = fileMessage.progress
                        }
                    }
                    MessageType.RECEIVE -> {
                        binding.sendLayout.visibility = View.GONE
                        binding.receiveLayout.visibility = View.VISIBLE
                        //判断接收是否完成
                        if (fileMessage.state) {
                            //接收步骤完成
                            binding.receiveProgressBar.visibility = View.GONE
                            if (fileMessage.error != null) {
                                //出现错误
                                Log.w(TAG, fileMessage.error!!)
                                //显示错误的图标
                            }
                        } else {
                            //接收步骤未完成
                            binding.receiveProgressBar.visibility = View.VISIBLE
                            binding.receiveProgressBar.progress = fileMessage.progress
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

    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}