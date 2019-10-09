package com.zionscape.server.tick;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A class that manages <code>Tickable</code>s for a specific <code>GameEngine</code>.
 *
 * @author Michael Bull
 * @author Lmctruck30
 * @version 1.1
 */
public class TickManager {

	/**
	 * The list of tickables.
	 */
	private List<Tick> ticks = new CopyOnWriteArrayList<Tick>();

	/**
	 * @return The tickables.
	 */
	public List<Tick> getTickables() {
		return ticks;
	}

	/**
	 * Cleans up the tick List
	 *
	 * @param tickable to remove
	 */
	public void cleanUp() {
		for (Tick t : ticks) {
			if (!t.isRunning()) {
				ticks.remove(t);
			}
		}
	}

	/**
	 * Gets the size of the ticks List
	 *
	 * @return This list Size
	 */
	public int getTickSize() {
		return ticks.size();
	}

	/**
	 * Submits a new tickable to the <code>GameEngine</code>.
	 *
	 * @param tickable The tickable to submit.
	 */
	public void submit(final Tick tick) {
		ticks.add(tick);
	}
}
