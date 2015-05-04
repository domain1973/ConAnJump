package com.ads.jump.conan.actors;

import com.ads.jump.conan.Answer;
import com.ads.jump.conan.Assets;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/7/6.
 */
public class AreaGroup extends Group {
    private Shape[] shapes = new Shape[36];//所有的方框
    private List<Shape> gateShapes = new ArrayList<Shape>();//当前关卡的方框
    private List<SpriteImage> gateSprites = new ArrayList<SpriteImage>();//当前关卡的精灵

    public AreaGroup() {
        buildShapeImages();
    }

    private void buildShapeImages() {
        int id = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                shapes[id] = new Shape(id);
                id ++;
            }
        }
    }

    public Shape[] getShapes() {
        return shapes;
    }

    public void handler(int num) {
        for (Shape actor : shapes) {
            actor.setSprite(null);
        }
        for (Shape actor : gateShapes) {
            actor.remove();
        }
        gateShapes.clear();
        for (SpriteImage actor : gateSprites) {
            actor.remove();
        }
        gateSprites.clear();

        int[] strings = Answer.CHALLENGES.get(num);
        for (int i=0; i<strings.length; i++) {
            int f = strings[i];
            if (f != 0) {
                Shape shape = shapes[i];
                gateShapes.add(shape);
                addActor(shape);
            }
        }
        for (int i=0; i<strings.length; i++) {
            int f = strings[i];
            if (f == 2) {
                Shape shape = shapes[i];
                SpriteImage si = new SpriteImage(i);
                si.setPosition(shape.getX(), shape.getY());
                shape.setSprite(si);
                gateSprites.add(si);
                addActor(si);
            }
        }
    }

    public SpriteImage getSpriteImage(Vector3 touchPoint) {
        for (Shape shape : shapes) {
            if (shape.contains(touchPoint.x, touchPoint.y)) {
                return shape.getSpriteImage();
            }
        }
        return null;
    }

    public void move(SpriteImage spriteImage, int newId) {
        int id = spriteImage.getId();
        shapes[id].setSprite(null);
        shapes[newId].setSprite(spriteImage);
        spriteImage.setPosition(shapes[newId].getX(), shapes[newId].getY());
        spriteImage.setId(newId);
    }

    public Vector2 find(SpriteImage spriteImage) {
        int destId = getId(spriteImage.getX(), spriteImage.getY());
        if (destId != -1 && spriteImage.getId() != destId) {
            SpriteImage si = shapes[destId].getSpriteImage();
            if (si == null) {
                int besideId = getBesideId(spriteImage.getId(), destId);
                if (besideId != -1 && shapes[besideId].getSpriteImage() != null) {
                    return new Vector2(destId, besideId);
                }
            }
        }
        return null;
    }

    private int getId(float x, float y) {
        for (Shape shape : gateShapes) {
            if (shape.contains(x + Assets.SHAPE_SIZE/2, y + Assets.SHAPE_SIZE/2)) {
                return shape.getId();
            }
        }
        return -1;
    }

    private int getBesideId(int resId, int destId) {
       return shapes[resId].getBesideId(destId);
    }

    public List<SpriteImage> getGateSprites() {
        return gateSprites;
    }

    public boolean isBesideSpriteImage(int id) {
        for (Vector2 vector2 : shapes[id].getRelatives()) {
            int besideId = (int)vector2.x;
            int destId = (int)vector2.y;
            if (shapes[besideId].getSpriteImage() != null) {
                for (Shape shape : gateShapes) {
                    if (shape.getId() == destId && shape.getSpriteImage() == null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public class Shape extends Image {
        private int id;
        private Rectangle bounds;
        private SpriteImage spriteImage;
        private List<Vector2> relatives = new ArrayList<Vector2>();

        public Shape(int i) {
            super(Assets.cube);
            id = i;
            float y = Assets.AREA_Y + (5 - i / 6) * Assets.SHAPE_SIZE;
            float x = i%6 * Assets.SHAPE_SIZE;
            setBounds(x, y, Assets.SHAPE_SIZE, Assets.SHAPE_SIZE);
            bounds = new Rectangle(x, y, Assets.SHAPE_SIZE, Assets.SHAPE_SIZE);
            fillRelativeShapes();
        }

        private void fillRelativeShapes() {
            String[] strings = Answer.SHAPES.get(id);
            for (String string : strings) {
                String[] strs = string.split(",");
                float beside = Float.parseFloat(strs[0]);
                relatives.add(new Vector2(beside, Float.parseFloat(strs[1])));
            }
        }

        public List<Vector2>  getRelatives() {
            return relatives;
        }

        public int getId() {
            return id;
        }

        public boolean contains(float x, float y) {
            return bounds.contains(x, y);
        }

        public SpriteImage getSpriteImage() {
            return spriteImage;
        }

        public void setSprite(SpriteImage si) {
            spriteImage = si;
        }

        public int getBesideId(int id) {
            for (Vector2 vector2 : relatives) {
                if (vector2.y == id) {
                    return (int)vector2.x;
                }
            }
            return -1;
        }
    }
}
