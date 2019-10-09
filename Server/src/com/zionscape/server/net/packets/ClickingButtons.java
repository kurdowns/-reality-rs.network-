package com.zionscape.server.net.packets;

import com.zionscape.server.Config;
import com.zionscape.server.Server;
import com.zionscape.server.ServerEvents;
import com.zionscape.server.model.content.Resting;
import com.zionscape.server.model.content.Tanner;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.content.minigames.DuelArena;
import com.zionscape.server.model.content.minigames.Lottery;
import com.zionscape.server.model.content.minigames.MagicArena;
import com.zionscape.server.model.content.minigames.christmas.BaubleMaking;
import com.zionscape.server.model.content.minigames.gambling.Gambling;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.PvPArtifacts;
import com.zionscape.server.model.players.combat.CombatHelper;
import com.zionscape.server.model.players.commands.impl.Gamble;
import com.zionscape.server.model.players.skills.fletching.Fletching;
import com.zionscape.server.model.players.skills.smithing.Smithing;
import com.zionscape.server.model.players.skills.summoning.Summoning;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.scripting.Scripting;
import com.zionscape.server.scripting.messages.impl.ButtonClickedMessage;
import com.zionscape.server.util.Misc;
import com.zionscape.server.util.Stream;
import com.zionscape.server.world.shops.Shops;

/**
 * Clicking most buttons
 */
public class ClickingButtons implements PacketType {

	public static final int[] cook = {377, 389, 7944, 383, 371, 335, 317};

	public static final int[][] SKILL_IDs = {
			{33206, PlayerConstants.ATTACK},
			{33209, PlayerConstants.STRENGTH},
			{33212, PlayerConstants.DEFENCE},
			{33215, PlayerConstants.RANGED},
			{33218, PlayerConstants.PRAYER},
			{33221, PlayerConstants.MAGIC},
			{33224, PlayerConstants.RUNECRAFTING},
			{105243, PlayerConstants.CONSTRUCTION},
			{33207, PlayerConstants.HITPOINTS},
			{33210, PlayerConstants.AGILITY},
			{34157, PlayerConstants.HERBLORE},
			{33216, PlayerConstants.THIEVING},
			{33219, PlayerConstants.CRAFTING},
			{33222, PlayerConstants.FLETCHING},
			{47130, PlayerConstants.SLAYER},
			{105244, PlayerConstants.HUNTER},
			{33208, PlayerConstants.MINING},
			{33211, PlayerConstants.SMITHING},
			{33214, PlayerConstants.FISHING},
			{33217, PlayerConstants.COOKING},
			{33220, PlayerConstants.FIREMAKING},
			{33223, PlayerConstants.WOODCUTTING},
			{54104, PlayerConstants.FARMING},
			{105245, PlayerConstants.SUMMONING}
	};

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(Player player, int packetType, int packetSize, Stream stream) {
		int actionButtonId = Misc.hexToInt(stream.buffer, 0, packetSize);
		if (player.rights >= 3) {
			player.sendMessage("Action button: " + actionButtonId + " :--)");
		}
		// int actionButtonId = stream.readShort();
		if (player.isDead) {
			return;
		}

		if(Gambling.inStake(player)) {
			return;
		}

		if (actionButtonId != 75108 && actionButtonId != 75109 && actionButtonId != 75110 && actionButtonId != 75121 && actionButtonId != 75115 && actionButtonId != 75111 && actionButtonId != 75112 && actionButtonId != 75113 && actionButtonId != 9157 && actionButtonId != 9158 && (player.openInterfaceId == 19302 || !player.getData().completedTutorial || player.attributeExists("doing_tutorial"))) {
			return;
		}

		if (Scripting.handleMessage(player, new ButtonClickedMessage(actionButtonId))) {
			return;
		}

		// stop players telporting etc when doing the tutorial
		if (player.isDoingTutorial()) {
			if (actionButtonId != 9157 && actionButtonId != 9158) {
				return;
			}
		}
		if (ServerEvents.onClickingButtons(player, actionButtonId)) {
			return;
		}

		if(player.attributeExists("pin_block_packets")) {
			return;
		}

		if (Tanner.onActionButton(player, actionButtonId)) {
			return;
		}
		if (player.assignAutocast(actionButtonId)) {
			return;
		}
		if (CombatHelper.setAttackStyle(player, actionButtonId)) {
			if (player.autocasting) {
				player.getPA().resetAutocast();
			}
			CombatHelper.setAttackVars(player);
			return;
		}
		if (Summoning.onActionButton(player, actionButtonId)) {
			return;
		}
		if (player.isBanking && player.getBank().clickingButton(actionButtonId)) {
			return;
		}
		if (player.getDuel() != null && DuelArena.clickingButton(player, actionButtonId)) {
			return;
		}
		int[] spellIds = {4128, 4130, 4132, 4134, 4136, 4139, 4142, 4145, 4148, 4151, 4153, 4157, 4159, 4161, 4164,
				4165, 4129, 4133, 4137, 6006, 6007, 6026, 6036, 6046, 6056, 4147, 6003, 47005, 4166, 4167, 4168, 48157,
				50193, 50187, 50101, 50061, 50163, 50211, 50119, 50081, 50151, 50199, 50111, 50071, 50175, 50223,
				50129, 50091};
		for (int i = 0; i < spellIds.length; i++) {
			if (actionButtonId == spellIds[i]) {
				player.autocasting = true;
				player.autocastId = i;
			}
		}

		for (int i = 0; i < SKILL_IDs.length; i++) {
			if (SKILL_IDs[i][0] == actionButtonId) {

				if (player.muted) {
					break;
				}

				player.forcedChat("[QC] My " + PlayerConstants.SKILL_NAMES[SKILL_IDs[i][1]] + " is " + player.getActualLevel(SKILL_IDs[i][1]) + " with " + Misc.formatNumber(player.xp[SKILL_IDs[i][1]]) + " xp.");
				break;
			}
		}

		// Achievements.handleButtons(c, actionButtonId);
		/**
		 * WeaponGame Interface
		 */
		/*
		 * attack: 33206
		 * 
		 * str: 33209 def: 33212 rng: 33215 hp: 33207 pray: 33218 mge: 33221
		 */
		switch (actionButtonId) {

			// fletching
			case 34185: // any shortbow(u) fletch

				if (player.craftingLeather) {
					player.getCrafting().handleCraftingClick(actionButtonId);
					return;
				}

				player.getPA().removeAllWindows();
				Fletching.startFletching(player, 1, "shortbow");
				break;
			case 34193: // any longbow(u) fletch

				if (player.craftingLeather) {
					player.getCrafting().handleCraftingClick(actionButtonId);
					return;
				}

				player.getPA().removeAllWindows();
				Fletching.startFletching(player, 1, "longbow");
				break;
			case 34184: // make any shortbow(u) x 5

				if (player.craftingLeather) {
					player.getCrafting().handleCraftingClick(actionButtonId);
					return;
				}

				player.getPA().removeAllWindows();
				Fletching.startFletching(player, 5, "shortbow");
				break;
			case 34192: // make any longbow(u) x 5
				if (player.craftingLeather) {
					player.getCrafting().handleCraftingClick(actionButtonId);
					return;
				}

				player.getPA().removeAllWindows();
				Fletching.startFletching(player, 5, "longbow");
				break;
			case 34183: // make any shortbow(u) x 10

				if (player.craftingLeather) {
					player.getCrafting().handleCraftingClick(actionButtonId);
					return;
				}

				player.getPA().removeAllWindows();
				Fletching.startFletching(player, 10, "shortbow");
				break;
			case 34191: // make any longbow(u) x 10

				if (player.craftingLeather) {
					player.getCrafting().handleCraftingClick(actionButtonId);
					return;
				}

				player.getPA().removeAllWindows();
				Fletching.startFletching(player, 10, "longbow");
				break;
			case 34189: // make 1x arrowshafts

				if (player.craftingLeather) {
					player.getCrafting().handleCraftingClick(actionButtonId);
					return;
				}

				if (player.getAttribute("logId") != "1511") {
					player.getPA().removeAllWindows();
					Fletching.startFletching(player, 1, "shaft");
					Fletching.setLogId(player, 52);
				}
				break;
			case 34188: // make 5x arrowshafts


				if (player.craftingLeather) {
					player.getCrafting().handleCraftingClick(actionButtonId);
					return;
				}

				if (player.getAttribute("logId") != "1511") {
					player.getPA().removeAllWindows();
					Fletching.startFletching(player, 5, "shaft");
					Fletching.setLogId(player, 52);
				}
				break;
			case 34187: // make 10x arrowshafts

				if (player.craftingLeather) {
					player.getCrafting().handleCraftingClick(actionButtonId);
					return;
				}

				if (player.getAttribute("logId") != "1511") {
					player.getPA().removeAllWindows();
					Fletching.startFletching(player, 10, "shaft");
					Fletching.setLogId(player, 52);
				}
				break;

			case 2:
				player.getPA().showInterface(53399);
				break;

			case 3139:
				player.getData().brightness = 0;
				break;
			case 3140:
				player.getData().brightness = 1;
				break;
			case 3142:
				player.getData().brightness = 2;
				break;
			case 3144:
				player.getData().brightness = 3;
				break;

			case 1: // reset xp counter
				player.getData().xpCounter = 0;
				break;

			case 211236: // Note(Stuart): ape atoll teleport
				player.getPA().spellTeleport(2787, 2786, 0);
				break;

			case 88234:
				player.getPA().showInterface(23871);
				break;
			case 93067:
				player.getPA().showInterface(22760);
				break;

			case 59097:
				player.getPA().showInterface(15106);
				break;
			case 66206:
				player.getPA().closeAllWindows();
				break;
			case 59100:
				player.getPA().sendFrame126("Items kept on death", 17103);
				for (int k = 0; k < 4; k++)
					player.getPA().sendFrame34a(10494, -1, k, 1);
				for (int k = 0; k < 39; k++)
					player.getPA().sendFrame34a(10600, -1, k, 1);
				for (int k = 17109; k <= 17130; k++)
					player.getPA().sendFrame126("", k);

				int amount = player.getItems().amountOfItemsKeptOnDeath();

				player.getPA().sendFrame126("~ " + amount + " ~", 17108);
				if (amount == 0) {
					player.getPA().sendFrame126("Skulled", 17110);
				} else if (amount == 1) {
					player.getPA().sendFrame126("Skulled", 17110);
					player.getPA().sendFrame126("Protect Item", 17111);
				} else if (amount == 3) {
					player.getPA().sendFrame126("Not Skulled", 17110);
				} else if (amount == 4) {
					player.getPA().sendFrame126("Not Skulled", 17110);
					player.getPA().sendFrame126("Protect Item", 17111);
				}

				player.getItems().keepItemsOnDeath(amount, true);

				player.getPA().showInterface(17100);
				break;

			case 118098:

				if (player.getDuel() != null) {
					if (!player.getDuel().isStarted()) {
						player.sendMessage("The duel hasn't started yet!");
						player.playerIndex = 0;
						return;
					}
					if (player.getDuel().getRules()[PlayerConstants.DUEL_MAGE]) {
						player.sendMessage("Magic has been disabled in this duel!");
						return;
					}
				}

				if (player.getPA().getLevelForXP(player.xp[6]) >= 94) {
					if (System.currentTimeMillis() - player.lastVeng > 30000) {
						if (player.getItems().playerHasItem(557, 10) && player.getItems().playerHasItem(9075, 4) && player.getItems().playerHasItem(560, 2)) {
							player.vengOn = true;
							player.lastVeng = System.currentTimeMillis();
							player.startAnimation(4410);
							player.gfx100(726);
							player.getPA().addSkillXP(112, PlayerConstants.MAGIC);
							player.getItems().deleteItem(557, player.getItems().getItemSlot(557), 10);
							player.getItems().deleteItem(560, player.getItems().getItemSlot(560), 2);
							player.getItems().deleteItem(9075, player.getItems().getItemSlot(9075), 4);
						} else {
							player.sendMessage("You do not have the required runes to cast this spell.");
						}
					} else {
						player.sendMessage("You must wait 30 seconds before casting this again.");
					}
				} else {
					player.sendMessage("You need 94 magic to cast vengeance!");
				}
				break;

			case 27069:
				if (player.puzzle != null)
					player.puzzle.sendHint();
				break;

			case 55095:
				if (player.destroyItem > 0) {
					player.getPA().closeAllWindows();
					player.getItems().deleteItem(player.destroyItem, player.destroyAmount);

					if (player.destroyAmount > 0) {
						player.sendMessage("Your " + player.getItems().getItemName(player.destroyItem)
								+ " vanish as you drop them on the ground.");
					} else {
						player.sendMessage("Your " + player.getItems().getItemName(player.destroyItem)
								+ " vanishes as you drop it on the ground.");
					}
					player.destroyItem = 0;
					player.destroyAmount = 0;
				}
				break;
			case 55096:
				player.getPA().closeAllWindows();
				break;
			case 61250:/** Red */
			case 61248:/** Yellow */
			case 61253:/** Blue */
			case 61251:/** Green */
			case 61252:
				/** Pink */
				BaubleMaking.handleBaublePainting(player, actionButtonId);
				break;
		/*
         * case 33206: case 33209: case 33215: case 33212: case 33207: case 33218: case 33221: //ihatethatshit:--(
		 * c.getPA().closeAllWindows(); c.outStream.createFrame(27); c.skillInput = actionButtonId; break;
		 */
			case 191206: // close
				player.getPA().closeAllWindows();
				player.currentClass = 0;
				break;
			case 191207: // melee
				player.currentClass = 1;
				player.getPA().sendFrame126("Current Class: Melee", 49107);
				player.getPA().sendFrame126("Class Level: " + player.meleeClassLvl, 49108);
				break;
			case 191208: // ranged
				player.currentClass = 2;
				player.getPA().sendFrame126("Current Class: Ranged", 49107);
				player.getPA().sendFrame126("Class Level: " + player.rangedClassLvl, 49108);
				break;
			case 191209: // magic
				player.currentClass = 3;
				player.getPA().sendFrame126("Current Class: Magic", 49107);
				player.getPA().sendFrame126("Class Level: " + player.magicClassLvl, 49108);
				break;
		}
		/** CURSES **/
		if (player.playerPrayerBook == 1) {
			switch (actionButtonId) {
				case 87231:
					player.getCombat().activateCurse(0);
					break;
				case 87233:
					player.getCombat().activateCurse(1);
					break;
				case 87235:
					player.getCombat().activateCurse(2);
					break;
				case 87237:
					player.getCombat().activateCurse(3);
					break;
				case 87239:
					player.getCombat().activateCurse(4);
					break;
				case 87241:
					player.getCombat().activateCurse(5);
					break;
				case 87243:
					player.getCombat().activateCurse(6);
					break;
				case 87245:
					player.getCombat().activateCurse(7);
					break;
				case 87247:
					player.getCombat().activateCurse(8);
					break;
				case 87249:
					player.getCombat().activateCurse(9);
					break;
				case 87251:
					player.getCombat().activateCurse(10);
					break;
				case 87253:
					player.getCombat().activateCurse(11);
					break;
				case 87255:
					player.getCombat().activateCurse(12);
					break;
				case 88001:
					player.getCombat().activateCurse(13);
					break;
				case 88003:
					player.getCombat().activateCurse(14);
					break;
				case 88005:
					player.getCombat().activateCurse(15);
					break;
				case 88007:
					player.getCombat().activateCurse(16);
					break;
				case 88009:
					player.getCombat().activateCurse(17);
					break;
				case 88011:
					player.getCombat().activateCurse(18);
					break;
				case 88013:
					player.getCombat().activateCurse(19);
					break;
			}
		}
		/** Prayers **/
		if (player.playerPrayerBook == 0) {
			switch (actionButtonId) {
				case 21233: // thick skin
					player.getCombat().activatePrayer(0);
					break;
				case 21234: // burst of str
					player.getCombat().activatePrayer(1);
					break;
				case 21235: // charity of thought
					player.getCombat().activatePrayer(2);
					break;
				case 70080: // range
					player.getCombat().activatePrayer(3);
					break;
				case 70082: // mage
					player.getCombat().activatePrayer(4);
					break;
				case 21236: // rockskin
					player.getCombat().activatePrayer(5);
					break;
				case 21237: // super human
					player.getCombat().activatePrayer(6);
					break;
				case 21238: // improved reflexes
					player.getCombat().activatePrayer(7);
					break;
				case 21239: // hawk eye
					player.getCombat().activatePrayer(8);
					break;
				case 21240:
					player.getCombat().activatePrayer(9);
					break;
				case 21241: // protect Item
					player.getCombat().activatePrayer(10);
					break;
				case 0:
					player.getCombat().activatePrayer(10);
					break;
				case 70084: // 26 range
					player.getCombat().activatePrayer(11);
					break;
				case 70086: // 27 mage
					player.getCombat().activatePrayer(12);
					break;
				case 21242: // steel skin
					player.getCombat().activatePrayer(13);
					break;
				case 21243: // ultimate str
					player.getCombat().activatePrayer(14);
					break;
				case 21244: // incredible reflex
					player.getCombat().activatePrayer(15);
					break;
				case 21245: // protect from magic
					player.getCombat().activatePrayer(16);
					break;
				case 21246: // protect from range
					player.getCombat().activatePrayer(17);
					break;
				case 21247: // protect from melee
					player.getCombat().activatePrayer(18);
					break;
				case 70088: // 44 range
					player.getCombat().activatePrayer(19);
					break;
				case 70090: // 45 mystic
					player.getCombat().activatePrayer(20);
					break;
				case 2171: // retrui
					player.getCombat().activatePrayer(21);
					break;
				case 2172: // redem
					player.getCombat().activatePrayer(22);
					break;
				case 2173: // smite
					player.getCombat().activatePrayer(23);
					break;
				case 70092: // chiv
					player.getCombat().activatePrayer(24);
					break;
				case 70094: // piety
					player.getCombat().activatePrayer(25);
					break;
				case 98024: // rigour
					player.getCombat().activatePrayer(26);
					break;
				case 98018: // augury
					player.getCombat().activatePrayer(27);
					break;
			}
		}
		switch (actionButtonId) {
			// crafting + fletching interface:
			case 150:
				if (player.autoRet == 0) {
					player.autoRet = 1;
				} else {
					player.autoRet = 0;
				}
				break;

		/*
         * Cooking buttons.
		 */
			case 53152:
				if (!player.isCooking) {
					player.getCooking().cookItem(player.getCooking().cookingItem, 1);
				}
				break;
			case 53151:
				if (!player.isCooking) {
					player.getCooking().cookItem(player.getCooking().cookingItem, 5);
				}
				break;
			case 53150:
				player.getOutStream().createFrame(27);
				break;
			case 53149:
				if (!player.isCooking) {
					player.getCooking().cookItem(player.getCooking().cookingItem,
							player.getItems().getItemAmount(player.getCooking().cookingItem));
				}
				break;
			case 26010:
				player.getPA().resetAutocast();
				break;
        /*
         * case 62159://pots if (c.getPA().inMiniGame() || c.inWild()) { return; } c.getItems().addItem(6686, 1000);
		 * c.getItems().addItem(2437, 1000); c.getItems().addItem(2441, 1000); c.getItems().addItem(2443, 1000);
		 * c.getItems().addItem(2445, 1000); c.getItems().addItem(2435, 1000); c.getItems().addItem(3025, 1000);
		 * c.getItems().addItem(2447, 1000); break; case 62160://barrage runes if (c.getPA().inMiniGame() || c.inWild())
		 * { return; } c.getItems().addItem(560, 1000); c.getItems().addItem(555, 1000); c.getItems().addItem(565,
		 * 1000); break;
		 */
			case 9167:
			case 9168:
			case 9169:
				this.doThreeOptionClick(player, actionButtonId);
				break;
			// 1st tele option
			case 9190:
            /*
			 * if (c.teleAction == 1) { //rock crabs c.getPA().spellTeleport(2676, 3715, 0); } else if (c.teleAction ==
			 * 2) { //barrows c.getPA().spellTeleport(3565, 3314, 0); } else if (c.teleAction == 3) { //godwars
			 * c.getPA().spellTeleport(2916, 3612, 0); c.sendMessage(
			 * "@red@If you die you will loose all your items in GWD. You have been warned." ); } else if (c.teleAction
			 * == 4) { //varrock wildy c.getPA().spellTeleport (3087+Misc.random(2),3558+Misc.random(2),0); } else if
			 * (c.teleAction == 5) { c.getPA().spellTeleport(3046,9779,0); } else if (c.teleAction == 6) { //Torm
			 * demon(S) c.getPA().spellTeleport(3233,9369,0); }
			 */
			/*
			 * if (c.teleAction == 400) {// edgeville tele, home object teleport c.getPA().spellTeleport(3093, 3493, 0);
			 * } else if (c.teleAction == 401) {// bork teleport, home object // teleport c.getPA().spellTeleport(3167,
			 * 2983, 0); c.sendMessage("Moving Bork."); } else if (c.teleAction == 402) {// kbd teleport, home object //
			 * teleport c.getPA().spellTeleport(2273, 4682, 0); } else if (c.teleAction == 403) {// fishing teleport,
			 * home object // teleport c.getPA().spellTeleport(2603, 3403, 0); } else if (c.teleAction == 404) {//
			 * zombies teleport, home object // teleport c.getPA().spellTeleport(2484, 3046, 0); }
			 */
				if (player.dialogueAction == 10) {
					player.getPA().spellTeleport(2845, 4832, 0);
					player.dialogueAction = -1;
				} else if (player.dialogueAction == 11) {
					player.getPA().spellTeleport(2786, 4839, 0);
					player.dialogueAction = -1;
				} else if (player.dialogueAction == 12) {
					player.getPA().spellTeleport(2398, 4841, 0);
					player.dialogueAction = -1;
				} else if (player.dialogueAction == 50) {
					player.getPA().buyColoredWhip("blue");
					player.dialogueAction = -1;
					player.getPA().removeAllWindows();
				} else if (player.dialogueAction == 1565) {
					player.getDH().sendDialogues(1569, 3082);
					player.dialogueAction = 0;
				}
				break;
			// mining - 3046,9779,0
			// smithing - 3079,9502,0
			// 2nd tele option
			case 9191:
				// if (c.teleAction == 1) {
				// tav dungeon
				// c.getPA().spellTeleport(2884, 9798, 0);
				// } else if (c.teleAction == 300) {
				// druids
				// c.getPA().spellTeleport(2896+(Misc.random(2)-Misc.random(2)),
				// 3444+(Misc.random(2)-Misc.random(2)), 0);
				// } else if (c.teleAction == 2) {
				// pest control
				// c.getPA().spellTeleport(2662, 2650, 0);
				// } else if (c.teleAction == 3) {
				// kbd
				// c.getPA().spellTeleport(3007, 3849, 0);
				// } else if (c.teleAction == 4) {
				// ruins
				// c.getPA().spellTeleport(3367+Misc.random(1),3929+Misc.random(1),0);
				// c.sendMessage("There is an obelisk to the southwest where you can escape");
				// } else if (c.teleAction == 5) {
				// c.getPA().spellTeleport(2474,3439,0);//agility
				// c.sendMessage("For quicker agility experience, try the wilderness agility course.");
				// } else if (c.teleAction == 6) {
				// //Corp Beast
				// c.getPA().spellTeleport(3218,2757,0);
				// }

			/*
			 * if (c.teleAction == 400) {//revnants pk, home object teleport c.getPA().spellTeleport(3280, 3886, 0);
			 * c.sendMessage("9191"); c.sendMessage("Try the Obelisk north of here for an easy escape."); } else if
			 * (c.teleAction == 401) {// jungle demon, home object teleport c.getPA().spellTeleport(2722, 2747, 0); }
			 * else if (c.teleAction == 402) {// tormented demons teleport, home // object teleport
			 * c.getPA().spellTeleport(3233, 9369, 0); } else if (c.teleAction == 403) {// agility demons teleport, home
			 * // object teleport c.getPA().spellTeleport(2480, 3437, 0); } else if (c.teleAction == 404) {// fight pits
			 * teleport, home object // teleport c.getPA().spellTeleport(2399, 5178, 0); }
			 */
				if (player.dialogueAction == 10) {
					player.getPA().spellTeleport(2796, 4818, 0);
					player.dialogueAction = -1;
				} else if (player.dialogueAction == 11) {
					player.getPA().spellTeleport(2527, 4833, 0);
					player.dialogueAction = -1;
				} else if (player.dialogueAction == 12) {
					player.getPA().spellTeleport(2464, 4834, 0);
					player.dialogueAction = -1;
				} else if (player.dialogueAction == 50) {
					player.getPA().buyColoredWhip("white");
					player.dialogueAction = -1;
					player.getPA().removeAllWindows();
				} else if (player.dialogueAction == 1565) {
					player.getDH().sendDialogues(1570, 3082);
					player.dialogueAction = 0;
				}
				break;
			// 3rd tele option
			case 9192:
				if (player.dialogueAction == 1565) {
					player.getItems().addItem(6864, 1);
					player.dialogueAction = 0;
					player.getPA().closeAllWindows();
					return;
				}
			/*
			 * if (c.teleAction == 1) { //slayer tower c.getPA().spellTeleport(3428, 3537, 0); } else if (c.teleAction
			 * == 2) { //tzhaar c.getPA().spellTeleport(2444, 5170, 0); } else if (c.teleAction == 3) { //dag kings
			 * c.getPA().spellTeleport(2479, 10147, 0); } else if (c.teleAction == 4) { //revs
			 * c.getPA().spellTeleport(3280, 3886, 0); c.sendMessage
			 * ("There is an obelisk north of here that will make an easy escape." ); } else if (c.teleAction == 5) {
			 * c.getPA().spellTeleport(2592,3415,0); } else if (c.teleAction == 6) { //k c.getPA().spellTeleport(3487,
			 * 9493, 0); }
			 */
				// if (c.teleAction == 400) {// mage bank, home teleport object
				// c.getPA().spellTeleport(2539, 4717, 0);
			/*
			 * if (c.teleAction == 401) {// kq teleport, home teleport // object c.getPA().spellTeleport(3505, 9494, 0);
			 * } else if (c.teleAction == 402) {// barrelchest teleport, home // object teleport
			 * c.getPA().spellTeleport(2367, 4956, 0); } else if (c.teleAction == 403) {// neitiznot island teleport,
			 * home object // teleport c.getPA().spellTeleport(2344, 3802, 0); } else if (c.teleAction == 404) {// clan
			 * wars? c.getPA().spellTeleport(3273, 3686, 0); }
			 */
			/*
			 * if (c.dialogueAction == 10) { c.getPA().spellTeleport(2713, 4836, 0); c.dialogueAction = -1; } else if
			 * (c.dialogueAction == 11) { c.getPA().spellTeleport(2162, 4833, 0); c.dialogueAction = -1; } else if
			 * (c.dialogueAction == 12) { c.getPA().spellTeleport(2207, 4836, 0); c.dialogueAction = -1; } else
			 * if(c.dialogueAction == 50) { c.getPA().buyColoredWhip("green"); c.getPA().removeAllWindows();
			 * c.dialogueAction = -1; }
			 */
				break;
			// 4th tele option
			case 9193:
			/*
			 * if (c.teleAction == 1) { //brimhaven dungeon c.getPA().spellTeleport(2710, 9466, 0); } else if
			 * (c.teleAction == 2) { //duel arena c.getPA().spellTeleport(3366, 3266, 0); } else if (c.teleAction ==
			 * 300) { //Varrock dungeon c.getPA().spellTeleport(3117, 9862, 0); } else if (c.teleAction == 3) { //chaos
			 * elemental c.getPA().spellTeleport(3295, 3921, 0); } else if (c.teleAction == 4) { //mb
			 * c.getPA().spellTeleport(2539, 4717, 0); } else if (c.teleAction == 5) { c.getPA().spellTeleport(2662,
			 * 3306,0); c.sendMessage("For magic logs, try north of the duel arena."); } else if (c.teleAction == 6) {
			 * c.getPA().spellTeleport(2367, 4956, 0); }
			 */
				// if (c.teleAction == 400) {// suggest teleport, home teleport object
				// c.getPA().spellTeleport(3333, 3333, 0);
			/*
			 * if (c.teleAction == 401) {// dagganoth teleport, home // teleport object c.getPA().spellTeleport(2442,
			 * 10146, 0); } else if (c.teleAction == 402) {// sea troll queen, home object // teleport
			 * c.getPA().spellTeleport(2526, 4570, 0); } else if (c.teleAction == 403) {// farming, home object teleport
			 * c.getPA().spellTeleport(2812, 3463, 0); } else if (c.teleAction == 404) {// weapons minigame, home object
			 * // teleport c.getPA().spellTeleport(1675, 5599, 0); }
			 */
				if (player.dialogueAction == 10) {
					player.getPA().spellTeleport(2660, 4839, 0);
					player.dialogueAction = -1;
				} else if (player.dialogueAction == 50) {
					player.getPA().buyColoredWhip("yellow");
					player.dialogueAction = -1;
					player.getPA().removeAllWindows();
				} else if (player.dialogueAction == 1565) {
					if (player.getItems().playerHasItem(6852)) {
						player.sendMessage("You may only have one of these boxes at a time!");
						player.dialogueAction = 0;
						player.getPA().closeAllWindows();
						return;
					}
					player.getItems().addItem(6852, 1);
					player.dialogueAction = 0;
					player.getPA().closeAllWindows();
					return;
				}
				break;
			// 5th tele option
			case 9194:
				// if (c.teleAction == 1) {
				// c.getDH().sendOption5("", "Druids", "", "Varrock Dungeon", "");
				// c.teleAction = 300;
				if (player.teleAction == 401) {
					player.getDH().sendOption5("KBD", "Tormented Demons", "BarrelChest", "Sea Troll Queen", "Corporeal Beast");
					player.teleAction = 402;

				/*
				 * } else if (c.teleAction == 1) { //last minigame spot c.sendMessage
				 * ("Suggest something for this spot on the forums!"); c.getPA().closeAllWindows(); } else if
				 * (c.teleAction == 2) { //last minigame spot
				 * c.sendMessage("Suggest something for this spot on the forums!" ); c.getPA().closeAllWindows(); } else
				 * if (c.teleAction == 3) { //last monster spot c.getDH().sendOption5("Tormented Demons",
				 * "Corporeal Beast", "Kalphite Queen", "Barrelchest @gre@NEW!", "@blu@Previous Page"); c.teleAction =
				 * 6; } else if (c.teleAction == 4) { //lvl 31 graves //c.getPA().spellTeleport(2561, 3311, 0);
				 * c.getPA().spellTeleport(3358+Misc.random(1)-Misc.random(1), 3680,0); } else if (c.teleAction == 5) {
				 * c.getPA().spellTeleport(2812,3463,0);
				 */
				/*
				 * } else if (c.teleAction == 402) {// corp teleport, home object // teleport
				 * c.getPA().spellTeleport(2957, 4382, 0); } else if (c.teleAction == 403) {// 5th skilling option, home
				 * object teleport c.getPA().spellTeleport(3224, 3222, 0); } else if (c.teleAction == 404) {// duel
				 * tournaments minigame, home // object teleport c.getPA().spellTeleport(3231, 5091, 0);
				 */
				}
				if (player.dialogueAction == 10 || player.dialogueAction == 11) {
					player.dialogueId++;
					player.getDH().sendDialogues(player.dialogueId, 0);
				} else if (player.dialogueAction == 12) {
					player.dialogueId = 17;
					player.getDH().sendDialogues(player.dialogueId, 0);
				} else if (player.dialogueAction == 50) {
					player.getPA().removeAllWindows();
				} else if (player.dialogueAction == 1565) {
					player.getItems().addItem(6853, 1);
					player.dialogueAction = 0;
					player.getPA().closeAllWindows();
					return;
				}
				break;
		/*
		 * case 62158://food set if (c.getPA().inMiniGame() || c.inWild()) { return; } c.getItems().addItem(7061, 1000);
		 * c.getItems().addItem(392, 1000); c.getItems().addItem(386, 1000); c.getItems().addItem(7947, 1000); break;
		 * case 62161://veng runes if (c.getPA().inMiniGame() || c.inWild()) { return; } c.getItems().addItem(560,
		 * 1000); c.getItems().addItem(557, 1000); c.getItems().addItem(9075, 1000); break; case 62162://melee set if
		 * (c.getPA().inMiniGame() || c.inWild()) { return; } c.getItems().addItem(10828, 1);
		 * c.getItems().addItem(10551, 1); c.getItems().addItem(4751, 1); c.getItems().addItem(6585, 1);
		 * c.getItems().addItem(4151, 1); c.getItems().addItem(5698, 1); c.getItems().addItem(11732, 1);
		 * c.getItems().addItem(7462, 1); c.getItems().addItem(1052, 1); c.getItems().addItem(8850, 1);
		 * c.getItems().addItem(6737, 1); break; case 62163://range set if (c.getPA().inMiniGame() || c.inWild()) {
		 * return; } c.getItems().addItem(4745, 1); c.getItems().addItem(6585, 1); c.getItems().addItem(2503, 1);
		 * c.getItems().addItem(2497, 1); c.getItems().addItem(11732, 1); c.getItems().addItem(868, 1000);
		 * c.getItems().addItem(10499, 1); c.getItems().addItem(9185, 1); c.getItems().addItem(9244, 1000);
		 * c.getItems().addItem(6524, 1); c.getItems().addItem(7462, 1); c.getItems().addItem(6733, 1); break; case
		 * 62164://mage set if (c.getPA().inMiniGame() || c.inWild()) { return; } c.getItems().addItem(4708, 1);
		 * c.getItems().addItem(4712, 1); c.getItems().addItem(4714, 1); c.getItems().addItem(4675, 1);
		 * c.getItems().addItem(6914, 1); c.getItems().addItem(6889, 1); c.getItems().addItem(2412, 1);
		 * c.getItems().addItem(2413, 1); c.getItems().addItem(2414, 1); c.getItems().addItem(6920, 1);
		 * c.getItems().addItem(6922, 1); c.getItems().addItem(6731, 1); break; case 62165://hybrid set if
		 * (c.getPA().inMiniGame() || c.inWild()) { return; } c.getItems().addItem(8850, 1); c.getItems().addItem(4712,
		 * 1); c.getItems().addItem(4714, 1); c.getItems().addItem(4675, 1); c.getItems().addItem(6889, 1);
		 * c.getItems().addItem(10551, 1); c.getItems().addItem(1079, 1); c.getItems().addItem(4151, 1);
		 * c.getItems().addItem(10828, 1); c.getItems().addItem(7462, 1); c.getItems().addItem(1052, 1);
		 * c.getItems().addItem(5698, 1); c.getItems().addItem(6585, 1); c.getItems().addItem(6920, 1);
		 * c.getItems().addItem(2550, 1); break;
		 */

			case 82020:

				if (!player.isBanking) {
					return;
				}

				if (player.getItems().freeInventorySlots() == 28) {
					player.sendMessage("You have no items in your backpack.");
				} else {
					for (int i = 0; i < player.inventory.length; i++) {
						player.getBank().depositItem(player.inventory[i] - 1, player.inventoryN[i], i);
					}
				}
				break;

			case 42252: // Close button, lottery interface
				player.getPA().closeAllWindows();
				break;
			case 42250:// enter lottery
				Server.lottery.enterLottery(player);
				// c.sendMessage("Your name has been entered into the lottery, good luck!");
				player.getPA().closeAllWindows();
				break;
			case 72175:
				player.getPA().showInterface(18500);
				break;

			case 9110: // smithing buy x
			case 15148:
			case 15152:
			case 15156:
			case 15160:
			case 16062:
			case 29018:
			case 29023:
				player.getPA().removeAllWindows();
				player.xInterfaceId = actionButtonId;
				player.getOutStream().createFrame(27);
				player.flushOutStream();
				break;
			case 34182: // short bow
				player.getPA().removeAllWindows();
				player.xInterfaceId = 34182;
				player.getOutStream().createFrame(27);
				player.flushOutStream();
				break;
			case 34186: // arrow shafts
				player.getPA().removeAllWindows();
				player.xInterfaceId = 34186;
				player.getOutStream().createFrame(27);
				player.flushOutStream();
				break;
			case 34190: // long bow
				player.getPA().removeAllWindows();
				player.xInterfaceId = 34190;
				player.getOutStream().createFrame(27);
				player.flushOutStream();
				break;

			case 34185:
			case 34184:
			case 34183:
			case 34189:
			case 34188:
			case 34187:
			case 34193:
			case 34192:
			case 34191:
				if (player.craftingLeather) {
					player.getCrafting().handleCraftingClick(actionButtonId);
				}
				break;

			case 15147: // smithing make 1
			case 15151:
			case 15155:
			case 15159:
			case 15163:
			case 29017:
			case 29022:
			case 29026:
				Smithing.startSmelting(player, actionButtonId, 1, false);
				break;
			case 15146: // smithing make 5
			case 15150:
			case 15154:
			case 15158:
			case 15162:
			case 29016:
			case 29020:
			case 29025:
				Smithing.startSmelting(player, actionButtonId, 5, false);
				break;
			case 10247: // smithing make 10
			case 15149:
			case 15153:
			case 15157:
			case 15161:
			case 24253:
			case 29019:
			case 29024:
				Smithing.startSmelting(player, actionButtonId, 10, false);
				break;
			case 58253:
				// c.getPA().showInterface(15106);
				player.getItems().writeBonus();
				break;
			case 59004:
				player.getPA().removeAllWindows();
				break;
		/* genie lamp xp givers */
			case 92155:// attack xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.lampVfy = false;
					player.getPA().addSkillXP(7000000, 0, false);
					player.getItems().deleteItem(2528, 1);
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Attack experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92159:// defence xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, 1, false);
					player.lampVfy = false;
					player.getItems().deleteItem(2528, 1);
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Defence experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			/** Clan chat **/
		/*
		 * case 71074 : if (c.clanId > -1) { if (Server.clanChat.clans[c.clanId]. owner.equalsIgnoreCase(c.username)) {
		 * Server.clanChat.sendClanMessage(c.clanId, "System", "Lootshare has been toggled to " +
		 * (!Server.clanChat.clans[c.clanId].lootshare ? "on" : "off") + " by the clan leader.", 2);
		 * Server.clanChat.clans[c.clanId].lootshare = !Server.clanChat.clans[c.clanId].lootshare; } else {
		 * c.getPA().sendClan("System", "Lootshare is currently " + (Server.clanChat.clans[c.clanId].lootshare ?
		 * "enabled." : "disabled."), Misc.formatPlayerName(Server.clanChat.clans[c.clanId].name), 2); } } break; case
		 * 70209 : if (c.clanId > -1) { System.out.println("valid clan id"); if (Server.clanChat.isClanOwner(c)) {
		 * DialogueHandler.sendDialogues(c, 30, -1); } else if (Server.clanChat.isClanRanked(c)) {
		 * DialogueHandler.sendDialogues(c, 31, -1); } else { c.sendMessage("You have left the clan.");
		 * Server.clanChat.leaveClan(c); } } else { System.out.println("invalid clan id"); for (int j = 0; j <
		 * Server.clanChat.clans.length; j++) { if (Server.clanChat.clans[j] != null) { if
		 * (c.username.equalsIgnoreCase(Server.clanChat.clans[j].owner)) { c.clanId = j; Server.clanChat.addToClan(c,
		 * j); return; } } } if (c.getOutStream() != null) { c.yInterfaceId = 1; c.getOutStream().createFrame(187);
		 * c.flushOutStream(); } } break; case 70212 : if (c.clanId > -1) { if (Server.clanChat.isClanOwner(c)) {
		 * DialogueHandler.sendDialogues(c, 32,-1); } else if (Server.clanChat.isClanRanked(c)) {
		 * c.sendMessage("You have left the clan."); Server.clanChat.leaveClan(c); } } else { c.yInterfaceId = 7;
		 * c.getOutStream().createFrame(187); c.flushOutStream(); } break;
		 */
			case 92157:// strength xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, 2, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Strength experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92161:// hitpoints xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, 3, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Constitution experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92163:// range xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, 4, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Range experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92165:// prayer xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, 5, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Prayer experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92167:// magic xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, 6, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Magic experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92187:// cooking xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, 7, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Cooking experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92191:// woodcutting xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, 8, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Woodcutting experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92177:// fletching xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, 9, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Fletching experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92185:// fishing xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, 10, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Fishing experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92189:// fletching xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, 11, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Firemaking experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92175:// fletching xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, 12, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Crafting experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92183:// fletching xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, 13, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Smithing experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92181:// fletching xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, 14, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Mining experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92171:// fletching xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, 15, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Herblore experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92169:// fletching xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, 16, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Agility experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92173:// fletching xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, 17, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Thieving experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92179:// fletching xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, 18, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Slayer experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92193:// fletching xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, 19, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Farming experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92195:// fletching xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, 20, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Runecrafting experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92197:// hunter xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, PlayerConstants.HUNTER, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Hunter experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			case 92199:// summoning xp genie
				if (System.currentTimeMillis() - player.lastXpgiven >= 9000 && player.lampVfy) {
					player.getPA().addSkillXP(7000000, PlayerConstants.SUMMONING, false);
					player.getItems().deleteItem(2528, 1);
					player.lampVfy = false;
					player.getPA().removeAllWindows();
					player.sendMessage("You are given 7,000,000 Summoning experience!");
					player.lastXpgiven = System.currentTimeMillis();
				}
				break;
			// case 92201://dung xp
			// c.getPA().addSkillXP(500000, 12);
			// break;
			case 19212:// friend and ignores
				player.setSidebarInterface(8, 5065);
				break;
			case 19215:
				player.setSidebarInterface(8, 5715);
				break;
			case 9178: // sendOption4 first option
				switch (player.dialogueAction) {
					case 30:
						if (player.getOutStream() != null) {
							player.yInterfaceId = 2;
							player.getOutStream().createFrame(187);
							player.flushOutStream();
						}
						break;
					case 32:
						if (player.getOutStream() != null) {
							player.yInterfaceId = 6;
							player.getOutStream().createFrame(187);
							player.flushOutStream();
						}
						break;
				}
				if (player.dialogueAction == 500) {
					player.getPA().assignTeams("p2p");
					player.choseMage = true;
				}
				if (player.switchAction == 1 && player.teleAction == 50) {

					Achievements.progressMade(player, Achievements.Types.SWITCH_MAGIC_BOOKS);

					player.startAnimation(6299);
					player.gfx0(1062);
					player.setSidebarInterface(6, 25555); // Lunar
					player.getPA().closeAllWindows();
					player.getItems().sendWeapon(player.equipment[player.playerWeapon],
							player.getItems().getItemName(player.equipment[player.playerWeapon]));
					player.getPA().resetAutocast();
					player.playerMagicBook = 2;
				}
				if (player.shopAction == 1) {
					Shops.open(player, 19);
				}
			/*
			 * if (c.teleAction == 65) c.getPA().spellTeleport(3565+Misc.random(2), 3314+Misc.random(2), 0);
			 * if(c.shopAction == 2) c.getShops().openShop(21); if(c.teleAction == 7 && c.isMember >= 3)
			 * c.getPA().spellTeleport(2796 +Misc.random(2),3791+Misc.random(2),0); if(c.teleAction == 4) { c.
			 * getPA().spellTeleport(3087+Misc.random(2),3558+Misc.random(2),0); }
			 */
				// if (c.teleAction == 400) {// edgeville tele, home object teleport
				// c.getPA().spellTeleport(3093, 3493, 0);
				// }
			/*
			 * if (c.teleAction == 401) {//bork teleport, home object teleport c.getPA().spellTeleport(3167, 2983, 0);
			 * c.sendMessage("Moving Bork."); } if (c.teleAction == 402) {//kbd teleport, home object teleport
			 * c.getPA().spellTeleport(3007, 3849, 0); }
			 */
				// if (c.teleAction == 400) {// edgeville tele, home object teleport
				// c.getPA().spellTeleport(3093, 3493, 0);
				// }
				if (player.usingGlory) {
					player.getPA().startTeleport(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0, "modern");
				}
				if (player.dialogueAction == 2) {
					player.getPA().startTeleport(3428, 3538, 0, "modern");
				}
				if (player.dialogueAction == 3) {
					player.getPA().startTeleport(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0, "modern");
				}
				if (player.dialogueAction == 4) {
					player.getPA().startTeleport(3565, 3314, 0, "modern");
				}
				if (player.dialogueAction == 20) {
					player.getPA().startTeleport(2897, 3618, 4, "modern");
					player.killCount = 0;
				}
				if (player.dialogueAction == 1594) {
					player.getDH().sendDialogues(1595, 9085);
					return;
				}
				if (player.pestAction == 1) {
					player.getPA().buyPestExp("attack");
				} else if (player.pestAction == 2) {
					player.getPA().buyPestExp("range");
				}
			/*
			 * if (c.dialogueAction == 131) { c.getDH().sendOption4("Edgeville", "Removed", "Mage Bank",
			 * "Revenants @red@+49 Multi)"); c.teleAction = 400; }
			 */
				break;
			case 9179: // sendOption4 2nd option
				switch (player.dialogueAction) {
					case 30:
						if (player.getOutStream() != null) {
							player.yInterfaceId = 3;
							player.getOutStream().createFrame(187);
							player.flushOutStream();
						}
						return;
				/*
				 * case 32 : c.getPA().removeAllWindows(); Server.clanChat.toggleChat(c); return;
				 */
				}
				if (player.dialogueAction == 500) {
					player.getPA().assignTeams("p2p");
					player.choseRange = true;
				}
				if (player.switchAction == 1 && player.teleAction == 50) {

					Achievements.progressMade(player, Achievements.Types.SWITCH_MAGIC_BOOKS);

					player.startAnimation(6299);
					player.gfx0(1062);
					player.setSidebarInterface(6, 12855); // ancient
					player.getItems().sendWeapon(player.equipment[player.playerWeapon],
							player.getItems().getItemName(player.equipment[player.playerWeapon]));
					player.getPA().closeAllWindows();
					player.getPA().resetAutocast();
					player.playerMagicBook = 1;
				}
				// if (c.teleAction == 65)
				// c.getPA().spellTeleport(2658+Misc.random(2), 2659+Misc.random(2),
				// 0);
				if (player.shopAction == 1) {
					Shops.open(player, 20);
				}
				if (player.shopAction == 2) {
					Shops.open(player, 20);
				}
				// if(c.teleAction == 7 && c.isMember >= 2)
				// c.getPA().spellTeleport(3103+Misc.random(2),3096+Misc.random(2),0);
				// if(c.teleAction == 4)
				// c.getPA().spellTeleport(3293+Misc.random(1),3176+Misc.random(1),0);
			/*
			 * if (c.teleAction == 400) {//revnants pk, home object teleport c.getPA().spellTeleport(3280, 3886, 0);
			 * c.sendMessage("Try the Obelisk north of here for an easy escape." ); }
			 */
			/*
			 * if (c.teleAction == 401) {//jungle demon, home object teleport c.getPA().spellTeleport(2722, 2747, 0); }
			 * if (c.teleAction == 402) {//tormented demons teleport, home object teleport c.getPA().spellTeleport(3233,
			 * 9369, 0); }
			 */
				if (player.usingGlory) {
					player.getPA().startTeleport(Config.AL_KHARID_X, Config.AL_KHARID_Y, 0, "modern");
				}
				if (player.dialogueAction == 2) {
					player.getPA().startTeleport(2884, 3395, 0, "modern");
				}
				if (player.dialogueAction == 3) {
					player.getPA().startTeleport(3243, 3513, 0, "modern");
				}
				if (player.dialogueAction == 4) {
					player.getPA().startTeleport(2444, 5170, 0, "modern");
				}
				if (player.dialogueAction == 20) {
					player.getPA().startTeleport(2897, 3618, 12, "modern");
					player.killCount = 0;
				}
				if (player.pestAction == 1) {
					player.getPA().buyPestExp("strength");
				} else if (player.pestAction == 2) {
					player.getPA().buyPestExp("mage");
				}
				if (player.dialogueAction == 131) {
					player.getDH().sendOption5("Bork", "Jungle Demon", "Kalphite Queen", "Daggonth Kings", "@blu@Next Page");
					player.teleAction = 401;
				}
				break;
			case 9180: // sendOption4 3rd option
				if (player.dialogueAction == 500) {
					player.getPA().assignTeams("p2p");
					player.choseMelee = true;
				}
				switch (player.dialogueAction) {
					case 30:
						if (player.getOutStream() != null) {
							player.yInterfaceId = 4;
							player.getOutStream().createFrame(187);
							player.flushOutStream();
						}
						return;
				/*
				 * case 32 : c.getPA().removeAllWindows(); Server.clanChat.toggleClan(c); return;
				 */
				}
				if (player.switchAction == 1 && player.teleAction == 50) {

					Achievements.progressMade(player, Achievements.Types.SWITCH_MAGIC_BOOKS);

					player.startAnimation(6299);
					player.gfx0(1062);
					player.setSidebarInterface(6, 1151); // modern
					player.getItems().sendWeapon(player.equipment[player.playerWeapon],
							player.getItems().getItemName(player.equipment[player.playerWeapon]));
					player.getPA().closeAllWindows();
					player.getPA().resetAutocast();
					player.autocastId = 0;
					player.getPA().sendFrame36(108, 0);
					player.playerMagicBook = 0;
				}
				if (player.shopAction == 2) {
					Shops.open(player, 19);
				}
				// if(c.teleAction == 7 && c.isMember >= 1)
				// c.getPA().spellTeleport(3504+Misc.random(2),3575+Misc.random(2),0);
				// if(c.teleAction == 65)
				// c.getPA().spellTeleport(2399, 5178,0);
				// c.getPA().closeAllWindows();
				// if(c.teleAction == 4)
				// c.getPA().spellTeleport(3352+Misc.random(2),3678+Misc.random(2),0);
				// if (c.teleAction == 400) {// mage bank, home teleport object
				// c.getPA().spellTeleport(2539, 4717, 0);
				// }
			/*
			 * if (c.teleAction == 401) {//kq teleport, home teleport object c.getPA().spellTeleport(3505, 9494, 0); }
			 * if (c.teleAction == 402) {//barrelchest teleport, home object teleport c.getPA().spellTeleport(2367,
			 * 4956, 0); }
			 */
				if (player.usingGlory) {
					player.getPA().startTeleport(3093, 3244, 0, "modern");
				}
				if (player.dialogueAction == 2) {
					player.getPA().startTeleport(2471, 10137, 0, "modern");
				}
				if (player.dialogueAction == 3) {
					player.getPA().startTeleport(3363, 3676, 0, "modern");
				}
				if (player.dialogueAction == 4) {
					player.getPA().startTeleport(2659, 2676, 0, "modern");
				}
				if (player.dialogueAction == 20) {
					player.getPA().startTeleport(2897, 3618, 8, "modern");
					player.killCount = 0;
				}
				if (player.pestAction == 1) {
					player.getPA().buyPestExp("Defence");
				} else if (player.pestAction == 2) {
					player.getPA().buyPestExp("prayer");
				}
				if (player.dialogueAction == 131) {
					player.getDH().sendOption5("Fishing", "Agility", "Neitiznot Island", "Farming", "Theiving");
					player.teleAction = 403;
				}
				break;
			case 9181: // sendOption4 4th option
				if (player.dialogueAction == 500) {
					player.getPA().removeAllWindows();
					player.dialogueAction = -1;
				}
				switch (player.dialogueAction) {
					case 30:
						if (player.getOutStream() != null) {
							player.yInterfaceId = 5;
							player.getOutStream().createFrame(187);
							player.flushOutStream();
						}
						return;
				/*
				 * case 32 : c.getPA().removeAllWindows(); Server.clanChat.leaveClan(c); return;
				 */
				}
				if (player.usingGlory) {
					player.getPA().startTeleport(2924, 3171, 0, "modern");
				}
				if (player.dialogueAction == 2) {
					player.getPA().startTeleport(2669, 3714, 0, "modern");
				}
				if (player.dialogueAction == 3) {
					player.getPA().startTeleport(2540, 4716, 0, "modern");
				}
				if (player.dialogueAction == 4) {
					player.getPA().startTeleport(3366, 3266, 0, "modern");
					player.sendMessage("Dueling is at your own risk. Refunds will not be given for items lost due to glitches.");
				} else if (player.teleAction == 4) {
					player.getDH().sendOption2("Continue into @red@Mage Bank", "Turn back");
					player.dialogueAction = 22;
				}

			/*
			 * if (c.teleAction == 401) {//dagganoth teleport, home teleport object c.getPA().spellTeleport(2442, 10146,
			 * 0); } if (c.teleAction == 402) {//sea troll queen, home object teleport c.getPA().spellTeleport(2526,
			 * 4570, 0); }
			 */
				if (player.dialogueAction == 20) {
					// c.getPA().startTeleport(3366, 3266, 0, "modern");
					// c.killCount = 0;
					player.sendMessage("This will be added shortly");
				}
				if (player.pestAction == 1) {
					player.getDH().sendOption4("Range (10 pts)", "Magic (10 pts)", "Prayer (10 pts)", "Hitpoints (10 pts)");
					player.pestAction = 2;
				} else if (player.pestAction == 2) {
					player.getPA().buyPestExp("hitpoints");
				}
				if (player.teleAction == 65) {
					// if(c.specAmount >= 10) {
				/*
				 * switch(Misc.random(3)+1) { case 1: c.getPA().spellTeleport(2394, 3106, 0); break; case 2:
				 * c.getPA().spellTeleport(2404, 3101, 0); break; case 3: c.getPA().spellTeleport(2403, 3107, 0);
				 * //cwars break; case 4: c.getPA().spellTeleport(2396+Misc.random(1), 3100, 0); break; }
				 */
					// c.getPA().spellTeleport(3367+Misc.random(6),3268+Misc.random(4),0);
					// //duel arena
					// c.getPA().spellTeleport(2644+Misc.random(4),
					// 9902+Misc.random(4),0); //new room funpk
					player.getPA().startTeleport(2484, 3046, 0, "modern");
					player.sendMessage("Remember, this minigame is new and might have a few bugs.");
					player.sendMessage("If you find any bugs please report them on the forums, ::forums.");
					// } else {
					// c.sendMessage("Get your special attack full!");
					// }
				}
				if (player.dialogueAction == 131) {
					player.getDH().sendOption5("Zombies Minigame", "Fight Pits", "Clan Wars", "Weapons Minigame",
							"Duel Tournaments");
					player.teleAction = 404;
				}
				break;
			case 1093:
			case 1094:
			case 1097:
				if (player.autocastId > 0) {
					player.getPA().resetAutocast();
				} else {
					if (player.playerMagicBook == 1) {
						if (player.equipment[player.playerWeapon] == 4675 || player.equipment[player.playerWeapon] == 15001
								|| player.equipment[player.playerWeapon] == 15040) {
							player.setSidebarInterface(0, 1689);
						} else {
							player.sendMessage("You can't autocast ancients without an ancient staff, staff of light, or chaotic staff.");
						}
					} else if (player.playerMagicBook == 0) {
						if (player.equipment[player.playerWeapon] == 4170) {
							player.setSidebarInterface(0, 12050);
						} else {
							player.setSidebarInterface(0, 1829);
						}
					}
				}
				break;
			// case 62155:
			// c.getPA().showInterface(26099);
		/*
		 * c.getPA().sendFrame200(26101, 9847);//chatid c.getPA().sendFrame185(26101); if (c.kills > c.deaths) {
		 * c.getPA().sendFrame126("@or1@Kills: @gre@"+c.kills+"", 26105);
		 * c.getPA().sendFrame126("@or1@Deaths: @red@"+c.deaths+"", 26106); } if (c.kills < c.deaths) {
		 * c.getPA().sendFrame126("@or1@Kills: @red@"+c.kills+"", 26105);
		 * c.getPA().sendFrame126("@or1@Deaths: @gre@"+c.deaths+"", 26106); }
		 * c.getPA().sendFrame126("@or1@Name: @gre@"+c.username+"", 26107);
		 * c.getPA().sendFrame126("@or1@Combat Level: @gre@"+c.combatLevel +"", 26108); if (c.rights == 1) {
		 * c.getPA().sendFrame126("@or1@Rank: @gre@Moderator", 26109); } if (c.rights == 2) {
		 * c.getPA().sendFrame126("@or1@Rank: @gre@Admin", 26109); } if (c.rights == 3) {
		 * c.getPA().sendFrame126("@or1@Rank: @gre@Owner", 26109); } if (c.rights == 0) {
		 * c.getPA().sendFrame126("@or1@Rank: @gre@Player", 26109); } if (c.isMember == 1 && c.rights == 0) {
		 * c.getPA().sendFrame126("@or1@Rank: @gre@Donator", 26109); }
		 * c.getPA().sendFrame126("@or1@Source Points: @gre@0", 26111); c.getPA
		 * ().sendFrame126("@or1@Activity Points: @gre@"+c.pcPoints+"", 26112);
		 * c.getPA().sendFrame126("@or1@PK Points: @gre@0", 26113); c.getPA().sendFrame126("@or1@Boss Points: @gre@0",
		 * 26115); c.getPA().sendFrame126("@or1@Pest Points: @gre@0", 26116);
		 * c.getPA().sendFrame126("@or1@Assault Points: @gre@0", 26117);
		 * 
		 * c.getPA().sendFrame126("@or1@Gambles Won: @gre@0", 26118);
		 * c.getPA().sendFrame126("@or1@Gambles Lost: @gre@0", 26119);
		 * c.getPA().sendFrame126("@or1@Battles Won: @gre@0", 26120);
		 * c.getPA().sendFrame126("@or1@Battles Lost: @gre@0", 26121); c.getPA().sendFrame126("@or1@NPC Kills: @gre@0",
		 * 26122); c.updateRequired = true; c.appearanceUpdateRequired = true;
		 */
			// break;
			case 82016:
				player.takeAsNote = !player.takeAsNote;
				break;
			case 9157:// barrows tele to tunnels
				if (player.usingRingOfWealth) {
					player.usingRingOfWealth = false;
					player.getPA().startTeleport(3367, 3267, 0, "modern");
					return;
				}
				switch (player.dialogueAction) {
					case 3501:
						player.getPA().removeAllWindows();
						player.getPA().movePlayer(3104, 3934, player.playerId * 4);
						if (player.mageState == 0) {
							NPCHandler.spawnNpc(player, 907, 3105, 3934, player.playerId * 4, 1, 15, 17, 70, 60, true, false);
						} else if (player.mageState == 1) {
							NPCHandler.spawnNpc(player, 908, 3105, 3934, player.playerId * 4, 1, 70, 17, 150, 60, true, false);
						} else if (player.mageState == 2) {
							NPCHandler.spawnNpc(player, 67, 3105, 3934, player.playerId * 4, 1, 125, 17, 200, 60, true, false);
						} else if (player.mageState == 3) {
							NPCHandler.spawnNpc(player, 1549, 3105, 3934, player.playerId * 4, 1, 250, 17, 250, 60, true, false);
						} else if (player.mageState == 4) {
							NPCHandler.spawnNpc(player, 911, 3105, 3934, player.playerId * 4, 1, 350, 17, 300, 60, true, false);
						}
						break;
					case 5004:
						player.dialogueAction = -1;
						player.getDH().sendDialogues(5004, -1);
						break;
					case 5003:
						PvPArtifacts.handleMandrith(player);
						return;
				}
				if (player.dialogueAction == 5020) {
					Shops.open(player, 50);
					player.sendMessage("@red@You have " + player.getData().pkPoints + " Pk Points.");
					return;
				}
				if (player.dialogueAction == 5022) {
					player.getDH().sendDialogues(5023, 6139);
					return;
				}
				if (player.dialogueAction == 200) {
					player.getPA().sendFrame126("Current fund: " + Lottery.lotteryFund, 11003);
					player.getPA().sendFrame126("Recent winner: " + Lottery.lastWinner, 11005);
					player.getPA().showInterface(11000);
					return;
				}
				if (player.teleAction == 5001) // search meh home
				{
					player.getPA().spellTeleport(3211, 3414, 0);
				}
				if (player.dialogueAction == 201) { // sea troll
					player.getPA().spellTeleport(2528, 4585, 0);
					player.teleAction = 0;
				}
				if (player.dialogueAction == 1) {
					int r = 4;
					// int r = Misc.random(3);
					switch (r) {
						case 0:
							player.getPA().movePlayer(3534, 9677, 0);
							break;
						case 1:
							player.getPA().movePlayer(3534, 9712, 0);
							break;
						case 2:
							player.getPA().movePlayer(3568, 9712, 0);
							break;
						case 3:
							player.getPA().movePlayer(3568, 9677, 0);
							break;
						case 4:
							player.getPA().movePlayer(3551, 9694, 0);
							break;
					}
				} else if (player.dialogueAction == 2) {
					player.getPA().movePlayer(2507, 4717, 0);
				} else if (player.dialogueAction == 201) {
					player.getPA().spellTeleport(2528, 4585, 0);
				} else if (player.sendOptionChoice == 2) {
					player.getItems().addItem(995, 25000);
					player.sendOptionChoice = 0;
					player.getPA().closeInterface();
				} else if (player.teleAction == 8) {
					player.getPA().spellTeleport(3211 + Misc.random(2), 3423 + Misc.random(2), 0);
				} else if (player.sendOptionChoice == 1) {
					player.actionTimer = 5;
					player.getPA().closeInterface();
					int randomCage = Misc.random(3);
					if (randomCage == 1) {
						player.getPA().movePlayer(player.cageOneX, player.cageOneY, 0);
						PlayerHandler.playersInFightArena++;
					} else if (randomCage == 2) {
						player.getPA().movePlayer(player.cageTwoX, player.cageTwoY, 0);
						PlayerHandler.playersInFightArena++;
					} else if (randomCage == 3) {
						player.getPA().movePlayer(player.cageThreeX, player.cageThreeY, 0);
						PlayerHandler.playersInFightArena++;
					}
				} else if (player.dialogueAction == 7) {
					player.getPA().startTeleport(3088, 3933, 0, "modern");
					player.sendMessage("NOTE: You are now in the wilderness...");
				} else if (player.dialogueAction == 8) {
					player.getPA().resetBarrows();
					player.sendMessage("Your barrows have been reset.");
				} else if (player.dialogueAction == 21) {
					player.getPA().buyYellPoints();
				} else if (player.dialogueAction == 22) {
					player.getPA().spellTeleport(2538, 4716, 0);
				} else if (player.dialogueAction == 41) {
					Server.yes++;
					player.voted = 1;
					player.sendMessage("Thank you for voting!");
					player.sendMessage("Poll currently: " + Server.yes + " voted yes and " + Server.no + " voted no");
					player.getPA().startTeleport(3012 + Misc.random(2), 3363 + Misc.random(2), 0, "dungeon");
				} else if (player.dialogueAction == 1000) {
					player.getPA().changeExt();
				} else if (player.dialogueAction == 5432) {
					player.sendMessage("You are Keeping the same bank pin.");
				}/*
			 * else if(c.dialogueAction == 301) { c.getPA().removeAllWindows(); Server.clanChat.addToClan(c,
			 * c.inviteClan); }
			 */ else if (player.dialogueAction == 62) {
					if (player.getPA().riskPkReq()) {
						player.getPA().spellTeleport(1761 + Misc.random(3), 5193 + Misc.random(3), 0);
						player.sendMessage("You can escape from the rope south of you!");
					} else {
						player.sendMessage("You need to have 5m of wealth on you and wearing 6 items to go into Risk PK!");
					}
				}
				if (player.dialogueAction == 500) {
					player.getPA().assignTeams("p2p");
				}
				if (player.clanAction == 1) {
					switch (player.clanTele) {
						case 1:
							player.getPA().spellTeleport(2860 + Misc.random(1) - Misc.random(1),
									9644 + Misc.random(1) - Misc.random(1), 0);
							for (int j = 0; j < PlayerHandler.players.length; j++) {
								if (PlayerHandler.players[j] != null) {
									Player c2 = PlayerHandler.players[j];
									c2.clanTele = 2;
								}
							}
							break;
						case 2:
							player.getPA().spellTeleport(2849 + Misc.random(1) - Misc.random(1),
									9644 + Misc.random(1) - Misc.random(1), 0);
							for (int j = 0; j < PlayerHandler.players.length; j++) {
								if (PlayerHandler.players[j] != null) {
									Player c2 = PlayerHandler.players[j];
									c2.clanTele = 3;
								}
							}
							break;
						case 3:
							player.getPA().spellTeleport(2863 - Misc.random(2), 9628 + Misc.random(2), 0);
							for (int j = 0; j < PlayerHandler.players.length; j++) {
								if (PlayerHandler.players[j] != null) {
									Player c2 = PlayerHandler.players[j];
									c2.clanTele = 4;
								}
							}
							break;
						case 4:
							player.getPA().spellTeleport(2848 + Misc.random(1) - Misc.random(1),
									9629 + Misc.random(1) - Misc.random(1), 0);
							for (int j = 0; j < PlayerHandler.players.length; j++) {
								if (PlayerHandler.players[j] != null) {
									Player c2 = PlayerHandler.players[j];
									c2.clanTele = 5;
								}
							}
							break;
						case 5:
							player.getPA().spellTeleport(2848 + Misc.random(1) - Misc.random(1),
									9629 + Misc.random(1) - Misc.random(1), 0);
							for (int j = 0; j < PlayerHandler.players.length; j++) {
								if (PlayerHandler.players[j] != null) {
									Player c2 = PlayerHandler.players[j];
									c2.clanTele = 1;
								}
							}
							break;
					}
					player.clanAction = -1;
				}
				player.dialogueAction = 0;
				player.getPA().removeAllWindows();
				break;
			case 9158:
				if (player.usingRingOfWealth) {
					player.usingRingOfWealth = false;
					player.getPA().startTeleport(2441, 3090, 0, "modern");
					return;
				}
				if (player.dialogueAction == 5022) {
					player.getDH().sendDialogues(5024, 6139);
					return;
				}
				if (player.dialogueAction == 5004) {
					player.dialogueAction = -1;
					player.getPA().removeAllWindows();
					player.sendMessage("You chose to skip the tutorial, it is highly recommended you do it!");
					// c.getPA().showInterface(3559);
					// c.canChangeAppearance = true;
					return;
				}
				if (player.dialogueAction == 5020) {
					Shops.open(player, 96);
					player.sendMessage("@red@You have " + player.getData().pkPoints + " Pk Points.");
					return;
				}
				if (player.dialogueAction == 41) {
					Server.no++;
					player.voted = 1;
					player.sendMessage("Thanks for voting!");
					player.sendMessage("Poll currently: " + Server.yes + " voted yes and " + Server.no + " voted no");
					player.getPA().startTeleport(3012 + Misc.random(2), 3363 + Misc.random(2), 0, "dungeon");
				}
				if (player.teleAction == 5001) // market
				// c.getPA().spellTeleport(3211+Misc.random(2),
				// 3414+Misc.random(2), 0); old one dont
				// c.getPA().spellTeleport(2999+Misc.random(2),
				// 3379+Misc.random(2), 0);
				{
					if (player.clanAction == 1) {
						player.clanAction = -1;
						player.getPA().removeAllWindows();
					}
				}
				if (player.dialogueAction == 8) {
					player.getPA().fixAllBarrows();
				} else if (player.teleAction == 8) {
					player.getPA().spellTeleport(3183 + Misc.random(2), 3429 + Misc.random(2), 0);
				} else if (player.teleAction == 65) {
					player.getPA().spellTeleport(2591 + Misc.random(2), 3872 + Misc.random(2), 0);
				} else if (player.sendOptionChoice == 2) {
					player.getItems().addItem(6199, 1);
					player.sendOptionChoice = 0;
					player.getPA().closeInterface();
				} else if (player.dialogueAction == 21) {
					player.getPA().openDonatorShop();
				} else {
					player.dialogueAction = 0;
					player.getPA().removeAllWindows();
				}
				break;
		/*
		 * case 62156: if (!c.isSkulled) { c.getItems().resetKeepItems(); c.getItems().keepItem(0, false);
		 * c.getItems().keepItem(1, false); c.getItems().keepItem(2, false); c.getItems().keepItem(3, false);
		 * c.sendMessage("You can keep three items and a fourth if you use the protect item prayer."); } else {
		 * c.getItems().resetKeepItems(); c.getItems().keepItem(0, false);
		 * c.sendMessage("You are skulled and will only keep one item if you use the protect item prayer."); }
		 * c.getItems().sendItemsKept(); c.getPA().showInterface(6960); c.getItems().resetKeepItems(); break;
		 */
			case 29213:
				player.specBarId = 7661;
				player.usingSpecial = !player.usingSpecial;
				player.getItems().updateSpecialBar();
				break;
			/** Specials **/
			case 30007:
				player.specBarId = 7711;
				player.usingSpecial = !player.usingSpecial;
				if (player.equipment[player.playerWeapon] == 15486) {
					player.getCombat().activateSolSpecial();
				}
				player.getItems().updateSpecialBar();
				break;
			case 29188:
				player.specBarId = 7636; // the special attack text - sendframe126(S P E
				// C I A L A T T A C K, c.specBarId);
				player.usingSpecial = !player.usingSpecial;
				player.getItems().updateSpecialBar();
				break;
			case 29163:
				player.specBarId = 7611;
				player.usingSpecial = !player.usingSpecial;
				player.getItems().updateSpecialBar();
				break;
			case 33033:
				player.specBarId = 8505;
				player.usingSpecial = !player.usingSpecial;
				player.getItems().updateSpecialBar();
				break;
			case 29038:
				if (player.equipment[player.playerWeapon] == 4153) {
					player.getCombat().handleGmaulSpec();
					player.getItems().updateSpecialBar();
				} else {
					player.usingSpecial = !player.usingSpecial;
					player.specBarId = 7486;
					player.getItems().updateSpecialBar();
				}
				break;
			case 29063:
				if (player.getCombat().checkSpecAmount(player.equipment[player.playerWeapon])) {
					player.gfx0(246);
					player.forcedChat("Raarrrrrgggggghhhhhhh!");
					player.startAnimation(1056);
					player.level[2] = player.getPA().getLevelForXP(player.xp[2]) + (player.getPA().getLevelForXP(player.xp[2]) * 15 / 100);
					player.getPA().refreshSkill(2);
					player.getItems().updateSpecialBar();
				} else {
					player.sendMessage("You don't have the required special energy to use this attack.");
				}
				break;
			case 48023:
				player.specBarId = 12335;
				player.usingSpecial = !player.usingSpecial;
				player.getItems().updateSpecialBar();
				break;
			case 29113:
				player.specBarId = 7561;
				player.usingSpecial = !player.usingSpecial;
				player.getItems().updateSpecialBar();
				break;
			case 30108:
				player.specBarId = 7812;
				player.usingSpecial = !player.usingSpecial;
				player.getItems().updateSpecialBar();
				break;
			case 29138:
				if (player.equipment[player.playerWeapon] == 15001) {
					if (player.getCombat().checkSpecAmount(player.equipment[player.playerWeapon])) {
						player.gfx0(1958);
						player.SolProtect = 120;
						player.startAnimation(10518);
						player.getItems().updateSpecialBar();
						player.usingSpecial = !player.usingSpecial;
						player.sendMessage("All damage will be split into half for 1 minute.");
						player.getPA().sendFrame126("@bla@S P E C I A L  A T T A C K", 7562);
					} else {
						player.sendMessage("You don't have the required special energy to use this attack.");
					}
				}
				player.specBarId = 7586;
				player.usingSpecial = !player.usingSpecial;
				player.getItems().updateSpecialBar();
				break;
			case 29238:
				player.specBarId = 7686;
				player.usingSpecial = !player.usingSpecial;
				player.getItems().updateSpecialBar();
				break;
			case 4169: // god spell charge
				player.usingMagic = true;
				if (!player.getCombat().checkMagicReqs(48)) {
					break;
				}
				if (player.equipment[player.playerCape] != MagicArena.GUTHIX - 3 && player.equipment[player.playerCape] != MagicArena.ZAMORAK - 3 && player.equipment[player.playerCape] != MagicArena.SARADOMIN - 3) {
					player.sendMessage("You must be weilding a god cape in order to cast this.");
					break;
				}
				if (System.currentTimeMillis() - player.godSpellDelay < Config.GOD_SPELL_CHARGE) {
					player.sendMessage("You still feel the charge in your body!");
					break;
				}
				player.godSpellDelay = System.currentTimeMillis();
				player.sendMessage("You feel charged with a magical power!");
				player.gfx100(player.MAGIC_SPELLS[48][3]);
				player.startAnimation(player.MAGIC_SPELLS[48][2]);
				player.usingMagic = false;
				break;
			case 28164: // item kept on death
				break;
			case 152:
			case 153:
				player.getWalkingQueue().setRunningToggled(!player.getWalkingQueue().isRunningToggled());
				break;
			case 154:
				Resting.start(player);
				break;
			case 32192:// toadflex
			case 32190:
			case 32202:// snapdragon
			case 32201:
			case 32193:// piratehook
			case 32189:
				player.sendMessage("Not Available!");
				break;
			case 9154:
				player.logout();
				break;
			case 21010:
				player.takeAsNote = true;
				break;
			case 21011:
				player.takeAsNote = false;
				break;
			// home teleports
			case 4171:
			case 50056:
			case 117048:
			case 211224:
				String type = player.playerMagicBook == 0 ? "modern" : "ancient";
				player.getPA().startTeleport(Config.RESPAWN_X + Misc.random(2), Config.RESPAWN_Y + Misc.random(2), 0, type);
				// new 3277 3164
				break;
			// case 117048:
			// c.getPA().startTeleport(3277+Misc.random(2), 3166+Misc.random(2), 0);
			// c.getDH().sendOption2("Home", "Market Area");
			// c.teleAction = 5001;
			// break;
			case 50235:
			case 4140:
			case 117162:
				player.getPA().spellTeleport(3213, 3428, 0);
				player.sendMessage("You teleport to Varrock.");
			/*
			 * c.getDH().sendOption5("Rock Crabs", "Taverly Dungeon", "Slayer Tower", "Brimhaven Dungeon",
			 * "@blu@Next Page"); c.teleAction = 1;
			 */
			/*
			 * int frame = 8147; c.getPA().sendFrame126("@red@How to train!", 8144); //Title
			 * c.getPA().sendFrame126("Training is very easy, and is instant.", frame++);
			 * c.getPA().sendFrame126("To train, simply click on the level tab", frame++);
			 * c.getPA().sendFrame126("Then click on the level you wish to advance", frame++);
			 * c.getPA().sendFrame126("You can choose att, str, def, pray, mage, range, hp", frame++);
			 * c.getPA().sendFrame126("Then type a number 1 - 99", frame++);
			 * c.getPA().sendFrame126("And then your stats will be edited!", frame++);
			 * c.getPA().sendFrame126("If you still need help ask a staff member or play", frame++); for (int i = 0; i <
			 * 80; i++) { c.getPA().sendFrame126("", frame + i); } c.getPA().showInterface(8134);
			 */
				break;
			case 4143:
			case 50245:
				player.getPA().spellTeleport(3222, 3219, 0);
				player.sendMessage("You teleport to Lumbridge..");
			/*
			 * case 117123: c.getDH().sendOption4("Barrows", "Pest Control", "Fight Arena", "Zombies Minigame");
			 * c.teleAction = 65; break;
			 */
				break;
			case 50253:
			case 4146:
			case 117131:
				player.getPA().spellTeleport(2965, 3382, 0);
				player.sendMessage("You teleport to Falador.");
				// c.sendMessage("Click the 'Greater teleport focus' to travel places.");
			/*
			 * c.getDH().sendOption5("Godwars", "King Black Dragon@red@ (Wild)", "Dagannoth Kings",
			 * "Chaos Elemental@red@ (Wild)", "@blu@Next Page"); c.teleAction = 3;
			 */
				break;
		/*
		 * case 62154: if (c.isMember > 0) { c.getPA().startTeleport(2409 + Misc.random(2), 4728 + Misc.random(2), 4,
		 * "dungeon"); c.sendMessage("Welcome to the Official @red@Normal Donator@gre@ Zone.");
		 * c.sendMessage("The altar gives you 2x the prayer exp. More features to come"); //
		 * c.sendMessage("@red@Currently fixing a glitch. WIll be back within 24 hours."); } else {
		 * c.sendMessage("Sorry, but you must be a donator."); } break;
		 */
			case 212146:
				player.getPA().showInterface(53399);
				//TeleportHandler.openInterface(c);
				break;
			case 51005:
			case 4150:
			case 117154:
				player.getPA().spellTeleport(2757, 3479, 0);
				player.sendMessage("You teleport to Camelot.");
			/*
			 * c.getDH().sendOption5("Edgeville@red@ (+5)", "Removed", "Revenant's @red@(Multi +46)", "Mage Bank",
			 * "EastDragons @red@(+21)"); c.teleAction = 4;
			 */
				break;
		/*
		 * case 62155: TeleportHandler.openInterface(c); break;
		 */
			case 51013:
			case 6004:
			case 117112:
				player.getPA().spellTeleport(2663, 3306, 0);
				player.sendMessage("You teleport to Ardougne.");
			/*
			 * c.getDH().sendOption5("Mining/Smithing", "Agility", "Fishing Guild", "Theiving", "Farming"); c.teleAction
			 * = 5;
			 */
				break;
			case 51023:
			case 6005:
				player.getPA().spellTeleport(2552, 3114, 0);
				player.sendMessage("You teleport to the Watchtower.");
				// c.getDH().sendOption5("Option 16", "Option 2", "Option 3",
				// "Option 4", "Option 5");
				// c.teleAction = 6;
				break;
			case 29031:
				player.getPA().spellTeleport(2838, 3588, 0);
				player.sendMessage("You teleport to Trollheim Stronghold.");
				break;
			case 72038:
			case 51039:
				// c.getDH().sendOption5("Option 18", "Option 2", "Option 3",
				// "Option 4", "Option 5");
				// c.teleAction = 8;
				break;
		/* Rules Interface Buttons */
			case 125011: // Click agree
				if (!player.ruleAgreeButton) {
					player.ruleAgreeButton = true;
					player.getPA().sendFrame36(701, 1);
				} else {
					player.ruleAgreeButton = false;
					player.getPA().sendFrame36(701, 0);
				}
				break;
			case 125003:// Accept
				if (player.ruleAgreeButton) {
					player.getPA().showInterface(3559);
					player.newPlayer = false;
				} else if (!player.ruleAgreeButton) {
					player.sendMessage("You need to click on you agree before you can continue on.");
				}
				break;
			case 125006:// Decline
				player.sendMessage("You have chosen to decline, Player will be disconnected from the Draynor.");
				break;
			case 3189:
				player.getData().splitChat = !player.getData().splitChat;
				player.getPA().sendFrame36(287, player.getData().splitChat ? 1 : 0);
				break;
		/* End Rules Interface Buttons */
		/* Player Options */
			case 74176:
				if (!player.mouseButton) {
					player.mouseButton = true;
					player.getPA().sendFrame36(500, 1);
					player.getPA().sendFrame36(170, 1);
				} else if (player.mouseButton) {
					player.mouseButton = false;
					player.getPA().sendFrame36(500, 0);
					player.getPA().sendFrame36(170, 0);
				}
				break;
			case 74188:
				if (!player.acceptAid) {
					player.acceptAid = true;
					player.getPA().sendFrame36(503, 1);
					player.getPA().sendFrame36(427, 1);
				} else {
					player.acceptAid = false;
					player.getPA().sendFrame36(503, 0);
					player.getPA().sendFrame36(427, 0);
				}
				break;
			case 74192:
				if (!player.getWalkingQueue().isRunningToggled()) {
					player.getWalkingQueue().setRunningToggled(true);
					player.getPA().sendFrame36(504, 1);
					player.getPA().sendFrame36(173, 1);
				} else {
					player.getWalkingQueue().setRunningToggled(false);
					player.getPA().sendFrame36(504, 0);
					player.getPA().sendFrame36(173, 0);
				}
				break;
			case 74201:// brightness1
				player.getPA().sendFrame36(505, 1);
				player.getPA().sendFrame36(506, 0);
				player.getPA().sendFrame36(507, 0);
				player.getPA().sendFrame36(508, 0);
				player.getPA().sendFrame36(166, 1);
				break;
			case 74203:// brightness2
				player.getPA().sendFrame36(505, 0);
				player.getPA().sendFrame36(506, 1);
				player.getPA().sendFrame36(507, 0);
				player.getPA().sendFrame36(508, 0);
				player.getPA().sendFrame36(166, 2);
				break;
			case 74204:// brightness3
				player.getPA().sendFrame36(505, 0);
				player.getPA().sendFrame36(506, 0);
				player.getPA().sendFrame36(507, 1);
				player.getPA().sendFrame36(508, 0);
				player.getPA().sendFrame36(166, 3);
				break;
			case 74205:// brightness4
				player.getPA().sendFrame36(505, 0);
				player.getPA().sendFrame36(506, 0);
				player.getPA().sendFrame36(507, 0);
				player.getPA().sendFrame36(508, 1);
				player.getPA().sendFrame36(166, 4);
				break;
			case 74206:// area1
				player.getPA().sendFrame36(509, 1);
				player.getPA().sendFrame36(510, 0);
				player.getPA().sendFrame36(511, 0);
				player.getPA().sendFrame36(512, 0);
				break;
			case 74207:// area2
				player.getPA().sendFrame36(509, 0);
				player.getPA().sendFrame36(510, 1);
				player.getPA().sendFrame36(511, 0);
				player.getPA().sendFrame36(512, 0);
				break;
			case 74208:// area3
				player.getPA().sendFrame36(509, 0);
				player.getPA().sendFrame36(510, 0);
				player.getPA().sendFrame36(511, 1);
				player.getPA().sendFrame36(512, 0);
				break;
			case 74209:// area4
				player.getPA().sendFrame36(509, 0);
				player.getPA().sendFrame36(510, 0);
				player.getPA().sendFrame36(511, 0);
				player.getPA().sendFrame36(512, 1);
				break;
			case 168:
				player.startAnimation(855);
				break;
			case 169:
				player.startAnimation(856);
				break;
			case 162:
				player.startAnimation(857);
				break;
			case 164:
				player.startAnimation(858);
				break;
			case 165:
				player.startAnimation(859);
				break;
			case 161:
				player.startAnimation(860);
				break;
			case 170:
				player.startAnimation(861);
				break;
			case 171:
				player.startAnimation(862);
				break;
			case 163:
				player.startAnimation(863);
				break;
			case 167:
				player.startAnimation(864);
				break;
			case 172:
				player.startAnimation(865);
				break;
			case 166:
				player.startAnimation(866);
				break;
			case 52050:
				player.startAnimation(2105);
				break;
			case 52051:
				player.startAnimation(2106);
				break;
			case 52052:
				player.startAnimation(2107);
				break;
			case 52053:
				switch (Misc.random(1) + 1) {
					case 1:
						player.startAnimation(2108);
						break;
					case 2:
						player.startAnimation(6409);
						break;
				}
				break;
			case 52054:
				player.startAnimation(2109);
				break;
			case 52055:
				player.startAnimation(2110);
				break;
			case 52056:
				player.startAnimation(2111);
				break;
			case 52057:
				player.startAnimation(2112);
				break;
			case 52058:
				player.startAnimation(2113);
				break;
			case 43092:
				player.startAnimation(0x558);
				break;
			case 2155:
				player.startAnimation(0x46B);
				break;
			case 25103:
				player.startAnimation(0x46A);
				break;
			case 25106:
				player.startAnimation(0x469);
				break;
			case 2154:
				player.startAnimation(0x468);
				player.gfx0(2602);
				break;
			case 52071:
				player.startAnimation(0x84F);
				break;
			case 52072:
				player.startAnimation(0x850);
				break;
			case 59062:
				player.startAnimation(2836);
				break;
			case 72032:
				player.startAnimation(3544);
				break;
			case 72033:
				player.startAnimation(3543);
				break;
			// case 72254:
			// c.startAnimation(3866);
			// break;
			case 88058:
				player.startAnimation(7531);
				break;
			case 88059:
				player.startAnimation(2414);
				player.gfx0(1537);
				break;
			case 88060:
				player.startAnimation(8770);
				player.gfx0(1553);
				break;
			case 88061:
				player.startAnimation(9990);
				player.gfx0(1734);
				break;
			case 88062:
				player.startAnimation(10530);
				player.gfx0(1864);
				break;
			case 88063:
				player.startAnimation(11044);
				player.gfx0(1973);
				break;
			case 28166:
				break;
			case 72254:

				int[] capes = {9784, 9763, 9793, 9796, 9766, 9781, 9799, 9790, 9802, 9808, 9748, 9754, 9811, 9778,
						9787, 9775, 9760, 9757, 9805, 9772, 9769, 9751, 9949, 9813, 12170, 9949};

				boolean found = false;

				for (int cape : capes) {
					if (player.equipment[PlayerConstants.CAPE] == cape) {
						found = true;
						break;
					}
				}

				if (!found) {
					player.sendMessage("You need a skill cape equipped");
					return;
				}
				if ((System.currentTimeMillis() - player.lastSkillEmote > 10000)
						|| (System.currentTimeMillis() - player.lastDungEmote > 3000)) {

					Achievements.progressMade(player, Achievements.Types.PERFORM_A_SKILLCAPE_EMOTE);

					int dung = 18509;
					int dung2 = 18508;
					if (player.equipment[player.playerCape] == dung || player.equipment[player.playerCape] == dung2
							|| player.equipment[player.playerCape] == 19709) {
						// c.dungSkill = true;
						player.lastDungEmote = System.currentTimeMillis();
					}
					if (player.equipment[player.playerCape] != 18509) {
						player.lastSkillEmote = System.currentTimeMillis();
					}
					int[] emotes = {4937, 4939, 4941, 4943, 4947, 4949, 4951, 4953, 4955, 4957, 4959, 4961, 4963, 4965,
							4967, 4969, 4979, 4973, 4975, 4977, 4971, 4981, 5158, 4945};
					int[] gfx = {812, 813, 814, 815, 817, 818, 819, 820, 821, 822, 823, 824, 825, 826, 827, 835, 829, 832,
							831, 830, 833, 828, 907, 816};
					for (int i = 0; i < capes.length; i++) {
						if (player.equipment[player.playerCape] == capes[i] || player.equipment[player.playerCape] == capes[i] - 1) {
							player.startAnimation(emotes[i]);
							player.gfx0(gfx[i]);
						}
					}
				}
				player.setEventHandled(true);
				break;
			case 24017:
				player.getPA().resetAutocast();
				// c.sendFrame246(329, 200, c.equipment[c.playerWeapon]);
				player.getItems().sendWeapon(player.equipment[player.playerWeapon],
						player.getItems().getItemName(player.equipment[player.playerWeapon]));
				// c.setSidebarInterface(0, 328);
				// c.setSidebarInterface(6, c.playerMagicBook == 0 ? 1151 :
				// c.playerMagicBook == 1 ? 12855 : 1151);
				break;
		}
	}

	public void doThreeOptionClick(Player c, int id) {
		/*
		 * if (c.dialogueAction == 131) { c.getDH().sendOption3("Edgeville", "Revenants @red@+49 Multi)", "Mage Bank");
		 * c.teleAction = 400; }
		 */
		switch (id) {
			case 9167:// sendOption3 first click
				if (c.usingRingOfWealth) {
					c.getPA().startTeleport(2512, 3860, 0, "modern");
					c.usingRingOfWealth = false;
					return;
				}
				switch (c.dialogueAction) {

					case 31:
						if (c.getOutStream() != null) {
							c.yInterfaceId = 2;
							c.getOutStream().createFrame(187);
							c.flushOutStream();
						}
						break;
					default:
						c.getPA().removeAllWindows();
						break;
				}
				break;
			case 9168:// sendOption3 second click
				if (c.usingRingOfWealth) {
					c.getPA().startTeleport(2948, 3147, 0, "modern");
					c.usingRingOfWealth = false;
					return;
				}
				switch (c.dialogueAction) {
					case 31:
						if (c.getOutStream() != null) {
							c.yInterfaceId = 3;
							c.getOutStream().createFrame(187);
							c.flushOutStream();
						}
						break;
					case 65:
						if (c.getOutStream() != null) {
							c.yInterfaceId = 8;
							c.getOutStream().createFrame(187);
							c.flushOutStream();
						}
						break;
					default:
						c.getPA().removeAllWindows();
						break;
				}
				break;
			case 9169:// sendOption3 third click
				if (c.usingRingOfWealth) {
					c.getPA().startTeleport(2851, 3238, 0, "modern");
					c.usingRingOfWealth = false;
					return;
				}
				switch (c.dialogueAction) {
					case 31:
						if (c.getOutStream() != null) {
							c.yInterfaceId = 5;
							c.getOutStream().createFrame(187);
							c.flushOutStream();
						}
						break;
					default:
						c.getPA().removeAllWindows();
						break;
				}
				break;
		}
		c.dialogueAction = -1;
	}
}
