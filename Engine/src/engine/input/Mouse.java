package engine.input;

import org.lwjgl.glfw.GLFW;

import engine.Window;
import engine.utils.maths.Vector2f;

public class Mouse {
	
	private static Vector2f pos = new Vector2f(0, 0);
	private static Vector2f last_pos = new Vector2f(0, 0);
	private static boolean lock = false;
	private static Window window;
	
	public static final int BUTTON_LEFT = GLFW.GLFW_MOUSE_BUTTON_LEFT;
	public static final int BUTTON_RIGHT = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
	public static final int BUTTON_MIDDLE = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
	
	private static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST+1];
	
	static {
		for(int i = 0; i < buttons.length;i++) {
			buttons[i] = false;
		}
	}
	
	private static float scroll = 0;
	
	public static void setWindow(Window win) {
		window = win;
	}
	
	public static Vector2f getMousePos() {
		return pos;
	}
	
	public static float getMouseX() {
		return pos.x;
	}
	
	public static float getMouseY() {
		return pos.y;
	}
	
	public static float getMouseDX() {
		return pos.x - last_pos.x;
	}
	
	public static float getMouseDY() {
		return pos.y - last_pos.y;
	}
	
	public static boolean isButtonDown(int key) {
		return buttons[key];
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
		last_pos.x = pos.x;
		last_pos.y = pos.y;
		pos.x = x;
		pos.y = y;
	}
	
	public static void buttonCallback(int button, int action) {
		if(action == GLFW.GLFW_PRESS) {
			buttons[button] = true;
		} else if(action == GLFW.GLFW_RELEASE) {
			buttons[button] = false;
		}
	}
	
	public static void scrollCallback(float x, float y) {
		scroll = y;
	}
	
	public static float getScroll() {
		float temp = scroll;
		scroll = 0;
		return temp;
	}
}
