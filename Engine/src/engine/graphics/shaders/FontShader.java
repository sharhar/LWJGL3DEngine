package engine.graphics.shaders;

import engine.graphics.ShaderProgram;
import engine.utils.maths.Matrix4f;
import engine.utils.maths.Vector2f;
import engine.utils.maths.Vector3f;

public class FontShader extends ShaderProgram {
	public static FontShader inst = null;

	private static final String VERTEX_FILE = "shaders/font.vert";
	private static final String FRAGMENT_FILE = "shaders/font.frag";

	public FontShader() {
		super();
	}

	public static void init() {
		if (inst == null) {
			inst = new FontShader();
		}

		inst.pushAllConstants();
		inst.compile(VERTEX_FILE, FRAGMENT_FILE);
	}

	protected void getAllUniformLocations() {
		super.getUniformLocation("color");
		super.getUniformLocation("translation");
		super.getUniformLocation("projectionMatrix");
		super.getUniformLocation("size");
		super.getUniformLocation("scale");
		super.getUniformLocation("width");
		super.getUniformLocation("edge");
	}

	public void pushAllConstants() {

	}

	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	
	public void loadTextSettings(float width, float edge) {
		super.loadFloat(uniforms.get("width"), width);
		super.loadFloat(uniforms.get("edge"), edge);
	}
	
	public void loadSize(float size) {
		super.loadFloat(uniforms.get("size"), size);
	}
	
	public void loadScale(Vector2f scale) {
		super.loadVec2(uniforms.get("scale"), scale);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix4f(uniforms.get("projectionMatrix"), matrix);
	}

	public void loadColor(Vector3f colour) {
		super.loadVec3(uniforms.get("color"), colour);
	}

	public void loadTranslation(Vector2f translation) {
		super.loadVec2(uniforms.get("translation"), translation);
	}
}
