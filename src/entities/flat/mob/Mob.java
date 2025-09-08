package entities.flat.mob;

import entities.flat.base.FlatInstance;
import world.*;

public class Mob extends FlatInstance{
    protected boolean inWater;
    protected boolean unlockZ = false;

    public Mob(float x, float y, float z, String texture) {
        super(x, y, z, texture);
    }

    public void update(double deltaTime) {}

    public void adjustElevation(World world){
        if (!unlockZ) {
            this.z = world.getElevation((int)this.x,(int)this.y)* 0.1f;
            System.out.println(z);
        }
    }

    public void wrap(World world) {
        this.x = (this.x % world.getWidth() + world.getWidth()) % world.getWidth();
        this.y = (this.y % world.getHeight() + world.getHeight()) % world.getHeight();
    }

    public boolean inWater(World world){
        return world.inWater((int)this.x,(int)this.y,(int)this.z);
    }

    public void setInWater(boolean inWater) {
        this.inWater = inWater;
    }

}