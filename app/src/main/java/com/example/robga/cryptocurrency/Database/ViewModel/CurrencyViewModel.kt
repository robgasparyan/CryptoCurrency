package com.example.robga.cryptocurrency.Database.ViewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.example.robga.cryptocurrency.Database.DBService
import com.example.robga.cryptocurrency.Database.Entity.CurrencyEntity
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by robga on 18-Jun-18.
 */
class CurrencyViewModel : AndroidViewModel {
    constructor(application: Application) : super(application)

    private var INSERT_CURRENCY = 1
    private var UPDATE_CURRENCY = 2
    private var DELETE_CURRENCY = 3
    private var digit = AtomicInteger(5)
    private val appDb: DBService = DBService.getDataBase(this.getApplication())

    fun getAllCurrency(): LiveData<List<CurrencyEntity>> {
        return appDb.daoCart().getAllCurrency()
    }

    fun insertCurrency(currencyEntity: CurrencyEntity) {
        digit.set(INSERT_CURRENCY)
        addAsynTask(digit, appDb).execute(currencyEntity)
    }

    fun updateCurrency(currencyEntity: CurrencyEntity) {
        digit.set(UPDATE_CURRENCY)
        addAsynTask(digit, appDb).execute(currencyEntity)
    }

    fun deleteCurrency(currencyEntity: CurrencyEntity) {
        digit.set(DELETE_CURRENCY)
        addAsynTask(digit, appDb).execute(currencyEntity)
    }

    class addAsynTask(var flag: AtomicInteger, db: DBService) : AsyncTask<CurrencyEntity, Void, Void>() {
        private var dbService = db
        override fun doInBackground(vararg params: CurrencyEntity): Void? {
            when (flag.get()) {
                1 -> {
                    dbService.daoCart().insertCurrency(params[0])
                }
                2 -> {
                    dbService.daoCart().updateCurrency(params[0])
                }
                3 -> {
                    dbService.daoCart().deleteCurrency(params[0])
                }
            }
            return null
        }

    }
}