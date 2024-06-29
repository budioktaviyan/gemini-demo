package id.android.demo.gemini.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = Purple80,
  secondary = PurpleGrey80,
  tertiary = Pink80,
  background = Color.Black
)

private val LightColorScheme = lightColorScheme(
  primary = Purple40,
  secondary = PurpleGrey40,
  tertiary = Pink40,
  background = Color.White
)

@Composable
fun GeminiDemoTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit
) {
  val colorScheme = when {
    dynamicColor -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}