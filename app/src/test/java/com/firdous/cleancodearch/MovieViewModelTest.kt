package com.firdous.cleancodearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.firdous.cleancodearch.data.Resource
import com.firdous.cleancodearch.domain.model.Movie
import com.firdous.cleancodearch.domain.usecase.MovieUseCase
import com.firdous.cleancodearch.presentation.movie.MovieViewModel
import com.firdous.cleancodearch.utils.CoroutineTestRule
import com.firdous.cleancodearch.utils.runBlockingTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {

    private lateinit var movieViewModel: MovieViewModel

    @Mock
    lateinit var movieUseCase: MovieUseCase

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = CoroutineTestRule()

    @Before
    fun setup() {
        movieViewModel = MovieViewModel(useCase = movieUseCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadMovies_onError() {
        testCoroutineRule.runBlockingTest {

            val flow = flow {
                emit(Resource.Error(message = "Something Went Wrong"))
            }

            `when`(movieUseCase.fetchMovies(anyInt())).thenReturn(flow)

            var emittedError = ""
            val job = launch {
                movieViewModel.movieStateFlow.collect {
                    if(it is Resource.Error)
                    emittedError = it.message
                }
            }

            movieViewModel.fetchMovies()
            verify(movieUseCase, times(2)).fetchMovies(anyInt())
            assertTrue(emittedError.isNotEmpty())
            assertEquals("Something Went Wrong", emittedError)
            job.cancel()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadMovies_onSuccess() {
        testCoroutineRule.runBlockingTest {
            val data = listOf(
                Movie(
                    id = 10,
                    title = "Avengers",
                    overview = "sample overview",
                    release_date = "12/09/2022",
                    poster_path = "poster_path"
                ),
                Movie(
                    id = 20,
                    title = "Doctor Strange",
                    overview = "sample overview",
                    release_date = "12/09/2022",
                    poster_path = "poster_path"
                )
            )

            val flow = flow {
                emit(Resource.Success(data))
            }

            `when`(movieUseCase.fetchMovies(anyInt())).thenReturn(flow)

            var emittedMovie: List<Movie> = ArrayList()
            val job = launch {
                movieViewModel.movieStateFlow.collect {
                    if(it is Resource.Success) {
                        emittedMovie = it.data
                    }
                }
            }
            movieViewModel.fetchMovies()
            verify(movieUseCase, times(2)).fetchMovies(anyInt())
            assertTrue(emittedMovie.isNotEmpty())
            assertEquals(2, emittedMovie.size)
            assertEquals("Doctor Strange", emittedMovie[1].title)
            job.cancel()
        }
    }

}