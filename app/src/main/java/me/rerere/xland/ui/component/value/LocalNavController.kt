package me.rerere.xland.ui.component.value

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController

val LocalNavController = staticCompositionLocalOf<NavController> { error("Not init yet") }