package french.learning.app.learn.frenchlearningapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DuolingoGreen,
    secondary = SkyBlue,
    tertiary = WarmYellow
)

private val LightColorScheme = lightColorScheme(
    primary = DuolingoGreen,
    secondary = SkyBlue,
    tertiary = WarmYellow,
    surface = SurfaceWhite,
    onSurface = TextDark
)

@Composable
fun FrenchLearningAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
