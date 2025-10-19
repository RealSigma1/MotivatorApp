package com.example.motivator

import retrofit2.http.GET
import retrofit2.http.Query

interface QuoteApiService {
    @GET("random")
    suspend fun getRandomQuote(
        @Query("tags") tags: String? = null
    ): RemoteQuote
}