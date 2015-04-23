package com.ads.jump.conan;

import com.ads.jump.conan.screen.LoadingScreen;
import com.ads.jump.conan.screen.MainScreen;
import com.badlogic.gdx.Game;

public class MyGame extends Game {
    private LoadingScreen loadingScreen;
    private PEvent pEvent;

    public MyGame(PEvent pe) {
        pEvent = pe;
    }

    @Override
    public void create() {
        loadingScreen = new LoadingScreen(this);
        setScreen(loadingScreen);
    }

    public MainScreen getMainScreen() {
        return loadingScreen.getMainScreen();
    }

    public PEvent getPEvent() {
        return pEvent;
    }
}
