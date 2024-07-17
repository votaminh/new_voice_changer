package com.msc.demo_mvvm.admob;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.msc.demo_mvvm.R;

public class InterAdmob extends BaseAdmob {
    private static final String TAG = "interAdmob";
    String id;
    InterstitialAd interstitialAd;

    public InterAdmob(Context context, String id){
        super(context);
        Log.i(TAG, "InterAdmob: " + id);
        this.id = id;
    }

    public void load(OnAdmobLoadListener callback){
        load(callback, 30000);
    }

    public void load(OnAdmobLoadListener callback1, long timeOutMilli){
        Log.i(TAG, "load: ");
        onAdmobLoadListener = callback1;

        if(context == null){
            onAdmobLoadListener.onError("null context");
            onAdmobLoadListener = null;
            return;
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if(interstitialAd == null){
                if(onAdmobLoadListener != null){
                    Log.i(TAG, "load: timeout ");
                    onAdmobLoadListener.onError("error");
                    onAdmobLoadListener = null;
                }
            }
        }, timeOutMilli);
        InterstitialAd.load(
                context,
                id,
                adRequestBuilder.build(),
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd i) {
                        Log.i(TAG, "onAdLoaded: ");
                        interstitialAd = i;
                        if(onAdmobLoadListener != null){
                            onAdmobLoadListener.onLoad();
                            onAdmobLoadListener = null;
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.i(TAG, "onAdFailedToLoad: ");
                        if(onAdmobLoadListener != null){
                            onAdmobLoadListener.onError(loadAdError.getMessage());
                            onAdmobLoadListener = null;
                        }
                    }
                });
    }

    public void showInterstitial(Activity activity, OnAdmobShowListener onInterShowListener) {
        Log.i(TAG, "showInterstitial: ");
        if (interstitialAd != null && !isShowingOpenAd) {
            interstitialAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            Log.i(TAG, "onAdDismissedFullScreenContent: " );
                            interstitialAd = null;
                            onInterShowListener.onShow();
                            canShowOpenApp = true;
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            Log.i(TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());
                            interstitialAd = null;
                            onInterShowListener.onError(adError.getMessage());
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.i(TAG, "onAdShowedFullScreenContent: ");
                            canShowOpenApp = false;
                        }
                    });

            Dialog dialog = new Dialog(activity,android.R.style.Theme_Translucent_NoTitleBar);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.loading_inter);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.flags = WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
            window.setAttributes(wlp);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.show();

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if(activity != null && !activity.isDestroyed()){
                    dialog.dismiss();
                    if(interstitialAd != null){
                        interstitialAd.show(activity);
                    }else {
                        onInterShowListener.onError("");
                    }
                }
            }, 800);
        }else {
            onInterShowListener.onError("");
        }
    }

    public boolean available() {
        boolean a;
        if (interstitialAd != null && !isShowingOpenAd){
            a = true;
        }else {
            a = false;
        }

        Log.i(TAG, "available: " + a);
        return a;
    }
}
