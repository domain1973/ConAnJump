package com.ads.jump.conan.actors;

import com.ads.jump.conan.Assets;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Created by Administrator on 2014/7/5.
 */
public class GateImageBtn extends ImageButton {

    public GateImageBtn(Drawable imageUp, int id) {
        super(imageUp);
        float x_off = Assets.WIDTH/7;
        float gateBtnSize = x_off - x_off/8;
        float hspace = Assets.HEIGHT / 8;
        setBounds(x_off + id % 5 * x_off, Assets.HEIGHT*3/4 - id / 5 * hspace,gateBtnSize,gateBtnSize);
    }
}
