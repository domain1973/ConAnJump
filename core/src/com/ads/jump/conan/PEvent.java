package com.ads.jump.conan;

import com.ads.jump.conan.screen.GameScreen;
import com.ads.jump.conan.screen.MainScreen;

/**
 * Created by Administrator on 2014/9/10.
 */
public abstract class PEvent {

    public abstract void exit(MainScreen ms);

    public abstract void share();

    public abstract void install(Series series);

    public abstract boolean isNetworkEnable();

    public abstract void save();

    public abstract void about();

    public abstract void showNetFailInfo();

    public abstract void spotAd();

    public abstract boolean isAdEnable();

    public abstract void help();

    public abstract void pass();

    public abstract void showClickAdInfo();
}
