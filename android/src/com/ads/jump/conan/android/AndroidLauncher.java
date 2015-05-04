package com.ads.jump.conan.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ads.jump.conan.Answer;
import com.ads.jump.conan.MyGame;
import com.ads.jump.conan.Settings;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import cn.domob.android.ads.AdManager;
import cn.domob.android.ads.AdView;
import cn.domob.android.ads.InterstitialAd;
import cn.domob.android.ads.InterstitialAdListener;

public class AndroidLauncher extends AndroidApplication {
    public static final String PUBLISHER_ID = "56OJxbJIuN7HHNQFZ3";
    public static final String InterstitialPPID = "16TLeSFoAp8e1NUdbQkv-vNi";
    public static final String InlinePPID = "16TLeSFoAp8e1NUd72kHIVIk";
    private InterstitialAd mInterstitialAd;
    private PEventImpl pEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        pEvent = new PEventImpl(AndroidLauncher.this);
        View gameView = initializeForView(new MyGame(pEvent), config);
        RelativeLayout layout = new RelativeLayout(this);
        layout.addView(gameView);
        setContentView(layout);
        loadGameConfig();
        //banner广告
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        // 设置广告条的悬浮位置
        layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角
        // 调用Activity的addContentView函数
        addContentView(new AdView(this, PUBLISHER_ID, InlinePPID), layoutParams);
        //插屏广告
        mInterstitialAd = new InterstitialAd(this, PUBLISHER_ID,
                InterstitialPPID);
        mInterstitialAd.setInterstitialAdListener(new DobomAdListenerImpl());
        mInterstitialAd.loadInterstitialAd();
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void spot() {
        if (mInterstitialAd.isInterstitialAdReady()) {
            mInterstitialAd.showInterstitialAd(this);
        } else {
            Log.i("DomobSDKDemo", "Interstitial Ad is not ready");
            mInterstitialAd.loadInterstitialAd();
        }
    }

    private void loadGameConfig() {
        SharedPreferences sharedata = getSharedPreferences("data", Context.MODE_PRIVATE);
        Settings.musicEnabled = sharedata.getBoolean("music", true);
        Settings.soundEnabled = sharedata.getBoolean("sound", true);
        Settings.unlockGateNum = sharedata.getInt("passNum", 0);
        Settings.clickedAd = sharedata.getBoolean("clickedAd", false);
        Answer.gateStars.clear();
        String[] split = sharedata.getString("starNum", "0").split("[,]");
        for (String starNum : split) {
            if (!"".equals(starNum)) {
                Answer.gateStars.add(Integer.parseInt(starNum));
            }
        }
    }

    public class DobomAdListenerImpl implements InterstitialAdListener {

        @Override
        public void onInterstitialAdReady() {
            Log.i("DomobSDKDemo", "onAdReady");
        }

        @Override
        public void onLandingPageOpen() {
            Log.i("DomobSDKDemo", "onLandingPageOpen");
        }

        @Override
        public void onLandingPageClose() {
            Log.i("DomobSDKDemo", "onLandingPageClose");
        }

        @Override
        public void onInterstitialAdPresent() {
            Log.i("DomobSDKDemo", "onInterstitialAdPresent");
        }

        @Override
        public void onInterstitialAdDismiss() {
            // Request new ad when the previous interstitial ad was closed.
            mInterstitialAd.loadInterstitialAd();
            Log.i("DomobSDKDemo", "onInterstitialAdDismiss");
        }

        @Override
        public void onInterstitialAdFailed(AdManager.ErrorCode arg0) {
            Log.i("DomobSDKDemo", "onInterstitialAdFailed");
        }

        @Override
        public void onInterstitialAdLeaveApplication() {
            Log.i("DomobSDKDemo", "onInterstitialAdLeaveApplication");
        }

        @Override
        public void onInterstitialAdClicked(InterstitialAd arg0) {
            Settings.clickedAd = true;
            Log.i("DomobSDKDemo", "onInterstitialAdClicked");
        }
    }
}
