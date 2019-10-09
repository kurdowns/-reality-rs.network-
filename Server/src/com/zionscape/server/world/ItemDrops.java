package com.zionscape.server.world;

import com.google.common.collect.Lists;
import com.zionscape.server.Server;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.items.GroundItem;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Handles ground items Concurrent Fixes
 * <p>
 * <p>
 * TODO remove the item prices / definitions bullshit and change to ItemDrops
 */
public class ItemDrops {

	/**
	 * Creates the ground item
	 */
	public static final int[][] BROKEN_BARROWS = {{4708, 4860}, {4710, 4866}, {4712, 4872}, {4714, 4878}, {4716, 4884},
			{4720, 4896}, {4718, 4890}, {4720, 4896}, {4722, 4902}, {4732, 4932}, {4734, 4938},
			{4736, 4944}, {4738, 4950}, {4724, 4908}, {4726, 4914}, {4728, 4920}, {4730, 4926},
			{4745, 4956}, {4747, 4926}, {4749, 4968}, {4751, 4994}, {4753, 4980}, {4755, 4986},
			{4757, 4992}, {4759, 4998}};

	private static final int HIDE_TICKS = 100; // 1 minute


	private static final int RESPAWN_DROPS_TIME = 17; // around 10 seconds

	private static List<GroundItem> items = new ArrayList<>();

	public static void load() {
		// global drops
		// public GroundItem(int id, int x, int y, int amount, int controller, int hideTicks, String name) {
		// public GroundItem(int id, int amount, int x, int y, boolean respawns) {

		addItem(new GroundItem(1925, 1, 2371, 3127, 0, true));
		addItem(new GroundItem(1925, 1, 2371, 3128, 0, true));
		addItem(new GroundItem(590, 1, 2368, 3135, 0, true));

		addItem(new GroundItem(1925, 1, 2428, 3080, 0, true));
		addItem(new GroundItem(1925, 1, 2428, 3079, 0, true));
		addItem(new GroundItem(590, 1, 2431, 3072, 0, true));
	}

	/**
	 * Adds item to list
	 */
	public static void addItem(GroundItem item) {
		items.add(item);
	}

	/**
	 * Shows items for everyone who is within 60 squares
	 */
	public static void createGlobalItem(GroundItem item) {
		for (Player player : PlayerHandler.players) {
			if (player != null) {
				if (player.playerId != item.getItemController()) {
					if (!player.getItems().tradeable(item.getItemId()) && player.playerId != item.getItemController()) {
						continue;
					}

					if (Location.isWithinDistance(player.getLocation(), item.getLocation(), 64)) {
						player.getItems().createGroundItem(item.getItemId(), item.getLocation().getX(), item.getLocation().getY(), item.getItemAmount());
					}
				}
			}
		}
	}

	//public static void createGroundItem(Player player, int itemId, int itemX, int itemY, int itemAmount, int playerId) {
	//     createGroundItem(player, itemId, itemX, itemY, itemAmount, playerId, true);
	//}

	public static List<GroundItem> createGroundItem(Player player, int itemId, int itemX, int itemY, int itemAmount, int playerId, boolean droppedByPlayer) {
		return createGroundItem(player, itemId, itemX, itemY, player.heightLevel, itemAmount, playerId, droppedByPlayer);
	}

	public static List<GroundItem> createGroundItem(Player player, int itemId, int itemX, int itemY, int itemZ, int itemAmount, int playerId, boolean droppedByPlayer) {

		if (itemId <= 0) {
			return Lists.newArrayList();
		}

		if (itemId >= 2412 && itemId <= 2414) {
			player.sendMessage("The cape vanishes as it touches the ground.");
			return Lists.newArrayList();
		}
		// if(ItemUtility.getName(itemId).toLowerCase().contains("clue scroll")) {
		//      return;
		// }
		if (itemId > 4705 && itemId < 4760) {
			for (int j = 0; j < BROKEN_BARROWS.length; j++) {
				if (BROKEN_BARROWS[j][0] == itemId) {
					itemId = BROKEN_BARROWS[j][1];
					break;
				}
			}
		}

		List<GroundItem> items = new ArrayList<>();

		int hideTicks = HIDE_TICKS;

            /*if (droppedByPlayer && player.inWild()) {
				hideTicks = 2;
            }*/

		if (!ItemUtility.isStackable(itemId) && itemAmount > 0) {
			for (int j = 0; j < itemAmount; j++) {
				player.getItems().createGroundItem(itemId, itemX, itemY, 1);
				GroundItem item = new GroundItem(itemId, itemX, itemY, itemZ, 1, player.playerId, hideTicks, PlayerHandler.players[playerId].username, droppedByPlayer);
				items.add(item);
				addItem(item);
			}
		} else {

			player.getItems().createGroundItem(itemId, itemX, itemY, itemAmount);

			GroundItem item = new GroundItem(itemId, itemX, itemY, itemZ, itemAmount, player.playerId, hideTicks, PlayerHandler.players[playerId].username, droppedByPlayer);
			items.add(item);
			addItem(item);
		}

		return items;
	}

	/**
	 * Item amount
	 */
	public static int itemAmount(String name, int itemId, int itemX, int itemY, int itemZ) {
		Iterator<GroundItem> itr = items.iterator();
		while (itr.hasNext()) {
			GroundItem i = itr.next();
			if (i != null) {
				if (i.hideTicks >= 1 && i.getName().equalsIgnoreCase(name)) {
					if (i.getItemId() == itemId && i.getLocation().equals(itemX, itemY, itemZ)) {
						return i.getItemAmount();
					}
				} else if (i.hideTicks < 1) {
					if (i.getItemId() == itemId && i.getLocation().equals(itemX, itemY, itemZ)) {
						return i.getItemAmount();
					}
				}
			}
		}
		return 0;
	}

	/**
	 * Item exists
	 */
	public static boolean itemExists(int itemId, int itemX, int itemY, int itemZ) {
		Iterator<GroundItem> itr = items.iterator();
		while (itr.hasNext()) {
			GroundItem i = itr.next();
			if (i != null) {
				if (i.getItemId() == itemId && i.getLocation().equals(itemX, itemY, itemZ)) {
					if (i.respawns() && i.getRespawnTimer() > -1) {
						return false;
					}
					return true;
				}
			}
		}
		return false;
	}

	public static boolean itemExists(Location location) {
		return itemExists(location.getX(), location.getY(), location.getZ());
	}

	/**
	 * Item exists
	 */
	public static boolean itemExists(int itemX, int itemY, int itemZ) {
		Iterator<GroundItem> itr = items.iterator();
		while (itr.hasNext()) {
			GroundItem i = itr.next();
			if (i != null) {
				if (i.getLocation().equals(itemX, itemY, itemZ)) {
					if (i.respawns() && i.getRespawnTimer() > -1) {
						return false;
					}
					return true;
				}
			}
		}
		return false;
	}


	public static void process() {
		for (Iterator<GroundItem> iterator = items.iterator(); iterator.hasNext(); ) {

			GroundItem i = iterator.next();

			if (i.respawns()) {
				if (i.getRespawnTimer() > 0) {
					i.setRespawnTimer(i.getRespawnTimer() - 1);
				}
				if (i.getRespawnTimer() == 0) {
					createGlobalItem(i);

					i.setRespawnTimer(-1);
				}
				continue;
			}

			if (i.hideTicks > 0) {
				i.hideTicks--;
			}
			if (i.hideTicks == 1) { // item can now be seen by others
				i.hideTicks = 0;
				createGlobalItem(i);
				i.removeTicks = 200; // 2 minutes
			}
			if (i.removeTicks > 0) {
				i.removeTicks--;
			}
			if (i.removeTicks == 1) {
				i.removeTicks = 0;
				removeGlobalItem(i, i.getItemId(), i.getLocation().getX(), i.getLocation().getY(), i.getLocation().getZ(), i.getItemAmount());
				iterator.remove();
			}
		}
	}

	/**
	 * Reloads any items if you enter a new region
	 */
	public static void reloadItems(Player c) {
		Predicate<GroundItem> visible = item -> ((c.getItems().tradeable(item.getItemId()) || item.getName().equalsIgnoreCase(c.username)) && Location.isWithinDistance(c.getLocation(), item.getLocation(), 64)) && (item.hideTicks > 0 && item.getName().equalsIgnoreCase(c.username) || item.hideTicks == 0);
		items.stream().filter(visible).forEach(i -> c.getItems().removeGroundItem(i.getItemId(), i.getLocation().getX(), i.getLocation().getY(), i.getItemAmount()));
		items.stream().filter(visible).forEach(i -> c.getItems().createGroundItem(i.getItemId(), i.getLocation().getX(), i.getLocation().getY(), i.getItemAmount()));
	}

	/**
	 * Remove item for just the item controller (item not global yet)
	 */
	public static void removeControllersItem(GroundItem item, Player c, int itemId, int itemX, int itemY, int itemAmount) {
		c.getItems().removeGroundItem(itemId, itemX, itemY, itemAmount);
	}

	/**
	 * Remove item for everyone within 60 squares
	 */
	public static void removeGlobalItem(GroundItem i, int itemId, int itemX, int itemY, int itemZ, int itemAmount) {
		for (Player p : PlayerHandler.players) {
			if (p == null) {
				continue;
			}
			if (Location.isWithinDistance(p.getLocation(), Location.create(itemX, itemY, itemZ), 64)) {
				p.getItems().removeGroundItem(itemId, itemX, itemY, itemAmount);
			}
		}

		if (i.respawns()) {
			i.setRespawnTimer(RESPAWN_DROPS_TIME);
		}

	}

	/**
	 * Removing the ground item
	 */
	public static void removeGroundItem(Player c, int itemId, int itemX, int itemY, boolean add) {
		Iterator<GroundItem> itr = items.iterator();
		while (itr.hasNext()) {
			GroundItem i = itr.next();
			if (i != null) {
				if (i.getItemId() == itemId && i.getLocation().getX() == itemX && i.getLocation().getY() == itemY && c.heightLevel == i.getLocation().getZ()) {


					if (c.ironman) {
						if (i.pkLoot) {
							c.sendMessage("You cannot do this on an ironman account.");
							continue;
						}

						if (i.playerDrop && !i.getName().equalsIgnoreCase(c.username)) {
							c.sendMessage("You cannot do this on an ironman account.");
							continue;
						}
						if (!i.getName().equalsIgnoreCase(c.username)) {
							c.sendMessage("You cannot pick up drops not belonging to you on an ironman account.");
							continue;
						}
					}

					if (i.hideTicks > 0 && i.getName().equalsIgnoreCase(c.username)) {
						if (add) {
							if (!c.getItems().specialCase(itemId)) {
								if (c.getItems().addItem(i.getItemId(), i.getItemAmount())) {
									addPickupLog(c, i);
									removeControllersItem(i, c, i.getItemId(), i.getLocation().getX(), i.getLocation().getY(), i.getItemAmount());
									itr.remove();
									break;
								}
							} else {
								c.getItems().handleSpecialPickup(itemId);
								removeControllersItem(i, c, i.getItemId(), i.getLocation().getX(), i.getLocation().getY(), i.getItemAmount());
								itr.remove();
								break;
							}
						} else {
							removeControllersItem(i, c, i.getItemId(), i.getLocation().getX(), i.getLocation().getY(), i.getItemAmount());
							itr.remove();
							break;
						}
					} else if (i.hideTicks <= 0) {
						if (add) {
							if (c.getItems().addItem(i.getItemId(), i.getItemAmount())) {
								addPickupLog(c, i);
								removeGlobalItem(i, i.getItemId(), i.getLocation().getX(), i.getLocation().getY(), c.heightLevel, i.getItemAmount());
								itr.remove();
								break;
							}
						} else {
							removeGlobalItem(i, i.getItemId(), i.getLocation().getX(), i.getLocation().getY(), c.heightLevel, i.getItemAmount());
							itr.remove();
							break;
						}
					}
				}
			}
		}
	}

	private static void addPickupLog(Player player, GroundItem i) {
		Server.submitWork(() -> {
			try (Connection connection = DatabaseUtil.getConnection()) {
				try (PreparedStatement ps = connection.prepareStatement("INSERT INTO player_pickup_logs (player_id, item, amount, x, y, z, who_dropped_it) VALUES(?, ?, ?, ?, ?, ?, ?)")) {
					ps.setInt(1, player.getDatabaseId());
					ps.setInt(2, i.getItemId());
					ps.setInt(3, i.getItemAmount());
					ps.setInt(4, i.getLocation().getX());
					ps.setInt(5, i.getLocation().getY());
					ps.setInt(6, i.getLocation().getZ());
					ps.setString(7, i.getName() == null ? "null" : i.getName());
					ps.execute();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

}
