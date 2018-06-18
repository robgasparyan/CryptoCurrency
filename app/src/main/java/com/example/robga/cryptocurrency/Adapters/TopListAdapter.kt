package com.example.robga.cryptocurrency.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.robga.cryptocurrency.Database.Entity.CurrencyEntity
import com.example.robga.cryptocurrency.R
import kotlinx.android.synthetic.main.currency_item.view.*

/**
 * Created by robga on 19-Jun-18.
 */
class TopListAdapter: RecyclerView.Adapter<TopListAdapter.ViewHolder>() {
    private var list: List<CurrencyEntity> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.currency_item, parent, false))
    }

    fun update(list: List<CurrencyEntity>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.firstCurrency.text = list[position].currencyFirst
        holder.valueConvertCurrency.text = list[position].currencyConvertValue.toString()
        holder.secondCurrency.text = list[position].currencySecond
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyItemContainer: LinearLayout = itemView.currency_item_container
        val firstCurrency: TextView = itemView.first_currency
        val valueConvertCurrency: TextView = itemView.convert_value
        val secondCurrency: TextView = itemView.second_currency
    }
}