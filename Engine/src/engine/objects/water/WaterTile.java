package engine.objects.water;

public class WaterTile {
    
    public static final float TILE_SIZE = 60;
     
    public float height;
    public float x,z;
     
    public WaterTile(float centerX, float centerZ, float height){
        this.x = centerX;
        this.z = centerZ;
        this.height = height;
    }
}
