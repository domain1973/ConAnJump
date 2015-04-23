package com.ads.jump.conan.listener;

import com.ads.jump.conan.Assets;
import com.ads.jump.conan.actors.AreaGroup;
import com.ads.jump.conan.actors.SpriteImage;
import com.ads.jump.conan.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Administrator on 2014/7/4.
 */
public class SpriteListener extends GestureDetector.GestureAdapter {
    private static final float THREE_PLANE_SIZE = Assets.SHAPE_SIZE * 6;
    private static final float THREE_PLANE_SIZE_ADD_TOP_H = Assets.TOPBAR_HEIGHT + Assets.SHAPE_SIZE * 6;
    private Stage stage;
    private GameScreen gameScreen;
    private Vector3 touchPoint;
    private AreaGroup areaGroup;
    private Vector2 rawVector;
    private int destId;
    private SpriteImage downSpriteImage;

    public SpriteListener(Stage stage, GameScreen gs) {
        this.stage = stage;
        gameScreen = gs;
        touchPoint = new Vector3();
        rawVector = new Vector2();
        areaGroup = gs.getAreaGroup();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        downSpriteImage = null;
        if (!gameScreen.isSuspend()) {
            stage.getCamera().unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            SpriteImage spriteImage = areaGroup.getSpriteImage(touchPoint);
            if (spriteImage != null) {
                downSpriteImage = spriteImage;
                rawVector.set(spriteImage.getX(), spriteImage.getY());
            }
        }
        return super.touchDown(x, y, pointer, button);
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return super.tap(x,y,count,button);
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (downSpriteImage != null) {
            float x1 = downSpriteImage.getX() + deltaX;
            float y1 = downSpriteImage.getY() - deltaY;
            downSpriteImage.setPosition(x1, y1);
            destId = areaGroup.find(downSpriteImage);
            if (destId != -1) {
                gameScreen.runFlash(destId);
            } else {
                gameScreen.stopFlash();
            }
        }
        return super.pan(x, y, deltaX, deltaY);
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        press();
        return super.panStop(x, y, pointer, button);
    }

    private void press() {
        if (downSpriteImage != null) {
            stage.getCamera().unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0)); // 坐标转化
            plane2pool(downSpriteImage);
            Assets.soundBtn();
        }
        gameScreen.stopFlash();
    }

//    private int getFlashVector(SpriteImage spriteImage) {
//        int x = getX(spriteImage.getX());
//        if (spriteImage.getX() < 0 || x == THREE_PLANE_SIZE) {
//            return -1;
//        }
//        int y = getY(spriteImage.getY());
//        if (y < 0) {
//            return -1;
//        }
//        if (y >= (int) THREE_PLANE_SIZE_ADD_TOP_H) {
//            return -1;
//        }
//        return areaGroup.find(spriteImage.getId(), x, y);
//    }

    private void plane2pool(SpriteImage spriteImage) {
        if (destId != -1) {
            areaGroup.move(spriteImage, destId);
        } else {
            spriteImage.setPosition(rawVector.x, rawVector.y);
        }
    }

    private int getX(float x) {
        float size = Assets.SHAPE_SIZE;
        int x1 = (int) (x / size);
        return (int) (x1 * size);
    }

    private int getY(float y) {
        float size = Assets.SHAPE_SIZE;
        float t = Assets.HEIGHT - y - size - Assets.TOPBAR_HEIGHT;
        if (t < 0) {
            return -1;
        }
        int y1 = (int) (t / size);
        return (int) (y1 * size + Assets.TOPBAR_HEIGHT);
    }
}
