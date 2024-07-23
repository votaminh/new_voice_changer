package com.msc.voice_chager.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.msc.voice_chager.R;

import java.io.File;


public class FileMethods {
    public static String getMainDirPath(Context context) {
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + context.getResources().getString(R.string.app_name), "VoiceEffectAudio");
            StringBuilder sb = new StringBuilder();
            sb.append("getMainDirPath: voiceEffectAudioFilePath ::  ");
            sb.append(file);
            Log.e("xz---", sb.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        } catch (Exception unused) {
            return context.getFilesDir().getAbsolutePath();
        }
    }

    public static File getDirectory(Activity activity) {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + activity.getResources().getString(R.string.app_name) + "/VoiceEffects");
        StringBuilder sb = new StringBuilder();
        sb.append("getMainDirPath: voiceEffectDirPath ::  ");
        sb.append(file);
        Log.e("xz---", sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static String milliSecFormat(long j) {
        String str;
        String str2;
        String str3;
        int i = (int) (j / 3600000);
        long j2 = j % 3600000;
        int i2 = ((int) j2) / 60000;
        int round = Math.round((float) ((j2 % 60000) / 1000));
        if (i > 0) {
            str = i + ":";
        } else {
            str = "";
        }
        if (i2 < 10) {
            str2 = "0" + i2;
        } else {
            str2 = "" + i2;
        }
        if (round < 10) {
            str3 = "0" + round;
        } else {
            str3 = "" + round;
        }
        return str + str2 + ":" + str3;
    }
}
