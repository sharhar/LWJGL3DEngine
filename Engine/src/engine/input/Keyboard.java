package engine.input;

import org.lwjgl.glfw.*;

public class Keyboard{
	public static final int KEY_NUM = GLFW.GLFW_KEY_LAST+1;
	public static boolean[] keys = new boolean[KEY_NUM];
	public static boolean[] past_keys = new boolean[KEY_NUM];
	
	public static boolean isKeyDown(int key) {
		return keys[key];
	}
	
	public static boolean isKeyPressed(int key) {
		boolean result = keys[key] && !past_keys[key];
		if(result) {
			past_keys[key] = true;
		}
		return result;
	}
	
	public static void callback(int key, int action) {
		past_keys[key] = keys[key];
		keys[key] = GLFW.GLFW_PRESS == action || GLFW.GLFW_REPEAT == action;
	}
	
	public static void init() {
		for(int i = 0; i < keys.length;i++) {
			keys[i] = false;
		}
		
		for(int i = 0; i < past_keys.length;i++) {
			past_keys[i] = false;
		}
	}
}
