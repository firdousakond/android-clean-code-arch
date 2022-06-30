package com.firdous.cleancodearch.di

import com.firdous.cleancodearch.domain.usecase.MovieUseCaseImpl
import com.firdous.cleancodearch.domain.usecase.MovieUseCase
import com.firdous.cleancodearch.presentation.movie.MovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule  = module {
    factory<MovieUseCase>{MovieUseCaseImpl(get())}
}
val viewModelModule = module {
    viewModel { MovieViewModel(get()) }
}
