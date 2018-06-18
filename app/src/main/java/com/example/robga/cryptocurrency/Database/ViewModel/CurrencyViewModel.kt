package com.example.robga.cryptocurrency.Database.ViewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.example.robga.cryptocurrency.Database.DBService
import com.example.robga.cryptocurrency.Database.Entity.CurrencyEntity

/**
 * Created by robga on 18-Jun-18.
 */
class CurrencyViewModel : AndroidViewModel {
    constructor(application: Application) : super(application)

    private val appDb: DBService = DBService.getDataBase(this.getApplication())

    fun getAllCurrency(): LiveData<List<CurrencyEntity>> {
        return appDb.daoCart().getAllCurrency()
    }

    fun insertCurrency(currencyEntity: CurrencyEntity) {
        appDb.daoCart().insertCurrency(currencyEntity)
    }
}