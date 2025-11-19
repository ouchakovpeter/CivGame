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

    public void update(double deltaTime) {
    }

    public void adjustElevation(World world){
        if (!unlockZ) {
            this.z = world.getElevation((int)this.x,(int)this.y)* 0.1f;
        }
    }

    public void wrap(World world) {
        this.x = (this.x % world.getWidth() + world.getWidth()) % world.getWidth();
        this.y = (this.y % world.getHeight() + world.getHeight()) % world.getHeight();
    }

    //returns all tiles around player
    public List<Tile> detectTiles(World world) {
        List<Tile> tiles = new ArrayList<>();

            for(int i = 0; i <= 2; i++)
            {
                for(int j = 0; j <= 2; j++)
                {
                    int tx = Math.floorMod((int)this.x + i, world.getWidth());
                    int ty = Math.floorMod((int)this.y + j, world.getHeight());

                    int tz = world.getElevation(tx, ty);
                    Tile t = world.getTile(tx,ty,tz);
                    if (t != null)
                    {
                        tiles.add(t);
                    }
                }
            }
        return tiles;
    }

    public List<FlatInstance> detectResources(World world) {

        int trees = 0;
        int rocks = 0;

        List<FlatInstance> resources = new ArrayList<>();


        return resources;
    }



    //Update Path
    public void updatePath(World world) {
        if(!inWater) {
            int tileX = ((int) this.x);
            int tileY = ((int) this.y);

            //pixel pos
            float xFrac = this.x - tileX;
            float yFrac = this.y - tileY;

            int pixelX = (int) (xFrac * 16.0f);
            int pixelY = (int) (yFrac * 16.0f);

            if (pixelX < 0) pixelX = 0;
            else if (pixelX > 15) pixelX = 15;

            if (pixelY < 0) pixelY = 0;
            else if (pixelY > 15) pixelY = 15;

            byte damage = world.getPathDamage(tileX, tileY, pixelX, pixelY);

            int damageMax = 3;

            if (damage < damageMax) {
                damage++;
                world.setPathDamage(tileX, tileY, pixelX, pixelY, damage);
            } else {
                world.setPathPixel(tileX, tileY, pixelX, pixelY, true);
            }
        }
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