package com.msc.voice_chager.component.home

import android.app.Activity
import android.content.Intent
import com.msc.voice_chager.base.activity.BaseActivity
import com.msc.voice_chager.component.change_effect.ChangeEffectActivity
import com.msc.voice_chager.component.import_file.ImportFileActivity
import com.msc.voice_chager.component.record.RecordActivity
import com.msc.voice_chager.databinding.ActivityMainBinding
import com.msc.voice_chager.reactlibrary.ChangeEffectsModule
import com.msc.voice_chager.utils.PermissionUtils
import com.msc.voice_chager.utils.SpManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityMainBinding>() {

    @Inject
    lateinit var spManager: SpManager

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

        spManager.saveOnBoarding()

        viewBinding.run {
            record.setOnClickListener {
                if(PermissionUtils.recordAudioGrant(this@HomeActivity)){
                    RecordActivity.start(this@HomeActivity)
                }else{
                    PermissionUtils.requestRecordAudio(this@HomeActivity, 325)
                }
            }

            importFile.setOnClickListener {
                if(PermissionUtils.storageAudioGrant(this@HomeActivity)){
                    ImportFileActivity.start(this@HomeActivity)
                }else{
                    PermissionUtils.requestStorageAudio(this@HomeActivity, 3221)
                }
            }
        }
    }
}