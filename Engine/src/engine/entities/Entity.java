package engine.entities;

import engine.graphics.models.TexturedModel;
import engine.utils.Time;
import engine.utils.maths.Vector3f;

public class Entity {

	protected TexturedModel model;
	protected Vector3f position;
	protected float rotX, rotY, rotZ;
	protected float scale;
	protected Vector3f rotOff = new Vector3f();

	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	public Vector3f getRotOff() {
		return rotOff;
	}
	
	public void setRotOff(Vector3f rot) {
		rotOff = rot;
	}
	
	public void move(Vector3f vel) {
		increasePosition(vel.x * Time.deltaTime, vel.y * Time.deltaTime, vel.z * Time.deltaTime);
	}
	
	public void rot(Vector3f vel) {
		increaseRotation(vel.x * Time.deltaTime, vel.y * Time.deltaTime, vel.z * Time.deltaTime);
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

}
