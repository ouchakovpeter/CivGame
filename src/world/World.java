package world;

import entities.flat.decor.Boulder;
import entities.flat.decor.Fern;
import render.*;
import io.*;
import entities.flat.base.*;
import entities.flat.trees.*;

import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.glDepthMask;

public class World {
    private List<FlatInstance> flats = new ArrayList<>();
    private byte[] tiles;
    private final NoiseGenerator generator;
    private int width;
    private int height;
    private int depth;

    private float treeVariableTest = 0;

    private Matrix4f worldTransform;

    public World(NoiseGenerator generator) {
        this.generator = generator;
        this.width = generator.getWidth();
        this.height = generator.getHeight();
        this.depth = generator.getDepth();

        tiles = new byte[width * height * depth];
        generateWorld();
        generateDecoration();
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

    public void generateDecoration(){
        flats.clear();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile topTile = null;
                int topZ = -1;
                for (int z = depth - 1; z >= 0; z--) {
                    Tile t = getTiles(x, y, z);
                    if (t != null && t != Tile.water) {
                        topTile = t;
                        topZ = z;
                        break;
                    }
                }
                if (topTile == Tile.grass || topTile == Tile.forest || topTile == Tile.deepforest) {
                    int decorCount = 1 + (int)(Math.random() * 10);

                    for (int d = 0; d < decorCount; d++) {
                        float offsetX = (float)(Math.random() * 0.9f + 0.1f);
                        float offsetY = (float)(Math.random() * 0.9f + 0.1f);

                        float decorX = x + offsetX;
                        float decorY = y + offsetY;
                        float decorZ = topZ * 0.1f;

                        // Randomly choose decoration type
                        // Example weights
                        int spruceWeight = 75;
                        int fernWeight   = 20;
                        int boulderWeight   = 1;

                        int totalWeight = spruceWeight + fernWeight + boulderWeight;

                        // Pick a random number up to total weight
                        int roll = (int)(Math.random() * totalWeight);

                        if (roll < spruceWeight) {
                            flats.add(new Spruce(decorX, decorY, decorZ));
                        } else if (roll < spruceWeight + fernWeight) {
                            flats.add(new Fern(decorX, decorY, decorZ));
                        } else {
                            flats.add(new Boulder(decorX, decorY, decorZ));
                        }
                    }
                }
                if(topTile == Tile.sand){
                    int decorCount = 1 + (int)(Math.random() * 4);

                    for (int d = 0; d < decorCount; d++) {
                        float offsetX = (float) (Math.random() * 0.9f + 0.1f);
                        float offsetY = (float) (Math.random() * 0.9f + 0.1f);

                        float decorX = x + offsetX;
                        float decorY = y + offsetY;
                        float decorZ = topZ * 0.1f;

                        int boulderWeight = 10;

                        int totalWeight = 100;

                        int roll = (int)(Math.random() * totalWeight);

                        if (roll < boulderWeight) {
                            flats.add(new Boulder(decorX, decorY, decorZ));
                        }
                    }
                }
            }
        }
    }

    public void render(TileRenderer renderer, FlatRenderer flatRenderer, Shader shader, Shader flatShader, Camera camera, Window window) {
        int minX = (int) (camera.getPosition().x - camera.getViewWidth()) - 7;
        int maxX = (int) (camera.getPosition().x + camera.getViewWidth()) + 7;
        int minY = (int) (camera.getPosition().y - camera.getViewWidth()) - 7;
        int maxY = (int) (camera.getPosition().y + camera.getViewWidth()) + 7;

        List<TileInstance> visibleTiles = new ArrayList<>();
        List<FlatInstance> visibleFlats = new ArrayList<>();

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {

                int wx = (x % width + width) % width;
                int wy = (y % height + height) % height;

                for (int z = 0; z < depth; z++) {
                    int tileIndex = index(wx, wy, z);

                    if (tiles[tileIndex] == -1) continue;

                    Tile t = getTiles(wx, wy, z);

                    if (t != null) {
                        boolean isLit = (z == depth - 1) || tiles[index(wx, wy, z + 1)] == -1;
                        float brightness = isLit ? 1.0f : 0.3f;
                        visibleTiles.add(new TileInstance(x, y, z, brightness, t.getTexture()));
                    }
                }
            }
        }
        for (FlatInstance flat : flats) {
            if (isFlatVisible(flat, camera, minX, maxX, minY, maxY)) {
                visibleFlats.add(flat);
            }
        }
        // Render all tiles via TileRenderer
        renderer.renderBatch(visibleTiles, shader, camera);
        flatRenderer.renderBatch(visibleFlats, flatShader, camera);
    }
    private boolean isFlatVisible(FlatInstance flat, Camera camera,
                                  int minX, int maxX, int minY, int maxY) {
        return flat.x >= minX && flat.x <= maxX &&
                flat.y >= minY && flat.y <= maxY;
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