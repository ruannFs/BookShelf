package com.seunome.bookshelf.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val BookShelfColorScheme = lightColorScheme(
    primary          = IndigoPrimary,
    onPrimary        = Color(0xFFFFFFFF),
    primaryContainer = IndigoContainer,
    onPrimaryContainer = IndigoDeep,
    secondary        = IndigoLight,
    surface          = SurfaceWarm,
    onSurface        = Color(0xFF1A1A2E),
    surfaceVariant   = Color(0xFFF0F0F8),
    outline          = Outline,
    background       = SurfaceWarm,
)

@Composable
fun BookShelfTheme(content: @Composable () -> Unit) {
    val colorScheme = BookShelfColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = IndigoDeep.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}