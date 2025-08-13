package world;

import util.*;

public class WorldGenerator {

    private FastNoiseLite noise;
    private World world;
    private int width;
    private int height;
    private int depth;
    private int seed;
    private int octaves;
    private float lacunarity;
    private float[][] noiseData;

    public WorldGenerator(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.noiseData = new float[height][width];
        this.noise = new FastNoiseLite();
        this.noise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        this.noise.SetFractalType(FastNoiseLite.FractalType.FBm);
        this.noise.SetFractalLacunarity(5);
        this.noise.SetFractalOctaves(3);
        this.noise.SetFractalGain(0.4f);

    }

    public float[][] generateNoise(){
        seed = (int)Math.random();
        System.out.println(seed);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                noiseData[x][y] = noise.GetNoise(x, y);
            }
        }
        return noiseData;
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
