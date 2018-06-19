package com.example.robga.cryptocurrency

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
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
import com.example.robga.cryptocurrency.Utils.Utils
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_alert_dialog_layout.view.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.InputFilter
import android.widget.ImageView
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    private lateinit var currencyViewModel: CurrencyViewModel
    private lateinit var currencyRecyclerViewAdapter: CurrencyAdapter
    private var context: Context? = null
    private val INTERNET_PERMISSION_FLAG = 1
    lateinit var backImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permission()
        backImageView = back_image_View
        context = this
        val addCurrencyPair = add_currency_pair
        addCurrencyPair.setOnClickListener {
            showAlertDialog()
        }
        currencyViewModel = ViewModelProviders.of(this).get(CurrencyViewModel::class.java)
        currencyRecyclerViewAdapter = CurrencyAdapter()
        val currencyRecyclerView = currency_RecyclerView
        currencyRecyclerView.layoutManager = LinearLayoutManager(this)
        currencyRecyclerView.adapter = currencyRecyclerViewAdapter
        currencyRecyclerViewAdapter.onLongClicklistener = object : CurrencyAdapter.onLongClickListener {
            override fun onLongClick(currencyEntity: CurrencyEntity) {
                val builder = AlertDialog.Builder(context as MainActivity)
                builder.setMessage(getString(R.string.delete_pair))
                        .setCancelable(true)
                        .setNegativeButton(getString(R.string.cancel)) { dialog, which -> }
                        .setPositiveButton(getString(R.string.delete)) { dialog, id ->
                            currencyViewModel.deleteCurrency(currencyEntity)
                        }
                val alert = builder.create()
                alert.show()
            }
        }
        currencyRecyclerViewAdapter.onClicklistener = object : CurrencyAdapter.onClickListener {
            override fun onClick(currencyEntity: CurrencyEntity) {
                val topListFragment = TopListFragment()
                topListFragment.currencyEntity = currencyEntity
                backImageView.visibility = View.VISIBLE
                backImageView.setOnClickListener {
                    this@MainActivity.onBackPressed()
                }
                this@MainActivity.supportFragmentManager.beginTransaction()
                        .add(R.id.top_list_fragment_container, topListFragment).addToBackStack(null)
                        .commitAllowingStateLoss()
            }
        }
        currencyViewModel.getAllCurrency().observe(this, Observer {
            if (it != null) {
                currencyRecyclerViewAdapter.update(it)
                if (it.isNotEmpty()) {
                    please_add_currency_title.visibility = View.GONE
                    Utils.updateCurrentCurrencyList(it, currencyViewModel)
                } else {
                    please_add_currency_title.visibility = View.VISIBLE
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

        // text all caps
        val inputFilter = arrayOf<InputFilter>(InputFilter.AllCaps())

        val autoCompleteTextView = dialogView.autoCompleteTextView
        autoCompleteTextView.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CurrencyApplication.instance.coins))
        autoCompleteTextView.filters = inputFilter

        val secondCurrency = dialogView.secondCurrency
        secondCurrency.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CurrencyApplication.instance.fiatCurrency))
        secondCurrency.filters = inputFilter

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
                        val str = response?.body()?.string()?.split(":")!![1]
                        val a = str.substring(0, str.length - 1).toDouble()
                        currencyViewModel.insertCurrency(CurrencyEntity(text1, text2, a))
                    } catch (e: Exception) {
                        Toast.makeText(context, getString(R.string.cant_find_pair), Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            INTERNET_PERMISSION_FLAG -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                } else {
                    this.finish()
                }
                return
            }

        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            backImageView.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }

    private fun permission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.INTERNET)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.INTERNET),
                        INTERNET_PERMISSION_FLAG)
            }
        }
    }
}
