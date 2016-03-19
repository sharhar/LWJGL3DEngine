package engine.graphics.renderers;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import engine.graphics.ShaderProgram;
import engine.graphics.models.RawModel;
import engine.graphics.shaders.WaterShader;
import engine.objects.cameras.Camera;
import engine.objects.water.WaterTile;
import engine.utils.Loader;
import engine.utils.maths.Maths;
import engine.utils.maths.Matrix4f;
import engine.utils.maths.Vector3f;

public class WaterRenderer {
	 
    private static RawModel quad;

    public static void init(Matrix4f projectionMatrix) {
        WaterShader.inst.start();
        WaterShader.inst.loadProjectionMatrix(projectionMatrix);
        ShaderProgram.stopShaders();
        setUpVAO();
    }
 
    public static void render(List<WaterTile> water) {
    	GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        
        for (WaterTile tile : water) {
            Matrix4f modelMatrix = Maths.createTransformationMatrix(new Vector3f(tile.x, tile.height, tile.z), 0, 0, 0,WaterTile.TILE_SIZE);
           WaterShader.inst.loadModelMatrix(modelMatrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
        }
        
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }
 
    private static void setUpVAO() {
        float[] vertices = {1, -1, -1, -1, -1, 1, -1, 1, 1, 1, 1, -1};
        quad = Loader.loadToVAO(vertices, 2);
    }
 
}
