package com.example.robga.cryptocurrency.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.robga.cryptocurrency.Network.ResponseModels.TopListResponse
import com.example.robga.cryptocurrency.R
import kotlinx.android.synthetic.main.top_list_item.view.*

/**
 * Created by robga on 19-Jun-18.
 */
class TopListAdapter : RecyclerView.Adapter<TopListAdapter.ViewHolder>() {
    private var response: TopListResponse? = null
    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.top_list_item, parent, false)
        if (context == null) context = view.context
        return ViewHolder(view)
    }

    fun update(response: TopListResponse?) {
        this.response = response
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        if (response == null || response?.data == null) return 0 else return response?.data?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.exchange.text = response?.data!![position].exchange
        holder.volume24.text = response?.data!![position].volume24h.toString()
        holder.volume24to.text = response?.data!![position].volume24hTo.toString()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exchange: TextView = itemView.exchange
        val volume24: TextView = itemView.volume24h
        val volume24to: TextView = itemView.volume24to
    }
}