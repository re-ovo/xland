package me.rerere.xland.ui.screen.index

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.rerere.xland.data.model.Post
import me.rerere.xland.data.repo.ContentRepo
import javax.inject.Inject

@HiltViewModel
class IndexViewModel @Inject constructor(
    private val contentRepo: ContentRepo,
): ViewModel() {
    val apiResponse = MutableStateFlow<List<Post>>(emptyList())

    init {
        viewModelScope.launch {
           apiResponse.value = contentRepo.getTimeline(0)
        }
    }
}