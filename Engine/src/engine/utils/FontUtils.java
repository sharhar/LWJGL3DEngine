package engine.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.graphics.font.Font;
import engine.graphics.font.FontCharacter;
import engine.utils.maths.Vector2f;

public class FontUtils {
	
	private static String getProperty(String line, String name) {
		int start = line.indexOf(name)+name.length()+1;
		int end = line.indexOf(' ', start);
		return line.substring(start, end);
	}
	
	private static int getIntProperty(String line, String name) {
		return Integer.parseInt(getProperty(line, name));
	}
	
	private static float getFloatProperty(String line, String name) {
		return Float.parseFloat(getProperty(line, name));
	}
	
	private static FontCharacter getCharacter(String line, int width, int height) {
		FontCharacter character = new FontCharacter();
		
		float wf = (float) width;
		float hf = (float) height;
		
		character.id = getIntProperty(line, "id");
		float x = getFloatProperty(line, "x")/wf;
		float y = getFloatProperty(line, "y")/hf;
		float w = getFloatProperty(line, "width");
		float h = getFloatProperty(line, "height");
		
		character.size = new Vector2f(w, h);
		w = w/wf;
		h = h/hf;
		
		character.tul = new Vector2f(x, y);
		character.tdl = new Vector2f(x, y + h);
		character.tur = new Vector2f(x + w, y);
		character.tdr = new Vector2f(x + w, y + h);
		
		character.xo = getIntProperty(line, "xoffset");
		character.yo = getIntProperty(line, "yoffset");
		character.xa = getIntProperty(line, "xadvance");
		
		return character;
	}
	
	public static Font loadFont(String path) {
		String pngPath = path.substring(3, path.length()) + ".png";
		String fntPath = path + ".fnt";
		
		BufferedImage fontImage = null;
		try {
			fontImage = ImageIO.read(Loader.class.getResourceAsStream(pngPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int fontImageWidth = fontImage.getWidth();
		int fontImageHeight = fontImage.getHeight();
		int texID = Loader.loadTexture(fontImage);
		String fntFile = FileUtils.readFile(fntPath);
		
		String[] allLines = fntFile.split("\n");
		
		boolean[] areLinesChars = new boolean[allLines.length];
		for(int i = 0; i < allLines.length;i++) {
			areLinesChars[i] = allLines[i].startsWith("char ");
		}
		
		int charLineCount = 0;
		for(boolean b:areLinesChars) {
			if(b){
				charLineCount++;
			}
		}
		
		String[] lines = new String[charLineCount];
		int charLineIndex = 0;
		for(int i = 0;i < allLines.length;i++) {
			if(areLinesChars[i]) {
				lines[charLineIndex] = allLines[i];
				charLineIndex++;
			}
		}
		
		FontCharacter[] chars = new FontCharacter[lines.length];
		for(int i = 0;i < chars.length;i++) {
			chars[i] = getCharacter(lines[i], fontImageWidth, fontImageHeight);
		}
		
		Font font = new Font(texID, fontImageHeight, fontImageHeight, getIntProperty(allLines[0], "size"));
		for(int i = 0;i < chars.length;i++) {
			font.chars.put(chars[i].id, chars[i]);
		}
		
		return font;
	}
}
