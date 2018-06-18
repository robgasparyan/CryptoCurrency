package com.example.robga.cryptocurrency

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.example.robga.cryptocurrency.Adapters.CurrencyAdapter
import com.example.robga.cryptocurrency.Database.DBService
import com.example.robga.cryptocurrency.Database.Entity.CurrencyEntity
import com.example.robga.cryptocurrency.Database.ViewModel.CurrencyViewModel
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_alert_dialog_layout.view.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var currencyViewModel: CurrencyViewModel
    private lateinit var currencyRecyclerViewAdapter: CurrencyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val addCurrencyPair = add_currency_pair
        addCurrencyPair.setOnClickListener {
            showAlertDialog()
        }
        currencyViewModel = ViewModelProviders.of(this).get(CurrencyViewModel::class.java)
        currencyRecyclerViewAdapter= CurrencyAdapter()
        val currencyRecyclerView=currency_RecyclerView
        currencyRecyclerView.layoutManager=LinearLayoutManager(this)
        currencyRecyclerView.adapter=currencyRecyclerViewAdapter
        currencyViewModel.getAllCurrency().observe(this, Observer {
            if(it!=null){
                currencyRecyclerViewAdapter.update(it)
                if(it.isNotEmpty()){
                    please_add_currency_title.visibility= View.GONE
                }
            }
        })
    }

    private fun showAlertDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog_layout, null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setTitle("Select")
        dialogBuilder.setCancelable(true)
        val autoCompleteTextView = dialogView.autoCompleteTextView
        autoCompleteTextView.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CurrencyApplication.instance.coins))
        val secondCurrency = dialogView.secondCurrency
        val addCurrencyPairTextView = dialogView.add_currency_pair_TextView
        addCurrencyPairTextView.setOnClickListener {
            val text1 = autoCompleteTextView.text.toString()
            val text2 = secondCurrency.text.toString()
            val context = this
            if (text1.isEmpty()) {
                Toast.makeText(context, getString(R.string.select_crypto), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (text2.isEmpty()) {
                Toast.makeText(context, getString(R.string.selec_currency), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            CurrencyApplication.instance.getNetworkService().getAnswers(text1, text2).enqueue(object : Callback<ResponseBody?> {
                override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                }

                override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>?) {
                    try {
                        val a: Double = JSONObject(response?.body()?.string()).get(text2) as Double
                        currencyViewModel.insertCurrency(CurrencyEntity(text1, text2, a))
                    } catch (e: JSONException) {
                        Toast.makeText(context, getString(R.string.cant_find_pair), Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}
