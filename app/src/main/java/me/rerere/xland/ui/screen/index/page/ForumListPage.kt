package me.rerere.xland.ui.screen.index.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.rerere.xland.ui.component.widget.ErrorAnimation
import me.rerere.xland.ui.component.widget.HtmlText
import me.rerere.xland.ui.component.widget.LoadingAnimation
import me.rerere.xland.ui.screen.index.IndexViewModel
import me.rerere.xland.util.onError
import me.rerere.xland.util.onLoading
import me.rerere.xland.util.onSuccess

@Composable
fun ForumListPage(viewModel: IndexViewModel) {
    val state by viewModel.forumList.collectAsState()
    val scope = rememberCoroutineScope()
    state.onSuccess { forumList ->
        val pager = rememberPagerState()
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ScrollableTabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = pager.currentPage,
                edgePadding = 4.dp
            ) {
                forumList.fastForEachIndexed { i, forumCategory ->
                    Tab(
                        selected = pager.currentPage == i,
                        onClick = {
                            scope.launch {
                                pager.animateScrollToPage(i)
                            }
                        },
                        text = {
                            Text(forumCategory.name)
                        }
                    )
                }
            }

            HorizontalPager(
                count = forumList.size,
                state = pager,
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(forumList[it].forums.filter { it.id.toInt() > 0 }) { forum ->
                        Card {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = forum.name,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                HtmlText(
                                    text = forum.msg
                                )
                            }
                        }
                    }
                }
            }
        }
    }.onLoading {
        LoadingAnimation(
            modifier = Modifier.fillMaxSize()
        )
    }.onError {
        ErrorAnimation(
            modifier = Modifier.fillMaxSize(),
            throwable = it
        )
    }
}
