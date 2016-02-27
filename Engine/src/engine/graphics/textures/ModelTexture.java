package engine.graphics.textures;

public class ModelTexture {
	
	private int textureID;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	private boolean hasTransparency = false;
	private boolean useFalseLighting = false;
	
	public ModelTexture(int texture){
		this.textureID = texture;
	}
	
	public int getID(){
		return textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public boolean isUseFalseLighting() {
		return useFalseLighting;
	}

	public void setUseFalseLighting(boolean useFalseLighting) {
		this.useFalseLighting = useFalseLighting;
	}

}
