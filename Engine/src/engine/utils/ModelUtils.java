package engine.utils;

import engine.graphics.models.RawModel;
import engine.graphics.models.TexturedModel;
import engine.graphics.textures.ModelTexture;

public class ModelUtils {
	
	public static TexturedModel loadModel(String pathToOBJ, String pathToImage) {
		ModelData modelData = OBJFileLoader.loadOBJ(pathToOBJ);
		RawModel rawModel = Loader.loadToVAO(modelData);
		int texID = Loader.loadTexture(pathToImage);
		ModelTexture texture = new ModelTexture(texID);
		return new TexturedModel(rawModel, texture);
	}
	
}
