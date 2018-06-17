package com.example.robga.cryptocurrency

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import com.example.robga.cryptocurrency.Network.ResponseModels.PairResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_alert_dialog_layout.view.*
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
        val addCurrencyPairTextView = dialogView.add_currency_pair_TextView
        addCurrencyPairTextView.setOnClickListener {
            CurrencyApplication.instance.getNetworkService().getAnswers().enqueue(object : Callback<PairResponse?> {
                override fun onFailure(call: Call<PairResponse?>?, t: Throwable?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(call: Call<PairResponse?>?, response: Response<PairResponse?>?) {
               Log.i("sdf","sf")
                }
            })
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}
