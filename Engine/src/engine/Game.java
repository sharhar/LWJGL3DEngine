package engine;

import static org.lwjgl.glfw.GLFW.*;

import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.opengl.GL11;

import engine.graphics.MasterRenderer;
import engine.graphics.shaders.EntityShader;
import engine.graphics.shaders.FontShader;
import engine.graphics.shaders.GUIShader;
import engine.graphics.shaders.TerrainShader;
import engine.graphics.shaders.WaterShader;
import engine.input.Keyboard;
import engine.input.Mouse;
import engine.objects.cameras.Camera;
import engine.utils.Loader;
import engine.utils.Time;

public class Game {
	
	public static Game current;
	
	public static void setCurrent(Game inst) {
		current = inst;
	}
	
	private Loop loop;
	private Window window;
	private boolean running = false;
	private Timer fpsCounter;
	private int fps = 0;
	private boolean printFPS = false;
	private Camera camera;
	
	public void setLoop(Loop loop) {
		this.loop = loop;
	}
	
	public static void setLights(int number) {
		EntityShader.setLights(number);
		TerrainShader.setLights(number);
	}
	
	public Game() {
		
	}
	
	public Window getWindow (){
		return window;
	}
	
	public void setCamera(Camera cam) {
		camera = cam;
	}
	
	public Game(Window window) {
		setCurrent(this);
		init(window);
		MasterRenderer.init();
		Keyboard.init();
		Mouse.setWindow(window);
		MasterRenderer.setSkyColor(0.2f, 0.8f, 1);
		MasterRenderer.setFogSettings(0.002f, 20);
		fpsCounter = new Timer();
		setCurrent(this);
	}
	
	public void init(Window window) {
		this.window = window;
		EntityShader.init();
		TerrainShader.init();
		GUIShader.init();
		WaterShader.init();
		FontShader.init();
	}
	
	public void printFPS(boolean pf) {
		printFPS = pf;
	}
	
	public void start() {
		running = true;
		
		if(camera == null) {
			System.err.println("ENGINE>> Error: Camera is set to null!");
			System.exit(-1);
		}
		
		fpsCounter.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if(printFPS) {
					System.out.println("ENGINE>> FPS: " + fps);
				}
				fps = 0;
			}
		}, 1000, 1000);
		run();
	}
	
	public void run() {
		while(running) {
			
			glfwPollEvents();
			window.update();
			Mouse.tick();
			Time.tick();
			//window.bindBuffer();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
			
			loop.loop();
			//MasterRenderer.renderScene(camera);
			//MasterRenderer.renderWater(camera);
			//MasterRenderer.renderGUI();
			
			//window.unbindBuffer();
			
			//MasterRenderer.clearBuffers();
			
			//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
			//window.copyBufferData();
			
			glfwSwapBuffers(window.getWindow());
			if(window.resized) {
				GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
				window.resized = false;
			}
			if(glfwWindowShouldClose(window.getWindow()) == GLFW_TRUE) {
				close();
			}
			
			fps++;
		}
		
		destroy();
	}
	
	public void close() {
		running = false;
	}
	
	public void destroy() {
		fpsCounter.cancel();
		EntityShader.inst.cleanUp();
		TerrainShader.inst.cleanUp();
		Loader.cleanUp();
		window.close();
	}
}
