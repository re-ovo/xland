package me.rerere.xland.ui.screen.index

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import me.rerere.xland.ui.component.widget.Md3TopBar

@Composable
fun IndexScreen() {
    Scaffold(
        topBar = {
            Md3TopBar(
                title = {
                    Text("X岛")
                }
            )
        }
    ) { paddingValues ->

    }
}