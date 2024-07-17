package com.msc.voice_chager.component.change_effect

import android.view.LayoutInflater
import android.view.ViewGroup
import com.flash.light.base.adapter.BaseAdapter
import com.msc.voice_chager.databinding.ItemEffectBinding
import com.msc.voice_chager.reactlibrary.`object`.ModelEffects

class EffectAdapter : BaseAdapter<ModelEffects, ItemEffectBinding>() {
    override fun provideViewBinding(parent: ViewGroup): ItemEffectBinding {
        return ItemEffectBinding.inflate(LayoutInflater.from(parent.context))
    }

    override fun binData(viewBinding: ItemEffectBinding, item: ModelEffects) {
        super.binData(viewBinding, item)
        viewBinding.tvName.text = item.strName
        viewBinding.root.setOnClickListener {
            onClickPossition?.invoke(item, dataSet.indexOf(item))
        }
    }
}