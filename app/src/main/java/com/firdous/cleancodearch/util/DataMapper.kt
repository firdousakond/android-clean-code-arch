package com.firdous.cleancodearch.util

import com.firdous.cleancodearch.data.source.local.entity.MovieEntity
import com.firdous.cleancodearch.data.source.remote.response.MovieItem
import com.firdous.cleancodearch.domain.model.Movie

object DataMapper {

    fun mapResponseToEntity(input: List<MovieItem>): List<MovieEntity> =
        input.map {
            MovieEntity(
                id = it.id,
                title = it.title.orEmpty(),
                posterPath = "$TMDB_POSTER_BASE_URL${it.poster_path}",
                releaseDate = it.release_date.orEmpty(),
                overview = it.overview.orEmpty()
            )
        }

    fun mapEntityToDomain(input: List<MovieEntity>): List<Movie> =
        input.map {
            Movie(
                id = it.id,
                title = it.title,
                poster_path = it.posterPath,
                release_date = it.releaseDate,
                overview = it.overview
            )
        }


    fun mapDomainToEntity(input: Movie) =
        MovieEntity(
            id = input.id,
            title = input.title,
            posterPath = input.poster_path,
            releaseDate = input.release_date,
            overview = input.overview
        )
}