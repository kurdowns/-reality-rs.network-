package com.zionscape.server.gamecycle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles all of our cycle based events
 *
 * @author Stuart
 */
public class GameCycleTaskHandler {

	/**
	 * Holds all of our events currently being ran
	 */
	private static final List<GameCycleTaskContainer> events = new ArrayList<>();
	private static final List<GameCycleTaskContainer> newEvents = new ArrayList<>();

	/**
	 * Add an event to the list
	 *
	 * @param owner
	 * @param event
	 * @param cycles
	 */
	public static GameCycleTaskContainer addEvent(Object owner, GameCycleTask event, int cycles) {
		return addEvent(owner, event, cycles, false);
	}

	/**
	 * Add an event to the list
	 *
	 * @param owner
	 * @param event
	 * @param cycles
	 */
	public static GameCycleTaskContainer addEvent(Object owner, GameCycleTask event, int cycles, boolean execute) {

		GameCycleTaskContainer container = new GameCycleTaskContainer(owner, event, cycles);

		if (execute) {
			container.execute();
		}

		newEvents.add(container);

		return container;
	}

	/**
	 * Does an event exist by the given name
	 *
	 * @param name
	 * @return
	 */
	public static boolean eventExists(String name) {
		return events.stream().filter(x -> x.getName() != null && x.getName().equalsIgnoreCase(name)).count() > 0;
	}

	/**
	 * Returns the amount of events currently running
	 *
	 * @return amount
	 */
	public static int getEventsCount() {
		return events.size();
	}

	/**
	 * Execute and remove events
	 */
	public static void process() {
		// add all new events to existing events
		events.addAll(newEvents);

		// clear the new events list
		newEvents.clear();

		// execute events
		events.stream().filter(x -> x.needsExecution() && x.isRunning()).forEach(x -> x.execute());

		// get a copy of only events running
		List<GameCycleTaskContainer> newEventsList = events.stream().filter(x -> x.isRunning())
				.collect(Collectors.toList());

		// clear out all events
		events.clear();

		// add back only the events running
		events.addAll(newEventsList);
	}

	/**
	 * Stops all events which are being ran by the given owner.
	 *
	 * @param owner
	 */
	public static void stopEvents(Object owner) {
		events.stream().filter(x -> x.getOwner() != null && x.getOwner().equals(owner)).forEach(GameCycleTaskContainer::stop);
	}

	/**
	 * Stop an event by its name
	 *
	 * @param name name of the event to stop
	 */
	public static void stopEvents(String name) {
		events.stream().filter(x -> x.getName() != null && x.getName().equalsIgnoreCase(name)).forEach(GameCycleTaskContainer::stop);
	}

	/**
	 * @param task
	 */
	public static void stopEvents(GameCycleTask task) {
		events.stream().filter(x -> x.getEvent().equals(task)).forEach(GameCycleTaskContainer::stop);
	}

	/**
	 * @param owner
	 */
	public static void stopEventsUponWalking(Object owner) {
		events.stream().filter(x -> x.stopUponWalking() && x.getOwner() != null && x.getOwner().equals(owner)).forEach(GameCycleTaskContainer::stop);
	}

}