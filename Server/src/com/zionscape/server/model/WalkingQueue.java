package com.zionscape.server.model;

import com.zionscape.server.model.content.minigames.castlewars.CastleWars;
import com.zionscape.server.model.players.Player;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A queue of {@link Direction}s which a {@link Character} will follow.
 *
 * @author Graham Edgecombe
 */
public final class WalkingQueue {

	/**
	 * The maximum size of the queue. If any additional steps are added, they
	 * are discarded.
	 */
	private static final int MAXIMUM_SIZE = 50;
	/**
	 * The character whose walking queue this is.
	 */
	private final Player character;
	/**
	 * The queue of directions.
	 */
	private final Deque<Point> points = new ArrayDeque<>();
	/**
	 * Flag indicating if this queue (only) should be ran.
	 */
	private boolean runQueue;
	/**
	 * Run toggle (button in client).
	 */
	private boolean runToggled = true;

	/**
	 * Creates a walking queue for the specified character.
	 *
	 * @param character The character.
	 */
	public WalkingQueue(Player character) {
		this.character = character;
	}

	/**
	 * Adds the first step to the queue, attempting to connect the server and
	 * client position by looking at the previous queue.
	 *
	 * @param clientConnectionPosition The first step.
	 * @return {@code true} if the queues could be connected correctly,
	 * {@code false} if not.
	 */
	public boolean addFirstStep(Location clientConnectionPosition) {
		points.clear();
		return addStep(clientConnectionPosition, true);
	}

	/**
	 * Adds a step.
	 *
	 * @param x    The x coordinate of this step.
	 * @param y    The y coordinate of this step.
	 * @param flag
	 */
	private boolean addStep(int x, int y, boolean flag) {
		if (points.size() >= MAXIMUM_SIZE)
			return false;
		final Point last = getLast();

		final int deltaX = x - last.position.getX();
		final int deltaY = y - last.position.getY();

		final Direction direction = Direction.fromDeltas(deltaX, deltaY);
		if (direction != Direction.NONE) {

			if (character.getCastleWarsState() != null && CastleWars.getBarricadeLocations()[x][y]) {
				return false;
			}

			final boolean checkStep = character instanceof Player;
			// TODO area checks for areas which do not support clipping
			boolean validStep = true;
			if (flag && checkStep) {
				validStep = checkStep(last.position, deltaX, deltaY);
			}
			if (validStep) {
				points.add(new Point(Location.create(x, y, character.getLocation().getZ()), direction));
			} else {
				character.getSprites().setSprites(Direction.NONE.toInteger(), Direction.NONE.toInteger());
			}
		}
		return true;
	}

	/**
	 * Adds a step to the queue.
	 *
	 * @param step The step to add.
	 * @oaram flag
	 */
	public boolean addStep(Location step, boolean flag) {
		final Point last = getLast();

		final int x = step.getX();
		final int y = step.getY();

		int deltaX = x - last.position.getX();
		int deltaY = y - last.position.getY();

		final int max = Math.max(Math.abs(deltaX), Math.abs(deltaY));
		for (int i = 0; i < max; i++) {
			if (deltaX < 0)
				deltaX++;
			else if (deltaX > 0)
				deltaX--;

			if (deltaY < 0)
				deltaY++;
			else if (deltaY > 0)
				deltaY--;

			if (!addStep(x - deltaX, y - deltaY, flag)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks walk steps
	 *
	 * @param position
	 * @param diffX
	 * @param diffY
	 * @return
	 */
	public boolean checkStep(Location position, int diffX, int diffY) {

		return /*
				 * Region.getClipping(position.getX(), position.getY(),
				 * character.getHeightLevel(), diffX, diffY)
				 */true; // Gives loads of issues
	}

	/**
	 * Gets the last point.
	 *
	 * @return The last point.
	 */
	private Point getLast() {
		final Point last = points.peekLast();
		if (last == null)
			return new Point(character.getLocation(), Direction.NONE);
		return last;
	}

	/**
	 * @return true if the player is moving.
	 */
	public boolean isMoving() {
		return !points.isEmpty();
	}

	/**
	 * Checks if any running flag is set.
	 *
	 * @return <code>true</code. if so, <code>false</code> if not.
	 */
	public boolean isRunning() {
		return runToggled || runQueue;
	}

	/**
	 * Gets the running queue flag.
	 *
	 * @return The running queue flag.
	 */
	public boolean isRunningQueue() {
		return runQueue;
	}

	/**
	 * Sets the running queue flag.
	 *
	 * @param running The running queue flag.
	 */
	public void setRunningQueue(boolean running) {
		runQueue = running;
	}

	/**
	 * Gets the run toggled flag.
	 *
	 * @return The run toggled flag.
	 */
	public boolean isRunningToggled() {
		return runToggled;
	}

	/**
	 * Sets the run toggled flag.
	 *
	 * @param runToggled The run toggled flag.
	 */
	public void setRunningToggled(boolean runToggled) {
		this.runToggled = runToggled;
	}

	/**
	 * Called every pulse, updates the queue.
	 */
	public void pulse() {
		if (character.getTeleportLocation() != null) {
			reset();
			character.didTeleport = true;
			character.setLocation(character.getTeleportLocation());
			character.setTeleportLocation(null);
		} else {
			Direction first = Direction.NONE;
			Direction second = Direction.NONE;
			Point next = points.poll();
			if (next != null) {
				first = next.direction;
				Location position = next.position;
				if (runQueue || runToggled) {
					next = points.poll();
					if (next != null) {
						second = next.direction;
						position = next.position;
					}
				}
				character.setLocation(position);
			}
			character.getSprites().setSprites(first.toInteger(), second.toInteger());
		}

		/*
		 * Check for a map region change, and if the map region has
		 * changed, set the appropriate flag so the new map region packet
		 * is sent.
		 */
		int diffX = character.getLocation().getX() - character.getLastKnownRegion().getRegionX() * 8;
		int diffY = character.getLocation().getY() - character.getLastKnownRegion().getRegionY() * 8;

		boolean changed = false;
		if (diffX < 16) {
			changed = true;
		} else if (diffX >= 88) {
			changed = true;
		}
		if (diffY < 16) {
			changed = true;
		} else if (diffY >= 88) {
			changed = true;
		}
		if (changed || character.getLocation().getZ() != character.getLastKnownRegion().getZ()) {
			character.mapRegionDidChange = true;
			/*
			 * Set the map region changing flag so the new map region packet is
			 * sent upon the next update.
			 */
			// entity.setMapRegionChanging(true);
		}
	}

	/**
	 * Resets the movement.
	 */
	public void reset() {
		reset(true);
	}

	/**
	 * Resets the movement.
	 *
	 * @param removeMiniflag
	 */
	public void reset(boolean removeMiniflag) {
		points.clear();
	}

	/**
	 * Gets the size of the queue.
	 *
	 * @return The size of the queue.
	 */
	public int size() {
		return points.size();
	}

	/**
	 * Represents a single point in the queue.
	 *
	 * @author Graham Edgecombe
	 */
	private static final class Point {

		/**
		 * The point's position.
		 */
		private final Location position;

		/**
		 * The direction to walk to this point.
		 */
		private final Direction direction;

		/**
		 * Creates a point.
		 *
		 * @param position  The position.
		 * @param direction The direction.
		 */
		public Point(Location position, Direction direction) {
			this.position = position;
			this.direction = direction;
		}

		@Override
		public String toString() {
			return Point.class.getName() + " [direction=" + direction
					+ ", position=" + position + "]";
		}

	}

}