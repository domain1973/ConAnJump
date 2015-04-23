package com.ads.jump.conan.listener;

import com.ads.jump.conan.screen.GameScreen;
import com.badlogic.gdx.input.GestureDetector;

/**
 * Created by Administrator on 2014/7/4.
 */
public class SpriteDetector extends GestureDetector {
    private GameScreen gameScreen;

    /**
     * Creates a new GestureDetector with default values: halfTapSquareSize=20, tapCountInterval=0.4f, longPressDuration=1.1f,
     * maxFlingDelay=0.15f.
     *
     * @param listener
     */
    public SpriteDetector(GameScreen gs, GestureListener listener) {
        super(listener);
        gameScreen = gs;
    }

    /**
     * 获取旁边的Shape ID
     * @param id
     * @return
     */
    private int[] getO(int id) {
        return null;
    }

    public boolean isPass() {
        return gameScreen.getAreaGroup().getGateSprites().size() == 1;
    }
}
