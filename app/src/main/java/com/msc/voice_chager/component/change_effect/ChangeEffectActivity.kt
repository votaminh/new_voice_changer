package com.msc.voice_chager.component.change_effect

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.msc.voice_chager.base.activity.BaseActivity
import com.msc.voice_chager.databinding.ActivityChangeEffectBinding
import com.msc.voice_chager.reactlibrary.ChangeEffectsModule
import com.msc.voice_chager.reactlibrary.`object`.ModelEffects
import com.msc.voice_chager.utils.DialogEx.showDialogSaveFile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeEffectActivity : BaseActivity<ActivityChangeEffectBinding>() {

    private val viewModel: ChangeEffectViewModel by viewModels()
    private val effectAdapter = EffectAdapter()
    private var mChangeEffectsModule : ChangeEffectsModule? = null

    private var pathAudio = ""

    companion object {
        const val KEY_PATH_FILE = "KEY_PATH_FILE"
        fun start(activity : Activity, path : String){
            val intent = Intent(activity, ChangeEffectActivity::class.java)
            intent.putExtra(KEY_PATH_FILE, path)
            activity.startActivity(intent)
        }
    }

    override fun provideViewBinding(): ActivityChangeEffectBinding {
        return ActivityChangeEffectBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()

        getData()

        viewBinding.run {
            save.setOnClickListener {
                showDialogSaveFile{ name ->
                    saveAudio(name)
                }
            }
        }

        initEffect()

        buildReEffect()
        viewModel.getEffect()
    }

    private fun getData() {
        pathAudio = intent.getStringExtra(KEY_PATH_FILE).toString()
    }

    private fun saveAudio(name: String) {

    }

    private fun initEffect() {
        mChangeEffectsModule = ChangeEffectsModule(this)
        mChangeEffectsModule?.run {
            createOutputDir(this@ChangeEffectActivity)
            setPath(pathAudio)
            createDBMedia()
        }
    }

    override fun initObserver() {
        super.initObserver()

        viewModel.run {
            effectModelsLive.observe(this@ChangeEffectActivity){
                it.forEach {
                    mChangeEffectsModule?.insertEffect(it)
                }
                effectAdapter.setData(it)
            }
        }
    }

    private fun buildReEffect() {
        viewBinding.reEffect.run {
            layoutManager = GridLayoutManager(this@ChangeEffectActivity, 4, RecyclerView.VERTICAL, false)
            adapter = effectAdapter
            effectAdapter.onClickPossition = { modelEffects, i ->
                mChangeEffectsModule?.effectPlay(i)
            }
        }
    }
}