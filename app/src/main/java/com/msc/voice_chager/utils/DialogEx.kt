package com.msc.voice_chager.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.msc.voice_chager.R
import com.msc.voice_chager.admob.NameRemoteAdmob
import com.msc.voice_chager.databinding.DialogExitBinding
import com.msc.voice_chager.databinding.DialogSaveFileBinding
import com.msc.voice_chager.utils.DialogEx.showDialogSuccess

object DialogEx {
    fun Activity.showDialogRequestWriteSettingPermission(okAction : (() -> Unit)? = null){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.txt_write_setting_permission_title))
        builder.setPositiveButton(getString(R.string.txt_ok), object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                okAction?.invoke()
            }
        })
        builder.setNegativeButton(getString(R.string.txt_cancel), object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {

            }
        })
        builder.show()
    }
    fun Activity.showDialogSuccess(text : String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(text)
        builder.setPositiveButton(getString(R.string.txt_ok), object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {

            }
        })
        builder.show()
    }

    fun Activity?.showDialogExit(
        lifecycle: LifecycleOwner,
        exitAction : (() -> Unit) ? = null
    ){
        this?.let {activity ->
            val builder = BottomSheetDialog(this)
            val binding = DialogExitBinding.inflate(LayoutInflater.from(this))
            builder.setContentView(binding.root)
            builder.show()

            NativeAdmobUtils.nativeExitLiveData?.run {
                nativeAdLive?.observe(lifecycle){
                    if(available() && SpManager.getInstance(activity).getBoolean(NameRemoteAdmob.NATIVE_EXIT, true)){
                        binding.flAdplaceholder.visibility = View.VISIBLE
                        showNative(binding.flAdplaceholder, null)
                    }
                }
            }

            with(binding){
                tvExit.setOnClickListener {
                    exitAction?.invoke()
                }
                tvCancel.setOnClickListener {
                    builder.dismiss()
                    NativeAdmobUtils.nativeExitLiveData?.reLoad()
                }
            }
        }
    }

    fun Activity?.showDialogSaveFile(saveAction : ((String) -> Unit)? = null){
        this?.let { activity ->
            val builder = AlertDialog.Builder(this)
            val binding = DialogSaveFileBinding.inflate(LayoutInflater.from(this))
            builder.setView(binding.root)
            val dialog = builder.create()
            dialog.show()
            binding.run {
                no.setOnClickListener {
                    dialog.dismiss()
                }

                save.setOnClickListener {
                    val name = edtName.text.toString()
                    if(name.isEmpty()){
                        Toast.makeText(activity, activity.getString(R.string.txt_name_not_empty), Toast.LENGTH_SHORT).show()
                    }else{
                        saveAction?.invoke(name)
                        dialog.dismiss()
                    }
                }
            }
        }
    }
}