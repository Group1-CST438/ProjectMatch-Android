package com.projectmatch.android.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

private val DarkColorScheme = darkColorScheme(
    primary = ButtonPrimary,
    onPrimary = TextPrimary,
    secondary = ButtonAccent,
    onSecondary = PageBgEnd,
    background = PageBgEnd,
    onBackground = TextPrimary,
    surface = SurfaceSecondary,
    onSurface = TextPrimary,
    error = Danger,
    onError = TextPrimary,
)

// Radial gradient matching: radial-gradient(1200px 680px at 12% -8%, #223856 0%, #0e1726 52%, #080d14 100%)
val PageBackgroundBrush = Brush.radialGradient(
    colorStops = arrayOf(
        0.0f to PageBgStart,
        0.52f to PageBgMid,
        1.0f to PageBgEnd,
    ),
    center = Offset(0.12f * 1080f, -0.08f * 1920f),
    radius = 1400f,
)

@Composable
fun ProjectMatchTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content,
    )
}

@Composable
fun PageBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = PageBackgroundBrush),
    ) {
        content()
    }
}
