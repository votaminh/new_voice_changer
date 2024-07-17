package com.msc.demo_mvvm.admob;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;

import androidx.annotation.NonNull;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

public class BannerAdmob extends BaseAdmob {
    private static final String TAG = "bannerAdmob";
    private AdView adView;
    private CollapsiblePositionType collapsiblePositionType;
    public BannerAdmob(Context context, CollapsiblePositionType collapsiblePositionType) {
        super(context);
        this.collapsiblePositionType = collapsiblePositionType;
    }

    public void showBanner(
            Activity activity,
            String id,
            ShimmerFrameLayout parent){

        Log.i(TAG, "showBanner: " + id);

        AdSize adSize = getAdSize(activity, parent);
        adView = new AdView(activity);
        adView.setAdSize(adSize);
        adView.setAdUnitId(id);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.i(TAG, "onAdFailedToLoad: ");
                super.onAdFailedToLoad(loadAdError);
                parent.removeAllViews();
            }

            @Override
            public void onAdLoaded() {
                Log.i(TAG, "onAdLoaded: ");
                super.onAdLoaded();
                parent.removeAllViews();
                parent.addView(adView);
                parent.hideShimmer();
            }
        });

        Bundle bundle = new Bundle();

        switch (collapsiblePositionType){
            case NONE:break;
            case TOP:
                bundle.putString("collapsible", "top");
                adRequestBuilder.addNetworkExtrasBundle(AdMobAdapter.class, bundle);
                break;
            case BOTTOM:
                bundle.putString("collapsible", "bottom");
                adRequestBuilder.addNetworkExtrasBundle(AdMobAdapter.class, bundle);
                break;
        }

        adView.loadAd(adRequestBuilder.build());
    }

    public AdSize getAdSize(Activity mActivity, View adContainer) {
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainer.getWidth();
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mActivity, adWidth);
    }

    public void onResume() {
        if(adView != null){
            adView.resume();        }
    }

    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
    }

    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
    }

    public void reload() {
        adView.loadAd(adRequestBuilder.build());
    }
}
