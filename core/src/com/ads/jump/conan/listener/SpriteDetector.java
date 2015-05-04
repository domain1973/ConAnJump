package com.ads.jump.conan.listener;

import com.ads.jump.conan.actors.SpriteImage;
import com.ads.jump.conan.screen.GameScreen;
import com.badlogic.gdx.input.GestureDetector;

import java.util.List;

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

    public boolean isFailure() {
        boolean b = true;
        List<SpriteImage> spriteImages = gameScreen.getAreaGroup().getGateSprites();
        for (SpriteImage spriteImage : spriteImages) {
            if (gameScreen.getAreaGroup().isBesideSpriteImage(spriteImage.getId())) {
                b = false;
            }
        }
        return b;
    }

    public boolean isPass() {
        return gameScreen.getAreaGroup().getGateSprites().size() == 1;
    }
}
