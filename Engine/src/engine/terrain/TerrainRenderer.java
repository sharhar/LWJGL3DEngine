package engine.terrain;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import engine.graphics.models.RawModel;
import engine.graphics.textures.ModelTexture;
import engine.shaders.ShaderProgram;
import engine.utils.Maths;
import engine.utils.maths.Matrix4f;
import engine.utils.maths.Vector3f;

public class TerrainRenderer {
	
	public static void init(Matrix4f projectionMatrix){
		TerrainShader.terrainShader.start();
		TerrainShader.terrainShader.loadProjectionMatrix(projectionMatrix);
		ShaderProgram.stopShaders();
	}
	
	public static void render(List<Terrain> terrains) {
		for(Terrain terrain: terrains) {
			RawModel rawModel = terrain.getModel();
			GL30.glBindVertexArray(rawModel.getVaoID());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			ModelTexture texture = terrain.getTexture();
			TerrainShader.terrainShader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTexture().getID());
			Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
			TerrainShader.terrainShader.loadTransformationMatrix(transformationMatrix);
			GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL20.glDisableVertexAttribArray(2);
			GL30.glBindVertexArray(0);
		}
	}
}
