package engine.terrain;

import java.util.ArrayList;
import java.util.List;

import engine.graphics.models.RawModel;
import engine.utils.maths.Maths;
import engine.utils.maths.Vector2f;
import engine.utils.maths.Vector3f;

public class Terrain {
	public List<TerrainTile> tiles = new ArrayList<TerrainTile>();
	
	private RawModel model;
	private float[][] heights;
	private TerrainTexturePack pack;
	
	public Terrain(TerrainTexturePack pack, String heightPath) {
		this.pack = pack;
		this.heights = TerrainUtils.getHeightMap(heightPath);
		this.model = TerrainUtils.generateTerrainFromMap(this.heights);
	}
	
	public RawModel getModel() {
		return model;
	}

	public TerrainTexturePack getPack() {
		return pack;
	}

	public void addTile(int gridX, int gridZ) {
		tiles.add(new TerrainTile(gridX, gridZ));
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
		
		float gridSquareSize = TerrainUtils.SIZE / ((float) heights.length - 1);
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
		
		if(px < tx || tx + TerrainUtils.SIZE < px) {
			return false;
		}
		
		if(pz < tz || tz + TerrainUtils.SIZE < pz) {
			return false;
		}
		
		return true;
	}
}
