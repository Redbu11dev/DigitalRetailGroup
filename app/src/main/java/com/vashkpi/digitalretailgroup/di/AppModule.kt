package com.vashkpi.digitalretailgroup.di

import android.content.Context
import com.google.gson.Gson
import com.vashkpi.digitalretailgroup.AppConstants
import com.vashkpi.digitalretailgroup.AppConstants.DEFAULT_API_BASE_URL
import com.vashkpi.digitalretailgroup.data.api.ApiRepository
import com.vashkpi.digitalretailgroup.data.api.ApiService
import com.vashkpi.digitalretailgroup.data.local.DataStoreRepository
import com.vashkpi.digitalretailgroup.data.local.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(DEFAULT_API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val credentials = Credentials.basic("user", "user")
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .header("Authorization", credentials)
                        .build()
                )
            }
//            .addInterceptor { chain ->
//                val newFullUrl = chain.request().url().toString()
//                    .replace(AppConstants.DEFAULT_API_BASE_URL, preferencesRepository.apiUrl)
//                chain.proceed(
//                    chain.request()
//                        .newBuilder()
//                        .url(newFullUrl)
//                        .build()
//                )
//            }
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .connectTimeout(AppConstants.API_TIMEOUT_CONNECTION_SECONDS, TimeUnit.SECONDS)
            .readTimeout(AppConstants.API_TIMEOUT_READ_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(AppConstants.API_TIMEOUT_WRITE_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideApiRepository(apiService: ApiService): ApiRepository {
        return ApiRepository(apiService)
    }

//    @Singleton
//    @Provides
//    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
//        return appContext.getSharedPreferences(DEFAULT_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
//    }
//
//    @Singleton
//    @Provides
//    fun provideSharedPreferencesRepository(sharedPreferences: SharedPreferences, gson: Gson): PreferencesRepository {
//        return PreferencesRepository(sharedPreferences, gson)
//    }

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStoreRepository {
        return DataStoreRepository(context.dataStore)
    }

}