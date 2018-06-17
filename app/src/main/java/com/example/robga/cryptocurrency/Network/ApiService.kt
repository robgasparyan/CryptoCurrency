package com.example.robga.cryptocurrency.Network

import com.example.robga.cryptocurrency.Network.ResponseModels.PairResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Created by robga on 18-Jun-18.
 */
interface ApiService {
    @GET("/data/price?fsym=BTC&tsyms=USD")
    fun getAnswers(): Call<PairResponse>
}