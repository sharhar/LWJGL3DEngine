package engine;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import engine.graphics.MasterRenderer;
import engine.input.Keyboard;
import engine.input.Mouse;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {

	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWWindowSizeCallback sizeCallback;
	private GLFWCursorPosCallback cursorPosCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWScrollCallback scrollCallback;
	public boolean resized = false;

	private long window;

	private int WIDTH;
	private int HEIGHT;

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}

	public long getWindow() {
		return window;
	}

	public void close() {
		try {
			glfwDestroyWindow(window);
			keyCallback.release();
			sizeCallback.release();
			cursorPosCallback.release();
			mouseButtonCallback.release();
			scrollCallback.release();
		} finally {
			glfwTerminate();
			errorCallback.release();
		}
	}

	public Window() {

	}

	public Window(String title, int width, int height, boolean vsync, boolean resizable) {
		create(title, width, height, vsync, resizable);
	}

	public void create(String title, int width, int height, boolean vsync, boolean resizable) {
		this.WIDTH = width;
		this.HEIGHT = height;

		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

		if (glfwInit() != GLFW_TRUE) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		if (resizable) {
			glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		} else {
			glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		}

		window = glfwCreateWindow(width, height, title, NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			public void invoke(long window, int key, int scancode, int action, int mods) {
				Keyboard.callback(key, action);
			}
		});

		glfwSetWindowSizeCallback(window, sizeCallback = new GLFWWindowSizeCallback() {
			public void invoke(long window, int w, int h) {
				resized = true;
				WIDTH = w;
				HEIGHT = h;
			}
		});

		glfwSetCursorPosCallback(window, cursorPosCallback = new GLFWCursorPosCallback() {
			public void invoke(long window, double xpos, double ypos) {
				Mouse.posCallback((float) xpos, HEIGHT - ((float) ypos));
			}
		});

		glfwSetMouseButtonCallback(window, mouseButtonCallback = new GLFWMouseButtonCallback() {
			public void invoke(long window, int button, int action, int mods) {
				Mouse.buttonCallback(button, action);
			}
		});

		glfwSetScrollCallback(window, scrollCallback = new GLFWScrollCallback() {
			public void invoke(long window, double xoffset, double yoffset) {
				Mouse.scrollCallback((float) xoffset, (float) yoffset);
			}
		});

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

		glfwMakeContextCurrent(window);
		if (vsync) {
			glfwSwapInterval(1);
		} else {
			glfwSwapInterval(0);
		}
		glfwShowWindow(window);

		GL.createCapabilities();

		glClearColor(0.2f, 0.3f, 0.9f, 1.0f);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
}
