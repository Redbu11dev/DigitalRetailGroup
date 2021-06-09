package com.vashkpi.digitalretailgroup.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.vashkpi.digitalretailgroup.AppConstants.DEFAULT_SHARED_PREFERENCES_NAME
import com.vashkpi.digitalretailgroup.data.local.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    @Singleton
//    @Provides
//    fun provideRetrofit(client: OkHttpClient): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(DEFAULT_API_BASE_URL)
//            .client(client)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    @Singleton
//    @Provides
//    fun provideOkHttpClient(preferencesRepository: PreferencesRepository, loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
//        return OkHttpClient.Builder()
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
//            .addInterceptor(loggingInterceptor)
//            .retryOnConnectionFailure(true)
//            .connectTimeout(AppConstants.API_TIMEOUT_CONNECTION_SECONDS, TimeUnit.SECONDS)
//            .readTimeout(AppConstants.API_TIMEOUT_READ_SECONDS, TimeUnit.SECONDS)
//            .writeTimeout(AppConstants.API_TIMEOUT_WRITE_SECONDS, TimeUnit.SECONDS)
//            .build()
//    }
//
//    @Singleton
//    @Provides
//    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
//        return HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
//    }
//
//    @Singleton
//    @Provides
//    fun provideApiInterface(retrofit: Retrofit): ApiInterface {
//        return retrofit.create(ApiInterface::class.java)
//    }
//
//    @Singleton
//    @Provides
//    fun provideApiRepository(@ApplicationContext appContext: Context, apiInterface: ApiInterface): ApiRepository {
//        return ApiRepository(appContext, apiInterface)
//    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(DEFAULT_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideSharedPreferencesRepository(sharedPreferences: SharedPreferences, gson: Gson): PreferencesRepository {
        return PreferencesRepository(sharedPreferences, gson)
    }

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

}