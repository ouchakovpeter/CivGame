package entities;

import entities.flat.mob.*;
import entities.flat.base.*;
import render.*;
import world.*;

import java.util.ArrayList;
import java.util.List;

public class MobManager {
    private List<Mob> mobs = new ArrayList<>();

    public void addMob(Mob mob) {
        mobs.add(mob);
    }
    public List<Mob> getMobs() {
        return mobs;
    }
    public void update(double deltaTime, World world) {
        for (Mob mob : mobs) {
            mob.update(deltaTime);
            mob.adjustElevation(world);
            mob.wrap(world);

            boolean water = world.inWater((int)mob.x, (int)mob.y, (int)mob.z);
            mob.setInWater(water);
        }
        mobs.removeIf(Mob::isDead);
    }
    public void adjustElevation(World world) {
        for (Mob mob : mobs) {
            mob.adjustElevation(world);
        }
    }
    public void wrap(World world) {
        for (Mob mob : mobs) {
            mob.wrap(world);
        }
    }

    public void spawnHuman(World world) {
        for (int i = 0; i < world.getWidth() * 5; i++) {
            float x, y;

            do {
                x = (float) (Math.random() * world.getWidth());
                y = (float) (Math.random() * world.getHeight());
            }
            while (world.inWater((int)x,(int)y,0)); //keeps running until the tile selected is not water

            Human human = new Human(x, y, 0);
            addMob(human);
        }
    }

    public void clearMobs(){
        mobs.clear();
    }

    public void render(FlatRenderer flats, Shader flatShader, Camera camera) {
        if (mobs.isEmpty()) return;

        // Collect all mobs as FlatInstances
        List<FlatInstance> mobInstances = new ArrayList<>();
        for (Mob mob : mobs) {
            mobInstances.add(mob); // Mob extends FlatInstance, so this works directly
        }

        // Render them in a batch
        flats.renderBatch(mobInstances, flatShader, camera);
    }
}
