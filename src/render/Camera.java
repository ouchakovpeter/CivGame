package render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Vector3f position;
    private Matrix4f projection;
    private float zoom = 5.0f; 
    private float aspectRatio;
    private float yaw = 0.0f;   
    private float pitch = 0.0f; 

    public Camera(int width, int height) {
        position = new Vector3f(0, 0, 0);
        this.aspectRatio = (float)width / (float)height;
        updateProjection(width, height);
    }
    
    public void updateProjection(int width, int height) {
        this.aspectRatio = (float)width / (float)height;
        float viewWidth = 20.0f * zoom; 
        float viewHeight = viewWidth / aspectRatio;
        
        projection = new Matrix4f().ortho(
                -viewWidth/2,   
                viewWidth/2,    
                -viewHeight/2,  
                viewHeight/2,   
                -1000.0f,       
                1000.0f        
        );
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    public void addPosition(Vector3f delta) {
        float angle = (float)Math.toRadians(yaw);
        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);
        
        float rotatedX = delta.x * cos - delta.y * sin;
        float rotatedY = delta.x * sin + delta.y * cos;
        
        position.add(rotatedX, rotatedY, 0);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f()
            .translate(-position.x, -position.y, 0)
            .rotateY((float)Math.toRadians(-yaw))
            .rotateX((float)Math.toRadians(-pitch));
    }

    public Matrix4f getProjectionMatrix() {
        return projection;
    }

    public Matrix4f getViewProjectionMatrix() {
        return new Matrix4f(projection).mul(getViewMatrix());
    }

    public void addYaw(float degrees) {
        this.yaw = (this.yaw + degrees) % 360;
    }

    public void addPitch(float degrees) {
        this.pitch = Math.max(-89, Math.min(89, this.pitch + degrees));
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setZoom(float zoom) {
        this.zoom = Math.max(0.5f, Math.min(20.0f, zoom));
        updateProjection((int)(40 * aspectRatio), 40);
    }

    public float getZoom() {
        return zoom;
    }

    public void moveBy(float dx, float dy) {
        // Adjust for zoom level
        float zoomFactor = 1.0f / zoom;
        
        // Rotate the movement vector by the inverse of the camera's yaw
        float angle = (float)Math.toRadians(-yaw);
        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);
        
        // Apply rotation and zoom to the movement
        float rotatedX = (dx * cos - dy * sin) * zoomFactor * 0.1f;
        float rotatedY = (dx * sin + dy * cos) * zoomFactor * 0.1f;
        
        position.add(-rotatedX, -rotatedY, 0);
    }
}
