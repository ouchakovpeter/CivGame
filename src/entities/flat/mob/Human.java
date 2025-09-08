package entities.flat.mob;

import world.World;

import java.util.Random;

import static java.lang.Math.*;

public class Human extends Mob {
    public float speed = 0.01f;
    public float health = 100;
    public float direction;

    public Human(float x, float y, float z) {
        super(x, y, z, randomTexture());
        Random rand = new Random();
        direction = rand.nextInt(361);
    }

    public static String randomTexture() {
        Random random = new Random();
        int textureIndex = random.nextInt(1);
        return "human_" + textureIndex;
    }

    public void update(double deltaTime) {

        float x = ((float) cos((PI/180)*(direction)) * speed);
        float y = ((float) sin((PI/180)*(direction)) * speed);

        this.x += x;
        this.y += y;

        if (inWater) {
            speed = 0.001f;
            unlockZ = true;
            z = -0.05f; // set manually
        }
        if (!inWater) {
            speed = 0.01f;
            unlockZ = false;
        }
    }

    public void status(float health){
        if(health < 0.0f){
            //death
        }
    }
}