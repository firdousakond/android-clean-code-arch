package com.firdous.cleancodearch.domain.usecase

import com.firdous.cleancodearch.data.Resource
import com.firdous.cleancodearch.domain.model.Movie
import com.firdous.cleancodearch.domain.repository.IMovieRepo
import kotlinx.coroutines.flow.Flow

class MovieUseCaseImpl(private val movieRepo: IMovieRepo) : MovieUseCase{

    override fun fetchMovies(page: Int): Flow<Resource<List<Movie>>>  = movieRepo.fetchMovies(page)

}