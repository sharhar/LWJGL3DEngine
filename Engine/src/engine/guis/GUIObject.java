package engine.guis;

import engine.utils.maths.Vector2f;

public class GUIObject {
	
	private int ID;
	private Vector2f position;
	private Vector2f scale;
	public GUIObject(int iD, Vector2f position, Vector2f scale) {
		super();
		ID = iD;
		this.position = position;
		this.scale = scale;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public void setPosition(Vector2f position) {
		this.position = position;
	}
	
	public Vector2f getScale() {
		return scale;
	}
	
	public void setScale(Vector2f scale) {
		this.scale = scale;
	}

	public int getID() {
		return ID;
	}
	
	
}
