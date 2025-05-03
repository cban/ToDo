package com.application.todo.di

import android.content.Context
import com.application.todo.BuildConfig
import com.application.todo.data.datasource.LocationDataSource
import com.application.todo.data.datasource.LocationDataSourceImpl
import com.application.todo.data.datasource.network.WeatherApi
import com.application.todo.data.datasource.network.WeatherDataSource
import com.application.todo.data.datasource.network.WeatherDataSourceImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    fun provideApiKeyInterceptor(): Interceptor =
        Interceptor { chain ->
            val original = chain.request()
            val newUrl = original.url.newBuilder()
                .addQueryParameter("key", BuildConfig.WEATHER_API_KEY)
                .build()

            val newRequest = original.newBuilder()
                .url(newUrl)
                .build()
            chain.proceed(newRequest)
        }

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        apiKeyInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .build()
    }

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    fun provideRemoteApi(
        retrofit: Retrofit
    ): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    @Provides
    fun provideWeatherDataSource(
        weatherApi: WeatherApi
    ): WeatherDataSource {
        return WeatherDataSourceImpl(weatherApi)
    }

    @Provides
    fun provideLocationDataSource(
        @ApplicationContext context: Context
    ): LocationDataSource {
        return LocationDataSourceImpl(context)
    }
}
