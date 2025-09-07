package entities.flat.mob;


import entities.flat.base.FlatInstance;

import java.util.Random;

public class Human extends FlatInstance {

    public Human(float x, float y, float z) {
        super(x, y, z, randomTexture());
    }
    public static String randomTexture() {
        Random random = new Random();
        int textureIndex = random.nextInt(3);
        return "spruce_tree_" + textureIndex;
    }
}