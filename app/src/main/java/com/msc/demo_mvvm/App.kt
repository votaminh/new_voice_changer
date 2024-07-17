package com.msc.demo_mvvm

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.FirebaseApp
import com.msc.demo_mvvm.admob.NameRemoteAdmob
import com.msc.demo_mvvm.admob.OpenAdmob
import com.msc.demo_mvvm.utils.AppEx.getDeviceLanguage
import com.msc.demo_mvvm.utils.LocaleHelper
import com.msc.demo_mvvm.utils.NetworkUtil
import com.msc.demo_mvvm.utils.RemoteConfig
import com.msc.demo_mvvm.utils.SpManager
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidApp
@Singleton
class App : Application(), Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {
    companion object {
        var instance: App? = null
    }

    @Inject
    lateinit var spManager: SpManager

    private var currentActivity: Activity? = null
    private var openAdmob: OpenAdmob? = null

    override fun onCreate() {
        super<Application>.onCreate()
        instance = this

        FirebaseApp.initializeApp(this)
        RemoteConfig.instance().fetch()
        registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        NetworkUtil.initNetwork(this)
    }

    fun initAds() {
        MobileAds.initialize(this)
        val requestConfiguration = RequestConfiguration.Builder().build()
        MobileAds.setRequestConfiguration(requestConfiguration)

        if(spManager.getBoolean(NameRemoteAdmob.APP_RESUME, true)){
            openAdmob = OpenAdmob(this, BuildConfig.open_resume)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        val locale = getDeviceLanguage()
        val language = spManager.getLanguage()
        LocaleHelper.onAttach(this, language.languageCode)
        super.onConfigurationChanged(newConfig)
    }

    override fun attachBaseContext(base: Context?) {
        val context = LocaleHelper.onAttach(base, Locale.getDefault().language)
        super.attachBaseContext(context)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        if(spManager.getBoolean(NameRemoteAdmob.APP_RESUME, true)){
            openAdmob?.run {
                currentActivity?.let { showAdIfAvailable(it) }
            }
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}