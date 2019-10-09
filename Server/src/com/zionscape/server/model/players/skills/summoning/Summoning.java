package com.zionscape.server.model.players.skills.summoning;

import com.google.gson.reflect.TypeToken;
import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.content.grandexchange.GrandExchange;
import com.zionscape.server.model.content.minigames.castlewars.CastleWars;
import com.zionscape.server.model.content.minigames.clanwars.Constants;
import com.zionscape.server.model.content.minigames.clanwars.War;
import com.zionscape.server.model.content.minigames.gambling.Gambling;
import com.zionscape.server.model.content.minigames.zombies.Zombies;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.npcs.NpcDefinition;
import com.zionscape.server.model.npcs.combat.kraken.Kraken;
import com.zionscape.server.model.npcs.combat.zulrah.Zulrah;
import com.zionscape.server.model.players.Areas;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.util.Misc;
import com.zionscape.server.world.ItemDrops;
import com.zionscape.server.world.shops.Shops;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;

public class Summoning {

	private static List<Scroll> scrolls;
	private static List<Pouch> pouches;
	private static List<Familiar> familiars;

	public static void depositItem(Player player, int itemId, int amount, int slot) {

		if (!player.getItems().playerHasItem(itemId)) {
			return;
		}

		if (player.openInterfaceId != 2700) {
			return;
		}

		if (player.isBanking || player.inTrade || player.getDuel() != null || player.getFamiliar() == null) {
			return;
		}

		Familiar familiar = player.getFamiliar().getType();
		List<Item> inventory = player.getFamiliar().getInventoryItems();
		Item existingItem = inventory.stream().filter(x -> x.getId() == itemId).findFirst().orElse(null);

		int tempAmount = player.getItems().getItemAmount(itemId);
		if (amount > tempAmount) {
			amount = tempAmount;
		}

		boolean stackable = ItemUtility.isStackable(itemId);
		int requiredSlots = !stackable ? amount : existingItem != null ? 0 : 1;

		int freeSpace = familiar.getInventorySize() - inventory.size();
		if (freeSpace == 0 && requiredSlots > 0) {
			player.sendMessage("Your BoB storage is full.");
			return;
		}

		if (requiredSlots > freeSpace) {
			if (!stackable) {
				amount = freeSpace;
			}
			player.sendMessage("Your BoB storage is full.");
		}

		player.getItems().deleteItem(itemId, amount);

		if (stackable && existingItem != null) {
			existingItem.incrementAmount(amount);
		} else {
			if (stackable) {
				inventory.add(new Item(itemId, amount));
			} else {
				for (int i = 0; i < amount; i++) {
					inventory.add(new Item(itemId, 1));
				}
			}
		}

		player.getItems().resetItems(5064);
		Summoning.sendFamiliarItems(player);
	}

	public static void destroyFamiliar(NPC npc) {
		if (npc == null) {
			return;
		}
		npc.isDead = true;
		npc.actionTimer = 0;
		npc.applyDead = false;
		npc.needRespawn = true;
	}

	/**
	 * Makes the players familiar begin attacking whatever is attacking the player
	 *
	 * @param player
	 */
	public static void familiarStartCombat(Player player) {
		NPC npc = player.getFamiliar().getNpc();

		if (player.playerIndex > 0 && player.inWild()) {

			if(!Areas.inMulti(player)) {
				player.sendMessage("Familiars can only attack other players in multi areas.");
				return;
			}

			npc.killerId = player.playerIndex;
			npc.underAttackBy = player.playerIndex;
		} else if (player.npcIndex > 0) {
			npc.attackingNpcId = player.npcIndex;
		}

		npc.underAttack = true;
	}

	private static int getFamiliarAttackRating(int npcId) {
		NpcDefinition definition = NPCHandler.getNPCDefinition(npcId);

		return definition.getCombatLevel() * 2;
	}

	private static int getFamiliarDefenceRating(int npcId) {
		NpcDefinition definition = NPCHandler.getNPCDefinition(npcId);

		return (int) (definition.getCombatLevel() * 1.75);
	}

	private static int getFamiliarMaxHit(int npcId) {
		NpcDefinition definition = NPCHandler.getNPCDefinition(npcId);

		return definition.getMaxDamage();
	}

	public static Scroll getScrollByItemId(int itemId) {
		return scrolls.stream().filter(x -> x.getScrollItemId() == itemId).findFirst().orElse(null);
	}

	public static void loadConfiguration() throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException {

		// load up the configuration
		scrolls = Misc.getGson().fromJson(FileUtils.readFileToString(new File("./config/summoning/scrolls.json")),
				new TypeToken<List<Scroll>>() {
				}.getType());
		pouches = Misc.getGson().fromJson(FileUtils.readFileToString(new File("./config/summoning/pouches.json")),
				new TypeToken<List<Pouch>>() {
				}.getType());
		familiars = Misc.getGson().fromJson(FileUtils.readFileToString(new File("./config/summoning/familiars.json")),
				new TypeToken<List<Familiar>>() {
				}.getType());

		// hook up the pouches to the familiar that they spawn
		pouches.stream().forEach((Pouch p) -> {
			Familiar f = familiars.stream().filter(x -> x.getNpcId() == p.getFamiliarId()).findFirst().orElse(null);
			if (f != null) {
				f.setPouch(p);
				p.setFamiliar(f);
			}
		});

		// hook up the scrolls to the familiar they spawn
		scrolls.stream().forEach(
				(Scroll s) -> {
					for (int pouchItemId : s.getRequiredPouchItemIds()) {
						Pouch pouch = pouches.stream().filter(x -> x.getPouchItemId() == pouchItemId).findFirst()
								.orElse(null);
						if (pouch != null) {
							Familiar familiar = familiars.stream().filter(x -> x.getNpcId() == pouch.getFamiliarId())
									.findFirst().orElse(null);

							familiar.setScroll(s);
							s.setFamiliar(familiar);
						}
					}
				});
	}

	public static boolean onActionButton(Player player, int button) {

		if (button == 201245) {
			player.getPA().showInterface(52700);
			return true;
		}

		if (button == 205226) {
			player.getPA().showInterface(51700);
			return true;
		}

		if (player.getFamiliar() == null) {
			return false;
		}

		switch (button) {
			case 166076:
			case 55255: // call follower
				if (System.currentTimeMillis() - player.getFamiliar().getLastCalled() < 10000) {
					return true;
				}
				player.getFamiliar().setLastCalled(System.currentTimeMillis());
				player.getFamiliar().setCalledByPlayer(true);
				return true;
			case 166078:
			case 56002: // dismiss follower

				// just in case precaution
				if (player.storingFamiliarItems) {
					player.getPA().closeAllWindows();
					player.storingFamiliarItems = false;
				}

				// drop any items the npc has in storage
				for (Item item : player.getFamiliar().getInventoryItems()) {
					if (player.getItems().freeInventorySlots() == 0) {
						player.getBank().depositItem(item.getId(), item.getAmount(), -1, true, false);
						player.sendMessage("Your inventory is full, the item has been deposited in your bank.");
					} else {
						ItemDrops.createGroundItem(player, item.getId(), player.getFamiliar().getNpc().getX(), player.getFamiliar().getNpc().getY(), item.getAmount(), player.getId(), false);
					}
				}

				Summoning.destroyFamiliar(player.getFamiliar().getNpc());
				player.setFamiliar(null);
				Summoning.sendInterface(player);
				return true;
			// case 2736:
			case 10176:
			case 55249: // withdraw bob
				if (player.getFamiliar().getInventoryItems().isEmpty()) {
					player.sendMessage("Your beast of burden has no more items.");
					return true;
				}

				boolean updateItemContainers = false;

				Iterator<Item> items = player.getFamiliar().getInventoryItems().iterator();
				while (items.hasNext()) {
					Item item = items.next();
					if (!(ItemUtility.isStackable(item.getId()) && player.getItems().playerHasItem(item.getId()))
							&& player.getItems().freeInventorySlots() == 0) {
						player.sendMessage("You do not have any inventory space.");
						return true;
					}
					player.getItems().addItem(item.getId(), item.getAmount());
					items.remove();
					updateItemContainers = true;
				}

				if (updateItemContainers) {
					Summoning.sendFamiliarItems(player);
					player.getItems().resetItems(5064);
				}

				if (button == 2736) {
					Summoning.sendFamiliarItems(player);
				}
				return true;
			case 166077:
			case 55252: // renew familiar
				Pouch pouch = player.getFamiliar().getType().getPouch();

				if (!player.getItems().playerHasItem(pouch.getPouchItemId())) {
					player.sendMessage("To renew your familiars timer you must have a pouch of that familiars type");
					player.sendMessage("and be ready to sacrifice it.");
					return true;
				}
				if (player.getFamiliar().getTicksLeft() >= 250) {
					player.sendMessage("Your familiars timer is not low enough to renew.");
					return true;
				}
				if (player.level[PlayerConstants.SUMMONING] < pouch.getFamiliar().getPointCost()) {
					player.sendMessage("You don't have enough summoning energy. Renew at an obelisk.");
					return true;
				}

				player.getPA().addSkillXP((int) (pouch.getFamiliar().getXpWhenSummoned()),
						PlayerConstants.SUMMONING);

				player.level[PlayerConstants.SUMMONING] -= pouch.getFamiliar().getPointCost();
				player.getPA().refreshSkill(PlayerConstants.SUMMONING);

				player.getItems().deleteItem(pouch.getPouchItemId(), 1);
				player.getFamiliar().setTicksLeft(pouch.getFamiliar().getMinutes() * 100);

				int minutes = player.getFamiliar().getTicksLeft() / 100;
				int ticksLeft = player.getFamiliar().getTicksLeft() - (minutes * 100);

				player.getPA().sendFrame126(minutes + "." + (ticksLeft == 50 ? "30" : "00"), 14343);
				return true;
			case 166074:
			case 55246: // attack button
				if (player.playerIndex == 0 && player.npcIndex == 0) {
					player.sendMessage("You're not under attack.");
					return true;
				}

				if (!player.getFamiliar().getType().canAttack()) {
					player.sendMessage("This familiar can only attack when attacked.");
					return true;
				}

			/*
			 * if (!player.inMulti()) { player.sendMessage("You must be in a multi combat area to do this."); return
			 * true; }
			 */

				Summoning.familiarStartCombat(player);
				return true;
			case 166072:
			case 55243: // cast special
				Scroll scroll = player.getFamiliar().getType().getScroll();

				if (scroll == null) {
					return true;
				}

				if (!player.getItems().playerHasItem(scroll.getScrollItemId(), 1)) {
					player.sendMessage("You do not have enough scrolls to do that.");
					return true;
				}

				if (!scroll.getFamiliar().equals(player.getFamiliar().getType())) {
					return true;
				}

				if (player.getFamiliar().getSpecialMovesEnergy() - scroll.getSpecialMovePoints() < 0) {
					player.sendMessage("You do not have enough special energy.");
					return true;
				}

				if (player.getFamiliar().getSpecial() == null) {
					player.sendMessage("Sorry this familiar currently does not have a special.");
					return true;
				}

				if (System.currentTimeMillis() - player.getFamiliar().getLastSpecial() < 1200) {
					return false;
				}

				if(player.inWild() && !Areas.inMulti(player)) {
					player.sendMessage("Familiars can only use specials on players in multi areas.");
					return false;
				}

				if (player.getFamiliar().getSpecial().execute()) {
					player.getPA().addSkillXP((int) (scroll.getUseXp()), PlayerConstants.SUMMONING);
					player.getItems().deleteItem(scroll.getScrollItemId(), 1);

					player.getFamiliar().setSpecialMovesEnergy(
							player.getFamiliar().getSpecialMovesEnergy() - scroll.getSpecialMovePoints());
					Summoning.sendSpecialBarEnergy(player);
					return true;
				}

				return true;
		}

		return false;
	}

	public static boolean onClickItem(Player player, int itemId, int option) {

		Pouch pouch = pouches.stream().filter(x -> x.getPouchItemId() == itemId).findFirst().orElse(null);
		if (pouch != null) {

			War war = (War) player.getAttribute("clan_war");
			if (war != null) {
				if (war.getCombatRules()[Constants.SUMMONING_RULE]) {
					player.sendMessage("You may may not summon familiars in this clan war.");
					return true;
				}
			}

			if (Gambling.inArea(player) || Zulrah.inArea(player) || CastleWars.inCastleWarsGame(player) || Kraken.inArea(player.getLocation())) {
				player.sendMessage("You may not summon familiars here.");
				return true;
			}

			if (player.getDuel() != null) {
				player.sendMessage("You may not summon familiars whilst in duel arena.");
				return true;
			}

			if (player.getFamiliar() != null) {
				player.sendMessage("You already have a familiar out.");
				return true;
			}

			if(player.getPet() != null) {
				player.sendMessage("You already have a pet out.");
				return true;
			}

			if (player.level[PlayerConstants.SUMMONING] < pouch.getFamiliar().getPointCost()) {
				player.sendMessage("You don't have enough summoning energy. Renew at an obelisk.");
				return true;
			}

			if (player.getPA().getLevelForXP(player.xp[PlayerConstants.SUMMONING]) < pouch.getLevelRequirement()) {
				player.sendMessage("You require level " + pouch.getLevelRequirement() + " Summoning to use this pouch.");
				return true;
			}

			Achievements.progressMade(player, Achievements.Types.SUMMON_FAMILIAR);

			player.getPA().addSkillXP((int)Math.ceil(pouch.getFamiliar().getXpWhenSummoned()), PlayerConstants.SUMMONING);

			player.level[PlayerConstants.SUMMONING] -= pouch.getFamiliar().getPointCost();
			player.getPA().refreshSkill(PlayerConstants.SUMMONING);

			Summoning.spawnFamiliar(player, pouch.getFamiliar());
			player.getItems().deleteItem(itemId, 1);
			return true;
		}
		return false;
	}

	public static boolean onDialogueOption(Player player, int option) {
		if (player.getDialogueOwner() != null && player.getDialogueOwner().equals(Summoning.class)) {

			if (player.getCurrentDialogueId() == 6970) {

				if (option == 1) {
					Shops.open(player, 80);
				} else {
					Shops.open(player, 81);
				}

				player.setDialogueOwner(null);
				player.setCurrentDialogueId(-1);
				return true;
			}

			if (player.getFamiliar() == null) {
				player.setDialogueOwner(null);
				return true;
			}

			if (option == 1) {
				player.getPA().sendNpcChat(player.getFamiliar().getNpc().type, "Hur hur hur...");
				return true;
			}

			if (player.getFamiliar().getType().getInventorySize() > 0) {
				if (option == 2) {
					Summoning.openStorage(player);
				} else if (option == 3) {
					Summoning.onActionButton(player, 70109);
				}
			} else {
				if (option == 2) {
					Summoning.onActionButton(player, 70109);
				}
			}

			return true;
		}

		return false;
	}

	public static boolean onItemOnItem(Player player, Item item, int slot, Item otherItem, int otherSlot) {
		if (player.getFamiliar() == null) {
			return false;
		}

		if (player.getFamiliar().getSpecial() == null) {
			return false;
		}

		if (System.currentTimeMillis() - player.getFamiliar().getLastSpecial() < 1200) {
			return false;
		}

		Scroll scroll = Summoning.getScrollByItemId(item.getId());

		if (scroll == null) {
			scroll = Summoning.getScrollByItemId(otherItem.getId());
		}

		if (scroll == null) {
			return false;
		}

		if (player.getFamiliar().getSpecial() == null) {
			player.sendMessage("Sorry this familiar currently does not have a special.");
			return true;
		}

		if (!scroll.getFamiliar().equals(player.getFamiliar().getType())) {
			return true;
		}

		if (player.getFamiliar().getSpecialMovesEnergy() - scroll.getSpecialMovePoints() < 0) {
			player.sendMessage("You do not have enough special energy.");
			return true;
		}

		if (player.getFamiliar().getSpecial().onItemOnItem(item, slot, otherItem, otherSlot)) {
			player.getPA().addSkillXP((int) (scroll.getUseXp()), PlayerConstants.SUMMONING);
			player.getItems().deleteItem(scroll.getScrollItemId(), 1);

			player.getFamiliar().setSpecialMovesEnergy(
					player.getFamiliar().getSpecialMovesEnergy() - scroll.getSpecialMovePoints());
			Summoning.sendSpecialBarEnergy(player);
			return true;
		}

		return false;
	}

	public static boolean onItemOnNpc(Player player, int itemId, NPC npc) {
		if (player.getFamiliar() == null) {
			return false;
		}

		Scroll scroll = scrolls.stream().filter(x -> x.getScrollItemId() == itemId).findFirst().orElse(null);

		if (scroll == null) {
			return false;
		}

		if (player.getFamiliar().getNpc().getOwnerId() != player.playerId) {
			return false;
		}

		player.sendMessage("These scrolls can only be used by clicking the Special Move button in the Followers tab.");
		return true;
	}

	public static boolean onMakeItem(Player player, int interfaceId, int itemId, int amount, int slot) {

		// pouch creation
		if (interfaceId == 52710) {

			Pouch pouch = pouches.stream().filter(x -> x.getPouchItemId() == itemId).findFirst().orElse(null);
			if (pouch == null) {
				return true;
			}

			if (pouch.getLevelRequirement() > player.level[PlayerConstants.SUMMONING]) {
				player.sendMessage("A level requirement of " + pouch.getLevelRequirement() + " summoning is required.");
				return true;
			}

			for (int i = 0; i < amount; i++) {

				for (Item item : pouch.getItemsRequired()) {
					if (!player.getItems().playerHasItem(item.getId(), item.getAmount())) {
						player.sendMessage("You require " + item.getAmount() + "x " + ItemUtility.getName(item.getId())
								+ " to create a " + pouch.getName() + ".");
						return true;
					}
				}

				player.startAnimation(725);
				player.gfx100(1207);

				for (Item item : pouch.getItemsRequired()) {
					player.getItems().deleteItem(item.getId(), item.getAmount());
				}

				if (player.getItems().freeInventorySlots() <= 0) {
					for (Item item : pouch.getItemsRequired()) {
						player.getItems().addItem(item.getId(), item.getAmount());
					}
					player.sendMessage("You do not have enough free inventory space.");
					return true;
				}

				player.getItems().addItem(pouch.getPouchItemId(), 1);
				player.getPA().addSkillXP((int) (pouch.getXpGained()), PlayerConstants.SUMMONING);
			}
			return true;
		}

		// scroll creation
		if (interfaceId == 51710) {

			Scroll scroll = scrolls.stream().filter(x -> x.getScrollItemId() == itemId).findFirst().orElse(null);
			if (scroll == null) {
				return true;
			}

			for (int i = 0; i < amount; i++) {

				int hasPouchItemId = -1;
				for (int item : scroll.getRequiredPouchItemIds()) {
					if (player.getItems().playerHasItem(item)) {
						hasPouchItemId = item;
						break;
					}
				}

				if (hasPouchItemId == -1) {
					player.sendMessage("You do not have the required pouch to create a " + scroll.getName() + ".");
					return true;
				}

				player.startAnimation(725);
				player.gfx100(1207);

				player.getItems().deleteItem(hasPouchItemId, 1);

				if (player.getItems().freeInventorySlots() <= 0) {
					player.getItems().addItem(hasPouchItemId, 1);
					player.sendMessage("You do not have enough free inventory space.");
					return true;
				}

				player.getItems().addItem(scroll.getScrollItemId(), 10);
				player.getPA().addSkillXP((int) Math.ceil(scroll.getCreateXp()), PlayerConstants.SUMMONING);
			}
			return true;
		}

		return false;
	}

	public static boolean onNpcClick(Player player, NPC npc, int option) {

		if (npc.type == 6970) {
			player.getPA().sendOptions("Shop 1", "Shop 2");
			player.setCurrentDialogueId(npc.type);
			player.setDialogueOwner(Summoning.class);
			return true;
		}

		if (npc.getOwnerId() == -1) {
			return false;
		}

		if(player.getFamiliar() == null) {
			return false;
		}

		if(player.getPet() != null) {
			return false;
		}

		if (player.playerId != npc.getOwnerId()) {
			player.sendMessage("This pet seems to ignore you. Get your own pet.");
			return true;
		}

		if (option == 1) {
			if (player.getFamiliar().getType().getInventorySize() > 0) {
				player.getPA().sendOptions("Chat", "Storage", "Withdraw");
			} else {
				player.getPA().sendOptions("Chat", "Withdraw");
			}
			player.setDialogueOwner(Summoning.class);
		}

		if (option == 2) {
			if (player.getFamiliar().getType().getInventorySize() > 0) {
				Summoning.openStorage(player);
			}
		}

		return true;
	}

	public static boolean onObjectClick(Player player, int objectId, Location objectLocation, int option) {
		switch (objectId) {
			case 4039:
				if (objectLocation.equals(Location.create(3090, 3492, 0))) {
					player.getPA().spellTeleport(2207, 5346, 0); //tele to summ
					return true;
				}
				return false;
			case 28716:
				if (option == 1) {
					player.getPA().showInterface(52700);
				} else if (option == 2) {
					int summoningLevel = player.getPA().getLevelForXP(player.xp[PlayerConstants.SUMMONING]);

					if (player.level[PlayerConstants.SUMMONING] >= summoningLevel) {
						player.sendMessage("Your Summoning energy is already full.");
						return true;
					}

					player.startAnimation(645);
					player.level[PlayerConstants.SUMMONING] = summoningLevel;
					player.getPA().refreshSkill(PlayerConstants.SUMMONING);
					player.sendMessage("You successfully renew your Summoning points.");
				}
				return true;
		}
		return false;
	}

	public static void onPlayerLoggedIn(Player player) {
		if (player.getFamiliar() != null) {
			Familiar familiar = familiars.stream().filter(x -> x.getNpcId() == player.getFamiliar().getNpcId()).findFirst().orElse(null);

			if (familiar != null) {
				if (familiar.getSpecialClassName() != null) {
					try {
						Constructor<?> constructor = Class.forName("com.zionscape.server.model.players.skills.summoning.specials." + familiar.getSpecialClassName()).getConstructor(Player.class);
						player.getFamiliar().setSpecial((FamiliarSpecial) constructor.newInstance(player));
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}

				NPC npc = Summoning.spawnNpc(player, familiar.getNpcId());
				npc.gfx0(1315);
				npc.animUpdateRequired = true;
				npc.setOwnerId(player.playerId);
				player.getFamiliar().setNpc(npc);
				player.getFamiliar().setType(familiar);
			} else {
				player.setFamiliar(null);
			}
		}
		Summoning.sendInterface(player);
	}

	public static void onPlayerLoggedOut(Player player) {
		if (player.getFamiliar() != null) {
			Summoning.destroyFamiliar(player.getFamiliar().getNpc());
			//player.setFamiliar(null);
		}
	}

	private static void openStorage(Player player) {

		if (GrandExchange.usingGrandExchange(player)) {
			return;
		}

		Summoning.sendFamiliarItems(player);

		player.getItems().resetItems(5064);
		player.getPA().sendFrame248(2700, 5063);

		player.storingFamiliarItems = true;
	}

	public static void process(Player player) {

		// player is teleporting
		if (player.teleTimer > 0) {
			return;
		}

		if (player.getFamiliar() != null && player.getFamiliar().getNpc() != null) {

			// prevent familiars attacking players outside of wilderness
			NPC npc = player.getFamiliar().getNpc();
			if(npc.underAttack && npc.underAttackBy > 0) {
				Player target = PlayerHandler.players[npc.underAttackBy];
				if(!target.inWild()) {
					npc.underAttackBy = 0;
					npc.underAttack = false;
				}
			}

			// decrement ticks left
			player.getFamiliar().decrementTicksLeft();

			// hide npcs within the home area of edgeville
			if(Areas.inHomeArea(player.getLocation())) {
				player.getFamiliar().getNpc().setAttribute("display_to_owner_only", true);

				//player.getFamiliar().getNpc().setAttribute("hidden", true);
			} else {
				//player.getFamiliar().getNpc().removeAttribute("hidden");
				player.getFamiliar().getNpc().removeAttribute("display_to_owner_only");
			}

			/**
			 * The Bunyip passively heals 2% of a player's maximum life points every 15 seconds. Over its 44 minutes
			 * duration, the Bunyip will have healed 352% of a player's maximum life points. Note that the healing
			 * ability still works even if a player does not have any remaining Summoning points. The healing effect
			 * makes the Bunyip useful as it can restore many life points over its duration of 44 minutes.
			 * On a player with 8000 maximum life points, this is roughly equivalent to 23 lobsters or 14 sharks,
			 * assuming one is getting the full health from each fish. Raw fish drops can provide additional healing,
			 * see "Swallow whole" above for detail.
			 */
			if(player.getFamiliar().getNpc().type == 6813) {
				npc = player.getFamiliar().getNpc();

				if(!npc.attributeExists("bunyip") || System.currentTimeMillis() - (long)npc.getAttribute("bunyip") > 15 * 1000) {
					int maxHitpoints = player.getActualLevel(PlayerConstants.HITPOINTS);
					if(player.level[PlayerConstants.HITPOINTS] < maxHitpoints) {
						int addHealth = (int)Math.ceil(maxHitpoints * 0.02);
						if(addHealth > 0) {
							if(player.level[PlayerConstants.HITPOINTS] + addHealth > maxHitpoints) {
								addHealth = maxHitpoints - player.level[PlayerConstants.HITPOINTS];
							}
							player.level[PlayerConstants.HITPOINTS] += addHealth;
							player.getPA().refreshSkill(PlayerConstants.HITPOINTS);
							player.gfx0(1507);
						}
					}
					npc.setAttribute("bunyip", System.currentTimeMillis());
				}
			}

			if (player.getFamiliar().getTicksLeft() == 0) { // destroy the familiar as its ticks have ran out
				npc = player.getFamiliar().getNpc();

				Summoning.destroyFamiliar(npc);

				if (player.storingFamiliarItems) {
					player.getPA().closeAllWindows();
					player.storingFamiliarItems = false;
				}

				for (Item item : player.getFamiliar().getInventoryItems()) {
					ItemDrops.createGroundItem(player, item.getId(), npc.getX(), npc.getY(), item.getAmount(),
							player.getId(), false);
				}

				player.setFamiliar(null);
				Summoning.sendInterface(player);

				player.sendMessage("Your familiar has expired.");
				return;
			} else if (player.getFamiliar().getTicksLeft() % 50 == 0) { // send remaining time
				int minutes = player.getFamiliar().getTicksLeft() / 100;
				int ticksLeft = player.getFamiliar().getTicksLeft() - (minutes * 100);

				player.getPA().sendFrame126(minutes + ":" + (ticksLeft == 50 ? "30" : "00"), 14343);
			}

			// summoning spec bar restore
			if (player.getFamiliar().getSpecialMovesEnergy() < 60) {
				player.getFamiliar().setSpecialBarRestoreTicks(player.getFamiliar().getSpecialBarRestoreTicks() + 1);
				if (player.getFamiliar().getSpecialBarRestoreTicks() >= 50) {
					player.getFamiliar().setSpecialMovesEnergy(player.getFamiliar().getSpecialMovesEnergy() + 15);

					if (player.getFamiliar().getSpecialMovesEnergy() > 60) {
						player.getFamiliar().setSpecialMovesEnergy(60);
					}

					player.getFamiliar().setSpecialBarRestoreTicks(0);
				}
			}

			// checking how many scrolls the player has in the inventory
			int amountOfScrolls = player.getItems().getItemAmount(
					player.getFamiliar().getType().getScroll().getScrollItemId());

			if (amountOfScrolls != player.getFamiliar().getCachedAmountOfScrolls()) {
				player.getFamiliar().setCachedAmountOfScrolls(amountOfScrolls);
				player.getPA().sendFrame126(amountOfScrolls + "", 14324);

				player.getPA().sendFrame36(143, amountOfScrolls);
			}

			// summoning energy draining
			if (player.level[PlayerConstants.SUMMONING] > 0) {
				player.getFamiliar().setEnergyDrainTicks(player.getFamiliar().getEnergyDrainTicks() + 1);

				if (player.getFamiliar().getEnergyDrainTicks() >= 100) {
					player.level[PlayerConstants.SUMMONING] -= 1;
					player.getPA().refreshSkill(PlayerConstants.SUMMONING);

					player.getFamiliar().setEnergyDrainTicks(0);
					Summoning.sendSpecialBarEnergy(player);
				}
			}

			int distance = (int) player.getLocation().getDistance(player.getFamiliar().getNpc().getLocation());
			if (!player.getFamiliar().isBeingRespawned() && (player.getFamiliar().isCalledByPlayer() || distance >= 15)) {
				Summoning.destroyFamiliar(player.getFamiliar().getNpc());
				player.getFamiliar().setBeingRespawned(true);
				player.getFamiliar().setCalledByPlayer(false);
				GameCycleTaskHandler.addEvent(player, new GameCycleTask() {

					@Override
					public void execute(GameCycleTaskContainer container) {
						Player player = container.getPlayer();

						NPC npc = Summoning.spawnNpc(player, player.getFamiliar().getType().getNpcId());
						npc.gfx0(1315);
						npc.animUpdateRequired = true;
						npc.setOwnerId(player.playerId);
						player.getFamiliar().setNpc(npc);
						player.getFamiliar().setBeingRespawned(false);

						container.stop();
					}
				}, 2);
			} else if (distance != 1 && !player.getFamiliar().getNpc().underAttack && player.getFamiliar().getNpc().slot > 0) {
				NPCHandler.follow(player.getFamiliar().getNpc(), player);
			}
		}
	}

	private static void sendFamiliarItems(Player player) {
		List<Item> items = player.getFamiliar().getInventoryItems();

		player.getOutStream().createFrameVarSizeWord(53);
		player.getOutStream().writeWord(2702);
		player.getOutStream().writeWord(items.size());

		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);

			if (item.getAmount() > 254) {
				player.getOutStream().writeByte(255);
				player.getOutStream().writeDWord_v2(item.getAmount());
			} else {
				player.getOutStream().writeByte(item.getAmount());
			}

			player.getOutStream().writeWordBigEndianA(item.getId() + 1);
		}

		player.getOutStream().endFrameVarSizeWord();
		player.flushOutStream();
	}

	public static void sendInterface(Player player) {
		if (player.getFamiliar() == null) {
			player.getPA().sendFrame75(4000, 14321);
			player.getPA().sendFrame126("", 14328);
			player.getPA().sendFrame126("00:00", 14343);
		} else {
			player.getPA().sendFrame75(player.getFamiliar().getType().getNpcId(), 14321);
			player.getPA().sendFrame126(
					NPCHandler.getNpcListName(player.getFamiliar().getType().getNpcId()).replace("_", " "),
					14328);
			player.getPA().sendFrame126(player.getFamiliar().getType().getMinutes() + ":00", 14343);
		}
		player.getPA().sendFrame126("0", 14324);
		player.getPA().sendFrame36(143, 0);
		Summoning.sendSpecialBarEnergy(player);
	}

	private static void sendSpecialBarEnergy(Player player) {
		double percentage = 0;

		if (player.getFamiliar() != null) {
			percentage = Math.round((player.getFamiliar().getSpecialMovesEnergy() * 100) / 60);
		}

		player.getPA().sendFrame126("S P E C I A L  M O V E " + percentage + "%", 14319);
	}

	private static void spawnFamiliar(Player player, Familiar familiar) {


		if (Zombies.inGameArea(player.getLocation()) || Zombies.inLobbyArea(player.getLocation())) {
			return;
		}

		NPC npc = Summoning.spawnNpc(player, familiar.getNpcId());
		npc.gfx0(1315);
		npc.setOwnerId(player.playerId);

		player.setFamiliar(new PlayerFamiliar(npc, familiar));

		if (familiar.getSpecialClassName() != null) {
			try {
				Constructor<?> constructor = Class.forName(
						"com.zionscape.server.model.players.skills.summoning.specials."
								+ familiar.getSpecialClassName()).getConstructor(Player.class);

				player.getFamiliar().setSpecial((FamiliarSpecial) constructor.newInstance(player));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		Summoning.sendFamiliarItems(player);
	}

	private static NPC spawnNpc(Player player, int npcId) {
		// public NPC spawnNpc(int type, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int
		// attack, int defence) {

		int hp = NPCHandler.getNpcListHP(npcId);
		return NPCHandler.spawnNpc(npcId, player.absX, player.absY, player.heightLevel, 0, hp, getFamiliarMaxHit(npcId), getFamiliarAttackRating(npcId), getFamiliarDefenceRating(npcId));
	}

	public static void withdrawItem(Player player, int itemId, int amount, int slot) {

		if (player.openInterfaceId != 2700) {
			return;
		}

		if (player.isBanking || player.inTrade || player.getDuel() != null || player.getFamiliar() == null) {
			return;
		}

		List<Item> inventory = player.getFamiliar().getInventoryItems();
		int totalExisting = (int) inventory.stream().filter(x -> x.getId() == itemId).count();

		if (totalExisting == 0) {
			return;
		}

		boolean stack = ItemUtility.isStackable(itemId);
		int requiredFreeSpace = 0;

		if (stack) {
			if (!player.getItems().playerHasItem(itemId)) {
				requiredFreeSpace = 0;
			}
		} else {
			if (amount > totalExisting) {
				amount = totalExisting;
			}
			requiredFreeSpace = amount;
		}

		int freeSpace = player.getItems().freeInventorySlots();

		if (requiredFreeSpace > 0 && freeSpace == 0) {
			player.sendMessage("You do not have enough inventory space.");
			return;
		}

		if (!stack && requiredFreeSpace > freeSpace) {
			player.sendMessage("You do not have enough inventory space.");
			amount = freeSpace;
		}

		if (stack) {
			Item item = inventory.stream().filter(x -> x.getId() == itemId).findFirst().orElse(null);

			if (item == null) {
				return;
			}

			if (amount > item.getAmount()) {
				amount = item.getAmount();
			}

			if (amount >= item.getAmount()) {
				inventory.remove(item);
			} else {
				item.setAmount(item.getAmount() - amount);
			}
		} else {
			int count = 0;
			Iterator<Item> itr = inventory.iterator();
			while (count < amount) {
				Item item = itr.next();

				if (item.getId() == itemId) {
					itr.remove();

					count++;
				}
			}
		}

		player.getItems().addItem(itemId, amount);

		player.getItems().resetItems(5064);
		Summoning.sendFamiliarItems(player);
	}

	public static void destroyFamiliar(Player player) {
		Summoning.destroyFamiliar(player.getFamiliar().getNpc());
		player.setFamiliar(null);
		Summoning.sendInterface(player);
	}

	public static boolean hasPoucheInInventory(Player player) {
		for (Pouch pouch : pouches) {
			if (player.getItems().playerHasItem(pouch.getPouchItemId())) {
				return true;
			}
		}
		return false;
	}

}