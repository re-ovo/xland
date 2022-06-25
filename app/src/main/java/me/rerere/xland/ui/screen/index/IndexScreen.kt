package me.rerere.xland.ui.screen.index

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.rerere.md3compat.ThemeChooser
import me.rerere.xland.R
import me.rerere.xland.ui.Destination
import me.rerere.xland.ui.component.value.LocalNavController
import me.rerere.xland.ui.component.widget.Md3BottomNavigation
import me.rerere.xland.ui.component.widget.Md3TopBar
import me.rerere.xland.ui.screen.index.page.TimelinePage

@Composable
fun IndexScreen(viewModel: IndexViewModel = hiltViewModel()) {
    val scope = rememberCoroutineScope()
    val pager = rememberPagerState()
    val navController = LocalNavController.current
    Scaffold(
        topBar = {
            Md3TopBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(Icons.Outlined.Search, null)
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(
                currentSelection = pager.currentPage,
                onSelection = {
                    scope.launch { pager.animateScrollToPage(it) }
                }
            )
        }
    ) { paddingValues ->
        HorizontalPager(
            count = 3,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            state = pager
        ) {
            when (it) {
                0 -> {
                    val state = viewModel.timelinePager.collectAsLazyPagingItems()
                    TimelinePage(
                        state = state,
                        onRefresh = {
                            state.refresh()
                        },
                        onClickPost = { post ->
                            navController.navigate("${Destination.Thread.route}/${post.tid}")
                        }
                    )
                }
                else -> {
                    ThemeChooser()
                }
            }
        }
    }
}

@Composable
private fun BottomBar(
    currentSelection: Int,
    onSelection: (Int) -> Unit
) {
    Md3BottomNavigation {
        NavigationBarItem(
            selected = currentSelection == 0,
            onClick = { onSelection(0) },
            label = {
                Text("时间线")
            },
            icon = {
                Icon(Icons.Outlined.Timeline, null)
            }
        )
        NavigationBarItem(
            selected = currentSelection == 1,
            onClick = { onSelection(1) },
            label = {
                Text("分区")
            },
            icon = {
                Icon(Icons.Outlined.Category, null)
            }
        )
        NavigationBarItem(
            selected = currentSelection == 2,
            onClick = { onSelection(2) },
            label = {
                Text("我的")
            },
            icon = {
                Icon(Icons.Outlined.People, null)
            }
        )
    }
}