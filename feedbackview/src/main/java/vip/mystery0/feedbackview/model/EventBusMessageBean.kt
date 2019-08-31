package vip.mystery0.feedbackview.model

data class EventBusMessageBean(
    val message: BaseMessage,
    val clearInput: Boolean
)

const val EVENT_BUS_MESSAGE_ADD = "EVENT_BUS_MESSAGE_ADD"
const val EVENT_BUS_MESSAGE_UPDATE = "EVENT_BUS_MESSAGE_UPDATE"