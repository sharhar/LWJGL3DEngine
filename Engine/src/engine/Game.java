package engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.opengl.GL11;

import engine.graphics.MasterRenderer;
import engine.input.Keyboard;
import engine.input.Mouse;
import engine.shaders.StaticShader;
import engine.terrain.TerrainShader;
//import engine.sound.SoundManager;
import engine.utils.Loader;

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
	
	public void setLoop(Loop loop) {
		this.loop = loop;
	}
	
	public Game() {
		
	}
	
	public Window getWindow (){
		return window;
	}
	
	public Game(Window window, Loop loop) {
		setCurrent(this);
		init(window, loop);
		MasterRenderer.init();
		//SoundManager.init();
		Keyboard.init();
		Mouse.setWindow(window);
		fpsCounter = new Timer();
		setCurrent(this);
	}
	
	public void init(Window window, Loop loop) {
		this.loop = loop;
		this.window = window;
		StaticShader.init();
		TerrainShader.init();
	}
	
	public void printFPS(boolean pf) {
		printFPS = pf;
	}
	
	public void start() {
		running = true;
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
			glClear(GL_COLOR_BUFFER_BIT);
			Mouse.tick();
			loop.run();
			glfwSwapBuffers(window.getWindow());
			glfwPollEvents();
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
		loop.stop();
		//SoundManager.destroy();
		fpsCounter.cancel();
		StaticShader.basicShader.cleanUp();
		TerrainShader.terrainShader.cleanUp();
		Loader.cleanUp();
		window.close();
	}
}
