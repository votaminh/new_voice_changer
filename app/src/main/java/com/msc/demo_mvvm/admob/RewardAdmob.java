package com.msc.demo_mvvm.admob;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class RewardAdmob extends BaseAdmob {
    private static final String TAG = "reward";
    private String id ;
    private RewardedAd rewardedAd;
    boolean isLoading;
    boolean earnReward;

    public RewardAdmob(Context context, String id) {
        super(context);
        this.id = id;
        Log.i(TAG, "RewardAdmob: " + id);
    }

    public void load(OnAdmobLoadListener onAdmobLoadListener){

        Log.i(TAG, "load: ");
        isLoading = true;
        RewardedAd.load(
                context,
                id,
                adRequestBuilder.build(),
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        rewardedAd = null;
                        isLoading = false;
                        if(onAdmobLoadListener != null) onAdmobLoadListener.onError(loadAdError.getMessage());
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd r) {
                        rewardedAd = r;
                        isLoading = false;
                        if(onAdmobLoadListener != null) onAdmobLoadListener.onLoad();
                    }
                });
    }

    public void show(Activity activity, OnAdmobShowListener onAdmobShowListener){
        Log.i(TAG, "show: ");
        earnReward = false;
        if (rewardedAd == null) {
            onAdmobShowListener.onError("null");
            return;
        }
        rewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.

                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        rewardedAd = null;
                        onAdmobShowListener.onError(adError.getMessage());
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        rewardedAd = null;
                        if(earnReward){
                            onAdmobShowListener.onShow();
                        }else {
                            onAdmobShowListener.onError("no reward");
                        }
                    }
                });
        rewardedAd.show(
                activity,
                new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        earnReward = true;
                    }
                });
    }

    public boolean loaded() {
        Log.i(TAG, "loaded: " + (rewardedAd != null));
        return rewardedAd != null;
    }
}
