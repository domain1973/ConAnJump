package com.ads.jump.conan.screen;

import com.ads.jump.conan.Answer;
import com.ads.jump.conan.MyGame;
import com.ads.jump.conan.Assets;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Administrator on 2014/7/27.
 */
public class OtherScreen extends BaseScreen {
    private ImageButton shareBtn;
    private ImageButton adBtn;
    private Image star;
    private Label starLabel;

    public OtherScreen(MyGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        createBtns();
        removeLayerBg();
    }

    protected void createBtns() {
        super.createBtns();
        shareBtn = new ImageButton(new TextureRegionDrawable(Assets.barShare));
        shareBtn.setBounds(Assets.TOPBAR_HEIGHT, getY_bar(), Assets.TOPBAR_HEIGHT, Assets.TOPBAR_HEIGHT);
        shareBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Rectangle bound = new Rectangle(0, 0, shareBtn.getWidth(), shareBtn.getHeight());
                if (bound.contains(x, y)) {
                    Assets.playSound(Assets.btnSound);
                    getMyGame().getPEvent().share();
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });
        adBtn = new ImageButton(new TextureRegionDrawable(Assets.recommend));
        adBtn.setBounds(Assets.TOPBAR_HEIGHT * 2, getY_bar(), Assets.TOPBAR_HEIGHT, Assets.TOPBAR_HEIGHT);
        adBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Rectangle bound = new Rectangle(0, 0, adBtn.getWidth(), adBtn.getHeight());
                if (bound.contains(x, y)) {
                    Assets.playSound(Assets.btnSound);
                    if (getMyGame().getPEvent().isNetworkEnable()) {
                        getMyGame().setScreen(new AdGameScreen(getMyGame(), OtherScreen.this));
                    }
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });
        star = new Image(new TextureRegionDrawable(Assets.star));
        star.setBounds(Assets.WIDTH - Assets.TOPBAR_HEIGHT, getY_bar(), Assets.TOPBAR_HEIGHT, Assets.TOPBAR_HEIGHT);

        BitmapFont font = getCommonFont();
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE); // 创建一个Label样式，使用默认白色字体
        String str = getStarNumInfo();
        BitmapFont.TextBounds bounds = font.getBounds(str);
        starLabel = new Label(str, labelStyle);
        starLabel.setPosition(Assets.WIDTH - bounds.width - Assets.TOPBAR_HEIGHT, getY_bar());

        addActor(starLabel);
        addActor(shareBtn);
        if (getMyGame().getPEvent().isAdEnable()) {
            addActor(adBtn);
        }
        addActor(star);
    }

    protected String getStarNumInfo() {
        return "总计:" + getStarNum();
    }

    protected int getStarNum() {
        int starNum = 0;
        for (int num : Answer.gateStars) {
            starNum = starNum + num;
        }
        return starNum;
    }

    protected void setStarNum() {
        starLabel.setText(getStarNumInfo());
    }
}
