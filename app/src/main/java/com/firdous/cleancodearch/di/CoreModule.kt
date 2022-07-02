package com.firdous.cleancodearch.di

import androidx.room.Room
import com.firdous.cleancodearch.BuildConfig
import com.firdous.cleancodearch.data.MovieRepo
import com.firdous.cleancodearch.data.source.local.LocalDataSource
import com.firdous.cleancodearch.data.source.local.room.MovieDatabase
import com.firdous.cleancodearch.data.source.remote.RemoteDataSource
import com.firdous.cleancodearch.data.source.remote.network.ApiService
import com.firdous.cleancodearch.domain.repository.IMovieRepo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val databaseModule = module {

    factory { get<MovieDatabase>().movieDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            MovieDatabase::class.java, "movies.db"
        ).fallbackToDestructiveMigration()
            .build()
    }
}

    val networkModule = module {
        single {
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
        }
        single {
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(get())
                .build()
            retrofit.create(ApiService::class.java)
        }
    }

    val repositoryModule = module {
        single { LocalDataSource(get()) }
        single { RemoteDataSource(get()) }
        single<IMovieRepo> {
            MovieRepo(
                get(),
                get(),
                androidContext()
            )
        }
}