package com.msc.voice_chager.component.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import com.flash.light.base.adapter.BaseAdapter
import com.msc.voice_chager.databinding.ItemOnboardingBinding
import com.msc.voice_chager.utils.ViewEx.invisible
import com.msc.voice_chager.utils.ViewEx.visible

class OnBoardingAdapter : BaseAdapter<OnBoarding, ItemOnboardingBinding>() {

    override fun binData(viewBinding: ItemOnboardingBinding, item: OnBoarding) {
        viewBinding.apply {
            if(item.resImage == OnBoarding.FULL_NATIVE_FLAG){
                layoutAdNative.visible()
                layoutOnboard.invisible()
                item.nativeAdmob?.showNative(layoutAdNative, null)
            }else{
                layoutAdNative.invisible()
                layoutOnboard.visible()
//                tvMess.setText(item.resDescription)
                tvTitle.setText(item.resTitle)
                imgOnBoarding.setImageResource(item.resImage)
            }
        }
    }

    override fun provideViewBinding(parent: ViewGroup): ItemOnboardingBinding =
        ItemOnboardingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
}