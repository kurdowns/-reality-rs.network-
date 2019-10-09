package com.zionscape.server.model.content.minigames.christmas;

import com.zionscape.server.Server;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.tick.Tick;

public class MarionetteMaking {

	public static void stringMarionette(Player player, int itemId) {
		if (itemId == 6874) {
			player.getItems().deleteItem(6874, 1);
			player.getItems().deleteItem(6864, 1);
			player.getItems().addItem(6870, 1);
		}
		if (itemId == 6878) {
			player.getItems().deleteItem(6878, 1);
			player.getItems().deleteItem(6864, 1);
			player.getItems().addItem(6868, 1);
		}
		if (itemId == 6882) {
			player.getItems().deleteItem(6882, 1);
			player.getItems().deleteItem(6864, 1);
			player.getItems().addItem(6869, 1);
		}
	}

	public static void checkPuppetBox(Player player) {
		if (player.redAmount == 2 && player.blueAmount == 2 && player.greenAmount == 2) {
			player.redAmount = 0;
			player.blueAmount = 0;
			player.greenAmount = 0;
			player.getItems().deleteItem(6852, 1);
			player.getItems().addItem(6854, 1);
			player.sendMessage("You've completed this puppet box! You can go ask Rosie for another one.");
		}
	}

	public static void completePuppetBox(Player player) {
		player.puppetBoxesCompleted++;
		player.getItems().deleteItem(6854, 1);
		player.sendMessage("You've given Rosie " + player.puppetBoxesCompleted + "/5 marionette boxes required.");
		if (player.puppetBoxesCompleted == 5) {
			player.sendMessage("Congratulations, you've given Rosie all the completed puppet boxes!");
			player.marionetteMaking = 1;
		}
		if (player.puppetBoxesCompleted == 5 && player.baubleBoxesCompleted == 6) {
			player.sendMessage("@blu@Congratulations, you've completed the christmas task! Go speak with Santa!");
		}
	}

	public static void fillPuppetBox(Player player, int itemId) {
		if (itemId == 6874 || itemId == 6878 || itemId == 6882) {
			player.sendMessage("You need to string this before you can put it in the puppet box!");
			return;
		}
		if (itemId == 6870) {
			if (player.redAmount < 2) {
				player.getItems().deleteItem(itemId, 1);
				player.sendMessage("You put the red marionette carefully into the box.");
				player.redAmount++;
			} else {
				player.sendMessage("This puppet box is already full with red marionettes!");
			}
		}
		if (itemId == 6868) {
			if (player.blueAmount < 2) {
				player.getItems().deleteItem(itemId, 1);
				player.sendMessage("You put the blue marionette carefully into the box.");
				player.blueAmount++;
			} else {
				player.sendMessage("This puppet box is already full with blue marionettes!");
			}
		}
		if (itemId == 6869) {
			if (player.greenAmount < 2) {
				player.getItems().deleteItem(itemId, 1);
				player.sendMessage("You put the green marionette carefully into the box.");
				player.greenAmount++;
			} else {
				player.sendMessage("This puppet box is already full with green marionettes!");
			}
		}
		checkPuppetBox(player);
	}

	public static void handleObjectClicking(Player player, int objectId) {
		if (player.marionetteMaking == 1) {
			player.sendMessage("You've already finished with marionette making!");
			return;
		}
		player.startAnimation(832);
		switch (objectId) {
			case 10687: // blue torso
				if (player.getItems().playerHasItem(6877)) {
					player.sendMessage("You can only do 1 puppet of the same color at a time!");
					return;
				}
				player.getDH().sendStatement("You take a blue puppet torso from the pile.");
				player.getItems().addItem(6877, 1);
				break;
			case 10689: // blue legs
				if (player.getItems().playerHasItem(6876)) {
					player.sendMessage("You can only do 1 puppet of the same color at a time!");
					return;
				}
				player.getDH().sendStatement("You take blue puppet legs from the pile.");
				player.getItems().addItem(6876, 1);
				break;
			case 10688: // blue arms
				if (player.getItems().playerHasItem(6875)) {
					player.sendMessage("You can only do 1 puppet of the same color at a time!");
					return;
				}
				player.getDH().sendStatement("You take blue puppet arms from the pile.");
				player.getItems().addItem(6875, 1);
				break;
			case 10686: // blue head
				if (!player.getItems().playerHasItem(6877)) {
					player.getDH().sendStatement("I think I should get a blue torso before doing this.");
					return;
				}
				if (player.blueHead) {
					player.getDH().sendStatement("You've already attached the head to the puppet!");
					return;
				}
				player.blueHead = true;
				player.getDH().sendStatement("You seem to be able to attach the head to the puppet easily.");
				break;
			case 10695: // green torso
				if (player.getItems().playerHasItem(6881)) {
					player.sendMessage("You can only do 1 puppet of the same color at a time!");
					return;
				}
				player.getDH().sendStatement("You take a green puppet torso from the pile.");
				player.getItems().addItem(6881, 1);
				break;
			case 10697: // green legs
				if (player.getItems().playerHasItem(6880)) {
					player.sendMessage("You can only do 1 puppet of the same color at a time!");
					return;
				}
				player.getDH().sendStatement("You take green puppet legs from the pile.");
				player.getItems().addItem(6880, 1);
				break;
			case 10696: // green arms
				if (player.getItems().playerHasItem(6879)) {
					player.sendMessage("You can only do 1 puppet of the same color at a time!");
					return;
				}
				player.getDH().sendStatement("You take green puppet arms from the pile.");
				player.getItems().addItem(6879, 1);
				break;
			case 10694: // green head
				if (!player.getItems().playerHasItem(6881)) {
					player.getDH().sendStatement("I think I should get a green torso before doing this.");
					return;
				}
				if (player.greenHead) {
					player.getDH().sendStatement("You've already attached the head to the puppet!");
					return;
				}
				player.greenHead = true;
				player.getDH().sendStatement("You seem to be able to attach the head to the puppet easily.");
				break;
			case 10691: // red torso
				if (player.getItems().playerHasItem(6873)) {
					player.sendMessage("You can only do 1 puppet of the same color at a time!");
					return;
				}
				player.getDH().sendStatement("You take a red puppet torso from the pile.");
				player.getItems().addItem(6873, 1);
				break;
			case 10693: // red legs
				if (player.getItems().playerHasItem(6872)) {
					player.sendMessage("You can only do 1 puppet of the same color at a time!");
					return;
				}
				player.getDH().sendStatement("You take red puppet legs from the pile.");
				player.getItems().addItem(6872, 1);
				break;
			case 10692: // red arms
				if (player.getItems().playerHasItem(6871)) {
					player.sendMessage("You can only do 1 puppet of the same color at a time!");
					return;
				}
				player.getDH().sendStatement("You take red puppet arms from the pile.");
				player.getItems().addItem(6871, 1);
				break;
			case 10690: // red head
				if (!player.getItems().playerHasItem(6873)) {
					player.getDH().sendStatement("I think I should get a red torso before doing this.");
					return;
				}
				if (player.redHead) {
					player.getDH().sendStatement("You've already attached the head to the puppet!");
					return;
				}
				player.redHead = true;
				player.getDH().sendStatement("You seem to be able to attach the head to the puppet easily.");
				break;
		}
	}

	public static void buildPuppet(final Player player) {
		if (player.marionetteMaking == 1) {
			player.sendMessage("You've already finished with marionette making!");
			return;
		}
		if (player.doingAction)
			return;
		player.doingAction = true;
		Server.getTickManager().submit(new Tick(2) {

			public void execute() {
				player.startAnimation(9045);
				this.stop();
			}
		});
		Server.getTickManager().submit(new Tick(6) {

			public void execute() {
				switch (player.color) {
					case "blue":
						if (player.getItems().playerHasItem(6875) && player.getItems().playerHasItem(6876)
								&& player.getItems().playerHasItem(6877)) {
							if (!player.blueHead) {
								player.sendMessage("You don't have the head attached to this puppet!");
								this.stop();
								return;
							}
							player.getItems().deleteItem(6875, 1);
							player.getItems().deleteItem(6876, 1);
							player.getItems().deleteItem(6877, 1);
							player.getItems().addItem(6878, 1);
							player.blueHead = false;
							player.sendMessage("You build the blue puppet together.");
						} else {
							player.sendMessage("You don't have all the correct pieces to build this puppet correctly!");
							player.sendMessage("Make sure you're building at the correct colored table for your puppet.");
						}
						break;
					case "green":
						if (player.getItems().playerHasItem(6881) && player.getItems().playerHasItem(6880)
								&& player.getItems().playerHasItem(6879)) {
							if (!player.greenHead) {
								player.sendMessage("You don't have the head attached to this puppet!");
								this.stop();
								return;
							}
							player.getItems().deleteItem(6880, 1);
							player.getItems().deleteItem(6881, 1);
							player.getItems().deleteItem(6879, 1);
							player.getItems().addItem(6882, 1);
							player.greenHead = false;
							player.sendMessage("You build the green puppet together.");
						} else {
							player.sendMessage("You don't have all the correct pieces to build this puppet correctly!");
							player.sendMessage("Make sure you're building at the correct colored table for your puppet.");
						}
						break;
					case "red":
						if (player.getItems().playerHasItem(6871) && player.getItems().playerHasItem(6872)
								&& player.getItems().playerHasItem(6873)) {
							if (!player.redHead) {
								player.sendMessage("You don't have the head attached to this puppet!");
								this.stop();
								return;
							}
							player.getItems().deleteItem(6871, 1);
							player.getItems().deleteItem(6872, 1);
							player.getItems().deleteItem(6873, 1);
							player.getItems().addItem(6874, 1);
							player.redHead = false;
							player.sendMessage("You build the red puppet together.");
						} else {
							player.sendMessage("You don't have all the correct pieces to build this puppet correctly!");
							player.sendMessage("Make sure you're building at the correct colored table for your puppet.");
						}
						break;
				}
				player.doingAction = false;
				this.stop();
			}
		});
	}
}
