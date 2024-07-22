package com.msc.voice_chager.utils

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import com.msc.voice_chager.domain.layer.Audio


class AudioManager(private val contentResolver: ContentResolver) {

    fun getAllAudio(): List<Audio> {
        val audios = mutableListOf<Audio>()

        val cursor: Cursor? = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.DATE_MODIFIED
            ),
            null,
            null,
            "${MediaStore.Audio.Media.DATE_MODIFIED} DESC"
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val pathIndex = it.getColumnIndex(MediaStore.Audio.Media.DATA)
                val dateAddedIndex = it.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)
                val dateModifiedIndex = it.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED)

                do {
                    val path = it.getString(pathIndex)
                    val dateAdded = it.getLong(dateAddedIndex)
                    val dateModified = it.getLong(dateModifiedIndex)

                    val audio = Audio(path, dateAdded, dateModified)
                    audios.add(audio)
                } while (it.moveToNext())
            }
        }

        return audios
    }
}