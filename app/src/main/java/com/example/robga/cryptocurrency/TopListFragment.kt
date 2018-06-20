package com.example.robga.cryptocurrency

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.robga.cryptocurrency.Adapters.TopListAdapter
import com.example.robga.cryptocurrency.Database.Entity.CurrencyEntity
import com.example.robga.cryptocurrency.Network.ResponseModels.TopListResponse
import kotlinx.android.synthetic.main.fragment_layout.*
import kotlinx.android.synthetic.main.fragment_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by robga on 19-Jun-18.
 */
class TopListFragment : Fragment() {
    var currencyEntity: CurrencyEntity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_layout, container, false)
        val topListRecyclerView = view.topList_recycler_view
        topListRecyclerView.layoutManager = LinearLayoutManager(context)
        val topListAdapter = TopListAdapter()
        topListRecyclerView.adapter = topListAdapter

        if (currencyEntity == null) {
            activity?.onBackPressed()
        } else {
            CurrencyApplication.instance.getNetworkService().getTopListForItem(currencyEntity!!.currencyFirst, currencyEntity!!.currencySecond).enqueue(object : Callback<TopListResponse?> {
                override fun onFailure(call: Call<TopListResponse?>?, t: Throwable?) {
                }

                override fun onResponse(call: Call<TopListResponse?>?, response: Response<TopListResponse?>?) {
                    if (response?.body() != null) {
                        topListAdapter.update(response.body())

                        does_not_have_top_list.visibility = if (!response.body()?.data!!.isEmpty()) View.GONE else View.VISIBLE
                    }
                }
            })
        }

        return view
    }
}