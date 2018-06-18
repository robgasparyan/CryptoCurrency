package com.example.robga.cryptocurrency.Database.Entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by robga on 18-Jun-18.
 */
@Entity(tableName = "currency_table")
data class CurrencyEntity(
        @ColumnInfo(name = "currency_first")
        var currencyFirst: String,
        @ColumnInfo(name = "currency_second")
        var currencySecond: String,
        @ColumnInfo(name = "currency_converted_value")
        var currencyConvertValue: Double) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "currency_id")
    var currencyId: Int = 0

}