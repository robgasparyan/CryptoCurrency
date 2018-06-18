package com.example.robga.cryptocurrency.Network.ResponseModels

import com.google.gson.annotations.SerializedName

data class TopListResponse(
        @SerializedName("Response")
        val response: String, // Success
        @SerializedName("Data")
        val data: List<Data>
) {
    data class Data(
            val exchange: String, // Kraken
            val fromSymbol: String, // BTC
            val toSymbol: String, // USD
            val volume24h: Double, // 3617.4853282199992
            val volume24hTo: Double // 23760161.28948807
    )
}