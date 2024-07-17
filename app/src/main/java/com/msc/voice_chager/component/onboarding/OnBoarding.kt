package com.msc.voice_chager.component.onboarding

import com.msc.voice_chager.admob.NativeAdmob


data class OnBoarding(val resImage: Int, val resTitle: Int, val resDescription: Int, var nativeAdmob : NativeAdmob? = null){
    companion object {
        const val FULL_NATIVE_FLAG = 1822
    }
}
