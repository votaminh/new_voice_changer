package com.msc.voice_chager.component.record

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.databinding.Observable
import com.msc.voice_chager.R
import com.msc.voice_chager.base.activity.BaseActivity
import com.msc.voice_chager.databinding.ActivityRecordBinding
import com.msc.voice_chager.utils.AppConstant
import com.msc.voice_chager.utils.ViewEx.invisible
import com.msc.voice_chager.utils.ViewEx.visible
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class RecordActivity : BaseActivity<ActivityRecordBinding>() {

    private val viewModel: RecordingViewModel by viewModels()

    var stateAudio: RecordAudioType = RecordAudioType.STATE_PREPARE
    var callback: Observable.OnPropertyChangedCallback? = null
    private var timer: Timer? = null
    var failedAds70:kotlin.Boolean = false
    var isLoadAds70: Boolean = false
    var isCall70: Boolean = false
    var isAdsClick: Boolean = false
    var isFirstCallBack: Boolean = true
    var isFirst: Boolean = true
    private var isRecording = false
    private var playerSecondsElapsed = 0
    private var recorderSecondsElapsed = -1

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, RecordActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityRecordBinding {
        return ActivityRecordBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()

        viewBinding.run {
            toolbar.imvBack.setOnClickListener {
                finish()
            }

            ivReset.setOnClickListener {
//                showDialogNotSaved(
//                    true,
//                    false,
//                    this@RecordingActivity.recordingActivity.getResources()
//                        .getString(R.string.audio_has_not_been_saved_reset),
//                    this@RecordingActivity.getResources().getString(R.string.reset)
//                )
            }

            icStart.setOnClickListener {
                m122x7cd206(
                    icStart
                )
            }

            imgRecord.setOnClickListener {
                if (stateAudio === RecordAudioType.STATE_PREPARE) {
                    Log.e("re---", "invoke: 1111")
                    startRecordAudio()
                    getBindingData().rlyBottom.setVisibility(
                        View.VISIBLE
                    )
                    getBindingData().imgRecord.setVisibility(
                        View.VISIBLE
                    )
                    return@setOnClickListener
                } else if (stateAudio === RecordAudioType.STATE_START) {
                    Log.e("re---", "invoke: 222")
                    pauseRecordAudio()
                    startStopRecording()
                    getBindingData().rlyBottom.setVisibility(
                        View.VISIBLE
                    )
                    getBindingData().imgRecord.setVisibility(
                        View.VISIBLE
                    )
                    return@setOnClickListener
                } else {
                    Log.e("re---", "invoke: 3333")
                    startRecordAudio()
                    getBindingData().rlyBottom.setVisibility(
                        View.VISIBLE
                    )
                    getBindingData().imgRecord.setVisibility(
                        View.VISIBLE
                    )
                    return@setOnClickListener
                }
            }

            imgStop.setOnClickListener {
                isFirst = false
                viewModel.recStop()
                stopAnim()
                stopTimer()

                getWindow().clearFlags(128)
                viewModel.recording.observe(this@RecordActivity){ recordingModel ->
                    if (recordingModel != null) {
                        val bundle = Bundle()
                        bundle.putString(
                            AppConstant.APP_CONSTANT.getKEY_PATH_VOICE(),
                            recordingModel.getPath()
                        )
                        bundle.putString(
                            AppConstant.APP_CONSTANT.getKEY_SCREEN_INTO_VOICE_EFFECTS(),
                            "RecordActivity"
                        )
//                        nextActivity(
//                            ChangeEffectActivity::class.java,
//                            bundle
//                        )
                        getBindingData().icStart.setClickable(
                            true
                        )
                        getBindingData().txtStartRecord.setText(
                            "Start Record"
                        )
                        txtExtra.setVisibility(
                            View.VISIBLE
                        )
                        icStart.setImageResource(
                            R.drawable.ic_start_record
                        )
                    }
                }
                stateAudio = RecordAudioType.STATE_PREPARE
                showHideExRecord()
                null
            }
        }
    }

    fun showHideExRecord() {
        val iArr = IntArray(MobileState.values().size)
        iArr[MobileState.STATE_RINGTONE.ordinal] = 1
        iArr[MobileState.STATE_ALARM.ordinal] = 2
        iArr[MobileState.STATE_NOTIFICATION.ordinal] = 3
        if (iArr[stateAudio.ordinal] == 1) {
            this.recorderSecondsElapsed = 0
            this.playerSecondsElapsed = 0
            getBindingData().imgRecord.isClickable = true
            getBindingData().imgRecord.setImageResource(R.drawable.ic_start_record)
            getBindingData().rlyBottom.visibility = View.GONE
            getBindingData().imgRecord.visibility = View.GONE
            getBindingData().txtStartRecord.setText(R.string.txt_start_record)
        }
    }

    private fun m122x7cd206(icStart: ImageView) {
        getBindingData().txtStartRecord.setText("Recording...")
        startRecordAudio()
        getBindingData().icStart.setClickable(false)
        getBindingData().icStart.setImageResource(R.drawable.ic)
        getBindingData().rlyBottom.setVisibility(View.VISIBLE)
        getBindingData().imgRecord.setVisibility(View.VISIBLE)
        getBindingData().txtExtra.setVisibility(View.GONE)
    }

    fun startRecordAudio() {
        Log.e("VoiceChanger", "startRecordAudio")
        Log.e("record---", "startRecordAudio: ")
        this.stateAudio = RecordAudioType.STATE_START
        getBindingData().imgRecord.setImageResource(R.drawable.ic_pause)
        getBindingData().icStart.setImageResource(R.drawable.ic)
        startStopRecording()
    }

    fun startStopRecording() {
        this.isFirstCallBack = false
        if (viewModel.serviceRecording.value != true) {
            this.isFirst = false
            this.isRecording = true
            viewModel.recStart()
            window.addFlags(128)
            startAnim()
            startTimer()
            return
        }
        pauseRecord()
    }
    private fun pauseRecord() {
        if (viewModel.serviceRecordResume.value == false) {
            viewModel.recResume()
            startAnim()
            startTimer()
            if (this.isFirst) {
                this.isFirst = false
                this.isFirstCallBack = true
            }
            viewModel.observableInt.observe(this@RecordActivity){
                if (isFirstCallBack) {
                    isFirstCallBack = false
                }
            }
            return
        }
        this.isFirst = false
        viewModel.recPause()
        stopAnim()
        stopTimer()
        this.isRecording = false
    }

    fun pauseRecordAudio() {
        Log.e("VoiceChanger", "pauseRecordAudio")
        Log.e("record---", "pauseRecordAudio: ")
        this.stateAudio = RecordAudioType.STATE_PAUSE
        getBindingData().imgRecord.setImageResource(R.drawable.ic_play)
    }

    fun startAnim() {
        Log.e("eee---", "startAnim: ")
        getBindingData().recordLottie.visible()
    }

    fun stopAnim() {
        Log.e("eee---", "stopAnim: ")
        getBindingData().recordLottie.invisible()
    }

    private fun startTimer() {
        Log.e("eee---", "startTimer: ")
        stopTimer()
        val timer = Timer()
        this.timer = timer
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                updateTimer()
            }
        }, 800L, 1000L)
    }

    fun stopTimer() {
        Log.e("eee---", "stopTimer: ")
        val timer = this.timer
        if (timer != null) {
            timer.purge()
            this.timer!!.cancel()
            this.timer = null
        }
    }

    fun updateTimer() {
        runOnUiThread {
            val textView: TextView
            val i: Int
            if (!isRecording) {
                access308()
                textView = getBindingData().txtStartRecord
                i = playerSecondsElapsed
            } else {
                access408()
                textView = getBindingData().txtStartRecord
                i = recorderSecondsElapsed
            }
            textView.setText(formatSeconds(i))
            playerSecondsElapsed = i
        }
    }


    fun access308(): Int {
        val i: Int = playerSecondsElapsed
        playerSecondsElapsed = i + 1
        return i
    }

    fun access408(): Int {
        val i: Int = recorderSecondsElapsed
        recorderSecondsElapsed = i + 1
        return i
    }

    fun formatSeconds(i: Int): String {
        return getTwoDecimalsValue(i / 3600) + ":" + getTwoDecimalsValue(
            i / 60
        ) + ":" + getTwoDecimalsValue(i % 60)
    }

    private fun getTwoDecimalsValue(i: Int): String {
        val sb: StringBuilder
        if (i < 0 || i > 9) {
            sb = StringBuilder()
            sb.append(i)
            sb.append("")
        } else {
            sb = StringBuilder()
            sb.append("0")
            sb.append(i)
        }
        return sb.toString()
    }

    private fun getBindingData(): ActivityRecordBinding {
        return viewBinding
    }
}