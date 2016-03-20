package engine.graphics.renderers;

import static org.lwjgl.opengl.GL11.*;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import engine.Game;
import engine.graphics.ShaderProgram;
import engine.graphics.font.DynamicFont;
import engine.graphics.font.DynamicText;
import engine.graphics.font.FontCharacter;
import engine.graphics.models.RawModel;
import engine.graphics.shaders.FontShader;
import engine.utils.maths.Matrix4f;
import engine.utils.maths.Vector2f;
import engine.utils.maths.Vector3f;

public class FontRenderer {
	
	public static void init() {
		float right = Game.current.getWindow().getWidth();
		float top = Game.current.getWindow().getHeight();
		float bottom = 0;
		float left = 0;
		float far = 1;
		float near = -1;
		
		Matrix4f ortho = new Matrix4f();
		ortho.setIdentity();
		ortho.m00 = 2.0f/(right - left);
		ortho.m11 = 2.0f/(top - bottom);
		ortho.m22 = -2.0f/(far - near);
		ortho.m30 = -(right + left)/(right - left);
		ortho.m31 = -(top + bottom)/(top - bottom);
		ortho.m32 = -(far + near)/(far - near);
		
		FontShader.inst.start();
		FontShader.inst.loadProjectionMatrix(ortho);
		ShaderProgram.stopShaders();
	}
	
	public static void renderDynamicText(DynamicText text) {
		renderDynamicText(text.text, text.font, text.pos, text.color, text.fontSize, text.width, text.edge);
	}
	
	public static void renderDynamicText(String text, DynamicFont font, Vector2f pos, Vector3f color, float fontSize) {
		renderDynamicText(text, font, pos, color, fontSize, 0.5f, 0.1f);
	}	
	
	public static void renderDynamicText(Map<DynamicFont, List<DynamicText>> dynamicTexts) {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		for(DynamicFont font:dynamicTexts.keySet()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.texID);
			List<DynamicText> batch = dynamicTexts.get(font);
			for(DynamicText text:batch) {
				renderDynamicTextMaster(text.text, text.font, text.pos, text.color, text.fontSize, text.width, text.edge);
			}
		}
		
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	private static void renderDynamicTextMaster(String text, DynamicFont font, Vector2f pos, Vector3f color, float fontSize, float width, float edge) {
		float size = fontSize/font.fontSize;
		
		char[] chars = new char[text.length()];
		text.getChars(0, text.length(), chars, 0);
		
		FontShader.inst.loadColor(color);
		FontShader.inst.loadTextSettings(width, edge);
		
		float cursorx = 0;
		
		for(char c:chars) {
			RawModel model = font.getCharModel(c);
			FontCharacter fontChar = font.chars.get((int) c);
			
			FontShader.inst.loadTranslation(new Vector2f(pos.x + cursorx + fontChar.xo * size, pos.y - fontChar.yo * size));
			FontShader.inst.loadScale(new Vector2f(fontChar.size.x * size, fontChar.size.y * size));
			cursorx += fontChar.xa*size;
			
			GL30.glBindVertexArray(model.getVaoID());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL30.glBindVertexArray(0);
		}
	}
	
	public static void renderDynamicText(String text, DynamicFont font, Vector2f pos, Vector3f color, float fontSize, float width, float edge) {
		FontShader.inst.start();
		
		float size = fontSize/font.fontSize;
		
		char[] chars = new char[text.length()];
		text.getChars(0, text.length(), chars, 0);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.texID);
		
		FontShader.inst.loadColor(color);
		FontShader.inst.loadTextSettings(width, edge);
		
		float cursorx = 0;
		
		for(char c:chars) {
			RawModel model = font.getCharModel(c);
			FontCharacter fontChar = font.chars.get((int) c);
			
			FontShader.inst.loadTranslation(new Vector2f(pos.x + cursorx + fontChar.xo * size, pos.y - fontChar.yo * size));
			FontShader.inst.loadScale(new Vector2f(fontChar.size.x * size, fontChar.size.y * size));
			cursorx += fontChar.xa*size;
			
			GL30.glBindVertexArray(model.getVaoID());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL30.glBindVertexArray(0);
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		ShaderProgram.stopShaders();
	}
	
}