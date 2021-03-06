package engine.graphics.renderers;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import engine.graphics.ShaderProgram;
import engine.graphics.models.RawModel;
import engine.graphics.shaders.TerrainShader;
import engine.graphics.textures.TerrainTexturePack;
import engine.objects.terrain.Terrain;
import engine.objects.terrain.TerrainTile;
import engine.utils.maths.Maths;
import engine.utils.maths.Matrix4f;
import engine.utils.maths.Vector3f;

public class TerrainRenderer {
	
	public static void init(Matrix4f projectionMatrix){
		TerrainShader.inst.start();
		TerrainShader.inst.loadProjectionMatrix(projectionMatrix);
		TerrainShader.inst.connectTextureUnits();
		ShaderProgram.stopShaders();
	}
	
	public static void render(List<Terrain> terrains) {
		for(Terrain terrain: terrains) {
			RawModel rawModel = terrain.getModel();
			GL30.glBindVertexArray(rawModel.getVaoID());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			
			TerrainTexturePack texture = terrain.getPack();
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getBgTex().getTexID());
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getrTex().getTexID());
			GL13.glActiveTexture(GL13.GL_TEXTURE2);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getgTex().getTexID());
			GL13.glActiveTexture(GL13.GL_TEXTURE3);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getbTex().getTexID());
			GL13.glActiveTexture(GL13.GL_TEXTURE4);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getBlendMap().getTexID());
			TerrainShader.inst.loadShineVariables(1, 0);
			
			for(TerrainTile tile:terrain.tiles) {
				Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(tile.getX(), 0, tile.getZ()), 0, 0, 0, 1);
				TerrainShader.inst.loadTransformationMatrix(transformationMatrix);
				GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL20.glDisableVertexAttribArray(2);
			GL30.glBindVertexArray(0);
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
}
