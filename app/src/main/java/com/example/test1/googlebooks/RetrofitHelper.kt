package com.example.test1.googlebooks

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit

internal object RetrofitHelper {

    private fun creatConverterFactory(): Converter.Factory {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
        }
        return json.asConverterFactory(contentType)
    }

    fun creatRetrofit():BooksApi{
        println("!!! start retrofit")

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor{chain ->
                try {
                    val request = chain.request()
                        .newBuilder()
                        .addHeader(
                            "Authorization", "107456255038276427352"
                        ).build()

                    println("Request URL: ${request.url}")
                    val response = chain.proceed(request)
                    println("Response code: ${response.code}")
                    println("!!! ${request.headers}")
                    response
                } catch (e: Exception) {
                    println("Network error: ${e.message}")
                    throw e
                }
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .client(okHttpClient)
            .addConverterFactory(creatConverterFactory())
            .build()

        return retrofit.create(BooksApi::class.java)
    }
}