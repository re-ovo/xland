package me.rerere.xland.ui.screen.forum

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.rerere.xland.data.repo.ContentRepo
import javax.inject.Inject

@HiltViewModel
class ForumViewModel @Inject constructor(
    private val contentRepo: ContentRepo,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val forumId = checkNotNull(savedStateHandle["id"]) as Int
}