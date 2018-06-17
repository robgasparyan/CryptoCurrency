package com.example.robga.cryptocurrency.Database.Dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.example.robga.cryptocurrency.Database.Entity.CurrencyEntity

/**
 * Created by robga on 18-Jun-18.
 */
@Dao
interface CurrencyDao  {
    @Query("SELECT * FROM currency_table")
    fun getAllCurrency():LiveData<List<CurrencyEntity>>
}