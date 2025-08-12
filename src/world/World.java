package world;

import org.joml.Vector2f;
import org.joml.Vector4f;
import render.*;
import io.*;
import util.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class World {
    private byte[] tiles;
    private final WorldGenerator generator;
    private int width;
    private int height;
    private int depth;
    private int scale;

    // World transformation matrix
    private Matrix4f worldTransform;

    public World(WorldGenerator generator) {
        this.generator = generator;
        this.width = generator.getWidth();
        this.height = generator.getHeight();
        this.depth = generator.getDepth();
        this.scale = 1;

        tiles = new byte[width * height * depth];
        generator.generateNoise();
        // Fill with some test tiles
    }

    public void render(TileRenderer renderer, Shader shader, Camera camera, Window window) {

        float viewWidth = 20.0f;  // Should match the value in Camera's updateProjection
        int minX = (Math.max(0, (int)(camera.getPosition().x - viewWidth)))-7;
        int maxX = (Math.min(width - 1, (int)(camera.getPosition().x + viewWidth)))+7;
        int minY = (Math.max(0, (int)(camera.getPosition().y - viewWidth)))-7;
        int maxY = (Math.min(height - 1, (int)(camera.getPosition().y + viewWidth)))+7;

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = 0; z < depth; z ++) {
                    Tile t = getTiles(x, y, z);
                    if (t != null) {
                        renderer.renderTile(t, x, y, z, shader, camera);
                    }
                }
            }
        }
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

    public void setTile(Tile tile, int x, int y, int z) {
        if (x >= 0 && x < width && y >= 0 && y < height && z >= 0 && z < depth) {
            tiles[index(x, y, z)] = tile.getId();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}