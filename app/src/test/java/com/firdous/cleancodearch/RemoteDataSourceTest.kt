package com.firdous.cleancodearch

import com.firdous.cleancodearch.data.source.remote.RemoteDataSource
import com.firdous.cleancodearch.data.source.remote.network.ApiResponse
import com.firdous.cleancodearch.data.source.remote.network.ApiService
import com.firdous.cleancodearch.data.source.remote.response.MovieItem
import com.firdous.cleancodearch.data.source.remote.response.MovieResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RemoteDataSourceTest {

    private lateinit var remoteDataSource: RemoteDataSource

    @Mock
    lateinit var apiService: ApiService

    @Before
    fun setup() {
        remoteDataSource = RemoteDataSource(apiService)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun fetchMovies_onSuccess() {
        runTest {
            val data = listOf(
                MovieItem(
                    id = 10,
                    title = "Avengers",
                    overview = "sample overview",
                    release_date = "12/09/2022",
                    poster_path = "poster_path",
                    adult = false,
                    backdrop_path = "",
                    genre_ids = null,
                    video = true,
                    vote_count = 1,
                    vote_average = 1.0,
                    popularity = 10.0,
                    original_title = "",
                    original_language = "en"
                ),
                MovieItem(
                    id = 20,
                    title = "Doctor Strange",
                    overview = "sample overview",
                    release_date = "12/10/2022",
                    poster_path = "poster_path",
                    adult = false,
                    backdrop_path = "",
                    genre_ids = null,
                    video = true,
                    vote_count = 1,
                    vote_average = 1.0,
                    popularity = 10.0,
                    original_title = "",
                    original_language = "en"
                )
            )
            val response =
                MovieResponse(page = 2, results = data, total_pages = 20, total_results = 400)
            var movieList: List<MovieItem> = ArrayList()
            `when`(apiService.getMovieList(page = 2)).thenReturn(response)
            remoteDataSource.getMovies(2)
                .collect {
                    movieList = (it as ApiResponse.Success).data
                }
            verify(apiService, times(1)).getMovieList(page = 2)
            assertTrue(movieList.isNotEmpty())
            assertEquals(20, movieList[1].id)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun fetchMovies_onError() {
        runTest {
            `when`(apiService.getMovieList(page = 1)).thenThrow(RuntimeException("Exception Occurred"))
            var errorMessage = ""
            remoteDataSource.getMovies(1)
                .collect {
                    errorMessage = (it as ApiResponse.Error).errorMessage
                }
            assertEquals(
                "java.lang.RuntimeException: Exception Occurred", errorMessage
            )

        }
    }

}