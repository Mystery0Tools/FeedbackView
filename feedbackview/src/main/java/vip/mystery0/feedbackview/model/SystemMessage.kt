package vip.mystery0.feedbackview.model

import java.io.Serializable

class SystemMessage(var text: String) : BaseMessage(MessageType.SYSTEM, Type.SYSTEM, true), Serializable