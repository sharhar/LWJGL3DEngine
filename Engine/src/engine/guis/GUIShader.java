package engine.guis;
import engine.graphics.ShaderProgram;
import engine.utils.maths.Matrix4f;

public class GUIShader extends ShaderProgram{
	
	public static GUIShader inst = null;
	
	private static final String VERTEX_FILE = "shaders/gui.vert";
	private static final String FRAGMENT_FILE = "shaders/gui.frag";


	public GUIShader() {
		super();
	}
	
	public static void init() {
		if(inst == null) {
			inst = new GUIShader();
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
	}
	
	protected void getAllUniformLocations() {
		super.getUniformLocation("transformationMatrix");
	}
	
	public void loadTransformationMatrix(Matrix4f data) {
		super.loadMatrix4f(uniforms.get("transformationMatrix"), data);
	}
	
}