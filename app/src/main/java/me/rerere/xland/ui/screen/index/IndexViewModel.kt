package me.rerere.xland.ui.screen.index

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import me.rerere.xland.data.paging.ForumPaging
import me.rerere.xland.data.repo.ContentRepo
import javax.inject.Inject

@HiltViewModel
class IndexViewModel @Inject constructor(
    private val contentRepo: ContentRepo
): ViewModel() {
    val timelinePager = Pager(
        config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 4,
            enablePlaceholders = false,
            initialLoadSize = 20
        )
    ) {
        ForumPaging(
            path = "https://www.nmbxd1.com/Forum/timeline/id/1",
            contentRepo = contentRepo
        )
    }.flow.cachedIn(viewModelScope)
}