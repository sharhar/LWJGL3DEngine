package engine.graphics.renderers;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import engine.graphics.MasterRenderer;
import engine.graphics.ShaderProgram;
import engine.graphics.models.RawModel;
import engine.graphics.models.TexturedModel;
import engine.graphics.shaders.EntityShader;
import engine.graphics.textures.ModelTexture;
import engine.objects.Entity;
import engine.utils.maths.Maths;
import engine.utils.maths.Matrix4f;
import engine.utils.maths.Vector2f;

public class EntityRenderer {
	
	public static void init(Matrix4f projectionMatrix){
		EntityShader.inst.start();
		EntityShader.inst.loadProjectionMatrix(projectionMatrix);
		ShaderProgram.stopShaders();
	}
	
	public static void render(Map<TexturedModel, List<Entity>> entities) {
		for(TexturedModel model: entities.keySet()) {
			RawModel rawModel = model.getRawModel();
			GL30.glBindVertexArray(rawModel.getVaoID());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			ModelTexture texture = model.getTexture();
			EntityShader.inst.loadNumberOfRows(texture.getNumberOfRows());
			if(texture.isHasTransparency()) {
				MasterRenderer.disableCulling();
			}
			EntityShader.inst.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
			EntityShader.inst.loadFakeLighting(texture.isUseFalseLighting());
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
			List<Entity> batch = entities.get(model);
			for(Entity entity: batch) {
				Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
						entity.getRotX() + entity.getRotOff().x, entity.getRotY() + entity.getRotOff().y, entity.getRotZ() + entity.getRotOff().z, entity.getScale());
				EntityShader.inst.loadTransformationMatrix(transformationMatrix);
				EntityShader.inst.loadTextureOffSet(new Vector2f(entity.getTextureXOffset(), entity.getTextureYOffset()));
				GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			if(texture.isHasTransparency()) {
				MasterRenderer.enableCulling();
			}
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL20.glDisableVertexAttribArray(2);
			GL30.glBindVertexArray(0);
		}
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public static void render(Entity entity) {
		TexturedModel model = entity.getModel();
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		EntityShader.inst.loadTransformationMatrix(transformationMatrix);
		ModelTexture texture = model.getTexture();
		EntityShader.inst.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

}
