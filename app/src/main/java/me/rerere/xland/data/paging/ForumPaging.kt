package me.rerere.xland.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import me.rerere.xland.data.model.Post
import me.rerere.xland.data.repo.ContentRepo

class ForumPaging(
    private val contentRepo: ContentRepo,
    private val path: String
): PagingSource<Int, Post>() {
    override fun getRefreshKey(state: PagingState<Int, Post>): Int {
        return 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        val currentPage = params.key ?: 1
       return runCatching {
            val result = contentRepo.getForum(
                path = path,
                page = currentPage
            )
           LoadResult.Page(
               data = result.data.posts,
               prevKey = if(result.hasPreviousPage) currentPage - 1 else null,
               nextKey = if(result.hasNextPage) currentPage + 1 else null
           )
        }.getOrElse {
            LoadResult.Error(it)
        }
    }
}