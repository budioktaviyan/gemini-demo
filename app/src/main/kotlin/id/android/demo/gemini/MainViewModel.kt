package id.android.demo.gemini

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.asTextOrNull
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

  private val generativeModel = GenerativeModel(
    modelName = "gemini-1.5-flash-latest",
    apiKey = BuildConfig.geminiApiKey,
    generationConfig = generationConfig {
      temperature = 0.7f
      topK = 32
      topP = 1f
      maxOutputTokens = 8192
    },
    safetySettings = listOf(
      SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE),
      SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE),
      SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.MEDIUM_AND_ABOVE),
      SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.MEDIUM_AND_ABOVE)
    )
  )

  private val chat = generativeModel.startChat(
    history = listOf(
      content(role = Participant.MODEL.name) {
        text("Hi! Anything I can help with?")
      }
    )
  )

  private val mUiState = MutableStateFlow(
    MainUIState(
      chat.history.map { content ->
        Chat(
          text = content.parts.first().asTextOrNull() ?: "",
          participant = if (content.role.equals("user")) Participant.USER else Participant.MODEL,
          isPending = false
        )
      }
    )
  )
  val uiState = mUiState.asStateFlow()

  fun sendMessage(message: String) {
    mUiState.value.addMessage(
      Chat(
        text = message,
        participant = Participant.USER,
        isPending = true
      )
    )

    viewModelScope.launch {
      try {
        val outputMessage = Chat(
          participant = Participant.MODEL,
          isPending = true
        )
        mUiState.value.replaceLastPendingMessage()
        mUiState.value.addMessage(outputMessage)

        chat.sendMessageStream(message).collect { response ->
          outputMessage.isPending = false
          outputMessage.text = outputMessage.text.plus(response.text)
          mUiState.value.replaceLastPendingMessage()
        }
      } catch (error: Exception) {
        mUiState.value.replaceLastPendingMessage()
        mUiState.value.addMessage(
          Chat(
            text = error.localizedMessage ?: "Unknown Error!",
            participant = Participant.ERROR
          )
        )
      }
    }
  }
}