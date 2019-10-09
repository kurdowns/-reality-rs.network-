package com.zionscape.server;

import com.zionscape.server.events.Event;
import com.zionscape.server.events.impl.*;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.*;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.content.cannon.DwarfCannon;
import com.zionscape.server.model.content.grandexchange.GrandExchange;
import com.zionscape.server.model.content.minigames.*;
import com.zionscape.server.model.content.minigames.castlewars.CastleWars;
import com.zionscape.server.model.content.minigames.clanwars.ClanWars;
import com.zionscape.server.model.content.minigames.gambling.Gambling;
import com.zionscape.server.model.content.minigames.quests.QuestHandler;
import com.zionscape.server.model.content.minigames.zombies.Zombies;
import com.zionscape.server.model.content.pets.Pets;
import com.zionscape.server.model.content.treasuretrails.TreasureTrails;
import com.zionscape.server.model.items.*;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.other.FancyDan;
import com.zionscape.server.model.npcs.other.MissCheevers;
import com.zionscape.server.model.npcs.other.TzHaarMejJal;
import com.zionscape.server.model.players.*;
import com.zionscape.server.model.players.commands.impl.Crash;
import com.zionscape.server.model.players.commands.impl.Empty;
import com.zionscape.server.model.players.dialogue.CloseDialogue;
import com.zionscape.server.model.players.skills.Crafting;
import com.zionscape.server.model.players.skills.Prayer;
import com.zionscape.server.model.players.skills.Thieving;
import com.zionscape.server.model.players.skills.farming.Farming;
import com.zionscape.server.model.players.skills.fletching.Fletching;
import com.zionscape.server.model.players.skills.herblore.Herblore;
import com.zionscape.server.model.players.skills.hunter.Hunter;
import com.zionscape.server.model.players.skills.mining.Mining;
import com.zionscape.server.model.players.skills.slayer.Slayer;
import com.zionscape.server.model.players.skills.summoning.Summoning;
import com.zionscape.server.model.players.skills.woodcutting.Woodcutting;
import com.zionscape.server.net.packets.Walking;
import com.zionscape.server.scripting.Scripting;
import com.zionscape.server.scripting.messages.impl.DialogueOptionClickedMessage;
import com.zionscape.server.world.gates.GateHandler;

public class ServerEvents {

	/**
	 * The firstItemId is always the lower item id of the two
	 *
	 * @param player
	 * @param firstItemId
	 * @param firstItemSlot
	 * @param secondItemId
	 * @param secondItemSlot
	 * @return
	 */
	public static boolean itemOnItem(Player player, int firstItemId, int firstItemSlot, int secondItemId, int secondItemSlot) {

		if(firstItemId == 1629 && secondItemId == 1755) {

			if(!player.getItems().playerHasItem(1629)) {
				return true;
			}

			player.getPA().addSkillXP(65, PlayerConstants.CRAFTING);
			player.getItems().deleteItem(1629, 1);
			player.getItems().addItem(1613, 1);

			return true;
		}

		if(Herblore.itemOnItem(player, firstItemId, firstItemSlot, secondItemId, secondItemSlot)) {
			return true;
		}

		if(Crafting.itemOnItem(player, firstItemId, firstItemSlot, secondItemId, secondItemSlot)) {
			return true;
		}

		if (Fletching.itemOnItem(player, firstItemId, firstItemSlot, secondItemId, secondItemSlot)) {
			return true;
		}
		if (Blowpipe.itemOnItem(player, firstItemId, firstItemSlot, secondItemId, secondItemSlot)) {
			return true;
		}
		if (SerpentineHelmet.itemOnItem(player, firstItemId, firstItemSlot, secondItemId, secondItemSlot)) {
			return true;
		}
		if(TridentOfTheSeas.itemOnItem(player, firstItemId, firstItemSlot, secondItemId, secondItemSlot)) {
			return true;
		}
		if(TridentOfTheSwamp.itemOnItem(player, firstItemId, firstItemSlot, secondItemId, secondItemSlot)) {
			return true;
		}
		if (Farming.itemOnItem(player, firstItemId, firstItemSlot, secondItemId, secondItemSlot)) {
			return true;
		}

		return false;
	}

	public static boolean onItemClicked(Player player, int itemId, int itemSlot, int option) {

		Event event = new ItemClickedEvent(player, itemId, option);
		Server.getEventBus().post(event);

		if(event.isHandled()) {
			return true;
		}


		if(Woodcutting.onItemClicked(player, itemId, itemSlot, option)) {
			return true;
		}

		if (Blowpipe.onItemClicked(player, itemId, itemSlot, option)) {
			return true;
		}
		if (SerpentineHelmet.onItemClicked(player, itemId, itemSlot, option)) {
			return true;
		}
		if(TridentOfTheSeas.onItemClicked(player, itemId, itemSlot, option)) {
			return true;
		}
		if(TridentOfTheSwamp.onItemClicked(player, itemId, itemSlot, option)) {
			return true;
		}
		// frozen key
		if (itemId == 20121) {
			for (int i = 20121; i <= 20124; i++) {
				if (!player.getItems().playerHasItem(i)) {
					player.sendMessage("You do not have all the frozen key pieces to create a key.");
					return true;
				}
			}
			for (int i = 20121; i <= 20124; i++) {
				player.getItems().deleteItem(i, 1);
			}
			player.getItems().addItem(20120, 1);
			player.sendMessage("You combine the key pieces together to make a frozen key.");
			Achievements.progressMade(player, Achievements.Types.CREATE_50_FROZEN_KEYS);
			return true;
		}

		if (Hunter.onItemClicked(player, itemId, itemSlot, option)) {
			return true;
		}

		return false;
	}

	public static boolean onWearItem(Player player, int item, int invSlot, int wearSlot) {
		if (CastleWars.onWearItem(player, item, invSlot, wearSlot)) {
			return true;
		}
		return false;
	}

	/**
	 * @param player
	 */
	public static void onPlayerProcess(Player player) {
		Event event = new PlayerProcessEvent(player);
		Server.getEventBus().post(event);

		Summoning.process(player);
		ClanWars.onPlayerProcess(player);
		Gambling.onPlayerProcess(player);
		Pets.process(player);
		Pins.process(player);
	}

	public static boolean onAttackPlayer(Player player, Player target) {
		if (ClanWars.onAttackPlayer(player, target)) {
			return true;
		}
		return false;
	}

	/**
	 * returning this to true will block walking
	 *
	 * @param player
	 * @return
	 */
	public static boolean onPlayerWalk(Player player, Walking.WalkingType type) {
		if (CastleWars.onPlayerWalk(player)) {
			return true;
		}
		if (ClanWars.onPlayerWalk(player, type)) {
			return true;
		}

		Gambling.onPlayerWalk(player, type);

		return false;
	}

	/**
	 * returning this true will block teleporting
	 *
	 * @param player
	 * @return
	 */
	public static boolean onTeleport(Player player) {
		if (CastleWars.onTeleport(player)) {
			return true;
		}
		if (ClanWars.onTeleport(player)) {
			return true;
		}

		GodWarsDungeon.resetKillcount(player);

		return false;
	}

	/**
	 * usually called when closing interfaces
	 *
	 * @param player
	 * @return
	 */
	public static boolean onClickingStuff(Player player) {
		if (CastleWars.onClickingStuff(player)) {
			return true;
		}
		if (ClanWars.onClickingStuff(player)) {
			return true;
		}
		if(Gambling.onClickingStuff(player)) {
			return true;
		}
		return false;
	}

	/**
	 * called when clicking on an npc, this does not include attacking an npc
	 *
	 * @param player
	 * @param npc
	 * @param option
	 * @return
	 */
	public static boolean onNpcClick(Player player, NPC npc, int option) {

		// stop actions when a player is teleporting
		if (player.teleTimer > 0) {
			return false;
		}

		Event event = new NpcClickedEvent(player, npc, option);
		Server.getEventBus().post(event);
		if(event.isHandled()) {
			return true;
		}

		if(TzHaarMejJal.onNpcClick(player, npc, option)) {
			return true;
		}
		if(Pets.onNpcClick(player, npc, option)) {
			return true;
		}
		if (Hunter.onNpcClick(player, npc, option)) {
			return true;
		}

		if (FancyDan.onNpcClick(player, npc, option)) {
			return true;
		}
		if (MissCheevers.onNpcClick(player, npc, option)) {
			return true;
		}

		if (QuestHandler.onNpcClick(player, npc, option)) {
			return true;
		}
		if (LoyaltyTitles.onNpcClick(player, npc, option)) {
			return true;
		}
		if (Slayer.onNpcClick(player, npc, option)) {
			return true;
		}
		if (Zombies.onNpcClick(player, npc, option)) {
			return true;
		}
		// ashuelot reis
		if (npc.type == 13455) {
			if (option == 1) {
				player.getPA().sendNpcChat(npc.type, "Welcome to the Nex lobby.");
			} else {
				player.getPA().openUpBank();
			}
		}

		return false;
	}

	/**
	 * called when a player clicks a button within the client
	 *
	 * @param player
	 * @param button
	 * @return
	 */
	public static boolean onClickingButtons(Player player, int button) {

		Event event = new ClickingButtonEvent(player, button);

		switch (button) {
			case 9157: // option 1
			case 9167:
			case 9178:
			case 9190:
				if (ServerEvents.onDialogueOption(player, 1)) {
					return true;
				}
				break;
			case 9158: // option 2
			case 9168:
			case 9179:
			case 9191:
				if (ServerEvents.onDialogueOption(player, 2)) {
					return true;
				}
				break;
			case 9169: // option 3
			case 9180:
			case 9192:
				if (ServerEvents.onDialogueOption(player, 3)) {
					return true;
				}
				break;
			case 9181: // option 4
			case 9193:
				if (ServerEvents.onDialogueOption(player, 4)) {
					return true;
				}
				break;
			case 9194: // option 5
				if (ServerEvents.onDialogueOption(player, 5)) {
					return true;
				}
				break;
		}

		Server.getEventBus().post(event);
		if(event.isHandled()) {
			return true;
		}

		if(Gambling.onClickingButtons(player, button)) {
			return true;
		}
		if(TreasureTrails.onClickingButtons(player, button)) {
			return true;
		}
		if (MissCheevers.onClickingButtons(player, button)) {
			return true;
		}
		if (Thieving.onClickingButtons(player, button)) {
			return true;
		}
		if (Hunter.onClickingButtons(player, button)) {
			return true;
		}
		if (Trading.onClickingButtons(player, button)) {
			return true;
		}
		if (TeleportInterface.onClickingButtons(player, button)) {
			return true;
		}
		if (QuestHandler.onClickingButtons(player, button)) {
			return true;
		}
		if (CastleWars.onActionButton(player, button)) {
			return true;
		}
		if (ClanWars.onClickingButtons(player, button)) {
			return true;
		}
		if (Achievements.onClickingButtons(player, button)) {
			return true;
		}
		if (InfoTab.onClickingButtons(player, button)) {
			return true;
		}
		if (LoyaltyTitles.onClickingButtons(player, button)) {
			return true;
		}
		if (Slayer.onClickingButtons(player, button)) {
			return true;
		}
		if (GrandExchange.onClickingButtons(player, button)) {
			return true;
		}
		if (BossKillcounts.onClickingButtons(player, button)) {
			return true;
		}

		return false;
	}

	/**
	 * called when an option on a dialogue is pressed
	 *
	 * @param player
	 * @param option
	 * @return
	 */
	public static boolean onDialogueOption(Player player, int option) {

		// stop actions when a player is teleporting
		if (player.teleTimer > 0) {
			return false;
		}

		if(player.getDialogueOwner() != null && player.getDialogueOwner().equals(CloseDialogue.class)) {
			player.resetDialogue();
			return true;
		}

		if(Pins.onDialogueOption(player, option)) {
			return true;
		}
		if(player.attributeExists("pin_block_packets")) {
			return true;
		}

		Event event = new DialogueOptionEvent(player, option);
		Server.getEventBus().post(event);
		if(event.isHandled()) {
			return true;
		}

		if (Scripting.handleMessage(player, new DialogueOptionClickedMessage(option))) {
			return true;
		}
		if(TzHaarMejJal.onDialogueOption(player, option)) {
			return true;
		}
		if (player.getDialogueOwner() != null && Hunter.onDialogueOption(player, option)) {
			return true;
		}
		if (player.getDialogueOwner() != null && TeleportInterface.onDialogueOption(player, option)) {
			return true;
		}
		if (player.getDialogueOwner() != null && Prayer.onDialogueOption(player, option)) {
			return true;
		}
		if (player.getDialogueOwner() != null && Summoning.onDialogueOption(player, option)) {
			return true;
		}
		if (player.getDialogueOwner() != null && QuestHandler.onDialogueOption(player, option)) {
			return true;
		}
		if (player.getDialogueOwner() != null && Slayer.onDialogueOption(player, option)) {
			return true;
		}
		if (player.getDialogueOwner() != null && Zombies.onDialogueOption(player, option)) {
			return true;
		}
		if (player.getDialogueOwner() != null && FancyDan.onDialogueOption(player, option)) {
			return true;
		}
		if (player.getDialogueOwner() != null && MissCheevers.onDialogueOption(player, option)) {
			return true;
		}
		if (Empty.onDialogueOption(player, option)) {
			return true;
		}
		if (player.getDialogueOwner() != null && player.getDialogueOwner().equals(FightCaves.class)) {
			if (player.getCurrentDialogueId() == 1) {
				if (option == 1) {
					FightCaves.enter(player);
					player.getPA().closeAllWindows();
				}

				if (option == 2) {
					if (player.getData().enteredFightKiln) {
						FightKiln.enterFightKiln(player);
					} else {
						player.getPA().sendNpcChat(2617, "As this is your first time here, you must sacrifice your Fire cape. Would you like to do that?");
						player.setCurrentDialogueId(2);
					}
				}
				return true;
			}
			if (player.getCurrentDialogueId() == 2) {
				if (option == 1) {
					if (player.getItems().playerHasItem(6570)) {
						player.getPA().sendNpcChat(2617, "You may now enter the Fight Kiln as many times as you like. Good luck.");
						player.setCurrentDialogueId(4);
						player.getItems().deleteItem(6570, 1);
						player.getData().enteredFightKiln = true;
					} else {
						player.getPA().sendNpcChat(2617, "You do not have a Firecape to sacrifice.");
						player.setCurrentDialogueId(3);
					}
				}
				if (option == 2) {
					player.setDialogueOwner(null);
					player.setCurrentDialogueId(-1);
					player.getPA().closeAllWindows();
				}
				return true;
			}
		}

		return false;
	}

	/**
	 * called when a player clicks continue on dialogue
	 *
	 * @param player
	 * @return
	 */
	public static boolean onDialogueContinue(Player player) {

		// stop actions when a player is teleporting
		if (player.teleTimer > 0) {
			return false;
		}

		if(player.getDialogueOwner() != null && player.getDialogueOwner().equals(CloseDialogue.class)) {
			player.resetDialogue();
			return true;
		}

		if(Pins.onDialogueContinue(player)) {
			return true;
		}

		if(player.attributeExists("pin_block_packets")) {
			return true;
		}

		Event event = new DialogueContinueEvent(player);
		Server.getEventBus().post(event);
		if(event.isHandled()) {
			return true;
		}

		if(TzHaarMejJal.onDialogueContinue(player)) {
			return true;
		}
		if (QuestHandler.onDialogueContinue(player)) {
			return true;
		}
		if (Slayer.onDialogueContinue(player)) {
			return true;
		}
		if (Zombies.onDialogueContinue(player)) {
			return true;
		}
		if (player.getDialogueOwner() != null && player.getDialogueOwner().equals(FightCaves.class)) {

			switch (player.getCurrentDialogueId()) {
				case 2:
					player.getPA().sendOptions("Yes", "No");
					break;
				case 3:
					player.setDialogueOwner(null);
					player.setCurrentDialogueId(-1);
					player.getPA().closeAllWindows();
					break;
				case 4:
					FightKiln.enterFightKiln(player);
					break;
				default:
					player.getPA().sendOptions("Enter Fight Caves - Obtain Fire cape", "Enter Fight Kiln - Obtain Tokhaar-Kal");
					player.setCurrentDialogueId(1);
					break;
			}
			return true;
		}

		return false;
	}

	/**
	 * called when an npc is killed by a player
	 *
	 * @param player
	 * @param npc
	 */
	public static void onNpcKilled(Player player, NPC npc) {

		Event event = new NpcDiedEvent(player, npc);
		Server.getEventBus().post(event);

		player.getData().monstersKilled++;

		if (npc.type == 8133) {
			Achievements.progressMade(player, Achievements.Types.KILL_500_CORPS);
		}

		if (npc.type >= 2042 && npc.type <= 2044) {
			Achievements.progressMade(player, Achievements.Types.KILL_400_ZULRAHS);
		}

		if (npc.type == 5862) {
			Achievements.progressMade(player, Achievements.Types.KILL_400_CERBERUS);
		}

		if(npc.type == 493) {
			Achievements.progressMade(player, Achievements.Types.KILL_500_KRAKENS);
		}

		if(npc.type == 14383) {
			Achievements.progressMade(player, Achievements.Types.KILL_250_CALLISTO);
		}
		if(npc.type == 14384) {
			Achievements.progressMade(player, Achievements.Types.KILL_300_VENENATIS);
		}
		if(npc.type == 14385) {
			Achievements.progressMade(player, Achievements.Types.KILL_300_THERMO);
		}
		if(npc.type == 14386) {
			Achievements.progressMade(player, Achievements.Types.KILL_250_VETIONS);
		}

		// kill 25 cows achievement
		if (npc.type == 81) {
			Achievements.progressMade(player, Achievements.Types.KILL_250_COWS);
		}
		if (npc.type == 1592 || npc.type == 3590) {
			Achievements.progressMade(player, Achievements.Types.KILL_150_STEEL_DRAGONS);
		}
		if (npc.type == 2745) {
			Achievements.progressMade(player, Achievements.Types.DEFEAT_JAD_TWICE);
			Achievements.progressMade(player, Achievements.Types.KILL_JAD_50_TIMES);
		}
		if (npc.type == 50) {
			Achievements.progressMade(player, Achievements.Types.KILL_250_KBD);
		}
		if (npc.type == 13447) {
			Achievements.progressMade(player, Achievements.Types.KILL_NEX_100_TIMES);
		}
		if (npc.type >= 10770 && npc.type <= 10775) {
			Achievements.progressMade(player, Achievements.Types.KILL_1500_FROST_DRAGONS);
		}
		if (npc.type >= 8349 && npc.type <= 8366) {
			Achievements.progressMade(player, Achievements.Types.KILL_400_TORMENTED_DEMONS);
		}
		if (npc.type >= 13820 && npc.type <= 13822) {
			Achievements.progressMade(player, Achievements.Types.KILL_500_JADINKOS);
		}
		if (npc.getDefinition().getName() != null && npc.getDefinition().getName().startsWith("Revenant")) {
			Achievements.progressMade(player, Achievements.Types.KILL_500_REVENANTS);
		}
		if (npc.type == 14301) {
			Achievements.progressMade(player, Achievements.Types.KILL_500_GLACORS);
		}
		if (npc.type == 6260 || npc.type == 6222 || npc.type == 6203 || npc.type == 6247 || npc.type == 13447) {
			Achievements.progressMade(player, Achievements.Types.KILL_EACH_GWD_BOSS, npc.type);
		}

		BossKillcounts.onNpcKilled(player, npc);
		LoyaltyTitles.onNpcKilled(player, npc);
		FightCaves.onNpcKilled(player, npc);
		FightKiln.onNpcKilled(player, npc);
		QuestHandler.onNpcKilled(player, npc);
		Slayer.onNpcKilled(player, npc);
		Zombies.onNpcKilled(player, npc);
	}

	/**
	 * called when x amount is entered in dialogue
	 *
	 * @param player
	 * @param interfaceId
	 * @param amount
	 * @return
	 */
	public static boolean onXAmountEntered(Player player, int interfaceId, int amount) {
		if (Prayer.onXAmountEntered(player, interfaceId, amount)) {
			return true;
		}
		if (GrandExchange.onXAmountEntered(player, interfaceId, amount)) {
			return true;
		}
		return false;
	}

	/**
	 * called when a player logs in
	 *
	 * @param player
	 */
	public static void onPlayerLoggedIn(Player player) {

		Server.getEventBus().post(new PlayerLoggedInEvent(player));

		Pins.onPlayerLoggedIn(player);
		Pets.onPlayerLoggedIn(player);
		InfoTab.onPlayerLoggedIn(player);
		QuestHandler.onPlayerLoggedIn(player);
		Summoning.onPlayerLoggedIn(player);
		CastleWars.onPlayerLogin(player);
		ClanWars.onPlayerLoggedIn(player);
		Achievements.onPlayerLoggedIn(player);
		FightKiln.onPlayerLoggedIn(player);
		FightCaves.onPlayerLoggedIn(player);
		Zombies.onPlayerLoggedIn(player);
		Farming.onPlayerLoggedIn(player);

		// overloads
		if (System.currentTimeMillis() - player.getData().lastOverloadPotion < 5 * 60 * 1000) {
			Potions.startOverloadPotionEvent(player);
		}
	}

	/**
	 * called when a player logs out
	 *
	 * @param player
	 */
	public static void onPlayerLoggedOut(Player player) {
		Pets.onPlayerLoggedOut(player);
		Gambling.onPlayerLoggedOut(player);
		if (player.inCastleWars()) {
			CastleWars.onLoggedOut(player);
		}
		if (player.getDuel() != null) {
			DuelArena.playerDisconnected(player);
		}
		if (player.getCannon() != null) {
			DwarfCannon.pickupCannon(player, -1, null, true);
		}
		PestControl.playerLoggedOut(player);
		ClanWars.onPlayerLoggedOut(player);
		FightKiln.onPlayerLoggedOut(player);
		FightCaves.onPlayerLoggedOut(player);
		Slayer.onLoggedOut(player);
		Zombies.onPlayerLoggedOut(player);
		MagicArena.onPlayerLoggedOut(player);
		Summoning.onPlayerLoggedOut(player);

		if (player.instancesArea != null) {
			player.instancesArea.leave(false);
			player.instancesArea = null;
		}

	}

	/**
	 * called when a player clicks an object
	 *
	 * @param player
	 * @param objectId
	 * @param objectLocation
	 * @param option
	 * @return
	 */
	public static boolean onObjectClick(Player player, int objectId, Location objectLocation, int option) {
		Event event = new ClickObjectEvent(player, objectId, objectLocation, option);
		Server.getEventBus().post(event);
		if(event.isHandled()) {
			return true;
		}

		if (GateHandler.onObjectClick(player, objectId, objectLocation, option)) {
			return true;
		}
		if (Zombies.onObjectClick(player, objectId, objectLocation, option)) {
			return true;
		}
		if (ClanWars.onObjectClick(player, objectId, objectLocation, option)) {
			return true;
		}
		if (CastleWars.onObjectClick(player, objectId, objectLocation, option)) {
			return true;
		}
		if (Summoning.onObjectClick(player, objectId, objectLocation, option)) {
			return true;
		}
		if (DwarfCannon.clickingObject(player, objectId, objectLocation, option)) {
			return true;
		}
		if (FightKiln.onObjectClick(player, objectId, objectLocation, option)) {
			return true;
		}
		if (FightCaves.onObjectClick(player, objectId, objectLocation, option)) {
			return true;
		}
		if (Woodcutting.onObjectClick(player, objectId, objectLocation, option)) {
			return true;
		}
		if (Mining.onObjectClick(player, objectId, objectLocation, option)) {
			return true;
		}
		if (Obelisks.onObjectClick(player, objectId, objectLocation, option)) {
			return true;
		}
		if (Farming.onObjectClick(player, objectId, objectLocation, option)) {
			return true;
		}
		if (Hunter.onObjectClick(player, objectId, objectLocation, option)) {
			return true;
		}
		// fight cave entrance
		if (objectId == 9356) {
			player.getPA().sendNpcChat(2617, "Hello and welcome to the Fight caves. How can I help you?");
			player.setDialogueOwner(FightCaves.class);
			return true;
		}

		// nex landslide::
		if (objectId == 57225) {
			if (player.absX <= 5204) {
				player.getPA().movePlayer(2911, 5203, 0);
			} else {
				player.sendMessage("You cannot escape this prison!");
			}
			return true;
		}

		if (objectId >= 14826 && objectId <= 14831) {
			Achievements.progressMade(player, Achievements.Types.USE_WILDERNESS_OBELISK);
		}
		return false;
	}

	public static void onPlayerDied(Player player) {

		if (player.instancesArea != null) {
			player.instancesArea.leave(true);
			player.instancesArea = null;
		}

		DwarfCannon.playerDied(player);
		GodWarsDungeon.resetKillcount(player);
	}

	public static boolean onItemOnPlayer(Player player, Player other, Item item) {
		if (Slayer.onItemOnPlayer(player, other, item)) {
			return true;
		}
		return false;
	}

	public static boolean onPlayerDroppedItem(Player player, Item item) {
		if(Pets.onPlayerDroppedItem(player, item)) {
			return true;
		}
		if (Blowpipe.onPlayerDroppedItem(player, item)) {
			return true;
		}
		if (SerpentineHelmet.onPlayerDroppedItem(player, item)) {
			return true;
		}
		return false;
	}

}