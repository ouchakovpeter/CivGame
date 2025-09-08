package entities.flat.mob;

import java.util.Random;

public class Horse extends Mob {

    public Horse(float x, float y, float z) {
        super(x, y, z, randomTexture());
    }
    public static String randomTexture() {
        Random random = new Random();
        int textureIndex = random.nextInt(1);
        return "horse_" + textureIndex;
    }
}