package me.rerere.xland.ui.screen.thread

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.rerere.xland.data.model.Reply
import me.rerere.xland.ui.component.widget.*
import me.rerere.xland.util.DataState
import me.rerere.xland.util.TimeUtil
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
        val post by viewModel.post.collectAsState()
        val pager = viewModel.postPager.collectAsLazyPagingItems()
        SwipeRefresh(
            state = rememberSwipeRefreshState(
                isRefreshing = post is DataState.Loading || pager.loadState.refresh is LoadState.Loading
            ),
            onRefresh = {
                viewModel.refresh()
                pager.refresh()
            }
        ) {
            LazyColumn(
                contentPadding = innerPadding + PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                post.readSafely()?.let {
                    item {
                        PostCard(
                            post = it,
                            type = PostCardType.Detail
                        )
                    }
                }

                items(pager) {
                    ReplyCard(
                        reply = it!!
                    )
                }
            }
        }
    }
}

@Composable
private fun ReplyCard(reply: Reply) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // info
            ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = reply.user_hash
                    )

                    Text(
                        text = "No.${reply.id}",
                    )

                    Text(
                        text = TimeUtil.convertTimeToBetterFormat(reply.now)
                    )
                }
            }

            // content
            ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                HtmlText(
                    text = reply.content
                )
            }
        }
    }
}