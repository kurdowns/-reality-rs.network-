package com.zionscape.server.model.content.minigames.gambling;

import com.zionscape.server.model.players.Player;

public class GambleRequest {

    private final long requestedAt;
    private final Player player;
    private Type type;
    private boolean confirmed;

    public GambleRequest(Player player) {
        this.requestedAt = System.currentTimeMillis();
        this.player = player;
    }

    public long getRequestedAt() {
        return requestedAt;
    }

    public Player getPlayer() {
        return player;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
