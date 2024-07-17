package com.msc.demo_mvvm.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.msc.demo_mvvm.R
import com.msc.demo_mvvm.domain.layer.LanguageModel

class SpManager(private val preferences: SharedPreferences) {
    companion object {
        private var instance: SpManager? = null

        fun getInstance(context: Context): SpManager {
            if (instance == null) {
                instance = SpManager(context.getSharedPreferences("flashlight",Context.MODE_PRIVATE))
            }
            return instance!!
        }
    }

    fun getInt(key: String, defaultValue: Int): Int = preferences.getInt(key, defaultValue)

    fun putInt(key: String, value: Int) {
        val editor = preferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        preferences.getBoolean(key, defaultValue)


    fun getLong(key: String, defaultValue: Long): Long =
        preferences.getLong(key, defaultValue)

    fun putLong(key: String, value: Long) {
        val editor = preferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getString(key: String, defaultValue: String): String? =
        preferences.getString(key, defaultValue)

    fun putString(key: String, value: String) {
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun saveLanguage(languageModel: LanguageModel) {
        preferences.edit().putString(Constant.KEY_SP_CURRENT_LANGUAGE, Gson().toJson(languageModel))
            .apply()
    }

    fun getLanguage(): LanguageModel {
        return Gson().fromJson(preferences.getString(Constant.KEY_SP_CURRENT_LANGUAGE, ""), LanguageModel::class.java) ?: LanguageModel("en", R.drawable.ic_english, R.string.english)
    }

    fun setUMPShowed(showed: Boolean) {
        preferences.edit().putBoolean(Constant.KEY_SP_UMP_SHOWED, showed).apply()
    }
    fun isUMPShowed() : Boolean {
        return preferences.getBoolean(Constant.KEY_SP_UMP_SHOWED, false)
    }

    fun setSettingUMPShowing(b: Boolean) {
        preferences.edit().putBoolean(Constant.KEY_SP_UMP_SETTING_SHOWED, b).apply()
    }

    fun isSettingUMPShowing() : Boolean {
        return preferences.getBoolean(Constant.KEY_SP_UMP_SETTING_SHOWED, false)
    }

    fun saveOnBoarding() {
        preferences.edit().putBoolean(Constant.KEY_SP_SHOW_ONBOARDING, false).apply()
    }

    fun getShowOnBoarding(): Boolean {
        return preferences.getBoolean(Constant.KEY_SP_SHOW_ONBOARDING, true)
    }

    fun getDefaultRingtone(): String? {
        return preferences.getString("default_uri_ringtone", "")
    }

    fun setDefaultRingtone(uri : String) {
        return preferences.edit().putString("default_uri_ringtone", uri).apply()
    }

    fun getDefaultRingtoneNotification(): String? {
        return preferences.getString("default_uri_ringtone_notification", "")
    }

    fun setDefaultRingtoneNotification(uri : String) {
        return preferences.edit().putString("default_uri_ringtone_notification", uri).apply()
    }

    fun getDefaultRingtoneAlarm(): String? {
        return preferences.getString("getDefaultRingtoneAlarm", "")
    }

    fun setDefaultRingtoneAlarm(uri : String) {
        return preferences.edit().putString("getDefaultRingtoneAlarm", uri).apply()
    }
}