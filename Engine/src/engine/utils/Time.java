package engine.utils;

public class Time {
	
	public static float deltaTime = 0;
	private static long startTime = 0;
	
	public static void tick() {
		if(startTime != 0) {
			long delta = System.nanoTime() - startTime;
			deltaTime = (float) ((delta*1.0)/1000000000L);
		}
		
		startTime = System.nanoTime();
	}
	
}
