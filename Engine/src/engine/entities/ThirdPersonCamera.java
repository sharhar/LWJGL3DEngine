package engine.entities;

import org.lwjgl.glfw.GLFW;

import engine.input.Keyboard;
import engine.input.Mouse;

public class ThirdPersonCamera extends PlayerCamera{
	
	float distance = 20;
	float angleAround = 180;

	public ThirdPersonCamera(Player player) {
		super(player);
	}
	
	public void move() {
		if(Keyboard.isKeyDown(GLFW.GLFW_KEY_KP_5)) {
			angleAround = 180;
		}
		
		float zoomLevel = Mouse.getScroll() * distance * 0.03f;
		distance -= zoomLevel;
		if(Mouse.isButtonDown(Mouse.BUTTON_RIGHT)) {
			float pitchDelta = Mouse.getMouseDY() * 0.1f;
			rot.x -= pitchDelta;
		}
		
		if(Mouse.isButtonDown(Mouse.BUTTON_LEFT)) {
			float angleDelta = Mouse.getMouseDX() * 0.1f;
			angleAround -= angleDelta;
		}
		
		double radRot = Math.toRadians(rot.x);
		
		float hor = (float) (distance * Math.cos(radRot));
		float ver = (float) (distance * Math.sin(radRot));
		
		position.y = player.position.y + ver + 02f;
		
		float theta = player.getRotY() + angleAround;
		double radTheta = Math.toRadians(theta);
		float xOff = (float) (hor * Math.sin(radTheta));
		float zOff = (float) (hor * Math.cos(radTheta));
		
		position.x = player.position.x - xOff;
		position.z = player.position.z - zOff;
		
		rot.y = -angleAround-player.rotY-180;
	}
}
