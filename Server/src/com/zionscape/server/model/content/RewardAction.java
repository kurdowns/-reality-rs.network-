package com.zionscape.server.model.content;

/**
 * @author Jinrake
 */
public abstract class RewardAction {

	/**
	 * The execute method is called when the event is run. The general contract of the execute method is that it may
	 * take any action whatsoever.
	 */
	public abstract void execute();
}
