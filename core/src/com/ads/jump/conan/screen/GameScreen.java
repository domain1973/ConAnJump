package com.ads.jump.conan.screen;

import com.ads.jump.conan.Answer;
import com.ads.jump.conan.Assets;
import com.ads.jump.conan.Settings;
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
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
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
    private ScheduledExecutorService executStarCount;
    private ScheduledExecutorService executTime;

    private Image tFlash;
    private ParticleEffect effect;
    private GateScreen gateScreen;
    private AreaGroup areaGroup;
    private SpriteDetector spriteDetector;
    private SupsendWin supsendWin;
    private InputMultiplexer multiplexer;
    private String timeStr;
    private Label labTime;
    private Label labCount;

    private boolean openResultWin;
    private boolean isPass;
    private boolean isSuspend;
    private boolean isUsedHelp;
    private boolean isUsingHelp;
    private int level;
    private int gateNum;
    private int seconds;
    private int starNum;
    private int planeId = 0;
    private Image flashImage;
    private Label.LabelStyle yellowStyle;
    private Label.LabelStyle redStyle;
    private Label.LabelStyle quizStyle;
    private Label labGateNum;
    private int gatePage;

    public GameScreen(GateScreen gs) {
        super(gs.getMyGame());
        gateNum = -1;
        gateScreen = gs;
        isUsingHelp = true;
        BitmapFont font = getOtherFont();
        font.setScale(Assets.WIDTH / 480 * (float)0.8);
        yellowStyle = new Label.LabelStyle(font, Color.YELLOW);
        redStyle = new Label.LabelStyle(font, Color.RED);
        quizStyle = new Label.LabelStyle(getQuizFont(), Color.RED);
        executorGateEnd = Executors.newSingleThreadScheduledExecutor();
        flashImage = new Image(Assets.flash);
    }

    @Override
    public void show() {
        if (!isShow()) {
            super.show();
            Image bg = new Image(Assets.gameareaBg);
            bg.setBounds(0, Assets.HEIGHT - Assets.TOPBAR_HEIGHT - Assets.WIDTH, Assets.WIDTH, Assets.WIDTH);
            addActor(bg);
            timeStr = "00:00";
            areaGroup = new AreaGroup();
            addActor(areaGroup);
            createTopBar();
            addLabels();
            initEffect();
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
        final ImageButton sosBtn = new ImageButton(new TextureRegionDrawable(Assets.light));
        sosBtn.setBounds(Assets.WIDTH - 4 * Assets.TOPBAR_HEIGHT, getY_bar(), Assets.TOPBAR_HEIGHT, Assets.TOPBAR_HEIGHT);
        sosBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Rectangle bound = new Rectangle(0, 0, sosBtn.getWidth(), sosBtn.getHeight());
                if (bound.contains(x, y)) {
                    Assets.playSound(Assets.btnSound);
                    if (Settings.helpNum > 0) {//TODO
                        getMyGame().getPEvent().sos(GameScreen.this);
                    } else {
                        getMyGame().getPEvent().invalidateSos();
                    }
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });
        addActor(sosBtn);

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
                    getMyGame().getPEvent().help(level);
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
        BitmapFont font = getOtherFont();
        BitmapFont.TextBounds bounds = font.getBounds("00");
        labTime = new Label("", yellowStyle);
        labTime.setPosition(Assets.TOPBAR_HEIGHT, Assets.HEIGHT - Assets.TOPBAR_HEIGHT / 2);
        addActor(labTime);
        float w = bounds.width;
        labCount = new Label("", redStyle);
        labCount.setPosition(Assets.WIDTH - 4 * Assets.TOPBAR_HEIGHT - w / 3, Assets.HEIGHT - Assets.TOPBAR_HEIGHT / 2);
        addActor(labCount);

        String s = "把下面的6个拼图片移到上面的图中,移动后点击\n拼图可变方向. 注意拼图是不能重叠的.";
        Label c = new Label(s, quizStyle);
        c.setPosition(0, Assets.SHAPE_SIZE + Assets.SHAPE_SIZE /2);
        addActor(c);
        labGateNum = new Label("", yellowStyle);
        labGateNum.setPosition(0, Assets.AREA_Y + bounds.height - 5);
        addActor(labGateNum);
    }

    private void initEffect() {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("data/test.p"), Gdx.files.internal("data/"));
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
        labCount.setText(Settings.helpNum + "");
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
        if (seconds <= 0) {//game over
            Gdx.input.setInputProcessor(null);
            executTime.shutdown();
            openResultWin = true; //关卡结束
            isPass = true;
        } else if (spriteDetector.isPass()) {
            Gdx.input.setInputProcessor(null);
            executTime.shutdown();
            Runnable runner = new Runnable() {
                public void run() {
                    openResultWin = true; //关卡结束
                }
            };
            executorGateEnd.schedule(runner, 1500, TimeUnit.MILLISECONDS);
            isPass = true;
        }
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
        labTime.setStyle(yellowStyle);
        executTime = Executors.newSingleThreadScheduledExecutor();
        executTime.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (!isSuspend) {
                    seconds--; //
                    if (seconds < 15) {
                        if (seconds % 2 == 0) {
                            labTime.setStyle(redStyle);
                        } else {
                            labTime.setStyle(yellowStyle);
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

    public void runFlash(int id) {
        flashImage.setPosition(areaGroup.getShapes()[id].getX(), areaGroup.getShapes()[id].getY());
        addActor(tFlash);
    }

    public void stopFlash() {
        if (flashImage != null) {
            flashImage.remove();
        }
    }


    public void return2init() {
        isPass = false;
        if (!executTime.isShutdown()) {
            executTime.shutdown();
        }
        createTimer();
        reset();
    }

    private void reset() {
        stopFlash();
    }

    public void useSos() {
        isUsingHelp = true;
        isUsedHelp = true;
        multiplexer.removeProcessor(spriteDetector);
        multiplexer.removeProcessor(getStage());
        reset();
    }

    public void handleNewGate(int num) {
        level = num / Assets.LEVEL_GATE_MAX;
        if (isShow()) {
            if (num != gateNum) {
                return2init();
                areaGroup.handler(gateNum);
            } else {
                resumeTimer();
            }
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
}
