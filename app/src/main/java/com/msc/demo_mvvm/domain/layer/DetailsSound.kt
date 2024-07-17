package com.msc.demo_mvvm.domain.layer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite")
data class DetailsSound(
    val name : String,
    @PrimaryKey val soundRes : Int,
    val iconAssetPath : String,
    var flag : Int = 0
){
    var isFavourite = false
    var isPlaying = false

    companion object {
        const val ADS_FLAG = 1
    }
}