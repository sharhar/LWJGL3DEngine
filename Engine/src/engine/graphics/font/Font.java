package engine.graphics.font;

import java.util.HashMap;
import java.util.Map;

public class Font {
	public Map<Integer, FontCharacter> chars = new HashMap<Integer, FontCharacter>();
	public int texID, imageWidth, imageHeight, fontSize;
	
	public Font(int texID, int imageWidth, int imageHeight, int fontSize) {
		this.texID = texID;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.fontSize = fontSize;
	}
	
	public boolean equals(Font other) {
		return other.texID == texID;
	}
}
