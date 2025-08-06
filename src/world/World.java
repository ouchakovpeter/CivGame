package world;

import render.*;
import io.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.swing.text.html.StyleSheet;

public class World {
    private byte[] tiles;
    private int width;
    private int height;

    private int depth;

    private Matrix4f world;

    private int scale;


    public World(int worldWidth, int worldHeight){
        width = 640;
        height = 480;
        depth = 1;
        scale = 16; //pixel size i think

        tiles = new byte[width * height * depth];

        world = new Matrix4f().setTranslation(new Vector3f(0));
        world.scale(scale);
    }

    private int index(int x, int y, int z) {
        return x + y * width + z * width * height;
    }

    public void render(TileRenderer render, Shader shader, Camera camera, Window window) {

        //finds the amount of tiles on the screen and then only renders that amount
        int tilesAcross = (int) Math.ceil(window.getWidth() / (scale * 2)) + 2;
        int tilesDown = (int) Math.ceil(window.getHeight() / (scale * 2)) + 2;

        int posX = ((int) camera.getPosition().x + (window.getWidth() / 2)) / (int) (scale * 2);
        int posY = ((int) camera.getPosition().y - (window.getHeight() / 2)) / (int) (scale * 2);

        for (int z = 0; z < 1; z++) {
            for (int i = 0; i < tilesAcross; i++) {
                for (int j = 0; j < tilesDown; j++) {
                    Tile t = getTiles(i - posX, j + posY, z);
                    if (t != null) {
                        render.renderTile(t, i - posX, -j - posY, z, shader, world, camera);
                    }
                }
            }
        }
    }
    //restricting the world
    public void correctCamera(Camera camera, Window window){
        Vector3f pos = camera.getPosition();

        float effectiveScale = scale;

        int w = -width * (int)effectiveScale * 2;
        int h = height * (int)effectiveScale * 2;

//        //right
//        if(pos.x>-(window.getWidth()/2)+scale){
//            pos.x = -(window.getWidth()/2)+scale;
//        }
//
//        //left
//        if(pos.x < w + (window.getWidth()/2)+scale){
//            pos.x = w + (window.getWidth()/2)+scale;
//        }
//
//        //top
//        if(pos.y < (window.getHeight()/2) - scale){
//            pos.y = (window.getHeight()/2) - scale;
//        }
//
//        //bottom
//        if(pos.y > h - (window.getHeight()/2)-scale){
//            pos.y = h - (window.getHeight()/2)-scale;
//        }
    }

    public void setTile(Tile tile, int x, int y, int z) {
        tiles[index(x, y, z)] = tile.getId();
    }

    public Tile getTiles(int x, int y, int z) {
        try {
            return Tile.tiles[tiles[index(x, y, z)]];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

}
