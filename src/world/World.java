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
        generateWorld();

        // Fill with some test tiles
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
                } else if (noiseValue < 0.3f) {
                    depthValue = 4;
                } else if (noiseValue < 0.4f) {
                    depthValue = 5;
                } else if (noiseValue < 0.5f) {
                    depthValue = 6;
                } else if (noiseValue < 0.6f) {
                    depthValue = 7;
                } else if (noiseValue < 0.65f) {
                    depthValue = 8;
                } else if (noiseValue < 0.7f) {
                    depthValue = 9;
                } else {
                    depthValue = 10;
                }

                for (int z = 0; z < depth; z++) {
                    byte tileId;
                    if (z >= depthValue) {
                        tileId = -1;
                    }
                    else
                    {
                        if (noiseValue < 0.05f) {
                            tileId = Tile.water.getId();;
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
                            tileId = -1;
                        }
                    }
                    tiles[index(x, y, z)] = tileId;
                }
            }
        }
    }

    private boolean isTileVisible(int x, int y, int z) {
        // Directly above empty? → visible
        if (z == depth - 1 || tiles[index(x, y, z + 1)] == -1) {
            return true;
        }

        // Check neighbors at same height
        int[][] offsets = {
                {1, 0},  // east
                {-1, 0}, // west
                {0, 1},  // north
                {0, -1}  // south
        };

        for (int[] off : offsets) {
            int nx = x + off[0];
            int ny = y + off[1];
            if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                if (tiles[index(nx, ny, z)] == -1) {
                    return true; // neighbor gap → visible
                }
            }
        }

        return false; // completely surrounded
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

                    if (tileIndex < 0 || tileIndex >= tiles.length || tiles[tileIndex] == -1) {
                        continue;
                    }

                    if (tileIndex >= 0 && tileIndex < tiles.length && tiles[tileIndex] != -1) {
                        Tile t = getTiles(x, y, z);
                        if (t != null) {
                            boolean isLit = (z == depth - 1) || tiles[index(x, y, z + 1)] == -1;
                            float brightness = isLit ? 1.0f : 0.3f; //shadow intensity
                            renderer.renderTile(t, x, y, z, shader, camera, brightness);
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}