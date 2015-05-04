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
    private Stage stage;
    private GameScreen gameScreen;
    private Vector3 touchPoint;
    private AreaGroup areaGroup;
    private Vector2 rawVector;
    private int destId = -1;
    private int besideId = -1;
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
            Vector2 vector2 = areaGroup.find(downSpriteImage);
            if (vector2 != null) {
                destId = (int) vector2.x;
                besideId = (int) vector2.y;
            } else {
                destId = -1;
                besideId = -1;
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
    }

    private void plane2pool(SpriteImage spriteImage) {
        if (destId != -1 && besideId != -1) {
            SpriteImage si = areaGroup.getShapes()[besideId].getSpriteImage();
            if (si != null) {
                areaGroup.getShapes()[besideId].setSprite(null);
                areaGroup.getGateSprites().remove(si);
                si.remove();
            }
            areaGroup.move(spriteImage, destId);
        } else {
            spriteImage.setPosition(rawVector.x, rawVector.y);
        }
    }
}
