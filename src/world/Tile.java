package world;

public class Tile {
    public static Tile tiles[] = new Tile[16];//amount of tiles in the world
    public static byte number_of_tiles = 0;
    public static final Tile grass = new Tile("grass"); //tile 3
    public static final Tile grid = new Tile("grid"); //tile 0
    public static final Tile dark_smile = new Tile("smile2"); //tile 1
    public static final Tile light_smile = new Tile("smile"); //tile 2


    private byte id;
    private String texture;

    public Tile(String texture){
        this.id = number_of_tiles;
        number_of_tiles++;
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
