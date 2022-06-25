package me.rerere.xland.ui.screen.thread

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import me.rerere.xland.ui.component.widget.BackIcon
import me.rerere.xland.ui.component.widget.Md3TopBar
import me.rerere.xland.util.plus

@Composable
fun ThreadScreen(viewModel: ThreadViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            Md3TopBar(
                title = {
                    Text("帖子内容 No.${viewModel.tid}")
                },
                navigationIcon = {
                    BackIcon()
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding + PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(text = "Hello")
            }
        }
    }
}