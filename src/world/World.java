package world;

import entities.billboard.base.BillboardObject;
import render.*;
import io.*;
import entities.*;

import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class World {
    private List<BillboardObject> billboards = new ArrayList<>();
    private Model quadModel;
    private byte[] tiles;
    private final NoiseGenerator generator;
    private int width;
    private int height;
    private int depth;


    // World transformation matrix
    private Matrix4f worldTransform;

    public World(NoiseGenerator generator) {
        this.generator = generator;
        this.width = generator.getWidth();
        this.height = generator.getHeight();
        this.depth = generator.getDepth();

        tiles = new byte[width * height * depth];
        generateWorld();
        billboards.add(new BillboardObject(0, 0, 0, "tree.png", quadModel));
    }

    public void generateWorld() {
        float[][] noiseMap = generator.generateNoise();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float noiseValue = noiseMap[x][y];

                int depthValue;
                if (noiseValue < 0.05f) {
                    depthValue = 1;;
                } else if (noiseValue < 0.08f) {
                    depthValue = 2;
                } else if (noiseValue < 0.2f) {
                    depthValue = 3;
                } else if (noiseValue < 0.25f) {
                    depthValue = 4;
                } else if (noiseValue < 0.3f) {
                    depthValue = 5;
                } else if (noiseValue < 0.35f) {
                    depthValue = 6;
                } else if (noiseValue < 0.4f) {
                    depthValue = 7;
                } else if (noiseValue < 0.45f) {
                    depthValue = 8;
                } else if (noiseValue < 0.5f) {
                    depthValue = 9;
                } else if (noiseValue < 0.55f) {
                    depthValue = 10;
                } else if (noiseValue < 0.6f) {
                    depthValue = 11;
                } else if (noiseValue < 0.65f) {
                    depthValue = 12;
                } else if (noiseValue < 0.7f) {
                    depthValue = 13;
                } else if (noiseValue < 0.75f) {
                    depthValue = 14;
                } else if (noiseValue < 0.8f) {
                    depthValue = 15;
                } else if (noiseValue < 0.85f) {
                    depthValue = 16;
                } else if (noiseValue < 0.9f) {
                    depthValue = 17;
                } else if (noiseValue < 0.95f) {
                    depthValue = 18;
                } else {
                    depthValue = 20;
                }

                for (int z = 0; z < depth; z++) {
                    byte tileId;
                    if (z >= depthValue) {
                        tileId = -1;
                    }
                    else
                    {
                        if (noiseValue < 0.05f) {
                            tileId = Tile.water.getId();
                        } else if (noiseValue < 0.08f) {
                            tileId = Tile.sand.getId();
                        } else if (noiseValue < 0.15f) {
                            tileId = Tile.grass.getId();
                        } else if (noiseValue < 0.2f) {
                            tileId = Tile.forest.getId();
                        } else if (noiseValue < 0.6f) {
                            tileId = Tile.deepforest.getId();
                        } else if (noiseValue < 0.7f) {
                            tileId = Tile.stone.getId();
                        } else {
                            tileId = Tile.stone.getId();;
                        }
                    }
                    tiles[index(x, y, z)] = tileId;
                }
            }
        }
    }

    public void render(TileRenderer renderer, Shader shader, Camera camera, Window window) {
        int minX = Math.max(0, (int) (camera.getPosition().x - camera.getViewWidth())) - 7;
        int maxX = Math.min(width - 1, (int) (camera.getPosition().x + camera.getViewWidth())) + 7;
        int minY = Math.max(0, (int) (camera.getPosition().y - camera.getViewWidth())) - 7;
        int maxY = Math.min(height - 1, (int) (camera.getPosition().y + camera.getViewWidth())) + 7;

        List<TileInstance> visibleTiles = new ArrayList<>();

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = 0; z < depth; z++) {
                    int tileIndex = index(x, y, z);
                    if (tileIndex < 0 || tileIndex >= tiles.length || tiles[tileIndex] == -1) continue;

                    Tile t = getTiles(x, y, z);
                    if (t != null) {
                        boolean isLit = (z == depth - 1) || tiles[index(x, y, z + 1)] == -1;
                        float brightness = isLit ? 1.0f : 0.3f; // tile shading
                        visibleTiles.add(new TileInstance(x, y, z, brightness, t.getTexture()));
                    }
                }
            }
        // Render all tiles via TileRenderer
        renderer.renderBatch(visibleTiles, shader, camera);
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    public int getDepth() {
        return depth;
    }
}