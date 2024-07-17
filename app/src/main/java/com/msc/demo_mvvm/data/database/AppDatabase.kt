package com.msc.demo_mvvm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.msc.demo_mvvm.data.database.dao.FavouriteDao
import com.msc.demo_mvvm.domain.layer.DetailsSound

@Database(entities = arrayOf(DetailsSound::class), version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "ringtone"
    }

    abstract fun favouriteDao() : FavouriteDao
}