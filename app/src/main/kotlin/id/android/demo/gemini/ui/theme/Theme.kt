package id.android.demo.gemini.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primaryContainer = Blue80,
  secondaryContainer = LightBlue80,
  tertiaryContainer = Green80,
  background = Color.Black
)

private val LightColorScheme = lightColorScheme(
  primaryContainer = Blue40,
  secondaryContainer = LightBlue40,
  tertiaryContainer = Green40,
  background = Color.White
)

@Composable
fun GeminiDemoTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme = when {
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}