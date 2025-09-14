package world;

import util.*;

import static org.joml.Math.lerp;

public class NoiseGenerator {

    private FastNoiseLite noise;
    private World world;
    private int width;
    private int height;
    private int depth;
    private int seed;
    private int octaves;
    private float lacunarity;
    private float[][] noiseRaw;
    private float[][] noiseData;

    private int feather;

    public NoiseGenerator(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.noiseRaw = new float[height][width];
        this.noiseData = new float[height][width];
        this.noise = new FastNoiseLite();
        this.noise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        this.noise.SetFractalType(FastNoiseLite.FractalType.FBm);
        this.noise.SetFractalLacunarity(5);
        this.noise.SetFractalOctaves(3);
        this.noise.SetFractalGain(0.4f);
        feather = 3;
    }

    //improve so that the generation works with wrapping

    public float[][] generateNoise(){
        seed = (int)(Math.random() * 10000);
        System.out.println("seed:" + seed);
        this.noise.SetSeed(seed);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                noiseRaw[x][y] = noise.GetNoise(x, y);
                noiseData[x][y] = (noiseRaw[x][y] + 0.1f) * 0.5f; //shapes the noise
            }
        }

        if (feather > 0) {
            // Left/Right edges
            for (int x = 0; x < feather; x++) {
                float s = 1.0f - (float)x / (float)feather;
                for (int y = 0; y < height; y++) {
                    int rx = width - 1 - x;
                    float left = noiseData[x][y];
                    float right = noiseData[rx][y];
                    float target = 0.5f * (left + right);
                    noiseData[x][y] = lerp(left, target, s);
                    noiseData[rx][y] = lerp(right, target, s);
                }
            }
            // Top/Bottom edges
            for (int y = 0; y < feather; y++) {
                float s = 1.0f - (float)y / (float)feather;
                int by = height - 1 - y;
                for (int x = 0; x < width; x++) {
                    float top = noiseData[x][y];
                    float bot = noiseData[x][by];
                    float target = 0.5f * (top + bot);
                    noiseData[x][y] = lerp(top, target, s);
                    noiseData[x][by] = lerp(bot, target, s);
                }
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
