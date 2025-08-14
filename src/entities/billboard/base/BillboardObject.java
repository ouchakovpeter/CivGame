package entities.billboard.base;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import render.Model;
import render.Texture;
import render.Shader;
import render.Camera;

public class BillboardObject {

    private float x, y, z;
    private Texture texture;
    private Model model;

    public BillboardObject(float x, float y, float z, String textureFile, Model model) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.texture = new Texture(textureFile);
        this.model = model;
    }

    public void render(Camera camera, Shader shader) {
        shader.bind();

        // Position + rotate billboard to face camera
        Matrix4f modelMatrix = new Matrix4f()
                .translate(new Vector3f(x, y, z))
                .rotateY((float)Math.toRadians(camera.getRoll())); // facing

        shader.setUniform("model", modelMatrix);
        shader.setUniform("projection", camera.getProjection());
        shader.setUniform("view", camera.getViewMatrix());

        texture.bind(0); // bind to texture unit 0
        model.render();
    }
}
