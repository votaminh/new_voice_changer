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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeEffectActivity : BaseActivity<ActivityChangeEffectBinding>() {

    private val viewModel: ChangeEffectViewModel by viewModels()
    private val effectAdapter = EffectAdapter()
    private var mChangeEffectsModule : ChangeEffectsModule? = null

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, ChangeEffectActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityChangeEffectBinding {
        return ActivityChangeEffectBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()

        initEffect()

        buildReEffect()
        viewModel.getEffect()
    }

    private fun initEffect() {
        val path = "/data/data/com.voice.chager/files/stutter-i-love-you-82913.mp3"
        mChangeEffectsModule = ChangeEffectsModule(this)
        mChangeEffectsModule?.run {
            createOutputDir(this@ChangeEffectActivity)
            setPath(path)
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
            layoutManager = GridLayoutManager(this@ChangeEffectActivity, 3, RecyclerView.VERTICAL, false)
            adapter = effectAdapter
            effectAdapter.onClickPossition = { modelEffects, i ->
                mChangeEffectsModule?.effectPlay(i)
            }
        }
    }
}