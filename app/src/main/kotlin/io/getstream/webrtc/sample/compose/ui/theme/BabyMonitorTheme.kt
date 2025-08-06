package io.getstream.webrtc.sample.compose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.sp
import io.getstream.webrtc.sample.compose.R


val DarkSecondaryTransparent = Color(0xB9795391) // #1DB954 with 26 hex alpha = 66 decimal = 0x42

// Light Theme Colors
val LightColors = lightColorScheme(
    primary = Color(0xFF2196F3),
    onPrimary = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.DarkGray,
    secondary = Color(0xFFE3F2FD)
)

// Dark Theme Colors
val DarkColors = darkColorScheme(
    primary = Color(0xFFBB86FC),
    onPrimary = Color.White,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF121212),
    onSurface = Color.LightGray,
    secondary = DarkSecondaryTransparent // ✅ updated
)

@Composable
fun BabyMonitorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColors else LightColors,
            typography = AppTypography,
            shapes = Shapes(),
            content = content
        )
}
@Composable
fun appColorScheme(): ColorScheme {
    return if (isSystemInDarkTheme()) DarkColors else LightColors
}
val ArabotoMedium = FontFamily(
    Font(R.font.araboto_medium400, FontWeight.Normal)
)

val ArabotoBold = FontFamily(
    Font(R.font.araboto_bold400, FontWeight.Bold)
)

val AppTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = ArabotoBold,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = (22 * 1.19).sp,
        letterSpacing = 0.01.sp,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    ),
    labelLarge = TextStyle( // For button text
        fontFamily = ArabotoBold,
        fontWeight = FontWeight.Normal,
        lineHeight = (22 * 1.19).sp,
        letterSpacing = 0.01.sp,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    ),
    bodyLarge = TextStyle( // General text
        fontFamily = ArabotoMedium,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = (15 * 1.38).sp,
        letterSpacing = 0.01.sp,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
)