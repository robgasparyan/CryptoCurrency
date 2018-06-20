package com.example.robga.cryptocurrency.Database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.robga.cryptocurrency.AppConstants
import com.example.robga.cryptocurrency.Database.Dao.CurrencyDao
import com.example.robga.cryptocurrency.Database.Entity.CurrencyEntity

/**
 * Created by robga on 18-Jun-18.
 */
@Database(entities = [(CurrencyEntity::class)], version = AppConstants.dbVersion, exportSchema = false)
abstract class DBService : RoomDatabase() {
    companion object {
        private var INSTANCE: DBService? = null
        fun getDataBase(context: Context): DBService {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, DBService::class.java, AppConstants.dbName)
                        .build()
            }
            return INSTANCE as DBService
        }
    }

    abstract fun daoCart(): CurrencyDao
}