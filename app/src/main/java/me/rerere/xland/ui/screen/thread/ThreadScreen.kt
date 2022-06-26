package me.rerere.xland.ui.screen.thread

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Reply
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.rerere.xland.data.model.Post
import me.rerere.xland.data.model.Reply
import me.rerere.xland.ui.component.widget.*
import me.rerere.xland.util.DataState
import me.rerere.xland.util.TimeUtil

@Composable
fun ThreadScreen(viewModel: ThreadViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            Md3TopBar(
                title = {
                    Text("No.${viewModel.tid}")
                },
                navigationIcon = {
                    BackIcon()
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Outlined.Star, null)
                    }

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Outlined.Share, null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.navigationBarsPadding()
            ) {
                Icon(Icons.Outlined.Reply, null)
            }
        }
    ) { innerPadding ->
        val post by viewModel.post.collectAsState()
        val pager = viewModel.postPager.collectAsLazyPagingItems()
        SwipeRefresh(
            state = rememberSwipeRefreshState(
                isRefreshing = pager.loadState.refresh is LoadState.Loading || post is DataState.Loading
            ),
            onRefresh = {
                viewModel.refresh()
                pager.refresh()
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    post.readSafely()?.let {
                        PostCard(
                            post = it,
                            type = PostCardType.Detail
                        )
                    }
                }

                items(pager) {
                    ReplyCard(
                        post = post.readSafely(),
                        reply = it!!
                    )
                }

                when(pager.loadState.append) {
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
}

@Composable
private fun ReplyCard(post: Post?, reply: Reply) {
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (reply.user_hash == post?.user_hash) {
                        Text(
                            text = "PO",
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.colorScheme.secondary)
                                .padding(2.dp),
                            color = contentColorFor(MaterialTheme.colorScheme.secondary),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

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

            // img
            if (reply.img.isNotEmpty()) {
                ExpandableImage(path = reply.img, ext = reply.ext)
            }
        }
    }
}