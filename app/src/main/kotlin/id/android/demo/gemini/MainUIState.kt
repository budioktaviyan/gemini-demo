package id.android.demo.gemini

import androidx.compose.runtime.toMutableStateList
import java.util.UUID

enum class Participant {
  USER,
  MODEL,
  ERROR
}

data class Chat(
  val id: String = UUID.randomUUID().toString(),
  var text: String = "",
  val participant: Participant = Participant.USER,
  var isPending: Boolean = false
)

class MainUIState(
  messages: List<Chat> = emptyList()
) {

  private val mMessages = messages.toMutableStateList()
  val messages = mMessages

  fun addMessage(message: Chat) {
    mMessages.add(message)
  }

  fun replaceLastPendingMessage() {
    val lastMessage = mMessages.lastOrNull()
    lastMessage?.let { message ->
      val newMessage = message.apply {
        isPending = false
      }
      mMessages.removeLast()
      mMessages.add(newMessage)
    }
  }
}