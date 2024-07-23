package com.msc.voice_chager.component.record

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.msc.voice_chager.domain.layer.RecordingModel
import com.msc.voice_chager.service.ServiceRecordingVoice
import com.msc.voice_chager.service.ServiceRecordingVoice.LocalBinder
import com.msc.voice_chager.service.ServiceRecordingVoice.OnRecordingStatusChangedListener
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.jvm.internal.Intrinsics

@HiltViewModel
class RecordingViewModel @Inject constructor(@ApplicationContext val context : Context) : ViewModel() {
    var recordingVoice: ServiceRecordingVoice? = null
    val mutableLiveData: MutableLiveData<Int> = MutableLiveData()
    val recording: MutableLiveData<RecordingModel> = MutableLiveData()
    val serviceRecordResume = MutableLiveData(false)
    val serviceRecording = MutableLiveData(false)
    val toastMsg = MutableLiveData<Int>()
    val observableInt = MutableLiveData(0)
    val observableBoolean = MutableLiveData(false)

    val onScheduledRecordingListener: OnRecordingStatusChangedListener =
        object : OnRecordingStatusChangedListener {
            override fun onAmplitudeInfo(i: Int) {
                mutableLiveData.postValue(i)
            }

            override fun onPauseRecording() {
                serviceRecordResume.postValue(false)
                serviceRecordResume.postValue(false)
            }

            override fun onResumeRecording() {
                serviceRecordResume.postValue(true)
            }

            override fun onSkipRecording() {
                serviceRecordResume.postValue(false)
                serviceRecording.postValue(false)
                observableInt.postValue(0)
            }

            override fun onStartedRecording() {
                serviceRecording.postValue(true)
                serviceRecordResume.postValue(true)
            }

            override fun onStopRecording(recordingModel: RecordingModel) {
                serviceRecordResume.postValue(false)
                serviceRecording.postValue(false)
                observableInt.postValue(0)
                recording.postValue(recordingModel)
            }

            override fun onTimerChanged(i: Int) {
                observableInt.postValue(i)
            }
        }
    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            this@RecordingViewModel.recordingVoice = (iBinder as LocalBinder).service
            recordingVoice?.let {
                observableBoolean.postValue(true)
                it.setOnRecordingStatusChangedListener(this@RecordingViewModel.onScheduledRecordingListener)
                serviceRecording.postValue(it.isRecording)
                serviceRecordResume.postValue(it.isResumeRecording)
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            if (this@RecordingViewModel.recordingVoice != null) {
                val serviceRecordingVoice = this@RecordingViewModel.recordingVoice
                Intrinsics.checkNotNull(serviceRecordingVoice)
                serviceRecordingVoice!!.setOnRecordingStatusChangedListener(null)
                this@RecordingViewModel.recordingVoice = null
            }
            observableBoolean.postValue(false)
        }
    }


    fun connectService(intent: Intent) {
        try {
            context.startService(intent)
            context.bindService(intent, this.connection, 1)
        } catch (e: Exception) {
            println(Intrinsics.stringPlus("RecordViewModel.connectService e = ", e))
        }
    }

    fun serviceStartStop(intent: Intent) {
        try {
            if (observableBoolean.value!!) {
                context.unbindService(this.connection)
                if (!serviceRecording.value!!) {
                    context.stopService(intent)
                }
                val serviceRecordingVoice = this.recordingVoice
                if (serviceRecordingVoice != null) {
                    val onRecordingStatusChangedListener: OnRecordingStatusChangedListener? = null
                    serviceRecordingVoice.setOnRecordingStatusChangedListener(null)
                }
                this.recordingVoice = null
                observableBoolean.postValue(false)
            }
        } catch (e: Exception) {
            println(Intrinsics.stringPlus("RecordViewModel.disconnectAndStopService e = ", e))
        }
    }

    fun recStart() {
        Log.e("cc----", "recStart: ")
        val serviceRecordingVoice = this.recordingVoice
        serviceRecordingVoice?.startRecording(0)
        serviceRecording.postValue(true)
        serviceRecordResume.postValue(true)
    }

    fun recStop() {
        Log.e("cc----", "recStop: ")
        val serviceRecordingVoice = this.recordingVoice
        serviceRecordingVoice?.recordingStop()
    }

    fun recPause() {
        Log.e("cc----", "recPause: ")
        val serviceRecordingVoice = this.recordingVoice
        serviceRecordingVoice?.pauseRecording()
    }

    fun recResume() {
        Log.e("cc----", "recResume: ")
        val serviceRecordingVoice = this.recordingVoice
        serviceRecordingVoice?.resumeRecording()
    }

    fun recSkipFile() {
        Log.e("cc----", "recSkipFile: ")
        val serviceRecordingVoice = this.recordingVoice
        serviceRecordingVoice?.fileRecordSkip()
    }
}
