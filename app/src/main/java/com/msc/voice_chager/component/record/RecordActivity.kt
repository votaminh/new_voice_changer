package com.msc.voice_chager.component.record

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import com.msc.voice_chager.base.activity.BaseActivity
import com.msc.voice_chager.databinding.ActivityRecordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordActivity : BaseActivity<ActivityRecordBinding>() {

    private val viewModel: RecordingViewModel by viewModels()

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, RecordActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityRecordBinding {
        return ActivityRecordBinding.inflate(layoutInflater)
    }
}