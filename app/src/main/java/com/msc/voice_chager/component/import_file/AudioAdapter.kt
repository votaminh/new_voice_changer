package com.msc.voice_chager.component.import_file

import android.view.LayoutInflater
import android.view.ViewGroup
import com.flash.light.base.adapter.BaseAdapter
import com.msc.voice_chager.databinding.ItemAudioBinding
import com.msc.voice_chager.domain.layer.Audio
import com.msc.voice_chager.utils.AppEx.timeToString
import java.io.File

class AudioAdapter : BaseAdapter<Audio, ItemAudioBinding>()  {
    override fun provideViewBinding(parent: ViewGroup): ItemAudioBinding {
        return ItemAudioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun binData(viewBinding: ItemAudioBinding, item: Audio) {
        super.binData(viewBinding, item)
        viewBinding.run {
            val file = File(item.path)
            tvName.text = file.name
            tvTime.text = file.lastModified().timeToString()
        }
    }
}