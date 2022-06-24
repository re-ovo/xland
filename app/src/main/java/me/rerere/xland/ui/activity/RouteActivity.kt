package me.rerere.xland.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import me.rerere.xland.ui.NavigationUI
import me.rerere.xland.ui.theme.XlandTheme

@AndroidEntryPoint
class RouteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            XlandTheme {
               NavigationUI()
            }
        }
    }
}