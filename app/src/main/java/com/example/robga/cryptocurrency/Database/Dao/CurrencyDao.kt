package com.example.robga.cryptocurrency.Database.Dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.example.robga.cryptocurrency.Database.Entity.CurrencyEntity

/**
 * Created by robga on 18-Jun-18.
 */
@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currency_table")
    fun getAllCurrency(): LiveData<List<CurrencyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrency(currencyEntity: CurrencyEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCurrency(currencyEntity: CurrencyEntity)

    @Delete()
    fun deleteCurrency(currencyEntity: CurrencyEntity)
}