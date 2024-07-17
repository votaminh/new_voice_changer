package com.msc.demo_mvvm.base.activity

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.viewbinding.ViewBinding
import com.msc.demo_mvvm.R
import com.msc.demo_mvvm.utils.SpManager
import java.util.Locale


abstract class BaseActivity<V : ViewBinding> : AppCompatActivity() {
    lateinit var viewBinding: V

    open fun onBack() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val language: String = SpManager.getInstance(this).getLanguage().languageCode
        if (language.isNotEmpty()) {
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.setLocale(Locale(language.lowercase(Locale.getDefault())))
            res.updateConfiguration(conf, dm)
        }

        setStatusBarColor(ContextCompat.getColor(this, R.color.app_bg), false)
        hideSystemUI()
        viewBinding = provideViewBinding()
        setContentView(viewBinding.root)
        initViews()
        initData()
        initObserver()
        handleOnBackPressed()
    }

    private fun hideSystemUI() {
//        val decorView = window.decorView
//        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//        decorView.systemUiVisibility = uiOptions
    }

    open fun initAdmobInterId(): String = ""
    open fun admobConfigLoadInter() = true

    open var isReloadInter = true

    open fun handleOnBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        })
    }

    fun replaceFragment(id: Int, fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(fragment::class.java.simpleName)
            replace(id, fragment, fragment::class.java.simpleName)
        }
    }

    fun addFragment(id: Int, fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(fragment::class.java.simpleName)
            add(id, fragment, fragment::class.java.simpleName)
        }
    }

    abstract fun provideViewBinding(): V

    open fun initViews() {}
    open fun initData() {}
    open fun initObserver() {}

    fun showLoading() {

    }

    fun showToast(mes: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, mes, duration).show()
    }

    fun hideLoading() {

    }

    fun setStatusBarColor(color: Int, lightStatus : Boolean) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = lightStatus
        }
    }

}