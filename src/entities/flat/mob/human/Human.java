package entities.flat.mob.human;

import entities.flat.mob.Mob;
import world.Tile;
import world.World;

import java.util.Random;

import static java.lang.Math.*;

public class Human extends Mob {

    public World world;
    public String name;
    public Personality personality;
    public float walkSpeed = 0.005f;
    public float waterSpeed = 0.001f;
    private float actionTimer = 0f;
    public HumanState state;

    public Human(float x, float y, float z, Random rand) {
        super(x, y, z, randomTexture(rand));
        this.personality = new Personality(rand);
        direction = rand.nextInt(361);
        this.state = HumanState.WANDERING;
        this.health = 100;
        this.viewDistance = 2f;//2 tiles ahead
    }

    public static String randomTexture(Random rand) {
        int textureIndex = rand.nextInt(1);
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

            }
            case FORAGING -> {

            }
            case BUILDING -> {

            }
            case SOCIALIZING -> {

            }
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