package com.example.robga.cryptocurrency

import android.app.Application
import com.example.robga.cryptocurrency.Network.ApiService
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit


/**
 * Created by robga on 17-Jun-18.
 */
class CurrencyApplication : Application() {

    private var retrofit: Retrofit? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
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
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofit!!
    }
}