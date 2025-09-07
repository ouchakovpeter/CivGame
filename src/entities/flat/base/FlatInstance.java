package entities.flat.base;

public class FlatInstance {
    public float x, y, z;
//    public float width, height;
    public String texture;

    public FlatInstance(float x, float y, float z, String texture) {
        this.x = x;
        this.y = y;
        this.z = z;
//        this.width = width;
//        this.height = height;
        this.texture = texture;
    }
}
