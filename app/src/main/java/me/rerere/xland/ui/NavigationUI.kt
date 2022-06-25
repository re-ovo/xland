package me.rerere.xland.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import me.rerere.xland.ui.component.value.LocalNavController
import me.rerere.xland.ui.screen.forum.ForumScreen
import me.rerere.xland.ui.screen.index.IndexScreen
import me.rerere.xland.ui.screen.thread.ThreadScreen

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
                .background(MaterialTheme.colorScheme.background),
            enterTransition = Transition.defaultEnterTransition,
            exitTransition = Transition.defaultExitTransition,
            popEnterTransition = Transition.defaultPopEnterTransition,
            popExitTransition = Transition.defaultPopExitTransition,
        ) {
            composable(Destination.Index.route) {
                IndexScreen()
            }

            composable(
                route = "${Destination.Thread.route}/{id}",
                arguments = listOf(
                    navArgument("id") {
                        nullable = false
                        type = NavType.IntType
                    }
                )
            ) {
                ForumScreen()
            }

            composable(
                route = "${Destination.Thread.route}/{tid}",
                arguments = listOf(
                    navArgument("tid") {
                        nullable = false
                        type = NavType.LongType
                    }
                )
            ) {
                ThreadScreen()
            }
        }
    }
}

sealed class Destination(val route: String) {
    object Index : Destination("index")
    object Forum: Destination("forum")
    object Thread: Destination("thread")
}

fun NavController.navigate(
    destination: Destination,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(destination.route, builder)
}

object Transition {
    val defaultEnterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition) = {
        slideInHorizontally(
            initialOffsetX = {
                it
            },
            animationSpec = tween()
        )
    }

    val defaultExitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition) = {
        slideOutHorizontally(
            targetOffsetX = {
                -it
            },
            animationSpec = tween()
        ) + fadeOut(
            animationSpec = tween()
        )
    }

    val defaultPopEnterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition) =
        {
            slideInHorizontally(
                initialOffsetX = {
                    -it
                },
                animationSpec = tween()
            )
        }

    val defaultPopExitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition) = {
        slideOutHorizontally(
            targetOffsetX = {
                it
            },
            animationSpec = tween()
        )
    }
}