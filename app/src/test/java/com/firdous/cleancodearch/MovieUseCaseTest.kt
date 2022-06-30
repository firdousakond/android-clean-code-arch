package com.firdous.cleancodearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.firdous.cleancodearch.data.Resource
import com.firdous.cleancodearch.domain.model.Movie
import com.firdous.cleancodearch.domain.repository.IMovieRepo
import com.firdous.cleancodearch.domain.usecase.MovieUseCaseImpl
import com.firdous.cleancodearch.utils.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
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
class MovieUseCaseTest {

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = CoroutineTestRule()

    private lateinit var movieUseCase: MovieUseCaseImpl

    @Mock
    lateinit var movieRepo: IMovieRepo

    @Before
    fun setup() {
        movieUseCase = MovieUseCaseImpl(movieRepo)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun fetchMovies_onSuccess() {
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

            var movieData: List<Movie> = ArrayList()
            val flow = flow {
                emit(Resource.Success(data))
            }

            `when`(movieRepo.fetchMovies(1)).thenReturn(flow)
            movieUseCase.fetchMovies(1)
                .collect {
                    movieData = it.data.orEmpty()
                }
            verify(movieRepo, times(1)).fetchMovies(1)
            assertTrue(movieData.isNotEmpty())
            assertEquals("Avengers", movieData[0].title)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun fetchMovies_onError() {
        runBlockingTest {
            var errorMessage = ""
            val flow = flow {
                emit(Resource.Error(data = null, message = "Something went wrong"))
            }

            `when`(movieRepo.fetchMovies(1)).thenReturn(flow)
            movieUseCase.fetchMovies(1).collect { errorMessage = it.message.orEmpty() }
            verify(movieRepo, times(1)).fetchMovies(1)
            assertTrue(errorMessage.isNotEmpty())
            assertEquals("Something went wrong", errorMessage)
        }
    }

}