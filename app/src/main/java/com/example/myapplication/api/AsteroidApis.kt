package com.example.myapplication.Data.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.example.myapplication.Constants
import com.example.myapplication.PictureOfDay
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

interface AsteroidApis {

    @GET("neo/rest/v1/feed")
    fun getAsteroidsList(
        @Query("start_date") start_date: String ,
        @Query("end_date") end_date: String,
        @Query("api_key") api_key: String
    ): Response<String>

    @GET("planetary/apod")
    suspend  fun getPictureOfDay(
        @Query("api_key") apiKey: String
    ): PictureOfDay
}

object AsteroidApi {
    val retrofitService: AsteroidApis by lazy { retrofit.create(AsteroidApis::class.java) }
}