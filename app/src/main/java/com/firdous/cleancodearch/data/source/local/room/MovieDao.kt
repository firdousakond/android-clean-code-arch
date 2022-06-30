package com.firdous.cleancodearch.data.source.local.room

import androidx.room.*
import com.firdous.cleancodearch.data.source.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<MovieEntity>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movies: List<MovieEntity>) : List<Long>
    @Update
    fun updateMovie(movie: MovieEntity)
}