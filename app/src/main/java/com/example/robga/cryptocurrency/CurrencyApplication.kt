package com.example.robga.cryptocurrency

import android.app.Application
import com.example.robga.cryptocurrency.Network.ApiService
import com.example.robga.cryptocurrency.Network.ResponseModels.AllCoinsResponse
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by robga on 17-Jun-18.
 */
class CurrencyApplication : Application() {

    private lateinit var retrofit: Retrofit

    var coins = arrayListOf<String>()
    var fiatCurrency = arrayListOf<String>()

    override fun onCreate() {
        super.onCreate()
        instance = this

        getNetworkService().getAllCoins("https://api.coinmarketcap.com/v2/listings/").enqueue(object: Callback<AllCoinsResponse?> {
            override fun onFailure(call: Call<AllCoinsResponse?>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<AllCoinsResponse?>?, response: Response<AllCoinsResponse?>?) {
                response?.body()?.data?.forEach {
                    coins.add(it.symbol)
                }
            }
        })

        CurrencyApplication.instance.getNetworkService().getFiatCurency("https://openexchangerates.org/api/currencies.json").enqueue(object: Callback<ResponseBody?> {
            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
            }

            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>?) {
                JSONObject(response?.body()?.string()).keys().forEach {
                    fiatCurrency.add(it)
                }
            }
        })

    }

    companion object {
        @JvmStatic
        lateinit var instance: CurrencyApplication
            private set
    }

    fun getNetworkService(): ApiService {
        return this.getClient(AppConstants.baseURL).create(ApiService::class.java)
    }

    private fun getClient(baseUrl: String): Retrofit {

        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit
    }
}