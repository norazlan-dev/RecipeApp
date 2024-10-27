package com.noradev.recipeapp.data.network

import com.google.gson.GsonBuilder
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.noradev.recipeapp.data.model.Root
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {
    @GET("random.php")
    @Headers("Accept: application/json")
    suspend fun getRandomMeal(): Root

    companion object {
        private var BASE_URL =  "https://www.themealdb.com/api/json/v1/1/"

        fun create(): ApiService {
            val httpClient = OkHttpClient.Builder()
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.interceptors().add(logging)
            httpClient.interceptors().add(OkHttpProfilerInterceptor())

            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}