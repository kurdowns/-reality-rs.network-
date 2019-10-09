package com.zionscape.server.model.content.minigames.gambling;

import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.objects.GameObject;
import com.zionscape.server.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class GambleStake {

    private final Player player;
    private final Player otherPlayer;

    private final List<Item> playerStakedItems = new ArrayList<>();
    private final List<Item> otherPlayerStakedItems = new ArrayList<>();

    private boolean playerConfirmed;
    private boolean otherPlayerConfirmed;

    private final List<Integer> playerRolled = new ArrayList<>();
    private final List<Integer> otherplayerRolled = new ArrayList<>();

    private final List<GameObject> gameObjects = new ArrayList<>();

    private int rolls;

    private final Type type;
    private Stage stage = Stage.STAKING;

    private int winsA;
    private int winsB;


    public GambleStake(Player player, Player otherPlayer, Type type) {
        this.player = player;
        this.otherPlayer = otherPlayer;
        this.type = type;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getOtherPlayer() {
        return otherPlayer;
    }

    public Type getType() {
        return type;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public List<Item> getPlayerStakedItems() {
        return playerStakedItems;
    }

    public List<Item> getOtherPlayerStakedItems() {
        return otherPlayerStakedItems;
    }

    public boolean isPlayerConfirmed() {
        return playerConfirmed;
    }

    public void setPlayerConfirmed(boolean playerConfirmed) {
        this.playerConfirmed = playerConfirmed;
    }

    public boolean isOtherPlayerConfirmed() {
        return otherPlayerConfirmed;
    }

    public void setOtherPlayerConfirmed(boolean otherPlayerConfirmed) {
        this.otherPlayerConfirmed = otherPlayerConfirmed;
    }

    public List<Integer> getPlayerRolled() {
        return playerRolled;
    }

    public List<Integer> getOtherplayerRolled() {
        return otherplayerRolled;
    }

    public int getRolls() {
        return rolls;
    }

    public void setRolls(int rolls) {
        this.rolls = rolls;
    }

    public void incrementRolls() {
        rolls++;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public int getWinsA() {
        return winsA;
    }

    public void setWinsA(int winsA) {
        this.winsA = winsA;
    }

    public int getWinsB() {
        return winsB;
    }

    public void setWinsB(int winsB) {
        this.winsB = winsB;
    }
}
