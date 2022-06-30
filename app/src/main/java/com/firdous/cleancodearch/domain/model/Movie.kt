package com.firdous.cleancodearch.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val overview: String
): Parcelable