package com.firdous.cleancodearch.domain.usecase

import com.firdous.cleancodearch.data.Resource
import com.firdous.cleancodearch.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieUseCase {
    fun fetchMovies(page: Int) : Flow<Resource<List<Movie>>>
}