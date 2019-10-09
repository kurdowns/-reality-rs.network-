package com.zionscape.server.model.content.minigames.gambling;

import com.google.common.collect.Lists;
import com.zionscape.server.Config;
import com.zionscape.server.Server;
import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Area;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.grandexchange.GrandExchange;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.objects.GameObject;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.dialogue.CloseDialogue;
import com.zionscape.server.net.packets.Walking;
import com.zionscape.server.util.DatabaseUtil;
import com.zionscape.server.util.Misc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Gambling {

    private static final Location SPAWN_LOCATION = Location.create(2207, 4959, 0);
    private static final Area area = new Area(2194, 4946, 2221, 4973, -1);

    public static void teleport(Player player) {

     //   if(player.rights != 3) {
       //     player.getPA().sendStatement("Gambling has been disabled.");
       //     player.setDialogueOwner(CloseDialogue.class);
      //      return;
      //  }

        if(player.ironman) {
            player.getPA().sendStatement("Ironmen cannot gamble.");
            return;
        }
        if(player.getData().minutesOnline < (5 * 60)) { // 5 hours
            player.getPA().sendStatement("You must have a minimum of 5 hours time online to gamble.");
            return;
        }
        if(player.getFamiliar() != null) {
            player.sendMessage("You may not take familiars into the gambling zone.");
            return;
        }
        player.getPA().spellTeleport(SPAWN_LOCATION.getX(), SPAWN_LOCATION.getY(), SPAWN_LOCATION.getZ());
    }

    public static void request(Player player, Player other) {

      //  if(player.rights != 3) {
        //    player.getPA().sendStatement("Gambling has been disabled.");
        //    player.setDialogueOwner(CloseDialogue.class);
         //   return;
      //  }

        if(PlayerHandler.updateRunning) {
            player.sendMessage("You cannot gamble during an update.");
            return;
        }

        if (GrandExchange.usingGrandExchange(player)) {
            return;
        }

        if(player.playerId == other.playerId) {
            return;
        }

        if(player.getFamiliar() != null) {
            return;
        }

        if(player.isBanking) {
            return;
        }

        if(player.getDuel() != null) {
            return;
        }

        if(player.storingFamiliarItems) {
            return;
        }

        if(player.attributeExists("shop")) {
            return;
        }

        if(player.ironman) {
            player.sendMessage("Ironmen cannot gamble.");
            return;
        }

        if(other.attributeExists("gamble_stake")) {
            player.sendMessage("Other player is busy.");
            return;
        }

        if(player.attributeExists("gamble_stake")) {
            player.sendMessage("Please close your existing stake before requesting or accepting another challenge.");
            return;
        }

        // cannot request a gamble on any floor other than 0
        if(player.getLocation().getZ() != 0 && other.getLocation().getZ() != 0) {
            return;
        }

        // they should be atleast visable to each other, ie someone from home cannot challenge
        if(!player.getLocation().isWithinDistance(other.getLocation())) {
            return;
        }

        // check they're next to each other, otherwise walk to the player
        if(!player.getLocation().isWithinInteractionDistance(other.getLocation())) {
            player.gambleRequestId = other.playerId;
            return;
        }

        // see if the other player already has a gamble request with us
        if(other.attributeExists("gamble_request")) {
            GambleRequest request = (GambleRequest)other.getAttribute("gamble_request");
            if(request.getPlayer() != null && request.getPlayer().equals(player) && request.isConfirmed()) {
                GambleStake stake = new GambleStake(player, other, request.getType());

                openStakeInterface(player, other, stake);
                openStakeInterface(other, player, stake);

                removeRequestAttributes(other);
                removeRequestAttributes(player);
                return;
            }
        }

        player.getPA().showInterface(19323);
        player.setAttribute("gamble_type", Type.FLOWER);
        player.setAttribute("gamble_request", new GambleRequest(other));
        player.getPA().sendFrame126("Send gamble request to: " + other.username, 19333);
        player.getPA().sendConfig(1155, 1);
    }

    private static void sendStakedItems(Player player, int inventoryId, List<Item> items) {
        player.getOutStream().createFrameVarSizeWord(53);
        player.getOutStream().writeWord(inventoryId);
        player.getOutStream().writeWord(items.size());
        for (Item item : items) {
            if (item.getAmount() > 254) {
                player.getOutStream().writeByte(255);
                player.getOutStream().writeDWord_v2(item.getAmount());
            } else {
                player.getOutStream().writeByte(item.getAmount());
            }
            player.getOutStream().writeWordBigEndianA(item.getId() + 1);
        }
        player.getOutStream().endFrameVarSizeWord();
    }

    private static void openStakeInterface(Player player, Player other, GambleStake stake) {
        if(stake.getType().equals(Type.FLOWER)) {
            player.getPA().sendFrame126("Flower Gamble", 19340);
            player.getPA().sendFrame126("Players plant 5 flowers, \\nwhoever ends up with the \\nhighest score wins the pot! \\nNo pairs of the same color: \\nBust Pair of the same color: \\nOne pair 2 pairs of the same \\nflower: Double pair 3 of the \\nsame flower: 3 of a kind 3 of \\nthe same flower & a pair: \\nFull house 4 colors of the \\nsame flower: Four of a kind 5 \\nof the same flower: Five of \\na kind", 19344);
        } else {
            player.getPA().sendFrame126("Dice Duel", 19340);
            player.getPA().sendFrame126("The rules of Dice Duels are \\n simple, there are 5 rounds, \\nthe first player to roll the \\nhigher number 3 times out \\nof 5 wins the pot!", 19344);
        }
        player.getPA().sendFrame126("", 19358);
        player.getPA().sendFrame126("Opponents Offer " + other.username + " :", 19342);

        player.setAttribute("gamble_stake", stake);

        sendStakedItems(player, 19353, Lists.newArrayList());
        sendStakedItems(player, 19354, Lists.newArrayList());

        player.getPA().sendFrame248(19336, 19355);
        player.getItems().resetItems(19356);
    }

    public static void stakeItem(Player player, int item, int amount) {

        if(player.openInterfaceId != 19336) {
            return;
        }
        if(!player.attributeExists("gamble_stake")) {
            return;
        }
        if(!player.getItems().playerHasItem(item)) {
            return;
        }
        int playerAmount = player.getItems().getItemAmount(item);
        if(amount > playerAmount) {
            amount = playerAmount;
        }

        GambleStake stake = (GambleStake)player.getAttribute("gamble_stake");

        if(!stake.getStage().equals(Stage.STAKING)) {
            return;
        }

        for (int i : Config.UNTRADABLE_ITEM_IDS) {
            if (i == item) {
                player.sendMessage("This item cannot be staked.");
                return;
            }
        }

        List<Item> stakedItems;
        if(stake.getPlayer().equals(player)) {
            stakedItems = stake.getPlayerStakedItems();
        } else {
            stakedItems = stake.getOtherPlayerStakedItems();
        }

        boolean stackable = ItemUtility.isStackable(item);
        if(stackable) {
            // check for rollover
            Optional<Item> optionalExistingItem = stakedItems.stream().filter(x -> x.getId() == item).findAny();

            if(optionalExistingItem.isPresent()) {
                Item existingItem  = optionalExistingItem.get();

                if((long)existingItem.getAmount() + (long)amount > Integer.MAX_VALUE) {
                    amount = Integer.MAX_VALUE - existingItem.getAmount();
                    player.sendMessage("You cannot stake more than " + Misc.formatNumber(Integer.MAX_VALUE) + " of any item.");

                    if(amount == 0) {
                        return;
                    }

                }
                existingItem.setAmount(existingItem.getAmount() + amount);
            } else {
                stakedItems.add(new Item(item, amount));
            }
        } else {
            for(int i = 0; i < amount; i++) {
                stakedItems.add(new Item(item, 1));
            }
        }

        player.getItems().deleteItem(item, amount);
        player.getItems().resetItems(19356);

        if(stake.getPlayer().equals(player)) {
            sendStakedItems(player, 19353, stake.getPlayerStakedItems());
            sendStakedItems(stake.getOtherPlayer(), 19354, stake.getPlayerStakedItems());
        } else {
            sendStakedItems(stake.getOtherPlayer(), 19353, stake.getOtherPlayerStakedItems());
            sendStakedItems(stake.getPlayer(), 19354, stake.getOtherPlayerStakedItems());
        }

        if(stake.isPlayerConfirmed() || stake.isOtherPlayerConfirmed()) {
            stake.getPlayer().getPA().sendFrame126("Items changed, please reconfirm.", 19358);
            stake.getOtherPlayer().getPA().sendFrame126("Items changed, please reconfirm.", 19358);

            stake.setPlayerConfirmed(false);
            stake.setOtherPlayerConfirmed(false);
        }
    }

    public static void removeItem(Player player, int item, int amount) {

        if(player.openInterfaceId != 19336) {
            return;
        }
        if(!player.attributeExists("gamble_stake")) {
            return;
        }

        GambleStake stake = (GambleStake)player.getAttribute("gamble_stake");

        if(!stake.getStage().equals(Stage.STAKING)) {
            return;
        }

        List<Item> stakedItems;
        if(player.equals(stake.getPlayer())) {
            stakedItems = stake.getPlayerStakedItems();
        } else {
            stakedItems = stake.getOtherPlayerStakedItems();
        }

        List<Item> items = stakedItems.stream().filter(x -> x.getId() == item).collect(Collectors.toList());
        if(items.size() == 0) {
            return;
        }

        int stakedAmount = 0;
        for(Item i : items) {
            stakedAmount += i.getAmount();
        }

        if(amount > stakedAmount) {
            amount = stakedAmount;
        }

        int amountToDelete = amount;
        Iterator<Item> itr = stakedItems.iterator();
        while(itr.hasNext()) {
            Item i = itr.next();

            if(i.getId() != item) {
                continue;
            }

            if(amountToDelete >= i.getAmount()) {
                amountToDelete -= i.getAmount();
                itr.remove();
            } else {
                i.setAmount(i.getAmount() - amountToDelete);
                amountToDelete = 0;
            }

            if(amountToDelete == 0) {
                break;
            }
        }

        player.getItems().addItem(item, amount);
        player.getItems().resetItems(19356);

        if(stake.getPlayer().equals(player)) {
            sendStakedItems(player, 19353, stake.getPlayerStakedItems());
            sendStakedItems(stake.getOtherPlayer(), 19354, stake.getPlayerStakedItems());
        } else {
            sendStakedItems(stake.getOtherPlayer(), 19353, stake.getOtherPlayerStakedItems());
            sendStakedItems(stake.getPlayer(), 19354, stake.getOtherPlayerStakedItems());
        }

        if(stake.isPlayerConfirmed() || stake.isOtherPlayerConfirmed()) {
            stake.getPlayer().getPA().sendFrame126("Items changed, please reconfirm.", 19358);
            stake.getOtherPlayer().getPA().sendFrame126("Items changed, please reconfirm.", 19358);

            stake.setPlayerConfirmed(false);
            stake.setOtherPlayerConfirmed(false);
        }

    }

    public static boolean onClickingButtons(Player player, int button) {
        switch (button) {
            case 75128:
                if(player.openInterfaceId != 19323) {
                    return true;
                }
                player.setAttribute("gamble_type", Type.FLOWER);
                player.getPA().sendConfig(1155, 1); // flower
                return true;
            case 75129:
                if(player.openInterfaceId != 19323) {
                    return true;
                }
                player.setAttribute("gamble_type", Type.DICE);
                player.getPA().sendConfig(1155, 2); // dice
                return true;
            case 75130:
                if(player.openInterfaceId != 19323) {
                    player.getPA().closeAllWindows();
                    return true;
                }
                if(!player.attributeExists("gamble_request")) {
                    player.getPA().closeAllWindows();
                    return true;
                }

                GambleRequest request = (GambleRequest)player.getAttribute("gamble_request");

                if(request.getPlayer() == null || !player.getLocation().isWithinDistance(request.getPlayer().getLocation())) {
                    removeRequestAttributes(player);
                    player.getPA().closeAllWindows();
                    player.sendMessage("The other player is not available.");
                    return true;
                }

                request.setConfirmed(true);
                request.setType((Type)player.getAttribute("gamble_type"));

                player.sendMessage("Sending gamble request to " + request.getPlayer().username + "...");
                request.getPlayer().sendMessage(player.username + (request.getType().equals(Type.FLOWER) ? ":flowerreq:" : ":dicereq:"));

                player.getPA().closeAllWindows();
                return true;

            case 75148: // decline
                if(player.openInterfaceId != 19336) {
                    return true;
                }

                if(!player.attributeExists("gamble_stake")) {
                    return true;
                }

                GambleStake stake = (GambleStake)player.getAttribute("gamble_stake");
                if(stake.getStage() != Stage.STAKING) {
                    return true;
                }

                declineStake(stake);
                return true;

            case 75145: // accept
                if(player.openInterfaceId != 19336) {
                    return true;
                }

                if(!player.attributeExists("gamble_stake")) {
                    return true;
                }

                stake = (GambleStake)player.getAttribute("gamble_stake");
                if(stake.getStage() != Stage.STAKING) {
                    return true;
                }

                if(stake.getPlayer().equals(player)) {
                    stake.setPlayerConfirmed(true);
                } else {
                    stake.setOtherPlayerConfirmed(true);
                }

                if(stake.isPlayerConfirmed() && stake.isOtherPlayerConfirmed()) {

                    stake.getPlayer().getPA().closeAllWindows();
                    stake.getOtherPlayer().getPA().closeAllWindows();

                    //start the gamble
                    stake.setStage(Stage.DUELING);

                    int height = stake.getPlayer().playerId * 4;

                    stake.getPlayer().getPA().movePlayer(2207, 4957, height);
                    stake.getOtherPlayer().getPA().movePlayer(2208, 4957, height);

                    stake.getPlayer().blockWalking = true;
                    stake.getOtherPlayer().blockWalking = true;
                    stake.getPlayer().getPA().resetFollow();
                    stake.getOtherPlayer().getPA().resetFollow();
                    stake.getPlayer().immuneToDamage = true;
                    stake.getOtherPlayer().immuneToDamage = true;

                    if(stake.getType().equals(Type.FLOWER)) {
                        GameCycleTaskHandler.addEvent(Gambling.class, new GameCycleTask() {
                            @Override
                            public void execute(GameCycleTaskContainer container) {

                                // the actual dicing part
                                GameCycleTaskHandler.addEvent(Gambling.class, new GameCycleTask() {
                                    @Override
                                    public void execute(GameCycleTaskContainer container) {
                                        plantFlower(stake);
                                    }
                                }, 6, true).setName("gamble-" + stake.getPlayer().username);

                                container.stop();
                            }
                        }, 2).setName("gamble-" + stake.getPlayer().username);
                    } else if(stake.getType().equals(Type.DICE)) {

                        GameCycleTaskHandler.addEvent(Gambling.class, new GameCycleTask() {
                            @Override
                            public void execute(GameCycleTaskContainer container) {

                                // the actual dicing part
                                GameCycleTaskHandler.addEvent(Gambling.class, new GameCycleTask() {
                                    @Override
                                    public void execute(GameCycleTaskContainer container) {
                                        rollDice(stake);
                                    }
                                }, 6, true).setName("gamble-" + stake.getPlayer().username);


                                container.stop();
                            }
                        }, 2).setName("gamble-" + stake.getPlayer().username);
                    }
                } else {
                    if(stake.isPlayerConfirmed()) {
                        stake.getPlayer().getPA().sendFrame126("Waiting for other player...", 19358);
                        stake.getOtherPlayer().getPA().sendFrame126("Other player has accepted...", 19358);
                    } else {
                        stake.getOtherPlayer().getPA().sendFrame126("Waiting for other player...", 19358);
                        stake.getPlayer().getPA().sendFrame126("Other player has accepted...", 19358);
                    }
                }
                return true;
        }

        return false;
    }

    private static void plantFlower(GambleStake stake) {
        if(stake.getRolls() >= 5) {
            endStake(stake);
            return;
        }

        stake.incrementRolls();

        int objectA = 2980 + Misc.random(6);
        int objectB = 2980 + Misc.random(6);

        if(Misc.random(10) == 0 && !stake.getOtherPlayer().winGamble) {
            objectA = 2986 + Misc.random(1, 2);
        } else if(Misc.random(10) == 0 && !stake.getPlayer().winGamble) {
            objectB = 2986 + Misc.random(1, 2);
        }

        if(stake.getRolls() >= 3) {
            if(stake.getPlayer().winGamble) {
                objectA = 2986 + Misc.random(1, 2);
            }
            if(stake.getOtherPlayer().winGamble) {
                objectB = 2986 + Misc.random(1, 2);
            }
        }

        final int flowerObjectA = objectA;
        final int flowerObjectB = objectB;

        stake.getPlayerRolled().add(objectA);
        stake.getOtherplayerRolled().add(objectB);

        Location locationA = stake.getPlayer().getLocation();
        Location locationB = stake.getOtherPlayer().getLocation();

        GameObject obj1 = new GameObject(objectA, locationA.getX(), locationA.getY(), locationA.getZ(), 0, 10, -1, -1);
        GameObject obj2 = new GameObject(objectB, locationB.getX(), locationB.getY(), locationB.getZ(), 0, 10, -1, -1);

        stake.getGameObjects().add(obj1);
        stake.getGameObjects().add(obj2);

        GameCycleTaskHandler.addEvent(Gambling.class, container -> {

            stake.getPlayer().getPA().walkTo(0, -1);
            stake.getOtherPlayer().getPA().walkTo(0, -1);

            if(flowerObjectA >= 2987 || flowerObjectB >= 2987) {
                GameCycleTaskHandler.addEvent(Gambling.class, container1 -> { endStake(stake); }, 1).setName("gamble-" + stake.getPlayer().username);
                container.stop();
                return;
            }

            container.stop();
        }, 1).setName("gamble-" + stake.getPlayer().username);
    }

    private static void rollDice(GambleStake stake) {

        if(stake.getRolls() >= 5) {
            endStake(stake);
            return;
        }

        stake.incrementRolls();

        stake.getPlayer().startAnimation(11900);
        stake.getPlayer().gfx0(2075);

        stake.getOtherPlayer().startAnimation(11900);
        stake.getOtherPlayer().gfx0(2075);

        int playerRolled = Misc.random(stake.getPlayer().winGamble ? 70 : 1, 100);
        int otherRolled = Misc.random(stake.getOtherPlayer().winGamble ? 70 : 1, 100);

        stake.getPlayerRolled().add(playerRolled);
        stake.getOtherplayerRolled().add(otherRolled);

        if(playerRolled > otherRolled) {
            stake.setWinsA(stake.getWinsA() + 1);
        } else if(otherRolled > playerRolled) {
            stake.setWinsB(stake.getWinsB() + 1);
        } else if(otherRolled == playerRolled) {
            stake.setWinsA(stake.getWinsA() + 1);
            stake.setWinsB(stake.getWinsB() + 1);
        }

        GameCycleTaskHandler.addEvent(Gambling.class, container -> {
            if(playerRolled > otherRolled) {
                stake.getPlayer().forcedChat("I rolled a " + playerRolled + " on the percentile, " + getDiceEndText(stake, true));
                stake.getOtherPlayer().forcedChat("I rolled a " + otherRolled + " on the percentile, " + getDiceEndText(stake, false));
            } else if(otherRolled > playerRolled) {
                stake.getOtherPlayer().forcedChat("I rolled a " + otherRolled + " on the percentile, " + getDiceEndText(stake, false));
                stake.getPlayer().forcedChat("I rolled a " + playerRolled + " on the percentile, " + getDiceEndText(stake, true));
            } else if(otherRolled == playerRolled) {
                stake.getOtherPlayer().forcedChat("I rolled a " + playerRolled + " on the percentile, " + getDiceEndText(stake, false));
                stake.getPlayer().forcedChat("I rolled a " + otherRolled + " on the percentile, " + getDiceEndText(stake, true));
            }
            container.stop();
        }, 1).setName("gamble-" + stake.getPlayer().username);
    }

    private static String getDiceEndText(GambleStake stake, boolean firstPlayer) {
        if(stake.getWinsA() == stake.getWinsB()) {
            return "its tied " + stake.getWinsA() + "-" + stake.getWinsB() + ".";
        }

        if(firstPlayer) {
            if(stake.getWinsA() > stake.getWinsB()) {
                return "i'm up " + stake.getWinsA() + "-" + stake.getWinsB() + ".";
            } else {
                return "i'm down " + stake.getWinsA() + "-" + stake.getWinsB() + ".";
            }
        } else {
            if(stake.getWinsB() > stake.getWinsA()) {
                return "i'm up " + stake.getWinsB() + "-" + stake.getWinsA() + ".";
            } else {
                return "i'm down " + stake.getWinsB() + "-" + stake.getWinsA() + ".";
            }
        }
    }

    public static void onPlayerLoggedOut(Player player) {
        if(player.attributeExists("gamble_stake")) {
            GambleStake stake =  (GambleStake)player.getAttribute("gamble_stake");
            if(stake.getStage() == Stage.STAKING) {
                declineStake(stake);
            }
            if(stake.getStage() == Stage.DUELING) {
                endStake(stake);
            }
        }
    }

    private static void endStake(GambleStake stake) {
        GameCycleTaskHandler.stopEvents("gamble-" + stake.getPlayer().username);

        stake.getPlayer().immuneToDamage = false;
        stake.getOtherPlayer().immuneToDamage = false;

        stake.getPlayer().blockWalking = false;
        stake.getOtherPlayer().blockWalking = false;

        stake.getPlayer().winGamble = false;
        stake.getOtherPlayer().winGamble = false;

        stake.getPlayer().getPA().movePlayer(SPAWN_LOCATION);
        stake.getOtherPlayer().getPA().movePlayer(SPAWN_LOCATION);

        int playerA = 0;
        int playerB = 0;

        if(stake.getType().equals(Type.DICE)) {
            for(int i = 0; i < 5; i++) {
                if(i > stake.getPlayerRolled().size() - 1) {
                    continue;
                }

                int a = stake.getPlayerRolled().get(i);
                int b = stake.getOtherplayerRolled().get(i);

                if(a == b) {
                    playerA++;
                    playerB++;
                } else if(a > b) {
                    playerA++;
                } else if(b > a) {
                    playerB++;
                }
            }
        } else if(stake.getType().equals(Type.FLOWER)) {
            for(GameObject obj : stake.getGameObjects()) {
                Server.objectManager.removeObject(obj);
                Server.objectManager.objects.remove(obj);
            }

            int a[] = new int[10];
            int b[] = new int[10];

            for(int i = 0; i < stake.getPlayerRolled().size(); i++) {

                int a1 = stake.getPlayerRolled().get(i);
                int b1 = stake.getOtherplayerRolled().get(i);

                // black and white is an auto win
                if(a1 >= 2987) {
                    a[a1 - 2980] = 10;
                }
                if(b1 >= 2987) {
                    b[b1 - 2980] = 10;
                }

                a[a1 - 2980] += 1;
                b[b1 - 2980] += 1;
            }

            for(int i = 0; i < a.length; i++) {
                if(a[i] > playerA) {
                    playerA = a[i];
                }
                if (b[i] > playerB) {
                    playerB = b[i];
                }
            }
        }

        if(stake.getPlayer().isDisconnected()) {
            playerB = 100;
        } else if(stake.getOtherPlayer().isDisconnected()) {
            playerA = 100;
        }

        Player whoWon = null;

        if(playerA > playerB) {
            whoWon = stake.getPlayer();

            stake.getPlayer().sendMessage("Congratulations, you won.");
            stake.getOtherPlayer().sendMessage("Oh dear, you lost.");

            for(Item item : stake.getPlayerStakedItems()) {
                stake.getPlayer().getItems().addOrBank(item.getId(), item.getAmount());
            }
            for(Item item : stake.getOtherPlayerStakedItems()) {
                stake.getPlayer().getItems().addOrBank(item.getId(), item.getAmount());
            }
        } else if(playerB > playerA) {
            whoWon = stake.getOtherPlayer();
            stake.getPlayer().sendMessage("Oh dear, you lost.");
            stake.getOtherPlayer().sendMessage("Congratulations, you won.");

            for(Item item : stake.getPlayerStakedItems()) {
                stake.getOtherPlayer().getItems().addOrBank(item.getId(), item.getAmount());
            }
            for(Item item : stake.getOtherPlayerStakedItems()) {
                stake.getOtherPlayer().getItems().addOrBank(item.getId(), item.getAmount());
            }
        } else if(playerA == playerB) {
            stake.getPlayer().sendMessage("Whoops, it looks like a draw.");
            stake.getOtherPlayer().sendMessage("Whoops, it looks like a draw.");
            for(Item item : stake.getPlayerStakedItems()) {
                stake.getPlayer().getItems().addOrBank(item.getId(), item.getAmount());
            }
            for(Item item : stake.getOtherPlayerStakedItems()) {
                stake.getOtherPlayer().getItems().addOrBank(item.getId(), item.getAmount());
            }
        }

        final Player finalWhoWon = whoWon;
        stake.getPlayerStakedItems().forEach(Item::fillName);
        stake.getOtherPlayerStakedItems().forEach(Item::fillName);

        Server.submitWork(() -> {

            try(Connection connection = DatabaseUtil.getConnection()) {
                try(PreparedStatement ps = connection.prepareStatement("INSERT INTO gamble_logs (player_id, username, other_player_id, other_username, player_items, other_items, who_won) VALUES(?, ?, ?, ?, ?, ?, ?)")) {

                    ps.setInt(1, stake.getPlayer().getDatabaseId());
                    ps.setString(2, stake.getPlayer().username);
                    ps.setInt(3, stake.getOtherPlayer().getDatabaseId());
                    ps.setString(4, stake.getOtherPlayer().username);
                    ps.setString(5, Misc.getGson().toJson(stake.getPlayerStakedItems()));
                    ps.setString(6, Misc.getGson().toJson(stake.getOtherPlayerStakedItems()));

                    if(finalWhoWon == null) {
                        ps.setString(7, "DRAW");
                    } else {
                        ps.setString(7, finalWhoWon.username);
                    }

                    ps.execute();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        stake.getPlayer().removeAttribute("gamble_stake");
        stake.getOtherPlayer().removeAttribute("gamble_stake");
    }

    private static void declineStake(GambleStake stake) {
        for(Item item : stake.getPlayerStakedItems()) {
            stake.getPlayer().getItems().addItem(item.getId(), item.getAmount());
        }
        for(Item item : stake.getOtherPlayerStakedItems()) {
            stake.getOtherPlayer().getItems().addItem(item.getId(), item.getAmount());
        }

        stake.getPlayer().sendMessage("The gamble has been canceled.");
        stake.getPlayer().getPA().closeAllWindows();
        stake.getPlayer().removeAttribute("gamble_stake");

        stake.getOtherPlayer().sendMessage("The gamble has been canceled.");
        stake.getOtherPlayer().getPA().closeAllWindows();
        stake.getOtherPlayer().removeAttribute("gamble_stake");
    }

    public static boolean onClickingStuff(Player player) {
        if(inArea(player) && player.attributeExists("gamble_request")) {
            removeRequestAttributes(player);
            return true;
        }
        return false;
    }

    private static void removeRequestAttributes(Player player) {
        player.removeAttribute("gamble_request");
        player.removeAttribute("gamble_type");
    }

    public static boolean onPlayerWalk(Player player, Walking.WalkingType type) {

        if(player.attributeExists("gamble_stake")) {
            GambleStake stake = (GambleStake)player.getAttribute("gamble_stake");

            if(stake.getStage().equals(Stage.STAKING)) {
                declineStake(stake);
            }
        }

        return false;
    }

    public static boolean inArea(Player player) {
        return area.inArea(player.getLocation());
    }

    public static void onPlayerProcess(Player player) {
        if(player.attributeExists("gamble_request")) {
            GambleRequest challenge = (GambleRequest)player.getAttribute("gamble_request");
            if(System.currentTimeMillis() - challenge.getRequestedAt() > 30 * 1000) {
                player.removeAttribute("gamble_request");

                if(player.openInterfaceId == 19323) {
                    player.getPA().closeAllWindows();
                    player.sendMessage("Your gamble challenge has expired.");
                }
            }
            if(challenge.getPlayer() == null || challenge.getPlayer().isDisconnected() || !inArea(player) || !inArea(challenge.getPlayer())) {
                player.removeAttribute("gamble_request");

                if(player.openInterfaceId == 19323) {
                    player.getPA().closeAllWindows();
                    player.sendMessage("Your gamble challenge has expired.");
                }
            }
        }
        if(player.attributeExists("gamble_stake")) {
            GambleStake stake = (GambleStake)player.getAttribute("gamble_stake");
            // if either player is not in the gamble area end the stake
            if(!inArea(stake.getPlayer()) || !inArea(stake.getOtherPlayer())) {
                if(stake.getStage().equals(Stage.STAKING)) {
                    declineStake(stake);
                } else if(stake.getStage().equals(Stage.DUELING)) {
                    endStake(stake);
                }
            }
        }
    }

    public static boolean inStake(Player player) {
        if(player.attributeExists("gamble_stake")) {
            GambleStake stake = (GambleStake) player.getAttribute("gamble_stake");
            if(stake.getStage().equals(Stage.DUELING)) {
                return true;
            }
        }
        return false;
    }

}