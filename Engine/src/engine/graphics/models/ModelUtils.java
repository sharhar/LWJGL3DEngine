package engine.graphics.models;

import engine.graphics.textures.ModelTexture;
import engine.utils.Loader;
import engine.utils.ModelData;
import engine.utils.OBJFileLoader;

public class ModelUtils {
	
	public static TexturedModel loadModel(String pathToOBJ, String pathToImage) {
		ModelData modelData = OBJFileLoader.loadOBJ(pathToOBJ);
		RawModel rawModel = Loader.loadToVAO(modelData);
		int texID = Loader.loadTexture(pathToImage);
		ModelTexture texture = new ModelTexture(texID);
		return new TexturedModel(rawModel, texture);
	}
	
}
