package render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Vector3f position; //sets position in world space
    private Matrix4f projection; //projection The orthographic projection matrix that defines how the scene is projected onto the screen (no perspective distortion).

    private float yaw = -90f;
    private float pitch = 0f;

    public Camera(int width, int height)
    {
        position = new Vector3f(0,0,0); //initialized camera at 0,0,0;
        projection = new Matrix4f().perspective((float) Math.toRadians(70.0f), (float) width / height, 0.1f, 1000f);
        //Sets up a 2D orthographic projection from -width/2 to width/2 horizontally and -height/2 to height/2 vertically, which centers the projection on the origin.
    }

    public void setPosition(Vector3f position){
        this.position = position;
    }
    public void addPosition(Vector3f position){
        this.position.add(position);
    }

    public Vector3f getPosition() {
        return position;
    }

//    public Matrix4f getProjection() {
//        Matrix4f target = new Matrix4f(); //used to store the result of multiplying the projection and translation matrices
//        //Matrix4f pos = new Matrix4f().setTranslation(position); //creates a translation matrix based on the camera’s current
//
//        Matrix4f view = new Matrix4f();
//                view.translate(position);
//
//        target = projection.mul(view, target); //Multiply the projection matrix by the translation matrix, and store the result in target
//        return target;
//    }

    public Matrix4f getProjection() {
        float cosPitch = (float)Math.cos(Math.toRadians(pitch));
        float sinPitch = (float)Math.sin(Math.toRadians(pitch));
        float cosYaw = (float)Math.cos(Math.toRadians(yaw));
        float sinYaw = (float)Math.sin(Math.toRadians(yaw));

        Vector3f direction = new Vector3f(
                cosPitch * sinYaw, sinPitch,
                -cosPitch * cosYaw
        ).normalize();

        Vector3f target = new Vector3f(position).add(direction);

        Matrix4f view = new Matrix4f().lookAt(position, target, new Vector3f(0, 1, 0));
        return new Matrix4f(projection).mul(view);
    }


    public void addYaw(float amount) {
        yaw += amount;
    }

    public void addPitch(float amount) {
        pitch += amount;
        pitch = Math.max(-89.0f, Math.min(89.0f, pitch)); // prevent flipping
    }

}
