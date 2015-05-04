package com.ads.jump.conan.screen;

import com.ads.jump.conan.Answer;
import com.ads.jump.conan.Assets;
import com.ads.jump.conan.actors.AreaGroup;
import com.ads.jump.conan.listener.SpriteDetector;
import com.ads.jump.conan.listener.SpriteListener;
import com.ads.jump.conan.window.ResultWin;
import com.ads.jump.conan.window.SupsendWin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2014/6/24.
 */
public class GameScreen extends BaseScreen {
    private ScheduledExecutorService executorGateEnd;
    private ScheduledExecutorService executTime;

    private GateScreen gateScreen;
    private AreaGroup areaGroup;
    private SpriteDetector spriteDetector;
    private SupsendWin supsendWin;
    private InputMultiplexer multiplexer;
    private String timeStr;
    private Label labTime;
    private boolean failure = false;

    private boolean openResultWin;
    private boolean isPass;
    private boolean isSuspend;
    private int level;
    private int gateNum;
    private int seconds;
    private int starNum;
    private Label.LabelStyle commonWhiteStyle;
    private Label.LabelStyle commonRedStyle;
    private Label.LabelStyle gameScreenStyle;
    private Label labGateNum;
    private int gatePage;

    public GameScreen(GateScreen gs) {
        super(gs.getMyGame());
        gateNum = -1;
        gateScreen = gs;
        BitmapFont font = getCommonFont();
        font.setScale(Assets.WIDTH / 480);
        commonWhiteStyle = new Label.LabelStyle(font, Color.WHITE);
        commonRedStyle = new Label.LabelStyle(font, Color.RED);
        gameScreenStyle = new Label.LabelStyle(getGameScreenFont(), Color.WHITE);
        executorGateEnd = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void show() {
        if (!isShow()) {
            super.show();
            timeStr = "00:00";
            areaGroup = new AreaGroup();
            addActor(areaGroup);
            createTopBar();
            addLabels();
            createTimer();
            removeLayerBg();
            areaGroup.handler(gateNum);
            setShow(true);
        }
        multiplexer = new InputMultiplexer(); // 多输入接收器
        spriteDetector = new SpriteDetector(GameScreen.this, new SpriteListener(getStage(), GameScreen.this));
        multiplexer.addProcessor(spriteDetector); // 添加手势识别
        multiplexer.addProcessor(getStage()); // 添加舞台
        Gdx.input.setInputProcessor(multiplexer); // 设置多输入接收器为接收器
    }

    private void createTopBar() {
        super.createBtns();
        final ImageButton share = new ImageButton(new TextureRegionDrawable(Assets.barShare));
        share.setBounds(Assets.WIDTH - 3 * Assets.TOPBAR_HEIGHT, getY_bar(), Assets.TOPBAR_HEIGHT, Assets.TOPBAR_HEIGHT);
        share.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Rectangle bound = new Rectangle(0, 0, share.getWidth(), share.getHeight());
                if (bound.contains(x, y)) {
                    Assets.playSound(Assets.btnSound);
                    getMyGame().getPEvent().share();
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });
        addActor(share);

        final ImageButton resetBtn = new ImageButton(new TextureRegionDrawable(Assets.reset));
        resetBtn.setBounds(Assets.WIDTH - 2 * Assets.TOPBAR_HEIGHT, getY_bar(), Assets.TOPBAR_HEIGHT, Assets.TOPBAR_HEIGHT);
        resetBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Rectangle bound = new Rectangle(0, 0, resetBtn.getWidth(), resetBtn.getHeight());
                if (bound.contains(x, y)) {
                    Assets.playSound(Assets.btnSound);
                    areaGroup.handler(gateNum);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });
        addActor(resetBtn);

        final ImageButton suspendBtn = new ImageButton(new TextureRegionDrawable(Assets.suspend));
        suspendBtn.setBounds(Assets.WIDTH - Assets.TOPBAR_HEIGHT, getY_bar(), Assets.TOPBAR_HEIGHT, Assets.TOPBAR_HEIGHT);
        suspendBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Rectangle bound = new Rectangle(0, 0, suspendBtn.getWidth(), suspendBtn.getHeight());
                if (bound.contains(x, y)) {
                    Assets.playSound(Assets.btnSound);
                    suspendTimer();
                    supsendWin = new SupsendWin(GameScreen.this, gatePage);
                    addActor(supsendWin);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });
        addActor(suspendBtn);

        final Image helpBtn = new Image(new TextureRegionDrawable(Assets.help));
        helpBtn.addAction(Actions.repeat(2000, Actions.rotateBy(360, 3f)));
        helpBtn.setBounds(Assets.WIDTH - Assets.TOPBAR_HEIGHT, Assets.AREA_Y - Assets.TOPBAR_HEIGHT, Assets.TOPBAR_HEIGHT, Assets.TOPBAR_HEIGHT);
        helpBtn.setOrigin(helpBtn.getWidth() / 2, helpBtn.getHeight() / 2);
        helpBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Rectangle bound = new Rectangle(0, 0, helpBtn.getWidth(), helpBtn.getHeight());
                if (bound.contains(x, y)) {
                    Assets.playSound(Assets.btnSound);
                    getMyGame().getPEvent().help();
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });
        addActor(helpBtn);

        returnBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Rectangle bound = new Rectangle(0, 0, returnBtn.getWidth(), returnBtn.getHeight());
                if (bound.contains(x, y)) {
                    Assets.playSound(Assets.btnSound);
                    suspendTimer();
                    gateScreen.buildGateImage(gatePage);
                    getMyGame().setScreen(gateScreen);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    private void addLabels() {
        BitmapFont font = getCommonFont();
        BitmapFont.TextBounds bounds = font.getBounds("00");
        labTime = new Label("", commonWhiteStyle);
        labTime.setPosition(Assets.TOPBAR_HEIGHT, Assets.HEIGHT - Assets.TOPBAR_HEIGHT / 2);
        addActor(labTime);

        String s = "跨越移动柯南直到剩下最后一个";
        Label c = new Label(s, gameScreenStyle);
        c.setPosition(0, Assets.SHAPE_SIZE + Assets.SHAPE_SIZE /2);
        addActor(c);
        labGateNum = new Label("", commonWhiteStyle);
        labGateNum.setPosition(0, Assets.AREA_Y + bounds.height - 5);
        addActor(labGateNum);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            if (!isSuspend && isClosedResultWin()) {
                gateScreen.setBackFlag(true);
                gateScreen.buildGateImage(gatePage);
                getMyGame().setScreen(gateScreen);
                return;
            }
        }
        super.render(delta);
        handlePass();
        labTime.setText(timeStr);
        labGateNum.setText((gateNum + 1) + "/" + Answer.CHALLENGES.size());
    }

    private void handlePass() {
        if (!isPass) {
            Gdx.input.setInputProcessor(multiplexer);
            handleGate();
        }
        if (openResultWin) {
            computerStarNum();
            addActor(new ResultWin(this, starNum));
            openResultWin = false;
            if (starNum == 0) {
                getMyGame().getPEvent().spotAd();
            }
        }
    }

    private boolean isClosedResultWin() {
        Array<Actor> actors = getStage().getActors();
        for (Actor actor : actors) {
            if (actor instanceof ResultWin) {
                return false;
            }
        }
        return true;
    }

    private void computerStarNum() {
        int[] timeLevel = Answer.timeLevels.get(level);
        if (failure) {
            starNum = 4;
            return;
        }
        if (timeLevel[0] < seconds && timeLevel[1] >= seconds) {
            starNum = 1;
        } else if (timeLevel[1] < seconds && timeLevel[2] >= seconds) {
            starNum = 2;
        } else if (timeLevel[2] < seconds && timeLevel[3] >= seconds) {
            starNum = 3;
        } else {
            starNum = 0;
        }
        if (Answer.gateStars.size() > gateNum) {//可能重玩
            if (starNum > 0) {
                Answer.gateStars.set(gateNum, starNum);
            }
        } else {
            Answer.gateStars.add(starNum);
        }
    }

    private void handleGate() {
        if (spriteDetector.isPass()) {
            executTime.shutdown();
            passAfter();
            failure = false;
        } else if (spriteDetector.isFailure()) {
            passAfter();
            failure = true;
            suspendTimer();
        } else if (seconds <= 0) {//game over
            Gdx.input.setInputProcessor(null);
            executTime.shutdown();
            openResultWin = true; //关卡结束
            isPass = true;
            failure = false;
        }
    }

    private void passAfter() {
        Gdx.input.setInputProcessor(null);
        Runnable runner = new Runnable() {
            public void run() {
                openResultWin = true; //关卡结束
            }
        };
        executorGateEnd.schedule(runner, 1500, TimeUnit.MILLISECONDS);
        isPass = true;
    }

    private void suspendTimer() {
        isSuspend = true;
    }

    public void resumeTimer() {
        isSuspend = false;
    }

    private void createTimer() {
        isSuspend = false;
        seconds = Answer.timeLevels.get(level)[3];
        labTime.setStyle(commonWhiteStyle);
        executTime = Executors.newSingleThreadScheduledExecutor();
        executTime.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (!isSuspend) {
                    seconds--; //
                    if (seconds < 8) {
                        if (seconds % 2 == 0) {
                            labTime.setStyle(commonRedStyle);
                        } else {
                            labTime.setStyle(commonWhiteStyle);
                        }
                    }
                    buildTimeStr();
                }
            }

            private void buildTimeStr() {
                String str0 = "%d";
                String str1 = "%d";
                int minute = seconds / 60;
                int second = seconds % 60;
                if (seconds < 0) {
                    second = 0;
                }
                if (minute < 10) {
                    str0 = "0%d";
                }
                if (second < 10) {
                    str1 = "0%d";
                }
                timeStr = String.format("倒计时" + str0 + ":" + str1, minute, second);
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public void pass2false() {
        isPass = false;
        failure = false;
        resumeTimer();
    }

    public void return2init() {
        pass2false();
        if (!executTime.isShutdown()) {
            executTime.shutdown();
        }
        createTimer();
    }

    public void handleNewGate(int num) {
        level = num / Assets.LEVEL_GATE_MAX;
        if (isShow()) {
            if (num != gateNum) {
                areaGroup.handler(num);
                return2init();
            }
            resumeTimer();
        }
        gateNum = num;
        gatePage = num / Assets.SCREEN_GATE_MAX;
    }

    public int getGateNum() {
        return gateNum;
    }

    public boolean isSuspend() {
        return isSuspend;
    }

    public GateScreen getGateScreen() {
        return gateScreen;
    }

    public AreaGroup getAreaGroup() {
        return areaGroup;
    }

    public boolean nextNeedClickAD() {
        return getGateScreen().isNeedClickAD(gateNum + 1);
    }
}
