package com.mahmouddev.appweather.dependency

import android.content.Context
import androidx.room.Room
import com.mahmouddev.appweather.retrofit.ApiHelper
import com.mahmouddev.appweather.retrofit.ApiHelperImpl
import com.mahmouddev.appweather.retrofit.ApiService
import com.mahmouddev.appweather.room.AppDatabase
import com.mahmouddev.appweather.room.DatabaseHelper
import com.mahmouddev.appweather.room.DatabaseHelperImpl
import com.mahmouddev.appweather.util.Constants.BASE_URL
import com.mahmouddev.appweather.util.Constants.HOST
import com.mahmouddev.appweather.util.Constants.KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {



    @Provides
    @Singleton
    fun builder(): OkHttpClient.Builder = OkHttpClient.Builder()
        .callTimeout(2, TimeUnit.MINUTES)
        .connectTimeout(2, TimeUnit.MINUTES)
        .readTimeout(2, TimeUnit.MINUTES)
        .writeTimeout(2, TimeUnit.MINUTES)
        .addInterceptor { chain ->
        val request: Request =
            chain.request().newBuilder()
                .addHeader("x-rapidapi-key", KEY)
                .addHeader("x-rapidapi-host", HOST)
                .build()
        chain.proceed(request)
        }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper

    @Singleton
    @Provides
    fun buildRoomDB(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "db-weather"
    ).build()

    @Provides
    fun daoWeather(db: AppDatabase) = db.daoWeather()

    @Provides
    @Singleton
    fun provideDBHelper(dbHelper: DatabaseHelperImpl): DatabaseHelper = dbHelper


}