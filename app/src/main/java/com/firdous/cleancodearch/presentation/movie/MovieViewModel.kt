package com.firdous.cleancodearch.presentation.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firdous.cleancodearch.data.Resource
import com.firdous.cleancodearch.domain.model.Movie
import com.firdous.cleancodearch.domain.usecase.MovieUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MovieViewModel(private val useCase: MovieUseCase) : ViewModel() {

    var page: Int = 1

    private var _movieStateFlow = MutableStateFlow<Resource<List<Movie>>>(Resource.Loading())
    val movieStateFlow: StateFlow<Resource<List<Movie>>> = _movieStateFlow

    init {
        fetchMovies()
    }

    fun fetchMovies() {
        viewModelScope.launch {
            useCase.fetchMovies(page)
                .catch { e ->
                    _movieStateFlow.value = Resource.Error(e.toString(), null)
                }
                .collect {
                    _movieStateFlow.value = it
                }
        }
    }

}