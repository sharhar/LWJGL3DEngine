package engine.objects;

import engine.graphics.models.TexturedModel;
import engine.objects.terrain.Terrain;
import engine.utils.Time;
import engine.utils.maths.Maths;
import engine.utils.maths.Vector3f;

public class Player extends Entity{

	private boolean isInAir = false;
	private boolean forceJump = false;
	private float jumpTime = 0;
	private float jumpHeight = 0;
	private float jumpTotalTime = 0;
	private boolean jump = false;
	private boolean jumpInited = false;
	private float yOff = 0;
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public void setJumpSettings(float height, float totalTime) {
		jumpHeight = height;
		jumpTotalTime = totalTime;
		jumpInited = true;
	}
	
	public void jump() {
		if(!isInAir || forceJump) {
			jumpTime = 0;
			jump = true;
			isInAir = true;
			jumpInited = true;
		}
	}
	
	public void jump(float height, float totalTime) {
		if(!isInAir || forceJump) {
			jumpHeight = height;
			jumpTotalTime = totalTime;
			jumpTime = 0;
			jump = true;
			isInAir = true;
			jumpInited = true;
		}
	}
	
	public void setForceJump(boolean force) {
		forceJump = force;
	}
	
	public void tick(Terrain terrain) {
		if(!jumpInited) {
			setJumpSettings(5, 2);
		}
		
		jumpTime += Time.deltaTime;
		
		if(jump) {
			float fall = Maths.jumpHeightCalc(jumpHeight, jumpTotalTime, jumpTime) * Time.deltaTime;
			//System.out.println("Fall = " + fall);
			//System.out.println("Total = " + jumpTotalTime);
			//System.out.println("Height = " + jumpHeight);
			//System.out.println("Time = " + jumpTime);
			yOff += fall;
			
			if(jumpTime > jumpTotalTime) {
				jump = false;
				isInAir = false;
				yOff = 0;
			}
		}
		
		float terrainHeight = terrain.getHeightAt(position);
		//if(position.y < terrainHeight) {
			position.y = terrainHeight;
		//	isInAir = false;
		//}
		
		//if(!jump && !isInAir) {
		//	jumpTime = jumpTotalTime/2f;
		//	isInAir = true;
		//	yOff = 0;
		//}
		
		//if(isInAir && position.y + yOff <= terrainHeight) {
		//	position.y = terrainHeight;
		//	isInAir = false;
		//}
	}
	
	@Override
	public Vector3f getPosition() {
		return new Vector3f(position.x ,position.y + yOff, position.z);
	}
	
	public void move(float vel) {
		Vector3f rotVel = new Vector3f();
		double angle = Math.toRadians(rotY);
		rotVel.x = (float) (Math.sin(angle)*vel);
		rotVel.z = (float) (Math.cos(angle)*vel);
		super.move(rotVel);
	}
}
