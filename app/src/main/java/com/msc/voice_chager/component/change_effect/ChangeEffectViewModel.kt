package com.msc.voice_chager.component.change_effect

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.msc.voice_chager.reactlibrary.dataMng.ParsingJsonObjects
import com.msc.voice_chager.reactlibrary.`object`.ModelEffects
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class ChangeEffectViewModel @Inject constructor(@ApplicationContext val context: Context) : ViewModel() {

    var effectModelsLive = MutableLiveData<List<ModelEffects>>()

    fun getEffect(){
        val open: InputStream = context.getAssets().open("effects.json")
        val bArr = ByteArray(open.available())
        open.read(bArr)
        open.close()
        val UTF_8 = StandardCharsets.UTF_8
        val effectString = String(bArr, UTF_8)
        val jSONArray= JSONArray(effectString)
        val effects = arrayListOf<ModelEffects>()
        for(i in 0 until  jSONArray.length()){
            val jSONObject: JSONObject = jSONArray.getJSONObject(i)
            val effectModel = ParsingJsonObjects.jsonToObjectEffects(jSONObject.toString())
            effects.add(effectModel)
        }
        effectModelsLive.postValue(effects)
    }
}