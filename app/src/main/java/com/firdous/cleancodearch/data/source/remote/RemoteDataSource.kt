package com.firdous.cleancodearch.data.source.remote

import com.firdous.cleancodearch.data.source.remote.network.ApiResponse
import com.firdous.cleancodearch.data.source.remote.network.ApiService
import com.firdous.cleancodearch.data.source.remote.response.MovieItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class RemoteDataSource (private val apiService: ApiService){

     suspend fun getMovies(page: Int): Flow<ApiResponse<List<MovieItem>>> {
        return flow {
            try {
                val movies = apiService.getMovieList(page = page)
                if (!movies.results.isNullOrEmpty()) {
                    emit(ApiResponse.Success(movies.results))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Timber.e(e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

}