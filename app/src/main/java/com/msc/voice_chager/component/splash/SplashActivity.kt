package com.msc.voice_chager.component.splash

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.view.View
import com.msc.voice_chager.admob.NameRemoteAdmob
import com.msc.voice_chager.BuildConfig
import com.msc.voice_chager.admob.BannerAdmob
import com.msc.voice_chager.admob.BaseAdmob
import com.msc.voice_chager.admob.CollapsiblePositionType
import com.msc.voice_chager.admob.InterAdmob
import com.msc.voice_chager.base.activity.BaseActivity
import com.msc.voice_chager.component.home.HomeActivity
import com.msc.voice_chager.component.language.LanguageActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.msc.voice_chager.databinding.ActivitySplashBinding
import com.msc.voice_chager.utils.NativeAdmobUtils
import com.msc.voice_chager.utils.NetworkUtil
import com.msc.voice_chager.utils.SpManager

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    private var progressAnimator: ValueAnimator? = null

    @Inject
    lateinit var spManager: SpManager

    companion object {
        fun start(activity: Activity) {
            activity.overridePendingTransition(0, 0)
            activity.startActivity(Intent(activity, SplashActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun onDestroy() {
        cancelLoadingListener()
        super.onDestroy()
    }

    private fun cancelLoadingListener() {
        progressAnimator?.removeAllListeners()
        progressAnimator?.cancel()
        progressAnimator = null
    }

    override fun onResume() {
        super.onResume()
        if (progressAnimator?.isPaused == true) {
            progressAnimator?.resume()
        }
    }

    override fun onPause() {
        progressAnimator?.pause()
        super.onPause()
    }

    override fun initViews() {
        if (spManager.getShowOnBoarding() && NetworkUtil.isOnline) {
            if (spManager.getBoolean(NameRemoteAdmob.NATIVE_LANGUAGE, true)) {
                NativeAdmobUtils.loadNativeLanguage()
            }
        }

        runProgress()
    }

    private fun runProgress() {

        showBanner()

        if (spManager.getBoolean(NameRemoteAdmob.INTER_SPLASH, true)) {
            val interAdmob = InterAdmob(this@SplashActivity, BuildConfig.inter_splash)
            interAdmob.load(object : BaseAdmob.OnAdmobLoadListener {
                override fun onLoad() {
                    if (spManager.getBoolean(NameRemoteAdmob.INTER_SPLASH, true)) {
                        interAdmob.showInterstitial(
                            this@SplashActivity,
                            object : BaseAdmob.OnAdmobShowListener {
                                override fun onShow() {
                                    gotoMainScreen()
                                }

                                override fun onError(e: String?) {
                                    gotoMainScreen()
                                }
                            })
                    } else {
                        gotoMainScreen()
                    }
                }

                override fun onError(e: String?) {
                    gotoMainScreen()
                }
            })
        } else {
            gotoMainScreen()
        }
    }

    private fun showBanner() {
        if(spManager.getBoolean(NameRemoteAdmob.BANNER_SPLASH, true)){
            val bannerAdmob = BannerAdmob(this, CollapsiblePositionType.NONE)
            bannerAdmob.showBanner(this@SplashActivity, BuildConfig.banner_splash, viewBinding.banner)
        }else{
            viewBinding.banner.visibility = View.GONE
        }
    }

    private fun gotoMainScreen() {
        cancelLoadingListener()
        if (spManager.getShowOnBoarding()) {
            LanguageActivity.start(this, true)
        } else {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        finish()
    }
}