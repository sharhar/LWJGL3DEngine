package engine.utils.maths;

public class Vector4f {
	
	public float x;
	public float y;
	public float z;
	public float w;
	
	public Vector4f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 0;
	}
	
	public Vector4f() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.w = 0;
	}
}

