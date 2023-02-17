package com.example.imagesearchunsplash.api

import com.example.imagesearchunsplash.BuildConfig
import com.example.imagesearchunsplash.data.model.UnsplashPhoto
import com.example.imagesearchunsplash.data.model.UnsplashResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UnsplashApi {


    @Headers("Accept-Version: v1", "Authorization: Client-ID ${BuildConfig.API_KEY}")
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): UnsplashResponse<UnsplashPhoto>
}