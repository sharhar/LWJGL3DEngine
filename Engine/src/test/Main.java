package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.glfw.GLFW;

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
import engine.input.Keyboard;
import engine.terrain.Terrain;
import engine.terrain.TerrainTexturePack;
import engine.utils.maths.Vector3f;

public class Main implements Loop{

	Game game;
	List<Entity> stuff = new ArrayList<Entity>();
	Player player;
	Light light;
	ThirdPersonCamera camera;
	Terrain terrain;
	
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
		MasterRenderer.renderScene(light, camera);
	}

	public void stop() {
		
	}
	
	public Main() {
		Window window = new Window("Game", 1280, 720, true, false);
		game = new Game(window, this);
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
		
		terrain = new Terrain(pack, "res/heightmap.png");
		terrain.addTile( 0, -1);
		terrain.addTile(-1, -1);
		terrain.addTile( 0,  0);
		terrain.addTile(-1,  0);
		
		TexturedModel treeModel = ModelUtils.loadModel("res/tree.obj", "/tree.png");
		TexturedModel monkeyModel = ModelUtils.loadModel("res/mokeyTree.obj", "/monkeyTree.png");
		
		Random random = new Random();
		
		for(int i = 0; i < 100;i++) {
			Vector3f pos = new Vector3f(random.nextInt(400)-200,0,-random.nextInt(200));
			stuff.add(new Entity(treeModel, pos, 0, random.nextFloat()*360, 0, 1f));
		}
		
		for(int i = 0; i < 100;i++) {
			Vector3f pos = new Vector3f(random.nextInt(400)-200,0,-random.nextInt(200));
			stuff.add(new Entity(monkeyModel, pos, 0, random.nextFloat()*360, 0, 0.8f));
		}
		
		game.start();
	}
	
	public static void main(String[] args) {
		new Main();
	}


}
