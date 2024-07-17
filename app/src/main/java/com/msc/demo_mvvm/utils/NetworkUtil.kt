package com.msc.demo_mvvm.utils

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtil {
    private var mContext: Context? = null
    fun initNetwork(context: Context?) {
        mContext = context
    }

    val isOnline: Boolean
        get() {
            val connectivityManager =
                mContext!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = connectivityManager.activeNetworkInfo
            return netInfo != null && netInfo.isConnected
        }
}