package com.zionscape.server.model.players.skills.mining;

import com.zionscape.server.Server;
import com.zionscape.server.cache.clientref.ObjectDef;
import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.MapObject;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.objects.GameObject;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.skills.SkillItems;
import com.zionscape.server.model.players.skills.SkillUtils;
import com.zionscape.server.model.region.RegionUtility;
import com.zionscape.server.util.Misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Mining {

    private static List<Location> depletedRocks = new ArrayList<>();

    @SuppressWarnings("unused")
    public static boolean onObjectClick(Player player, int objectId, Location objectLocation, int option) {
        if (player.isBusy()) {
            return false;
        }
        final Rock rock = Rock.getRock(objectId);
        if (rock == null) {
            return false;
        }

        if (option == 2) {
            player.setBusy(true);
            GameCycleTaskHandler.addEvent(player, (GameCycleTaskContainer container) -> {
                player.sendMessage("This rock contains " + rock.getName() + ".");
                player.setBusy(false);
                container.stop();
            }, 2);
            return true;
        }

        final PickAxe axe = PickAxe.get(player);
        if (axe == null) {
            player.sendMessage("You need a pickaxe to mine this rock");
            return true;
        }
        if (player.getActualLevel(PlayerConstants.MINING) < rock.getLevelRequired()) {
            player.sendMessage("You need a Mining level of " + rock.getLevelRequired() + " to mine this rock");
            return true;
        }
        if (player.getItems().freeInventorySlots() <= 0) {
            player.sendMessage("You do not have enough inventory space.");
            return true;
        }

        if (depletedRocks.contains(objectLocation)) {
            player.sendMessage("This rock has depleted.");
            return true;
        }

        final Location playerLocation = player.getLocation();
        player.setBusy(true);
        player.startAnimation(axe.getAnimation());
        player.skilling = true;
        GameCycleTaskHandler.addEvent(player, new GameCycleTask() {
            int animCount = 0;
            int count = algorithm(player.getActualLevel(PlayerConstants.MINING), axe.getBonus(), rock.getDelay());
            int tempCount = 0;

            @Override
            public void execute(final GameCycleTaskContainer container) {
                animCount++;
                tempCount++;
                if (animCount >= axe.getAnimationCycles()) {
                    animCount = 0;
                    player.startAnimation(axe.getAnimation());
                }

                if (!player.skilling) {
                    container.stop();
                    return;
                }

                if (depletedRocks.contains(objectLocation)) {
                    container.stop();
                    return;
                }

                if (!playerLocation.equals(player.getLocation())) {
                    container.stop();
                    return;
                }
                if (!player.isBusy()) {
                    container.stop();
                    return;
                }
                if (tempCount >= count) {
                    count = algorithm(player.getActualLevel(PlayerConstants.MINING), axe.getBonus(), rock.getDelay());
                    tempCount = 0;

                    int skillItemCount = SkillItems.getMiningItemsWorn(player);

                    int ore = rock.getOre();
                    int amount = 1;

                    if (ore == -1) {
                        ore = Rock.getGemId();
                    }

                    if(skillItemCount >= 5 && Misc.random(5) == 0) {
                        ore = ItemUtility.getNotedId(ore);
                    }

                    double xpMulti = 1;
                    if(skillItemCount > 0) {
                        if(skillItemCount >= 4) {
                            xpMulti += 0.10;
                        } else {
                            xpMulti += skillItemCount * 0.015;
                        }
                    }

                    /*if (ore == 1436) {
                        if (player.getActualLevel(PlayerConstants.MINING) >= 30) {
                            ore = 7936;
                        }
                        if (player.getActualLevel(PlayerConstants.MINING) < 20) {
                            amount = 1;
                        } else {
                            amount = Misc.random(1, ((player.getActualLevel(PlayerConstants.MINING) + 1) / 10));
                        }
                    }*/

                    player.getItems().addItem(ore, amount);
                    player.getPA().addSkillXP((int)Math.round(xpMulti * (double)rock.getXp()), PlayerConstants.MINING);
                    player.sendMessage("You get some " + ItemUtility.getName(ore) + ".");

                    if (SkillUtils.givePet(player.getActualLevel(PlayerConstants.MINING), ore, skillItemCount >= 5 ? 0.05 : 0)) {
                        player.getItems().addOrBank(25084, 1);
                        player.sendMessage("You have been given a Rock Golem pet.");
                    }

                    if (ore == 436) {
                        Achievements.progressMade(player, Achievements.Types.MINE_100_COPPER_ORE);
                    }
                    if (ore == 451) {
                        Achievements.progressMade(player, Achievements.Types.MINE_500_RUNE_ORE);
                    }
                    if (ore == 1436) {
                        Achievements.progressMade(player, Achievements.Types.MINE_2500_RUNE_ESS);
                    }

                    boolean fullInvent = false;
                    animCount = 0;
                    player.startAnimation(axe.getAnimation());
                    if (player.getItems().freeInventorySlots() <= 0) {
                        player.sendMessage("You cannot hold any more ore.");
                        fullInvent = true;
                    }

                    // don't stop mining till inventory is full when mining full essence
                    if (ore != 1436) {
                        int depletedId = getDepletedObjectId(objectId);

                        if(depletedId == -1) {
                            depletedId = 11554;
                        }

                        if (depletedId > -1) {
                            Optional<MapObject> optional = RegionUtility.getMapObject(objectId, objectLocation);
                            if (optional.isPresent()) {
                                MapObject mapObject = optional.get();

                                depletedRocks.add(objectLocation);

                                // remove any existing rock
                                Server.objectManager.removeObject(objectLocation, mapObject.getType(), false);

                                //public GameObject(int id, int x, int y, int height, int face, int type, int newId, int ticks) {
                                new GameObject(depletedId, objectLocation.getX(), objectLocation.getY(), objectLocation.getZ(), mapObject.getOrientation(), mapObject.getType(), objectId, rock.getRespawnTime());

                                GameCycleTaskHandler.addEvent(Mining.class, container1 -> {
                                    depletedRocks.remove(objectLocation);
                                    container1.stop();
                                }, rock.getRespawnTime());


                                //ObjectHandler.addObject(new MapObject(tree.getStump(), objectLoc, object.getType(), object.getRotation(), object, tree.getRespawnTime(), Type.ORIGINAL));
                            }
                        }

                        container.stop();
                    }

                    if (fullInvent) {
                        container.stop();
                        return;
                    }
                }
            }

            @Override
            public void stop() {
                player.startAnimation(65535);
                player.setBusy(false);
            }
        }, 1);
        return true;
    }

    public static int algorithm(final int level, final int axe, final int delay) {
        final int random = (int) ((10 - (level / 5.00) - axe) + delay);
        return Misc.random(random);
    }


    // add color checking
    private static int getDepletedObjectId(int objectId) {

        switch (objectId) {
            case 2096:
            case 2097:
            case 2093:
            case 2092:
                return 451;
        }

        int modelId = -1;

        ObjectDef def = ObjectDef.forID(objectId);

        if (def != null) {

            if (def.objectModelIDS != null) {
                modelId = def.objectModelIDS[0];
            } else {
                if (def.alternativeIDS != null) {
                    for (int i : def.alternativeIDS) {
                        if (i > -1) {
                            def = ObjectDef.forID(i);
                            if (def != null && def.objectModelIDS != null) {
                                modelId = def.objectModelIDS[0];
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (modelId > -1) {
            switch (modelId) {
                case 3184:
                    return 11552;
                case 3183:
                    return 11553;
                case 3195:
                    return 11554;

            }
        }

        return -1;
    }

}
