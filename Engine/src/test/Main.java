package test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.*;

import engine.Game;
import engine.Loop;
import engine.Window;
import engine.entities.Entity;
import engine.entities.Light;
import engine.entities.Player;
import engine.entities.ThirdPersonCamera;
import engine.graphics.MasterRenderer;
import engine.graphics.models.ModelUtils;
import engine.graphics.models.TexturedModel;
import engine.guis.GUIObject;
import engine.input.Keyboard;
import engine.terrain.Terrain;
import engine.terrain.TerrainTexturePack;
import engine.utils.Loader;
import engine.utils.maths.Vector2f;
import engine.utils.maths.Vector3f;

public class Main implements Loop{

	Game game;
	List<Entity> stuff = new ArrayList<Entity>();
	Player player;
	Light light;
	ThirdPersonCamera camera;
	Terrain terrain;
	List<GUIObject> guis = new ArrayList<GUIObject>();
	
	public void run() {
		float move = 0;
		Vector3f rot = new Vector3f();
		
		if(Keyboard.isKeyDown(GLFW.GLFW_KEY_UP)) {
			move = -100;
		} 
		
		if(Keyboard.isKeyDown(GLFW.GLFW_KEY_DOWN)) {
			move = 100;
		}
		
		if(Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT)) {
			rot.y = 160;
		} 
		
		if(Keyboard.isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
			rot.y = -160;
		}
		
		if(Keyboard.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
			player.jump(5,1);
		}
		
		player.move(move);
		player.rot(rot);
		player.tick(terrain);
		
		camera.move();
		
		MasterRenderer.addEntity(player);
		MasterRenderer.addTerrain(terrain);
		MasterRenderer.addEntitys(stuff);
		MasterRenderer.addGUIs(guis);
		MasterRenderer.renderScene(light, camera);
	}

	public void stop() {
		
	}
	
	public Main() {
		Window window = new Window("Game", 1280, 720, true, false, 8);
		game = new Game(window, this);
		glEnable(GL13.GL_MULTISAMPLE);
		
		MasterRenderer.setSkyColor(0.2f,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 1, 1);
		MasterRenderer.setFogSettings(0.002f, 20);
		
		light = new Light(new Vector3f(3000,2000,0), 100, 2300, new Vector3f(1,1,1), null);
		
		stuff.add(new Entity(ModelUtils.loadModel("res/house.obj", "/house.png"), new Vector3f(0, 3f, 30), 0, 0, 0, 1));
		stuff.get(stuff.size()-1).setRotOff(new Vector3f(0, 90, 0));
		
		player = new Player(ModelUtils.loadModel("res/trump.obj", "/trump.png"), new Vector3f(0 , 0, -5), 0, 0, 0, 0.5f);
		player.setRotOff(new Vector3f(0, 90, 0));
		
		camera = new ThirdPersonCamera(player);
		
		String[] texPaths = {"/grass.png", "/mud.png", "/grassFlowers.png", "/path.png", "/BlendMap.png"};
		TerrainTexturePack pack = new TerrainTexturePack(texPaths);
		
		terrain = new Terrain(pack, "res/terrainMap.png", 200, 15);
		terrain.addTile(-0.5f, -1);
		
		TexturedModel treeModel = ModelUtils.loadModel("res/tree.obj", "/tree.png");
		
		Random random = new Random();
		
		for(int i = 0; i < 100;i++) {
			Vector3f pos = new Vector3f(random.nextInt(400)-200,0,-random.nextInt(200));
			stuff.add(new Entity(treeModel, pos, 0, random.nextFloat()*360, 0, 2.5f));
		}
		
		
		
		guis.add(new GUIObject(Loader.loadTexture("/guiTest.png"), new Vector2f(0, 0.75f), new Vector2f(0.25f, 0.25f)));
		
		game.start();
	}
	
	public static void main(String[] args) {
		new Main();
	}


}
