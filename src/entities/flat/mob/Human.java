package entities.flat.mob;

import world.World;

import java.util.Random;

import static java.lang.Math.*;

public class Human extends Mob {

    public float speed;
    public float walkSpeed = 0.005f;
    public float waterSpeed = 0.001f;
    public float health = 100;
    public float direction;
    private float actionTimer = 0f;
    public HumanState state;

    public Human(float x, float y, float z) {
        super(x, y, z, randomTexture());
        Random rand = new Random();
        direction = rand.nextInt(361);
        this.state = HumanState.WANDERING;
    }

    public static String randomTexture() {
        Random random = new Random();
        int textureIndex = random.nextInt(1);
        return "human_" + textureIndex;
    }

    public enum HumanState {
        WANDERING,
        FORAGING,
        BUILDING,
        SOCIALIZING,
    }

    public void update(double deltaTime) {

        status(health);

        switch (state) {
            case WANDERING -> {
                float x = ((float) cos((PI / 180) * (direction)) * speed);
                float y = ((float) sin((PI / 180) * (direction)) * speed);

                this.x += x;
                this.y += y;

                actionTimer += deltaTime;
            }
            case FORAGING -> {

            }
            case BUILDING -> {

            }
            case SOCIALIZING -> {

            }
        }

        if(actionTimer >= 2f){
            Random rand = new Random();
            direction += rand.nextInt(-90,90);
            actionTimer = 0f;
        }

        if (inWater) {
            speed = waterSpeed;
            unlockZ = true;
            if(z > -0.1f) {
                z -= 0.002f;
            }
            if(z < -0.1f){
                kill();
                System.out.println("Human Has Drowned");
            }
        }
        if (!inWater) {
            speed = walkSpeed;
            unlockZ = false;
        }
    }

    public void status(float health){
        if(health < 0.0f){
            kill();
        }
    }
}