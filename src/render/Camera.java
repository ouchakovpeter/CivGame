package render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Vector3f position;
    private Matrix4f projection;
    private float aspectRatio;
    private float roll = 0.0f;
    private float pitch = 0.0f; 

    public Camera(int width, int height) {
        position = new Vector3f(0, 0, 0);
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
                -1000.0f,
                1000.0f
        );
    }

    public Vector3f getPosition() {
        return position;
    }

//    public Matrix4f getViewMatrix() {
//        return new Matrix4f().translate(-position.x, 0, -position.y).rotateX((float)-pitch).rotateZ((float)-roll);
//    }
public Matrix4f getViewMatrix() {
    return new Matrix4f().translate(-position.x, -position.y, -position.z);
}

    public Matrix4f getProjectionMatrix() {
        return projection;
    }

    public void addRoll(float degrees) {
        this.roll = this.roll + degrees;
    }

    public void addPitch(float degrees) {
        this.pitch = this.pitch + degrees;
    }

    public float getRoll() {
        return roll;
    }

    public float getPitch() {
        return pitch;
    }
}
