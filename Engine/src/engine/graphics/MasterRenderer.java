package engine.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import engine.Game;
import engine.graphics.font.DynamicFont;
import engine.graphics.font.DynamicText;
import engine.graphics.models.TexturedModel;
import engine.graphics.renderers.EntityRenderer;
import engine.graphics.renderers.FontRenderer;
import engine.graphics.renderers.GUIRenderer;
import engine.graphics.renderers.TerrainRenderer;
import engine.graphics.renderers.WaterRenderer;
import engine.graphics.shaders.EntityShader;
import engine.graphics.shaders.FontShader;
import engine.graphics.shaders.GUIShader;
import engine.graphics.shaders.TerrainShader;
import engine.graphics.shaders.WaterShader;
import engine.objects.Entity;
import engine.objects.GUIObject;
import engine.objects.Light;
import engine.objects.cameras.Camera;
import engine.objects.terrain.Terrain;
import engine.objects.water.WaterFrameBuffers;
import engine.objects.water.WaterTile;
import engine.utils.MiscUtils;
import engine.utils.maths.Matrix4f;
import engine.utils.maths.Vector4f;

public class MasterRenderer {
	
	public static Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	public static Map<DynamicFont, List<DynamicText>> dynamicTexts = new HashMap<DynamicFont, List<DynamicText>>();
	public static List<Terrain> terrains = new ArrayList<Terrain>();
	public static List<GUIObject> guis = new ArrayList<GUIObject>();
	public static List<Light> lights = new ArrayList<Light>();
	public static List<WaterTile> waters = new ArrayList<WaterTile>();
	private static boolean water = true;
	
	public static Matrix4f projectionMatrix;
	private static final float FOV = 80;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	public static void renderScene(Camera camera, Vector4f clipPlane) {
		EntityShader.inst.start();
		EntityShader.inst.loadLights(lights);
		EntityShader.inst.loadViewMatrix(camera);
		EntityShader.inst.loadClipPlane(clipPlane);
		EntityRenderer.render(entities);
		
		TerrainShader.inst.start();
		TerrainShader.inst.loadLights(lights);
		TerrainShader.inst.loadViewMatrix(camera);
		TerrainShader.inst.loadClipPlane(clipPlane);
		TerrainRenderer.render(terrains);
		
		ShaderProgram.stopShaders();
	}
	
	public static void renderWater(Camera camera) {
		WaterShader.inst.start();
    	WaterShader.inst.loadViewMatrix(camera);
		WaterRenderer.render(waters, camera);
		
		ShaderProgram.stopShaders();
	}
	
	public static void renderGUI() {
		GUIShader.inst.start();
		GUIRenderer.render(guis);
		
		FontShader.inst.start();
		FontRenderer.renderDynamicText(dynamicTexts);
		
		ShaderProgram.stopShaders();
	}
	
	public static void clearBuffers() {
		entities.clear();
		terrains.clear();
		guis.clear();
		lights.clear();
		waters.clear();
		dynamicTexts.clear();
	}
	
	public static void enableCulling() {
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
	}
	
	public static void setFogSettings(float density, float gradient) {
		EntityShader.inst.start();
		EntityShader.inst.loadFogSettings(density, gradient);
		ShaderProgram.stopShaders();
		
		TerrainShader.inst.start();
		TerrainShader.inst.loadFogSettings(density, gradient);
		ShaderProgram.stopShaders();
	}
	
	public static void setSkyColor(float r, float g, float b) {
		glClearColor(r,g,b,1);
		EntityShader.inst.start();
		EntityShader.inst.setSkyColor(r, g, b);
		ShaderProgram.stopShaders();
		
		TerrainShader.inst.start();
		TerrainShader.inst.setSkyColor(r, g, b);
		ShaderProgram.stopShaders();
	}
	
	public static void renderWater(Camera camera, int fbo, boolean allScene) {
		if(waters.size() == 0) {
			return;
		}
		
		if (allScene) {
			WaterFrameBuffers.bindRefractionFrameBuffer();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			MasterRenderer.renderScene(camera, null);
		}
		
		for(int i = 0;i < waters.size();i++) {
			WaterFrameBuffers.bindReflectionFrameBuffer();
			float dist = 2 * (camera.getPosition().y - waters.get(i).height);
			camera.getPosition().y -= dist;
			camera.invertPitch();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			MasterRenderer.renderScene(camera, new Vector4f(0, 1, 0, -waters.get(i).height));
			camera.getPosition().y += dist;
			camera.invertPitch();
			
			if(!allScene) {
				WaterFrameBuffers.bindRefractionFrameBuffer();
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				MasterRenderer.renderScene(camera, new Vector4f(0, -1, 0, waters.get(i).height));
			}
			
			GL11.glViewport(0, 0, Game.current.getWindow().getWidth(), Game.current.getWindow().getHeight());
			
			GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
			WaterRenderer.render(waters.get(i), camera);
		}
		
		ShaderProgram.stopShaders();
	}
	
	public static void renderLight(Light light) {
		lights.add(light);
	}
	
	public static void renderLight(List<Light> light) {
		lights.addAll(light);
	}
	
	public static void disableCulling() {
		glDisable(GL_CULL_FACE);
	}
	
	public static void renderTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public static void renderTerrains(List<Terrain> terrain) {
		terrains.addAll(terrain);
	}
	
	public static void renderGUI(GUIObject gui) {
		guis.add(gui);
	}
	
	public static void renderGUIs(List<GUIObject> gui) {
		guis.addAll(gui);
	}
	
	public static void renderWater(WaterTile waterTile) {
		if(water) {
			waters.add(waterTile);
		} else {
			MiscUtils.enginePrintSeriousWarning("Water disabled! Not adding water to render buffer!");
		}
	}
	
	public static void renderWaters(List<WaterTile> waterTiles) {
		if(water) {
			waters.addAll(waterTiles);
		} else {
			MiscUtils.enginePrintSeriousWarning("Water disabled! Not adding water to render buffer!");
		}
	}
	
	public static void renderDynamicText(DynamicText text) {
		DynamicFont font = text.font;
		List<DynamicText> batch = dynamicTexts.get(font);
		if(batch != null) {
			batch.add(text);
		} else {
				List<DynamicText> newBatch = new ArrayList<DynamicText>();
				newBatch.add(text);
				dynamicTexts.put(font, newBatch);
		}
	}
	
	public static void renderEntity(Entity entity) {
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
	
	public static void renderEntitys(List<Entity> entitys) {
		for(Entity ent:entitys) {
			renderEntity(ent);
		}
	}
	
	public static void init(boolean w) {
		water = w;
		
		createProjectionMatrix();
		
		EntityRenderer.init(projectionMatrix);
		TerrainRenderer.init(projectionMatrix);
		GUIRenderer.init();
		FontRenderer.init();
		
		if(water) {
			WaterRenderer.init(projectionMatrix);
		}
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
