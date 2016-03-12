package engine.terrain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.graphics.models.RawModel;
import engine.utils.Loader;
import engine.utils.maths.Vector3f;

public class TerrainUtils {
	public  static final float SIZE = 800;
	private static final float MAX_HEIGHT = 40;
	private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
	
	public static float[][] getHeightMap(String heightPath) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(heightPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		float[][] result = new float[image.getWidth()][image.getHeight()];
		
		for(int x = 0; x < image.getWidth();x++) {
			for(int z = 0; z < image.getHeight();z++) {
				result[x][z] = getHeight(x, z, image);
			}
		}
		
		return result;
	}
	
	public static RawModel generateTerrainFromMap(float[][] heightMap) {
		int VERTEX_COUNT = heightMap.length;
		
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				float xpos = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				float zpos = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer*3] = xpos;
				vertices[vertexPointer*3+1] = heightMap[j][i];//getHeight(j, i, image);
				vertices[vertexPointer*3+2] = zpos;
				Vector3f normal = calcNormal(j, i, heightMap);
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return Loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	public static  Vector3f calcNormal(int x, int z, float[][] map) {
		float heightL = 0;
		float heightR = 0;
		float heightD = 0;
		float heightU = 0;
		
		try {
			heightL = map[x-1][z];
			heightR = map[x+1][z];
			heightD = map[x][z-1];
			heightU = map[x][z+1];
		} catch (ArrayIndexOutOfBoundsException e) {
			return new Vector3f(0, 1, 0);
		}
		
		Vector3f normal = new Vector3f(heightL-heightR, 2f, heightD-heightU);
		normal.normalize();
		
		return normal;
	}
	
	public static  float getHeight(int x, int z, BufferedImage image) {
		if(x < 0 || x > image.getHeight()-1 || z < 0 || z > image.getHeight()-1){
			return 0;
		}
		//System.out.println("X = " + x + "\tZ = " + z);
		float height = image.getRGB(x, z);
		height += MAX_PIXEL_COLOR/2f;
		height /= MAX_PIXEL_COLOR/2f;
		height *= MAX_HEIGHT;
		return height;
	}
}
