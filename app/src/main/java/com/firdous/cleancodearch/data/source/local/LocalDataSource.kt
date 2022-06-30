package com.firdous.cleancodearch.data.source.local

import com.firdous.cleancodearch.data.source.local.entity.MovieEntity
import com.firdous.cleancodearch.data.source.local.room.MovieDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val movieDao: MovieDao) {

    fun getAllMovies(): Flow<List<MovieEntity>> = movieDao.getAllMovies()

    suspend fun insertMovies(movieList: List<MovieEntity>) = movieDao.insertMovie(movieList)
}