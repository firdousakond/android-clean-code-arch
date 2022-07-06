package com.firdous.cleancodearch

import com.firdous.cleancodearch.data.Resource
import com.firdous.cleancodearch.domain.model.Movie
import com.firdous.cleancodearch.domain.repository.IMovieRepo
import com.firdous.cleancodearch.domain.usecase.MovieUseCaseImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieUseCaseTest {

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
       runTest {
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
                    movieData = (it as Resource.Success).data
                }
            verify(movieRepo, times(1)).fetchMovies(1)
            assertTrue(movieData.isNotEmpty())
            assertEquals("Avengers", movieData[0].title)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun fetchMovies_onError() {
        runTest {
            var errorMessage = ""
            val flow = flow {
                emit(Resource.Error(message = "Something went wrong"))
            }

            `when`(movieRepo.fetchMovies(anyInt())).thenReturn(flow)
            movieUseCase.fetchMovies(1).collect {
                errorMessage = (it as Resource.Error).message
            }
            verify(movieRepo, times(1)).fetchMovies(anyInt())
            assertTrue(errorMessage.isNotEmpty())
            assertEquals("Something went wrong", errorMessage)
        }
    }

}