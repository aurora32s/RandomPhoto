package com.haman.core.network.di

import com.haman.core.network.BuildConfig
import com.haman.core.network.image.ImageApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideImageApiService(
        retrofit: Retrofit
    ): ImageApiService = retrofit.create(ImageApiService::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converter: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(converter)
            .baseUrl("https://picsum.photos")
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ConvertFactoryModule {
    @Provides
    fun provideJsonConverter(): Converter.Factory {
        return Json {
            coerceInputValues = true // null 값으로 들어오면 default value로 처리
            ignoreUnknownKeys = true // 프로퍼티로 선언되지 않은 key는 무시
        }.asConverterFactory("application/json".toMediaType())
    }
}

@Module
@InstallIn(SingletonComponent::class)
object HttpClientModule {
    private const val TIMEOUT = 3L

    @Provides
    fun provideHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
}

