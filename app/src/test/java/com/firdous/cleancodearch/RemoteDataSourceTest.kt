package com.firdous.cleancodearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.firdous.cleancodearch.data.source.remote.RemoteDataSource
import com.firdous.cleancodearch.data.source.remote.network.ApiResponse
import com.firdous.cleancodearch.data.source.remote.network.ApiService
import com.firdous.cleancodearch.data.source.remote.response.MovieItem
import com.firdous.cleancodearch.data.source.remote.response.MovieResponse
import com.firdous.cleancodearch.utils.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
class RemoteDataSourceTest {

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = CoroutineTestRule()

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
        runBlockingTest {
//            val data = listOf(
//                MovieItem(
//                    id = 10,
//                    title = "Avengers",
//                    overview = "sample overview",
//                    release_date = "12/09/2022",
//                    poster_path = "poster_path",
//                    adult = false,
//                    backdrop_path = "",
//                    genre_ids = null,
//                    video = true,
//                    vote_count = 1,
//                    vote_average = 1.0,
//                    popularity = 10.0,
//                    original_title = "",
//                    original_language = "en"
//                ),
//                MovieItem(
//                    id = 20,
//                    title = "Doctor Strange",
//                    overview = "sample overview",
//                    release_date = "12/10/2022",
//                    poster_path = "poster_path",
//                    adult = false,
//                    backdrop_path = "",
//                    genre_ids = null,
//                    video = true,
//                    vote_count = 1,
//                    vote_average = 1.0,
//                    popularity = 10.0,
//                    original_title = "",
//                    original_language = "en"
//                )
//            )
//            val response =
//                MovieResponse(page = 1, results = data, total_pages = 20, total_results = 400)
//            var movieData: List<MovieItem>
//            `when`(apiService.getMovieList(page = 1)).thenReturn(response)
//            val job = launch {
//                remoteDataSource.getMovies(1)
//                    .collect {
//                        movieData = (it as ApiResponse.Success).data
//                        assertTrue(movieData.isNotEmpty())
//                        assertEquals(10, movieData[0].id)
//                    }
//            }
//
//            verify(apiService, times(1)).getMovieList(page = 1)
//            job.cancel()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun fetchMovies_onError() {
        runBlockingTest {
            `when`(apiService.getMovieList(page = 1)).thenThrow(RuntimeException("Exception Occurred"))
            val job = launch {
                remoteDataSource.getMovies(1)
                    .collect {
                        assertTrue(it is ApiResponse.Error)
                        assertEquals("Exception Occurred", it.errorMessage)
                    }

            }
            job.cancel()
        }
    }

}