package com.example.imagesearchunsplash.presentation.viewModel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.imagesearchunsplash.data.repository.UnsplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: UnsplashRepository,
    state: SavedStateHandle
) : ViewModel() {

private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    val photos = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val CURRENT_QUERY ="current_query"
        private const val DEFAULT_QUERY = "car"
    }
}