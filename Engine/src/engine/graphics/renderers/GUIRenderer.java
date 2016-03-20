package engine.graphics.renderers;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import engine.graphics.models.RawModel;
import engine.graphics.shaders.GUIShader;
import engine.objects.GUIObject;
import engine.utils.Loader;
import engine.utils.maths.Maths;
import engine.utils.maths.Matrix4f;

public class GUIRenderer {
	
	public static RawModel quadModel = null;
	
	public static void init() {
		float[] verts = {
				-1,  1,
				-1, -1,
				 1,  1,
				 1, -1
		};
		
		quadModel = Loader.loadToVAO(verts, 2);
	}
	
	
	public static void render(List<GUIObject> guis) {
		GL30.glBindVertexArray(quadModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		
		for(GUIObject gui: guis) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getID());
			Matrix4f transformation = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
			GUIShader.inst.loadTransformationMatrix(transformation);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quadModel.getVertexCount());
		}
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
}
