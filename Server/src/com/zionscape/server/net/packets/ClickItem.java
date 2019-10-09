package com.zionscape.server.net.packets;

import com.zionscape.server.Server;
import com.zionscape.server.ServerEvents;
import com.zionscape.server.model.content.cannon.DwarfCannon;
import com.zionscape.server.model.content.minigames.castlewars.CastleWars;
import com.zionscape.server.model.content.minigames.christmas.BaubleMaking;
import com.zionscape.server.model.content.minigames.christmas.ChristmasEvent;
import com.zionscape.server.model.content.treasuretrails.TreasureTrails;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.skills.herblore.Herblore;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.scripting.Scripting;
import com.zionscape.server.scripting.messages.impl.FirstItemActionMessage;
import com.zionscape.server.tick.Tick;
import com.zionscape.server.util.Misc;
import com.zionscape.server.util.Stream;

/**
 * Clicking an item, bury bone, eat food etc
 */
public class ClickItem implements PacketType {

	@SuppressWarnings("unused")
	@Override
	public void processPacket(final Player player, int packetType, int packetSize, Stream stream) {
		int junk = stream.readSignedWordBigEndianA();
		int itemSlot = stream.readUnsignedWordA();
		int itemId = stream.readUnsignedWordBigEndian();

		if (itemSlot > 28 || itemSlot < 0 || itemId < 0) {
			return;
		}

		if (player.inventory[itemSlot] != (itemId + 1)) {
			return;
		}

		if (player.rights >= 3) {
			player.sendMessage("clickitem " + itemSlot + " " + itemId);
		}

		if (Scripting.handleMessage(player,
				new FirstItemActionMessage(new Item(itemId, player.inventoryN[itemSlot]), itemSlot))) {
			return;
		}

		if (ServerEvents.onItemClicked(player, itemId, itemSlot, 1)) {
			return;
		}

		if (TreasureTrails.clickingItem(player, itemId, itemSlot)) {
			return;
		}
		if (CastleWars.onItemClick(player, itemId, 1)) {
			return;
		}
		if (DwarfCannon.clickingItem(player, itemId, 1)) {
			return;
		}
		if (itemId == 5509) {
			player.getPA().addSmallPouch();
		}
		if (itemId == 5510) {
			if (player.level[PlayerConstants.RUNECRAFTING] < 25) {
				player.sendMessage("You need level 25 runecrafting to use this item.");
				return;
			}
			player.getPA().addMediumPouch();
		}
		if (itemId == 5511) {
			if (player.level[PlayerConstants.RUNECRAFTING] < 25) {
				player.sendMessage("You need level 25 runecrafting to use this item.");
				return;
			}
			player.getPA().addMediumPouch();
		}
		if (itemId == 5512) {
			if (player.level[PlayerConstants.RUNECRAFTING] < 50) {
				player.sendMessage("You need level 50 runecrafting to use this item.");
				return;
			}
			player.getPA().addLargePouch();
		}
		if (itemId == 5513) {
			if (player.level[PlayerConstants.RUNECRAFTING] < 50) {
				player.sendMessage("You need level 50 runecrafting to use this item.");
				return;
			}
			player.getPA().addLargePouch();
		}
		if (itemId == 5514) {
			if (player.level[PlayerConstants.RUNECRAFTING] < 75) {
				player.sendMessage("You need level 75 runecrafting to use this item.");
				return;
			}
			player.getPA().addGiantPouch();
		}
		if (itemId == 5515) {
			if (player.level[PlayerConstants.RUNECRAFTING] < 75) {
				player.sendMessage("You need level 75 runecrafting to use this item.");
				return;
			}
			player.getPA().addGiantPouch();
		}
		if (itemId == 6865) {
			player.action = "jump";
			ChristmasEvent.blueMarionetteActions(player);
		}
		if (itemId == 6866) {
			player.action = "jump";
			ChristmasEvent.greenMarionetteActions(player);
		}
		if (itemId == 6867) {
			player.action = "jump";
			ChristmasEvent.redMarionetteActions(player);
		}
		if (itemId == 2379 || itemId == 7509)
			if (player.level[3] - 20 > 0)
				player.dealDamage(20);
		if (itemId == 6852) {
			player.sendMessage("You have " + player.redAmount + " red, " + player.blueAmount + " blue and "
					+ player.greenAmount + " green");
			return;
		}
		if (itemId == 6853) {
			if (!BaubleMaking.canMakeBox(player)) {
				player.sendMessage("You don't have 5 of the same colored baubles to do this!");
				return;
			}
			if (BaubleMaking.hasYellowBaubles(player)) {
				if (player.yellowBox) {
					player.sendMessage("You already have a yellow box of baubles made!");
					return;
				}
				player.getItems().deleteItem(6823, 1);
				player.getItems().deleteItem(6829, 1);
				player.getItems().deleteItem(6835, 1);
				player.getItems().deleteItem(6841, 1);
				player.getItems().deleteItem(6847, 1);
				player.getItems().deleteItem(6853, 1);
				player.getItems().addItem(6855, 1);
				player.sendMessage("You've completed the yellow colored box of baubles!");
				player.yellowBox = true;
				return;
			}
			if (BaubleMaking.hasRedBaubles(player)) {
				if (player.redBox) {
					player.sendMessage("You already have a red box of baubles made!");
					return;
				}
				player.getItems().deleteItem(6824, 1);
				player.getItems().deleteItem(6830, 1);
				player.getItems().deleteItem(6836, 1);
				player.getItems().deleteItem(6842, 1);
				player.getItems().deleteItem(6848, 1);
				player.getItems().deleteItem(6853, 1);
				player.getItems().addItem(6855, 1);
				player.sendMessage("You've completed the red colored box of baubles!");
				player.redBox = true;
				return;
			}
			if (BaubleMaking.hasBlueBaubles(player)) {
				if (player.blueBox) {
					player.sendMessage("You already have a blue box of baubles made!");
					return;
				}
				player.getItems().deleteItem(6825, 1);
				player.getItems().deleteItem(6831, 1);
				player.getItems().deleteItem(6837, 1);
				player.getItems().deleteItem(6843, 1);
				player.getItems().deleteItem(6849, 1);
				player.getItems().deleteItem(6853, 1);
				player.getItems().addItem(6855, 1);
				player.sendMessage("You've completed the blue colored box of baubles!");
				player.blueBox = true;
				return;
			}
			if (BaubleMaking.hasGreenBaubles(player)) {
				if (player.greenBox) {
					player.sendMessage("You already have a green box of baubles made!");
					return;
				}
				player.getItems().deleteItem(6826, 1);
				player.getItems().deleteItem(6832, 1);
				player.getItems().deleteItem(6838, 1);
				player.getItems().deleteItem(6844, 1);
				player.getItems().deleteItem(6850, 1);
				player.getItems().deleteItem(6853, 1);
				player.getItems().addItem(6855, 1);
				player.sendMessage("You've completed the green colored box of baubles!");
				player.greenBox = true;
				return;
			}
			if (BaubleMaking.hasPinkBaubles(player)) {
				if (player.pinkBox) {
					player.sendMessage("You already have a pink box of baubles made!");
					return;
				}
				player.getItems().deleteItem(6827, 1);
				player.getItems().deleteItem(6833, 1);
				player.getItems().deleteItem(6839, 1);
				player.getItems().deleteItem(6845, 1);
				player.getItems().deleteItem(6851, 1);
				player.getItems().deleteItem(6853, 1);
				player.getItems().addItem(6855, 1);
				player.sendMessage("You've completed the pink colored box of baubles!");
				player.pinkBox = true;
				return;
			}
			return;
		}
		if (itemId == 952) {
			if (player.inArea(3553, 3301, 3561, 3294)) {
				player.teleTimer = 3;
				player.newLocation = 1;
			} else if (player.inArea(3550, 3287, 3557, 3278)) {
				player.teleTimer = 3;
				player.newLocation = 2;
			} else if (player.inArea(3561, 3292, 3568, 3285)) {
				player.teleTimer = 3;
				player.newLocation = 3;
			} else if (player.inArea(3570, 3302, 3579, 3293)) {
				player.teleTimer = 3;
				player.newLocation = 4;
			} else if (player.inArea(3571, 3285, 3582, 3278)) {
				player.teleTimer = 3;
				player.newLocation = 5;
			} else if (player.inArea(3562, 3279, 3569, 3273)) {
				player.teleTimer = 3;
				player.newLocation = 6;
			}
			if (player.teleTimer > 0 && player.newLocation > 0) {
				Server.getTickManager().submit(new Tick(player.teleTimer) {

					@Override
					public void execute() {
						if (!player.isDisconnected()) {
							player.getPA().changeLocation();
							player.teleTimer = 0;
						}
						this.stop();
					}
				});
			}
		}
		if (itemId == 9044) {
			// c.getItems().deleteItem(#,c.getItems().getItemSlot(#),1);
			player.getPA().teleTabTeleport(3173, 2981, 0, "teleTab");
		}
		if (itemId == 8007) {
			player.getItems().deleteItem(8007, 1);
			player.getPA().teleTabTeleport(3214, 3425, 0, "modern");
		}
		if (itemId == 8008) {
			player.getItems().deleteItem(8008, 1);
			player.getPA().teleTabTeleport(3222, 3218, 0, "modern");
		}
		if (itemId == 8009) {
			player.getItems().deleteItem(8009, 1);
			player.getPA().teleTabTeleport(2964, 3381, 0, "modern");
		}
		if (itemId == 8011) {
			player.getItems().deleteItem(8011, 1);
			player.getPA().teleTabTeleport(2662, 3306, 0, "modern");
		}
		if (itemId == 15503) { // change 3008 to the item id you have for the potion client sided
			if (player.inWild()) {
				return;
			}
			if (player.specAmount >= 6.0) {
				player.sendMessage("You can not use this potion if you Special is greater than 50%.");
			} else {
				player.specAmount += 5.0;
				player.lastSpecialUse = System.currentTimeMillis();
				player.sendMessage("Your Special has gone up 50%!");
				player.getItems().deleteItem(3008, 1);
				player.getItems().addItem(229, 1);
			}
		}
		if (itemId == 1505) {
			player.getDH().sendStatement("I can be sold for 3m at a store!");
		}
		if (itemId == 8010) {
			player.getItems().deleteItem(8010, 1);
			player.getPA().teleTabTeleport(2757, 3477, 0, "modern");
		}
		switch (itemId) {
		case 6199:
			int[] goodItems = { 3839, 3841, 3843, 6731, 6733, 6735, 6585, 2581, 2577, 3827, 3831, 3835 };
			int[] badItems = { 952, 592, 117, 123, 1712, 2560, 1731 };
			int[] multipleItems = { 537, 558, 560, 562, 561, 882, 888, 892, 995 };
			int a = (int) (Math.random() * goodItems.length);
			int b = (int) (Math.random() * badItems.length);
			int d = (int) (Math.random() * multipleItems.length);
			int ran = Misc.random(10);
			if (ran >= 8) {
				player.getItems().deleteItem(6199, 1);
				player.getItems().addItem(goodItems[a], 1);
			}
			if (ran <= 7 && ran >= 4) {
				player.getItems().deleteItem(6199, 1);
				player.getItems().addItem(badItems[b], 1);
			}
			if (ran <= 3) {
				player.getItems().deleteItem(6199, 1);
				player.getItems().addItem(multipleItems[d], Misc.random(100));
			}
			break;
		case 13180:
			player.startAnimation(13180);
			break;
		case 2528:
			if (player.elite) {
				player.sendMessage("Elite mode accounts cannot use XP lamps.");
				break;
			}

			player.getPA().showInterface(23700);
			player.lampVfy = true;
			player.sendMessage("@red@NOTE: You only get one chance to select which skill you want 7M EXP in.");
			break;
		}
		Herblore.cleanHerb(player, itemId, itemSlot);
		if (player.getFood().isFood(itemId)) {
			player.getFood().eat(itemId, itemSlot);
		}
		// ScriptManager.callFunc("itemClick_"+itemId, c, itemId, itemSlot);
		if (player.getPotions().isPotion(itemId)) {
			player.getPotions().handlePotion(itemId, itemSlot);
		}
		/*
		 * if(c.getPotions().isOvl(itemId))
		 * c.getPotions().drinkOvl(itemId,itemId+1,itemSlot);
		 */
		if (player.getPrayer().isBone(itemId)) {
			player.getPrayer().buryBone(itemId, itemSlot);
		}
	}
}