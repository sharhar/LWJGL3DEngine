package engine.entities;

import engine.utils.maths.Vector2f;
import engine.utils.maths.Vector3f;

public class Light {
	
	protected static boolean[] lightIDs;
	public static void initLightArray(int amount) {
		lightIDs = new boolean[amount];
	}
	
	protected static int getID() {
		for(int i = 0; i < lightIDs.length;i++) {
			if(!lightIDs[i]) {
				lightIDs[i] = true;
				return i;
			}
		}
		
		throw new ArrayIndexOutOfBoundsException("Too many lights in scene!");
	}
	
	public Vector3f pos;
	public Vector3f color;
	public Vector3f attenuation;
	public int ID;
	
	public Light(Vector3f pos, Vector3f color, Vector3f attenuation) {
		this.pos = pos;
		this.color = color;
		this.attenuation = attenuation;
		this.ID = getID();
	}
	
	public Light(Vector3f pos, Vector3f color) {
		this(pos, color, new Vector3f(1, 0, 0));
	}
	
	public void move(Vector2f move) {
		pos.x += move.x;
		pos.y += move.y;
	}
	
	/*
	public static Light createDefaultLight(Vector3f pos, float intensity, float range) {
		return new Light(pos, intensity, range, new Vector3f(1, 1, 1), new Vector3f(0.5f,  01f,  0));
	}
	
	public static Light createDefaultLight(Vector3f pos, float intensity, float range, Vector3f color) {
		return new Light(pos, intensity, range, color, new Vector3f(0.5f,  01f,  0));
	}
	*/
	
	public void destroy() {
		lightIDs[ID] = false;
	}
}
