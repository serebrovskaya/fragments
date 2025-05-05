package com.example.test1.googlebooks

import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApi {

    @GET("volumes")
    suspend fun getBooks(
        @Query("q") query: String
        //@Query("maxResults") maxResults: Int = 3//,
        //@Query("key") apiKey: String
    ): BooksResponse
}