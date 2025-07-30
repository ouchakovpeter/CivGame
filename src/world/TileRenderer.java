package world;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import render.*;

import java.util.HashMap;

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
    public void renderTile(Tile id, int x, int y, Shader shader, Matrix4f world, Camera cam){
        shader.bind();
        if(tile_textures.containsKey(id.getTexture())){
            tile_textures.get(id.getTexture()).bind(0);
        }
        Matrix4f tile_pos = new Matrix4f().translate(new Vector3f(x*2, y*2, 0));
        Matrix4f target = new Matrix4f();

        cam.getProjection().mul(world, target);
        target.mul(tile_pos);

        shader.setUniform("sampler", 0);
        shader.setUniform("projection", target);

        model.render();
    }
}


