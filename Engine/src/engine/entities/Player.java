package engine.entities;

import engine.graphics.models.TexturedModel;
import engine.utils.Settings;
import engine.utils.Time;
import engine.utils.maths.Vector3f;

public class Player extends Entity{

	private float ySpeed = 0;
	private boolean isInAir = true;
	private boolean forceJump = false;
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public void jump(float vel) {
		if(!isInAir || forceJump) {
			ySpeed = vel;
			isInAir = true;
		}
	}
	
	public void setForceJump(boolean force) {
		forceJump = force;
	}
	
	public void tick() {
		ySpeed += Settings.gravity * Time.deltaTime;
		position.y += ySpeed * Time.deltaTime;
		if(position.y < 0) {
			position.y = 0;
			ySpeed = 0;
			isInAir = false;
		}
	}
	
	public void move(float vel) {
		Vector3f rotVel = new Vector3f();
		double angle = Math.toRadians(rotY);
		rotVel.x = (float) (Math.sin(angle)*vel);
		rotVel.z = (float) (Math.cos(angle)*vel);
		super.move(rotVel);
	}
}
