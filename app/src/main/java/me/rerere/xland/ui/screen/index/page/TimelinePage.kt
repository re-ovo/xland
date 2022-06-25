package me.rerere.xland.ui.screen.index.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.rerere.xland.data.model.Post
import me.rerere.xland.ui.Destination
import me.rerere.xland.ui.component.value.LocalNavController
import me.rerere.xland.ui.component.widget.PostCard

@Composable
fun TimelinePage(
    state: LazyPagingItems<Post>
) {
    val navController = LocalNavController.current
    SwipeRefresh(
        state = rememberSwipeRefreshState(state.loadState.refresh is LoadState.Loading),
        onRefresh = {
            state.refresh()
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(state) {
                PostCard(
                    post = it!!,
                    showReply = true
                ) {
                    navController.navigate("${Destination.Thread.route}/${it.id}")
                }
            }
        }
    }
}