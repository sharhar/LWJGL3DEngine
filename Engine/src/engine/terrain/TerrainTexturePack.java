package engine.terrain;

public class TerrainTexturePack {
	private TerrainTexture bgTex;
	private TerrainTexture rTex;
	private TerrainTexture gTex;
	private TerrainTexture bTex;
	private TerrainTexture blendMap;
	
	public TerrainTexturePack(TerrainTexture bgTex, TerrainTexture rTex, TerrainTexture gTex, TerrainTexture bTex,
			TerrainTexture blendMap) {
		super();
		this.bgTex = bgTex;
		this.rTex = rTex;
		this.gTex = gTex;
		this.bTex = bTex;
		this.blendMap = blendMap;
	}

	public TerrainTexture getBgTex() {
		return bgTex;
	}

	public TerrainTexture getrTex() {
		return rTex;
	}

	public TerrainTexture getgTex() {
		return gTex;
	}

	public TerrainTexture getbTex() {
		return bTex;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}
	
	
}
