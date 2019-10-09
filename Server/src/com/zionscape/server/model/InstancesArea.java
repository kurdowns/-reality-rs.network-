package com.zionscape.server.model;

import com.zionscape.server.Config;
import com.zionscape.server.Server;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.objects.GameObject;
import com.zionscape.server.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class InstancesArea {

	private Player player;
	private List<NPC> npcs = new ArrayList<>();
	private List<GameObject> gameObjects = new ArrayList<>();
	private Area area;

	public InstancesArea(Player player, List<NPC> npcs, Area area) {
		this.player = player;
		this.npcs = npcs;
		this.area = area;
	}

	public InstancesArea(Player player, Area area, NPC... npcs) {
		this.player = player;
		this.area = area;

		for (NPC n : npcs) {
			n.setAttribute("instanced_area", this);
			this.npcs.add(n);
		}
	}

	public Player getPlayer() {
		return player;
	}

	public List<NPC> getNpcs() {
		return npcs;
	}

	public Area getArea() {
		return area;
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}

	public void leave(boolean died) {
		if (!died) {
			player.absX = Config.EDGEVILLE_X;
			player.absY = Config.EDGEVILLE_Y;
			player.heightLevel = 0;
		}

		if (npcs == null || npcs.size() == 0) {
			return;
		}

		for (NPC npc : npcs) {
			if (npc == null) {
				continue;
			}

			if (!npc.isDead) {
				npc.isDead = true;
			}

			npc.setAttribute("dont_drop", true);
			npc.setRespawn(false);
		}

		for (GameObject gameObject : gameObjects) {
			if (gameObject == null) {
				continue;
			}
			Server.objectManager.removeObject(Location.create(gameObject.objectX, gameObject.objectY, gameObject.height), gameObject.type, true);
		}
	}

}