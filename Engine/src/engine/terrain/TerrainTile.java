package engine.terrain;

public class TerrainTile {
	private float x;
	private float z;
	
	public TerrainTile(int gridX, int gridZ) {
		this.x = gridX * TerrainUtils.SIZE;
		this.z = gridZ * TerrainUtils.SIZE;
	}
	
	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}
}
