package com.zionscape.server.model.content.minigames.christmas;

import com.zionscape.server.Server;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.tick.Tick;

public class ChristmasTree {

	public static void decorateTree(final Player client) {
		if (client.christmasEvent < 4) {
			client.sendMessage("You must complete the Christmas event before decorating this tree!");
			return;
		}
		if (client.giftAmount == 5) {
			client.sendMessage("This tree is fully decorated! You can not decorate it anymore.");
			return;
		}
		if (!client.getItems().playerHasItem(6855)) {
			client.sendMessage("You need a bauble box if you wish to decorate this tree!");
			return;
		}
		if (client.doingAction)
			return;
		client.getItems().deleteItem(6855, 1);
		client.doingAction = true;
		client.giftAmount++;
		client.startAnimation(899);
		client.sendMessage("You put the bauble box under the tree...");
		Server.getTickManager().submit(new Tick(4) {

			public void execute() {
				if (client.giftAmount != 5) {
					client.sendMessage("You notice it seems to be slightly brighter then before.");
				}
				switch (client.giftAmount) {
					case 0:
						client.getPA().object(10652, 3085, 3485, 1, 10);
						break;
					case 1:
						client.getPA().object(10653, 3085, 3485, 1, 10);
						break;
					case 2:
						client.getPA().object(10654, 3085, 3485, 1, 10);
						break;
					case 3:
						client.getPA().object(10658, 3085, 3485, 1, 10);
						break;
					case 4:
						client.getPA().object(10659, 3085, 3485, 1, 10);
						break;
					case 5:
						client.baubleMaking = 1;
						client.sendMessage("Congratulations! You've finished completely decorating the Christmas tree!");
						client.getPA().object(10660, 3085, 3485, 1, 10);
						break;
				}
				client.doingAction = false;
				this.stop();
			}
		});
	}

}
