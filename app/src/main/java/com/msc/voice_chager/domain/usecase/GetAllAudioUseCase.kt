package com.msc.voice_chager.domain.usecase

import android.content.Context
import com.msc.voice_chager.domain.layer.Audio
import com.msc.voice_chager.utils.AudioManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetAllAudioUseCase @Inject constructor(@ApplicationContext val context: Context) : UseCase<GetAllAudioUseCase.Param, List<Audio>>() {
    class Param : UseCase.Param()

    override suspend fun execute(param: Param): List<Audio> {
        val audioManager = AudioManager(context.contentResolver)
        return audioManager.getAllAudio()
    }
}