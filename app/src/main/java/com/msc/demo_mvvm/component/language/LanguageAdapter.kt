package com.flash.light.component.language

import android.view.LayoutInflater
import android.view.ViewGroup
import com.flash.light.base.adapter.BaseAdapter
import com.msc.demo_mvvm.databinding.ItemLanguageBinding
import com.msc.demo_mvvm.domain.layer.LanguageModel

class LanguageAdapter : BaseAdapter<LanguageModel, ItemLanguageBinding>() {
    override fun provideViewBinding(parent: ViewGroup): ItemLanguageBinding {
        return ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun binData(viewBinding: ItemLanguageBinding, item: LanguageModel) {
        viewBinding.imgLanguage.setImageResource(item.iconRes)
        viewBinding.tvTitleLanguage.setText(item.nameRes)
        viewBinding.swLanguage.isChecked = item.selected

//        if(item.selected){
//            viewBinding.container.setBackgroundResource(R.drawable.bg_stroke1_gray1_round10)
//        }else{
//            viewBinding.container.setBackgroundResource(R.drawable.bg_stroke1_gray1_round10)
//        }

        viewBinding.root.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    fun selectLanguage(languageCode: String) {
        var index = dataSet.indexOfFirst { it.selected }
        if (index > -1) {
            dataSet[index].selected = false
            notifyItemChanged(index)
        }
        index = dataSet.indexOfFirst { it.languageCode == languageCode }
        if (index > -1) {
            dataSet[index].selected = true
            notifyItemChanged(index)
        }
    }

    fun selectedLanguage(): LanguageModel? = dataSet.firstOrNull { it.selected }
}