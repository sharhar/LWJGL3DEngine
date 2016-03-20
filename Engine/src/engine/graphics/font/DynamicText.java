package engine.graphics.font;

import engine.utils.maths.Vector2f;
import engine.utils.maths.Vector3f;

public class DynamicText {
	public String text;
	public DynamicFont font;
	public Vector2f pos;
	public Vector3f color;
	public float fontSize, width, edge;
	
	public DynamicText(String text, DynamicFont font, Vector2f pos, Vector3f color, float fontSize) {
		this(text, font, pos, color, fontSize, 0.5f, 0.1f);
	}
	
	public DynamicText(String text, DynamicFont font, Vector2f pos, Vector3f color, float fontSize, float width, float edge) {
		this.text = text;
		this.font = font;
		this.pos = pos;
		this.color = color;
		this.fontSize = fontSize;
		this.width = width;
		this.edge = edge;
	}
}
