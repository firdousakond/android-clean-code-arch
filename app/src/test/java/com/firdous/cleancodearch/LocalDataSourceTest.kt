package com.firdous.cleancodearch

import com.firdous.cleancodearch.data.source.local.LocalDataSource
import com.firdous.cleancodearch.data.source.local.entity.MovieEntity
import com.firdous.cleancodearch.data.source.local.room.MovieDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LocalDataSourceTest {

    private lateinit var localDataSource: LocalDataSource

    @Mock
    lateinit var movieDao: MovieDao

    @Before
    fun setup() {
        localDataSource = LocalDataSource(movieDao)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun fetchMovies_onSuccess() {
        runTest {
            val data = listOf(
                MovieEntity(
                    id = 10,
                    title = "Avengers",
                    overview = "sample overview",
                    releaseDate = "20/08/2022",
                    posterPath = "poster"
                ),
                MovieEntity(
                    id = 20,
                    title = "Doctor Strange",
                    overview = "sample overview",
                    releaseDate = "12/10/2022",
                    posterPath = "poster_path"
                )
            )
            var emittedData: List<MovieEntity> = ArrayList()
            val flow = flow {
                emit(data)
            }
            Mockito.`when`(movieDao.getAllMovies()).thenReturn(flow)
            localDataSource.getAllMovies().collect {
                emittedData = it
            }
            assertTrue(emittedData.size == 2)
            assertEquals(20, emittedData[1].id)
            Mockito.verify(movieDao, Mockito.times(1)).getAllMovies()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun insertMovies_onSuccess() {
        runTest {
            val data = listOf(
                MovieEntity(
                    id = 50,
                    title = "Black Widow",
                    overview = "sample overview",
                    releaseDate = "20/08/2022",
                    posterPath = "poster"
                ),
                MovieEntity(
                    id = 60,
                    title = "Iron Man",
                    overview = "sample overview",
                    releaseDate = "12/11/2022",
                    posterPath = "poster_path"
                ),
                MovieEntity(
                    id = 70,
                    title = "Thor",
                    overview = "sample overview",
                    releaseDate = "10/10/2022",
                    posterPath = "poster_path"
                )
            )
            localDataSource.insertMovies(data)
            Mockito.verify(movieDao, Mockito.times(1)).insertMovie(data)
        }
    }

}