package world;

import util.*;

public class WorldGenerator {

    private FastNoiseLite noise;
    private World world;
    private int width;
    private int height;
    private int depth;
    private int seed;
    private int octaves = 3;
    private float lacunarity = 1.3f;
    private float[][] noiseData;

    public WorldGenerator(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.noiseData = new float[height][width];
        this.noise = new FastNoiseLite();
        this.noise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
    }

    public void generateNoise(){
        seed = (int)Math.random();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                noiseData[x][y] = noise.GetNoise(x, y);
                System.out.println(noiseData[x][y]);
            }
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
