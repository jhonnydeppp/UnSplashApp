package com.deneb.unsplashapp.features.photos

import com.deneb.unsplashapp.features.photos.model.UnsplashResponse
import com.deneb.unsplashapp.features.photos.model.UnsplashDetailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

internal interface PhotosApi {
    @GET("photos/?client_id=TU_CLAVE_AQUÍ")
    fun photos(): Call<UnsplashResponse>

    @GET("photos/{id}/?client_id=TU_CLAVE_AQUÍ")
    fun detailPhoto(@Path("id")id: String): Call<UnsplashDetailResponse>
}