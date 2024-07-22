package com.msc.voice_chager.component.import_file

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.msc.voice_chager.base.activity.BaseActivity
import com.msc.voice_chager.component.change_effect.ChangeEffectActivity
import com.msc.voice_chager.databinding.ActivityImportFileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImportFileActivity : BaseActivity<ActivityImportFileBinding>() {

    private val audioAdapter = AudioAdapter()
    private val viewModel: ImportFileViewModel by viewModels()

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, ImportFileActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityImportFileBinding {
        return ActivityImportFileBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()

        buildReAudio()
        viewModel.getAllAudio()
    }

    private fun buildReAudio() {
        viewBinding.reAudio.run {
            layoutManager = LinearLayoutManager(this@ImportFileActivity, RecyclerView.VERTICAL, false)
            adapter = audioAdapter
            audioAdapter.onClick = { audio ->
                ChangeEffectActivity.start(this@ImportFileActivity, audio.path)
            }
        }
    }

    override fun initObserver() {
        super.initObserver()
        viewModel.run {
            audiosLive.observe(this@ImportFileActivity){
                audioAdapter.setData(it)
            }
        }
    }

}