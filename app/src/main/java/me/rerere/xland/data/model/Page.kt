package me.rerere.xland.data.model

class Page<T>(
    val data: T,
    val currentPage: Int,
    val hasPreviousPage: Boolean,
    val hasNextPage: Boolean
) {
    companion object {
        fun <T> empty() = Page(
            data = emptyList<T>(),
            currentPage = 0,
            hasPreviousPage = false,
            hasNextPage = false
        )
    }
}