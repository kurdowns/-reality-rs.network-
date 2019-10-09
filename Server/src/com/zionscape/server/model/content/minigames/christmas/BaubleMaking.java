package com.zionscape.server.model.content.minigames.christmas;

import com.zionscape.server.Server;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.tick.Tick;

public class BaubleMaking {

	static int interfaceId = 15843;

	static int[] colors = {61250, /** Red */
			61248, /** Yellow */
			61253, /** Blue */
			61251, /** Green */
			61252, /** Pink */
	};
	static int[] baubles = {6822, 6828, 6834, 6840, 6846};

	private static void reset(Player client) {
		client.redBox = false;
		client.yellowBox = false;
		client.blueBox = false;
		client.pinkBox = false;
		client.greenBox = false;
	}

	public static void completeBaubleBox(Player client) {
		if (client.baubleBoxesCompleted < 5) {
			client.baubleBoxesCompleted++;
			client.getItems().deleteItem(6855, 1);
			client.sendMessage("You've given Rosie " + client.baubleBoxesCompleted + "/5 bauble boxes required.");
		}
		if (client.baubleBoxesCompleted == 5) {
			client.baubleBoxesCompleted++;
			client.sendMessage("Congratulations, you've given Rosie all completed bauble boxes! Enjoy the scarf.");
			client.getItems().addItem(9470, 1);
			reset(client);
		}
		if (client.baubleBoxesCompleted > 5 && client.baubleMaking != 1) {
			client.getDH().sendDialogues(1565, 3082);
		}
		if (client.puppetBoxesCompleted == 5 && client.baubleBoxesCompleted == 6) {
			client.sendMessage("@blu@Congratulations, you've completed the christmas task! Go speak with Santa!");
		}
	}

	public static boolean hasYellowBaubles(Player client) {
		return (client.getItems().playerHasItem(6823, 1) && client.getItems().playerHasItem(6829, 1)
				&& client.getItems().playerHasItem(6835, 1) && client.getItems().playerHasItem(6841, 1) && client
				.getItems().playerHasItem(6847, 1));
	}

	public static boolean hasRedBaubles(Player client) {
		return (client.getItems().playerHasItem(6824, 1) && client.getItems().playerHasItem(6830, 1)
				&& client.getItems().playerHasItem(6836, 1) && client.getItems().playerHasItem(6842, 1) && client
				.getItems().playerHasItem(6848, 1));
	}

	public static boolean hasBlueBaubles(Player client) {
		return (client.getItems().playerHasItem(6825, 1) && client.getItems().playerHasItem(6831, 1)
				&& client.getItems().playerHasItem(6837, 1) && client.getItems().playerHasItem(6843, 1) && client
				.getItems().playerHasItem(6849, 1));
	}

	public static boolean hasGreenBaubles(Player client) {
		return (client.getItems().playerHasItem(6826, 1) && client.getItems().playerHasItem(6832, 1)
				&& client.getItems().playerHasItem(6838, 1) && client.getItems().playerHasItem(6844, 1) && client
				.getItems().playerHasItem(6850, 1));
	}

	public static boolean hasPinkBaubles(Player client) {
		return (client.getItems().playerHasItem(6827, 1) && client.getItems().playerHasItem(6833, 1)
				&& client.getItems().playerHasItem(6839, 1) && client.getItems().playerHasItem(6845, 1) && client
				.getItems().playerHasItem(6851, 1));
	}

	public static boolean canMakeBox(Player client) {
		return (hasYellowBaubles(client) || (hasRedBaubles(client) || (hasBlueBaubles(client) || (hasGreenBaubles(client) || (hasPinkBaubles(client))))));
	}

	public static void removeBaubles(Player client) {
		client.starBauble = 0;
		client.boxBauble = 0;
		client.treeBauble = 0;
		client.diamondBauble = 0;
		client.bellBauble = 0;
	}

	public static boolean checkBaubles(Player client) {
		return (client.getItems().playerHasItem(6822) || client.getItems().playerHasItem(6828)
				|| client.getItems().playerHasItem(6834) || client.getItems().playerHasItem(6840) || client.getItems()
				.playerHasItem(6846));
	}

	public static void handleBaubleSearching(final Player client) {
		if (client.christmasEvent < 2) {
			client.sendMessage("It's probably best I speak with the head pixie Rosie so I know what I'm doing!");
			return;
		}
		if (client.baubleMaking == 1 && client.giftAmount == 5) {
			client.sendMessage("You've already finished with bauble making!");
			return;
		}
		if (client.doingAction)
			return;
		client.doingAction = true;
		client.startAnimation(827);
		client.sendMessage("You search the decorations box...");
		Server.getTickManager().submit(new Tick(3) {

			public void execute() {
				client.getItems().addItem(randomBauble(), 1);
				client.sendMessage("... and find some sort of bauble.");
				this.stop();
				client.doingAction = false;
			}
		});
	}

	public static void handleBaublePaintingInterface(final Player client) {
		if (client.christmasEvent < 2) {
			client.sendMessage("It's probably best I speak with the head pixie Rosie so I know what I'm doing!");
			return;
		}
		if (client.baubleMaking == 1 && client.giftAmount == 5) {
			client.sendMessage("You've already finished with bauble making!");
			return;
		}
		if (client.doingAction)
			return;
		client.doingAction = true;
		Server.getTickManager().submit(new Tick(1) {

			public void execute() {
				client.startAnimation(1352);
				client.sendMessage("You sit down at the painting table.");
				this.stop();
			}
		});
		Server.getTickManager().submit(new Tick(5) {

			public void execute() {
				client.getPA().showInterface(interfaceId);
				client.startAnimation(0);
				client.doingAction = false;
				client.startAnimation(0);
				this.stop();
			}
		});
	}

	public static void handleBaublePainting(Player client, int colorId) {
		if (client.baubleMaking == 1 && client.giftAmount == 5) {
			client.sendMessage("You've already finished with bauble making!");
			return;
		}
		if (!checkBaubles(client)) {
			client.sendMessage("You don't have any baubles to paint!");
			client.getPA().closeAllWindows();
			return;
		}
		getStarBaubleAmount(client);
		getBoxBaubleAmount(client);
		getDiamondBaubleAmount(client);
		getTreeBaubleAmount(client);
		getBellBaubleAmount(client);
		switch (colorId) {
			case 61250:
				if (client.redBox == true) {
					client.sendMessage("You already have a red box done!");
					removeBaubles(client);
					return;
				}
				if (client.getItems().playerHasItem(6822)) {
					client.getItems().deleteItem(6822, client.starBauble);
					client.getItems().addItem(6824, client.starBauble);
				}
				if (client.getItems().playerHasItem(6828)) {
					client.getItems().deleteItem(6828, client.boxBauble);
					client.getItems().addItem(6830, client.boxBauble);
				}
				if (client.getItems().playerHasItem(6834)) {
					client.getItems().deleteItem(6834, client.diamondBauble);
					client.getItems().addItem(6836, client.diamondBauble);
				}
				if (client.getItems().playerHasItem(6840)) {
					client.getItems().deleteItem(6840, client.treeBauble);
					client.getItems().addItem(6842, client.treeBauble);
				}
				if (client.getItems().playerHasItem(6846)) {
					client.getItems().deleteItem(6846, client.bellBauble);
					client.getItems().addItem(6848, client.bellBauble);
				}
				client.sendMessage("You paint your unpainted baubles red.");
				client.getPA().closeAllWindows();
				break;
			case 61248:
				if (client.yellowBox == true) {
					client.sendMessage("You already have a yellow box done!");
					removeBaubles(client);
					return;
				}
				if (client.getItems().playerHasItem(6822)) {
					client.getItems().deleteItem(6822, client.starBauble);
					client.getItems().addItem(6823, client.starBauble);
				}
				if (client.getItems().playerHasItem(6828)) {
					client.getItems().deleteItem(6828, client.boxBauble);
					client.getItems().addItem(6829, client.boxBauble);
				}
				if (client.getItems().playerHasItem(6834)) {
					client.getItems().deleteItem(6834, client.diamondBauble);
					client.getItems().addItem(6835, client.diamondBauble);
				}
				if (client.getItems().playerHasItem(6840)) {
					client.getItems().deleteItem(6840, client.treeBauble);
					client.getItems().addItem(6841, client.treeBauble);
				}
				if (client.getItems().playerHasItem(6846)) {
					client.getItems().deleteItem(6846, client.bellBauble);
					client.getItems().addItem(6847, client.bellBauble);
				}
				client.sendMessage("You paint your unpainted baubles yellow.");
				client.getPA().closeAllWindows();
				break;
			case 61253:
				if (client.blueBox == true) {
					client.sendMessage("You already have a blue box done!");
					removeBaubles(client);
					return;
				}
				if (client.getItems().playerHasItem(6822)) {
					client.getItems().deleteItem(6822, client.starBauble);
					client.getItems().addItem(6825, client.starBauble);
				}
				if (client.getItems().playerHasItem(6828)) {
					client.getItems().deleteItem(6828, client.boxBauble);
					client.getItems().addItem(6831, client.boxBauble);
				}
				if (client.getItems().playerHasItem(6834)) {
					client.getItems().deleteItem(6834, client.diamondBauble);
					client.getItems().addItem(6837, client.diamondBauble);
				}
				if (client.getItems().playerHasItem(6840)) {
					client.getItems().deleteItem(6840, client.treeBauble);
					client.getItems().addItem(6843, client.treeBauble);
				}
				if (client.getItems().playerHasItem(6846)) {
					client.getItems().deleteItem(6846, client.bellBauble);
					client.getItems().addItem(6849, client.bellBauble);
				}
				client.sendMessage("You paint your unpainted baubles blue.");
				client.getPA().closeAllWindows();
				break;
			case 61251:
				if (client.greenBox == true) {
					client.sendMessage("You already have a green box done!");
					removeBaubles(client);
					return;
				}
				if (client.getItems().playerHasItem(6822)) {
					client.getItems().deleteItem(6822, client.starBauble);
					client.getItems().addItem(6826, client.starBauble);
				}
				if (client.getItems().playerHasItem(6828)) {
					client.getItems().deleteItem(6828, client.boxBauble);
					client.getItems().addItem(6832, client.boxBauble);
				}
				if (client.getItems().playerHasItem(6834)) {
					client.getItems().deleteItem(6834, client.diamondBauble);
					client.getItems().addItem(6838, client.diamondBauble);
				}
				if (client.getItems().playerHasItem(6840)) {
					client.getItems().deleteItem(6840, client.treeBauble);
					client.getItems().addItem(6844, client.treeBauble);
				}
				if (client.getItems().playerHasItem(6846)) {
					client.getItems().deleteItem(6846, client.bellBauble);
					client.getItems().addItem(6850, client.bellBauble);
				}
				client.sendMessage("You paint your unpainted baubles green.");
				client.getPA().closeAllWindows();
				break;
			case 61252:
				if (client.pinkBox == true) {
					client.sendMessage("You already have a pink box done!");
					removeBaubles(client);
					return;
				}
				if (client.getItems().playerHasItem(6822)) {
					client.getItems().deleteItem(6822, client.starBauble);
					client.getItems().addItem(6827, client.starBauble);
				}
				if (client.getItems().playerHasItem(6828)) {
					client.getItems().deleteItem(6828, client.boxBauble);
					client.getItems().addItem(6833, client.boxBauble);
				}
				if (client.getItems().playerHasItem(6834)) {
					client.getItems().deleteItem(6834, client.diamondBauble);
					client.getItems().addItem(6839, client.diamondBauble);
				}
				if (client.getItems().playerHasItem(6840)) {
					client.getItems().deleteItem(6840, client.treeBauble);
					client.getItems().addItem(6845, client.treeBauble);
				}
				if (client.getItems().playerHasItem(6846)) {
					client.getItems().deleteItem(6846, client.bellBauble);
					client.getItems().addItem(6851, client.bellBauble);
				}
				client.sendMessage("You paint your unpainted baubles pink.");
				client.getPA().closeAllWindows();
				break;
		}
		removeBaubles(client);
	}

	public static int getStarBaubleAmount(Player client) {
		for (int i = 0; i < 28; i++) {
			if (client.getItems().playerHasItem(6822, i + 1)) {
				client.starBauble++;
			}
		}
		return client.starBauble;
	}

	public static int getBoxBaubleAmount(Player client) {
		for (int i = 0; i < 28; i++) {
			if (client.getItems().playerHasItem(6828, i + 1)) {
				client.boxBauble++;
			}
		}
		return client.boxBauble;
	}

	public static int getDiamondBaubleAmount(Player client) {
		for (int i = 0; i < 28; i++) {
			if (client.getItems().playerHasItem(6834, i + 1)) {
				client.diamondBauble++;
			}
		}
		return client.diamondBauble;
	}

	public static int getTreeBaubleAmount(Player client) {
		for (int i = 0; i < 28; i++) {
			if (client.getItems().playerHasItem(6840, i + 1)) {
				client.treeBauble++;
			}
		}
		return client.treeBauble;
	}

	public static int getBellBaubleAmount(Player client) {
		for (int i = 0; i < 28; i++) {
			if (client.getItems().playerHasItem(6846, i + 1)) {
				client.bellBauble++;
			}
		}
		return client.bellBauble;
	}

	public static int randomBauble() {
		return baubles[(int) (Math.random() * baubles.length)];
	}

}
