package engine.graphics.textures;

import engine.utils.Loader;

public class TerrainTexturePack {
	private TerrainTexture bgTex;
	private TerrainTexture rTex;
	private TerrainTexture gTex;
	private TerrainTexture bTex;
	private TerrainTexture blendMap;
	
	public TerrainTexturePack(String[] paths) {
		this(paths[0], paths[1], paths[2], paths[3], paths[4]);
	}
	
	public TerrainTexturePack(String bgTex, String rTex, String gTex, String bTex, String blendMap) {
		super();
		this.bgTex = new TerrainTexture(Loader.loadTexture(bgTex));
		this.rTex = new TerrainTexture(Loader.loadTexture(rTex));;
		this.gTex = new TerrainTexture(Loader.loadTexture(gTex));;
		this.bTex = new TerrainTexture(Loader.loadTexture(bTex));
		this.blendMap = new TerrainTexture(Loader.loadTexture(blendMap));
	}
	
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
