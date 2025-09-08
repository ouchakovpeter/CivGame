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
            adjustElevation(world);
            wrap(world);

            boolean water = world.inWater((int)mob.x, (int)mob.y, (int)mob.z);
            mob.setInWater(water);

        }
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

    public void spawnHuman(Camera camera){
        Human human = new Human(camera.getPosition().x, camera.getPosition().y,0 );
        addMob(human);
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
