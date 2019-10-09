package com.zionscape.server.model.items;

import com.zionscape.server.Config;
import com.zionscape.server.cache.clientref.ObjectDef;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.content.minigames.christmas.ChristmasTree;
import com.zionscape.server.model.content.minigames.christmas.MarionetteMaking;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.skills.herblore.Herblore;
import com.zionscape.server.model.players.skills.Prayer;
import com.zionscape.server.model.players.skills.farming.Farming;
import com.zionscape.server.model.players.skills.firemaking.Firemaking;
import com.zionscape.server.model.players.skills.fletching.Fletching;
import com.zionscape.server.model.players.skills.hunter.Hunter;
import com.zionscape.server.model.players.skills.smithing.Smithing;

/**
 * @author Ryan / Lmctruck30
 */
public class UseItem {

    public static void ItemonItem(Player player, int itemUsed, int useWith) {

        if (Fletching.isFletchable(player, itemUsed, useWith)) {
            return;
        }

        if (Firemaking.itemOnItem(player, itemUsed, useWith)) {
            return;
        }

        if (player.getItems().getItemName(itemUsed).contains("(") && player.getItems().getItemName(useWith).contains("(")) {
            player.getPotMixing().potionCombination(itemUsed, useWith);
        }

        if (useWith == 6852) {
            MarionetteMaking.fillPuppetBox(player, itemUsed);
            return;
        }
        if (useWith == 6864) {
            MarionetteMaking.stringMarionette(player, itemUsed);
            return;
        }
        if (itemUsed == 1733 || useWith == 1733) {
            player.getCrafting().handleLeather(itemUsed, useWith);
        }
        if (itemUsed == 1755 || useWith == 1755) {
            player.getCrafting().handleChisel(itemUsed, useWith);
        }
        if ((itemUsed == 1540 && useWith == 11286) || (itemUsed == 11286 && useWith == 1540)) {
            if (player.level[PlayerConstants.SMITHING] >= 95) {
                player.getItems().deleteItem(1540, player.getItems().getItemSlot(1540), 1);
                player.getItems().deleteItem(11286, player.getItems().getItemSlot(11286), 1);
                player.getItems().addItem(11284, 1);
                player.sendMessage("You combine the two materials to create a dragonfire shield.");
                player.getPA().addSkillXP(500 * Config.SMITHING_EXPERIENCE, PlayerConstants.SMITHING);

                Achievements.progressMade(player, Achievements.Types.SMITH_A_DFS);
            } else {
                player.sendMessage("You need a smithing level of 95 to create a dragonfire shield.");
            }
        }
        if (Herblore.useVial(player, itemUsed, useWith)) {
            return;
        }
        if (Herblore.invokeItemOnItem(player, itemUsed, useWith)) {
            return;
        }
        if (itemUsed == 9142 && useWith == 9190 || itemUsed == 9190 && useWith == 9142) {
            if (player.level[PlayerConstants.FLETCHING] >= 58) {
                int boltsMade = player.getItems().getItemAmount(itemUsed) > player.getItems().getItemAmount(useWith) ? player
                        .getItems().getItemAmount(useWith) : player.getItems().getItemAmount(itemUsed);
                player.getItems().deleteItem(useWith, player.getItems().getItemSlot(useWith), boltsMade);
                player.getItems().deleteItem(itemUsed, player.getItems().getItemSlot(itemUsed), boltsMade);
                player.getItems().addItem(9241, boltsMade);
                player.getPA().addSkillXP(boltsMade * 6, PlayerConstants.FLETCHING);
            } else {
                player.sendMessage("You need a fletching level of 58 to fletch this item.");
            }
        }
        if (itemUsed == 4151 && useWith == 21369 || itemUsed == 21369 && useWith == 4151) {
            if (player.level[PlayerConstants.SLAYER] >= 80 && player.level[PlayerConstants.ATTACK] >= 75) {
                if (player.getItems().playerHasItem(4151, 1) && player.getItems().playerHasItem(21369)) {
                    player.getItems().deleteItem(4151, 1);
                    player.getItems().deleteItem(21369, 1);
                    player.getItems().addItem(21371, 1);
                    player.sendMessage("You have created an abyssal vine whip!");


                    Achievements.progressMade(player, Achievements.Types.CREATE_A_VINE_WHIP);
                }
            } else {
                player.sendMessage("You need a slayer level of 80 and an attack level of 75 to create this item.");
            }
        }
        if (itemUsed == 9143 && useWith == 9191 || itemUsed == 9191 && useWith == 9143) {
            if (player.level[PlayerConstants.FLETCHING] >= 63) {
                int boltsMade = player.getItems().getItemAmount(itemUsed) > player.getItems().getItemAmount(useWith) ? player
                        .getItems().getItemAmount(useWith) : player.getItems().getItemAmount(itemUsed);
                player.getItems().deleteItem(useWith, player.getItems().getItemSlot(useWith), boltsMade);
                player.getItems().deleteItem(itemUsed, player.getItems().getItemSlot(itemUsed), boltsMade);
                player.getItems().addItem(9242, boltsMade);
                player.getPA().addSkillXP(boltsMade * 7, PlayerConstants.FLETCHING);
            } else {
                player.sendMessage("You need a fletching level of 63 to fletch this item.");
            }
        }
        if (itemUsed == 9143 && useWith == 9192 || itemUsed == 9192 && useWith == 9143) {
            if (player.level[PlayerConstants.FLETCHING] >= 65) {
                int boltsMade = player.getItems().getItemAmount(itemUsed) > player.getItems().getItemAmount(useWith) ? player
                        .getItems().getItemAmount(useWith) : player.getItems().getItemAmount(itemUsed);
                player.getItems().deleteItem(useWith, player.getItems().getItemSlot(useWith), boltsMade);
                player.getItems().deleteItem(itemUsed, player.getItems().getItemSlot(itemUsed), boltsMade);
                player.getItems().addItem(9243, boltsMade);
                player.getPA().addSkillXP(boltsMade * 7, PlayerConstants.FLETCHING);
            } else {
                player.sendMessage("You need a fletching level of 65 to fletch this item.");
            }
        }
        if (itemUsed == 9144 && useWith == 9193 || itemUsed == 9193 && useWith == 9144) {
            if (player.level[PlayerConstants.FLETCHING] >= 71) {
                int boltsMade = player.getItems().getItemAmount(itemUsed) > player.getItems().getItemAmount(useWith) ? player
                        .getItems().getItemAmount(useWith) : player.getItems().getItemAmount(itemUsed);
                player.getItems().deleteItem(useWith, player.getItems().getItemSlot(useWith), boltsMade);
                player.getItems().deleteItem(itemUsed, player.getItems().getItemSlot(itemUsed), boltsMade);
                player.getItems().addItem(9244, boltsMade);
                player.getPA().addSkillXP(boltsMade * 10, PlayerConstants.FLETCHING);
            } else {
                player.sendMessage("You need a fletching level of 71 to fletch this item.");
            }
        }
        if (itemUsed == 9144 && useWith == 9194 || itemUsed == 9194 && useWith == 9144) {
            if (player.level[PlayerConstants.FLETCHING] >= 58) {
                int boltsMade = player.getItems().getItemAmount(itemUsed) > player.getItems().getItemAmount(useWith) ? player
                        .getItems().getItemAmount(useWith) : player.getItems().getItemAmount(itemUsed);
                player.getItems().deleteItem(useWith, player.getItems().getItemSlot(useWith), boltsMade);
                player.getItems().deleteItem(itemUsed, player.getItems().getItemSlot(itemUsed), boltsMade);
                player.getItems().addItem(9245, boltsMade);
                player.getPA().addSkillXP(boltsMade * 13, PlayerConstants.FLETCHING);
            } else {
                player.sendMessage("You need a fletching level of 58 to fletch this item.");
            }
        }
        if (itemUsed == 1601 && useWith == 1755 || itemUsed == 1755 && useWith == 1601) {
            if (player.level[PlayerConstants.FLETCHING] >= 63) {
                player.getItems().deleteItem(1601, player.getItems().getItemSlot(1601), 1);
                player.getItems().addItem(9192, 15);
                player.getPA().addSkillXP(8, PlayerConstants.FLETCHING);
            } else {
                player.sendMessage("You need a fletching level of 63 to fletch this item.");
            }
        }
        if (itemUsed == 1607 && useWith == 1755 || itemUsed == 1755 && useWith == 1607) {
            if (player.level[PlayerConstants.FLETCHING] >= 65) {
                player.getItems().deleteItem(1607, player.getItems().getItemSlot(1607), 1);
                player.getItems().addItem(9189, 15);
                player.getPA().addSkillXP(8, PlayerConstants.FLETCHING);
            } else {
                player.sendMessage("You need a fletching level of 65 to fletch this item.");
            }
        }
        if (itemUsed == 1605 && useWith == 1755 || itemUsed == 1755 && useWith == 1605) {
            if (player.level[PlayerConstants.FLETCHING] >= 71) {
                player.getItems().deleteItem(1605, player.getItems().getItemSlot(1605), 1);
                player.getItems().addItem(9190, 15);
                player.getPA().addSkillXP(8, PlayerConstants.FLETCHING);
            } else {
                player.sendMessage("You need a fletching level of 71 to fletch this item.");
            }
        }
        if (itemUsed == 1603 && useWith == 1755 || itemUsed == 1755 && useWith == 1603) {
            if (player.level[PlayerConstants.FLETCHING] >= 73) {
                player.getItems().deleteItem(1603, player.getItems().getItemSlot(1603), 1);
                player.getItems().addItem(9191, 15);
                player.getPA().addSkillXP(8, PlayerConstants.FLETCHING);
            } else {
                player.sendMessage("You need a fletching level of 73 to fletch this item.");
            }
        }
        if (itemUsed == 1615 && useWith == 1755 || itemUsed == 1755 && useWith == 1615) {
            if (player.level[PlayerConstants.FLETCHING] >= 73) {
                player.getItems().deleteItem(1615, player.getItems().getItemSlot(1615), 1);
                player.getItems().addItem(9193, 15);
                player.getPA().addSkillXP(8, PlayerConstants.FLETCHING);
            } else {
                player.sendMessage("You need a fletching level of 73 to fletch this item.");
            }
        }
        if (itemUsed >= 11710 && itemUsed <= 11714 && useWith >= 11710 && useWith <= 11714) {
            if (player.getItems().hasAllShards()) {
                player.getItems().makeBlade();
            }
        }
        if (itemUsed == 13263 && useWith == 15488 || itemUsed == 13263 && useWith == 15490 || itemUsed == 15490
                && useWith == 15488) {
            if (player.getItems().hasHelmParts()) {
                player.getItems().makeFullSlayerHelm();
            }
        }
        if (itemUsed == 19333 && useWith == 6585 || useWith == 19333 && itemUsed == 6585) {
            player.getItems().makeFury();
        }
        if (itemUsed == 13734 && useWith == 13754 || itemUsed == 13754 && useWith == 13734) {
            player.getItems().makeBlessed();
        }
        if (itemUsed == 13736 && useWith == 13748 || itemUsed == 13748 && useWith == 13736) {
            player.getItems().makeDivine();
        }
        if (itemUsed == 13736 && useWith == 13752 || itemUsed == 13748 && useWith == 13752) {
            player.getItems().makeSpectral();
        }
        if (itemUsed == 13736 && useWith == 13746 || itemUsed == 13746 && useWith == 13736) {
            player.getItems().makeArcane();
        }
        if (itemUsed == 13736 && useWith == 13750 || itemUsed == 13750 && useWith == 13736) {
            player.getItems().makeElysian();
        }
        if (itemUsed == 2368 && useWith == 2366 || itemUsed == 2366 && useWith == 2368) {
            player.getItems().deleteItem(2368, player.getItems().getItemSlot(2368), 1);
            player.getItems().deleteItem(2366, player.getItems().getItemSlot(2366), 1);
            player.getItems().addItem(1187, 1);
        }
        if (itemUsed == 985 && useWith == 987 || itemUsed == 987 && useWith == 985) {
            player.getItems().deleteItem(985, player.getItems().getItemSlot(985), 1);
            player.getItems().deleteItem(987, player.getItems().getItemSlot(987), 1);
            player.getItems().addItem(989, 1);
        }
        if (player.getItems().isHilt(itemUsed) || player.getItems().isHilt(useWith)) {
            int hilt = player.getItems().isHilt(itemUsed) ? itemUsed : useWith;
            int blade = player.getItems().isHilt(itemUsed) ? useWith : itemUsed;
            if (blade == 11690) {
                player.getItems().makeGodsword(hilt);
            }
        }
        if (itemUsed == 1755 && useWith == 19592 || itemUsed == 11854 && useWith == 19592) {
            player.getItems().deleteItem(19592, 1);
            player.getItems().addItem(11724, 1);
            player.getItems().addItem(11726, 1);
            player.getItems().addItem(11728, 1);
            player.sendMessage("You get a Bandos armour set!");
        }
        if (itemUsed == 1755 && useWith == 19588 || itemUsed == 19588 && useWith == 19592) {
            player.getItems().deleteItem(19588, 1);
            player.getItems().addItem(11718, 1);
            player.getItems().addItem(11720, 1);
            player.getItems().addItem(11722, 1);
            player.sendMessage("You get an Armadyl armour set!");
        }
        if (itemUsed == 1755 && useWith == 19596 || itemUsed == 19596 && useWith == 19592) {
            player.getItems().deleteItem(19596, 1);
            player.getItems().addItem(20159, 1);
            player.getItems().addItem(20163, 1);
            player.getItems().addItem(20167, 1);
            player.sendMessage("You get a Virtus armour set!");
        }
        if (itemUsed == 1755 && useWith == 11938 || itemUsed == 11938 && useWith == 19592) {
            player.getItems().deleteItem(11938, 1);
            player.getItems().addItem(3481, 1);
            player.getItems().addItem(3483, 1);
            player.getItems().addItem(3486, 1);
            player.getItems().addItem(3488, 1);
            player.sendMessage("You get a Gilded armour set!");
        }
        if (itemUsed == 1755 && useWith == 19598 || itemUsed == 19598 && useWith == 19592) {
            player.getItems().deleteItem(19598, 1);
            player.getItems().addItem(20135, 1);
            player.getItems().addItem(20139, 1);
            player.getItems().addItem(20143, 1);
            player.sendMessage("You get a Torva armour set!");
        }
        switch (itemUsed) {
            default:
                if (player.rights == 3) {
                    System.out.println("Player used Item id: " + itemUsed + " with Item id: " + useWith);
                }
                break;
        }
    }

    public static void ItemonNpc(Player c, int itemId, int npcId, int slot) {
        switch (itemId) {

            case 494: // items on banker
            case 495:
            case 496:
            case 498:
            case 499:
                if (c.ironman) {
                    if (ItemUtility.isNote(itemId)) {
                        int unoted = ItemUtility.getUnotedId(itemId);

                        // just to double check
                        if (unoted == -1) {
                            return;
                        }

                        c.setAttribute("ironman_unoting", true);
                        c.getPA().sendX(itemId);
                    } else {

                        int noted = ItemUtility.getNotedId(itemId);

                        if (noted == -1) {
                            c.sendMessage("You cannot note this item.");
                            return;
                        }

                        int amount = c.getItems().getItemAmount(itemId);

                        c.getItems().deleteItem(itemId, amount);
                        c.getItems().addItem(noted, amount);
                    }
                }
                break;

            default:
                if (c.rights == 3) {
                    System.out.println("Player used Item id: " + itemId + " with Npc id: " + npcId + " With Slot : " + slot);
                }
                break;
        }
    }

    public static void ItemonObject(Player c, int objectID, int objectX, int objectY, int itemId) {
        if (!c.getItems().playerHasItem(itemId, 1)) {
            return;
        }

        if (Farming.itemOnObject(c, objectID, Location.create(objectX, objectY, c.heightLevel), itemId)) {
            return;
        }

        if (Hunter.itemOnObject(c, objectID, Location.create(objectX, objectY, c.heightLevel), itemId)) {
            return;
        }

        if (Smithing.itemOnObject(c, objectID, Location.create(objectX, objectY, c.heightLevel), itemId)) {
            return;
        }

        if (c.ironman) {
            ObjectDef def = ObjectDef.forID(objectID);
            if (objectID == 2879 || def != null && def.name != null && def.name.equalsIgnoreCase("bank booth")) {

                if (ItemUtility.isNote(itemId)) {
                    int unoted = ItemUtility.getUnotedId(itemId);

                    // just to double check
                    if (unoted == -1) {
                        return;
                    }

                    c.setAttribute("ironman_unoting", true);
                    c.getPA().sendX(itemId);
                } else {

                    int noted = ItemUtility.getNotedId(itemId);

                    if (noted == -1) {
                        c.sendMessage("You cannot note this item.");
                        return;
                    }

                    int amount = c.getItems().getItemAmount(itemId);

                    c.getItems().deleteItem(itemId, amount);
                    c.getItems().addItem(noted, amount);
                }
            }
        }

		/*
         * Cooking on fire
		 */
        if (objectID == 2732 || objectID == 21302 || objectID == 2728 || objectID == 12269 || objectID == 11269) {
            if (c.getCooking().isCookable(itemId)) {
                c.getCooking().cookingItem = itemId;
                if (objectID == 2728) {
                    c.getCooking().useRange = true;
                } else {
                    c.getCooking().useRange = false;
                }
                c.getPA().sendFrame246(13716, 200, itemId);
                c.getPA().sendFrame164(1743);
            }
        }
        switch (objectID) {
            case 10652:
            case 10653:
            case 10654:
            case 10658:
            case 10659:
            case 10660:
                ChristmasTree.decorateTree(c);
                break;
            case 10677:
                c.color = "blue";
                MarionetteMaking.buildPuppet(c);
                break;
            case 10676:
                c.color = "green";
                MarionetteMaking.buildPuppet(c);
                break;
            case 10678:
                c.color = "red";
                MarionetteMaking.buildPuppet(c);
                break;
            case 2783:
            case 2782:
                c.getSmithingInt().showSmithInterface(itemId);
                break;
            case 2728:
            case 409:
            case 10638:
                if (c.getPrayer().isBone(itemId)) {
                    c.getPA().sendOptions("1", "10", "X", "All");
                    c.setCurrentDialogueId(itemId);
                    c.setDialogueOwner(Prayer.Dialogue.NORMAL_ALTER);
                }
                break;
            case 61:
                if (c.getPrayer().isBone(itemId)) {
                    c.getPA().sendOptions("1", "10", "X", "All");
                    c.setCurrentDialogueId(itemId);
                    c.setDialogueOwner(Prayer.Dialogue.DONATOR_ALTER);
                }
                break;
            default:
                if (c.rights == 3) {
                    System.out.println("Player At GameObject id: " + objectID + " with Item id: " + itemId);
                }
                break;
        }
    }
}
