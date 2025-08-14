package world;

public class TileInstance {
    public float x, y, z;
    public float brightness;
    public String texture;

    public TileInstance(float x, float y, float z, float brightness, String texture) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.brightness = brightness;
        this.texture = texture;
    }
}
