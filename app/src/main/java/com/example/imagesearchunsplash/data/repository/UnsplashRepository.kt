package com.example.imagesearchunsplash.data.repository

import androidx.paging.*
import com.example.imagesearchunsplash.api.UnsplashApi
import com.example.imagesearchunsplash.data.model.UnsplashPhoto
import com.example.imagesearchunsplash.data.paging.UnsplashPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject constructor(private val unsplashApi: UnsplashApi) {

    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
            ),
            pagingSourceFactory = { UnsplashPagingSource(unsplashApi, query) }
        ).liveData
}