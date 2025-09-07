package world;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import render.*;

import java.nio.FloatBuffer;
import java.util.*;

public class TileRenderer {
    private HashMap<String , Texture> tile_textures;
    private Model model;

    public TileRenderer(){
        tile_textures = new HashMap<String, Texture>();

        //defining a quad / square by defining the 4 corners in 3d space. / -0.5 means the square is 1 unit tall in width and height centered at 0,0
        float[] vertices = new float[] {
                -1f, 1f, 0,  // top left      0
                1f, 1f, 0,   // top right     1
                1f, -1f, 0,  // bottom right  2
                -1f, -1f, 0, // top left      3
        };

        //uv texture coordinates
        float[] texture = new float[]{
                1,1, // Top-left corner
                0,1, // Top-right corner
                0,0, // Bottom-right corner
                1,0, // Bottom-left corner
        };

        //draws the triangles / splits the square into two triangles // these indicies refer to the order fo the vertices // indices save memory by reusing vertices
        int [] indices = new int[]{
                0,1,2, // First triangle (top-left, top-right, bottom-right)
                2,3,0  // Second triangle (bottom-right, bottom-left, top-left)
        };

        //shaders are important to calculate where vertices are on screen/ vertex shader puts where vertices appear on the screen/ fragment shader colors each pixel
        model = new Model(vertices, texture, indices); //stores the shape (vertices, texture coords, indices).

        for(int i = 0; i < Tile.tiles.length; i++){
            if(Tile.tiles[i] != null){
                if (!tile_textures.containsKey(Tile.tiles[i].getTexture()))
                {
                    String tex = Tile.tiles[i].getTexture();
                    tile_textures.put (tex, new Texture(tex + ".png") );
                }
            }
        }
    }
    public void renderBatch(List<TileInstance> tiles, Shader shader, Camera cam) {
        shader.bind();

        // Group tiles by texture
        Map<String, List<TileInstance>> textureGroups = new HashMap<>();
        for (TileInstance t : tiles) {
            textureGroups.computeIfAbsent(t.texture, k -> new ArrayList<>()).add(t);
        }

        for (Map.Entry<String, List<TileInstance>> entry : textureGroups.entrySet()) {
            String textureName = entry.getKey();
            List<TileInstance> group = entry.getValue();
            if (group.isEmpty()) continue;

            // Bind the texture
            if (!tile_textures.containsKey(textureName)) {
                tile_textures.put(textureName, new Texture(textureName + ".png"));
            }
            Texture tex = tile_textures.get(textureName);
            tex.bind(0);
            shader.setUniform("sampler", 0); // must match fragment shader uniform

            // Build instance buffer (x, y, z, brightness)
            float[] instanceData = new float[group.size() * 4];
            for (int i = 0; i < group.size(); i++) {
                TileInstance ti = group.get(i);
                instanceData[i * 4] = ti.x + 0.5f;
                instanceData[i * 4 + 1] = ti.y + 0.5f;
                instanceData[i * 4 + 2] = ti.z * 0.1f;
                instanceData[i * 4 + 3] = ti.brightness; // ensure 0.3–1.0 range
            }

            // Render instanced
            model.renderInstanced(instanceData, group.size(), cam, shader);
        }
    }
}
