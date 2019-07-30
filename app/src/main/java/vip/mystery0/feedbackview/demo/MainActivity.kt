package vip.mystery0.feedbackview.demo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import vip.mystery0.feedbackview.FeedbackViewHelper
import vip.mystery0.feedbackview.listener.MessageSendListener
import vip.mystery0.feedbackview.model.BaseMessage
import vip.mystery0.feedbackview.model.TextMessage
import vip.mystery0.feedbackview.model.Type
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
