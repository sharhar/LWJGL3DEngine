package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.glfw.GLFW;

import engine.Game;
import engine.Window;
import engine.graphics.MasterRenderer;
import engine.graphics.models.ModelUtils;
import engine.graphics.models.TexturedModel;
import engine.graphics.renderers.WaterRenderer;
import engine.graphics.textures.TerrainTexturePack;
import engine.guis.GUIObject;
import engine.input.Keyboard;
import engine.objects.Entity;
import engine.objects.Light;
import engine.objects.Player;
import engine.objects.cameras.ThirdPersonCamera;
import engine.objects.terrain.Terrain;
import engine.objects.water.WaterTile;
import engine.utils.maths.Vector3f;

public class Main{
	Game game;
	List<Entity> stuff = new ArrayList<Entity>();
	Player player;
	Light light;
	ThirdPersonCamera camera;
	Terrain terrain;
	List<GUIObject> guis = new ArrayList<GUIObject>();
	List<WaterTile> waters = new ArrayList<WaterTile>();
	
	public void playerTick() {
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
	}
	
	public void init() {
		light = new Light(new Vector3f(2000,3000,-100), new Vector3f(1, 1, 1));
		
		player = new Player(ModelUtils.loadModel("res/trump.obj", "/trump.png"), new Vector3f(0 , 0, -20), 0, 0, 0, 0.5f);
		player.setRotOff(new Vector3f(0, 90, 0));
		
		camera = new ThirdPersonCamera(player);
		
		String[] texPaths = {"/grass.png", "/mud.png", "/grassFlowers.png", "/path.png", "/BlendMap.png"};
		TerrainTexturePack pack = new TerrainTexturePack(texPaths);
		
		terrain = new Terrain(pack, "res/terrainMap.png", 200, 15);
		terrain.addTile(-0.5f, -1);
		
		waters.add(new WaterTile(0, -100, -1));
		
		TexturedModel treeModel = ModelUtils.loadModel("res/tree.obj", "/tree.png");
		
		Random random = new Random();
		
		for(int i = 0; i < 100;i++) {
			Vector3f pos = new Vector3f(random.nextInt(200)-100,0,-random.nextInt(200));
			if(pos.x > 80 || pos.x < -80 || pos.z > -10 || pos.z < -180) {
				stuff.add(new Entity(treeModel, pos, 0, random.nextFloat()*360, 0, 2.5f));
			} else {
				i--;
			}
		}
	}
	
	public Main() {
		//Game.setLights(2);
		Window window = new Window("Game", 1280, 720, true, true, 2);
		game = new Game(window);
		init();
		game.setCamera(camera);
		
		game.setLoop(() -> {
			playerTick();
			
			camera.move();
			
			MasterRenderer.addEntity(player);
			MasterRenderer.addTerrain(terrain);
			MasterRenderer.addEntitys(stuff);
			MasterRenderer.addLight(light);
			MasterRenderer.addWaters(waters);
		});
		
		game.start();
	}
	
	public static void main(String[] args) {
		new Main();
	}


}
