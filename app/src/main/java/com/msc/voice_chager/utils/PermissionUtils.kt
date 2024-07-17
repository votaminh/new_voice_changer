package com.msc.voice_chager.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class PermissionUtils {
    companion object {
        fun writeSettingGrant(activity: Activity) : Boolean {
            return Settings.System.canWrite(activity)
        }

        fun requestWriteSetting(activity: Activity, request : Int){
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:" + activity.packageName)
            activity.startActivityForResult(intent, request)
        }

        fun checkNotificationPermission(activity: Activity): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            } else {
                true // Notification permission is automatically granted for Android versions below TIRAMISU (Android 13)
            }
        }

        fun requestNotificationPermission(activity: Activity, requestCode: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), requestCode)
            }
        }

        fun readContactGrant(activity: Activity) : Boolean {
            return (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED)
        }

        fun requestReadContactPermission(activity: Activity, request : Int){
            val permissions = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
            ActivityCompat.requestPermissions(activity, permissions, request)
        }

        fun storageGrant(activity: Activity) : Boolean{
            return if (Build.VERSION.SDK_INT < 23) {
                true
            } else {
                if (Build.VERSION.SDK_INT < 33) {
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                } else {
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
                }
            }
        }

        fun requestStorage(activity: Activity, request : Int){
            if (Build.VERSION.SDK_INT < 23) {
                return
            } else {
                val permissions = if (Build.VERSION.SDK_INT < 33) {
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                } else {
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_AUDIO)
                }
                ActivityCompat.requestPermissions(activity, permissions, request)
            }
        }
    }
}