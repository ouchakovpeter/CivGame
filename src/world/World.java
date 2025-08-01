package world;

import render.*;
import io.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.swing.text.html.StyleSheet;

public class World {
    private final int view = 24;
    private byte[] tiles;
    private int width;
    private int height;

    private Matrix4f world;

    private int scale;

    public World(){
        width = 640;
        height = 480;
        scale = 16;

        tiles = new byte[width * height];

        world = new Matrix4f().setTranslation(new Vector3f(0));
        world.scale(scale);
    }
    public void render(TileRenderer render, Shader shader, Camera camera, Window window) {

        int posX = ((int)camera.getPosition().x + (window.getWidth()/2)) / (scale * 2);
        int posY = ((int)camera.getPosition().y - (window.getHeight()/2)) / (scale * 2);

        for (int i = 0; i < view; i++) {
            for (int j = 0; j < view; j++) {
                Tile t = getTiles(i-posX, j+posY);
                if(t != null) {
                    render.renderTile(t, i-posX, -j-posY, shader, world, camera);
                }
            }
        }
    }
    //restricting the world
    public void correctCamera(Camera camera, Window window){
        Vector3f pos = camera.getPosition();

        int w = -width * scale * 2;
        int h = height * scale * 2;

        //right
        if(pos.x>-(window.getWidth()/2)+scale){
            pos.x = -(window.getWidth()/2)+scale;
        }

        //left
        if(pos.x < w + (window.getWidth()/2)+scale){
            pos.x = w + (window.getWidth()/2)+scale;
        }

        //top
        if(pos.y < (window.getHeight()/2) - scale){
            pos.y = (window.getHeight()/2) - scale;
        }

        //bottom
        if(pos.y > h - (window.getHeight()/2)-scale){
            pos.y = h - (window.getHeight()/2)-scale;
        }
    }

    public void setTile(Tile tile, int x, int y){
        tiles[x + y * width] = tile.getId();

    }

    public Tile getTiles(int x, int y) {
        try{
            return Tile.tiles[tiles[x + y * width]];
        }
        catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }
}
