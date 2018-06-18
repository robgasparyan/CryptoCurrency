package com.example.robga.cryptocurrency.Network

import com.example.robga.cryptocurrency.Network.ResponseModels.AllCoinsResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Created by robga on 18-Jun-18.
 */
interface ApiService {
    @GET("/data/price")
    fun getAnswers(@Query("fsym") cryptoSymbol:String, @Query("tsyms") fiatSymbol:String): Call<ResponseBody>

    @GET
    fun getAllCoins(@Url url: String): Call<AllCoinsResponse>
}