package engine.graphics.font;

import java.util.HashMap;
import java.util.Map;

import engine.graphics.models.RawModel;
import engine.utils.Loader;

public class DynamicFont extends Font{
	
	public Map<Integer, RawModel> charModels = new HashMap<Integer, RawModel>();
	
	public static float[] vertices = {
			1, -1, 	// rd
			0, -1, // ld
			0, 0, 	// lu
			
			0, 0, 	// lu
			1, 0, 	// ru
			1, -1};	// rd
	
	public DynamicFont(Font font) {
		super(font.texID, font.imageWidth, font.imageHeight, font.fontSize);
		this.chars = font.chars;
		
		for(Integer id:chars.keySet()) {
			charModels.put(id, getModelFromCharacter(chars.get(id)));
		}
	}
	
	public RawModel getCharModel(char c) {
		return charModels.get((int) c);
	}
	
	private static RawModel getModelFromCharacter(FontCharacter character) {
		float[] texCoords = {
				character.tdr.x, character.tdr.y,
				character.tdl.x, character.tdl.y,
				character.tul.x, character.tul.y,
				
				character.tul.x, character.tul.y,
				character.tur.x, character.tur.y,
				character.tdr.x, character.tdr.y
		};
		
		return Loader.loadToVAO(vertices, texCoords);
	}
}
