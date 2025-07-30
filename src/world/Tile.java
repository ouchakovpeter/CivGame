package world;

public class Tile {
    public static Tile tiles[] = new Tile[16];//amount of tiles in the world

    public static final Tile test_tile = new Tile((byte)0,"smile");

    private byte id;
    private String texture;

    public Tile(byte id, String texture){
        this.id = id;
        this.texture = texture;
        if(tiles[id] != null){
            throw new IllegalStateException("Tiles at [" + id + "] is already being used!");
        }
        tiles[id] = this;
    }

    public byte getId() {
        return id;
    }

    public String getTexture() {
        return texture;
    }
}
