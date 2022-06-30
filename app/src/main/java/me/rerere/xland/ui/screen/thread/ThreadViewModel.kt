package me.rerere.xland.ui.screen.thread

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.rerere.xland.data.api.RefAPI
import me.rerere.xland.data.model.Post
import me.rerere.xland.data.model.Reply
import me.rerere.xland.data.repo.ContentRepo
import me.rerere.xland.util.DataState
import javax.inject.Inject

@HiltViewModel
class ThreadViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val refAPI: RefAPI,
    private val contentRepo: ContentRepo,
): ViewModel() {
    val tid = checkNotNull(savedStateHandle["tid"]) as Long
    val post = MutableStateFlow<DataState<Post>>(DataState.Empty)
    val postPager = Pager(
        config = PagingConfig(
            pageSize = 10,
            initialLoadSize = 10,
            prefetchDistance = 4
        ),
        pagingSourceFactory = {
            object : PagingSource<Int, Reply>() {
                override fun getRefreshKey(state: PagingState<Int, Reply>): Int {
                    return 1
                }

                override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Reply> {
                    val currentPage = params.key ?: 1
                    return kotlin.runCatching {
                        val thread = contentRepo.getThread(
                            id = tid,
                            page = currentPage
                        )
                        LoadResult.Page(
                            data = thread.Replies,
                            prevKey = if(currentPage == 1) null else currentPage - 1,
                            nextKey = if(thread.Replies.isEmpty()) null else currentPage + 1
                        )
                    }.getOrElse {
                        it.printStackTrace()
                        LoadResult.Error(it)
                    }
                }
            }
        }
    ).flow.cachedIn(viewModelScope)

    fun refresh() {
        viewModelScope.launch {
            post.value = DataState.Loading
            kotlin.runCatching {
                post.value = DataState.Success(contentRepo.getThread(tid, 1))
            }.onFailure {
                it.printStackTrace()
                post.value = DataState.Error(it)
            }
        }
    }

    init {
        refresh()
    }
}