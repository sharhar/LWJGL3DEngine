package engine.terrain;

import java.util.List;

import engine.entities.Camera;
import engine.entities.Light;
import engine.graphics.ShaderProgram;
import engine.utils.maths.Maths;
import engine.utils.maths.Matrix4f;
import engine.utils.maths.Vector3f;

public class TerrainShader extends ShaderProgram{
	
	public static TerrainShader inst = null;
	public static int amountOfLights = 0;
	private static boolean lightInited = false;
	
	private static final String VERTEX_FILE = "shaders/terrain.vert";
	private static final String FRAGMENT_FILE = "shaders/terrain.frag";


	public TerrainShader() {
		super();
	}
	
	public static void init() {
		if(inst == null) {
			inst = new TerrainShader();
		}
		
		if(!lightInited) {
			setLights(1, false);
		}
		
		inst.pushAllConstants();
		inst.compile(VERTEX_FILE, FRAGMENT_FILE);
	}
	public void pushAllConstants() {
		for(String name:consts.keySet()) {
			inst.constants.put(name, consts.get(name));
		}
	}
	
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}
	
	protected void getAllUniformLocations() {
		super.getUniformLocation("transformationMatrix");
		super.getUniformLocation("projectionMatrix");
		super.getUniformLocation("viewMatrix");
		
		for(int i = 0; i < amountOfLights;i++) {
			super.getUniformLocation("lightColor[" + i + "]");
			super.getUniformLocation("lightPosition[" + i + "]");
			super.getUniformLocation("attenuation[" + i + "]");
		}
		
		super.getUniformLocation("shineDamper");
		super.getUniformLocation("reflectivity");
		super.getUniformLocation("skyColor");
		super.getUniformLocation("density");
		super.getUniformLocation("gradient");
		
		super.getUniformLocation("bgTex");
		super.getUniformLocation("rTex");
		super.getUniformLocation("gTex");
		super.getUniformLocation("bTex");
		super.getUniformLocation("blendMap");
	}
	
	public void connectTextureUnits() {
		super.loadInt(uniforms.get("bgTex"), 0);
		super.loadInt(uniforms.get("rTex"), 1);
		super.loadInt(uniforms.get("gTex"), 2);
		super.loadInt(uniforms.get("bTex"), 3);
		super.loadInt(uniforms.get("blendMap"), 4);
	}
	
	public void loadFogSettings(float density, float gradient) {
		super.loadFloat(uniforms.get("density"), density);
		super.loadFloat(uniforms.get("gradient"), gradient);
	}
	
	public void setSkyColor(float r, float g, float b) {
		super.loadVec3(uniforms.get("skyColor"), new Vector3f(r,g,b));
	}
	
	public void loadProjectionMatrix(Matrix4f data) {
		super.loadMatrix4f(uniforms.get("projectionMatrix"), data);
	}
	
	public static void setLights(int amount) {
		setLights(amount, true);
	}
	
	protected static void setLights(int amount, boolean lights) {
		if(inst == null) {
			inst = new TerrainShader();
		}
		inst.addConstant("__lightNum__", "" + amount);
		amountOfLights = amount;
		lightInited = true;
		Light.initLightArray(amount);
	}
	
	public void loadLights(List<Light> lights) {
		for(int i = 0; i < amountOfLights;i++) {
			if(i < lights.size()) {
				super.loadVec3(uniforms.get("lightPosition[" + i + "]"), lights.get(i).pos);
				super.loadVec3(uniforms.get("lightColor[" + i + "]"), lights.get(i).color);
				super.loadVec3(uniforms.get("attenuation[" + i + "]"), lights.get(i).attenuation);
			} else {
				super.loadVec3(uniforms.get("lightPosition[" + i + "]"), new Vector3f());
				super.loadVec3(uniforms.get("lightColor[" + i + "]"), new Vector3f());
				super.loadVec3(uniforms.get("attenuation[" + i + "]"), new Vector3f(1, 0, 0));
			}
		}
	}
	
	public void loadTextureID(int ID) {
		super.loadFloat(uniforms.get("modelTexture"), ID);
	}
	
	public void loadTransformationMatrix(Matrix4f data) {
		super.loadMatrix4f(uniforms.get("transformationMatrix"), data);
	}
	
	public void loadShineVariables(float damper,float reflectivity){
		super.loadFloat(uniforms.get("shineDamper"), damper);
		super.loadFloat(uniforms.get("reflectivity"), reflectivity);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix4f(uniforms.get("viewMatrix"), viewMatrix);
	}
}
