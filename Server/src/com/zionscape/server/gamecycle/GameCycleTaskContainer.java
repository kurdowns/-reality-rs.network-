package com.zionscape.server.gamecycle;

import com.zionscape.server.model.players.Player;

/**
 * The wrapper for our event
 *
 * @author Stuart <RogueX>
 */
public final class GameCycleTaskContainer {

	/**
	 * Event owner
	 */
	private final Object owner;
	/**
	 * The actual event
	 */
	private final GameCycleTask event;
	/**
	 * Is the event running or not
	 */
	private boolean isRunning;
	/**
	 * The amount of cycles per event execution
	 */
	private int tick;
	/**
	 * The current amount of cycles passed
	 */
	private int cyclesPassed;

	/**
	 * Event name
	 */
	private String name;

	/**
	 *
	 */
	private boolean stopUponWalking;

	/**
	 * Sets the event containers details
	 *
	 * @param owner , the owner of the event
	 * @param event , the actual event to run
	 * @param tick  , the cycles between execution of the event
	 */
	public GameCycleTaskContainer(final Object owner, final GameCycleTask event, final int tick) {
		this.owner = owner;
		this.event = event;
		this.isRunning = true;
		this.cyclesPassed = 0;
		this.tick = tick;
	}

	/**
	 * Execute the contents of the event
	 */
	public void execute() {
		try {
			event.execute(this);
		} catch (Exception e) {
			e.printStackTrace();
			stop();
		}
	}

	/**
	 * Stop the event from running
	 */
	public void stop() {
		isRunning = false;
		event.stop();
	}

	public void stopExecution() {
		isRunning = false;
	}

	/**
	 * Does the event need to be ran?
	 *
	 * @return true yes false no
	 */
	public boolean needsExecution() {
		if (++this.cyclesPassed >= tick) {
			this.cyclesPassed = 0;
			return true;
		}
		return false;
	}

	/**
	 * Returns the owner of the event
	 *
	 * @return
	 */
	public Object getOwner() {
		return owner;
	}

	/**
	 * Returns the owner of this event as a <code>Player</code>
	 *
	 * @return
	 */
	public Player getPlayer() {
		return (Player) owner;
	}

	/**
	 * Is the event running?
	 *
	 * @return true yes false no
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Set if or not this event is running
	 *
	 * @param running
	 */
	public void setRunning(boolean running) {
		this.isRunning = running;
	}

	/**
	 * Set the amount of cycles between the execution
	 *
	 * @param tick
	 */
	public void setTick(final int tick) {
		this.tick = tick;
	}

	/**
	 * Get the event name
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public GameCycleTaskContainer setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * @return
	 */
	public GameCycleTask getEvent() {
		return event;
	}

	/**
	 * @return
	 */
	public boolean stopUponWalking() {
		return stopUponWalking;
	}

	/**
	 * @param stopUponWalking
	 */
	public GameCycleTaskContainer setStopUponWalking(boolean stopUponWalking) {
		this.stopUponWalking = stopUponWalking;
		return this;
	}

}