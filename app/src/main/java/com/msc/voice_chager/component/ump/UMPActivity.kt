package com.msc.voice_chager.component.ump

import com.msc.voice_chager.base.activity.BaseActivity
import com.msc.voice_chager.component.splash.SplashActivity
import com.msc.voice_chager.App
import com.msc.voice_chager.databinding.ActivityUmpBinding
import com.msc.voice_chager.utils.RemoteConfig
import com.msc.voice_chager.utils.SpManager
import com.msc.voice_chager.utils.UMPUtils


class UMPActivity : BaseActivity<ActivityUmpBinding>() {
    private val TAG = "ump_activity"

    override fun provideViewBinding(): ActivityUmpBinding = ActivityUmpBinding.inflate(layoutInflater)

    override fun initData() {
        super.initData()

        if(SpManager.getInstance(this).isUMPShowed()){
            RemoteConfig.instance().fetch()
            openSplash();
        }else{
            RemoteConfig.instance().fetch{
                initUmp()
            }
        }
    }

    private fun openSplash() {

        val app : App = application as App
        app.initAds()

        SpManager.getInstance(this).setUMPShowed(true)
        SplashActivity.start(this);
        finish()
    }

    private fun initUmp() {
        UMPUtils.init(this@UMPActivity, nextAction = {
            openSplash()
        }, true, false)
    }
}