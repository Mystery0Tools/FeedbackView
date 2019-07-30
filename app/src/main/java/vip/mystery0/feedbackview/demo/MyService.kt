package vip.mystery0.feedbackview.demo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import vip.mystery0.feedbackview.FeedbackViewHelper
import vip.mystery0.feedbackview.model.TextMessage
import java.util.*

class MyService : Service() {

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        Thread {
            (0..5).forEach {
                val message = TextMessage.receive("模拟接收到的消息，时间：${Date().toLocaleString()}，编号：$it")
                val key = FeedbackViewHelper.instance.add(message, clearInput = false)
                Thread.sleep(3000)
                message.state = true
                FeedbackViewHelper.instance.update(key, message)
            }
        }.start()
    }
}
