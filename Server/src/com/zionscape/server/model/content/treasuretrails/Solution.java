package com.zionscape.server.model.content.treasuretrails;

import com.zionscape.server.model.Location;
import com.zionscape.server.model.players.Player;

public interface Solution {

	public void show(Player player);

	public Location getLocation();

	public int getObjectId();

	public int getNpcId();

}
