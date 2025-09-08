package render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import world.*;

public class Camera {
    private Vector3f position;
    private Matrix4f projection;
    private float aspectRatio;
    private float roll = 0.75f; //angle of the world
    private float pitch = 0.85f;
    private final World world;
    private float viewWidth = 20.0f;

    private float zoom = 1.0f;

    public Camera(int width, int height,World world) {
        this.world = world;
        position = new Vector3f(world.getWidth()/2, world.getHeight()/2, 0);
        this.aspectRatio = (float)width / (float)height;
        updateProjection(width, height);
    }
    
    public void updateProjection(int width, int height) {
        this.aspectRatio = (float)width / (float)height;
        float viewHeight = viewWidth / aspectRatio;

        projection = new Matrix4f().ortho(
                -viewWidth / zoom,
                viewWidth  / zoom,
                -viewHeight  / zoom,
                viewHeight  / zoom,
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

    public void wrap(World world) {
        this.position.x = (position.x % world.getWidth() + world.getWidth()) % world.getWidth();
        this.position.y = (position.y % world.getHeight() + world.getHeight()) % world.getHeight();
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

    //I might use this
    public void addPitch(float degrees) {
        this.pitch = this.pitch + degrees;
        if(this.pitch > 0.85){
           this.pitch = 0.85f;
        }
        if(this.pitch < 0){
            this.pitch = 0;
        }
    }

    public float getRoll() {
        return roll;
    }

    public float getPitch() {
        return pitch;
    }

    public float getViewWidth(){return viewWidth;}

    public Matrix4f getProjection() {
        return projection;
    }

    public void setZoom(float zoom, int width, int height) {
        this.zoom = zoom;
        updateProjection(width, height);
    }
    public float getZoom() {
        return zoom;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRoll(float roll){
        this.roll = roll;
    }
}
