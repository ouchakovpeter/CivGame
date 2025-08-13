package world;

import render.*;
import io.*;
import org.joml.Matrix4f;

public class World {
    private byte[] tiles;
    private final WorldGenerator generator;
    private int width;
    private int height;
    private int depth;

    // World transformation matrix
    private Matrix4f worldTransform;

    public World(WorldGenerator generator) {
        this.generator = generator;
        this.width = generator.getWidth();
        this.height = generator.getHeight();
        this.depth = generator.getDepth();

        tiles = new byte[width * height * depth];
        //generator.generateNoise();
        assignTile();
        // Fill with some test tiles
    }

    public void assignTile() {
        float[][][] noiseMap = generator.generateNoise();
        for (int z = 0; z < depth; z++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    float noiseValue = noiseMap[x][y][z];
                    //float n = (float)Math.random();

                    byte tileId;
                    if (noiseValue < 0.05f) {
                        tileId = Tile.water.getId();
                    } else if (noiseValue < 0.2f) {
                        tileId = Tile.grass.getId();
                    } else {
                        tileId = Tile.forest.getId();
                    }

                    tiles[index(x, y, z)] = tileId;
                }
            }
        }
    }

    public void render(TileRenderer renderer, Shader shader, Camera camera, Window window) {

        int minX = (Math.max(0, (int)(camera.getPosition().x - camera.getViewWidth())))-7;
        int maxX = (Math.min(width - 1, (int)(camera.getPosition().x +  camera.getViewWidth())))+7;
        int minY = (Math.max(0, (int)(camera.getPosition().y -  camera.getViewWidth())))-7;
        int maxY = (Math.min(height - 1, (int)(camera.getPosition().y +  camera.getViewWidth())))+7;

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = 0; z < depth; z ++) {
                    int tileIndex = index(x, y, z);
                    if (tileIndex >= 0 && tileIndex < tiles.length && tiles[tileIndex] != -1) {
                        Tile t = getTiles(x, y, z);
                        if (t != null) {
                            renderer.renderTile(t, x, y, z, shader, camera);
                        }
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

//    public void setTile(Tile tile, int x, int y, int z) {
//        if (x >= 0 && x < width && y >= 0 && y < height && z >= 0 && z < depth) {
//            tiles[index(x, y, z)] = tile.getId();
//        }
//    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}