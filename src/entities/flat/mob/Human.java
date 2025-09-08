package entities.flat.mob;

import entities.flat.base.FlatInstance;
import java.util.Random;

public class Human extends Mob {

    public Human(float x, float y, float z) {
        super(x, y, z, randomTexture());
    }

    public static String randomTexture() {
        Random random = new Random();
        int textureIndex = random.nextInt(1);
        return "horse_" + textureIndex;
    }

    public void update(double deltaTime) {
        super.update(deltaTime);

        this.x += 0.1f;
    }
}