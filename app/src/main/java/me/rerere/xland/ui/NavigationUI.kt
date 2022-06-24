package me.rerere.xland.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import me.rerere.xland.ui.component.value.LocalNavController
import me.rerere.xland.ui.screen.index.IndexScreen

@Composable
fun NavigationUI() {
    val navController = rememberAnimatedNavController()

    CompositionLocalProvider(
        LocalNavController provides navController
    ) {
        AnimatedNavHost(
            navController = navController,
            startDestination = Destination.Index.route,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            composable(Destination.Index.route) {
                IndexScreen()
            }
        }
    }
}

sealed class Destination(val route: String) {
    object Index : Destination("index")
}