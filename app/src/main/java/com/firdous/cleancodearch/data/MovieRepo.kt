package com.firdous.cleancodearch.data

import android.content.Context
import com.firdous.cleancodearch.data.source.local.LocalDataSource
import com.firdous.cleancodearch.data.source.remote.RemoteDataSource
import com.firdous.cleancodearch.data.source.remote.network.ApiResponse
import com.firdous.cleancodearch.data.source.remote.response.MovieItem
import com.firdous.cleancodearch.domain.model.Movie
import com.firdous.cleancodearch.domain.repository.IMovieRepo
import com.firdous.cleancodearch.util.DataMapper
import com.firdous.cleancodearch.util.NetworkUtil
import kotlinx.coroutines.flow.*

class MovieRepo(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val context: Context
) : IMovieRepo {

    override fun fetchMovies(page: Int): Flow<Resource<List<Movie>>> =

        object : NetworkBoundResource<List<Movie>, List<MovieItem>>() {

            override fun shouldFetch(data: List<Movie>?): Boolean {
                return NetworkUtil.isInternetConnected(context)
            }

            override fun loadFromDB(): Flow<List<Movie>> {
                return localDataSource.getAllMovies().map { DataMapper.mapEntityToDomain(it) }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<MovieItem>>> {
                return remoteDataSource.getMovies(page)
            }

            override suspend fun saveCallResult(data: List<MovieItem>) {
                val entity = DataMapper.mapResponseToEntity(data)
                localDataSource.insertMovies(entity)
            }

        }.asFlow()

}