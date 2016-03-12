package engine.terrain;

public class TerrainTile {
	private float x;
	private float z;
	
	public TerrainTile(float gridX, float gridZ, float size) {
		this.x = gridX * size;
		this.z = gridZ * size;
	}
	
	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}
}
