package co.edu.upb.mimercado.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import co.edu.upb.mimercado.R

val Green = Color(0xFF2F7D4B)
val GreenSoft = Color(0xFF66A477)
val Yellow = Color(0xFFF2B84B)
val Ink = Color(0xFF1F2A24)
val Muted = Color(0xFF66706A)
val Line = Color(0xFFD9E1DA)
val Canvas = Color(0xFFF7F9F7)
val Mist = Color(0xFFF0F4F1)
val Mint = Color(0xFFE8F5EE)
val Danger = Color(0xFFD9534F)
val Cream = Color(0xFFFFF8ED)

val Poppins = FontFamily(
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold),
)

val Inter = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
)

private val LightColors = lightColorScheme(
    primary = Green,
    onPrimary = Color.White,
    primaryContainer = Mint,
    onPrimaryContainer = Ink,
    secondary = Yellow,
    onSecondary = Ink,
    background = Canvas,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = Mist,
    onSurfaceVariant = Muted,
    outline = Line,
    error = Danger,
    onError = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = GreenSoft,
    onPrimary = Color(0xFF102117),
    primaryContainer = Color(0xFF1E3A2A),
    onPrimaryContainer = Color(0xFFD7F0E0),
    secondary = Yellow,
    onSecondary = Ink,
    background = Color(0xFF121A16),
    onBackground = Color(0xFFF3F7F4),
    surface = Color(0xFF1B2820),
    onSurface = Color(0xFFF3F7F4),
    surfaceVariant = Color(0xFF24332B),
    onSurfaceVariant = Color(0xFFA8B5AC),
    outline = Color(0xFF314239),
    error = Color(0xFFFF8A84),
    onError = Color(0xFF2A0C0B),
)

private val AppTypography = Typography(
    headlineMedium = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    ),
)

@Composable
fun MiMercadoTheme(
    dark: Boolean,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (dark) DarkColors else LightColors,
        typography = AppTypography,
        content = content,
    )
}
