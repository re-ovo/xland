package me.rerere.xland.ui.screen.thread

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import me.rerere.xland.ui.component.widget.BackIcon
import me.rerere.xland.ui.component.widget.Md3TopBar

@Composable
fun ThreadScreen() {
    Scaffold(
        topBar = {
            Md3TopBar(
                title = {
                    Text("帖子内容")
                },
                navigationIcon = {
                    BackIcon()
                }
            )
        }
    ) {

    }
}