package me.rerere.xland.util

import androidx.compose.runtime.Composable

sealed class DataState<out T> {
    object Empty : DataState<Nothing>()
    object Loading : DataState<Nothing>()

    data class Error(
        val message: String
    ) : DataState<Nothing>()

    data class Success<T>(
        val data: T
    ) : DataState<T>()

    fun readSafely(): T? = if (this is Success<T>) data else null
}

@Composable
fun <T> DataState<T>.onSuccess(
    content: @Composable ((T) -> Unit)
): DataState<T> {
    if (this is DataState.Success) {
        content(this.data)
    }
    return this
}

@Composable
fun <T> DataState<T>.onError(
    content: @Composable ((String) -> Unit)
): DataState<T> {
    if (this is DataState.Error) {
        content(this.message)
    }
    return this
}

@Composable
fun <T> DataState<T>.onLoading(
    content: @Composable (() -> Unit)
): DataState<T> {
    if (this is DataState.Loading) {
        content()
    }
    return this
}