package me.rerere.xland.ui.screen.index

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.rerere.xland.data.model.ForumList
import me.rerere.xland.data.model.Post
import me.rerere.xland.data.repo.ContentRepo
import me.rerere.xland.util.DataState
import javax.inject.Inject

@HiltViewModel
class IndexViewModel @Inject constructor(
    private val contentRepo: ContentRepo,
): ViewModel() {
    val timelinePager = Pager(
        config = PagingConfig(
            pageSize = 1,
            initialLoadSize = 1,
            prefetchDistance = 4
        ),
        pagingSourceFactory = {
            object : PagingSource<Int, Post>() {
                override fun getRefreshKey(state: PagingState<Int, Post>): Int {
                    return 0
                }

                override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
                    val curr = params.key ?: 1
                    return kotlin.runCatching {
                        LoadResult.Page(
                            data = contentRepo.getTimeline(curr),
                            prevKey = if(curr == 1) null else curr - 1,
                            nextKey = curr + 1
                        )

                    }.getOrElse {
                        it.printStackTrace()
                        LoadResult.Error(it)
                    }
                }
            }
        }
    ).flow.cachedIn(viewModelScope)

    val forumList = MutableStateFlow<DataState<ForumList>>(DataState.Empty)

    init {
        viewModelScope.launch {
            forumList.value = DataState.Loading
            delay(500)
            kotlin.runCatching {
                forumList.value = DataState.Success(contentRepo.getForumList())
            }.onFailure {
                it.printStackTrace()
                forumList.value = DataState.Error(it)
            }
        }
    }
}