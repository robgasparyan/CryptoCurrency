package com.example.robga.cryptocurrency

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.add_currency_pair
import kotlinx.android.synthetic.main.custom_alert_dialog_layout.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val addCurrencyPair = add_currency_pair
        addCurrencyPair.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun showAlertDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog_layout, null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setTitle("Select")
        val autoCompleteTextView = dialogView.autoCompleteTextView
        autoCompleteTextView.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CurrencyApplication.instance.coins))
        val secondCurrency = dialogView.secondCurrency
        val addCurrencyPairTextView = dialogView.add_currency_pair_TextView
        addCurrencyPairTextView.setOnClickListener {
            val text1=autoCompleteTextView.text.toString()
            val text2=secondCurrency.text.toString()
            if(text1.isEmpty()){
                Toast.makeText(this,getString(R.string.select_crypto),Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if(text2.isEmpty()){
                Toast.makeText(this,getString(R.string.selec_currency),Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            CurrencyApplication.instance.getNetworkService().getAnswers(text1, text2).enqueue(object : Callback<ResponseBody?> {
                override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                }

                override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>?) {
                    val a = JSONObject(response?.body()?.string()).get(text2)
                }
            })
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}
