package entities;

import entities.flat.mob.*;
import entities.flat.base.*;
import org.joml.Vector3f;
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
    public void update(double deltaTime) {
        for (Mob mob : mobs) {
            mob.update(deltaTime);
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
