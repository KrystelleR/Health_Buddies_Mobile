package com.varsitycollege.xbcad.healthbuddies

import retrofit2.Call
import retrofit2.http.GET

interface QuoteService {
    @GET("random")
    fun getRandomQuote(): Call<data.Quote>
}
