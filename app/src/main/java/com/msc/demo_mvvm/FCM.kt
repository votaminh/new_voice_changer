package com.msc.demo_mvvm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class FCM : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.i("gsdgdsg", "onNewToken: " + token)
        super.onNewToken(token)
    }
}