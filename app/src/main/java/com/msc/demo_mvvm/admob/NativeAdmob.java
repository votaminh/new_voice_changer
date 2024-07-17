package com.msc.demo_mvvm.admob;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MediaAspectRatio;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.msc.demo_mvvm.BuildConfig;
import com.msc.demo_mvvm.R;

public class NativeAdmob extends BaseAdmob {
    private static final String TAG = "nativeAdmob";
    private static int indexLoadNative = 0;

    private final String id;
    private int indexDebug = 0;
    private final MutableLiveData<NativeAd> nativeAdLive = new MutableLiveData<>();

    private boolean canReload = false;

    public NativeAdmob(Context context, String id) {
        super(context);
        this.id = id;
    }

    public boolean available() {
        if (nativeAdLive.getValue() != null && !canReload) {
            return true;
        } else {
            return false;
        }
    }


    public void showNative(
            ShimmerFrameLayout parent,
            OnAdmobShowListener onNativeShowListener
    ) {
        Log.i(TAG, "showNative: ");
        if (nativeAdLive.getValue() != null && parent != null) {

            enableReload(true);

            NativeAdView adView = parent.findViewById(R.id.native_ad_view);

            parent.hideShimmer();
            parent.stopShimmer();

            populateNativeAdView(NativeAdmob.this, nativeAdLive.getValue(), adView);

            if (onNativeShowListener != null) {
                onNativeShowListener.onShow();
            }

        } else {
            if (onNativeShowListener != null) {
                onNativeShowListener.onError("");
            }
        }
    }

    private void enableReload(boolean b) {
        canReload = b;
    }

    public void load(OnAdmobLoadListener onAdmobLoadListener) {
        load(onAdmobLoadListener, 30000);
    }

    public void load(OnAdmobLoadListener onAdmobLoadListener, boolean nativeFull) {
        load(onAdmobLoadListener, 30000, nativeFull);
    }

    public void load(OnAdmobLoadListener onNativeLoadListener1, long timeoutMillis) {
        load(onNativeLoadListener1, timeoutMillis, false);
    }

    public void load(OnAdmobLoadListener onNativeLoadListener1, long timeoutMillis, boolean nativeFull) {
        indexDebug = (indexLoadNative++);
        Log.i(TAG, "NativeAdmob: " + id + " indexDebug: " + indexDebug);

        enableReload(false);

        Log.i(TAG, "load: ");
        onAdmobLoadListener = onNativeLoadListener1;
        new Handler().postDelayed(() -> {
            if (onAdmobLoadListener != null) {
                onAdmobLoadListener.onError("timeout");
                onAdmobLoadListener = null;
                Log.i(TAG, "load: timeout");
            }
        }, timeoutMillis);

        AdLoader.Builder builder = new AdLoader.Builder(context, id);
        builder.forNativeAd(
                a -> {

                    if (BuildConfig.DEBUG) {
                        Log.i(TAG, "onNativeAdLoaded: " + a.getHeadline());
                    } else {
                        Log.i(TAG, "onNativeAdLoaded: ");
                    }

                    if (nativeAdLive.getValue() != null) {
                        nativeAdLive.getValue().destroy();
                    }

                    setNativeAd(a);

                    if (onAdmobLoadListener != null) {
                        onAdmobLoadListener.onLoad();
                        onAdmobLoadListener = null;
                    }
                });

        if(nativeFull){
            VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(false).setCustomControlsRequested(true).build();
            NativeAdOptions adOptions = new NativeAdOptions.Builder().setMediaAspectRatio(MediaAspectRatio.ANY).setVideoOptions(videoOptions).build();
            builder.withNativeAdOptions(adOptions);
        }else {
            VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
            NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
            builder.withNativeAdOptions(adOptions);
        }

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.i(TAG, "onAdFailedToLoad: ");
                if (onAdmobLoadListener != null) {
                    onAdmobLoadListener.onError(loadAdError.getMessage());
                    onAdmobLoadListener = null;
                }
            }
        }).build();
        adLoader.loadAd(adRequestBuilder.build());
    }

    public void reLoad() {
        Log.i(TAG, "reLoad: ");
        if (!canReload) {
            Log.i(TAG, "not show, disable reload");
            return;
        }

        load(null);
    }

    public MutableLiveData<NativeAd> getNativeAdLive() {
        return nativeAdLive;
    }

    public void setNativeAd(NativeAd nativeAd) {
        nativeAdLive.postValue(nativeAd);
    }

    public static void populateNativeAdView(NativeAdmob nativeAdmob, NativeAd nativeAd, NativeAdView adView) {
        Log.i(TAG, "populateNativeAdView: ");
        // Set the media view.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every NativeAd.
        if (BuildConfig.DEBUG) {
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline() + nativeAdmob.indexDebug);
        } else {
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        }

        try {
            MediaView mediaView = adView.getMediaView();
            if (mediaView != null) {
                mediaView.setMediaContent(nativeAd.getMediaContent());
            }
        }catch (Exception e){

        }

        try {
            if (nativeAd.getBody() == null) {
                adView.getBodyView().setVisibility(View.INVISIBLE);
            } else {
                adView.getBodyView().setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            }
        } catch (Exception e) {

        }


        try {
            if (nativeAd.getCallToAction() == null) {
                adView.getCallToActionView().setVisibility(View.INVISIBLE);
            } else {
                adView.getCallToActionView().setVisibility(View.VISIBLE);
                ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }
        } catch (Exception e) {

        }


        try {
            if (nativeAd.getIcon() == null) {
                adView.getIconView().setVisibility(View.GONE);
            } else {
                ((ImageView) adView.getIconView()).setImageDrawable(
                        nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }


        try {
            if (nativeAd.getPrice() == null) {
                adView.getPriceView().setVisibility(View.INVISIBLE);
            } else {
                adView.getPriceView().setVisibility(View.VISIBLE);
                ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
            }
        } catch (Exception e) {

        }


        try {
            if (nativeAd.getStore() == null) {
                adView.getStoreView().setVisibility(View.INVISIBLE);
            } else {
                adView.getStoreView().setVisibility(View.VISIBLE);
                ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
            }
        } catch (Exception e) {

        }


        try {
            if (nativeAd.getStarRating() == null) {
                adView.getStarRatingView().setVisibility(View.INVISIBLE);
            } else {
                ((RatingBar) adView.getStarRatingView())
                        .setRating(nativeAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {

        }


        try {
            if (nativeAd.getAdvertiser() == null) {
                adView.getAdvertiserView().setVisibility(View.INVISIBLE);
            } else {
                ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {

        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getMediaContent().getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (nativeAd.getMediaContent() != null && nativeAd.getMediaContent().hasVideoContent()) {

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    super.onVideoEnd();
                }
            });
        } else {
        }
    }
}
