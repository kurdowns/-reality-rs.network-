package com.zionscape.server.model.content.minigames.christmas;

import com.zionscape.server.Server;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.tick.Tick;

/**
 * Christmas event based off the original 2005 Runescape Christmas event.
 *
 * @author Tyler
 */
public class ChristmasEvent {

	public static void blueMarionetteActions(Player client) {
		switch (client.action) {
			case "jump":
				client.startAnimation(3003);
				client.gfx0(511);
				break;
			case "walk":
				client.startAnimation(3004);
				client.gfx0(512);
				break;
			case "bow":
				client.startAnimation(3005);
				client.gfx0(513);
				break;
			case "dance":
				client.startAnimation(3006);
				client.gfx0(514);
				break;
		}
	}

	public static void greenMarionetteActions(Player client) {
		switch (client.action) {
			case "jump":
				client.startAnimation(3003);
				client.gfx0(515);
				break;
			case "walk":
				client.startAnimation(3004);
				client.gfx0(516);
				break;
			case "bow":
				client.startAnimation(3005);
				client.gfx0(517);
				break;
			case "dance":
				client.startAnimation(3006);
				client.gfx0(518);
				break;
		}
	}

	public static void redMarionetteActions(Player client) {
		switch (client.action) {
			case "jump":
				client.startAnimation(3003);
				client.gfx0(507);
				break;
			case "walk":
				client.startAnimation(3004);
				client.gfx0(508);
				break;
			case "bow":
				client.startAnimation(3005);
				client.gfx0(509);
				break;
			case "dance":
				client.startAnimation(3006);
				client.gfx0(510);
				break;
		}
	}

	public static void completeEvent(Player client) {
		client.christmasEvent = 3;
		client.greenBox = false;
		client.blueBox = false;
		client.redBox = false;
		client.yellowBox = false;
		client.pinkBox = false;
		client.gfx0(623);
		client.getItems().addItem(14595, 1);
		client.getItems().addItem(14603, 1);
		client.getItems().addItem(14602, 1);
		client.getItems().addItem(14605, 1);
		client.sendMessage("Congratulations, you've completed this years Christmas Event!");
		client.sendMessage("Merry Christmas from the Draynor.org management!");
	}

	public static void grabMarionettes(Player client) {
		if (client.christmasEvent != 4) {
			client.sendMessage("We're not sure how this happened, however your details have been blacklisted and");
			client.sendMessage("we are investigating the issue.");
			return;
		}
		client.getItems().addItem(6865, 1);
		client.getItems().addItem(6866, 1);
		client.getItems().addItem(6867, 1);
		client.sendMessage("Enjoy the marionettes!");
	}

	public static void handleObjectClicking(final Player client, final int objectId) {
		Server.getTickManager().submit(new Tick(1) {

			public void execute() {
				if (objectId == 10804) {
					client.startAnimation(827);
				} else {
					client.startAnimation(828);
				}
				this.stop();
			}
		});
		Server.getTickManager().submit(new Tick(4) {

			public void execute() {
				switch (objectId) {
					case 10804:
						/** Climbing down trapdoor */
						if (client.getItems().playerHasItem(6854, 1)) {
							client.sendMessage("The puppet box has been removed. You may obtain another one from Rosie");
							client.getItems().deleteItem(6854, 28);
						}
						if (client.christmasEvent >= 1) {
							// client.getPA().movePlayer(2004, 4426, 1);
							// client.sendMessage("You carefully make your way down the trapdoor...");
							// client.sendMessage("... and find yourself in some sort of workshop.");
						} else {
							client.sendMessage("Check back next year!");
						}
						break;
					case 10699:
						/** Climbing back up to trapdoor */
						client.getPA().movePlayer(3091, 3275, 0);
						client.sendMessage("You carefully make your way up the ladder...");
						client.sendMessage("... and find yourself back in the normal world.");
						break;
					case 10708:
						/** Climbing down left ladder */
						switch (client.objectX) {
							case 2006:
								client.getPA().movePlayer(2007, 4431, 0);
								break;
							case 2009:
								client.getPA().movePlayer(2008, 4431, 0);
								break;
						}
						break;
					case 10707:
						/** Climbing up left ladder */
						switch (client.objectX) {
							case 2006:
								client.getPA().movePlayer(2005, 4431, 1);
								break;
							case 2009:
								client.getPA().movePlayer(2010, 4431, 1);
								break;
						}
						break;
				}
				this.stop();
			}
		});
	}

}
