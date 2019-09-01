package vip.mystery0.feedbackview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import vip.mystery0.feedbackview.databinding.ItemFeedbackFileMessageBinding
import vip.mystery0.feedbackview.databinding.ItemFeedbackImageMessageBinding
import vip.mystery0.feedbackview.databinding.ItemFeedbackSystemMessageBinding
import vip.mystery0.feedbackview.databinding.ItemFeedbackTextMessageBinding
import vip.mystery0.feedbackview.model.*
import vip.mystery0.feedbackview.utils.changeLayoutParams
import vip.mystery0.tools.getTColor
import vip.mystery0.tools.utils.DensityTools
import vip.mystery0.tools.utils.StringTools
import vip.mystery0.tools.utils.getFormatFileSize
import vip.mystery0.tools.utils.getScreenWidth
import kotlin.math.abs
import kotlin.math.roundToInt

class FeedbackAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = "FeedbackAdapter"
    private val maxWidth: Int = (getScreenWidth() * 0.7).roundToInt()

    var messageList = ArrayList<BaseMessage>()
        private set

    private val map = HashMap<String, Bitmap>()

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
                        binding.imageView.backgroundTintList = ColorStateList.valueOf(context.getTColor(R.color.sendColor))
                        binding.guideLineStart.setGuidelinePercent(0.3F)
                        binding.guideLineEnd.setGuidelinePercent(1F)
                        when {
                            imageMessage.error != null -> {
                                //出现错误
                                Log.w(TAG, imageMessage.error!!)
                                //显示错误的图标
                                binding.imageView.load(R.drawable.ic_image_load_failed)
                                binding.circlePercent.visibility = View.GONE
                            }
                            imageMessage.state -> {
                                //发送步骤完成
                                binding.circlePercent.updateProgress(100)
                                binding.circlePercent.visibility = View.GONE
                                binding.imageView.load(imageMessage.localFile)
                            }
                            !imageMessage.state -> {
                                //发送步骤未完成
                                binding.circlePercent.visibility = View.VISIBLE
                                binding.circlePercent.updateProgress(imageMessage.progress)
                                binding.imageView.load(imageMessage.localFile)
                            }
                        }
                    }
                    MessageType.RECEIVE -> {
                        binding.imageView.changeLayoutParams {
                            val params = it as ConstraintLayout.LayoutParams
                            params.horizontalBias = 0F
                            params
                        }
                        binding.imageView.backgroundTintList = ColorStateList.valueOf(context.getTColor(R.color.receiveColor))
                        binding.guideLineStart.setGuidelinePercent(0F)
                        binding.guideLineEnd.setGuidelinePercent(0.7F)
                        when {
                            imageMessage.error != null -> {
                                //出现错误
                                Log.w(TAG, imageMessage.error!!)
                                //显示错误的图标
                                binding.imageView.load(R.drawable.ic_image_load_failed)
                                binding.circlePercent.visibility = View.GONE
                            }
                            imageMessage.state -> {
                                //接收步骤完成
                                binding.circlePercent.updateProgress(100)
                                binding.circlePercent.visibility = View.GONE
                                binding.imageView.load(imageMessage.localFile)
                            }
                            !imageMessage.state -> {
                                //接收步骤未完成
                                binding.circlePercent.visibility = View.VISIBLE
                                binding.circlePercent.updateProgress(imageMessage.progress)
                                binding.imageView.load(R.drawable.ic_image_load_failed)
                            }
                        }
                    }
                    else -> {
                    }
                }
            }
            is FileViewHolder -> {
                val fileMessage = message as FileMessage
                val binding = DataBindingUtil.getBinding<ItemFeedbackFileMessageBinding>(holder.itemView)!!
                when (fileMessage.messageType) {
                    MessageType.SEND -> {
                        binding.parentLayout.backgroundTintList = ColorStateList.valueOf(context.getTColor(R.color.sendColor))
                        binding.guideLineStart.setGuidelinePercent(0.3F)
                        binding.guideLineEnd.setGuidelinePercent(1F)
                        binding.fileTitle.text = fileMessage.fileTitle
                        binding.fileDetail.text = fileMessage.fileSize.getFormatFileSize(2)
                        when {
                            fileMessage.error != null -> {
                                //出现错误
                                Log.w(TAG, fileMessage.error!!)
                                //显示错误的图标
                                binding.imageView.load(R.drawable.ic_image_load_failed)
                                binding.circlePercent.visibility = View.GONE
                            }
                            fileMessage.state -> {
                                //发送步骤完成
                                binding.circlePercent.updateProgress(100)
                                binding.circlePercent.visibility = View.GONE
                                if (fileMessage.fileTitle!!.contains('.'))
                                    binding.imageView.setImageBitmap(getBitmap(fileMessage.fileTitle!!.substringAfterLast('.')))
                                else
                                    binding.imageView.setImageBitmap(getBitmap(""))
                            }
                            !fileMessage.state -> {
                                //发送步骤未完成
                                binding.circlePercent.visibility = View.VISIBLE
                                binding.circlePercent.updateProgress(fileMessage.progress)
                                if (fileMessage.fileTitle!!.contains('.'))
                                    binding.imageView.setImageBitmap(getBitmap(fileMessage.fileTitle!!.substringAfterLast('.')))
                                else
                                    binding.imageView.setImageBitmap(getBitmap(""))
                            }
                        }
                    }
                    MessageType.RECEIVE -> {
                        binding.parentLayout.backgroundTintList = ColorStateList.valueOf(context.getTColor(R.color.receiveColor))
                        binding.guideLineStart.setGuidelinePercent(0F)
                        binding.guideLineEnd.setGuidelinePercent(0.7F)
                        binding.fileTitle.text = fileMessage.fileTitle
                        binding.fileDetail.text = fileMessage.fileSize.getFormatFileSize(2)
                        when {
                            fileMessage.error != null -> {
                                //出现错误
                                Log.w(TAG, fileMessage.error!!)
                                //显示错误的图标
                                binding.imageView.load(R.drawable.ic_image_load_failed)
                                binding.circlePercent.visibility = View.GONE
                            }
                            fileMessage.state -> {
                                //发送步骤完成
                                binding.circlePercent.updateProgress(100)
                                binding.circlePercent.visibility = View.GONE
                                if (fileMessage.fileTitle!!.contains('.'))
                                    binding.imageView.setImageBitmap(getBitmap(fileMessage.fileTitle!!.substringAfterLast('.')))
                                else
                                    binding.imageView.setImageBitmap(getBitmap(""))
                            }
                            !fileMessage.state -> {
                                //发送步骤未完成
                                binding.circlePercent.visibility = View.VISIBLE
                                binding.circlePercent.updateProgress(fileMessage.progress)
                                if (fileMessage.fileTitle!!.contains('.'))
                                    binding.imageView.setImageBitmap(getBitmap(fileMessage.fileTitle!!.substringAfterLast('.')))
                                else
                                    binding.imageView.setImageBitmap(getBitmap(""))
                            }
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

    private val backgroundColor = intArrayOf(
        Color.parseColor("#34d960"),
        Color.parseColor("#f76710"),
        Color.parseColor("#e5594a"),
        Color.parseColor("#a94ae4"),
        Color.parseColor("#a7d41f"),
        Color.parseColor("#0eccff"),
        Color.parseColor("#a7d41f"),
        Color.parseColor("#008ddf"),
        Color.parseColor("#00deab"),
        Color.parseColor("#e551cd")
    )

    private fun getBitmap(extensionString: String): Bitmap {
        if (map[extensionString] != null)
            return map[extensionString]!!
        val bitmap = generateBitmap(extensionString)
        map[extensionString] = bitmap
        return bitmap
    }

    private fun generateBitmap(extensionString: String): Bitmap {
        val extension = if (extensionString != "") extensionString else "file"
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.color = backgroundColor[StringTools.instance.md5(extension)[0].toInt() % backgroundColor.size]
        canvas.drawCircle(50F, 50F, 50F, paint)
        canvas.translate(50F, 50F)
        paint.textSize = DensityTools.instance.dp2px(10)
        paint.color = Color.WHITE
        val textWidth = paint.measureText(extension)
        val baseLineY = abs((paint.ascent() + paint.descent()) / 2)
        canvas.drawText(extension, -textWidth / 2, baseLineY, paint)
        return bitmap
    }
}