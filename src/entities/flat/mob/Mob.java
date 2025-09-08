package entities.flat.mob;

import entities.flat.base.FlatInstance;
import world.*;

public class Mob extends FlatInstance{
    protected float vx, vy;   // velocity

    public Mob(float x, float y, float z, String texture) {
        super(x, y, z, texture);
    }

    public void update(double deltaTime) {
        this.x += vx * deltaTime;
        this.y += vy * deltaTime;
        System.out.println("Mob moved to: " + x + ", " + y + " (vx=" + vx + ", vy=" + vy + ")");
    }

    public void wrap(World world) {
        this.x = (this.x % world.getWidth() + world.getWidth()) % world.getWidth();
        this.y = (this.y % world.getHeight() + world.getHeight()) % world.getHeight();
    }

    public void setVelocity(float vx, float vy) {
        this.vx = vx;
        this.vy = vy;
    }
}