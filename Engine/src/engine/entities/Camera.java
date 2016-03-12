package engine.entities;

import engine.utils.maths.Vector3f;

abstract public class Camera {
	
	protected Vector3f position = new Vector3f(0,5,0);
	protected Vector3f rot  = new Vector3f(0,0,0);
	
	public Camera(){}
	
	abstract public void move();

	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getRot() {
		return rot;
	}
	
}
