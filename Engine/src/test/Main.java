package test;

import engine.utils.Loader;
import engine.utils.OBJLoader;
import engine.utils.maths.Vector3f;

import org.lwjgl.opengl.GL11;

import engine.Game;
import engine.Loop;
import engine.Window;
import engine.entities.Camera;
import engine.entities.Entity;
import engine.entities.Light;
import engine.graphics.MasterRenderer;
import engine.graphics.models.RawModel;
import engine.graphics.models.TexturedModel;
import engine.graphics.textures.ModelTexture;
import engine.terrain.Terrain;

public class Main implements Loop{

	Game game;
	Entity entity;
	Light light;
	Camera camera;
	Terrain terrain;
	
	public void run() {
		camera.move();
		MasterRenderer.addEntity(entity);
		MasterRenderer.addTerrain(terrain);
		
		MasterRenderer.renderScene(light, camera);
	}

	public void stop() {
		
	}
	
	public Main() {
		Window window = new Window("Game", 1280, 720, true, false);
		game = new Game(window, this);
		
		GL11.glClearColor(0.2f, 0.3f, 0.7f, 1);
		
		RawModel model = OBJLoader.loadObjModel("res/stall.obj");
		
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(Loader.loadTexture("/stallTexture.png")));
		ModelTexture texture = staticModel.getTexture();
		texture.setShineDamper(8);
		texture.setReflectivity(1f);
		
		entity = new Entity(staticModel, new Vector3f(0,0,-25),0,0,0,1);
		light = new Light(new Vector3f(0,2000,0), 100, 2300, new Vector3f(1,1,1), null);
		camera = new Camera();
		
		terrain = new Terrain(0, -1, new ModelTexture(Loader.loadTexture("/grass.png")));
		
		game.start();
	}
	
	public static void main(String[] args) {
		new Main();
	}


}
