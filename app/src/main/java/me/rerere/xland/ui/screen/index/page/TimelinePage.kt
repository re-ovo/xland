package me.rerere.xland.ui.screen.index.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.rerere.xland.ui.Destination
import me.rerere.xland.ui.component.value.LocalNavController
import me.rerere.xland.ui.component.widget.PostCard
import me.rerere.xland.ui.component.widget.PostCardType
import me.rerere.xland.ui.screen.index.IndexViewModel

@Composable
fun TimelinePage(
    viewModel: IndexViewModel
) {
    val state = viewModel.timelinePager.collectAsLazyPagingItems()
    val navController = LocalNavController.current
    SwipeRefresh(
        state = rememberSwipeRefreshState(
            state.loadState.refresh is LoadState.Loading
        ),
        onRefresh = {
            state.refresh()
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = state
            ) { item ->
                PostCard(
                    post = item!!,
                    type = PostCardType.Preview
                ) {
                    navController.navigate("${Destination.Thread.route}/${item.id}")
                }
            }

            when(state.loadState.append) {
                is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                else -> {}
            }
        }
    }
}