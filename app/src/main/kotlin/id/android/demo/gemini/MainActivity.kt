package id.android.demo.gemini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import id.android.demo.gemini.ui.theme.GeminiDemoTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      GeminiDemoTheme {
        val viewModel by viewModels<MainViewModel>()

        val mainUIState by viewModel.uiState.collectAsState()
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
          bottomBar = {
            MessageInput(
              onSendMessage = { inputText ->
                viewModel.sendMessage(inputText)
              },
              resetScroll = {
                coroutineScope.launch {
                  listState.scrollToItem(0)
                }
              }
            )
          }
        ) { paddingValues ->
          Column(
            modifier = Modifier
              .padding(paddingValues)
              .fillMaxSize()
          ) {
            ChatList(
              messages = mainUIState.messages,
              listState = listState
            )
          }
        }
      }
    }
  }
}