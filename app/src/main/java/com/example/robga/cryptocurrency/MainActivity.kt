package com.example.robga.cryptocurrency

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.EditText
import android.view.LayoutInflater


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
        dialogBuilder.setPositiveButton(getString(R.string.add)) { dialog, which ->

        }
        dialogBuilder.setPositiveButton(getString(R.string.cancel)) { dialog, which ->

        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}
