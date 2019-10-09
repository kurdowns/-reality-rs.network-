package com.zionscape.server.gamecycle;

public interface GameCycleTask {

	/**
	 * Code which should be ran when the event is executed
	 *
	 * @param container
	 */
	void execute(GameCycleTaskContainer container);

	/**
	 * Code which should be ran when the event stops
	 */
	default void stop() {

	}

}
