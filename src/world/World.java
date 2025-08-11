package world;

import org.joml.Vector2f;
import org.joml.Vector4f;
import render.*;
import io.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class World {
    private byte[] tiles;
    private int width;
    private int height;
    private int depth;
    private int scale;

    // World transformation matrix
    private Matrix4f worldTransform;

    public World(int worldWidth, int worldHeight) {
        this.width = worldWidth;
        this.height = worldHeight;
        this.depth = 1;
        this.scale = 1;

        tiles = new byte[width * height * depth];
        worldTransform = new Matrix4f().identity();

        // Fill with some test tiles
    }

    private int index(int x, int y, int z) {
        return x + y * width + z * width * height;
    }

    public Tile getTiles(int x, int y, int z) {
        try {
            return Tile.tiles[tiles[index(x, y, z)]];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public void render(TileRenderer renderer, Shader shader, Camera camera, Window window) {

        int posX = ((int)camera.getPosition().x + (window.getWidth()/2)) / (scale * 2);
        int posY = ((int)camera.getPosition().y - (window.getHeight()/2)) / (scale * 2);

        // Calculate world transform
        worldTransform.identity()
                .scale(scale);

        for (int z = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Tile t = getTiles(x, y, z);
                    if (t != null) {
                        renderer.renderTile(t, x, y, z, shader, worldTransform, camera);
                    }
                }
            }
        }
    }

    public void setTile(Tile tile, int x, int y, int z) {
        if (x >= 0 && x < width && y >= 0 && y < height && z >= 0 && z < depth) {
            tiles[index(x, y, z)] = tile.getId();
        }
    }

    public Matrix4f getWorldTransform() {
        return worldTransform;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}