package entities.flat.base;

public class Flat {
    public static Flat flats[] = new Flat[16];//amount of tiles types / textures
    public static byte number_of_tiles = 0;

    private byte id;
    private String texture;

    public Flat(String texture){
        this.id = number_of_tiles;
        number_of_tiles++;
        this.texture = texture;
        if(flats[id] != null){
            throw new IllegalStateException("Tiles at [" + id + "] is already being used!");
        }
        flats[id] = this;
    }

    public byte getId() {
        return id;
    }

    public String getTexture() {
        return texture;
    }
}
