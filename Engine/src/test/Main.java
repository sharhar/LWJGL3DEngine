package test;

import engine.utils.Loader;
import engine.utils.ModelData;
import engine.utils.OBJFileLoader;
import engine.utils.maths.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import engine.Game;
import engine.Loop;
import engine.Window;
import engine.entities.Camera;
import engine.entities.Entity;
import engine.entities.Light;
import engine.graphics.MasterRenderer;
import engine.graphics.models.TexturedModel;
import engine.graphics.textures.ModelTexture;
import engine.terrain.Terrain;

public class Main implements Loop{

	Game game;
	List<Entity> stuff = new ArrayList<Entity>();
	Light light;
	Camera camera;
	List<Terrain> terrains = new ArrayList<Terrain>();
	
	public void run() {
		camera.move();
		MasterRenderer.addTerrains(terrains);
		MasterRenderer.addEntitys(stuff);
		
		MasterRenderer.renderScene(light, camera);
	}

	public void stop() {
		
	}
	
	public Main() {
		Window window = new Window("Game", 1280, 720, true, false);
		game = new Game(window, this);
		game.setSkyColor(1, 1, 1);
		
		light = new Light(new Vector3f(3000,2000,0), 100, 2300, new Vector3f(1,1,1), null);
		camera = new Camera();
		
		terrains.add(new Terrain(0, -1, new ModelTexture(Loader.loadTexture("/grass.png"))));
		terrains.add(new Terrain(-1, -1, new ModelTexture(Loader.loadTexture("/grass.png"))));
		
		ModelData treeOBJ = OBJFileLoader.loadOBJ("res/tree.obj");
		ModelData fernOBJ = OBJFileLoader.loadOBJ("res/fern.obj");
		ModelData grassOBJ = OBJFileLoader.loadOBJ("res/grassModel.obj");
		
		TexturedModel treeModel = new TexturedModel(Loader.loadToVAO(treeOBJ.getVertices(), treeOBJ.getTextureCoords(), treeOBJ.getNormals(), treeOBJ.getIndices()), new ModelTexture(Loader.loadTexture("/tree.png")));
		TexturedModel fernModel = new TexturedModel(Loader.loadToVAO(fernOBJ.getVertices(), fernOBJ.getTextureCoords(), fernOBJ.getNormals(), fernOBJ.getIndices()), new ModelTexture(Loader.loadTexture("/fern.png")));
		TexturedModel grassModel = new TexturedModel(Loader.loadToVAO(grassOBJ.getVertices(), grassOBJ.getTextureCoords(), grassOBJ.getNormals(), grassOBJ.getIndices()), new ModelTexture(Loader.loadTexture("/grassTexture.png")));
		fernModel.getTexture().setHasTransparency(true);
		grassModel.getTexture().setHasTransparency(true);
		grassModel.getTexture().setUseFalseLighting(true);
		
		Random random = new Random();
		
		for(int i = 0; i < 100;i++) {
			stuff.add(new Entity(treeModel, new Vector3f(random.nextInt(400)-200,0,-random.nextInt(200)), 0, random.nextFloat()*360, 0, 3f));
		}
		
		for(int i = 0; i < 100;i++) {
			stuff.add(new Entity(fernModel, new Vector3f(random.nextInt(400)-200,0,-random.nextInt(200)), 0, random.nextFloat()*360, 0, 0.5f));
		}
		
		for(int i = 0; i < 100;i++) {
			stuff.add(new Entity(grassModel, new Vector3f(random.nextInt(400)-200,0,-random.nextInt(200)), 0, random.nextFloat()*360, 0, 1f));
		}
		
		game.start();
	}
	
	public static void main(String[] args) {
		new Main();
	}


}
