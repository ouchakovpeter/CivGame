package entities.flat.decor;


import entities.flat.base.FlatInstance;

import java.util.Random;

public class Boulder extends FlatInstance {

    public Boulder(float x, float y, float z) {
        super(x, y, z, randomTexture());
    }
    public static String randomTexture() {
        Random random = new Random();
        int textureIndex = random.nextInt(4);
        return "boulder_" + textureIndex;
    }
}