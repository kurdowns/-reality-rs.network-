package com.zionscape.server.model.players.skills;

import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.players.Areas;
import com.zionscape.server.model.players.Player;

public class Prayer {

	public int[][] bonesExp = {{526, 30}, {532, 55}, {534, 85}, {20268, 100}, {536, 145}, {6812, 145}, {6729, 190}, {14793, 200}, {4834, 275}, {18830, 250}};
	Player c;

	public Prayer(Player c) {
		this.c = c;
	}

	public static boolean onDialogueOption(Player player, int option) {
		if (player.getDialogueOwner().equals(Prayer.Dialogue.NORMAL_ALTER)
				|| player.getDialogueOwner().equals(Prayer.Dialogue.DONATOR_ALTER)) {
			switch (option) {
				case 1:
					player.getPrayer().bonesOnAltar(player.getCurrentDialogueId(), 1,
							player.getDialogueOwner().equals(Prayer.Dialogue.DONATOR_ALTER));
					break;
				case 2:
					player.getPrayer().bonesOnAltar(player.getCurrentDialogueId(), 10,
							player.getDialogueOwner().equals(Prayer.Dialogue.DONATOR_ALTER));
					break;
				case 3:
					player.getPA().sendX(0);
					break;
				case 4:
					player.getPrayer().bonesOnAltar(player.getCurrentDialogueId(), 28,
							player.getDialogueOwner().equals(Prayer.Dialogue.DONATOR_ALTER));
					break;
			}
			return true;
		}
		return false;
	}

	public static boolean onXAmountEntered(Player player, int interfaceId, int amount) {

		if (player.getDialogueOwner() == null) {
			return false;
		}

		if (player.getDialogueOwner().equals(Prayer.Dialogue.NORMAL_ALTER)
				|| player.getDialogueOwner().equals(Prayer.Dialogue.DONATOR_ALTER)) {
			player.getPrayer().bonesOnAltar(player.getCurrentDialogueId(), amount,
					player.getDialogueOwner().equals(Prayer.Dialogue.DONATOR_ALTER));
			return true;
		}
		return false;
	}

	public void buryBone(int id, int slot) {
		if (System.currentTimeMillis() - c.buryDelay > 1500) {
			c.getItems().deleteItem(id, slot, 1);
			c.sendMessage("You bury the bones.");
			c.getPA().addSkillXP(this.getExp(id), 5);
			c.buryDelay = System.currentTimeMillis();
			c.startAnimation(827);

			Achievements.progressMade(c, Achievements.Types.BURY_100_BONES);

			if (id == 532) {
				Achievements.progressMade(c, Achievements.Types.BURY_250_BIG_BONES);
			}
			if (id == 536) {
				Achievements.progressMade(c, Achievements.Types.BURY_500_DRAGON_BONES);
			}
			if (id == 18830) {
				Achievements.progressMade(c, Achievements.Types.BURY_500_FROST_BONES);
				Achievements.progressMade(c, Achievements.Types.BURY_1000_FROST_BONES);
			}
		}
	}

	public void bonesOnAltar(int id, int amount, boolean donator) {

		int playerHas = c.getItems().amountOfItem(id);

		if (amount > playerHas) {
			amount = playerHas;
		}

		if (amount == 0) {
			return;
		}

		final int finalAmount = amount;

		c.getPA().closeAllWindows();
		c.setDialogueOwner(null);
		c.setCurrentDialogueId(0);

		c.setBusy(true);
		final Location playerLocation = c.getLocation();

		GameCycleTaskHandler.addEvent(c, new GameCycleTask() {

			int processed = 0;

			@Override
			public void execute(GameCycleTaskContainer container) {

				if (!c.getLocation().equals(playerLocation)) {
					container.stop();
					return;
				}

				if (!c.getItems().playerHasItem(id)) {
					container.stop();
					return;
				}

				c.startAnimation(896);

				c.getItems().deleteItem(id, 1);
				c.sendMessage("The gods are pleased with your offering.");

				int xp = (int) Math.floor((1 * getExp(id)) * 2.5);


				if(Areas.inRespectedArea(c.getLocation())) {
					xp *= 2.5;
				}
				if(Areas.inExtremeArea(c.getLocation())) {
					xp *= 2.0;
				}
				if (donator) {
					c.getPA().addSkillXP((int)Math.floor((double)xp * 1.5), 5);
				} else {
					c.getPA().addSkillXP(xp, 5);
				}

				if (++processed >= finalAmount) {
					container.stop();
				}
			}

			@Override
			public void stop() {
				c.startAnimation(65535);
				c.setBusy(false);
			}
		}, 5, true);
	}

	public boolean isBone(int id) {
		for (int j = 0; j < bonesExp.length; j++) {
			if (bonesExp[j][0] == id) {
				return true;
			}
		}
		return false;
	}

	public int getExp(int id) {
		for (int j = 0; j < bonesExp.length; j++) {
			if (bonesExp[j][0] == id) {
				return bonesExp[j][1];
			}
		}
		return 0;
	}

	public enum Dialogue {
		NORMAL_ALTER, DONATOR_ALTER
	}

}