package com.zionscape.server.model.players.skills.agility;

public enum ObstacleType {

	PIPE(844, "You squeezed your way through the pipe!"),
	ROPE(751, "You successfully swing across the obstacle!"),
	BALANCING_LEDGE(756, "You manage to keep your balance!"),
	OVER_OBJECT(839, "You skillfully climb over the object!"),
	CLIMBING(828, "You manage to climb up the obstacle!"),
	STONES(741, "You manage to hop across using the stones!"),
	MONKEYBARS(745, "You manage to cross the bars!"),
	LOG(762, "You skillfully walked across the log!");

	private int animation;
	private String message;

	ObstacleType(int animation, String message) {
		this.animation = animation;
		this.message = message;
	}

	public int getAnim() {
		return animation;
	}

	public String getMessage() {
		return message;
	}

}
