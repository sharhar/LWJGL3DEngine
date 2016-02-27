package engine.input;

import org.lwjgl.glfw.GLFW;

import engine.Window;
import engine.utils.maths.Vector2f;

public class Mouse {
	
	public static Vector2f pos = new Vector2f(0, 0);
	private static boolean lock = false;
	private static Window window;
	
	public static void setWindow(Window win) {
		window = win;
	}
	
	public static void lockMouseInWindow(boolean lock) {
		if(lock) {
			GLFW.glfwSetInputMode(window.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		} else {
			GLFW.glfwSetInputMode(window.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		}
	}
	
	public static void lockMouseBounds(boolean value) {
		lock = value;
	}
	
	public static void tick() {
		if(lock) {
			if(pos.x < 0) {
				GLFW.glfwSetCursorPos(window.getWindow(), 0, window.getHeight() - pos.y);
				pos.x = 0;
			}
			if(pos.x > window.getWidth()) {
				GLFW.glfwSetCursorPos(window.getWindow(), window.getWidth(), window.getHeight() - pos.y);
				pos.x = window.getWidth();
			}
			if(pos.y < 0) {
				GLFW.glfwSetCursorPos(window.getWindow(), pos.x, window.getHeight());
				pos.y = 0;
			}
			if(pos.y > window.getHeight()) {
				GLFW.glfwSetCursorPos(window.getWindow(), pos.x, 0);
				pos.y = window.getHeight();
			}
		}
	}
	
	public static void posCallback(float x, float y) {
		pos.x = x;
		pos.y = y;
	}
	
	public static void buttonCallback(int button, int action) {
		
	}
	
	public static void scrollCallback(float x, float y) {
		System.out.println(x+ "|" + y);
	}
	
}
