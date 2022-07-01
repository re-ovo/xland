package me.rerere.xland.ui.screen.history

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.rerere.xland.ui.component.widget.BackIcon
import me.rerere.xland.ui.component.widget.Md3TopBar

@Composable
fun HistoryScreen() {
    Scaffold(
        topBar = {
            Md3TopBar(
                title = {
                    Text("浏览历史")
                },
                navigationIcon = {
                    BackIcon()
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize()
        ) {

        }
    }
}