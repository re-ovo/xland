package me.rerere.xland.ui.screen.index.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.rerere.xland.data.model.Post
import me.rerere.xland.ui.component.widget.PostCard

@Composable
fun TimelinePage(
    items: List<Post>,
    onRefresh: () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(false),
        onRefresh = onRefresh
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) {
                PostCard(it, true) {}
            }
        }
    }
}