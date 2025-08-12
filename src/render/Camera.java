package render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import world.*;

public class Camera {
    private Vector3f position;
    private Vector3f forward;
    private Matrix4f projection;
    private float aspectRatio;
    private float roll = 0.75f;
    private float pitch = 0.75f;
    private final World world;

    public Camera(int width, int height,World world) {
        this.world = world;
        position = new Vector3f(world.getWidth()/2, world.getHeight()/2, 0);
        forward = new Vector3f(0, 0, 0);
        this.aspectRatio = (float)width / (float)height;
        updateProjection(width, height);
    }
    
    public void updateProjection(int width, int height) {
        this.aspectRatio = (float)width / (float)height;
        float viewWidth = 20.0f;
        float viewHeight = viewWidth / aspectRatio;

        projection = new Matrix4f().ortho(
                -viewWidth,
                viewWidth,
                -viewHeight,
                viewHeight,
                -100,
                100
        );
    }


    public Matrix4f getViewMatrix() {
    return new Matrix4f()
            .translate(-position.x, -position.y, -position.z)
            .translate(position.x, position.y, 0)
            .rotateX((float)(-getPitch()))
            .rotateZ((float)(-getRoll()))
            .translate(-position.x, -position.y, 0);
    }

    public Matrix4f getProjectionMatrix() {
        return projection;
    }
    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getForward() {
        Vector3f forward = new Vector3f((float)Math.cos(roll), (float)Math.sin(roll), 0);
        return forward;
    }
    public void addRoll(float degrees) {
        this.roll = this.roll + degrees;
    }

    public void addPitch(float degrees) {
        if(this.pitch > 0.75){
           this.pitch = 0.75f;
        }
        if(this.pitch < 0){
            this.pitch = 0;
        }
        this.pitch = this.pitch + degrees;
        System.out.println(pitch);
    }

    public float getRoll() {
        return roll;
    }

    public float getPitch() {
        return pitch;
    }
}
