package com.zionscape.server.plugin.impl.minigames;

import com.zionscape.server.events.Listener;
import com.zionscape.server.events.impl.*;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Area;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.dialogue.CloseDialogue;
import com.zionscape.server.plugin.Plugin;
import com.zionscape.server.util.Misc;
import com.zionscape.server.world.ItemDrops;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WarriorsGuild implements Plugin, Listener {

    private static final int SHANOMI = 4290;
    private static final int KAMFREENA = 4289;
    private static final Area mainArea = new Area(2848, 3541, 2876, 3556, 2);
    private static final Area smallArea = new Area(2866, 2534, 2876, 3540,  2);
    private static final int TOKEN_ID = 8851;
    private static final int[] DEFENDERS = { 8844, 8845, 8846, 8847, 8848, 8849, 8850, 20072};

    @Override
    public void onNpcDied(NpcDiedEvent event) {
        Player player = event.getPlayer();

        if(event.getNpc().type == 4292 || event.getNpc().type == 4291) {
            for(int defender : DEFENDERS) {
                if(player.getItems().playerHasItemGlobally(defender)) {
                    continue;
                }

                // rs uses 50 for all defenders and 100 for dragon
                if(Misc.random(defender == 20072 ? 18 : 9) == 0) {
                    ItemDrops.createGroundItem(player, defender, event.getNpc().getX(), event.getNpc().getY(), event.getNpc().heightLevel, 1, player.playerId, false);
                }
                break;
            }
        }
    }

    @Override
    public void onDialogueContinue(DialogueContinueEvent event) {
        Player player = event.getPlayer();

        if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(WarriorsGuild.class)) {
            return;
        }

        event.setHandled(true);

        switch (player.getCurrentDialogueId()) {
            case 1:
                player.getPA().sendPlayerChat(SHANOMI, "Ok, so I can only use play full plate armour, no trimmed or other stuff.");
                player.setDialogueOwner(CloseDialogue.class);
                break;
        }
    }

    @Override
    public void onObjectClicked(ClickObjectEvent event) {
        Player player = event.getPlayer();

        if(event.getObjectLocation().equals(2847, 3541) && event.getObjectId() == 15644 || event.getObjectLocation().equals(2847, 3540) && event.getObjectId() == 15641) {
            event.setHandled(true);
            if(player.getX() <= 2846) {
                if(!player.getItems().playerHasItem(TOKEN_ID, 100)) {
                    player.getPA().sendNpcChat(KAMFREENA, "You don't have enough Warrior Guild Tokens to enter the cyclopes enclosure yet, collect at least 100 then come back.");
                    player.setDialogueOwner(CloseDialogue.class);
                    return;
                }
                player.getPA().movePlayer(2848, 3541, 2);
            } else {
                player.getPA().movePlayer(2846, 3541, 2);
                player.removeAttribute("warriors_guild_warned");
            }
        }
    }

    @Override
    public void onPlayerProcess(PlayerProcessEvent event) {
        Player player = event.getPlayer();

        if(!inWarriorsGuild(player)) {
            return;
        }

        player.getData().warriorsGuildRoomTicks++;

        if(!player.getItems().playerHasItem(TOKEN_ID, 10)) {
            if(player.attributeExists("warriors_guild_warned") && player.getData().warriorsGuildRoomTicks >= 50) {
                player.getPA().sendNpcChat(KAMFREENA, "Next time, please leave as soon as your time is up.");
                player.sendMessage("Next time, please leave as soon as your time is up.");
                player.getPA().movePlayer(2846, 3541, 2);
                player.removeAttribute("warriors_guild_warned");
                player.getData().warriorsGuildRoomTicks = 0;
            } else {
                player.getPA().sendNpcChat(KAMFREENA, "You do not have any tokens you should not be in the enclosure!");
                player.sendMessage("You do not have any tokens you should not be in the enclosure!");
                player.getPA().movePlayer(2846, 3541, 2);
                player.removeAttribute("warriors_guild_warned");
                player.getData().warriorsGuildRoomTicks = 0;
            }
        } else if(player.getData().warriorsGuildRoomTicks >= 100) {
            player.getItems().deleteItem(TOKEN_ID, 10);
            player.sendMessage("10 of your tokens crumble away.");

            if(!player.getItems().playerHasItem(TOKEN_ID, 10)) {
                player.getPA().sendNpcChat(KAMFREENA, "Your time is up, please make your way to the exit.");
                player.sendMessage("Your time is up, please make your way to the exit.");
                player.setAttribute("warriors_guild_warned", true);
            }

            player.getData().warriorsGuildRoomTicks = 0;
        }

    }

    @Override
    public void onItemOnObject(ItemOnObjectEvent event) {

        Player player = event.getPlayer();

        if(event.getObjectId() == 15621) {
            event.setHandled(true);

            Optional<Armour> optional = Arrays.stream(Armour.values()).filter(x -> x.items.contains(event.getItem())).findAny();

            if(!optional.isPresent()) {
                player.getPA().sendNpcChat(SHANOMI, "Plain full plate armour in those machines only you may use. What other items used, bad things happen, yes!");
                player.setDialogueOwner(WarriorsGuild.class);
                player.setCurrentDialogueId(1);
                return;
            }

            if(player.isBusy()) {
                return;
            }

            Armour armour = optional.get();

            for(int item : armour.items) {
                if(!player.getItems().playerHasItem(item)) {
                    player.getPA().sendNpcChat(SHANOMI, "You need a platebody, legs and full helm of the same type to activate the armour animator.");
                    player.setDialogueOwner(CloseDialogue.class);
                    return;
                }
            }

            for(NPC npc : NPCHandler.npcs) {
                if(npc == null) {
                    continue;
                }
                if(!npc.isDead && npc.spawnedBy == player.playerId && npc.type >= 4278 && npc.type <= 4284) {
                    player.getPA().sendNpcChat(SHANOMI, "You already have activated an animator, kill it first before spawning another.");
                    player.setDialogueOwner(CloseDialogue.class);
                    return;
                }
            }

            for(int item : armour.items) {
                player.getItems().deleteItem(item, 1);
            }

            player.setBusy(true);
            player.getPA().sendStatement("You place your armour on the platform where it disappears...");
            player.setDialogueOwner(CloseDialogue.class);
            player.startAnimation(827);
            player.blockWalking = true;

            GameCycleTaskHandler.addEvent(player, container -> {
                player.getPA().sendStatement("The animator hums, something appears to be working. You stand back...");
                player.setDialogueOwner(CloseDialogue.class);
                player.getPA().walkTo(0, 2);
                NPC npc = NPCHandler.spawnNpc(player, armour.npcId, player.getX(), player.getY(), player.getHeightLevel(), 0, armour.hp, armour.maxHit, armour.stats, armour.stats, true, false);
                npc.forceChat("I'm ALIVE!");
                container.stop();

                GameCycleTaskHandler.addEvent(player, container1 -> {
                    player.blockWalking = false;
                    player.setBusy(false);
                    container1.stop();
                }, 1);
            }, 6);
        }
    }

    private static boolean inWarriorsGuild(Player player) {
        if(mainArea.inArea(player.getLocation()) || smallArea.inArea(player.getLocation())) {
            return true;
        }
        return false;
    }

    private enum Armour {
        BRONZE(4278, 10, 60, 2, Arrays.asList(1075, 1117, 1155)),
        IRON(4279, 20, 70, 3, Arrays.asList(1067, 1115, 1153)),
        STEEL(4280, 40, 90, 5, Arrays.asList(1069, 1119, 1157)),
        BLACK(4281, 60, 110, 7, Arrays.asList(1077, 1125, 1165)),
        MITHRIL(4282, 80, 130, 10, Arrays.asList(1071, 1121, 1159)),
        ADAMANT(4283, 99, 149, 12, Arrays.asList(1073, 1123, 1161)),
        RUNE(4284, 120, 170, 14, Arrays.asList(1079, 1127, 1163));

        private final int npcId;
        private final int hp;
        private final int stats;
        private final int maxHit;
        private final List<Integer> items;

        Armour(int npcId, int hp, int stats, int maxHit, List<Integer> items) {
            this.npcId = npcId;
            this.hp = hp;
            this.stats = stats;
            this.maxHit = maxHit;
            this.items = items;
        }
    }

}