package com.example.robga.cryptocurrency

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.robga.cryptocurrency.Adapters.CurrencyAdapter
import com.example.robga.cryptocurrency.Database.Entity.CurrencyEntity
import com.example.robga.cryptocurrency.Database.ViewModel.CurrencyViewModel
import com.example.robga.cryptocurrency.Utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_alert_dialog_layout.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.text.InputFilter
import android.widget.ImageView
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    private lateinit var currencyViewModel: CurrencyViewModel
    private lateinit var currencyRecyclerViewAdapter: CurrencyAdapter
    private var context: Context? = null
    private lateinit var list: List<CurrencyEntity>
    lateinit var backImageView: ImageView
    private var isNeedUpdate = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        backImageView = back_image_View
        context = this
        isNeedUpdate = true
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
                list = it
                if (it.isNotEmpty()) {
                    please_add_currency_title.visibility = View.GONE
                } else {
                    please_add_currency_title.visibility = View.VISIBLE
                }
                if (isNeedUpdate) {
                    Utils.updateCurrentCurrencyList(list, currencyViewModel)
                    isNeedUpdate = false
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
        val inputFilter = arrayOf<InputFilter>(InputFilter.AllCaps())

        val autoCompleteTextView = dialogView.autoCompleteTextView
        autoCompleteTextView.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CurrencyApplication.instance.coins))
        autoCompleteTextView.filters = inputFilter

        val secondCurrency = dialogView.secondCurrency
        secondCurrency.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CurrencyApplication.instance.fiatCurrency))
        secondCurrency.filters = inputFilter

        val addCurrencyPairTextView = dialogView.add_currency_pair_TextView
        val cancelTextView = dialogView.cancelTextView
        val alertDialog = dialogBuilder.create()
        cancelTextView.setOnClickListener {
            alertDialog.dismiss()
        }
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
            for (item in list) {
                if (item.currencyFirst == text1 && item.currencySecond == text2) {
                    Toast.makeText(context, getString(R.string.duplicate_pair), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            CurrencyApplication.instance.getNetworkService().getAnswers(text1, text2).enqueue(object : Callback<ResponseBody?> {
                override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                }

                override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>?) {
                    try {
                        val str = response?.body()?.string()?.split(":")!![1]
                        val a = str.substring(0, str.length - 1).toDouble()
                        currencyViewModel.insertCurrency(CurrencyEntity(text1, text2, a))
                        alertDialog.dismiss()
                    } catch (e: Exception) {
                        Toast.makeText(context, getString(R.string.cant_find_pair), Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }


        alertDialog.show()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            backImageView.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }
}
