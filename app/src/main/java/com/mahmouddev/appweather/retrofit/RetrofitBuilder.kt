package com.mahmouddev.appweather.retrofit

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitBuilder {

    private const val BASE_URL = "https://community-open-weather-map.p.rapidapi.com"
    private const val KEY = "de2d7e7b99mshfd6f985597e6b9fp1447e3jsn470eabff9943"
    private const val HOST = "community-open-weather-map.p.rapidapi.com"

    private fun getRetrofit(): Retrofit {
        try {


            val builder = OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)

            builder.addInterceptor { chain ->
                val request: Request =
                    chain.request().newBuilder()
                        .addHeader("x-rapidapi-key", KEY)
                        .addHeader("x-rapidapi-host", HOST)

                        .build()
                chain.proceed(request)
            }

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        } catch (ex: Exception) {
            return getRetrofit()
        }
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)



}