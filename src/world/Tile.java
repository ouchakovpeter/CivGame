package world;

public class Tile {
    public static Tile tiles[] = new Tile[16];//amount of tiles types / textures
    public static byte number_of_tiles = 0;

        //IDing the textures to a tile type
    public static final Tile grass = new Tile("grass"); //tile 0
    public static final Tile forest = new Tile("forest"); //tile 1
    public static final Tile water = new Tile("water"); //tile 2


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
