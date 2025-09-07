package entities.flat.base;

import org.joml.Vector3f;
import render.Camera;
import render.Model;
import render.Shader;
import render.Texture;
import world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlatRenderer {
    private Model flatModel;
    private Map<String, Texture> textures;  // Add this

    public FlatRenderer(Camera camera){

        this.textures = new HashMap<>();

        //defining a quad / square by defining the 4 corners in 3d space. / -0.5 means the square is 1 unit tall in width and height centered at 0,0
        float[] vertices = new float[] {
                -1f, 0f, 0f,  // bottom left      0
                1f, 0f, 0f,   // bottom right     1
                1f, 0f, 2f,  // top right     2
                -1f, 0f, 2f, // top left      3
        };

        //uv texture coordinates
        float[] texture = new float[]{
                0.0f, 0.0f,  // bottom left
                1.0f, 0.0f,  // bottom right
                1.0f, 1.0f,  // top right
                0.0f, 1.0f   // top left
        };

        //draws the triangles / splits the square into two triangles // these indicies refer to the order fo the vertices // indices save memory by reusing vertices
        int [] indices = new int[]{
                0,1,2, // First triangle (top-left, top-right, bottom-right)
                2,3,0  // Second triangle (bottom-right, bottom-left, top-left)
        };

        //shaders are important to calculate where vertices are on screen/ vertex shader puts where vertices appear on the screen/ fragment shader colors each pixel
        flatModel = new Model(vertices, texture, indices); //stores the shape (vertices, texture coords, indices).
    }
    public void renderBatch(List<FlatInstance> flats, Shader flatshader, Camera cam) {
        flatshader.bind();

        // Set up projection and view matrices
        flatshader.setUniform("projection", cam.getProjection());
        flatshader.setUniform("view", cam.getViewMatrix());
        //flatshader.setUniform("cameraPosition", cam.getPosition());

        // Group billboards by texture
        Map<String, List<FlatInstance>> textureGroups = new HashMap<>();
        for (FlatInstance flat : flats) {
            textureGroups.computeIfAbsent(flat.texture, k -> new ArrayList<>()).add(flat);
        }

        for (Map.Entry<String, List<FlatInstance>> entry : textureGroups.entrySet()) {
            String textureName = entry.getKey();
            List<FlatInstance> group = entry.getValue();
            if (group.isEmpty()) continue;

            // Bind texture
            if (!textures.containsKey(textureName)) {
                textures.put(textureName, new Texture(textureName + ".png"));
            }
            Texture tex = textures.get(textureName);
            tex.bind(0);
            flatshader.setUniform("sampler", 0);

            // Build instance data: [x, y, z, width, height, brightness, rotation]
            float[] instanceData = new float[group.size() * 4];
            for (int i = 0; i < group.size(); i++) {
                FlatInstance bi = group.get(i);
                int base = i * 4;
                instanceData[base + 0] = bi.x;
                instanceData[base + 1] = bi.y;
                instanceData[base + 2] = bi.z;
                instanceData[base + 3] = 1.0f;
            }

            // Render instanced
            flatModel.renderInstanced(instanceData, group.size(), cam, flatshader);
        }
    }
}
