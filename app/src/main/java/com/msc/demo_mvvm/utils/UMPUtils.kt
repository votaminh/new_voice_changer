package com.msc.demo_mvvm.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.google.firebase.analytics.FirebaseAnalytics
import com.msc.demo_mvvm.BuildConfig
import com.msc.demo_mvvm.R
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Locale

class UMPUtils {
    companion object {
        private val TAG = "umpUtils"

        @SuppressLint("HardwareIds")
        fun init(
            activity : Activity,
            nextAction : (() -> Unit)? = null,
            reset: Boolean,
            showLoading : Boolean
            ) {

            Log.i(TAG, "init: ")

            val progressDialog = ProgressDialog(activity)
            progressDialog.setMessage(activity.getString(R.string.loading_cmp))

            if(showLoading){
                progressDialog.show()
            }

            val debugSettings = ConsentDebugSettings.Builder(activity)

            if(BuildConfig.DEBUG){
                val android_id = Settings.Secure.getString(
                    activity.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
                val deviceId = md5(android_id)
                    .uppercase(Locale.getDefault())
                debugSettings.setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                    .addTestDeviceHashedId(deviceId).build()
            }

            val params = ConsentRequestParameters.Builder()
                .setConsentDebugSettings(debugSettings.build())
                .setTagForUnderAgeOfConsent(false).build()

            val consentInformation = UserMessagingPlatform.getConsentInformation(activity)
            if(reset){
                consentInformation.reset()
            }
            consentInformation.requestConsentInfoUpdate(activity, params, {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    activity
                ) { loadAndShowError ->
                    if(showLoading){
                        progressDialog.dismiss()
                    }
                    val isPrivacyOptionsRequired = consentInformation.privacyOptionsRequirementStatus ==
                            ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED

                    SpManager.getInstance(activity).setSettingUMPShowing(isPrivacyOptionsRequired)

                    if(consentInformation.canRequestAds()){
                        nextAction?.invoke()
                        Log.i(TAG, "loadAndShowError: canRequestAds")
                    }else{
                        Log.i(TAG, "loadAndShowError: canNot RequestAds")
                    }

                    Log.i(TAG, "initUmp: showing cmp")
                    if(UMPUtils.granted(activity)){
                        FirebaseAnalytics.getInstance(activity).logEvent("cmp_granted", Bundle())
                    }else{
                        FirebaseAnalytics.getInstance(activity).logEvent("cmp_not_grant", Bundle())
                    }
                }
            }, { requestConsentError ->
                if(showLoading){
                    progressDialog.dismiss()
                }
                Log.i(TAG, "requestConsentError: " + requestConsentError.message)
                if(consentInformation.canRequestAds()){
                    nextAction?.invoke()
                }else{
                }
            })

        }

        fun granted(activity: Activity): Boolean {
            val sharedPref: SharedPreferences = activity.getSharedPreferences(
                activity.packageName + "_preferences",
                AppCompatActivity.MODE_PRIVATE
            )

            val purposeConsents = sharedPref.getString("IABTCF_PurposeConsents", "")
            // Purposes are zero-indexed. Index 0 contains information about Purpose 1.
            // Purposes are zero-indexed. Index 0 contains information about Purpose 1.
            if (!purposeConsents!!.isEmpty()) {
                val purposeOneString = purposeConsents!![0].toString()
                if(purposeOneString == "1"){
                    return true
                }
            }

            return false
        }

        fun md5(s: String): String {
            try {
                val digest = MessageDigest.getInstance("MD5")
                digest.update(s.toByteArray())
                val messageDigest = digest.digest()
                val hexString = StringBuffer()
                for (i in messageDigest.indices) {
                    var h = Integer.toHexString(0xFF and messageDigest[i].toInt())
                    while (h.length < 2) h = "0$h"
                    hexString.append(h)
                }
                return hexString.toString()
            } catch (e: NoSuchAlgorithmException) {
            }
            return ""
        }
    }
}