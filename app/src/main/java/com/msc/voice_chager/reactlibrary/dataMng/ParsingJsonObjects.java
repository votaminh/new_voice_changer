package com.msc.voice_chager.reactlibrary.dataMng;

import com.msc.voice_chager.reactlibrary.constants.InterfaceVoiceChangerListener;
import com.msc.voice_chager.reactlibrary.object.ModelEffects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ParsingJsonObjects implements InterfaceVoiceChangerListener {
    public static final String TAG = "JsonParsingUtils";

    public static ModelEffects jsonToObjectEffects(String str) {
        String str2;
        boolean z;
        JSONArray jSONArray;
        int length;
        JSONArray jSONArray2;
        int length2;
        JSONArray jSONArray3;
        int length3;
        JSONArray jSONArray4;
        int length4;
        JSONArray jSONArray5;
        int length5;
        JSONArray jSONArray6;
        int length6;
        JSONArray jSONArray7;
        int length7;
        JSONArray jSONArray8;
        int length8;
        JSONArray jSONArray9;
        int length9;
        JSONArray jSONArray10;
        int length10;
        JSONArray jSONArray11;
        int length11;
        JSONArray jSONArray12;
        int length12;
        JSONArray jSONArray13;
        int length13;
        if (str == null && str.equals("")) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            String string = jSONObject.getString("id");
            String string2 = jSONObject.getString("name");
            int i = jSONObject.getInt("pitch");
            int i2 = jSONObject.getInt("rate");
            if (jSONObject.opt("reverse") != null) {
                z = jSONObject.getBoolean("reverse");
                str2 = "echo4";
            } else {
                str2 = "echo4";
                z = false;
            }
            ModelEffects modelEffects = new ModelEffects(string, string2, i, i2);
            modelEffects.setReverseBool(z);
            if (jSONObject.opt("amplify") != null) {
                modelEffects.setFloatAmplify((float) jSONObject.getDouble("amplify"));
            }
            if (jSONObject.opt("isMix") != null) {
                modelEffects.setMixBool(jSONObject.getBoolean("isMix"));
                modelEffects.setStrPathMix(jSONObject.getString("path"));
            }
            if (jSONObject.opt("rotate") != null) {
                modelEffects.setFloatRotate((float) jSONObject.getDouble("rotate"));
            }
            if (jSONObject.opt("reverb") != null && (length = (jSONArray = jSONObject.getJSONArray("reverb")).length()) > 0) {
                float[] fArr = new float[length];
                for (int i3 = 0; i3 < length; i3++) {
                    fArr[i3] = (float) jSONArray.getDouble(i3);
                }
                modelEffects.setFloatReverb(fArr);
            }
            if (jSONObject.opt("distort") != null && (length2 = (jSONArray2 = jSONObject.getJSONArray("distort")).length()) > 0) {
                float[] fArr2 = new float[length2];
                for (int i4 = 0; i4 < length2; i4++) {
                    fArr2[i4] = (float) jSONArray2.getDouble(i4);
                }
                modelEffects.setFloatDistort(fArr2);
            }
            if (jSONObject.opt("chorus") != null && (length3 = (jSONArray3 = jSONObject.getJSONArray("chorus")).length()) > 0) {
                float[] fArr3 = new float[length3];
                for (int i5 = 0; i5 < length3; i5++) {
                    fArr3[i5] = (float) jSONArray3.getDouble(i5);
                }
                modelEffects.setFloatChorus(fArr3);
            }
            if (jSONObject.opt("flanger") != null && (length4 = (jSONArray4 = jSONObject.getJSONArray("flanger")).length()) > 0) {
                float[] fArr4 = new float[length4];
                for (int i6 = 0; i6 < length4; i6++) {
                    fArr4[i6] = (float) jSONArray4.getDouble(i6);
                }
                modelEffects.setFloatFlange(fArr4);
            }
            if (jSONObject.opt("filter") != null && (length5 = (jSONArray5 = jSONObject.getJSONArray("filter")).length()) > 0) {
                float[] fArr5 = new float[length5];
                for (int i7 = 0; i7 < length5; i7++) {
                    fArr5[i7] = (float) jSONArray5.getDouble(i7);
                }
                modelEffects.setFloatFilter(fArr5);
            }
            if (jSONObject.opt("echo") != null && (length6 = (jSONArray6 = jSONObject.getJSONArray("echo")).length()) > 0) {
                float[] fArr6 = new float[length6];
                for (int i8 = 0; i8 < length6; i8++) {
                    fArr6[i8] = (float) jSONArray6.getDouble(i8);
                }
                modelEffects.setFloatEcho(fArr6);
            }
            String str3 = str2;
            if (jSONObject.opt(str3) != null && (length7 = (jSONArray7 = jSONObject.getJSONArray(str3)).length()) > 0) {
                float[] fArr7 = new float[length7];
                for (int i9 = 0; i9 < length7; i9++) {
                    fArr7[i9] = (float) jSONArray7.getDouble(i9);
                }
                modelEffects.setFloatEcho4(fArr7);
            }
            if (jSONObject.opt("eq1") != null && (length8 = (jSONArray8 = jSONObject.getJSONArray("eq1")).length()) > 0) {
                float[] fArr8 = new float[length8];
                for (int i10 = 0; i10 < length8; i10++) {
                    fArr8[i10] = (float) jSONArray8.getDouble(i10);
                }
                modelEffects.setFloatyEcho1(fArr8);
            }
            if (jSONObject.opt("eq2") != null && (length9 = (jSONArray9 = jSONObject.getJSONArray("eq2")).length()) > 0) {
                float[] fArr9 = new float[length9];
                for (int i11 = 0; i11 < length9; i11++) {
                    fArr9[i11] = (float) jSONArray9.getDouble(i11);
                }
                modelEffects.setFloatEq2(fArr9);
            }
            if (jSONObject.opt("eq3") != null && (length10 = (jSONArray10 = jSONObject.getJSONArray("eq3")).length()) > 0) {
                float[] fArr10 = new float[length10];
                for (int i12 = 0; i12 < length10; i12++) {
                    fArr10[i12] = (float) jSONArray10.getDouble(i12);
                }
                modelEffects.setFloatEq3(fArr10);
            }
            if (jSONObject.opt("phaser") != null && (length11 = (jSONArray11 = jSONObject.getJSONArray("phaser")).length()) > 0) {
                float[] fArr11 = new float[length11];
                for (int i13 = 0; i13 < length11; i13++) {
                    fArr11[i13] = (float) jSONArray11.getDouble(i13);
                }
                modelEffects.setFloatPhaser(fArr11);
            }
            if (jSONObject.opt("autowah") != null && (length12 = (jSONArray12 = jSONObject.getJSONArray("autowah")).length()) > 0) {
                float[] fArr12 = new float[length12];
                for (int i14 = 0; i14 < length12; i14++) {
                    fArr12[i14] = (float) jSONArray12.getDouble(i14);
                }
                modelEffects.setFloatAutoWah(fArr12);
            }
            if (jSONObject.opt("compressor") != null && (length13 = (jSONArray13 = jSONObject.getJSONArray("compressor")).length()) > 0) {
                float[] fArr13 = new float[length13];
                for (int i15 = 0; i15 < length13; i15++) {
                    fArr13[i15] = (float) jSONArray13.getDouble(i15);
                }
                modelEffects.setFloatCompressor(fArr13);
            }
            return modelEffects;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
