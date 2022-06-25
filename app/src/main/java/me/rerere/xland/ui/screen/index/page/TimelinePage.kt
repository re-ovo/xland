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
import me.rerere.xland.ui.component.widget.ErrorAnimation
import me.rerere.xland.ui.component.widget.PostCard

@Composable
fun TimelinePage(
    state: LazyPagingItems<Post>,
    onRefresh: () -> Unit,
    onClickPost: (Post) -> Unit
) {
    when(state.loadState.refresh) {
        is LoadState.Error -> {
            ErrorAnimation(
                modifier = Modifier.fillMaxSize(),
                throwable = (state.loadState.refresh as LoadState.Error).error
            ) {
                onRefresh()
            }
        }
        else -> {
            SwipeRefresh(
                state = rememberSwipeRefreshState(
                    isRefreshing = state.loadState.refresh is LoadState.Loading
                ),
                onRefresh = {
                    onRefresh()
                }
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(state) { post ->
                        PostCard(
                            post = post!!,
                            showReply = true
                        ) {
                            onClickPost(post)
                        }
                    }
                }
            }
        }
    }
}