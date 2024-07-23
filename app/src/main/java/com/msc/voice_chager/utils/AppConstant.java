package com.msc.voice_chager.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import kotlin.jvm.internal.Intrinsics;


public final class AppConstant {
    public static final AppConstant APP_CONSTANT = new AppConstant();
    public static final String actionService = "action_service";
    public static final String actionStart = "action_start";
    public static final String actionStop = "action_stop";
    public static final String channelId = "save_audio_service";
    public static final String channelName = "save audio service";
    private static final String keyDurationVoice = "key_duration_effect";
    private static final String keyPathVoice = "key_path_voice";
    private static final String keyPositionEffect = "key_position_effect";
    private static final String keyScreenIntoVoiceEffect = "key_screen_into_voice_effect";
    private static final String keySizeVoice = "key_size_effect";

    public String getKEY_DURATION_VOICE() {
        return keyDurationVoice;
    }

    public String getKEY_FILENAME_EFFECT() {
        return "key_filename_effect";
    }

    public String getKEY_PATH_VOICE() {
        return keyPathVoice;
    }

    public String getKEY_POSITION_EFFECT() {
        return keyPositionEffect;
    }

    public String getKEY_SCREEN_INTO_VOICE_EFFECTS() {
        return keyScreenIntoVoiceEffect;
    }

    public String getKEY_SIZE_VOICE() {
        return keySizeVoice;
    }

    public void setCheckResumePermissionMain(boolean z) {
    }

    public void setCheckResumePermissionRingtone(boolean z) {
    }

    public void setCheckResumeShare(boolean z) {
    }

    public void setCheckResumeShareMyVoice(boolean z) {
    }

    private AppConstant() {
    }

    public String getVoiceEffect(Context context) {
        try {
            InputStream open = context.getAssets().open("effects.json");
            Intrinsics.checkNotNullExpressionValue(open, "context.assets.open(\"effects.json\")");
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            Charset UTF_8 = StandardCharsets.UTF_8;
            Intrinsics.checkNotNullExpressionValue(UTF_8, "UTF_8");
            return new String(bArr, UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
