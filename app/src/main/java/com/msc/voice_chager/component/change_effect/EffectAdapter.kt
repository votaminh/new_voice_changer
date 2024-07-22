package com.msc.voice_chager.component.change_effect

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.flash.light.base.adapter.BaseAdapter
import com.msc.voice_chager.databinding.ItemEffectBinding
import com.msc.voice_chager.reactlibrary.`object`.ModelEffects

class EffectAdapter : BaseAdapter<ModelEffects, ItemEffectBinding>() {
    override fun provideViewBinding(parent: ViewGroup): ItemEffectBinding {
        return ItemEffectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun binData(viewBinding: ItemEffectBinding, item: ModelEffects) {
        super.binData(viewBinding, item)
        viewBinding.tvName.text = item.strName.replace("Images.", "")
        Glide.with(viewBinding.root.context)
            .load(Uri.parse("file:///android_asset/" + item.strName.replace(".","/") + ".png"))
            .into(viewBinding.imv)
        viewBinding.root.setOnClickListener {
            onClickPossition?.invoke(item, dataSet.indexOf(item))
        }
    }
}