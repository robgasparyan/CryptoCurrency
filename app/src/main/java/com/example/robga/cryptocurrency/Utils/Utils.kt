package com.example.robga.cryptocurrency.Utils

import android.widget.Toast
import com.example.robga.cryptocurrency.CurrencyApplication
import com.example.robga.cryptocurrency.Database.Entity.CurrencyEntity
import com.example.robga.cryptocurrency.Database.ViewModel.CurrencyViewModel
import com.example.robga.cryptocurrency.R
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

/**
 * Created by robga on 19-Jun-18.
 */
class Utils {
    companion object {
        fun updateCurrentCurrencyList(currencyList:List<CurrencyEntity>,viewModel: CurrencyViewModel){
            for (currencyItem in currencyList){
                CurrencyApplication.instance.getNetworkService().getAnswers(currencyItem.currencyFirst, currencyItem.currencySecond).enqueue(object : Callback<ResponseBody?> {
                    override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                    }

                    override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>?) {
                        try {
                            val a: Double = JSONObject(response?.body()?.string()).get(currencyItem.currencySecond) as Double
                            if(currencyItem.currencyConvertValue != a){
                                val currencyEntity=CurrencyEntity(currencyItem.currencyFirst, currencyItem.currencySecond, a)
                                currencyEntity.currencyId=currencyItem.currencyId
                                viewModel.updateCurrency(currencyItem)
                            }
                        } catch (e: Exception) {

                        }
                    }
                })
            }

        }
    }
}