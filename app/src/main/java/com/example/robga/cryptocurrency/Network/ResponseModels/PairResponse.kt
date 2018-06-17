package com.example.robga.cryptocurrency.Network.ResponseModels

import com.google.gson.annotations.SerializedName

data class PairResponse(
        @SerializedName("USD")
        val uSD: Double // 6509.93
)