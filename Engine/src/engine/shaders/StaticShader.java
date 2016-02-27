package engine.shaders;

import engine.utils.Maths;
import engine.utils.maths.Matrix4f;

import engine.entities.Camera;
import engine.entities.Light;

public class StaticShader extends ShaderProgram{
	
	public static StaticShader basicShader = null;
	public static int amountOfLights = 0;
	private static boolean lightInited = false;
	@SuppressWarnings("unused")
	private static boolean lightsOn = false;
	
	private static final String VERTEX_FILE = "shaders/entity.vert";
	private static final String FRAGMENT_FILE = "shaders/entity.frag";


	public StaticShader() {
		super();
	}
	
	public static void init() {
		if(basicShader == null) {
			basicShader = new StaticShader();
		}
		
		if(!lightInited) {
			setLights(1, false);
		}
		
		basicShader.pushAllConstants();
		basicShader.compile(VERTEX_FILE, FRAGMENT_FILE);
	}
	public void pushAllConstants() {
		for(String name:consts.keySet()) {
			basicShader.constants.put(name, consts.get(name));
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
		super.getUniformLocation("lightColor");
		super.getUniformLocation("lightPosition");
		super.getUniformLocation("shineDamper");
		super.getUniformLocation("reflectivity");
	}
	
	public void loadProjectionMatrix(Matrix4f data) {
		super.loadMatrix4f(uniforms.get("projectionMatrix"), data);
	}
	
	public static void setLights(int amount) {
		setLights(amount, true);
	}
	
	protected static void setLights(int amount, boolean lights) {
		if(basicShader == null) {
			basicShader = new StaticShader();
		}
		basicShader.addConstant("__lightNum__", "" + amount);
		amountOfLights = amount;
		lightInited = true;
		Light.initLightArray(amount);
		lightsOn = lights;
	}
	
	public void loadLightAdvanced(Light light) {
		super.loadVec3(uniforms.get("lightPosition[" + light.ID + "]"), light.pos);
		super.loadVec3(uniforms.get("lightAttenuation[" + light.ID + "]"), light.attenuation);
		super.loadVec3(uniforms.get("lightColor[" + light.ID + "]"), light.color);
		super.loadFloat(uniforms.get("lightIntensity[" + light.ID + "]"), light.intensity);
		super.loadFloat(uniforms.get("lightRange[" + light.ID + "]"), light.range);
	}
	
	public void loadLight(Light light) {
		super.loadVec3(uniforms.get("lightColor"), light.color);
		super.loadVec3(uniforms.get("lightPosition"), light.pos);
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
