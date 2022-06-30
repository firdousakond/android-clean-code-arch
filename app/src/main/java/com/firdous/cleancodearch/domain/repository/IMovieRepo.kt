package com.firdous.cleancodearch.domain.repository

import com.firdous.cleancodearch.data.Resource
import com.firdous.cleancodearch.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface IMovieRepo {
    fun fetchMovies(page: Int) : Flow<Resource<List<Movie>>>
}