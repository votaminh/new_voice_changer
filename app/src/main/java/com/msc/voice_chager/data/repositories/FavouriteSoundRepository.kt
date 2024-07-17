package com.msc.voice_chager.data.repositories

import com.msc.voice_chager.data.database.AppDatabase
import com.msc.voice_chager.domain.layer.DetailsSound
import javax.inject.Inject

class FavouriteSoundRepository @Inject constructor(val appDatabase: AppDatabase) {
    fun insert(sound: DetailsSound) : Long {
        return appDatabase.favouriteDao().insert(sound)
    }

    fun getFavourite(soundRes : Int) : DetailsSound? {
        return appDatabase.favouriteDao().getFavourite(soundRes)
    }

    fun remove(sound: DetailsSound) : Int{
        return appDatabase.favouriteDao().remove(sound)
    }

    fun getList() : List<DetailsSound> {
        return appDatabase.favouriteDao().getAll()
    }
}