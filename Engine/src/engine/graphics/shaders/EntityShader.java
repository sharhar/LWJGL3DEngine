package engine.graphics.shaders;

import engine.graphics.ShaderProgram;
import engine.objects.Light;
import engine.objects.cameras.Camera;
import engine.utils.maths.Maths;
import engine.utils.maths.Matrix4f;
import engine.utils.maths.Vector2f;
import engine.utils.maths.Vector3f;

import java.util.List;

public class EntityShader extends ShaderProgram{
	
	public static EntityShader inst = null;
	public static int amountOfLights = 0;
	private static boolean lightInited = false;
	
	private static final String VERTEX_FILE = "shaders/entity.vert";
	private static final String FRAGMENT_FILE = "shaders/entity.frag";


	public EntityShader() {
		super();
	}
	
	public static void init() {
		if(inst == null) {
			inst = new EntityShader();
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
		
		for(int i = 0;i < amountOfLights;i++) {
			super.getUniformLocation("lightColor[" + i + "]");
			super.getUniformLocation("lightPosition[" + i + "]");
			super.getUniformLocation("attenuation[" + i + "]");
		}
		
		super.getUniformLocation("shineDamper");
		super.getUniformLocation("reflectivity");
		super.getUniformLocation("useFakeLighting");
		super.getUniformLocation("skyColor");
		super.getUniformLocation("density");
		super.getUniformLocation("gradient");
		super.getUniformLocation("numberOfRows");
		super.getUniformLocation("offSet");
	}
	
	public void loadNumberOfRows(int numberOfRows) {
		super.loadFloat(uniforms.get("numberOfRows"), numberOfRows);
	}
	
	public void loadTextureOffSet(Vector2f off) {
		super.loadVec2(uniforms.get("offSet"), off);
	}
	
	public void loadFogSettings(float density, float gradient) {
		super.loadFloat(uniforms.get("density"), density);
		super.loadFloat(uniforms.get("gradient"), gradient);
	}
	
	public void setSkyColor(float r, float g, float b) {
		super.loadVec3(uniforms.get("skyColor"), new Vector3f(r,g,b));
	}
	
	public void loadFakeLighting(boolean useFakeLighting) {
		super.loadBool(uniforms.get("useFakeLighting"), useFakeLighting);
	}
	
	public void loadProjectionMatrix(Matrix4f data) {
		super.loadMatrix4f(uniforms.get("projectionMatrix"), data);
	}
	
	public static void setLights(int amount) {
		setLights(amount, true);
	}
	
	protected static void setLights(int amount, boolean lights) {
		if(inst == null) {
			inst = new EntityShader();
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
