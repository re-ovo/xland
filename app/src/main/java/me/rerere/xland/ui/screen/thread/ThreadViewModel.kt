package me.rerere.xland.ui.screen.thread

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.rerere.xland.data.repo.ContentRepo
import javax.inject.Inject

@HiltViewModel
class ThreadViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val contentRepo: ContentRepo
): ViewModel() {
    val tid = checkNotNull(savedStateHandle["tid"]) as Long
}