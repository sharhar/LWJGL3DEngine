package engine.graphics;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glDisable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import engine.Game;
import engine.entities.Camera;
import engine.entities.Entity;
import engine.entities.Light;
import engine.graphics.models.TexturedModel;
import engine.guis.GUIObject;
import engine.guis.GUIRenderer;
import engine.guis.GUIShader;
import engine.shaders.ShaderProgram;
import engine.shaders.StaticShader;
import engine.terrain.Terrain;
import engine.terrain.TerrainRenderer;
import engine.terrain.TerrainShader;
import engine.utils.maths.Matrix4f;

public class MasterRenderer {
	
	public static Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	public static List<Terrain> terrains = new ArrayList<Terrain>();
	public static List<GUIObject> guis = new ArrayList<GUIObject>();
	
	private static Matrix4f projectionMatrix;
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	public static void renderScene(Light light, Camera camera) {
		prepare();
		StaticShader.basicShader.start();
		StaticShader.basicShader.loadLight(light);
		StaticShader.basicShader.loadViewMatrix(camera);
		EntityRenderer.render(entities);
		ShaderProgram.stopShaders();
		entities.clear();
		
		TerrainShader.terrainShader.start();
		TerrainShader.terrainShader.loadLight(light);
		TerrainShader.terrainShader.loadViewMatrix(camera);
		TerrainRenderer.render(terrains);
		ShaderProgram.stopShaders();
		terrains.clear();
		
		GUIShader.inst.start();
		GUIRenderer.render(guis);
		ShaderProgram.stopShaders();
		guis.clear();
	}
	
	public static void enableCulling() {
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
	}
	
	public static void setFogSettings(float density, float gradient) {
		StaticShader.basicShader.start();
		StaticShader.basicShader.loadFogSettings(density, gradient);
		ShaderProgram.stopShaders();
		
		TerrainShader.terrainShader.start();
		TerrainShader.terrainShader.loadFogSettings(density, gradient);
		ShaderProgram.stopShaders();
	}
	
	public static void setSkyColor(float r, float g, float b) {
		glClearColor(r,g,b,1);
		StaticShader.basicShader.start();
		StaticShader.basicShader.setSkyColor(r, g, b);
		ShaderProgram.stopShaders();
		
		TerrainShader.terrainShader.start();
		TerrainShader.terrainShader.setSkyColor(r, g, b);
		ShaderProgram.stopShaders();
	}
	
	public static void disableCulling() {
		glDisable(GL_CULL_FACE);
	}
	
	public static void addTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public static void addTerrains(List<Terrain> terrain) {
		terrains.addAll(terrain);
	}
	
	public static void addGUI(GUIObject gui) {
		guis.add(gui);
	}
	
	public static void addGUIs(List<GUIObject> gui) {
		guis.addAll(gui);
	}
	
	public static void addEntity(Entity entity) {
		TexturedModel model = entity.getModel();
		List<Entity> batch = entities.get(model);
		if(batch!=null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(model, newBatch);
		}
	}
	
	public static void addEntitys(List<Entity> entitys) {
		for(Entity ent:entitys) {
			addEntity(ent);
		}
	}
	
	public static void prepare() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public static void init() {
		createProjectionMatrix();
		EntityRenderer.init(projectionMatrix);
		TerrainRenderer.init(projectionMatrix);
		GUIRenderer.init();
	}
	
	private static void createProjectionMatrix(){
		float aspectRatio = (float) Game.current.getWindow().getWidth() / (float) Game.current.getWindow().getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
}
