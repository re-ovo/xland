package me.rerere.xland.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import me.rerere.md3compat.Md3CompatTheme

@Composable
fun XlandTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    val activity = LocalContext.current as Activity
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.apply {
                statusBarColor = Color.Transparent.toArgb()
                navigationBarColor = Color.Transparent.toArgb()
            }
            WindowCompat.getInsetsController(activity.window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    Md3CompatTheme(
        typography = Typography,
        content = content
    )
}