package com.msc.demo_mvvm.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.msc.demo_mvvm.domain.layer.DetailsSound

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(sound: DetailsSound) : Long

    @Query("select * from favourite where soundRes=:soundRes")
    fun getFavourite(soundRes : Int) : DetailsSound?

    @Delete()
    fun remove(sound: DetailsSound) : Int

    @Query("select * from favourite")
    fun getAll(): List<DetailsSound>
}