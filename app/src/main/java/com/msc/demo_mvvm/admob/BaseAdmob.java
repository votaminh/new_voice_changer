package com.msc.demo_mvvm.admob;

import android.content.Context;
import android.provider.Settings;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.msc.demo_mvvm.BuildConfig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class BaseAdmob {
    public static boolean canShowOpenApp = true;
    public static boolean isShowingOpenAd = false;
    public static long latestTimeShowOpenAd = 0;

    public Context context;
    public AdRequest.Builder adRequestBuilder;
    public OnAdmobLoadListener onAdmobLoadListener;

    public BaseAdmob(Context context){
        this.context = context;
        MobileAds.initialize(context, initializationStatus -> {});

        String deviceId = "";
        if(BuildConfig.DEBUG) {
            String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            deviceId = md5(android_id).toUpperCase();
        }

        List<String> testDeviceIds = Arrays.asList(deviceId);
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        adRequestBuilder = new AdRequest.Builder()
                .setRequestAgent(AdRequest.DEVICE_ID_EMULATOR)
                .setRequestAgent(deviceId);
    }

    public static final String md5(final String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {

        }
        return "";
    }

    public interface OnAdmobLoadListener{
        void onLoad();
        void onError(String e);
    }

    public interface OnAdmobShowListener{
        void onShow();
        void onError(String e);
    }
}
