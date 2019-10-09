package com.zionscape.server.model.content.minigames.quests;

import com.zionscape.server.Server;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.dialogue.CloseDialogue;
import com.zionscape.server.model.players.dialogue.Dialogue;
import com.zionscape.server.model.players.dialogue.DialogueHelper;
import com.zionscape.server.tick.Tick;
import com.zionscape.server.world.ItemDrops;
import com.zionscape.server.world.shops.Shops;

import java.util.List;

public class TheStolenCannon {

	public static final int COMPLETED_STAGE = 12;
	private static final int NULODION = 209;
	private static final int DROGO_DWARF = 579;
	private static List<Dialogue> dialogues;

	static {
		try {
			dialogues = DialogueHelper.getDialogues("the-stolen-cannon");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void onPlayerLoggedIn(final Player player) {

		Server.getTickManager().submit(new Tick(17) {

			@Override
			public void execute() {
				if (player == null) {
					stop();
					return;
				}

				if (inMineGasArea(player) && player.equipment[PlayerConstants.HAT] != 1506) {

					if (player.level[PlayerConstants.HITPOINTS] <= 0 || player.isDead) {
						return;
					}

					int totalHp = player.getPA().getLevelForXP(player.xp[PlayerConstants.HITPOINTS]);
					int damage = totalHp / 10;

					if (damage > player.level[PlayerConstants.HITPOINTS]) {
						damage = player.level[PlayerConstants.HITPOINTS];
					}

					player.setHitUpdateRequired(true);
					player.setHitDiff(damage);
					player.updateRequired = true;
					player.poisonMask = 1;
					player.dealDamage(damage);
				}

			}
		});

	}

	public static void sendQuestInformation(Player player) {
		QuestInterfaceGenerator qig = new QuestInterfaceGenerator("The Stolen Cannon");
		qig.add(player.theStolenCannonStatus, 0,
				"You can start this quest by talking to Nulodion at the entrance to the Dwarven Mine on Ice Mountain.");
		qig.lineBreak();
		qig.add(player.theStolenCannonStatus, 1,
				"Nulodion has told you to go see his cousin at the Southern Peninsula of Port Sarim.");
		qig.lineBreak();
		qig.add(player.theStolenCannonStatus, 2,
				"Drogo dwarf was rather rude, maybe we should return to Nulodion and get more information.");
		qig.lineBreak();
		qig.add(player.theStolenCannonStatus, 3,
				"Nulodion has recommended that I take some Iron and Tin ore and speak to his cousin again.");
		qig.lineBreak();
		qig.add(player.theStolenCannonStatus, 4, "Bring 5 pieces of leather to get the Gasmask.");
		qig.lineBreak();
		qig.add(player.theStolenCannonStatus, 5,
				"You should return back to Nulodion and let him know you now have a Gas mask.");
		qig.lineBreak();
		qig.add(player.theStolenCannonStatus, 6,
				"Go speak to Thrain in the Dwarfen mine which witnessed the whole event.");
		qig.lineBreak();
		qig.add(player.theStolenCannonStatus,
				7,
				"You need to find the 3 gang members and slay them all, returning the cannon pieces back to Thrain. He said they're most likely hiding in mainland mines.");
		qig.lineBreak();
		qig.add(player.theStolenCannonStatus, 8,
				"Thrain wants you to locate Zessfar, he will be located somewhere in comfort, such as an area filled with rocks.");
		qig.lineBreak();
		qig.add(player.theStolenCannonStatus, 9,
				"Now you have killed Zessfar, return back to the Thrain in the Dwarfen mine.");
		qig.lineBreak();
		qig.add(player.theStolenCannonStatus, 10, "You should return back to Nulodion.");
		qig.lineBreak();
		qig.add(player.theStolenCannonStatus, 11, "Nulodion has asked you to fetch him a Toolkit.");
		qig.lineBreak();
		qig.writeQuest(player);
	}

	public static boolean OnNpcClick(Player player, NPC npc, int option) {

		if (npc.type == 5880) {
			if (option == 1) {
				if (player.theStolenCannonStatus == 6) {
					DialogueHelper.sendDialogue(player, getDialogueById(68));
				}
				if (player.theStolenCannonStatus == 7) {
					if (player.theStolenCannonNpcsKilled.size() == 3) {
						if (player.getItems().playerHasItem(6, 1) && player.getItems().playerHasItem(8, 1)
								&& player.getItems().playerHasItem(10, 1)) {
							DialogueHelper.sendDialogue(player, getDialogueById(88));
						} else {
							DialogueHelper.sendDialogue(player, getDialogueById(85));
						}
					} else {
						DialogueHelper.sendDialogue(player, getDialogueById(82));
					}
				}
				if (player.theStolenCannonStatus == 8) {
					DialogueHelper.sendDialogue(player, getDialogueById(93));
				}
				if (player.theStolenCannonStatus == 9) {
					if (player.getItems().playerHasItem(12, 1)) {
						DialogueHelper.sendDialogue(player, getDialogueById(94));
					} else {
						DialogueHelper.sendDialogue(player, getDialogueById(97));
					}
				}
			}
			player.setDialogueOwner(TheStolenCannon.class);
			return true;
		}

		if (npc.type == DROGO_DWARF) {
			if (option == 1) {
				if (player.theStolenCannonStatus == 0) {
					if(!player.getData().getQuestProgress().containsKey("beginners-adventure") || player.getData().getQuestProgress().get("beginners-adventure") != 4) {
						player.getPA().sendNpcChat(DROGO_DWARF, "You must complete A Beginners Adventure before starting this quest");
						player.setDialogueOwner(CloseDialogue.class);
					} else {
						DialogueHelper.sendDialogue(player, getDialogueById(45));
					}
				} else if (player.theStolenCannonStatus == 1) {
					DialogueHelper.sendDialogue(player, getDialogueById(38));
				} else if (player.theStolenCannonStatus == 3) {
					if (player.getItems().playerHasItem(440, 1) && player.getItems().playerHasItem(438, 1)) {
						DialogueHelper.sendDialogue(player, getDialogueById(51));
					} else {
						DialogueHelper.sendDialogue(player, getDialogueById(63));
					}
				} else if (player.theStolenCannonStatus >= 4 && player.theStolenCannonStatus != COMPLETED_STAGE) {
					if (player.getItems().playerHasItem(1741, 5)) {
						DialogueHelper.sendDialogue(player, getDialogueById(59));
					} else {
						DialogueHelper.sendDialogue(player, getDialogueById(58));
					}
				}
				player.setDialogueOwner(TheStolenCannon.class);
				return true;
			}
		}

		if (npc.type == NULODION) {
			switch (option) {
				case 1: // Dialogue
					switch (player.theStolenCannonStatus) {
						case 0: // Starting the quest
							DialogueHelper.sendDialogue(player, getDialogueById(0));
							break;
						case 1: // Tell the player to go see his cousin
							DialogueHelper.sendDialogue(player, getDialogueById(37));
							break;
						case 2: // Returning back from seeing his cousin the first time
							DialogueHelper.sendDialogue(player, getDialogueById(46));
							break;
						case 3: // Ask the player if they have taken ores with them to see drago
							DialogueHelper.sendDialogue(player, getDialogueById(50));
							break;
						case 4:
							DialogueHelper.sendDialogue(player, getDialogueById(67));
							break;
						case 5: // tell the player to head down the thingy bob
							DialogueHelper.sendDialogue(player, getDialogueById(64));
							break;
						case 10:
							DialogueHelper.sendDialogue(player, getDialogueById(100));
							break;
						case 11:
							if (player.getItems().playerHasItem(1, 1)) {
								DialogueHelper.sendDialogue(player, getDialogueById(103));
							} else {
								DialogueHelper.sendDialogue(player, getDialogueById(102));
							}
						default:
							if (player.theStolenCannonStatus < COMPLETED_STAGE) {
								// i dont know what to put here
							} else {
								DialogueHelper.sendDialogue(player, getDialogueById(35));
							}
							break;
					}
					player.setDialogueOwner(TheStolenCannon.class);
					return true;
				case 2: // Trade
					if (player.theStolenCannonStatus >= COMPLETED_STAGE) {
						Shops.open(player, 79);
					} else {
						player.sendMessage("You must of completed The Stolen Cannon quest before you can shop here.");
					}
					return true;
				case 3: // Redeem cannon
					if (player.theStolenCannonStatus >= COMPLETED_STAGE) {
						// TODO redeeming
					} else {
						player.sendMessage("You must of completed The Stolen Cannon quest before you can redeem a cannon.");
					}
					return true;
			}
		}

		return false;
	}

	public static boolean onDialogueOption(Player player, int option) {
		if (player.getDialogueOwner() != null && player.getDialogueOwner().equals(TheStolenCannon.class)) {
			// Accepting the quest
			if (player.getCurrentDialogueId() == 28) {
				if (option == 1) {
					DialogueHelper.sendDialogue(player, getDialogueById(30));
					player.theStolenCannonStatus = 1;
					QuestHandler.sendQuests(player);
				} else {
					DialogueHelper.sendDialogue(player, getDialogueById(29));
				}
				return true;
			}
		}
		return false;
	}

	public static boolean onDialogueContinue(Player player) {
		if (player.getDialogueOwner() != null && player.getDialogueOwner().equals(TheStolenCannon.class)) {

			if (player.getNextDialogueId() == 0) {
				player.resetDialogue();
				return true;
			}

			// Speaking to drogo dwarf
			if (player.getNextDialogueId() == 44) {
				player.theStolenCannonStatus = 2;
			}

			// speaking back with Nulodion about his cousin
			if (player.getNextDialogueId() == 48) {
				player.theStolenCannonStatus = 3;
			}

			// where drogo tells him what he needs for the
			if (player.getNextDialogueId() == 57) {
				player.theStolenCannonStatus = 4;
			}

			// drogo taking the items for the gas mask
			if (player.getNextDialogueId() == 61) {
				if (player.getItems().playerHasItem(1741, 5)) {
					player.getItems().deleteItem(1741, 5);
				} else {
					return true;
				}
			}

			// drogo handing back the gas mask
			if (player.getNextDialogueId() == 62) {
				player.getItems().addItem(1506, 1);

				if (player.theStolenCannonStatus < 5)
					player.theStolenCannonStatus = 5;
			}

			if (player.getNextDialogueId() == 66) {
				player.theStolenCannonStatus = 6;
			}

			if (player.getNextDialogueId() == 81) {
				player.theStolenCannonStatus = 7;
			}

			if (player.getNextDialogueId() == 90) {
				if (player.getItems().playerHasItem(6, 1) && player.getItems().playerHasItem(8, 1)
						&& player.getItems().playerHasItem(10, 1)) {
					player.getItems().deleteItem(6, 1);
					player.getItems().deleteItem(8, 1);
					player.getItems().deleteItem(10, 1);
					player.theStolenCannonStatus = 8;
				} else {
					return true;
				}
			}

			if (player.getNextDialogueId() == 96) {
				if (player.getItems().playerHasItem(12, 1)) {
					player.getItems().deleteItem(12, 1);
					player.theStolenCannonStatus = 10;
				} else {
					return true;
				}
			}

			if (player.getNextDialogueId() == 102) {
				player.theStolenCannonStatus = 11;
			}

			if (player.getNextDialogueId() == 104) {
				if (player.getItems().playerHasItem(1, 1)) {
					player.getItems().deleteItem(1, 1);
				}
			}

			if (player.getNextDialogueId() == 105) {
				player.theStolenCannonStatus = 12;
				QuestHandler.sendQuests(player);
				player.questPoints += 2;
				QuestHandler.sendQuestPoints(player);
				QuestHandler.sendQuestRewardInterface(player, "The Stolen Cannon", "+2 Quest Points",
						"Can purchase Dwarf cannon from Nulodion", "Can now set up a Dwarf cannon.");
				return true;
			}

			DialogueHelper.sendDialogue(player, getDialogueById(player.getNextDialogueId()));
			return true;
		}
		return false;
	}

	public static void onNpcKilled(Player player, NPC npc) {
		if (npc.type == 1795 || npc.type == 1796 || npc.type == 1797) {
			if (player.theStolenCannonStatus == 7 && !player.theStolenCannonNpcsKilled.contains(npc.type)) {

				int drop = 6;
				if (npc.type == 1796) {
					drop = 8;
				} else if (npc.type == 1797) {
					drop = 10;
				}

				ItemDrops.createGroundItem(player, drop, npc.absX, npc.absY, 1, player.playerId, false);
				player.theStolenCannonNpcsKilled.add(npc.type);
			}
		}
		if (npc.type == 3220) {
			if (player.theStolenCannonStatus == 8) {
				ItemDrops.createGroundItem(player, 12, npc.absX, npc.absY, 1, player.playerId, false);
				player.theStolenCannonStatus = 9;
			}
		}
	}

	private static Dialogue getDialogueById(int id) {
		for (Dialogue dialogue : dialogues) {
			if (dialogue.getId() == id) {
				return dialogue;
			}
		}
		return null;
	}

	public static boolean inMineGasArea(Player player) {
		return player.theStolenCannonStatus > 0 && player.theStolenCannonStatus < COMPLETED_STAGE
				&& player.absX >= 2972 && player.absX <= 3084 && player.absY >= 9743 && player.absY <= 9851;
	}

}
