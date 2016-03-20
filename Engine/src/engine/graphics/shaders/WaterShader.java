package engine.graphics.shaders;

import engine.graphics.ShaderProgram;
import engine.objects.cameras.Camera;
import engine.utils.maths.Maths;
import engine.utils.maths.Matrix4f;
import engine.utils.maths.Vector3f;

public class WaterShader extends ShaderProgram {
	
	public static WaterShader inst = null;
	 
	private static final String VERTEX_FILE = "shaders/water.vert";
	private static final String FRAGMENT_FILE = "shaders/water.frag";
 
    public WaterShader() {
        super();
    }
    
    public static void init() {
		if(inst == null) {
			inst = new WaterShader();
		}
		
		//if(!lightInited) {
		//	setLights(1, false);
		//}
		
		inst.pushAllConstants();
		inst.compile(VERTEX_FILE, FRAGMENT_FILE);
	}
    
    
    public void pushAllConstants() {
		
	}
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
 
    @Override
    protected void getAllUniformLocations() {
    	super.getUniformLocation("projectionMatrix");
        super.getUniformLocation("viewMatrix");
        super.getUniformLocation("modelMatrix");
        
        super.getUniformLocation("reflectionTexture");
        super.getUniformLocation("refractionTexture");
        
        super.getUniformLocation("camPos");
    }
    
    public void setCameraPosition(Vector3f pos) {
    	super.loadVec3(uniforms.get("camPos"), pos);
    }
    
    public void connectTextureUnits() {
    	super.loadInt(uniforms.get("reflectionTexture"), 0);
    	super.loadInt(uniforms.get("refractionTexture"), 1);
    }
 
    public void loadProjectionMatrix(Matrix4f projection) {
        loadMatrix4f(uniforms.get("projectionMatrix"), projection);
    }
     
    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        loadMatrix4f(uniforms.get("viewMatrix"), viewMatrix);
    }
 
    public void loadModelMatrix(Matrix4f modelMatrix){
    	loadMatrix4f(uniforms.get("modelMatrix"), modelMatrix);
    }
 
}