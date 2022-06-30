package com.firdous.cleancodearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.firdous.cleancodearch.data.Resource
import com.firdous.cleancodearch.domain.model.Movie
import com.firdous.cleancodearch.domain.usecase.MovieUseCase
import com.firdous.cleancodearch.presentation.movie.MovieViewModel
import com.firdous.cleancodearch.utils.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
        runBlockingTest {

            val flow = flow {
                emit(Resource.Error(message = "Something Went Wrong", data = null))
            }

            `when`(movieUseCase.fetchMovies(1)).thenReturn(flow)

            var emittedMovie: Resource<List<Movie>> = Resource.Loading()
            val job = launch {
                movieViewModel.movieStateFlow.collect {
                    emittedMovie = it
                }
            }

            movieViewModel.fetchMovies()
            verify(movieUseCase, times(2)).fetchMovies(1)
            assertEquals("Something Went Wrong",emittedMovie.message)
            job.cancel()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadMovies_onSuccess() {
        runBlockingTest {
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

            `when`(movieUseCase.fetchMovies(1)).thenReturn(flow)

            var emittedMovie: List<Movie> = ArrayList()
            val job = launch {
                movieViewModel.movieStateFlow.collect {
                    emittedMovie = it.data.orEmpty()
                }
            }
            movieViewModel.fetchMovies()
            verify(movieUseCase, times(2)).fetchMovies(1)
            assertTrue(emittedMovie.isNotEmpty())
            assertEquals(2, emittedMovie.size)
            assertEquals("Doctor Strange", emittedMovie[1].title)
            job.cancel()
        }
    }

}