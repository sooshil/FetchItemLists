package com.sukajee.itemlist.di

import com.sukajee.itemlist.data.remote.ItemsApi
import com.sukajee.itemlist.data.repository.ItemsRepositoryImpl
import com.sukajee.itemlist.domain.repository.ItemsRepository
import com.sukajee.itemlist.presentation.ItemsViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://fetch-hiring.s3.amazonaws.com/"
val itemListModule = module {

    // HttpInterceptor
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    // Retrofit
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    // API Service
    single<ItemsApi> {
        val retrofit: Retrofit = get()
        retrofit.create(ItemsApi::class.java)
    }

    // Repository
    single<ItemsRepository> { ItemsRepositoryImpl(get()) }

    viewModel {
        ItemsViewModel(get())
    }
}