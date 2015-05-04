package com.ads.jump.conan.actors;

import com.ads.jump.conan.Assets;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Administrator on 2014/7/4.
 */
public class SpriteImage extends Group {
    private int orientation;
    private int id;
    private float x = 0;
    private float y = 0;

    public SpriteImage(int shapeId) {
        Image conan = new Image(Assets.jumper);
        conan.setWidth(Assets.SHAPE_SIZE);
        conan.setHeight(Assets.SHAPE_SIZE);
        addActor(conan);
        setOrigin(Assets.SHAPE_SIZE, Assets.SHAPE_SIZE);
        id = shapeId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


}
