package engine;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import engine.input.Keyboard;
import engine.input.Mouse;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.opengl.ARBFramebufferObject.*;

import java.nio.IntBuffer;

public class Window {

	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWWindowSizeCallback sizeCallback;
	private GLFWCursorPosCallback cursorPosCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWScrollCallback scrollCallback;
	private GLFWFramebufferSizeCallback fbCallback;
	private int frameBufferWidth;
	private int frameBufferHeight;
	private boolean resetFrameBuffer = false;
	private int colorRenderBuffer;
	private int depthRenderBuffer;
	private int samples;
	private int fbo;
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
	
	public int getSamples() {
		return samples;
	}

	public void close() {
		try {
			glfwDestroyWindow(window);
			keyCallback.release();
			sizeCallback.release();
			cursorPosCallback.release();
			mouseButtonCallback.release();
			scrollCallback.release();
			fbCallback.release();
		} finally {
			glfwTerminate();
			errorCallback.release();
		}
	}

	public Window() {

	}
	
	public Window(String title, int width, int height, boolean vsync, boolean resizable) {
		create(title, width, height, vsync, resizable, 1);
	}

	public Window(String title, int width, int height, boolean vsync, boolean resizable, int samples) {
		create(title, width, height, vsync, resizable, samples);
	}

	public void create(String title, int width, int height, boolean vsync, boolean resizable, int samp) {
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
		
		glfwSetFramebufferSizeCallback(window, fbCallback = new GLFWFramebufferSizeCallback() {
			public void invoke(long window, int width, int height) {
				if (width > 0 && height > 0 && (WIDTH != width || HEIGHT != height)) {
					frameBufferWidth = width;
					frameBufferHeight = height;
					resetFrameBuffer = true;
				}
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
		
		IntBuffer frameBufferSize = BufferUtils.createIntBuffer(2);
		nglfwGetFramebufferSize(window, memAddress(frameBufferSize), memAddress(frameBufferSize) + 4);
		frameBufferWidth = frameBufferSize.get(0);
		frameBufferHeight = frameBufferSize.get(1);
		
		GL.createCapabilities();
		
		samples = Math.min(samp, glGetInteger(GL_MAX_SAMPLES));
		
		glfwWindowHint(GLFW_SAMPLES, 4);
		
		colorRenderBuffer = glGenRenderbuffers();
		depthRenderBuffer = glGenRenderbuffers();
		fbo = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		
		glBindRenderbuffer(GL_RENDERBUFFER, colorRenderBuffer);
		glRenderbufferStorageMultisample(GL_RENDERBUFFER, samples, GL_RGBA8, frameBufferWidth, frameBufferHeight);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, colorRenderBuffer);
		
		glBindRenderbuffer(GL_RENDERBUFFER, depthRenderBuffer);
		glRenderbufferStorageMultisample(GL_RENDERBUFFER, samples, GL_DEPTH24_STENCIL8, frameBufferWidth, frameBufferHeight);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthRenderBuffer);
		
		int fboStatus = glCheckFramebufferStatus(GL_FRAMEBUFFER);
		if (fboStatus != GL_FRAMEBUFFER_COMPLETE) {
			throw new AssertionError("Could not create FBO: " + fboStatus);
		}
		glBindFramebuffer(GL_FRAMEBUFFER, 0);

		glClearColor(0.2f, 0.3f, 0.9f, 1.0f);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		//glEnable(GL_CULL_FACE);
		//glCullFace(GL_BACK);
		glEnable(GL13.GL_MULTISAMPLE);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public int getFBO() {
		return fbo;
	}
	
	public void bindBuffer() {
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
	}
	
	public void unbindBuffer() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	public void copyBufferData() {
		glBindFramebuffer(GL_READ_FRAMEBUFFER, fbo);
		glBlitFramebuffer(0, 0, frameBufferWidth, frameBufferHeight, 0, 0, frameBufferWidth, frameBufferHeight, GL_COLOR_BUFFER_BIT, GL_NEAREST);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	public void update() {
		if(resetFrameBuffer) {
			glDeleteRenderbuffers(depthRenderBuffer);
			glDeleteRenderbuffers(colorRenderBuffer);
			glDeleteFramebuffers(fbo);
			
			colorRenderBuffer = glGenRenderbuffers();
			depthRenderBuffer = glGenRenderbuffers();
			fbo = glGenFramebuffers();
			glBindFramebuffer(GL_FRAMEBUFFER, fbo);
			
			glBindRenderbuffer(GL_RENDERBUFFER, colorRenderBuffer);
			glRenderbufferStorageMultisample(GL_RENDERBUFFER, samples, GL_RGBA8, frameBufferWidth, frameBufferHeight);
			glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, colorRenderBuffer);
			
			glBindRenderbuffer(GL_RENDERBUFFER, depthRenderBuffer);
			glRenderbufferStorageMultisample(GL_RENDERBUFFER, samples, GL_DEPTH24_STENCIL8, frameBufferWidth, frameBufferHeight);
			glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthRenderBuffer);
			
			int fboStatus = glCheckFramebufferStatus(GL_FRAMEBUFFER);
			if (fboStatus != GL_FRAMEBUFFER_COMPLETE) {
				throw new AssertionError("Could not create FBO: " + fboStatus);
			}
			glBindFramebuffer(GL_FRAMEBUFFER, 0);
		}
	}
}
