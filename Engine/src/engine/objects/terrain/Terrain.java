package engine.objects.terrain;

import java.util.ArrayList;
import java.util.List;

import engine.graphics.models.RawModel;
import engine.graphics.textures.TerrainTexturePack;
import engine.utils.TerrainUtils;
import engine.utils.maths.Maths;
import engine.utils.maths.Vector2f;
import engine.utils.maths.Vector3f;

public class Terrain {
	public List<TerrainTile> tiles = new ArrayList<TerrainTile>();
	
	private RawModel model;
	private float[][] heights;
	private TerrainTexturePack pack;
	private float size;
	private float maxHeigh;
	
	public Terrain(TerrainTexturePack pack, String heightPath) {
		this(pack, heightPath, 800);
	}
	
	public Terrain(TerrainTexturePack pack, String heightPath, float size) {
		this(pack, heightPath, size, 40);
	}
	
	public Terrain(TerrainTexturePack pack, String heightPath, float size, float maxHeight) {
		this(pack, heightPath, size, maxHeight, 256 * 256 * 256);
	}
	
	public Terrain(TerrainTexturePack pack, String heightPath, float size, float maxHeight, float maxPixelColor) {
		this.pack = pack;
		this.size = size;
		this.maxHeigh = maxHeight;
		this.heights = TerrainUtils.getHeightMap(heightPath, this.maxHeigh, maxPixelColor);
		this.model = TerrainUtils.generateTerrainFromMap(this.heights, this.size);
	}
	
	public RawModel getModel() {
		return model;
	}

	public TerrainTexturePack getPack() {
		return pack;
	}

	public void addTile(float gridX, float gridZ) {
		tiles.add(new TerrainTile(gridX, gridZ, size));
	}
	
	public List<TerrainTile> getTiles() {
		return tiles;
	}
	
	public float getHeightAt(Vector3f pos) {
		TerrainTile current = getCurrentTile(pos);
		if(current == null) {
			return 0;
		}
		
		float x = pos.x - current.getX();
		float z = pos.z - current.getZ();
		
		float gridSquareSize = this.size / ((float) heights.length - 1);
		int gridX = (int) Math.floor(x / gridSquareSize);
		int gridZ = (int) Math.floor(z / gridSquareSize);
		
		if(gridX >= heights.length -1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}
		
		float xCoord = (x % gridSquareSize)/gridSquareSize;
		float zCoord = (z % gridSquareSize)/gridSquareSize;
		
		float result = 0;
		
		if (xCoord <= (1-zCoord)) {
			result = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			result = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		
		return result;
	}
	
	public TerrainTile getCurrentTile(Vector3f position) {
		for(TerrainTile tile:tiles) {
			if(isPlayerInTile(position, tile)) {
				return tile;
			}
		}
		
		return null;
	}
	
	private boolean isPlayerInTile(Vector3f position, TerrainTile tile) {
		float px = position.x;
		float pz = position.z;
		float tx = tile.getX();
		float tz = tile.getZ();
		
		if(px < tx || tx + this.size < px) {
			return false;
		}
		
		if(pz < tz || tz + this.size < pz) {
			return false;
		}
		
		return true;
	}
}
