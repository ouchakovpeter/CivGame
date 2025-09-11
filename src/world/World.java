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
    // Chunked storage for flats to keep rendering O(visible) instead of O(total)
    private static final int CHUNK_SIZE = 64; // tiles per chunk side (tweak to taste)
    private int chunkCols, chunkRows;
    private List<FlatInstance>[][] flatChunks;
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
        // Init chunk grid
        this.chunkCols = Math.max(1, (int)Math.ceil(width / (double)CHUNK_SIZE));
        this.chunkRows = Math.max(1, (int)Math.ceil(height / (double)CHUNK_SIZE));
        //noinspection unchecked
        this.flatChunks = new ArrayList[chunkCols][chunkRows];
        for (int cx = 0; cx < chunkCols; cx++) {
            for (int cy = 0; cy < chunkRows; cy++) {
                flatChunks[cx][cy] = new ArrayList<>();
            }
        }
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

    private void clearFlatChunks() {
        for (int cx = 0; cx < chunkCols; cx++) {
            for (int cy = 0; cy < chunkRows; cy++) {
                flatChunks[cx][cy].clear();
            }
        }
    }

    private void addFlatToChunks(FlatInstance f) {
        int cx = Math.floorMod((int) Math.floor(f.x), width) / CHUNK_SIZE;
        int cy = Math.floorMod((int) Math.floor(f.y), height) / CHUNK_SIZE;
        flatChunks[cx][cy].add(f);
    }

    public void generateDecoration(){
        flats.clear();
        clearFlatChunks();
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

                        // Randomly choose decoration type by weight
                        int spruceWeight = 75;
                        int fernWeight   = 20;
                        int boulderWeight   = 1;

                        int totalWeight = spruceWeight + fernWeight + boulderWeight;

                        // Pick a random number up to total weight
                        int roll = (int)(Math.random() * totalWeight);

                        if (roll < spruceWeight) {
                            FlatInstance f = new Spruce(decorX, decorY, decorZ);
                            flats.add(f);
                            addFlatToChunks(f);
                        } else if (roll < spruceWeight + fernWeight) {
                            FlatInstance f = new Fern(decorX, decorY, decorZ);
                            flats.add(f);
                            addFlatToChunks(f);
                        } else {
                            FlatInstance f = new Boulder(decorX, decorY, decorZ);
                            flats.add(f);
                            addFlatToChunks(f);
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
                            FlatInstance f = new Boulder(decorX, decorY, decorZ);
                            flats.add(f);
                            addFlatToChunks(f);
                        }
                    }
                }
            }
        }
    }

    public void render(TileRenderer renderer, FlatRenderer flatRenderer, Shader shader, Shader flatShader, Camera camera, Window window) {
        int minX = (int) (camera.getPosition().x - camera.getViewWidth()) - 8;
        int maxX = (int) (camera.getPosition().x + camera.getViewWidth()) + 8;
        int minY = (int) (camera.getPosition().y - camera.getViewWidth()) - 8;
        int maxY = (int) (camera.getPosition().y + camera.getViewWidth()) + 8;

        List<TileInstance> visibleTiles = new ArrayList<>();
        List<FlatInstance> visibleFlats = new ArrayList<>();

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {

                int wx = (x % width + width) % width;
                int wy = (y % height + height) % height;

                // Find the top-most solid tile at (wx, wy)
                int zTop = -1;
                Tile topTile = null;
                for (int z = depth - 1; z >= 0; z--) {
                    int tileIndex = index(wx, wy, z);
                    if (tiles[tileIndex] == -1) continue;
                    Tile t = getTiles(wx, wy, z);

                    if (t != null) {
                        zTop = z;
                        topTile = t;
                        break;
                    }
                }

                if(zTop != -1 && topTile != null){
                    visibleTiles.add(new TileInstance(x, y, zTop, 1, topTile.getTexture()));

                    int[][] dirs = new int[][] { {1,0}, {-1,0}, {0,1}, {0,-1} };
                    for (int[] d : dirs) {
                        int nx = x + d[0];
                        int ny = y + d[1];
                        int nwx = (nx % width + width) % width;
                        int nwy = (ny % height + height) % height;
                        int nTop = getElevation(nwx, nwy);

                        if (nTop < zTop) {
                            int sideDepth = zTop - nTop;
                            for (int k = 1; k <= sideDepth; k++) {
                                int zSide = zTop - k;
                                if (zSide < 0) break;
                                Tile sideTile = getTiles(wx, wy, zSide);
                                if (sideTile == null) break;

                                float tileBrightness = 0.4f;

                                visibleTiles.add(new TileInstance(x, y, zSide, tileBrightness, sideTile.getTexture()));
                            }
                        }
                    }
                }
            }
        }
        // Collect flats only from chunks overlapping the camera view (with wrapping when needed)
        boolean wrapLeft = minX < 0;
        boolean wrapRight = maxX >= width;
        boolean wrapDown = minY < 0;
        boolean wrapUp = maxY >= height;

        int startChunkX = Math.floorDiv(Math.max(minX, 0), CHUNK_SIZE);
        int endChunkX = Math.floorDiv(Math.min(maxX, width - 1), CHUNK_SIZE);
        int startChunkY = Math.floorDiv(Math.max(minY, 0), CHUNK_SIZE);
        int endChunkY = Math.floorDiv(Math.min(maxY, height - 1), CHUNK_SIZE);

        // Primary (non-wrapped) range
        for (int cx = startChunkX; cx <= endChunkX; cx++) {
            for (int cy = startChunkY; cy <= endChunkY; cy++) {
                for (FlatInstance f : flatChunks[cx][cy]) {
                    if (f.x >= minX && f.x <= maxX && f.y >= minY && f.y <= maxY) {
                        visibleFlats.add(f);
                    }
                }
            }
        }
        // Horizontal wrap range
        if (wrapLeft) {
            int cx = chunkCols - 1;
            for (int cy = startChunkY; cy <= endChunkY; cy++) {
                for (FlatInstance f : flatChunks[cx][cy]) {
                    float fx = f.x - width;
                    if (fx >= minX && fx <= maxX && f.y >= minY && f.y <= maxY) {
                        visibleFlats.add(new FlatInstance(fx, f.y, f.z, f.texture));
                    }
                }
            }
        }
        if (wrapRight) {
            int cx = 0;
            for (int cy = startChunkY; cy <= endChunkY; cy++) {
                for (FlatInstance f : flatChunks[cx][cy]) {
                    float fx = f.x + width;
                    if (fx >= minX && fx <= maxX && f.y >= minY && f.y <= maxY) {
                        visibleFlats.add(new FlatInstance(fx, f.y, f.z, f.texture));
                    }
                }
            }
        }
        // Vertical wrap range
        if (wrapDown) {
            int cy = chunkRows - 1;
            for (int cx = startChunkX; cx <= endChunkX; cx++) {
                for (FlatInstance f : flatChunks[cx][cy]) {
                    float fy = f.y - height;
                    if (f.x >= minX && f.x <= maxX && fy >= minY && fy <= maxY) {
                        visibleFlats.add(new FlatInstance(f.x, fy, f.z, f.texture));
                    }
                }
            }
        }
        if (wrapUp) {
            int cy = 0;
            for (int cx = startChunkX; cx <= endChunkX; cx++) {
                for (FlatInstance f : flatChunks[cx][cy]) {
                    float fy = f.y + height;
                    if (f.x >= minX && f.x <= maxX && fy >= minY && fy <= maxY) {
                        visibleFlats.add(new FlatInstance(f.x, fy, f.z, f.texture));
                    }
                }
            }
        }
        // Corner wraps (if both axes wrap)
        if (wrapLeft && wrapDown) {
            int cx = chunkCols - 1, cy = chunkRows - 1;
            for (FlatInstance f : flatChunks[cx][cy]) {
                float fx = f.x - width, fy = f.y - height;
                if (fx >= minX && fx <= maxX && fy >= minY && fy <= maxY) {
                    visibleFlats.add(new FlatInstance(fx, fy, f.z, f.texture));
                }
            }
        }
        if (wrapLeft && wrapUp) {
            int cx = chunkCols - 1, cy = 0;
            for (FlatInstance f : flatChunks[cx][cy]) {
                float fx = f.x - width, fy = f.y + height;
                if (fx >= minX && fx <= maxX && fy >= minY && fy <= maxY) {
                    visibleFlats.add(new FlatInstance(fx, fy, f.z, f.texture));
                }
            }
        }
        if (wrapRight && wrapDown) {
            int cx = 0, cy = chunkRows - 1;
            for (FlatInstance f : flatChunks[cx][cy]) {
                float fx = f.x + width, fy = f.y - height;
                if (fx >= minX && fx <= maxX && fy >= minY && fy <= maxY) {
                    visibleFlats.add(new FlatInstance(fx, fy, f.z, f.texture));
                }
            }
        }
        if (wrapRight && wrapUp) {
            int cx = 0, cy = 0;
            for (FlatInstance f : flatChunks[cx][cy]) {
                float fx = f.x + width, fy = f.y + height;
                if (fx >= minX && fx <= maxX && fy >= minY && fy <= maxY) {
                    visibleFlats.add(new FlatInstance(fx, fy, f.z, f.texture));
                }
            }
        }

        // Render all tiles via TileRenderer
        renderer.renderBatch(visibleTiles, shader, camera);
        flatRenderer.renderBatch(visibleFlats, flatShader, camera);
    }

    private int index(int x, int y, int z) {
        return x + y * width + z * width * height;
    }

    public int getElevation(int x, int y) {
        for (int z = depth - 1; z >= 0; z--) {
            Tile t = getTiles(x, y, z);
            if (t != null) {
                return z;
            }
        }
        return 0; // default if no tile found
    }

    public Boolean inWater(int x, int y, int z) {
        Tile t = getTiles(x, y, z);
        if(t == Tile.water){
            return true;
        }
            return false;
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