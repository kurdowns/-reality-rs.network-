package com.zionscape.server.model.npcs.drops;

import com.headius.options.IntegerOption;
import com.zionscape.server.model.items.ItemAssistant;
import com.zionscape.server.model.items.ItemDefinition;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.npcs.NPCHelper;
import com.zionscape.server.model.npcs.NpcDefinition;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.util.DatabaseUtil;
import com.zionscape.server.util.Misc;
import jnr.ffi.annotations.In;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class NPCDropsHandler {

    private static final Logger LOGGER = Logger.getLogger(NPCDropsHandler.class.getName());
    private static final List<DropContainer> dropContainers = new ArrayList<>();
    private static final Random random = new Random();

    public static void loadDrops() throws Exception {
        long start = System.currentTimeMillis();

        try (Connection connection = DatabaseUtil.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM npc_drops")) {
                try (ResultSet results = ps.executeQuery()) {
                    while (results.next()) {

                        //Drop npcDrop = drop.getIds() == null ? new Drop(drop.getId()) : new Drop(drop.getIds());
                        Drop npcDrop = new Drop(results.getInt("item_id"));

                        if (results.getInt("fixed_amount") > 0) {
                            npcDrop.setAmount(results.getInt("fixed_amount"));
                        } else {
                            npcDrop.setAmount(results.getInt("min_amount"), results.getInt("max_amount"));
                        }

                        Rate rate = null;

                        switch (results.getString("rate").toUpperCase()) {
                            case "ALWAYS":
                                rate = Rates.ALWAYS;
                                break;
                            case "COMMON":
                                rate = Rates.COMMON;
                                break;
                            case "OFTEN":
                                rate = Rates.OFTEN;
                                break;
                            case "UNCOMMON":
                                rate = Rates.UNCOMMON;
                                break;
                            case "SEMI_RARE":
                                rate = Rates.SEMI_RARE;
                                break;
                            case "RARE":
                                rate = Rates.RARE;
                                break;
                            case "RARER":
                                rate = Rates.RARER;
                                break;
                            case "VERY_RARE":
                                rate = Rates.VERY_RARE;
                                break;
                            case "SUPER_RARE":
                                rate = Rates.SUPER_RARE;
                                break;
                            case "IMPOSSIBLE":
                                rate = Rates.IMPOSSIBLE;
                                break;
                            default:
                                rate = new Rate(Integer.parseInt(results.getString("rate")));
                                break;
                        }

                        if (rate == null) {
                            throw new IllegalStateException("drop issue " + results.getInt("id"));
                        }

                        npcDrop.setRate(rate);

                        DropContainer container = getDropContainer(results.getInt("npc_id"));
                        if (container == null) {
                            container = new DropContainer(results.getInt("npc_id"));
                            dropContainers.add(container);
                        }

                        container.addDrop(npcDrop);
                    }
                }
            }

            LOGGER.info("Loaded npc drops (in " + (System.currentTimeMillis() - start) + "ms).");
        }
    }

    private static DropContainer getDropContainer(int npcId) {
        return dropContainers.stream().filter(x -> x.getNpcId() == npcId).findFirst().orElse(null);
    }


    private static Map<Integer, Integer> globalKills = new HashMap<>();

    public static List<Drop> getRandomDrops(Player player, int npcId, int amount) {
        List<Drop> drops = new ArrayList<>();

        DropContainer container = getDropContainer(npcId);
        if (container == null) {
            return drops;
        }

        // always drops
        if (container.getAlwaysDrops().size() > 0) {
            for (Drop drop : container.getAlwaysDrops()) {

                if (drop.getItemIds() == null) {
                    drops.add(drop);
                } else {
                    Drop tempDrop = new Drop(drop.getItemIds()[Misc.random(drop.getItemIds().length - 1)]);
                    tempDrop.setRate(drop.getRate());

                    if (drop.getFixedAmount() > 0) {
                        tempDrop.setAmount(drop.getFixedAmount());
                    } else {
                        tempDrop.setAmount(drop.getMinAmount(), drop.getMaxAmount());
                    }
                    drops.add(tempDrop);
                }
            }
        }


        // check if only common drops exist
        if (container.getRareDrops().size() == 0 && container.getCommonDrops().size() > 0) {
            List<Drop> common = container.getCommonDrops().stream().filter(x -> x.getRate().getB() == 0).collect(Collectors.toList());

            // THEY'RE all Rates.COMMON
            if (common.size() == container.getCommonDrops().size()) {
                drops.add(getDrop(common.get(Misc.random(common.size() - 1))));
                return drops;
            }
        }

        int amountDropped = 0;

        // rare drops
        if (container.getRareDrops().size() > 0) {

            Map<Integer, Integer> kills;
            if (player == null) {
                kills = globalKills;
            } else {
                if(!player.npcDrops.containsKey(npcId)) {
                    player.npcDrops.put(npcId, new HashMap<>());
                }
                kills = player.npcDrops.get(npcId);
            }

            List<Integer> rates = new ArrayList<>();
            for (Drop drop : container.getRareDrops()) {
                if (!kills.containsKey(drop.getRate().getB())) {
                    kills.put(drop.getRate().getB(), 0);
                } else {
                    if(!rates.contains(drop.getRate().getB())) {
                        kills.put(drop.getRate().getB(), kills.get(drop.getRate().getB()) + 1);
                        rates.add(drop.getRate().getB());
                    }
                }
            }

            for (Drop drop : container.getRareDrops()) {
                int r = drop.getRate().getB();

                if(player != null && player.equipment[PlayerConstants.RING] == 2572) {
                    r = (int)Math.round((double)r * 0.85);
                }

                // old
                //Misc.random(kills.get(drop.getRate().getB())) >= Misc.getRandomGaussianDistribution((int) Math.ceil(drop.getRate().getB() * 0.15), drop.getRate().getB())

                int killCount = kills.get(drop.getRate().getB());
                int rate = (r * 2) - killCount;

                if(rate < r) {
                    rate = r;
                }

              //  System.out.println(rate + " " + increment + " " + (kills.get(drop.getRate().getB()) * increment) + " " + kills.get(drop.getRate().getB()) + " " + drop.getRate().getB() + " " + start);

                if (Misc.random(rate) == 0 || Misc.random(kills.get(drop.getRate().getB())) >= Misc.getRandomGaussianDistribution((int) Math.ceil(drop.getRate().getB() * 0.15), drop.getRate().getB())) {

                    if(!meetsItemKillRequirement(drop, kills)) {
                        continue;
                    }

                    if(player != null && drop.getRate().getB() >= 300) {
                        PlayerHandler.yell("[Server]@red@" + player.username + " has just received a " + player.getItems().getItemName(drop.getItemId()) + " drop from " + NPCHandler.getNpcListName(npcId) + ".");
                    }

                    kills.put(drop.getRate().getB(), 0);
                    drops.add(getDrop(drop));

                    amountDropped++;
                    break;
                }

                // old code
                    /*if (random.nextInt(drop.getRate().getB()) == drop.getRate().getB() - 1) {
						amountDropped += 1;

						drops.add(getDrop(drop));

						break;
					}*/
            }
        }
        // common drops
        Collections.shuffle(container.getCommonDrops());
        if (amountDropped < amount && container.getCommonDrops().size() > 0) {
            outer:
            for (int i = 0; i < amount; i++) {
                for (Drop drop : container.getCommonDrops()) {
                    if (random.nextInt(drop.getRate().getB()) == drop.getRate().getB() - 1) {

                        drops.add(getDrop(drop));

                        continue outer;
                    }
                }
            }

        }

        return drops;
    }

    private static Drop getDrop(Drop drop) {
        if (drop.getItemIds() == null) {
            return drop;
        } else {
            Drop tempDrop = new Drop(drop.getItemIds()[Misc.random(drop.getItemIds().length - 1)]);
            tempDrop.setRate(drop.getRate());

            if (drop.getFixedAmount() > 0) {
                tempDrop.setAmount(drop.getFixedAmount());
            } else {
                tempDrop.setAmount(drop.getMinAmount(), drop.getMaxAmount());
            }
            return tempDrop;
        }
    }

    private static int[][] MINIMUM_KILL_REQUIREMENT = new int[][] {
            {25055, 600}, // Pet chaos elemental
            {25056, 600}, // Pet dagannoth supreme
            {25057, 600}, // Pet dagannoth prime
            {25058, 600}, // Pet dagannoth rex
            {25059, 600}, // Pet smoke devil
            {25060, 600}, // Pet kree'arra
            {25061, 600}, // Pet general graardor
            {25062, 600}, // Pet zilyana
            {25063, 600}, // Pet k'ril tsutsaroth
            {25064, 600}, // Prince black dragon
            {25065, 600}, // Kalphite princess
            {25066, 600}, // Pet Kraken
            {25067, 600}, // Baby mole
            {25068, 600}, // Pet penance queen
            {25069, 600}, // Pet dark core
            {25070, 600}, // Pet snakeling
            {25071, 600}, // Abyssal orphan
            {25072, 600}, // Callisto cub
            {25073, 600}, // Hell puppy
            {25074, 600}, // Tzrek-jad
            {25075, 600}, // Scorpia's offspring
            {25076, 600}, // Venenatis spiderline
            {25077, 600}, // Vet'ion jr.
            {25078, 600}, // Baby chinchompa
            {25079, 600}, // Baby chinchompa
            {25080, 600}, // Baby chinchompa
            {25081, 600}, // Baby chinchompa
            {25082, 600}, // Beaver
            {25083, 600}, // heron
            {25084, 600}, // rock golem

    };

    private static boolean meetsItemKillRequirement(Drop drop, Map<Integer, Integer> kills) {

        for(int i = 0; i < MINIMUM_KILL_REQUIREMENT.length; i++) {
            if(MINIMUM_KILL_REQUIREMENT[i][0] == drop.getItemId()) {
                if(kills.get(drop.getRate().getB()) < MINIMUM_KILL_REQUIREMENT[i][1]) {
                    return false;
                }
            }
        }

        return true;
    }


}