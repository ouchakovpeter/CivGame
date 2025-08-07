package world;

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
        // Get camera position and rotation
        Vector3f cameraPos = camera.getPosition();
        float cameraX = cameraPos.x;
        float cameraY = cameraPos.y;
        float cameraZ = cameraPos.z;
        
        // Calculate view area based on camera zoom and aspect ratio
        float viewWidth = 20.0f * camera.getZoom();
        float viewHeight = viewWidth * ((float)window.getHeight() / window.getWidth());
        
        // Calculate which tiles should be visible
        int startX = (int)(cameraX - viewWidth/2);
        int startY = (int)(cameraY - viewHeight/2);
        int endX = (int)(cameraX + viewWidth/2) + 1;
        int endY = (int)(cameraY + viewHeight/2) + 1;
        
        // Clamp to world bounds
        startX = Math.max(0, startX);
        startY = Math.max(0, startY);
        endX = Math.min(width, endX);
        endY = Math.min(height, endY);
        
        // Calculate world transform
        worldTransform.identity()
            .translate(-cameraX, -cameraY, -cameraZ)  // Center camera
            .rotateY((float)Math.toRadians(-camera.getYaw()))  // Apply yaw
            .rotateX((float)Math.toRadians(-camera.getPitch())) // Apply pitch
            .scale(scale);  // Apply scale
        
        // Render visible tiles
        for (int z = 0; z < depth; z++) {
            for (int x = startX; x < endX; x++) {
                for (int y = startY; y < endY; y++) {
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
}