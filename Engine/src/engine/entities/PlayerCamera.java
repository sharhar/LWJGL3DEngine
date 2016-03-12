package engine.entities;

public abstract class PlayerCamera extends Camera{
	protected Player player;
	
	public PlayerCamera (Player player) {
		this.player = player;
	}
}
