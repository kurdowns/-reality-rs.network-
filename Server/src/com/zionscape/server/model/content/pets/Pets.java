package com.zionscape.server.model.content.pets;

import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.skills.summoning.Summoning;

public class Pets {

    private static final int[][] ITEM_IDS_TO_NPC_ID = {
            {25055, 2055}, // Pet chaos elemental
            {25056, 6628}, // Pet dagannoth supreme
            {25057, 6629}, // Pet dagannoth prime
            {25058, 6630}, // Pet dagannoth rex
            {25059, 6639}, // Pet smoke devil
            {25060, 6631}, // Pet kree'arra
            {25061, 6632}, // Pet general graardor
            {25062, 6633}, // Pet zilyana
            {25063, 6634}, // Pet k'ril tsutsaroth
            {25064, 6636}, // Prince black dragon
            {25065, 6637}, // Kalphite princess
            {25066, 6640}, // Pet Kraken
            {25067, 6651}, // Baby mole
            {25068, 6642}, // Pet penance queen
            {25069, 318}, // Pet dark core
            {25070, 2127}, // Pet snakeling
            {25071, 5883}, // Abyssal orphan
            {25072, 497}, // Callisto cub
            {25073, 964}, // Hell puppy
            {25074, 5892}, // Tzrek-jad
            {25075, 5547}, // Scorpia's offspring
            {25076, 5557}, // Venenatis spiderline
            {25077, 5559}, // Vet'ion jr.
            {25078, 6718}, // Baby chinchompa
            {25079, 6719}, // Baby chinchompa
            {25080, 6720}, // Baby chinchompa
            {25081, 6721}, // Baby chinchompa
            {25082, 6724}, // Beaver
            {25083, 6722}, // heron
            {25084, 6723}, // rock golem
            {25619,14388},
            {25620,14389},
            {25621,14390},
            {25622,14391},
            {25623,14392},
            {25624,14393},
            {25625,14394},
            {25626,14395},
            {25627,14396},
            {25628,14397},
            {25629,14398},
            {25630,14399},
            {25631,14400},
            {25632,14401},
            {25633,14402},
            {25634,14403},
            {25635,14404},
            {25636, 14405},
            {964, 2862}, // death
    };

    public static void changeRiftGuardianColor(Player player, int item) {
        if(player.getPet() != null && player.getPet().type >= 14391 && player.getPet().type <= 14404) {
            for(int i = 0; i < ITEM_IDS_TO_NPC_ID.length; i++) {
                if (ITEM_IDS_TO_NPC_ID[i][0] == item && player.getPet().type != ITEM_IDS_TO_NPC_ID[i][1]) {
                    player.getPet().requestTransform(ITEM_IDS_TO_NPC_ID[i][1]);
                }
            }
        }
    }

    public static boolean onPlayerDroppedItem(Player player, Item item) {

        for(int i = 0; i < ITEM_IDS_TO_NPC_ID.length; i++) {
            if(ITEM_IDS_TO_NPC_ID[i][0] != item.getId()) {
                continue;
            }

            if(player.getFamiliar() != null) {
                player.sendMessage("You already have a familiar out.");
                return true;
            }
            if(player.getPet() != null) {
                player.sendMessage("You already have a pet out.");
                return true;
            }

            NPC npc = NPCHandler.spawnNpc(ITEM_IDS_TO_NPC_ID[i][1], player.absX, player.absY, player.heightLevel, 0, 0, 0, 0, 0);
            npc.setOwnerId(player.playerId);

            player.setPet(npc);

            player.getItems().deleteItem(item.getId(), 1);
            return true;
        }

        return false;
    }

    public static void process(Player player) {

        // player is teleporting
        if (player.teleTimer > 0) {
            return;
        }

        if (player.getPet() != null) {
            int distance = (int) player.getLocation().getDistance(player.getPet().getLocation());
            if (!player.attributeExists("pet_being_respawned") && distance >= 15) {

                int petId = player.getPet().type;

                Summoning.destroyFamiliar(player.getPet());

                player.setAttribute("pet_being_respawned", true);

                GameCycleTaskHandler.addEvent(player, new GameCycleTask() {

                    @Override
                    public void execute(GameCycleTaskContainer container) {
                        Player player = container.getPlayer();

                        NPC npc = NPCHandler.spawnNpc(petId, player.absX, player.absY, player.heightLevel, 0, 0, 0, 0, 0);
                        npc.setOwnerId(player.playerId);
                        player.setPet(npc);

                        player.removeAttribute("pet_being_respawned");

                        container.stop();
                    }
                }, 2);
            } else if (distance != 1 && !player.getPet().underAttack && player.getPet().slot > 0) {
                NPCHandler.follow(player.getPet(), player);
            }
        }
    }

    public static boolean onNpcClick(Player player, NPC npc, int option) {
        if(option == 1) {
            boolean found = false;
            for(int i = 0; i < ITEM_IDS_TO_NPC_ID.length; i++) {
                if(ITEM_IDS_TO_NPC_ID[i][1] == npc.type) {
                    found = true;
                    break;
                }
            }
            if(!found) {
                return false;
            }
            if(player.getPet() != null && player.getPet().equals(npc)) {
                player.sendMessage("The pet looks at you funny.");
            } else {
                player.sendMessage("This is not your pet to interact with.");
            }
            return true;
        }
        if(option == 2 && player.getPet() != null && player.getPet().equals(npc)) {

            if(player.getItems().freeInventorySlots() == 0) {
                player.sendMessage("You do not have enough inventory space.");
                return true;
            }

            for(int i = 0; i < ITEM_IDS_TO_NPC_ID.length; i++) {
                if (ITEM_IDS_TO_NPC_ID[i][1] != player.getPet().type) {
                    continue;
                }
                player.getItems().addItem(ITEM_IDS_TO_NPC_ID[i][0], 1);
                break;
            }

            Summoning.destroyFamiliar(npc);
            player.setPet(null);
            return true;
        }

        return false;
    }

    public static void onPlayerLoggedOut(Player player) {
        if(player.getPet() != null) {
            player.getData().petType = player.getPet().type;
            Summoning.destroyFamiliar(player.getPet());
        }
    }

    public static void onPlayerLoggedIn(Player player) {
        if(player.getData().petType > 0) {
            NPC npc = NPCHandler.spawnNpc(player.getData().petType, player.absX, player.absY, player.heightLevel, 0, 0, 0, 0, 0);
            npc.setOwnerId(player.playerId);
            player.setPet(npc);

            player.getData().petType = 0;
        }
    }

}
