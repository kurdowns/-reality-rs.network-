package com.zionscape.server.model.players;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.zionscape.server.Config;
import com.zionscape.server.Connection;
import com.zionscape.server.Server;
import com.zionscape.server.ServerEvents;
import com.zionscape.server.cache.Collision;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Area;
import com.zionscape.server.model.DynamicMapObject;
import com.zionscape.server.model.Entity;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.MapObject;
import com.zionscape.server.model.clan.Clan;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.content.minigames.DuelArena;
import com.zionscape.server.model.content.minigames.FightCaves;
import com.zionscape.server.model.content.minigames.FightKiln;
import com.zionscape.server.model.content.minigames.PestControl;
import com.zionscape.server.model.content.minigames.castlewars.CastleWars;
import com.zionscape.server.model.content.minigames.clanwars.ClanWars;
import com.zionscape.server.model.content.minigames.clanwars.War;
import com.zionscape.server.model.content.minigames.gambling.Gambling;
import com.zionscape.server.model.content.minigames.zombies.Zombies;
import com.zionscape.server.model.content.tradingpost.item.container.TradingPostItemContainer;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.items.SerpentineHelmet;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCConfig;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.npcs.other.MissCheevers;
import com.zionscape.server.model.players.combat.Animations;
import com.zionscape.server.model.players.combat.CombatHelper;
import com.zionscape.server.model.players.dialogue.DialogueAnimations;
import com.zionscape.server.model.players.dialogue.DialogueConstants;
import com.zionscape.server.model.players.skills.EnchantingMagic;
import com.zionscape.server.model.players.skills.slayer.Slayer;
import com.zionscape.server.model.players.skills.summoning.Summoning;
import com.zionscape.server.model.region.RegionUtility;
import com.zionscape.server.net.Packet;
import com.zionscape.server.net.PacketBuilder;
import com.zionscape.server.tick.Tick;
import com.zionscape.server.util.CollectionUtil;
import com.zionscape.server.util.DatabaseUtil;
import com.zionscape.server.util.Misc;
import com.zionscape.server.util.pathfinding.PlayerPathFinder;
import com.zionscape.server.world.shops.Shops;

/**
 * PlayerAssistant Class Handles the <Player> Actions
 *
 * @author Lmctruck30
 * @version 1.2
 */
public class PlayerAssistant {

	public final static int SMALL_POUCH = 5509;
	public final static int MEDIUM_POUCH = 5510;
	public final static int LARGE_POUCH = 5512;
	public final static int GIANT_POUCH = 5514;
	public final static int BROKEN_MEDIUM_POUCH = 5511;
	public final static int BROKEN_LARGE_POUCH = 5513;
	public final static int BROKEN_GIANT_POUCH = 5515;
	private final static int RUNE_ESS = 1436;
	private final static int PURE_ESS = 7936;
	// public static int Barrows[] = {4708, 4710, 4712, 4714, 4716, 4718, 4720,
	// 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749,
	// 4751, 4753, 4755, 4757, 4759};
	// public static int Runes[] = {4740,558,560,565};
	public static int[] Pots = {};
	public static int[] Crystal = { 1, 2, 3, 4 };
	public static int[] Crystal1 = {};
	private static int[] DO_NOT_ANNOUNCE_SKILL_IDS = { PlayerConstants.ATTACK, PlayerConstants.STRENGTH,
			PlayerConstants.HITPOINTS, PlayerConstants.DEFENCE, PlayerConstants.RANGED, PlayerConstants.MAGIC,
			PlayerConstants.THIEVING };
	/**
	 * Online Mods
	 */
	public ArrayList<String> onlineMods = new ArrayList<>();
	public ArrayList<String> playerNeedsHelp = new ArrayList<>();
	public int mapStatus = 0;
	/**
	 * Show option, attack, trade, follow etc
	 */
	public int mediumPouchCapacity;
	public int largePouchCapacity;
	public int giantPouchCapacity;
	// Note(stuart): used for caching the current walkable interface
	public int currentWalkableInterface = -1;
	private Player c;
	private String attackOption = "Null";

	public PlayerAssistant(Player player) {
		this.c = player;
	}

	public static int getCurrentDay() {
		Calendar cal = new GregorianCalendar();
		return cal.get(Calendar.DAY_OF_YEAR);
	}

	public static void resetPlayer(Player player) {
		for (int i = 0; i < player.level.length; i++) {
			player.level[i] = player.getPA().getActualLevel(i);
			player.getPA().refreshSkill(i);
		}
		player.specAmount = 10;
		player.poisonDamage = 0;
		player.getCombat().resetAllPrayers();
		player.usingSpecial = false;
		player.doJavelinSpecial = false;
		player.isSkulled = false;

		if (player.getFamiliar() != null) {
			Summoning.destroyFamiliar(player);
		}

		player.appearanceUpdateRequired = true;
		player.updateRequired = true;
	}

	public void addPouches() {
		if (c.getItems().getItemCount(SMALL_POUCH) < 1 && c.getBank().playerHasItem(SMALL_POUCH) < 1) {
			if (c.getItems().freeInventorySlots() > 0) {
				c.getItems().addItem(SMALL_POUCH, 1);
			} else {
				c.sendMessage("You have run out of inventory slots.");
				return;
			}
		}
		if ((c.getItems().getItemCount(MEDIUM_POUCH) < 1 && c.getBank().playerHasItem(MEDIUM_POUCH) < 1)) {
			if ((c.getItems().getItemCount(BROKEN_MEDIUM_POUCH) < 1
					&& c.getBank().playerHasItem(BROKEN_MEDIUM_POUCH) < 1)) {
				if (c.level[20] >= 25) {
					if (c.getItems().freeInventorySlots() > 0) {
						c.getItems().addItem(MEDIUM_POUCH, 1);
						c.mediumPouchDecay = 45;
					} else {
						c.sendMessage("You have run out of inventory slots.");
						return;
					}
				}
			}
		}
		if ((c.getItems().getItemCount(LARGE_POUCH) < 1 && c.getBank().playerHasItem(LARGE_POUCH) < 1)) {
			if ((c.getItems().getItemCount(BROKEN_LARGE_POUCH) < 1
					&& c.getBank().playerHasItem(BROKEN_LARGE_POUCH) < 1)) {
				if (c.level[20] >= 50) {
					if (c.getItems().freeInventorySlots() > 0) {
						c.getItems().addItem(LARGE_POUCH, 1);
						c.largePouchDecay = 29;
					} else {
						c.sendMessage("You have run out of inventory slots.");
						return;
					}
				}
			}
		}
		if ((c.getItems().getItemCount(GIANT_POUCH) < 1 && c.getBank().playerHasItem(GIANT_POUCH) < 1)) {
			if ((c.getItems().getItemCount(BROKEN_GIANT_POUCH) < 1
					&& c.getBank().playerHasItem(BROKEN_GIANT_POUCH) < 1)) {
				if (c.level[20] >= 75) {
					if (c.getItems().freeInventorySlots() > 0) {
						c.getItems().addItem(GIANT_POUCH, 1);
						c.giantPouchDecay = 10;
					} else {
						c.sendMessage("You have run out of inventory slots.");
					}
				}
			}
		}
	}

	public void repairPouches() {
		if (c.getItems().playerHasItem(BROKEN_MEDIUM_POUCH, 1)) {
			c.getItems().deleteItem(BROKEN_MEDIUM_POUCH, 1);
			c.getItems().addItem(MEDIUM_POUCH, 1);
		}
		if (c.getItems().playerHasItem(BROKEN_LARGE_POUCH, 1)) {
			c.getItems().deleteItem(BROKEN_LARGE_POUCH, 1);
			c.getItems().addItem(LARGE_POUCH, 1);
		}
		if (c.getItems().playerHasItem(BROKEN_GIANT_POUCH, 1)) {
			c.getItems().deleteItem(BROKEN_GIANT_POUCH, 1);
			c.getItems().addItem(GIANT_POUCH, 1);
		}
		c.mediumPouchDecay = 45;
		c.largePouchDecay = 29;
		c.giantPouchDecay = 10;
	}

	public void addSmallPouch() {
		if (c.smallPouchP + c.smallPouchE >= 3)
			return;
		int pEss = c.getItems().getItemCount(PURE_ESS);
		int rEss = c.getItems().getItemCount(RUNE_ESS);
		if (pEss > 3)
			pEss = 3;
		if (rEss > 3)
			rEss = 3;
		for (int i = 0; i < pEss; i++) {
			c.smallPouchP++;
			c.getItems().deleteItem(PURE_ESS, 1);
			if (c.smallPouchP + c.smallPouchE >= 3)
				return;
		}
		for (int j = 0; j < rEss; j++) {
			c.smallPouchE++;
			c.getItems().deleteItem(RUNE_ESS, 1);
			if (c.smallPouchP + c.smallPouchE >= 3)
				return;
		}
	}

	public void removeSmallPouch() {
		if (c.smallPouchE == 0 && c.smallPouchP == 0)
			return;
		int pouchp = c.smallPouchP;
		int pouche = c.smallPouchE;
		for (int i = 0; i < c.smallPouchP; i++) {
			int invSlots = c.getItems().freeInventorySlots();
			if (invSlots > 0) {
				c.getItems().addItem(PURE_ESS, 1);
				pouchp--;
			} else {
				c.sendMessage("You have run out of free inventory slots.");
				c.smallPouchP = pouchp;
				return;
			}
		}
		c.smallPouchP = pouchp;
		for (int i = 0; i < c.smallPouchE; i++) {
			int invSlots = c.getItems().freeInventorySlots();
			if (invSlots > 0) {
				c.getItems().addItem(RUNE_ESS, 1);
				pouche--;
			} else {
				c.sendMessage("You have run out of free inventory slots.");
				c.smallPouchE = pouche;
				return;
			}
		}
		c.smallPouchE = pouche;
	}

	public void addMediumPouch() {
		mediumPouchCapacity = 6;

		/*
		 * if (c.mediumPouchDecay < 0) mediumPouchCapacity = 3; else mediumPouchCapacity
		 * = 6;
		 */
		if (c.mediumPouchP + c.mediumPouchE >= mediumPouchCapacity)
			return;
		/*
		 * if (c.mediumPouchDecay > 0) c.mediumPouchDecay--; if (c.mediumPouchDecay ==
		 * 0) { c.getItems().deleteItem(MEDIUM_POUCH, 1);
		 * c.getItems().addItem(BROKEN_MEDIUM_POUCH, 1); mediumPouchCapacity = 3;
		 * c.mediumPouchDecay = -1; }
		 */
		// if (c.mediumPouchP + c.mediumPouchE >= mediumPouchCapacity)
		// return;
		int pEss = c.getItems().getItemCount(PURE_ESS);
		int rEss = c.getItems().getItemCount(RUNE_ESS);
		if (pEss > mediumPouchCapacity)
			pEss = mediumPouchCapacity;
		if (rEss > mediumPouchCapacity)
			rEss = mediumPouchCapacity;
		for (int i = 0; i < pEss; i++) {
			c.mediumPouchP++;
			c.getItems().deleteItem(PURE_ESS, 1);
			if (c.mediumPouchP + c.mediumPouchE >= mediumPouchCapacity)
				return;
		}
		for (int j = 0; j < rEss; j++) {
			c.mediumPouchE++;
			c.getItems().deleteItem(RUNE_ESS, 1);
			if (c.mediumPouchP + c.mediumPouchE >= mediumPouchCapacity)
				return;
		}
	}

	public void removeMediumPouch() {
		if (c.mediumPouchE == 0 && c.mediumPouchP == 0)
			return;
		int pouchp = c.mediumPouchP;
		int pouche = c.mediumPouchE;
		for (int i = 0; i < c.mediumPouchP; i++) {
			int invSlots = c.getItems().freeInventorySlots();
			if (invSlots > 0) {
				c.getItems().addItem(PURE_ESS, 1);
				pouchp--;
			} else {
				c.sendMessage("You have run out of free inventory slots.");
				c.mediumPouchP = pouchp;
				return;
			}
		}
		c.mediumPouchP = pouchp;
		for (int i = 0; i < c.mediumPouchE; i++) {
			int invSlots = c.getItems().freeInventorySlots();
			if (invSlots > 0) {
				c.getItems().addItem(RUNE_ESS, 1);
				pouche--;
			} else {
				c.sendMessage("You have run out of free inventory slots.");
				c.mediumPouchE = pouche;
				return;
			}
		}
		c.mediumPouchE = pouche;
	}

	public void addLargePouch() {
		largePouchCapacity = 9;

		/*
		 * if (c.largePouchDecay < 0) largePouchCapacity = 6; else largePouchCapacity =
		 * 9;
		 */
		if (c.largePouchP + c.largePouchE >= largePouchCapacity)
			return;
		/*
		 * if (c.largePouchDecay > 0) c.largePouchDecay--; if (c.largePouchDecay == 0) {
		 * c.getItems().deleteItem(LARGE_POUCH, 1);
		 * c.getItems().addItem(BROKEN_LARGE_POUCH, 1); largePouchCapacity = 6;
		 * c.largePouchDecay = -1; }
		 */
		// if (c.largePouchP + c.largePouchE >= largePouchCapacity)
		// return;
		int pEss = c.getItems().getItemCount(PURE_ESS);
		int rEss = c.getItems().getItemCount(RUNE_ESS);
		if (pEss > largePouchCapacity)
			pEss = largePouchCapacity;
		if (rEss > largePouchCapacity)
			rEss = largePouchCapacity;
		for (int i = 0; i < pEss; i++) {
			c.largePouchP++;
			c.getItems().deleteItem(PURE_ESS, 1);
			if (c.largePouchP + c.largePouchE >= largePouchCapacity)
				return;
		}
		for (int j = 0; j < rEss; j++) {
			c.largePouchE++;
			c.getItems().deleteItem(RUNE_ESS, 1);
			if (c.largePouchP + c.largePouchE >= largePouchCapacity)
				return;
		}
	}

	public void removeLargePouch() {
		if (c.largePouchE == 0 && c.largePouchP == 0)
			return;
		int pouchp = c.largePouchP;
		int pouche = c.largePouchE;
		for (int i = 0; i < c.largePouchP; i++) {
			int invSlots = c.getItems().freeInventorySlots();
			if (invSlots > 0) {
				c.getItems().addItem(PURE_ESS, 1);
				pouchp--;
			} else {
				c.sendMessage("You have run out of free inventory slots.");
				c.largePouchP = pouchp;
				return;
			}
		}
		c.largePouchP = pouchp;
		for (int i = 0; i < c.largePouchE; i++) {
			int invSlots = c.getItems().freeInventorySlots();
			if (invSlots > 0) {
				c.getItems().addItem(RUNE_ESS, 1);
				pouche--;
			} else {
				c.sendMessage("You have run out of free inventory slots.");
				c.largePouchE = pouche;
				return;
			}
		}
		c.largePouchE = pouche;
	}

	public void addGiantPouch() {
		giantPouchCapacity = 12;

		/*
		 * if (c.giantPouchDecay < 0) giantPouchCapacity = 9; else giantPouchCapacity =
		 * 12;
		 */
		if (c.giantPouchP + c.giantPouchE >= giantPouchCapacity)
			return;
		/*
		 * if (c.giantPouchDecay > 0) c.giantPouchDecay--; if (c.giantPouchDecay == 0) {
		 * c.getItems().deleteItem(GIANT_POUCH, 1);
		 * c.getItems().addItem(BROKEN_GIANT_POUCH, 1); giantPouchCapacity = 9;
		 * c.giantPouchDecay = -1; }
		 */
		// if (c.giantPouchP + c.giantPouchE >= giantPouchCapacity)
		// return;
		int pEss = c.getItems().getItemCount(PURE_ESS);
		int rEss = c.getItems().getItemCount(RUNE_ESS);
		if (pEss > giantPouchCapacity)
			pEss = giantPouchCapacity;
		if (rEss > giantPouchCapacity)
			rEss = giantPouchCapacity;
		for (int i = 0; i < pEss; i++) {
			c.giantPouchP++;
			c.getItems().deleteItem(PURE_ESS, 1);
			if (c.giantPouchP + c.giantPouchE >= giantPouchCapacity)
				return;
		}
		for (int j = 0; j < rEss; j++) {
			c.giantPouchE++;
			c.getItems().deleteItem(RUNE_ESS, 1);
			if (c.giantPouchP + c.giantPouchE >= giantPouchCapacity)
				return;
		}
	}

	public void removeGiantPouch() {
		if (c.giantPouchE == 0 && c.giantPouchP == 0)
			return;
		int pouchp = c.giantPouchP;
		int pouche = c.giantPouchE;
		for (int i = 0; i < c.giantPouchP; i++) {
			int invSlots = c.getItems().freeInventorySlots();
			if (invSlots > 0) {
				c.getItems().addItem(PURE_ESS, 1);
				pouchp--;
			} else {
				c.sendMessage("You have run out of free inventory slots.");
				c.giantPouchP = pouchp;
				return;
			}
		}
		c.giantPouchP = pouchp;
		for (int i = 0; i < c.giantPouchE; i++) {
			int invSlots = c.getItems().freeInventorySlots();
			if (invSlots > 0) {
				c.getItems().addItem(RUNE_ESS, 1);
				pouche--;
			} else {
				c.sendMessage("You have run out of free inventory slots.");
				c.giantPouchE = pouche;
				return;
			}
		}
		c.giantPouchE = pouche;
	}

	/**
	 * Get the level for the given xp
	 *
	 * @param xp
	 * @return
	 */
	public int getLevelForXP(int xp) {
		if (xp >= 13034431) {
			return 99;
		}
		if (xp < 83) {
			return 1;
		}
		for (int i = 0; i < PlayerConstants.XP_FOR_LEVELS.length; i++) {
			if (xp <= PlayerConstants.XP_FOR_LEVELS[i]) {
				return i;
			}
		}
		return 0;
	}

	public boolean addSkillXP(int amount, int skill) {
		return addSkillXP(amount, skill, true);
	}

	/**
	 * @param amount
	 *            the amount of xp to add to the skill
	 * @param skill
	 *            the skill id
	 * @return
	 */
	public boolean addSkillXP(int amount, int skill, boolean multiply) {

		if (c.getData().xpLock) {
			return false;
		}

		// prevent negative xp
		if (amount <= 0) {
			try {
				throw new RuntimeException("giving negative xp in " + skill);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			return false;
		}

		// stop xp going over 200m
		if (c.xp[skill] > 200_000_000) {
			c.xp[skill] = 200_000_000;
		}

		// check if we have reached max xp
		if (amount + c.xp[skill] > 200_000_000) {
			amount = 200_000_000 - c.xp[skill];

			if (amount <= 0) {
				return true;
			}
		}

		if (c.lockedEXP == 1) {
			return false;
		}

		if (multiply) {

			if (skill == PlayerConstants.PRAYER || skill >= PlayerConstants.COOKING) {
				amount = amount * Config.SKILL_EXP_RATE * c.getPA().expMultiplier();
			} else {
				amount = amount * Config.COMBAT_EXP_RATE;
			}

			if (c.elite) {
				amount = (int) Math.round((double) amount / 3.5);
			}

			if (MissCheevers.isPerkActive(c, MissCheevers.XP)) {
				amount = (int) Math.ceil((double) amount * 1.5);
			}

			if (c.getData().doubleXpTime > 0) {
				amount = amount * 2;
			}

			if (Areas.inRespectedArea(c.getLocation())) {
				amount *= 1.5;
			}
		}

		int oldLevel = this.getLevelForXP(c.xp[skill]);
		c.xp[skill] += amount;

		if (c.xp[skill] > 200_000_000) {
			Achievements.progressMade(c, Achievements.Types.EARN_200M_XP_IN_A_SKILL);
		}

		if (c.xp[skill] > 100_000_000 && PlayerConstants.nonCombatSkill(skill)
				&& !c.getData().skillAnnouncement100m[skill]) {
			PlayerHandler.yell("[Server]@red@The user " + c.username + " has achieved 100m xp in "
					+ PlayerConstants.SKILL_NAMES[skill] + ".");
			c.getData().skillAnnouncement100m[skill] = true;
		}

		if (oldLevel < this.getLevelForXP(c.xp[skill])) {
			if (c.level[skill] < getLevelForXP(c.xp[skill]) && skill != 3 && skill != 5
					&& skill != PlayerConstants.SUMMONING) {
				c.level[skill] = getLevelForXP(c.xp[skill]);
			}

			if (c.level[skill] == 99 && CollectionUtil.getIndexOfValue(DO_NOT_ANNOUNCE_SKILL_IDS, skill) == -1) {
				if (c.hardcoreIronman) {
					PlayerHandler.yell("@blu@[Server] @bla@The Ultimate Ironman " + c.username + " has achieved 99 "
							+ PlayerConstants.SKILL_NAMES[skill] + "!");
				} else if (c.ironman) {
					PlayerHandler.yell("@blu@[Server] @bla@The Ironman " + c.username + " has achieved 99 "
							+ PlayerConstants.SKILL_NAMES[skill] + "!");
				} else {
					PlayerHandler.yell("@blu@[Server] @bla@The user " + c.username + " has achieved 99 "
							+ PlayerConstants.SKILL_NAMES[skill] + "!");
				}
			}

			this.levelUp(skill);
			c.gfx100(199);
			this.requestUpdates();
			c.combatLevel = c.calculateCombatLevel();

			sendFrame126("Total Level: " + getTotalLevel(), 3984);

			if (skill != PlayerConstants.THIEVING && !c.getData().receivedSkillingBonus
					&& getLevelForXP(c.xp[skill]) == 99) {
				// c.getItems().addDirectlyToBank(995, 25_000_000);
				c.getData().receivedSkillingBonus = true;
				// c.sendMessage("For achieving your first 99, 25M GP has been deposited in your
				// bank.");
			}

		}
		this.refreshSkill(skill);
		addXpToCounter(amount);
		return true;
	}

	public void addXpToCounter(int xp) {

		c.getData().xpCounter += xp;

		c.outStream.createFrame(133);
		c.outStream.writeDWord(xp);
		c.flushOutStream();
	}

	private Item[] starterItems = { new Item(2441, 10), new Item(2437, 10), new Item(2443, 10), new Item(386, 200),
			new Item(997, 150000), new Item(4587, 10), new Item(1323, 10), new Item(1115, 10), new Item(1067, 10),
			new Item(1153, 10), new Item(1191, 10), new Item(1169, 1), new Item(1129, 1), new Item(1095, 1),
			new Item(882, 250), new Item(841, 1), new Item(555, 1000), new Item(556, 1000), new Item(557, 1000),
			new Item(558, 1000), new Item(1011, 11), };

	// Note(stuart): used by scripts
	public void addStarter() {

		c.tradeTimer = (int) Math.floor((15 * 60 * 1000) / 600);
		String playerIp = PlayerHandler.players[c.playerId].connectedFrom;
		if (c.ironman) {
			c.getItems().addMoneyToInventory(250000);
			c.getItems().addItem(1323, 1);
			c.getItems().addItem(1191, 1);
			return;
		}

		if (!Connection.hasRecieved1stStarter(playerIp)) {
			
			for (Item item : starterItems)
				c.getItems().addItem(item);
			Connection.addIpToStarterList1(playerIp);
			Connection.addIpToStarter1(playerIp);
			
		} else if (Connection.hasRecieved1stStarter(playerIp)
				&& !Connection.hasRecieved2ndStarter(playerIp)) {
			
			for (Item item : starterItems)
				c.getItems().addItem(item);
			Connection.addIpToStarterList2(playerIp);
			Connection.addIpToStarter2(playerIp);
			
		} else if (Connection.hasRecieved1stStarter(playerIp)
				&& Connection.hasRecieved2ndStarter(playerIp)) {
			c.sendMessage("You have already received your starters.");
			c.dialogueAction = -1;
		}
	}

	public void setScrollPos(int interfaceFrameID, int height) {
		c.getOutStream().createFrame(79);
		c.getOutStream().writeWordBigEndian(interfaceFrameID);
		c.getOutStream().writeWordA(height);
		c.flushOutStream();
	}

	public double antiFire() {
		if (c.antiFirePot() && (c.equipment[c.playerShield] == 11284 || c.equipment[c.playerShield] == 11283)) {
			return 1;
		}

		if (c.antiFirePot() && c.equipment[c.playerShield] == 1540) {
			return 0.30; // 70%
		}

		if (c.equipment[c.playerShield] == 11284 || c.equipment[c.playerShield] == 11283) {
			return 0.25; // 75
		}

		if (c.equipment[c.playerShield] == 1540) {
			return 0.40; // 60
		}

		if (c.antiFirePot()) {
			return 0.50;
		}

		return 0;
	}

	public void appendPoison(int damage) {

		// When equipped, the helmet will grant immunity to venom and poison.
		if (c.equipment[PlayerConstants.HAT] == SerpentineHelmet.HELM) {
			c.sendMessage("Your helmet prevents you from being poisoned.");
			return;
		}

		if (System.currentTimeMillis() - c.lastPoisonSip > c.poisonImmune) {
			c.sendMessage("You have been poisoned.");
			c.poisonDamage = damage;
		}
	}

	/**
	 * Dieing
	 */

	public void applyDead() {

		c.respawnTimer = 15;
		c.isDead = false;

		Achievements.progressMade(c, Achievements.Types.DIE_20_TIMES);

		if (c.getDuel() == null && c.getCastleWarsState() == null) {
			c.killerId = this.findKiller();
			Player o = PlayerHandler.players[c.killerId];
			if (o != null && o != c) {

				c.setAttribute("died_to_player", true);

				if (!ClanWars.inArena(c) && !ClanWars.inJail(c)) {
					c.deaths++;
				}
				if (c.killerId != c.playerId) {
					o.lastKilled = c.username;
					c.nemisis = o.username;
					c.playerKilled = 0;
				} else {
					o.sendMessage("Try killing other players before killing " + c.username + " again.");
					c.playerKilled = 0;
					c.shouldGetDrops = false;
				}

				if (c.playerId != o.playerId) {
					o.sendMessage("You have defeated " + c.username + ".");
					if (!ClanWars.inArena(c) && !ClanWars.inJail(c) & !this.inMiniGame()) {
						if (PlayerKill.addKill(o, c)) {

							o.getPA().sendFrame126("@or1@[@whi@" + o.kills + "@or1@] Kills", 54432);
							c.getPA().sendFrame126("@or1@[@whi@" + c.deaths + "@or1@] Deaths", 54433);

							Achievements.progressMade(o, Achievements.Types.EARN_PK_POINT);
							Achievements.progressMade(o, Achievements.Types.EARN_20_PK_POINTS);

							if (Misc.random(0, 11) == 0) {
								Achievements.progressMade(o, Achievements.Types.EARN_100_WILD_KEYS);

								o.getItems().addOrBank(9722, 1);
								o.sendMessage("@red@You have received a Wilderness key!");
								PlayerHandler.yell("[Server]\"" + o.username + "\" has received a Wilderness key.");
							}

							if (Misc.random(8000) == 0) {
								o.getItems().addOrBank(964, 1);
								o.sendMessage("You have been awarded with the Death pet.");
							}

							// o.getData().pkPoints++;
							if (Config.triplePk && new Area(3049, 3522, 3125, 3587, 0).inArea(o.getLocation())) {
								// o.getData().pkPoints++;
								// o.getData().pkPoints++;
							} else if (MissCheevers.isPerkActive(o, MissCheevers.PK)) {
								// o.getData().pkPoints++;
							}

							o.getPA().sendFrame126("@or1@[@whi@" + o.getData().pkPoints + "@or1@] Pk Points", 54431);
							o.kills++;
							o.getData().killstreak++;

							if (o.getData().killstreak >= 2) {
								PlayerHandler.yell("@blu@[Server]@red@" + o.username + " just killed " + c.username
										+ " and is on a " + o.getData().killstreak + " player kill streak!");
							}

							// o.getItems().addItem(995, 1000000);//
							// o.sendMessage("You have received a point, you now have " + o.pkPoints + " pk
							// points.");
							// o.sendMessage("You also receive 1m in gold for defeating your opponent!");
						} else {
							o.sendMessage("You have recently defeated " + c.username + ". You don't gain this kill.");
						}
					}
				}

				c.playerKilled = c.playerId;
				if (c.prayerActive[21]) {
					int damage = Misc.random(17);
					o.gfx0(437);
					o.hitDiff = damage;
					o.level[3] -= damage;
					o.getPA().refreshSkill(3);
					o.updateRequired = true;
					o.hitUpdateRequired = true;
				}

			}
		}

		ServerEvents.onPlayerDied(c);

		c.faceUpdate(0);
		c.npcIndex = 0;
		c.playerIndex = 0;
		c.stopMovement();
		if (c.getDuel() != null && c.getDuel().getStage() == DuelArena.Stage.FIGHTING) {
			c.sendMessage("You have lost the duel!");
		} else {
			c.sendMessage("Oh dear you are dead!");
			c.playerDeaths++;
		}

		c.removeAttribute("fire_boost");
		c.getData().energy = 100;
		sendEnergy();

		this.resetDamageDone();
		c.specAmount = 10;
		c.getItems().addSpecialBar(c.equipment[Player.playerWeapon]);
		c.lastVeng = 0;
		c.vengOn = false;
		this.resetFollowers();
		c.attackTimer = 10;
	}

	public void assignTeams(String type) {
		if (type.equalsIgnoreCase("p2p")) {
			if (this.redWaiting() - this.yellowWaiting() >= 0) {
				c.team = "yellow";
				c.getPA().spellTeleport(2379 + Misc.random(2), 9489 + Misc.random(2), 0);
			} else {
				c.team = "red";
				c.getPA().spellTeleport(2422 + Misc.random(2), 9525 + Misc.random(2), 0);
			}
		} else {
			c.sendMessage("nty");
		}
	}

	public void buyColoredWhip(String type) {
		int cash = 10000000;
		int cashAmount = c.getItems().getItemAmount(995);
		int slot = c.getItems().getItemSlot(995);
		int whip = c.getItems().getItemSlot(4151);
		int whipAmount = c.getItems().getItemAmount(4151);
		if (cashAmount < cash) {
			c.sendMessage("You do not have enough cash!");
			this.removeAllWindows();
			return;
		} else if (whipAmount < 1) {
			c.sendMessage("You need an abyssal whip to change the color");
			this.removeAllWindows();
			return;
		}
		if (type.equalsIgnoreCase("blue")) {
			c.getItems().deleteItem(995, slot, cash);
			c.getItems().deleteItem(4151, whip, 1);
			c.getItems().addItem(15442, 1);
		}
		if (type.equalsIgnoreCase("green")) {
			c.getItems().deleteItem(995, slot, cash);
			c.getItems().deleteItem(4151, whip, 1);
			c.getItems().addItem(15444, 1);
		}
		if (type.equalsIgnoreCase("yellow")) {
			c.getItems().deleteItem(995, slot, cash);
			c.getItems().deleteItem(4151, whip, 1);
			c.getItems().addItem(15441, 1);
		}
		if (type.equalsIgnoreCase("white")) {
			c.getItems().deleteItem(995, slot, cash);
			c.getItems().deleteItem(4151, whip, 1);
			c.getItems().addItem(15443, 1);
		}
		// if(kill)
		// break;
	}

	public void buyPestExp(String type) {
		int requiredPoints = 10;
		if (c.pcPoints < requiredPoints) {
			c.sendMessage("You do not have enough points");
			this.removeAllWindows();
			return;
		}
		if (type.equalsIgnoreCase("attack")) {
			this.addSkillXP(((c.level[0] + 25) - (c.level[0] - 24) / 606) * 35, 0);
			// addSkillXP(10000*expMultiplier(),0);
		} else if (type.equalsIgnoreCase("strength")) {
			this.addSkillXP(((c.level[2] + 25) - (c.level[2] - 24) / 606) * 35, 2);
			// addSkillXP(2, 10000*expMultiplier());
		} else if (type.equalsIgnoreCase("defence")) {
			this.addSkillXP(((c.level[1] + 25) - (c.level[1] - 24) / 606) * 35, 1);
			// addSkillXP(1, 10000*expMultiplier());
		} else if (type.equalsIgnoreCase("magic")) {
			this.addSkillXP(((c.level[6] + 25) - (c.level[6] - 24) / 606) * 32, 6);
			// addSkillXP(6, 7500*expMultiplier());
		} else if (type.equalsIgnoreCase("range")) {
			this.addSkillXP(((c.level[4] + 25) - (c.level[6] - 24) / 606) * 324, 4);
			// addSkillXP(4, 7500*expMultiplier());
		} else if (type.equalsIgnoreCase("prayer")) {
			this.addSkillXP(((c.level[5] + 25) - (c.level[5] - 24) / 606) * 18, 5);
			// addSkillXP(5, 5000*expMultiplier());
		} else if (type.equalsIgnoreCase("hitpoints")) {
			this.addSkillXP(((c.level[3] + 25) - (c.level[3] - 24) / 606) * 35, 3);
			// addSkillXP(3, 10000*expMultiplier());
		}
		c.sendMessage("Thank you for buying " + type + " experience");
		this.removeAllWindows();
		c.pcPoints -= 10;
		// break;
	}

	public void buyYellPoints() {
		int cashAmount = c.getItems().getItemAmount(995);
		if (cashAmount >= 1000000) {
			c.getItems().deleteItem(995, 1000000);
			c.yellPoints += 25;
			c.sendMessage("Thanks for buying the yell points!");
		} else {
			c.sendMessage("You don't have enough money!");
			this.closeAllWindows();
		}
	}

	public boolean byPassRights() {
		if (c.connectedFrom.equalsIgnoreCase("5.7.432.12")) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unused")
	public void changeExt() {
		int[] ext = { 15308, 15312, 15316, 15320, 15324 };
		int first = 15304;
		int[] slots = { c.getItems().getItemSlot(15308), c.getItems().getItemSlot(15312),
				c.getItems().getItemSlot(15316), c.getItems().getItemSlot(15320), c.getItems().getItemSlot(15324) }; // NO
		// IDEA
		// WHAT
		// I
		// WAS
		// DOING
		// ROFLLLL
		int extAtt = 15308, extStr = 15312, extDef = 15316, extMage = 15320, extRange = 15324;
		int attSlot = c.getItems().getItemSlot(15308), strSlot = c.getItems().getItemSlot(15312),
				defSlot = c.getItems().getItemSlot(15316), mageSlot = c.getItems().getItemSlot(15320),
				rangeSlot = c.getItems().getItemSlot(15324);
		int att = c.getItems().getItemAmount(extAtt), str = c.getItems().getItemAmount(extStr),
				def = c.getItems().getItemAmount(extDef), mage = c.getItems().getItemAmount(extMage),
				range = c.getItems().getItemAmount(extRange);
		if (att < 1 || str < 1 || def < 1 || mage < 1 || range < 1) {
			c.sendMessage("You need all five extreme potions!");
			this.removeAllWindows();
			return;
		}
		if (c.level[15] < 96) {
			c.sendMessage("You need 96 herblore to do this!");
			this.removeAllWindows();
			return;
		}
		c.getItems().deleteItem(extAtt, attSlot, 1);
		c.getItems().deleteItem(extStr, strSlot, 1);
		c.getItems().deleteItem(extDef, defSlot, 1);
		c.getItems().deleteItem(extMage, mageSlot, 1);
		c.getItems().deleteItem(extRange, rangeSlot, 1);
		c.getItems().addItem(15332, 1);
	}

	/**
	 * Location change for digging, levers etc
	 */
	public void changeLocation() {
		switch (c.newLocation) {
		case 1:
			this.sendFrame99(2);
			this.movePlayer(3578, 9706, 3);
			break;
		case 2:
			this.sendFrame99(2);
			this.movePlayer(3568, 9683, 3);
			break;
		case 3:
			this.sendFrame99(2);
			this.movePlayer(3557, 9703, 3);
			break;
		case 4:
			this.sendFrame99(2);
			this.movePlayer(3556, 9718, 3);
			break;
		case 5:
			this.sendFrame99(2);
			this.movePlayer(3534, 9704, 3);
			break;
		case 6:
			this.sendFrame99(2);
			this.movePlayer(3546, 9684, 3);
			break;
		}
		c.newLocation = 0;
	}

	public boolean checkForFlags() {
		int[][] itemsToCheck = { { 995, 100000000 }, { 35, 5 }, { 667, 5 }, { 2402, 5 }, { 746, 5 }, { 4151, 150 },
				{ 565, 100000 }, { 560, 100000 }, { 555, 300000 }, { 11235, 10 } };
		for (int j = 0; j < itemsToCheck.length; j++) {
			if (itemsToCheck[j][1] < c.getItems().getTotalCount(itemsToCheck[j][0])) {
				return true;
			}
		}
		return false;
	}

	public boolean checkForPlayer(int x, int y) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				if (p.getX() == x && p.getY() == y) {
					return true;
				}
			}
		}
		return false;
	}

	public void checkPouch(int i) {
		if (i < 0) {
			return;
		}
		c.sendMessage("This pouch has " + c.pouches[i] + " rune ess in it.");
	}

	public void closeAllWindows() {
		c.openInterfaceId = -1;

		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(219);
			c.flushOutStream();
			c.dialogueAction = 0;
			c.dialogueId = 0;
			// c.isBanking = false;
		}
	}

	public void closeInterface() {
		closeAllWindows();
	}

	@SuppressWarnings("unused")
	public void craftAstrals() {
		int runeID = 9075;
		int multiplier = 1;
		int req = 40;
		if (runeID == 9075) {
			if (c.level[20] >= 40) {
				if (c.getItems().playerHasItem(7936)) {
					// c.getRunecrafting().replaceEssence(7936, runeID,
					// multiplier, 9075);
					c.startAnimation(791);
					c.gfx100(186);
					return;
				}
				c.sendMessage("You need to have pure essence to craft astral runes!");
				return;
			}
			c.sendMessage("You need a Runecrafting level of " + req + " to craft this rune.");
		}
	}

	public void createFixedProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int Delay) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(y - (c.getLastKnownRegion().getRegionY() * 8) - 2);
			c.getOutStream().writeByteC(x - (c.getLastKnownRegion().getRegionX() * 8) - 3);
			c.getOutStream().createFrame(117);
			c.getOutStream().writeByte(50);
			c.getOutStream().writeByte(offY);
			c.getOutStream().writeByte(offX);
			c.getOutStream().writeWord(lockon);
			c.getOutStream().writeWord(gfxMoving);
			c.getOutStream().writeByte(startHeight);
			c.getOutStream().writeByte(endHeight);
			c.getOutStream().writeWord(Delay);
			c.getOutStream().writeWord(speed);
			c.getOutStream().writeByte(angle);
			c.getOutStream().writeByte(64);
			c.flushOutStream();
		}
	}

	public void createObjectHints(int x, int y, int height, int pos) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(254);
			c.getOutStream().writeByte(pos);
			c.getOutStream().writeWord(x);
			c.getOutStream().writeWord(y);
			c.getOutStream().writeByte(height);
			c.flushOutStream();
		}
	}

	public void createPlayerHints(int type, int id) {
		// synchronized(c) {
		if (c.getOutStream() != null) {
			c.getOutStream().createFrame(254);
			c.getOutStream().writeByte(type);
			c.getOutStream().writeWord(id);
			c.getOutStream().write3Byte(0);
			c.flushOutStream();
		}
	}

	public void createPlayersObjectAnim(int X, int Y, int animationID, int tileObjectType, int orientation) {
		try {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(Y - (c.getLastKnownRegion().getRegionY() * 8));
			c.getOutStream().writeByteC(X - (c.getLastKnownRegion().getRegionX() * 8));
			int x = 0;
			int y = 0;
			c.getOutStream().createFrame(160);
			c.getOutStream().writeByteS(((x & 7) << 4) + (y & 7));// tiles away - could just send 0
			c.getOutStream().writeByteS((tileObjectType << 2) + (orientation & 3));
			c.getOutStream().writeWordA(animationID);// animation id
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// projectiles for everyone within 25 squares
	public void createPlayersProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time) {
		// synchronized(c) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Player person = (Player) p;
				// if(person.getOutStream() != null) {
				if (person.distanceToPoint(x, y) <= 25) {
					if (p.heightLevel == c.heightLevel) {
						person.getPA().createProjectile(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
								endHeight, lockon, time);
					}
					// }
				}
			}
		}
	}

	public void createPlayersProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope) {
		// synchronized(c) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Player person = (Player) p;
				if (person != null) {
					// if(person.getOutStream() != null) {
					if (person.distanceToPoint(x, y) <= 25) {
						person.getPA().createProjectile2(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
								endHeight, lockon, time, slope);
					}
					// }
				}
			}
		}
	}

	// creates gfx for everyone
	public void createPlayersStillGfx(int id, int x, int y, int height, int time) {
		// synchronized(c) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Player person = (Player) p;
				if (person != null) {
					// if(person.getOutStream() != null) {
					if (person.distanceToPoint(x, y) <= 25 && person.heightLevel == height) {
						person.getPA().stillGfx(id, x, y, height, time);
					}
					// }
				}
			}
		}
	}

	/**
	 * Creating projectile
	 */
	public void createProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving, int startHeight,
			int endHeight, int lockon, int time) {
		// synchronized(c) {
		// if(c.getOutStream() != null && c != null) {
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC(y - c.getLastKnownRegion().getRegionY() * 8 - 2);
		c.getOutStream().writeByteC(x - c.getLastKnownRegion().getRegionX() * 8 - 3);

		c.getOutStream().createFrame(117);
		c.getOutStream().writeByte(angle);
		c.getOutStream().writeByte(offY);
		c.getOutStream().writeByte(offX);
		c.getOutStream().writeWord(lockon);
		c.getOutStream().writeWord(gfxMoving);
		c.getOutStream().writeByte(startHeight);
		c.getOutStream().writeByte(endHeight);
		c.getOutStream().writeWord(time);
		c.getOutStream().writeWord(speed);
		c.getOutStream().writeByte(16);
		c.getOutStream().writeByte(64);
		c.flushOutStream();
		// }
	}

	public void createProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope) {
		// synchronized(c) {
		// if(c.getOutStream() != null && c != null) {
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC(y - (c.getLastKnownRegion().getRegionY() * 8) - 2);
		c.getOutStream().writeByteC(x - (c.getLastKnownRegion().getRegionX() * 8) - 3);
		c.getOutStream().createFrame(117);
		c.getOutStream().writeByte(angle);
		c.getOutStream().writeByte(offY);
		c.getOutStream().writeByte(offX);
		c.getOutStream().writeWord(lockon);
		c.getOutStream().writeWord(gfxMoving);
		c.getOutStream().writeByte(startHeight);
		c.getOutStream().writeByte(endHeight);
		c.getOutStream().writeWord(time);
		c.getOutStream().writeWord(speed);
		c.getOutStream().writeByte(slope);
		c.getOutStream().writeByte(64);
		c.flushOutStream();
		// }
	}

	public void customObject(int objectId, int objectX, int objectY, int objectZ, int face, int objectType) {

		if (c.heightLevel != objectZ || c.distanceToPoint(objectX, objectY) > 64) {
			return;
		}

		Location loc = Location.create(objectX, objectY, objectZ);

		Optional<MapObject> obj = RegionUtility.getMapObject(objectId, loc);
		if (!obj.isPresent()) {
			RegionUtility.addEntity(new DynamicMapObject(objectId, loc, objectType, face), loc);
		}

		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC(objectY - (c.getLastKnownRegion().getRegionY() * 8));
		c.getOutStream().writeByteC(objectX - (c.getLastKnownRegion().getRegionX() * 8));

		c.getOutStream().createFrame(101);
		c.getOutStream().writeByteC((objectType << 2) + (face & 3));
		c.getOutStream().writeByte(0);

		if (objectId != -1) { // removing
			Collision.clipObject(objectId, objectX, objectY, objectZ, objectType, face);
			c.getOutStream().createFrame(151);
			c.getOutStream().writeByteS(0);
			c.getOutStream().writeWordBigEndian(objectId);
			c.getOutStream().writeByteS((objectType << 2) + (face & 3));
		} else {
			Collision.removeClipping(objectX, objectY, objectZ);
		}

		c.flushOutStream();
	}

	public void desertTreasure() {
		this.sendFrame126("Desert Treasure", 8144);
		if (c.desertStage == 0) {
			this.sendFrame126("I can start this quest by talking to the King.", 8147);
			this.sendFrame126("He is located at Catherby.", 8148);
			this.sendFrame126("", 8149);
			this.sendFrame126("", 8150);
		}
		if (c.desertStage == 1) {
			this.sendFrame126("The king has given me the first task.", 8147);
			this.sendFrame126("He wants me to steal a cake at Ardougne.", 8148);
			this.sendFrame126("", 8149);
			this.sendFrame126("", 8150);
		}
		if (c.desertStage == 2) {
			this.sendFrame126("The king has given me the second task.", 8147);
			this.sendFrame126("He wants me to get a magic shortbow.", 8148);
			this.sendFrame126("", 8149);
			this.sendFrame126("", 8150);
		}
		if (c.desertStage == 3) {
			this.sendFrame126("The king has given me the last task.", 8147);
			this.sendFrame126("He wants me to battle 4 bosses.", 8148);
			this.sendFrame126("", 8149);
			this.sendFrame126("", 8150);
		}
		if (c.desertStage == 15) {
			this.sendFrame126("I have completed the quest!", 8147);
			this.sendFrame126("I can now use ancient magic.", 8148);
			this.sendFrame126("", 8149);
			this.sendFrame126("", 8150);
		}
		this.showInterface(8134);
		c.flushOutStream();
	}

	public void openTab(int index) {
		c.outStream.createFrame(24);
		c.outStream.writeByteS(index);
	}

	public void drawHeadicon(HintType type, int id, Location location, int face) {
		c.outStream.createFrame(254);
		if (type.equals(HintType.NPC) || type.equals(HintType.PLAYER)) {
			c.outStream.writeByte(type.equals(HintType.NPC) ? 1 : 10);
			c.outStream.writeWord(id);
			c.outStream.write3Byte(0);
		} else {
			c.outStream.writeByte(face);
			c.outStream.writeWord(location.getX());
			c.outStream.writeWord(location.getY());
			c.outStream.writeByte(location.getZ());
		}
	}

	public int expMultiplier() {
		int multiplier = 1;
		if (c.equipment[c.playerRing] == 6465) {
			multiplier += 1;
		}
		return multiplier;
	}

	public void resetDamageDoneByPlayers() {
		for (int i = 0; i < c.damageTaken.length; i++) {
			c.damageTaken[i] = -1;
		}
	}

	public int findKiller() {
		int killer = c.playerId;
		int damage = 0;
		for (int j = 0; j < Config.MAX_PLAYERS; j++) {
			if (PlayerHandler.players[j] == null) {
				continue;
			}
			if (j == c.playerId) {
				continue;
			}
			if (c.goodDistance(c.absX, c.absY, PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, 40)
					|| c.goodDistance(c.absX, c.absY + 9400, PlayerHandler.players[j].absX,
							PlayerHandler.players[j].absY, 40)
					|| c.goodDistance(c.absX, c.absY, PlayerHandler.players[j].absX,
							PlayerHandler.players[j].absY + 9400, 40)) {
				if (c.damageTaken[j] > damage) {
					damage = c.damageTaken[j];
					killer = j;
				}
			}
		}
		return killer;
	}

	public void fixAllBarrows() {
		int totalCost = 0;
		int cashAmount = c.getItems().getItemAmount(995);
		for (int j = 0; j < c.inventory.length; j++) {
			boolean breakOut = false;
			for (int i = 0; i < c.getItems().brokenBarrows.length; i++) {
				if (c.inventory[j] - 1 == c.getItems().brokenBarrows[i][1]) {
					if (totalCost + 80000 > cashAmount) {
						breakOut = true;
						c.sendMessage("You have run out of money.");
						break;
					} else {
						totalCost += 80000;
					}
					c.inventory[j] = c.getItems().brokenBarrows[i][0] + 1;
				}
			}
			if (breakOut) {
				break;
			}
		}
		if (totalCost > 0) {
			c.getItems().deleteItem(995, c.getItems().getItemSlot(995), totalCost);
		}

		c.getItems().resetItems(3214);
	}

	public void follow() {
		Entity other = null;

		if (c.followId > 0) {
			other = PlayerHandler.players[c.followId];
		} else if (c.followId2 > 0) {
			other = NPCHandler.npcs[c.followId2];
		}

		// other player checks
		if (other == null || other.isDead()) {
			resetFollow();
			c.stopMovement();
			return;
		}

		// our checks
		if (c.isDead || c.level[3] <= 0) {
			resetFollow();
			c.stopMovement();
			return;
		}

		// make sure we are not frozen
		if (c.freezeTimer > 0) {
			c.stopMovement();
			return;
		}

		// make sure were in distance of actually following
		if (!c.getLocation().isWithinDistance(other.getLocation())) {
			resetFollow();
			c.stopMovement();
			return;
		}

		// tell the client we need to face the other player
		if (other instanceof Player) {
			c.faceUpdate(c.followId + 32768);
		} else {
			c.faceUpdate(c.followId2);
		}

		// see if we are on the same spot
		boolean sameSpot = c.getLocation().equals(other.getLocation());

		// calculate the distance
		int distance = CombatHelper.getRequiredDistance(c);
		/*
		 * if (c.usingBow || c.mageFollow || c.usingMagic || c.autocastId > 0) {
		 * distance = 8; } if (c.getCombat().usingHally()) { distance = 2; } if
		 * (c.usingRangeWeapon) { distance = 4; }
		 */

		// System.out.println("distance: " + distance);

		int offsetX = 0;
		int offsetY = 0;

		if (other instanceof NPC) {
			NPC npc = (NPC) other;
			offsetX = NPCConfig.offset(npc);
			offsetY = NPCConfig.offset(npc);
		}

		boolean canAttack = true;
		// only perform range checks whilst in combat
		if (distance > 1 && (c.playerIndex > 0 || c.npcIndex > 0)) {
			Location otherLocation = other.getLocation();
			if (!Collision.canProjectileMove(c.absX, c.absY, otherLocation.getX() + offsetX,
					otherLocation.getY() + offsetY, c.heightLevel, 1, 1)) {
				canAttack = false;
			}
		}

		// now we can check if we even need to move!
		if (canAttack && !sameSpot && Location.isWithinDistance(c.getLocation(),
				other.getLocation().transform(offsetX, offsetY, 0), 0, distance)) {
			c.stopMovement();
			return;
		}

		// int otherX = other.getLocation().getX();
		// int otherY = other.getLocation().getY();

		// if we're on the same spot we need to move away from the player, we may need
		// to check if the other player is already walking away! otherwise we can just
		// move closer to the other player
		if (sameSpot) {
			find: for (int i = 0; i < 4; i++) {
				switch (i) {
				case 0: // walking south
					if (!Collision.blockedSouth(c.getX(), c.getY(), c.getHeightLevel())) {
						walkTo(0, -1);
						break find;
					}
					break;
				case 1: // walking north
					if (!Collision.blockedNorth(c.getX(), c.getY(), c.getHeightLevel())) {
						walkTo(0, 1);
						break find;
					}
					break;
				case 2: // east
					if (!Collision.blockedEast(c.getX(), c.getY(), c.getHeightLevel())) {
						walkTo(1, 0);
						break find;
					}
					break;
				case 3: // west
					if (!Collision.blockedWest(c.getX(), c.getY(), c.getHeightLevel())) {
						walkTo(-1, 0);
						break find;
					}
					break;
				}
			}
			return;
		}

		/*
		 * int x = other.getLocation().getX() + offsetX; int y =
		 * other.getLocation().getY() + offsetY;
		 * 
		 * boolean walkN = !Collision.blockedNorth(c.absX, c.absY, c.heightLevel);
		 * boolean walkE = !Collision.blockedEast(c.absX, c.absY, c.heightLevel);
		 * boolean walkS = !Collision.blockedSouth(c.absX, c.absY, c.heightLevel);
		 * boolean walkW = !Collision.blockedWest(c.absX, c.absY, c.heightLevel);
		 * 
		 * System.out.println(walkN + " " + walkE + " " + walkS + " " + walkW);
		 * 
		 * if (c.absX < x) { if (walkW) {
		 * PlayerPathFinder.getPlayerPathFinder().findRoute(c, x, y, true, 1, 1); } else
		 * if (c.absY > y && walkN) {
		 * PlayerPathFinder.getPlayerPathFinder().findRoute(c, x, y + 1, true, 1, 1); }
		 * else if (c.absY < y && walkS) {
		 * PlayerPathFinder.getPlayerPathFinder().findRoute(c, x, y - 1, true, 1, 1); }
		 * else if (walkN) { PlayerPathFinder.getPlayerPathFinder().findRoute(c, x, y +
		 * 1, true, 1, 1); } else if (walkS) {
		 * PlayerPathFinder.getPlayerPathFinder().findRoute(c, x, y - 1, true, 1, 1); }
		 * else if (walkE) { PlayerPathFinder.getPlayerPathFinder().findRoute(c, x + 1,
		 * y, true, 1, 1); } } else if (c.absX > x) { if (walkE) {
		 * PlayerPathFinder.getPlayerPathFinder().findRoute(c, x + 1, y, true, 1, 1); }
		 * else if (c.absY > y && walkN) {
		 * PlayerPathFinder.getPlayerPathFinder().findRoute(c, x, y + 1, true, 1, 1); }
		 * else if (c.absY < y && walkS) {
		 * PlayerPathFinder.getPlayerPathFinder().findRoute(c, x, y - 1, true, 1, 1); }
		 * else if (walkN) { PlayerPathFinder.getPlayerPathFinder().findRoute(c, x, y +
		 * 1, true, 1, 1); } else if (walkS) {
		 * PlayerPathFinder.getPlayerPathFinder().findRoute(c, x, y - 1, true, 1, 1); }
		 * else if (walkW) { PlayerPathFinder.getPlayerPathFinder().findRoute(c, x - 1,
		 * y, true, 1, 1); } } else if (c.absY > y) { if (walkN) {
		 * PlayerPathFinder.getPlayerPathFinder().findRoute(c, x, y + 1, true, 1, 1); }
		 * else if (c.absX > x && walkE) {
		 * PlayerPathFinder.getPlayerPathFinder().findRoute(c, x + 1, y, true, 1, 1); }
		 * else if (c.absX < x && walkW) {
		 * PlayerPathFinder.getPlayerPathFinder().findRoute(c, x - 1, y, true, 1, 1); }
		 * else if (walkE) { PlayerPathFinder.getPlayerPathFinder().findRoute(c, x + 1,
		 * y, true, 1, 1); } else if (walkW) {
		 * PlayerPathFinder.getPlayerPathFinder().findRoute(c, x - 1, y, true, 1, 1); }
		 * else if (walkS) { PlayerPathFinder.getPlayerPathFinder().findRoute(c, x, y -
		 * 1, true, 1, 1); } } else if (c.absY < y) { if (walkS) {
		 * PlayerPathFinder.getPlayerPathFinder().findRoute(c, x, y - 1, true, 1, 1); }
		 * else if (c.absX > x && walkE) {
		 * PlayerPathFinder.getPlayerPathFinder().findRoute(c, x + 1, y, true, 1, 1); }
		 * else if (c.absX < x && walkW) {
		 * PlayerPathFinder.getPlayerPathFinder().findRoute(c, x - 1, y, true, 1, 1); }
		 * else if (walkE) { PlayerPathFinder.getPlayerPathFinder().findRoute(c, x + 1,
		 * y, true, 1, 1); } else if (walkW) {
		 * PlayerPathFinder.getPlayerPathFinder().findRoute(c, x - 1, y, true, 1, 1); }
		 * else if (walkN) { PlayerPathFinder.getPlayerPathFinder().findRoute(c, x, y +
		 * 1, true, 1, 1); } }
		 */

		PlayerPathFinder.getPlayerPathFinder().findRoute(c, other.getLocation().getX() + offsetX,
				other.getLocation().getY() + offsetY, true, 1, 1);
	}

	/**
	 * Reseting animations for everyone
	 */
	public void frame1() {
		// synchronized(c) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Player person = PlayerHandler.players[i];
				if (person != null) {
					if (/* person.getOutStream() != null && */!person.isDisconnected()) {
						if (c.distanceToPoint(person.getX(), person.getY()) <= 25) {
							person.getOutStream().createFrame(1);
							person.flushOutStream();
							person.getPA().requestUpdates();
						}
					}
				}
			}
		}
	}

	public void frame61(int i1) {
		c.outStream.createFrame(61);
		c.outStream.writeByte(i1);
		c.flushOutStream();
	}

	public boolean fullGuthans() {
		return c.equipment[c.playerHat] == 4724 && c.equipment[c.playerChest] == 4728
				&& c.equipment[c.playerLegs] == 4730 && c.equipment[Player.playerWeapon] == 4726;
	}

	public boolean fullVeracs() {
		return c.equipment[c.playerHat] == 4753 && c.equipment[c.playerChest] == 4757
				&& c.equipment[c.playerLegs] == 4759 && c.equipment[Player.playerWeapon] == 4755;
	}

	public int getActualLevel(int level) {
		return getLevelForXP(c.xp[level]);
	}

	/**
	 * Gets the players clan.
	 *
	 * @return
	 */
	public Clan getClan() {
		if (Server.clanManager.clanExists(c.username)) {
			return Server.clanManager.getClan(c.username);
		}
		return null;
	}

	public void getMods() {
		onlineMods.clear();
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].rights == 1) {
					if (c != null) {
						Player c = PlayerHandler.players[i];
						onlineMods.add(c.username);
					}
				}
			}
		}
	}

	public int getMove(int place1, int place2) {
		if (System.currentTimeMillis() - c.lastSpear < 4000) {
			return 0;
		}
		if ((place1 - place2) == 0) {
			return 0;
		} else if ((place1 - place2) < 0) {
			return 1;
		} else if ((place1 - place2) > 0) {
			return -1;
		}
		return 0;
	}

	public int getNpcId(int id) {
		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] != null) {
				if (NPCHandler.npcs[i].id == id) {
					return i;
				}
			}
		}
		return -1;
	}

	public int getRunningMove(int i, int j) {
		if (j - i > 2) {
			return 2;
		} else if (j - i < -2) {
			return -2;
		} else {
			return j - i;
		}
	}

	public void getSpeared(int otherX, int otherY) {
		int x = c.absX - otherX;
		int y = c.absY - otherY;
		if (x > 0) {
			x = 1;
		} else if (x < 0) {
			x = -1;
		}
		if (y > 0) {
			y = 1;
		} else if (y < 0) {
			y = -1;
		}
		this.moveCheck(x, y);
		c.lastSpear = System.currentTimeMillis();
	}

	public int getTotalLevel() {
		int level = 0;
		for (int i = 0; i < c.xp.length; i++) {
			level += getLevelForXP(c.xp[i]);
		}
		return level;
	}

	public long getTotalXP() {
		long level = 0;
		for (int i = 0; i < c.xp.length; i++) {
			level += c.xp[i];
		}
		return level;
	}

	public int getWearingAmount() {
		int count = 0;
		for (int j = 0; j < c.equipment.length; j++) {
			if (c.equipment[j] > 0) {
				count++;
			}
		}
		return count;
	}

	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor((double) lvl + 300.0 * Math.pow(2.0, (double) lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public void giveLife() {

		c.isDead = false;

		c.faceUpdate(-1);
		c.freezeTimer = 0;

		War war = (War) c.getAttribute("clan_war");
		if (war != null && war.looseItems() || DuelArena.dropItems(c) && !c.inPestControl && !c.inPits && !c.inPcGame()
				&& !c.inLobby() && !c.attributeExists("in_fight_caves") && !c.attributeExists("in_fight_kiln")
				&& !ClanWars.inArena(c) && !ClanWars.inJail(c) && !ClanWars.inWhitePortalArea(c)
				&& c.getCastleWarsState() == null && !c.getPA().inPitsWait() && !c.inPcGame() && !c.inPits()
				&& !c.inLobby()) { // if we
			c.getItems().keepItemsOnDeath(c.getItems().amountOfItemsKeptOnDeath(), false);
		}

		c.getCombat().resetAllPrayers();
		for (int i = 0; i < 20; i++) {
			c.level[i] = this.getLevelForXP(c.xp[i]);
			c.getPA().refreshSkill(i);
		}
		if (!c.inPits() && !c.inPcGame()) {
			if (c.inTrinityWar()) {
				c.gameReady = false;
			}
		}

		if (c.inPestControl) {
			PestControl.playerDied(c);
		} else if (c.attributeExists("in_fight_caves")) {
			FightCaves.onPlayerDied(c);
			c.getPA().movePlayer(2438, 5169, 0);
		} else if (c.attributeExists("in_fight_kiln")) {
			FightKiln.onPlayerDied(c);
			c.getPA().movePlayer(2438, 5169, 0);
		} else if (ClanWars.inArena(c)) {
			ClanWars.playerDied(c);
		} else if (c.inPits()) {
			this.movePlayer(2399, 5177, 0);
			for (int i = 0; i < 20; i++) {
				c.level[i] = this.getLevelForXP(c.xp[i]);
				c.getPA().refreshSkill(i);
			}
		} else if (c.inPcGame()) {
			c.getPA().movePlayer(2655, 2607, c.heightLevel);
		} else if (c.inDuelArena()) {
			DuelArena.playerDied(c);
		} else if (c.getCastleWarsState() != null) {
			CastleWars.onPlayerDeath(c);
		} else if (c.attributeExists("zombie_game")) {
			Zombies.onPlayerDied(c);
		} else {
			this.movePlayer(Config.RESPAWN_X, Config.RESPAWN_Y, 0);
		}

		GameCycleTaskHandler.stopEvents("overload-damage" + c.username);
		c.inPits = false;
		c.pitsStatus = 0;
		c.isSkulled = false;
		c.skullTimer = 0;
		c.attackedPlayers.clear();
		if (c.inPits() || c.inLobby()) {
			this.movePlayer(2400 + Misc.random(2), 5179 + Misc.random(2), 0);
		}
		/*
		 * if (c.clanDropBypass) { Location l =
		 * c.clan.getClanWarMap().getDeathLocations()[c.clan.getWarIndex()];
		 * movePlayer(l.getX(), l.getY(), 0); }
		 */
		c.getCombat().resetPlayerAttack();
		this.resetAnimation();
		c.startAnimation(65535);
		this.frame1();
		this.resetTb();
		c.getPA().createPlayerHints(10, -1);
		c.isSkulled = false;
		c.attackedPlayers.clear();
		c.headIconPk = -1;
		c.skullTimer = -1;
		c.damageTaken = new int[Config.MAX_PLAYERS];
		c.getPA().requestUpdates();// brb
		this.removeAllWindows();
		c.tradeResetNeeded = true;
		c.removeAttribute("died_to_player");
	}

	public void handleAlt(int id) {
		if (!c.getItems().playerHasItem(id)) {
			c.getItems().addItem(id, 1);
		}
	}

	public void handleGlory(int gloryId) {
		c.getDH().sendOption4("Edgeville", "Al Kharid", "Draynor", "Karamja");
		c.usingGlory = true;
	}

	public void rowTeleports() {
		c.getPA().removeAllWindows();
		c.getDH().sendOption3("Miscellania", "Karamja", "Crandor");
		c.usingRingOfWealth = true;
	}

	public void handleLoginText() {
		c.getPA().sendFrame126("Varrock Teleport", 13037);
		c.getPA().sendFrame126("Lumbridge Teleport", 13047);
		c.getPA().sendFrame126("Falador Teleport", 13055);
		c.getPA().sendFrame126("Camelot Teleport", 13063);
		c.getPA().sendFrame126("Ardougne Teleport", 13071);
		c.getPA().sendFrame126("Varrock Teleport", 1300);
		c.getPA().sendFrame126("Lumbridge Teleport", 1325);
		c.getPA().sendFrame126("Falador Teleport", 1350);
		c.getPA().sendFrame126("Camelot Teleport", 1382);
		c.getPA().sendFrame126("Ardougne Teleport", 1415);
		// c.getPA().sendFrame126(
		// "Players Online: " + PlayerHandler.getPlayerCount(), 640);
		c.getPA().sendFrame126("", 7333);
		c.getPA().sendFrame126("", 7334);
		c.getPA().sendFrame126("", 7336);
		c.getPA().sendFrame126("", 7383);
		c.getPA().sendFrame126("", 7339);
		c.getPA().sendFrame126("", 7338);
		c.getPA().sendFrame126("", 7340);
		c.getPA().sendFrame126("", 7346);
		c.getPA().sendFrame126("", 7341);
		c.getPA().sendFrame126("", 7342);
		c.getPA().sendFrame126("", 7337);
		c.getPA().sendFrame126("", 7343);
	}

	public void highGFX(int gfx) {
		c.mask100var1 = gfx;
		c.mask100var2 = 6553600;
		c.mask100update = true;
		c.updateRequired = true;
	}

	public boolean inMiniGame() {
		return c.inPits || c.inFightPits() || c.getDuel() != null || c.getCastleWarsState() != null
				|| c.attributeExists("zombie_game");
	}

	public boolean inPitsWait() {
		return c.getX() <= 2404 && c.getX() >= 2394 && c.getY() <= 5175 && c.getY() >= 5169;
	}

	public void latestUpdate() {
		Connection.saveIpToLog(c.username, c.connectedFrom);
		this.sendFrame126("full 15244 15767", 0);
		this.sendFrame126("Welcome to Proelium-Pk!", 15257);
		int days = PlayerAssistant.getCurrentDay() - c.lastConnectionDay;
		if (days == 0) {
			this.sendFrame126("You last logged in @red@Earlier Today@bla@ from @red@" + c.lastConnection, 15258);
		}
		if (days == 1) {
			this.sendFrame126("You last logged in @red@Yesterday@bla@ from @red@" + c.lastConnection, 15258);
		}
		if (days > 1) {
			this.sendFrame126("You last logged in @red@" + days + "@bla@ day ago from @red@" + c.lastConnection, 15258);
		}
		this.sendFrame126("You have @red@no @yel@unread messages\\nin your message centre.", 15261);
		if (c.isMember == 0) {
			this.sendFrame126("\\nYou haven't got any member\\nCredit on your account\\nDonate to upgrade", 15262);
		}
		if (c.isMember == 1) {
			this.sendFrame126("\\nYou have donated.,\\nYour member credit is set\\nto Donator.", 15262);
		}
		if (c.isMember == 2) {
			this.sendFrame126("\\nYou have donated.,\\nYour member credit is set\\nto Super Donator.", 15262);
		}
		if (c.isMember == 3) {
			this.sendFrame126("\\nYou have donated.,\\nYour member credit is set\\nto Extreme Donator.", 15262);
		}
		this.sendFrame126("\\nYou do not have a bank PIN. This feature \\nWill be added soon.", 15270);
		/*
		 * Bottom part
		 */
		this.sendFrame126("Welcome to Proelium-Pk.", 15769);
		this.sendFrame126("Stay active on the forums!", 15770);
		/*
		 * sendFrame126("@dbl@Last Update", 8144); //Title
		 * sendFrame126("Infinity robes are released!", 8146);
		 * sendFrame126("Morrigan's Thrown Axes have been released!", 8147);
		 * sendFrame126("Vesta's Spear has been released!!", 8148);
		 * sendFrame126("They are all EP drops", 8149);
		 * sendFrame126("The druids at the druid teleport now drop herbs and seeds"
		 * ,8150); sendFrame126(
		 * "The potion stock has been dropped to 50, allowing more profit", 8151);
		 * sendFrame126("through herblore", 8152); sendFrame126("Thanks,", 8153);
		 * sendFrame126("DeadlyPkerz staff", 8154);
		 * sendFrame126("Dont forget to vote and donate!", 8156); for (int i = 0; i <
		 * 100; i++) { sendFrame126("", 8156+i); } c.getPA().showInterface(8134);
		 * c.flushOutStream();
		 */
	}

	public void levelUp(int skill) {
		this.sendFrame126("Total Lvl: " + getTotalLevel(), 7127);
		switch (skill) {
		case 0:
			this.sendFrame126("Congratulations, you just advanced an attack level!", 6248);
			this.sendFrame126("Your attack level is now " + this.getLevelForXP(c.xp[skill]) + ".", 6249);
			c.sendMessage("Congratulations, you just advanced an attack level.");
			this.sendFrame164(6247);
			break;
		case 1:
			this.sendFrame126("Congratulations, you just advanced a defence level!", 6254);
			this.sendFrame126("Your defence level is now " + this.getLevelForXP(c.xp[skill]) + ".", 6255);
			c.sendMessage("Congratulations, you just advanced a defence level.");
			this.sendFrame164(6253);
			break;
		case 2:
			this.sendFrame126("Congratulations, you just advanced a strength level!", 6207);
			this.sendFrame126("Your strength level is now " + this.getLevelForXP(c.xp[skill]) + ".", 6208);
			c.sendMessage("Congratulations, you just advanced a strength level.");
			this.sendFrame164(6206);
			break;
		case 3:
			this.sendFrame126("Congratulations, you just advanced a hitpoints level!", 6217);
			this.sendFrame126("Your hitpoints level is now " + this.getLevelForXP(c.xp[skill]) + ".", 6218);
			c.sendMessage("Congratulations, you just advanced a hitpoints level.");
			this.sendFrame164(6216);
			break;
		case 4:
			this.sendFrame126("Congratulations, you just advanced a ranged level!", 5453);
			this.sendFrame126("Your ranged level is now " + this.getLevelForXP(c.xp[skill]) + ".", 6114);
			c.sendMessage("Congratulations, you just advanced a ranging level.");
			this.sendFrame164(4443);
			break;
		case 5:
			this.sendFrame126("Congratulations, you just advanced a prayer level!", 6243);
			this.sendFrame126("Your prayer level is now " + this.getLevelForXP(c.xp[skill]) + ".", 6244);
			c.sendMessage("Congratulations, you just advanced a prayer level.");
			this.sendFrame164(6242);
			break;
		case 6:
			this.sendFrame126("Congratulations, you just advanced a magic level!", 6212);
			this.sendFrame126("Your magic level is now " + this.getLevelForXP(c.xp[skill]) + ".", 6213);
			c.sendMessage("Congratulations, you just advanced a magic level.");
			this.sendFrame164(6211);
			break;
		case 7:
			this.sendFrame126("Congratulations, you just advanced a cooking level!", 6227);
			this.sendFrame126("Your cooking level is now " + this.getLevelForXP(c.xp[skill]) + ".", 6228);
			c.sendMessage("Congratulations, you just advanced a cooking level.");
			this.sendFrame164(6226);
			break;
		case 8:
			this.sendFrame126("Congratulations, you just advanced a woodcutting level!", 4273);
			this.sendFrame126("Your woodcutting level is now " + this.getLevelForXP(c.xp[skill]) + ".", 4274);
			c.sendMessage("Congratulations, you just advanced a woodcutting level.");
			this.sendFrame164(4272);
			break;
		case 9:
			this.sendFrame126("Congratulations, you just advanced a fletching level!", 6232);
			this.sendFrame126("Your fletching level is now " + this.getLevelForXP(c.xp[skill]) + ".", 6233);
			c.sendMessage("Congratulations, you just advanced a fletching level.");
			this.sendFrame164(6231);
			break;
		case 10:
			this.sendFrame126("Congratulations, you just advanced a fishing level!", 6259);
			this.sendFrame126("Your fishing level is now " + this.getLevelForXP(c.xp[skill]) + ".", 6260);
			c.sendMessage("Congratulations, you just advanced a fishing level.");
			this.sendFrame164(6258);
			break;
		case 11:
			this.sendFrame126("Congratulations, you just advanced a fire making level!", 4283);
			this.sendFrame126("Your firemaking level is now " + this.getLevelForXP(c.xp[skill]) + ".", 4284);
			c.sendMessage("Congratulations, you just advanced a fire making level.");
			this.sendFrame164(4282);
			break;
		case 12:
			this.sendFrame126("Congratulations, you just advanced a crafting level!", 6264);
			this.sendFrame126("Your crafting level is now " + this.getLevelForXP(c.xp[skill]) + ".", 6265);
			c.sendMessage("Congratulations, you just advanced a crafting level.");
			this.sendFrame164(6263);
			break;
		case 13:
			this.sendFrame126("Congratulations, you just advanced a smithing level!", 6222);
			this.sendFrame126("Your smithing level is now " + this.getLevelForXP(c.xp[skill]) + ".", 6223);
			c.sendMessage("Congratulations, you just advanced a smithing level.");
			this.sendFrame164(6221);
			break;
		case 14:
			this.sendFrame126("Congratulations, you just advanced a mining level!", 4417);
			this.sendFrame126("Your mining level is now " + this.getLevelForXP(c.xp[skill]) + ".", 4438);
			c.sendMessage("Congratulations, you just advanced a mining level.");
			this.sendFrame164(4416);
			break;
		case 15:
			this.sendFrame126("Congratulations, you just advanced a herblore level!", 6238);
			this.sendFrame126("Your herblore level is now " + this.getLevelForXP(c.xp[skill]) + ".", 6239);
			c.sendMessage("Congratulations, you just advanced a herblore level.");
			this.sendFrame164(6237);
			break;
		case 16:
			this.sendFrame126("Congratulations, you just advanced a agility level!", 4278);
			this.sendFrame126("Your agility level is now " + this.getLevelForXP(c.xp[skill]) + ".", 4279);
			c.sendMessage("Congratulations, you just advanced an agility level.");
			this.sendFrame164(4277);
			break;
		case 17:
			this.sendFrame126("Congratulations, you just advanced a thieving level!", 4263);
			this.sendFrame126("Your theiving level is now " + this.getLevelForXP(c.xp[skill]) + ".", 4264);
			c.sendMessage("Congratulations, you just advanced a thieving level.");
			this.sendFrame164(4261);
			break;
		case 18:
			this.sendFrame126("Congratulations, you just advanced a slayer level!", 12123);
			this.sendFrame126("Your slayer level is now " + this.getLevelForXP(c.xp[skill]) + ".", 12124);
			c.sendMessage("Congratulations, you just advanced a slayer level.");
			this.sendFrame164(12122);
			break;
		case 20:
			this.sendFrame126("Congratulations, you just advanced a runecrafting level!", 4268);
			this.sendFrame126("Your runecrafting level is now " + this.getLevelForXP(c.xp[skill]) + ".", 4269);
			c.sendMessage("Congratulations, you just advanced a runecrafting level.");
			this.sendFrame164(4267);
			break;
		}
		c.dialogueAction = 0;
		c.nextChat = 0;
	}

	/**
	 * Magic on items
	 */
	public void magicOnItems(int slot, int itemId, int spellId) {
		if (!c.getItems().playerHasItem(itemId, 1)) {
			// add a method here for logging cheaters(If you want)
			return;
		}
		switch (spellId) {
		case 1162: // low alch
			if (!c.getItems().playerHasItem(itemId, 1)) {
				return;
			}
			if (System.currentTimeMillis() - c.alchDelay > 1000) {
				if (!c.getCombat().checkMagicReqs(49)) {
					break;
				}
				if (itemId == 995) {
					c.sendMessage("You can't alch coins");
					break;
				}
				c.lastAlch = System.currentTimeMillis();
				c.getItems().deleteItem(itemId, slot, 1);
				c.getItems().addItem(995, ItemUtility.getValue(itemId) / 3);

				if (c.equipment[3] > 0 && ItemUtility.getName(c.equipment[3]).toLowerCase().contains("staff")) {
					c.startAnimation(9633);
					c.gfx0(112);
				} else {
					c.startAnimation(713);
					c.gfx0(113);
				}

				c.startAnimation(Player.MAGIC_SPELLS[49][2]);
				c.gfx0(Player.MAGIC_SPELLS[49][3]);
				c.alchDelay = System.currentTimeMillis();
				this.sendFrame106(6);
				this.addSkillXP(Player.MAGIC_SPELLS[49][7], 6);
			}
			break;
		case 1178: // high alch
			if (!c.getItems().playerHasItem(itemId, 1)) {
				return;
			}
			if (System.currentTimeMillis() - c.alchDelay > 2000) {
				if (!c.getCombat().checkMagicReqs(50)) {
					break;
				}
				if (itemId == 995) {
					c.sendMessage("You can't alch coins");
					break;
				}
				c.lastAlch = System.currentTimeMillis();
				c.getItems().deleteItem(itemId, slot, 1);
				c.getItems().addItem(995, (int) (ItemUtility.getValue(itemId) * .75));
				if (c.equipment[3] > 0 && ItemUtility.getName(c.equipment[3]).toLowerCase().contains("staff")) {
					c.startAnimation(9633);
					c.gfx0(112);
				} else {
					c.startAnimation(713);
					c.gfx0(113);
				}
				c.alchDelay = System.currentTimeMillis();
				this.sendFrame106(6);
				this.addSkillXP(Player.MAGIC_SPELLS[50][7], 6);
			}
			break;
		case 1155: // Lvl-1 enchant sapphire
		case 1165: // Lvl-2 enchant emerald
		case 1176: // Lvl-3 enchant ruby
		case 1180: // Lvl-4 enchant diamond
		case 1187: // Lvl-5 enchant dragonstone
		case 6003: // Lvl-6 enchant onyx
			EnchantingMagic.enchantItem(c, itemId, spellId);
			break;
		}
	}

	public void sendColorInterface(int item) {
		PacketBuilder builder = new PacketBuilder(247, Packet.Type.VARIABLE);
		builder.putShort(25316);
		builder.putShort(item);

		if (c.getData().itemColors.containsKey(item)) {
			Map<Integer, Integer> colors = c.getData().itemColors.get(item);

			builder.put((byte) colors.size());
			for (Map.Entry<Integer, Integer> entry : colors.entrySet()) {
				builder.put((byte) ((int) entry.getKey()));
				builder.putShort(entry.getValue());
			}
		} else {
			builder.put((byte) 0);
		}

		c.writePacket(builder.toPacket());
	}

	public void moveCheck(int xMove, int yMove) {
		this.spearPlayer(c.absX + xMove, c.absY + yMove, c.heightLevel);
	}

	public void movePlayer(int x, int y) {
		c.freezeTimer = 2;
		c.resetWalkingQueue();

		c.setTeleportLocation(Location.create(x, y, c.heightLevel));

		this.requestUpdates();
	}

	public void movePlayer(int x, int y, int h) {
		c.resetWalkingQueue();
		c.setTeleportLocation(Location.create(x, y, h));
		this.requestUpdates();
	}

	public void movePlayer(Location location) {
		movePlayer(location.getX(), location.getY(), location.getZ());
	}

	/**
	 * MulitCombat icon
	 *
	 * @param i1
	 *            0 = off 1 = on
	 */
	public void multiWay(int i1) {
		c.outStream.createFrame(61);
		c.outStream.writeByte(i1);
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	/**
	 * Objects, add and remove
	 */
	public void object(int objectId, int objectX, int objectY, int face, int objectType) {
		// synchronized(c) {
		// if(c.getOutStream() != null && c != null) {
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC(objectY - (c.getLastKnownRegion().getRegionY() * 8));
		c.getOutStream().writeByteC(objectX - (c.getLastKnownRegion().getRegionX() * 8));
		c.getOutStream().createFrame(101);
		c.getOutStream().writeByteC((objectType << 2) + (face & 3));
		c.getOutStream().writeByte(0);
		if (objectId != -1) { // removing
			c.getOutStream().createFrame(151);
			c.getOutStream().writeByteS(0);
			c.getOutStream().writeWordBigEndian(objectId);
			c.getOutStream().writeByteS((objectType << 2) + (face & 3));
		} else {
			// Collision.removeClipping(objectX, objectY, c.heightLevel);
		}
		c.flushOutStream();
		// }
	}

	public void objectAnim(int X, int Y, int animationID, int tileObjectType, int orientation) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				if (p.distanceToPoint(X, Y) <= 25) {
					p.getPA().createPlayersObjectAnim(X, Y, animationID, tileObjectType, orientation);
				}
			}
		}
	}

	private void objectToRemove(int X, int Y) {
		this.object(-1, X, Y, 10, 10);
	}

	private void objectToRemove2(int X, int Y) {
		this.object(-1, X, Y, -1, 0);
	}

	public void openDonatorShop() {
		if (c.isMember == 1) {
			Shops.open(c, 19);
		} else if (c.isMember == 2) {
			Shops.open(c, 20);
		} else {
			Shops.open(c, 21);
		}
	}

	/**
	 * Open bank
	 */

	public void openUpBank() {
		c.removeAttribute("open_ge");
		if (c.inTrade || c.tradeStatus == 1) {
			Player o = PlayerHandler.players[c.tradeWith];
			if (o != null) {
				o.getTradeAndDuel().declineTrade();
			}
		}
		Pins.open(c, Pins.Type.BANK);
	}

	public void playerWalk(int x, int y) {
		PlayerPathFinder.getPlayerPathFinder().findRoute(c, x, y, true, 1, 1);
	}

	/**
	 * Drink AntiPosion Potions
	 *
	 * @param itemId
	 *            The itemId
	 * @param itemSlot
	 *            The itemSlot
	 * @param newItemId
	 *            The new item After Drinking
	 * @param healType
	 *            The type of poison it heals
	 */

	public void potionPoisonHeal(int itemId, int itemSlot, int newItemId, int healType) {
		c.attackTimer = c.getCombat()
				.getAttackDelay(c.getItems().getItemName(c.equipment[Player.playerWeapon]).toLowerCase());
		if (!c.isDead && System.currentTimeMillis() - c.foodDelay > 2000) {
			if (c.getItems().playerHasItem(itemId, 1)) {
				c.sendMessage("You drink the " + c.getItems().getItemName(itemId).toLowerCase() + ".");
				c.foodDelay = System.currentTimeMillis();
				// Actions
				if (healType == 1) {
					// Cures The Poison
				} else if (healType == 2) {
					// Cures The Poison + protects from getting poison again
				}
				c.startAnimation(0x33D);
				c.getItems().deleteItem(itemId, itemSlot, 1);
				c.getItems().addItem(newItemId, 1);
				this.requestUpdates();
			}
		}
	}

	public void processTeleport() {

		c.getWalkingQueue().reset();
		c.blockWalking = true;
		GameCycleTaskHandler.addEvent(c, container -> {
			c.blockWalking = false;
			container.stop();
		}, 3);

		c.setTeleportLocation(Location.create(c.teleX, c.teleY, c.teleHeight));

		if (c.teleEndAnimation > 0) {
			c.startAnimation(c.teleEndAnimation);
			if (c.teleEndGfx > 0) {
				c.gfx100(c.teleEndGfx);
			}
		}
	}

	/*
	 * public int randomBarrows() { return
	 * Barrows[(int)(Math.random()*Barrows.length)]; }
	 */
	public int randomCrystal() {
		return Crystal[(int) (Math.random() * Crystal.length)];
	}

	public int randomCrystal1() {
		return Crystal1[(int) (Math.random() * Crystal1.length)];
	}

	public int randomPots() {
		return Pots[(int) (Math.random() * Pots.length)];
	}

	public int redWaiting() {
		int count = 0;
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = PlayerHandler.players[j];
				if (c.getPA().waitingRed()) {
					count++;
				}
			}
		}
		return count;
	}

	public void refreshSkill(int i) {
		this.setSkillLevel(i, c.level[i], c.xp[i]);
	}

	public void removeAllItems() {
		for (int i = 0; i < c.inventory.length; i++) {
			c.inventory[i] = 0;
		}
		for (int i = 0; i < c.inventoryN.length; i++) {
			c.inventoryN[i] = 0;
		}
		c.getItems().resetItems(3214);
	}

	public void removeAllWindows() {
		closeAllWindows();
	}

	public void removeObject(int x, int y) {
		this.object(-1, x, x, 10, 10);
	}

	public void removeObjects() {
		this.objectToRemove(2638, 4688);
		this.objectToRemove2(2635, 4693);
		this.objectToRemove2(2634, 4693);
	}

	public void requestUpdates() {
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	/**
	 * reseting animation
	 */

	public void resetAnimation() {
		Animations.setMovementAnimationIds(c);
		c.startAnimation(c.playerStandIndex);
		this.requestUpdates();
	}

	public void resetAutocast() {
		if (c.autocasting) {
			c.sendMessage("@autocastoff");
		}
		c.autocasting = false;
		c.autocastId = 0;
		c.getPA().sendFrame36(108, 0);
	}

	public void resetBarrows() {
		c.barrowsNpcs[0][1] = 0;
		c.barrowsNpcs[1][1] = 0;
		c.barrowsNpcs[2][1] = 0;
		c.barrowsNpcs[3][1] = 0;
		c.barrowsNpcs[4][1] = 0;
		c.barrowsNpcs[5][1] = 0;
		c.barrowsKillCount = 0;
		c.randomCoffin = Misc.random(3) + 1;
	}

	public void resetDamageDone() {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			if (PlayerHandler.players[i] != null) {
				PlayerHandler.players[i].damageTaken[c.playerId] = 0;
			}
		}
	}

	public void resetFollow() {
		c.followId = 0;
		c.followId2 = 0;
		c.mageFollow = false;
		// c.outStream.createFrame(174);
		// c.outStream.writeWord(0);
		// c.outStream.writeByte(0);
		// c.outStream.writeWord(1);
	}

	public void resetFollowers() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].followId == c.playerId) {
					Player c = PlayerHandler.players[j];
					c.getPA().resetFollow();
				}
			}
		}
	}

	public void resetTb() {
		c.teleBlockLength = 0;
		c.teleBlockDelay = 0;
	}

	public void resetVariables() {
		/* c.getFishing().resetFishing() */
		c.getCrafting().resetCrafting();
		c.usingGlory = false;
	}

	public void restorePlayer() {
		c.poisonDamage = -1;
		c.tradeResetNeeded = true;
		c.getCombat().resetPrayers();
		for (int i = 0; i < 20; i++) {
			c.level[i] = this.getLevelForXP(c.xp[i]);
			c.getPA().refreshSkill(i);
		}
		c.getCombat().resetPlayerAttack();
		this.resetAnimation();
		this.frame1();
		this.resetTb();
		c.isSkulled = false;
		c.attackedPlayers.clear();
		c.headIconPk = -1;
		c.skullTimer = -1;
		this.resetDamageDone();
		c.specAmount = 10;
		c.damageTaken = new int[Config.MAX_PLAYERS];
		c.prayerActive[10] = false;
		c.getItems().addSpecialBar(c.equipment[Player.playerWeapon]);
		c.isDead = false;
		c.getPA().requestUpdates();
	}

	public boolean riskPkReq() {
		return this.getWearingAmount() > 6 && c.getItems().getCarriedWealth() > 5000000;
	}

	public void sendClan(String name, String message, String clan, int rights, boolean elite) {
		c.outStream.createFrameVarSizeWord(217);
		c.outStream.writeString(name);
		c.outStream.writeString(Misc.formatPlayerName(message));
		c.outStream.writeString(clan);
		c.outStream.writeWord(rights);
		c.outStream.writeByte(elite ? 1 : 0);
		c.outStream.endFrameVarSize();
	}

	public void sendEnergy() {
		c.outStream.createFrame(110);
		c.outStream.writeByte(c.getData().energy);
	}

	public void sendFrame106(int sideIcon) {
		if (c.getOutStream() != null) {
			c.getOutStream().createFrame(106);
			c.getOutStream().writeByteC(sideIcon);
			c.flushOutStream();
			this.requestUpdates();
		}
	}

	public void sendFrame107() {
		if (c.getOutStream() != null) {
			c.getOutStream().createFrame(107);
			c.flushOutStream();
		}
	}

	public void sendFrame126(String s, int id) {

		s = replaceColors(s);

		try {
			if (c.getOutStream() != null) {
				if (s != null && id >= 0) {
					c.getOutStream().createFrameVarSizeWord(126);
					c.getOutStream().writeString(s);
					c.getOutStream().writeWordA(id);
					c.getOutStream().endFrameVarSizeWord();
					c.flushOutStream();
				}
			}
		} catch (StackOverflowError flow) {
			flow.printStackTrace();
		}
	}

	public void sendFrame164(int Frame) {

		c.openInterfaceId = Frame;

		// synchronized(c) {
		if (c.getOutStream() != null) {
			c.getOutStream().createFrame(164);
			c.getOutStream().writeWordBigEndian_dup(Frame);
			c.flushOutStream();
		}
	}

	public void hideShowLayer(int SubFrame, boolean hide) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(171);
			c.getOutStream().writeByte(hide ? 0 : 1);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();
		}
	}

	/*
	 * public void clanQuery(String affirmative, String negative) { for (int j = 0;
	 * j < PlayerHandler.players.length; j++) { if (PlayerHandler.players[j] !=
	 * null) { Player c2 = (Player)PlayerHandler.players[j]; if(c.clanId ==
	 * c2.clanId && c.clanId > -1 && c2.clanId > -1) { if(!c2.inWild()) {
	 * c2.getDH().sendOption2(affirmative, negative); c2.clanAction = 1; } } } } }
	 */
	public void sendFrame185(int Frame) {
		// synchronized(c) {
		if (c.getOutStream() != null) {
			c.getOutStream().createFrame(185);
			c.getOutStream().writeWordBigEndianA(Frame);
		}
	}

	public void sendFrame200(int MainFrame, int SubFrame) {
		// synchronized(c) {
		if (c.getOutStream() != null) {
			c.getOutStream().createFrame(200);
			c.getOutStream().writeWord(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();
		}
	}

	public void sendMapRegion() {
		c.setLastKnownRegion(c.getLocation());
		c.writePacket(new PacketBuilder(73).putShortA(c.getLocation().getRegionX() + 6)
				.putShort(c.getLocation().getRegionY() + 6).toPacket());
	}

	// called from scripting
	public void saveGameMode() {
		long ironman = (long) c.getAttribute("ironman");

		if (ironman == 2) {
			c.ironman = true;
			sendFrame126("rights:5", 0);
		}
		if (ironman == 3) {
			c.ironman = true;
			c.hardcoreIronman = true;
			sendFrame126("rights:6", 0);
		}

		long elite = (long) c.getAttribute("mode");
		if (elite == 2) {
			c.elite = true;
		}

		Server.submitWork(() -> {
			try (java.sql.Connection connection = DatabaseUtil.getConnection()) {
				try (PreparedStatement ps = connection.prepareStatement(
						"UPDATE players SET ironman = ?, hardcore_ironman = ?, elite = ? WHERE id = ?")) {
					ps.setBoolean(1, c.ironman);
					ps.setBoolean(2, c.hardcoreIronman);
					ps.setBoolean(3, c.elite);
					ps.setInt(4, c.getDatabaseId());
					ps.execute();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	public void sendFrame246(int MainFrame, int SubFrame, int SubFrame2) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(246);
			c.getOutStream().writeWordBigEndian(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.getOutStream().writeWord(SubFrame2);
			c.flushOutStream();
		}
	}

	public void sendFrame248(int MainFrame, int SubFrame) {

		c.openInterfaceId = MainFrame;

		// synchronized(c) {
		if (c.getOutStream() != null) {
			c.getOutStream().createFrame(248);
			c.getOutStream().writeWordA(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();
		}
	}

	public void sendFrame34(int itemId, int slot, int column, int amount) {
		// synchronized(c) {
		// if(c.getOutStream() != null && c != null) {
		c.outStream.createFrameVarSizeWord(34); // init item to smith screen
		c.outStream.writeWord(column); // Column Across Smith Screen
		c.outStream.writeByte(4); // Total Rows?
		c.outStream.writeDWord(slot); // Row Down The Smith Screen
		c.outStream.writeWord(itemId + 1); // item
		c.outStream.writeByte(amount); // how many there are?
		c.outStream.endFrameVarSizeWord();
		// }
	}

	public void sendFrame34a(int frame, int item, int slot, int amount) {
		c.outStream.createFrameVarSizeWord(34);
		c.outStream.writeWord(frame);
		c.outStream.writeByte(slot);
		c.outStream.writeWord(item + 1);
		c.outStream.writeByte(255);
		c.outStream.writeDWord(amount);
		c.outStream.endFrameVarSizeWord();
	}

	/*
	 * public int randomRunes() { return Runes[(int) (Math.random()*Runes.length)];
	 * }
	 */

	public void sendConfig(int id, int state) {
		if (state > Byte.MAX_VALUE) {
			sendFrame87(id, state);
		} else {
			sendFrame36(id, state);
		}
	}

	public void sendFrame36(int id, int state) {
		if (c.getOutStream() != null) {
			c.getOutStream().createFrame(36);
			c.getOutStream().writeWordBigEndian(id);
			c.getOutStream().writeByte(state);
			c.flushOutStream();
		}
	}

	public void sendFrame70(int i, int o, int id) {
		// synchronized(c) {
		if (c.getOutStream() != null) {
			c.getOutStream().createFrame(70);
			c.getOutStream().writeWord(i);
			c.getOutStream().writeWordBigEndian(o);
			c.getOutStream().writeWordBigEndian(id);
			c.flushOutStream();
		}
	}

	public void sendFrame75(int MainFrame, int SubFrame) {
		// synchronized(c) {
		if (c.getOutStream() != null) {
			c.getOutStream().createFrame(75);
			c.getOutStream().writeWordBigEndianA(MainFrame);
			c.getOutStream().writeWordBigEndianA(SubFrame);
			c.flushOutStream();
		}
	}

	public void sendFrame87(int id, int state) {
		// synchronized(c) {
		if (c.getOutStream() != null) {
			c.getOutStream().createFrame(87);
			c.getOutStream().writeWordBigEndian_dup(id);
			c.getOutStream().writeDWord_v1(state);
			c.flushOutStream();
		}
	}

	public void sendFrame99(int state) { // used for disabling map
		// synchronized(c) {
		// if(c.getOutStream() != null && c != null) {
		if (mapStatus != state) {
			mapStatus = state;
			c.getOutStream().createFrame(99);
			c.getOutStream().writeByte(state);
			c.flushOutStream();
		}
		// }
	}

	public void sendLink(String s) {
		if (c.getOutStream() != null) {
			c.getOutStream().createFrameVarSizeWord(187);
			c.getOutStream().writeString(s);
		}
	}

	public void sendNpcChat(int npcId, int animation, String dialogue) {
		sendNpcChat(npcId, animation, Misc.wrapText(dialogue, 55));
	}

	public void sendNpcChat(int npcId, int animation, String... dialogue) {
		if (dialogue.length == 0 || dialogue.length > DialogueConstants.NPC_DIALOGUE_CHAT_FRAMES.length) {
			System.err.println("invalid amount of dialogues lines provided " + dialogue.length);
			return;
		}

		final int startFrame = DialogueConstants.NPC_DIALOGUE_CHAT_FRAMES[dialogue.length - 1];

		// send animation
		sendFrame200(startFrame + 1, animation > 0 ? animation : DialogueAnimations.TALKING.getId());

		// npc name
		sendFrame126(NPCHandler.getNpcListName(npcId).replaceAll("_", " "), startFrame + 2);

		// write the dialogues
		for (int i = 0; i < dialogue.length; i++) {
			sendFrame126(dialogue[i], startFrame + 3 + i);
		}

		// send the interface
		sendFrame75(npcId, startFrame + 1);
		sendFrame164(startFrame);
	}

	/*
	 * public void resetAction() { c.clickObjectType = 0; c.clickObjectAtX = 0;
	 * c.clickObjectAtY = 0; }
	 */

	public void sendNpcChat(int npcId, String dialogue) {
		sendNpcChat(npcId, -1, Misc.wrapText(dialogue, 55));
	}

	public void sendNpcChat(int npcId, String... dialogue) {
		sendNpcChat(npcId, -1, dialogue);
	}

	public void sendOptions(String... options) {
		sendOptions(Optional.empty(), options);
	}

	public void sendOptions(Optional<String> title, String... options) {
		if (options.length == 0 || options.length > DialogueConstants.DIALOGUE_OPTION_FRAMES.length) {
			System.err.println("invalid amount of dialogues options supplied: " + options.length);
			return;
		}

		final int startFrame = DialogueConstants.DIALOGUE_OPTION_FRAMES[options.length];

		if (title.isPresent()) {
			hideShowLayer(DialogueConstants.DIALOGUE_OPTION_SWORD_LAYER_IDS[options.length][0], false);
			hideShowLayer(DialogueConstants.DIALOGUE_OPTION_SWORD_LAYER_IDS[options.length][1], true);
			sendFrame126(title.get(), startFrame);
		} else {
			hideShowLayer(DialogueConstants.DIALOGUE_OPTION_SWORD_LAYER_IDS[options.length][0], true);
			hideShowLayer(DialogueConstants.DIALOGUE_OPTION_SWORD_LAYER_IDS[options.length][1], false);
			sendFrame126("Select an Option", startFrame);
		}

		for (int i = 0; i < options.length; i++) {
			sendFrame126(options[i], startFrame + i + 1);
		}

		sendFrame164(startFrame - 1);
	}

	public void sendPlayerChat(int animation, String dialogue) {
		sendPlayerChat(animation, Misc.wrapText(dialogue, 55));
	}

	public void sendPlayerChat(int animation, String... dialogue) {
		if (dialogue.length == 0 || dialogue.length > DialogueConstants.PLAYER_DIALOGUE_FRAME_IDS.length) {
			System.err.println("invalid amount of dialogues provided " + dialogue.length);
			return;
		}

		final int startFrame = DialogueConstants.PLAYER_DIALOGUE_FRAME_IDS[dialogue.length - 1];

		// send animation
		sendFrame200(startFrame + 1, animation > -1 ? animation : DialogueAnimations.TALKING.getId());

		// player name
		sendFrame126(c.username, startFrame + 2);

		// write the dialogues
		for (int i = 0; i < dialogue.length; i++) {
			sendFrame126(dialogue[i], startFrame + 3 + i);
		}

		// send the interface
		sendFrame185(startFrame + 1);
		sendFrame164(startFrame);
	}

	public void sendPlayerChat(String dialogue) {
		sendPlayerChat(-1, Misc.wrapText(dialogue, 55));
	}

	public void sendPlayerChat(String... dialogue) {
		sendPlayerChat(-1, dialogue);
	}

	public void sendStatement(String s) {
		sendFrame126(s, 357);
		sendFrame126("Click here to continue", 358);
		sendFrame164(356);
	}

	public void sendTutorialIslandInterface(int percent) {
		this.sendFrame36(406, (percent / 5) + 1);
		for (int i = 12224; i <= 12227; i++) {
			this.hideShowLayer(i, false);
		}
		this.sendFrame126(percent + "%", 12224);
		this.walkableInterface(8680);
	}

	public void sendUptime() {

		long mil = System.currentTimeMillis() - Server.SERVER_STARTED_AT;

		int seconds = (int) Math.floor((mil / 1000));
		mil -= seconds * 1000;

		int minutes = (seconds / 60);
		seconds -= minutes * 60;

		int hours = (minutes / 60);
		minutes -= hours * 60;

		int days = (hours / 24);
		hours -= days * 24;

		int weeks = (days / 7);
		days -= weeks * 7;

		if (weeks > 0) {
			sendFrame126("@or2@Uptime: @whi@" + weeks + "w, " + days + "d, " + hours + "h", 54416);
		} else if (days > 0) {
			sendFrame126("@or2@Uptime: @whi@" + days + "d, " + hours + "h", 54416);
		} else if (hours > 0) {
			sendFrame126("@or2@Uptime: @whi@" + hours + "h, " + minutes + "m", 54416);
		} else {
			sendFrame126("@or2@Uptime: @whi@" + minutes + "m", 54416);
		}
	}

	public void sendTimeOnline() {

		int seconds = (int) Math.floor(c.getData().minutesOnline * 60);

		int minutes = (seconds / 60);
		seconds -= minutes * 60;

		int hours = (minutes / 60);
		minutes -= hours * 60;

		int days = (hours / 24);
		hours -= days * 24;

		int weeks = (days / 7);
		days -= weeks * 7;

		if (weeks > 0) {
			sendFrame126("@or1@Time Online: @whi@" + weeks + "w, " + days + "d, " + hours + "h", 54437);
		} else if (days > 0) {
			sendFrame126("@or1@Time Online: @whi@" + days + "d, " + hours + "h", 54437);
		} else if (hours > 0) {
			sendFrame126("@or1@Time Online: @whi@" + hours + "h, " + minutes + "m", 54437);
		} else {
			sendFrame126("@or1@Time Online: @whi@" + minutes + "m", 54437);
		}
	}

	public void sendX(int interfaceId) {
		c.getPA().removeAllWindows();
		c.xInterfaceId = interfaceId;
		c.getOutStream().createFrame(27);
		c.flushOutStream();
	}

	public void setChatOptions(int publicChat, int privateChat, int tradeBlock) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(206);
			c.getOutStream().writeByte(publicChat);
			c.getOutStream().writeByte(privateChat);
			c.getOutStream().writeByte(tradeBlock);
			c.flushOutStream();
		}
	}

	/**
	 * Sets the clan information for the player's clan.
	 */
	public void setClanData() {
		boolean exists = Server.clanManager.clanExists(c.username);
		if (!exists || c.clan == null) {
			this.sendFrame126("Join chat", 18135);
			this.sendFrame126("Talking in: Not in chat", 18139);
			this.sendFrame126("Owner: None", 18140);
		}
		if (!exists) {
			this.sendFrame126("Chat Disabled", 18306);
			String title = "";
			for (int id = 18307; id < 18317; id += 3) {
				if (id == 18307) {
					title = "Anyone";
				} else if (id == 18310) {
					title = "Anyone";
				} else if (id == 18313) {
					title = "General+";
				} else if (id == 18316) {
					title = "Only Me";
				}
				this.sendFrame126(title, id + 2);
			}
			for (int index = 0; index < 100; index++) {
				this.sendFrame126("", 18323 + index);
			}
			for (int index = 0; index < 100; index++) {
				this.sendFrame126("", 18424 + index);
			}
			// Clan clan = Server.clanManager.getClan("chat");
			// clan.addMember(c);
			return;
		}
		Clan clan = Server.clanManager.getClan(c.username);
		this.sendFrame126(clan.getTitle(), 18306);
		String title = "";
		for (int id = 18307; id < 18317; id += 3) {
			if (id == 18307) {
				title = clan.getRankTitle(clan.whoCanJoin)
						+ (clan.whoCanJoin > Clan.Rank.ANYONE && clan.whoCanJoin < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18310) {
				title = clan.getRankTitle(clan.whoCanTalk)
						+ (clan.whoCanTalk > Clan.Rank.ANYONE && clan.whoCanTalk < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18313) {
				title = clan.getRankTitle(clan.whoCanKick)
						+ (clan.whoCanKick > Clan.Rank.ANYONE && clan.whoCanKick < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18316) {
				title = clan.getRankTitle(clan.whoCanBan)
						+ (clan.whoCanBan > Clan.Rank.ANYONE && clan.whoCanBan < Clan.Rank.OWNER ? "+" : "");
			}
			this.sendFrame126(title, id + 2);
		}
		if (clan.rankedMembers != null) {
			for (int index = 0; index < 100; index++) {
				if (index < clan.rankedMembers.size()) {
					this.sendFrame126("<clan=" + clan.ranks.get(index) + ">" + clan.rankedMembers.get(index),
							18323 + index);
				} else {
					this.sendFrame126("", 18323 + index);
				}
			}
		}
		if (clan.bannedMembers != null) {
			for (int index = 0; index < 100; index++) {
				if (index < clan.bannedMembers.size()) {
					this.sendFrame126(clan.bannedMembers.get(index), 18424 + index);
				} else {
					this.sendFrame126("", 18424 + index);
				}
			}
		}
	}

	public void setComponentPosition(int componentId, int x, int y) {
		c.getOutStream().createFrame(70);
		c.getOutStream().writeWord(x);
		c.getOutStream().writeWordBigEndian(y);
		c.getOutStream().writeWordBigEndian(componentId);
		c.flushOutStream();
	}

	public void setFixedProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time) {
		// synchronized(c) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Player person = p;
				if (person.getOutStream() != null) {
					if (person.distanceToPoint(x, y) <= 25) {
						if (p.heightLevel == c.heightLevel) {
							person.getPA().createFixedProjectile(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
									endHeight, lockon, time);
						}
					}
				}
			}
		}
	}

	public void setMediaData(int componentId, int rotateX, int rotateY, int zoom) {
		c.getOutStream().createFrame(230);
		c.getOutStream().writeWordA(zoom);
		c.getOutStream().writeWord(componentId);
		c.getOutStream().writeWord(rotateX);
		c.getOutStream().writeWordBigEndianA(rotateY);
		c.flushOutStream();
	}

	public void setMediaModelId(int componentId, int modelId) {
		c.getOutStream().createFrame(8);
		c.getOutStream().writeWordBigEndianA(componentId);
		c.getOutStream().writeWord(modelId);
		c.flushOutStream();
	}

	public void setPrivateMessaging(int i) { // friends and ignore list status
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(221);
			c.getOutStream().writeByte(i);
			c.flushOutStream();
		}
	}

	private void setSkillLevel(int skillNum, int currentLevel, int XP) {

		if (skillNum == PlayerConstants.SUMMONING) {
			sendFrame126(currentLevel + "/" + getLevelForXP(c.xp[skillNum]), 14345);
		}

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(134);
			c.getOutStream().writeByte(skillNum);
			c.getOutStream().writeDWord_v1(XP);
			c.getOutStream().writeByte(currentLevel);
			c.flushOutStream();
		}
	}

	public void showInterface(int interfaceid) {

		c.openInterfaceId = interfaceid;

		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(97);
			c.getOutStream().writeDWord(interfaceid);
			c.flushOutStream();
		}
	}

	public void showOption(int i, int l, String s) {
		if (i == 3 && l == 0) {
			if (s.equalsIgnoreCase(attackOption)) {
				return;
			}
			attackOption = s;
		}

		c.getOutStream().createFrameVarSize(104);
		c.getOutStream().writeByteC(i);
		c.getOutStream().writeByteA(l);
		c.getOutStream().writeString(s);
		c.getOutStream().endFrameVarSize();
		c.flushOutStream();
	}

	public void spearPlayer(int x, int y, int h) {
		// c.resetWalkingQueue();

		c.setTeleportLocation(Location.create(x, y, h));

		this.requestUpdates();
	}

	/**
	 * Teleporting
	 */
	public void spellTeleport(int x, int y, int height) {
		if (c.inTrinityWar()) {
			c.sendMessage("A magical force doesn't allow you to teleport!");
			return;
		}
		if (c.inFightCaves()) {
			c.sendMessage("A magical force doesn't allow you to teleport!");
			return;
		}
		if (c.getDuel() != null && c.getDuel().getStage() == DuelArena.Stage.FIGHTING) {
			c.sendMessage("You cannot teleport out of a duel");
			return;
		}
		c.onObstacle = false;
		c.getPA().startTeleport(x, y, height, c.playerMagicBook == 1 ? "ancient" : "modern");
	}

	public void startTeleport(int x, int y, int height, String teleportType) {
		if (ServerEvents.onTeleport(c)) {
			return;
		}
		if (Gambling.inStake(c)) {
			return;
		}
		if (c.onObstacle) {
			c.sendMessage("You cannot teleport whilst on an obstacle.");
			return;
		}
		if (c.inVote() && !teleportType.equalsIgnoreCase("dungeon")) {
			c.sendMessage("Please vote before you leave");
			return;
		}
		if (c.getDuel() != null && c.getDuel().getStage() == DuelArena.Stage.FIGHTING) {
			c.sendMessage("You cannot teleport out of a duel");
			return;
		}
		if (c.inPits() || c.inFightCaves() || c.attributeExists("zombie_game") || c.inPestControl) {
			c.sendMessage("You cannot teleport in this minigame.");
			return;
		}
		/*
		 * if(c.inZombieHouse()) {
		 * c.sendMessage("You cannot teleport in this minigame."); return; }
		 *
		 * if(c.inZombieGame()) {
		 * c.sendMessage("You cannot teleport in this minigame."); return; }
		 */
		if (c.inPits || this.inPitsWait()) {
			c.sendMessage("You can't teleport in here!");
			return;
		}
		// if (c.underAttackBy > 0) {
		// return;
		// }

		int maxWildernessLevel = Config.NO_TELEPORT_WILD_LEVEL;
		if (c.usingGlory) {
			maxWildernessLevel = 30;
		}

		if (c.inWild() && c.wildLevel > maxWildernessLevel && x != 3067 && y != 10253) {
			c.sendMessage("You can't teleport above level " + Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
			return;
		}
		if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (c.jailTimer > 1) {
			c.sendMessage("You have " + (c.jailTimer / 100 / 60 / 24 > 1 ? c.jailTimer / 100 / 60 / 24 + " days"
					: c.jailTimer / 100 / 60 > 1 ? c.jailTimer / 100 / 60 + " hours" : c.jailTimer / 100 + " mins")
					+ " left in jail");
			return;
		}

		if (!c.isDead && c.teleTimer == 0 && c.respawnTimer == -6) {
			if (c.playerIndex > 0 || c.npcIndex > 0) {
				c.getCombat().resetPlayerAttack();
			}
			c.stopMovement();
			this.removeAllWindows();
			c.teleX = x;
			c.teleY = y;
			c.npcIndex = 0;
			c.onObstacle = false;
			c.playerIndex = 0;
			c.faceUpdate(0);
			c.teleHeight = height;
			if (c.dungTele) {
				teleportType = "dungeon";
			}
			if (teleportType.equalsIgnoreCase("modern")) {
				c.gfx0(1576);
				c.teleTimer = 4;
				c.startAnimation(8939);
				c.teleEndAnimation = 8941;
				c.teleEndGfx = 1577;
			}
			if (teleportType.equalsIgnoreCase("ancient")) {
				c.startAnimation(1979);
				c.teleGfx = 0;
				c.teleTimer = 4;
				c.teleEndAnimation = 0;
				c.gfx0(1681);
			}
			if (teleportType.equalsIgnoreCase("dungeon")) {
				c.startAnimation(9599);
				c.gfx0(2602);
				c.teleGfx = -1;
				c.teleTimer = 6;
				c.teleEndAnimation = 8941;
			}

			Server.getTickManager().submit(new Tick(1) {

				@Override
				public void execute() {
					if (c != null || c.isDisconnected()) {
						if (c.teleTimer > 0) {
							c.teleTimer--;
							if (!c.isDead) {
								if (c.teleTimer == 1 && c.newLocation > 0) {
									c.teleTimer = 0;
									c.getPA().closeAllWindows();
									PlayerAssistant.this.changeLocation();
								}
								if (c.teleTimer == 2) {
									// c.teleTimer--;
									PlayerAssistant.this.processTeleport();
								}
								if (c.teleTimer == 3 && c.teleGfx > 0) {
									// c.teleTimer--;
									c.gfx100(c.teleGfx);
								}
							} else {
								c.teleTimer = 0;
								this.stop();
							}
						} else {
							this.stop();
						}
					} else {
						this.stop();
					}
				}
			});
		}
	}

	public void startTeleport(Location location) {
		startTeleport(location.getX(), location.getY(), location.getZ());
	}

	public void startTeleport(int x, int y, int height) {
		if (ServerEvents.onTeleport(c)) {
			return;
		}
		if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (!c.isDead && c.teleTimer == 0) {
			c.stopMovement();
			this.removeAllWindows();
			c.teleX = x;
			c.teleY = y;
			c.npcIndex = 0;
			c.playerIndex = 0;
			c.faceUpdate(0);
			c.teleHeight = height;
			c.startAnimation(8939);
			c.teleTimer = 4;
			c.teleGfx = 1576;
			c.teleEndAnimation = 8941;
			c.teleEndGfx = 1577;
			Server.getTickManager().submit(new Tick(1) {

				@Override
				public void execute() {
					if (c != null || c.isDisconnected()) {
						if (c.teleTimer > 0) {
							c.teleTimer--;
							if (!c.isDead) {
								if (c.teleTimer == 1 && c.newLocation > 0) {
									c.teleTimer = 0;
									PlayerAssistant.this.changeLocation();
								}
								if (c.teleTimer == 2) {
									// c.teleTimer--;
									PlayerAssistant.this.processTeleport();
								}
								if (c.teleTimer == 3 && c.teleGfx > 0) {
									// c.teleTimer--;
									c.gfx100(c.teleGfx);
								}
							} else {
								c.teleTimer = 0;
								this.stop();
							}
						} else {
							this.stop();
						}
					} else {
						this.stop();
					}
				}
			});
		}
	}

	/**
	 * * GFX
	 */
	public void stillGfx(int id, int x, int y, int height, int time) {
		// synchronized(c) {
		// if(c.getOutStream() != null && c != null) {
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC(y - (c.getLastKnownRegion().getRegionY() * 8));
		c.getOutStream().writeByteC(x - (c.getLastKnownRegion().getRegionX() * 8));
		c.getOutStream().createFrame(4);
		c.getOutStream().writeByte(0);
		c.getOutStream().writeWord(id);
		c.getOutStream().writeByte(height);
		c.getOutStream().writeWord(time);
		c.flushOutStream();
		// }
	}

	/*
	 * public void handleTopPkers() { double killToDeathRatio =
	 * (c.killCount/c.deathCount); String pkerOne, pkerTwo, pkerThree, pkerFour,
	 * pkerFive, pkerSix, pkerSeven, pkerEight, pkerNine, pkerTen;
	 * if(!Server.PlayerHandler().isPlayerOn()); return; for(int i = 0; i <
	 * Config.MAX_PLAYERS; i++) { if(c.players[i] != null) {
	 * c.sendMessage("Finishing atm"); } }
	 *
	 *
	 * }
	 */

	/*
	 * public void updateTopPkers() { for(int i = 0; i < Config.MAX_PLAYERS; i++) {
	 * Player p = Server.playerHandler.players[i]; if(p.killToDeathRatio >
	 * p.pkerOne.killToDeathRatio) { p.killToDeathRatio.username = p.pkerOne; } } }
	 */

	public void teleTabTeleport(int x, int y, int height, String teleportType) {
		if (ServerEvents.onTeleport(c)) {
			return;
		}
		if (c.getDuel() != null && c.getDuel().getStage() == DuelArena.Stage.FIGHTING) {
			c.sendMessage("You cannot teleport out of a duel");
			return;
		}
		if (c.inPits() || c.inFightCaves() || c.attributeExists("zombie_game") || c.inPestControl) {
			c.sendMessage("You cannot teleport in this minigame.");
			return;
		}
		if (c.inPits) {
			c.sendMessage("You can't teleport during Fight Pits.");
			return;
		}
		if (c.getPA().inPitsWait()) {
			c.sendMessage("You can't teleport during Fight Pits.");
			return;
		}
		if (c.inWild() && c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
			c.sendMessage("You can't teleport above level " + Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
			return;
		}
		if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (c.jailTimer > 1) {
			c.sendMessage("You have " + c.jailTimer / 2 + " left in jail");
			return;
		}
		if (!c.isDead && c.teleTimer == 0 && c.respawnTimer == -6) {
			if (c.playerIndex > 0 || c.npcIndex > 0) {
				c.getCombat().resetPlayerAttack();
			}
			c.stopMovement();
			this.removeAllWindows();
			c.teleX = x;
			c.teleY = y;
			c.npcIndex = 0;
			c.playerIndex = 0;
			c.faceUpdate(0);
			c.teleHeight = height;
			c.teleEndAnimation = 9598;
			c.teleTimer = 4;
			c.gfx0(1680);
			c.startAnimation(9597);
			Server.getTickManager().submit(new Tick(1) {

				@Override
				public void execute() {
					if (c != null || c.isDisconnected()) {
						if (c.teleTimer > 0) {
							c.teleTimer--;
							if (!c.isDead) {
								if (c.teleTimer == 1 && c.newLocation > 0) {
									c.teleTimer = 0;
									PlayerAssistant.this.changeLocation();
								}
								if (c.teleTimer == 2) {
									// c.teleTimer--;
									PlayerAssistant.this.processTeleport();
								}
								if (c.teleTimer == 3 && c.teleGfx > 0) {
									// c.teleTimer--;
									c.gfx100(c.teleGfx);
								}
							} else {
								c.teleTimer = 0;
								this.stop();
							}
						} else {
							this.stop();
						}
					} else {
						this.stop();
					}
				}
			});
		}
	}

	public void topPkers() {
		// c.clearQuestInterface();
		for (int i = 0; i < 10; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].username != "null") {
					this.sendFrame126("@dbl@Top PKers", 8144); // Title
				}
				// sendFrame126("@bla@Rank "+(i+1)+": @bla@ - K:@whi@ "
				// +com.zionscape.server.model.players.Player.pker[i], 8147+i);
				/*
				 * sendFrame126("2. ", 8148); sendFrame126("3. ", 8149); sendFrame126("4. ",
				 * 8150); sendFrame126("5. ", 8151); sendFrame126("6. ", 8152);
				 * sendFrame126("7. ", 8153); sendFrame126("9. ", 8154); sendFrame126("10. ",
				 * 8155);
				 */
			}
			c.getPA().showInterface(8134);
			c.flushOutStream();
		}
	}

	public void updatePM(int pID, int world) { // used for private chat updates
		/*
		 * Player p = PlayerHandler.players[pID]; if (p == null || p.username == null ||
		 * p.username.equals("null")) { return; } Player o = p; //if (o == null) { //
		 * return; //} long l =
		 * Misc.playerNameToInt64(PlayerHandler.players[pID].username);
		 *
		 * if (p.privateChat == 0) { for (int i = 0; i < c.friends.length; i++) { if
		 * (c.friends[i] != 0) { if (l == c.friends[i]) { loadPM(l, world); return; } }
		 * } } else if (p.privateChat == 1) { for (int i = 0; i < c.friends.length; i++)
		 * { if (c.friends[i] != 0) { if (l == c.friends[i]) { if (o.getPA().isInPM(
		 * Misc.playerNameToInt64(c.username))) { loadPM(l, world); return; } else {
		 * loadPM(l, 0); return; } } } } } else if (p.privateChat == 2) { for (int i =
		 * 0; i < c.friends.length; i++) { if (c.friends[i] != 0) { if (l ==
		 * c.friends[i] && c.rights < 2) { loadPM(l, 0); return; } } } }
		 */// cba, duonkno what your dev was trying to fix, but guess he can do it when he
			// comes back
	}

	public void useOperate(int itemId) {
		switch (itemId) {

		case 18803:
			if (c.equipment[PlayerConstants.RING] != 18803) {
				break;
			}
			Slayer.operateMablesRing(c);
			break;

		case 1712:
		case 1710:
		case 1708:
		case 1706:
		case 1704:
			this.handleGlory(itemId);
			break;
		case 2572:
			this.rowTeleports();
			break;
		case 11283:
		case 11284:
			if (c.playerIndex > 0) {
				c.getCombat().handleDfs();
			} else if (c.npcIndex > 0) {
				c.getCombat().handleDfsNPC();
			}
			break;
		}
	}

	public boolean verifyEquippedItems() {
		for (int i = 0; i < c.equipment.length; i++) {
			if (c.equipment[i] > 0) {
				return true;
			}
		}
		return false;
	}

	public boolean waitingRed() {
		return c.absX > 2407 && c.absX < 2434 && c.absY > 9508 && c.absY < 9538;
	}

	/*
	 * public void giveHelmet(String type) { OLD SHIT SEARCH ME
	 * if(type.equalsIgnoreCase("p2p")) { if(c.choseMage) { if(c.level(1) >= 70 &&
	 * c.level(6) >= 70) else if((c.level(1) >= 25 && c.level(1) < 70) &&
	 * (c.level(6) >= 50 && c.level(6) < 70)) c.getItems().wearItem(6918, 0) else
	 * if(c.level(1) < 25 && >= 20 && c.level(6) >= 20) c.getItems().wearItem(4089,
	 * 0) if(c.level(1) } } }
	 */
	/*
	 * old fail if(c.level[1] >= 70 && c.level[6] >= 70) {
	 * c.getItems().wearItem(4708, 0); c.getItems().wearItem(4712, 4);
	 * c.getItems().wearItem(4714, 7); } else { c.getItems().wearItem(6916, 4);
	 * c.getItems().wearItem(6918, 0); c.getItems().wearItem(6924, 7); }
	 * c.getItems().wearItem(10362, 2); //all c.getItems().wearItem(6889, 5); //mage
	 * only c.getItems().wearItem(7462, 9); //all c.getItems().wearItem(2550, 11);
	 * //all c.getItems().wearItem(4675, 3); //mage only c.getItems().wearItem(6920,
	 * 10); //all c.bypassReqs = false;
	 */
	public boolean waitingYellow() {
		return c.absX > 2367 && c.absX < 2396 && c.absY > 9479 && c.absY < 9498;
	}

	public void walkableInterface(int id) {
		if (currentWalkableInterface != id) {
			currentWalkableInterface = id;
			c.getOutStream().createFrame(208);
			c.getOutStream().writeDWord(id);
		}
	}

	public void walkTo(int i, int j) {
		c.getWalkingQueue().reset();
		c.getWalkingQueue().addStep(c.getLocation().transform(i, j, 0), false);
	}

	public void walkTo2(int i, int j) {
		if (c.freezeDelay > 0) {
			return;
		}
		c.getWalkingQueue().reset();
		c.getWalkingQueue().addStep(c.getLocation().transform(i, j, 0), false);
	}

	public boolean wearing2xRing() {
		return c.equipment[c.playerRing] == 6465;
	}

	public int yellowWaiting() {
		int count = 0;
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = PlayerHandler.players[j];
				if (c.getPA().waitingYellow()) {
					count++;
				}
			}
		}
		return count;
	}

	public enum HintType {
		LOCATION, NPC, PLAYER;
	}

	public static String replaceColors(String str) {

		str = str.replaceAll("@red@", getColor(Colors.RED));
		str = str.replaceAll("@gre@", getColor(Colors.GREEN));
		str = str.replaceAll("@blu@", getColor(Colors.BLUE));
		str = str.replaceAll("@yel@", getColor(Colors.YELLOW));
		str = str.replaceAll("@cya@", getColor(Colors.CYAN));
		str = str.replaceAll("@mag@", getColor(Colors.MAGENTA));
		str = str.replaceAll("@whi@", getColor(Colors.WHITE));
		str = str.replaceAll("@bla@", getColor(Colors.BLACK));
		str = str.replaceAll("@or1@", getColor(Colors.ORANGE1));
		str = str.replaceAll("@or2@", getColor(Colors.ORANGE2));
		str = str.replaceAll("@or3@", getColor(Colors.ORANGE3));
		str = str.replaceAll("@gr1@", getColor(Colors.GREEN1));
		str = str.replaceAll("@gr2@", getColor(Colors.GREEN2));
		str = str.replaceAll("@gr3@", getColor(Colors.GREEN3));

		return str;
	}

	public enum Colors {
		RED, GREEN, BLUE, YELLOW, CYAN, MAGENTA, WHITE, ORANGE1, ORANGE2, ORANGE3, GREEN1, GREEN2, GREEN3, BLACK
	}

	public static String getColor(Colors color) {
		switch (color) {
		case RED:
			return "<col=ff0000>";
		case GREEN:
			return "<col=00ff00>";
		case BLUE:
			return "<col=255>";
		case YELLOW:
			return "<col=ffff00>";
		case CYAN:
			return "<col=65535>";
		case MAGENTA:
			return "<col=ff00ff>";
		case WHITE:
			return "<col=ffffff>";
		case BLACK:
			return "<col=0>";
		case ORANGE1:
			return "<col=ffb000>";
		case ORANGE2:
			return "<col=ff7000>";
		case ORANGE3:
			return "<col=ff3000>";
		case GREEN1:
			return "<col=c0ff00>";
		case GREEN2:
			return "<col=80ff00>";
		case GREEN3:
			return "<col=40ff00>";
		}

		return "ff000";
	}

	public void sendMoneyPouchUpdate(int amount) {
		if (System.currentTimeMillis() - c.lastPouchUpdate < 5000) {
			c.pouchUpdateAmount += amount;
		} else {
			c.pouchUpdateAmount = amount;
		}
		c.lastPouchUpdate = System.currentTimeMillis();
		sendFrame126((c.pouchUpdateAmount > 0 ? "+" : "-") + Misc.coinsToString(Math.abs(c.pouchUpdateAmount)), 253);
	}

	// TODO:
	public void sendScrollbarHeight(int containerScrollId, int scroll) {

	}

	public void sendItemContainerList(List<Item> asList, int coinContainerId) {

	}

	public void sendItemContainer(TradingPostItemContainer tradingPostHistoryContainer, int containerId) {

	}

	public void sendInterfaceItems(int myShopItemDisplayContainer, List<Object> asList) {

	}
}