package com.zionscape.server.model.players.skills.woodcutting;

/**
 * Tree class which holds details of each tree for the skill woodcutting
 *
 * @author thispixel
 */
public final class Tree {

	private static Tree[] trees = {
			new Tree(new int[]{1276, 1278, 1279, 1280}, 1, 24, 1, 1511, 1, 1342, 8), // normal_tree
			new Tree(new int[]{1315}, 1, 25, 2, 1511, 1, 1342, 6), // evergreen
			new Tree(new int[]{1281}, 15, 37, 3, 1521, 3, 1356, 8), // oak_tree
			new Tree(new int[]{1308, 5553, 5551, 5552}, 30, 67, 5, 1519, 4, 7399, 9), // willow_tree
			new Tree(new int[]{1307}, 45, 100, 10, 1517, 5, 7400, 12), // maple_tree
			new Tree(new int[]{1309}, 60, 175, 14, 1515, 6, 7402, 20), // yew_tree
			new Tree(new int[]{1306}, 75, 250, 25, 1513, 7, 7400, 30) // magic_tree
	};

	private final int[] id;
	private final int levelRequired;
	private final int xp;
	private final int delay;
	private final int logs;
	private final int amount;
	private final int stump;
	private final int respawnTime;

	private Tree(int[] id, int level, int xp, int delay, int logs, int amount, int stump, int respawnTime) {
		this.id = id;
		this.levelRequired = level;
		this.xp = xp;
		this.delay = delay;
		this.logs = logs;
		this.amount = amount;
		this.stump = stump;
		this.respawnTime = respawnTime;
	}

	/**
	 * Get all the tree's
	 */
	public static Tree[] getTrees() {
		return trees;
	}

	/**
	 * Get a tree's variables
	 *
	 * @param tree id of the tree
	 * @return the tree object otherwise null
	 */
	public static Tree getTree(final int tree) {
		for (final Tree tempTree : trees)
			for (final int id : tempTree.getId())
				if (id == tree)
					return tempTree;
		return null;
	}

	/**
	 * Get the tree id's used by this tree
	 *
	 * @return tree ids
	 */
	public int[] getId() {
		return this.id;
	}

	/**
	 * Get the required level of this tree
	 *
	 * @return level
	 */
	public int getLevelRequired() {
		return this.levelRequired;
	}

	/**
	 * Get the xp per log cut
	 *
	 * @return xp
	 */
	public int getXp() {
		return this.xp;
	}

	/**
	 * Get the delay between getting logs
	 *
	 * @return delay
	 */
	public int getDelay() {
		return this.delay;
	}

	/**
	 * Id of the log to give to the player
	 *
	 * @return
	 */
	public int getLogs() {
		return this.logs;
	}

	/**
	 * The max amount of logs you can cut from this tree
	 *
	 * @return amount
	 */
	public int getAmount() {
		return this.amount;
	}

	/**
	 * Return the stump object id
	 *
	 * @return stump id
	 */
	public int getStump() {
		return this.stump;
	}

	/**
	 * Get the time it takes to respawn
	 *
	 * @return respawn time
	 */
	public int getRespawnTime() {
		return respawnTime;
	}

}
