package com.example.test1.googlebooks

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApi {
    @GET("volumes")
    suspend fun getBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 3,
        @Query("fields") fields: String = "items(id,volumeInfo(title,authors,pageCount,industryIdentifiers,imageLinks))"
    ): Response<BooksResponse>
}