package vip.mystery0.feedbackview.demo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import vip.mystery0.feedbackview.helper.FeedbackViewHelper
import vip.mystery0.feedbackview.model.TextMessage
import java.util.*

class MyService : Service() {

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        Thread {
            (0..5).forEach {
                val message = TextMessage.receive("模拟接收到的消息，时间：${Date().toLocaleString()}，编号：$it")
                FeedbackViewHelper.instance.add(message, clearInput = false)
                Thread.sleep(1000)
                message.state = true
                FeedbackViewHelper.instance.update(message)
            }
        }.start()
    }
}
