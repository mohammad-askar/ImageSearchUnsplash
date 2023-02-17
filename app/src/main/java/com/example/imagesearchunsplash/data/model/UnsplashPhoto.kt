package com.example.imagesearchunsplash.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UnsplashPhoto(
    val id: String,
    val description: String?,
    val likes: Int,
    val urls: UnsplashUrls,
    val user: UnsplashUser
): Parcelable