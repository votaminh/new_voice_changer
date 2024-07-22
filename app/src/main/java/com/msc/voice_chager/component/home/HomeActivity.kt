package com.msc.voice_chager.component.home

import android.app.Activity
import android.content.Intent
import com.msc.voice_chager.base.activity.BaseActivity
import com.msc.voice_chager.component.change_effect.ChangeEffectActivity
import com.msc.voice_chager.databinding.ActivityMainBinding
import com.msc.voice_chager.reactlibrary.ChangeEffectsModule
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityMainBinding>() {

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, HomeActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
    }
}