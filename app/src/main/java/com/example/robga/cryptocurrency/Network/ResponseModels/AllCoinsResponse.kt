package com.example.robga.cryptocurrency.Network.ResponseModels

import com.google.gson.annotations.SerializedName

data class AllCoinsResponse(
        @SerializedName("data") val data: List<Data> = listOf(),
        @SerializedName("metadata") val metadata: Metadata = Metadata()
) {

    data class Metadata(
            @SerializedName("timestamp") val timestamp: Int = 0, // 1529340774
            @SerializedName("num_cryptocurrencies") val numCryptocurrencies: Int = 0, // 1630
            @SerializedName("error") val error: Any = Any() // null
    )

    data class Data(
            @SerializedName("id") val id: Int = 0, // 2866
            @SerializedName("name") val name: String = "", // Sentinel Protocol
            @SerializedName("symbol") val symbol: String = "", // UPP
            @SerializedName("website_slug") val websiteSlug: String = "" // sentinel-protocol
    )
}