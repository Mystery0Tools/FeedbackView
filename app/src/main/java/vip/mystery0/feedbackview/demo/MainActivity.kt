package vip.mystery0.feedbackview.demo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import vip.mystery0.feedbackview.helper.FeedbackViewHelper
import vip.mystery0.feedbackview.listener.DoDownloadListener
import vip.mystery0.feedbackview.listener.DoUploadListener
import vip.mystery0.feedbackview.listener.MessageSendListener
import vip.mystery0.feedbackview.model.*
import java.io.File
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FeedbackViewHelper.init(this)
        FeedbackViewHelper.instance.messageSendListener = object : MessageSendListener {
            override fun onSend(key: String, baseMessage: BaseMessage) {
                Thread {
                    Thread.sleep(Random.nextLong(2000, 5000))
                    baseMessage.state = true
                    if (baseMessage.type == Type.TEXT) {
                        val text = baseMessage as TextMessage
                        text.text = "模拟修改消息，原文是：「${text.text}」"
                    }
                    FeedbackViewHelper.instance.update(key, baseMessage)
                }.start()
            }
        }
        FeedbackViewHelper.instance.doUploadListener = object : DoUploadListener {
            override fun doUpload(file: File, info: UploadInfo): String {
                var progres = 0
                while (progres < 100) {
                    info.imageMessage.progress = progres
                    FeedbackViewHelper.instance.updateProgress(info)
                    Thread.sleep(500)
                    progres += 10
                }
                Thread.sleep(100)
                return "https://www.google.com/url?sa=i&source=images&cd=&ved=2ahUKEwiC_YG7n_rjAhWEQN4KHcvoCWsQjRx6BAgBEAQ&url=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FTerra_Ceia%2C_Florida&psig=AOvVaw3_1EWy9eqOmFeEr1tx23bX&ust=1565593243377080"
            }
        }
        FeedbackViewHelper.instance.doDownloadListener = object : DoDownloadListener {
            override fun doDownload(url: String, localFile: File, info: DownloadInfo) {

            }
        }
        button.setOnClickListener {
            FeedbackViewHelper.instance.start()
            start()
        }
    }

    private fun start() {
        val intent = Intent(this, MyService::class.java)
        startService(intent)
    }

    private fun stop() {
        val intent = Intent(this, MyService::class.java)
        stopService(intent)
    }
}
