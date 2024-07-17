package com.msc.voice_chager.reactlibrary.object;


public class ModelEffects {
    private float floatAmplify;
    private float[] floatAutoWah;
    private float[] floatChorus;
    private float[] floatCompressor;
    private float[] floatDistort;
    private float[] floatEcho;
    private float[] floatEcho4;
    private float[] floatEq2;
    private float[] floatEq3;
    private float[] floatFilter;
    private float[] floatFlange;
    private float[] floatPhaser;
    private float floatRate;
    private float[] floatReverb;
    private float floatRotate;
    private float[] floatyEcho1;
    private String id;
    private int intPitch;
    private boolean isMixBool;
    private boolean isPlayingBool = false;
    private boolean isReverseBool;
    private String strName;
    private String strPathMix;

    public ModelEffects(String str, String str2, int i, float f) {
        this.id = str;
        this.strName = str2;
        this.intPitch = i;
        this.floatRate = f;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getStrName() {
        return this.strName;
    }

    public void setStrName(String str) {
        this.strName = str;
    }

    public boolean isPlayingBool() {
        return this.isPlayingBool;
    }

    public void setPlayingBool(boolean z) {
        this.isPlayingBool = z;
    }

    public int getIntPitch() {
        return this.intPitch;
    }

    public void setIntPitch(int i) {
        this.intPitch = i;
    }

    public float getFloatRate() {
        return this.floatRate;
    }

    public void setFloatRate(float f) {
        this.floatRate = f;
    }

    public float[] getFloatReverb() {
        return this.floatReverb;
    }

    public void setFloatReverb(float[] fArr) {
        this.floatReverb = fArr;
    }

    public float[] getFloatFlange() {
        return this.floatFlange;
    }

    public void setFloatFlange(float[] fArr) {
        this.floatFlange = fArr;
    }

    public boolean isReverseBool() {
        return this.isReverseBool;
    }

    public void setReverseBool(boolean z) {
        this.isReverseBool = z;
    }

    public float[] getFloatEcho() {
        return this.floatEcho;
    }

    public void setFloatEcho(float[] fArr) {
        this.floatEcho = fArr;
    }

    public float[] getFloatyEcho1() {
        return this.floatyEcho1;
    }

    public void setFloatyEcho1(float[] fArr) {
        this.floatyEcho1 = fArr;
    }

    public float[] getFloatFilter() {
        return this.floatFilter;
    }

    public void setFloatFilter(float[] fArr) {
        this.floatFilter = fArr;
    }

    public float getFloatAmplify() {
        return this.floatAmplify;
    }

    public void setFloatAmplify(float f) {
        this.floatAmplify = f;
    }

    public float[] getFloatDistort() {
        return this.floatDistort;
    }

    public void setFloatDistort(float[] fArr) {
        this.floatDistort = fArr;
    }

    public float[] getFloatChorus() {
        return this.floatChorus;
    }

    public void setFloatChorus(float[] fArr) {
        this.floatChorus = fArr;
    }

    public float[] getFloatEcho4() {
        return this.floatEcho4;
    }

    public void setFloatEcho4(float[] fArr) {
        this.floatEcho4 = fArr;
    }

    public float[] getFloatEq2() {
        return this.floatEq2;
    }

    public void setFloatEq2(float[] fArr) {
        this.floatEq2 = fArr;
    }

    public float[] getFloatEq3() {
        return this.floatEq3;
    }

    public void setFloatEq3(float[] fArr) {
        this.floatEq3 = fArr;
    }

    public float getFloatRotate() {
        return this.floatRotate;
    }

    public void setFloatRotate(float f) {
        this.floatRotate = f;
    }

    public float[] getFloatPhaser() {
        return this.floatPhaser;
    }

    public void setFloatPhaser(float[] fArr) {
        this.floatPhaser = fArr;
    }

    public float[] getFloatAutoWah() {
        return this.floatAutoWah;
    }

    public void setFloatAutoWah(float[] fArr) {
        this.floatAutoWah = fArr;
    }

    public float[] getFloatCompressor() {
        return this.floatCompressor;
    }

    public void setFloatCompressor(float[] fArr) {
        this.floatCompressor = fArr;
    }

    public boolean isMixBool() {
        return this.isMixBool;
    }

    public void setMixBool(boolean z) {
        this.isMixBool = z;
    }

    public String getStrPathMix() {
        return this.strPathMix;
    }

    public void setStrPathMix(String str) {
        this.strPathMix = str;
    }
}
