package com.ads.jump.conan.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;

import com.ads.jump.conan.Answer;
import com.ads.jump.conan.MyGame;
import com.ads.jump.conan.Settings;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
    private PEventImpl pEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        pEvent = new PEventImpl(AndroidLauncher.this);
        initialize(new MyGame(pEvent), config);
        loadGameConfig();
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
    }

    private void loadGameConfig() {
        SharedPreferences sharedata = getSharedPreferences("data", Context.MODE_PRIVATE);
        Settings.musicEnabled = sharedata.getBoolean("music", true);
        Settings.soundEnabled = sharedata.getBoolean("sound", true);
        Settings.unlockGateNum = sharedata.getInt("passNum", 0);
        Settings.helpNum = sharedata.getInt("helpNum", 3);//TODO
        Answer.gateStars.clear();
        String[] split = sharedata.getString("starNum", "0").split("[,]");
        for (String starNum : split) {
            if (!"".equals(starNum)) {
                Answer.gateStars.add(Integer.parseInt(starNum));
            }
        }
    }
}
