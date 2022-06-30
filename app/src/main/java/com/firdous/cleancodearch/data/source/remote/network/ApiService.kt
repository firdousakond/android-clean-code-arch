package com.firdous.cleancodearch.data.source.remote.network

import com.firdous.cleancodearch.BuildConfig
import com.firdous.cleancodearch.data.source.remote.response.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie/upcoming")
    suspend fun getMovieList(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("page") page: Int? = 1
    ): MovieResponse
}