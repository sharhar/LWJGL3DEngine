package engine.objects.cameras;

import engine.objects.Player;

public abstract class PlayerCamera extends Camera{
	protected Player player;
	
	public PlayerCamera (Player player) {
		this.player = player;
	}
}
