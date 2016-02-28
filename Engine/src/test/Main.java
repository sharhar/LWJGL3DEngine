package test;

import engine.utils.Loader;
import engine.utils.ModelData;
import engine.utils.OBJFileLoader;
import engine.utils.maths.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.glfw.GLFW;

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
import engine.input.Keyboard;
import engine.terrain.Terrain;
import engine.terrain.TerrainTexture;
import engine.terrain.TerrainTexturePack;

public class Main implements Loop{

	Game game;
	List<Entity> stuff = new ArrayList<Entity>();
	Entity player;
	Light light;
	Camera camera;
	List<Terrain> terrains = new ArrayList<Terrain>();
	
	public void run() {
		camera.move();
		
		Vector3f move = new Vector3f();
		Vector3f rot = new Vector3f();
		
		if(Keyboard.isKeyDown(GLFW.GLFW_KEY_UP)) {
			move.z = -10;
		} if(Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT)) {
			rot.y = 160;
		} if(Keyboard.isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
			rot.y = -160;
		}
		
		player.move(move);
		player.rot(rot);
		
		MasterRenderer.addEntity(player);
		MasterRenderer.addTerrains(terrains);
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
		camera = new Camera();
		
		ModelData data = OBJFileLoader.loadOBJ("res/house.obj");
		RawModel rawModel = Loader.loadToVAO(data);
		ModelTexture texture = new ModelTexture(Loader.loadTexture("/house.png"));
		TexturedModel model = new TexturedModel(rawModel, texture);
		stuff.add(new Entity(model, new Vector3f(0, 3f, 30), 0, 0, 0, 3));
		stuff.get(stuff.size()-1).setRotOff(new Vector3f(0, 90, 0));
		
		ModelData playerData = OBJFileLoader.loadOBJ("res/trump.obj");
		RawModel playerRawModel = Loader.loadToVAO(playerData);
		ModelTexture playerTexture = new ModelTexture(Loader.loadTexture("/trump.png"));
		TexturedModel playerModel = new TexturedModel(playerRawModel, playerTexture);
		player = new Entity(playerModel, new Vector3f(0 , 0, -5), 0, 0, 0, 1);
		player.setRotOff(new Vector3f(0, 90, 0));
		
		TerrainTexturePack pack = new TerrainTexturePack(
				new TerrainTexture(Loader.loadTexture("/grass.png")), 
				new TerrainTexture(Loader.loadTexture("/mud.png")), 
				new TerrainTexture(Loader.loadTexture("/grassFlowers.png")), 
				new TerrainTexture(Loader.loadTexture("/path.png")), 
				new TerrainTexture(Loader.loadTexture("/BlendMap.png")));
		
		terrains.add(new Terrain(0, -1, pack));
		terrains.add(new Terrain(-1, -1, pack));
		terrains.add(new Terrain(0, 0, pack));
		terrains.add(new Terrain(-1, 0, pack));
		
		ModelData treeOBJ = OBJFileLoader.loadOBJ("res/tree.obj");
		ModelData monkeyOBJ = OBJFileLoader.loadOBJ("res/mokeyTree.obj");
		//ModelData grassOBJ = OBJFileLoader.loadOBJ("res/grassModel.obj");
		
		TexturedModel treeModel = new TexturedModel(Loader.loadToVAO(treeOBJ.getVertices(), treeOBJ.getTextureCoords(), treeOBJ.getNormals(), treeOBJ.getIndices()), new ModelTexture(Loader.loadTexture("/tree.png")));
		TexturedModel monkeyModel = new TexturedModel(Loader.loadToVAO(monkeyOBJ.getVertices(), monkeyOBJ.getTextureCoords(), monkeyOBJ.getNormals(), monkeyOBJ.getIndices()), new ModelTexture(Loader.loadTexture("/monkeyTree.png")));
		//TexturedModel grassModel = new TexturedModel(Loader.loadToVAO(grassOBJ.getVertices(), grassOBJ.getTextureCoords(), grassOBJ.getNormals(), grassOBJ.getIndices()), new ModelTexture(Loader.loadTexture("/grassTexture.png")));
		//fernModel.getTexture().setHasTransparency(true);
		//grassModel.getTexture().setHasTransparency(true);
		//grassModel.getTexture().setUseFalseLighting(true);
		
		Random random = new Random();
		
		for(int i = 0; i < 100;i++) {
			Vector3f pos = new Vector3f(random.nextInt(400)-200,0,-random.nextInt(200));
			stuff.add(new Entity(treeModel, pos, 0, random.nextFloat()*360, 0, 3f));
		}
		
		for(int i = 0; i < 100;i++) {
			Vector3f pos = new Vector3f(random.nextInt(400)-200,0,-random.nextInt(200));
			stuff.add(new Entity(monkeyModel, pos, 0, random.nextFloat()*360, 0, 2f));
		}
		
		//for(int i = 0; i < 100;i++) {
		//	stuff.add(new Entity(grassModel, new Vector3f(random.nextInt(400)-200,0,-random.nextInt(200)), 0, random.nextFloat()*360, 0, 0.7f));
		//}
		
		game.start();
	}
	
	public static void main(String[] args) {
		new Main();
	}


}
