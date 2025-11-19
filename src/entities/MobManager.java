package entities;

import entities.flat.mob.*;
import entities.flat.base.*;
import entities.flat.mob.human.Human;
import render.*;
import world.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

            mob.adjustElevation(world);
            mob.wrap(world);

            boolean water = mob.inWater(world);
            mob.setInWater(water);

            if (!water) {
                mob.updatePath(world);
            }
        }
        mobs.removeIf(Mob::isDead);
    }

    public void spawnHuman(World world) {
        for (int i = 0; i < world.getWidth() * 5; i++) {
            float x, y;

            do {
                x = (float) (Math.random() * world.getWidth());
                y = (float) (Math.random() * world.getHeight());
            }
            while (world.inWater((int)x,(int)y, world.getElevation((int)x, (int)y))); //keeps running until the tile selected is not water

            Human human = new Human(x, y, 0, new Random());
            addMob(human);
        }
    }

    public void clearMobs(){
        mobs.clear();
    }

    public void render(FlatRenderer flats, Shader flatShader, Camera camera, World world) {
        if (mobs.isEmpty()) return;

        // Camera view bounds (slightly padded like World.render)
        int minX = (int) (camera.getPosition().x - camera.getViewWidth()) - 8;
        int maxX = (int) (camera.getPosition().x + camera.getViewWidth()) + 8;
        int minY = (int) (camera.getPosition().y - camera.getViewWidth()) - 8;
        int maxY = (int) (camera.getPosition().y + camera.getViewWidth()) + 8;

        int width = world.getWidth();
        int height = world.getHeight();

        boolean wrapLeft = minX < 0;
        boolean wrapRight = maxX >= width;
        boolean wrapDown = minY < 0;
        boolean wrapUp = maxY >= height;

        List<FlatInstance> mobInstances = new ArrayList<>();

        // Center world
        for (Mob m : mobs) {
            if (m.x >= minX && m.x <= maxX && m.y >= minY && m.y <= maxY) {
                mobInstances.add(m);
            }
        }
        // Horizontal wraps
        if (wrapLeft) {
            for (Mob m : mobs) {
                float fx = m.x - width;
                if (fx >= minX && fx <= maxX && m.y >= minY && m.y <= maxY) {
                    mobInstances.add(new FlatInstance(fx, m.y, m.z, m.texture));
                }
            }
        }
        if (wrapRight) {
            for (Mob m : mobs) {
                float fx = m.x + width;
                if (fx >= minX && fx <= maxX && m.y >= minY && m.y <= maxY) {
                    mobInstances.add(new FlatInstance(fx, m.y, m.z, m.texture));
                }
            }
        }
        // Vertical wraps
        if (wrapDown) {
            for (Mob m : mobs) {
                float fy = m.y - height;
                if (m.x >= minX && m.x <= maxX && fy >= minY && fy <= maxY) {
                    mobInstances.add(new FlatInstance(m.x, fy, m.z, m.texture));
                }
            }
        }
        if (wrapUp) {
            for (Mob m : mobs) {
                float fy = m.y + height;
                if (m.x >= minX && m.x <= maxX && fy >= minY && fy <= maxY) {
                    mobInstances.add(new FlatInstance(m.x, fy, m.z, m.texture));
                }
            }
        }
        // Corner wraps
        if (wrapLeft && wrapDown) {
            for (Mob m : mobs) {
                float fx = m.x - width, fy = m.y - height;
                if (fx >= minX && fx <= maxX && fy >= minY && fy <= maxY) {
                    mobInstances.add(new FlatInstance(fx, fy, m.z, m.texture));
                }
            }
        }
        if (wrapLeft && wrapUp) {
            for (Mob m : mobs) {
                float fx = m.x - width, fy = m.y + height;
                if (fx >= minX && fx <= maxX && fy >= minY && fy <= maxY) {
                    mobInstances.add(new FlatInstance(fx, fy, m.z, m.texture));
                }
            }
        }
        if (wrapRight && wrapDown) {
            for (Mob m : mobs) {
                float fx = m.x + width, fy = m.y - height;
                if (fx >= minX && fx <= maxX && fy >= minY && fy <= maxY) {
                    mobInstances.add(new FlatInstance(fx, fy, m.z, m.texture));
                }
            }
        }
        if (wrapRight && wrapUp) {
            for (Mob m : mobs) {
                float fx = m.x + width, fy = m.y + height;
                if (fx >= minX && fx <= maxX && fy >= minY && fy <= maxY) {
                    mobInstances.add(new FlatInstance(fx, fy, m.z, m.texture));
                }
            }
        }

        // Render them in a batch
        flats.renderBatch(mobInstances, flatShader, camera);
    }
}
