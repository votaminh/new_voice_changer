package com.msc.voice_chager.component.import_file

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msc.voice_chager.domain.layer.Audio
import com.msc.voice_chager.domain.usecase.GetAllAudioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImportFileViewModel @Inject constructor(
    val getAllAudioUseCase: GetAllAudioUseCase
): ViewModel() {

    val audiosLive = MutableLiveData<List<Audio>>()

    fun getAllAudio(){
        viewModelScope.launch{
            audiosLive.postValue(getAllAudioUseCase.execute(GetAllAudioUseCase.Param()))
        }
    }
}