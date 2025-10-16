package entities.flat.mob;

import entities.flat.base.FlatInstance;
import world.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;
import static java.lang.Math.PI;

public class Mob extends FlatInstance{
    protected boolean inWater;
    protected boolean unlockZ = false; //if z should change or not
    protected boolean dead = false;
    public float direction;
    public float speed;
    public float health;
    public float viewDistance;

    public Mob(float x, float y, float z, String texture) {
        super(x, y, z, texture);
    }

    public void update(double deltaTime) {}

    public void adjustElevation(World world){
        if (!unlockZ) {
            this.z = world.getElevation((int)this.x,(int)this.y)* 0.1f;
        }
    }

    public void wrap(World world) {
        this.x = (this.x % world.getWidth() + world.getWidth()) % world.getWidth();
        this.y = (this.y % world.getHeight() + world.getHeight()) % world.getHeight();
    }

    // Returns the first tile ahead (legacy convenience)
    public Tile detectTile(World world){
        List<Tile> tiles = detectTiles(world);
        return tiles.isEmpty() ? null : tiles.get(0);
    }

    // New: returns all tiles along the look direction up to viewDistance (1 tile increments)
    public List<Tile> detectTiles(World world){
        List<Tile> tiles = new ArrayList<>();
        int steps = (int) ceil(max(0f, viewDistance));
        double rad = (PI / 180.0) * direction;
        double cx = cos(rad);
        double cy = sin(rad);

        for (int i = 1; i <= steps; i++) {
            int tx = Math.floorMod((int) Math.round(this.x + cx * i), world.getWidth());
            int ty = Math.floorMod((int) Math.round(this.y + cy * i), world.getHeight());
            int tz = world.getElevation(tx, ty);
            Tile t = world.getTile(tx, ty, tz);
            if (t != null) {
                tiles.add(t);
            }
        }
        return tiles;
    }

    public boolean inWater(World world){
        int ez = world.getElevation((int)this.x, (int)this.y);
        return world.inWater((int)this.x,(int)this.y, ez);
    }

    public void setInWater(boolean inWater) {
        this.inWater = inWater;
    }

    public void kill() {
        dead = true;
    }

    public boolean isDead() {
        return dead;
    }

    public void target(float targetPosX, float targetPosY){
        float dx = targetPosX - x;
        float dy = targetPosY - y;
        direction = (float) Math.toDegrees(Math.atan2(dy, dx));
    }
}