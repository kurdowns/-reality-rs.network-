package com.zionscape.server.model.content.minigames.castlewars;

import com.zionscape.server.cache.Collision;
import com.zionscape.server.model.npcs.NPC;

public class Barricade {

	private Team team;
	private NPC npc;
	private boolean burning = false;
	private int burningTicks = Constants.BARRICADE_ON_FIRE_DURATION;
	private int originalTileClipping;

	public Barricade(Team team, NPC npc) {
		this.team = team;
		this.npc = npc;
		originalTileClipping = Collision.getClipping(npc.getX(), npc.getY(), npc.heightLevel);

		Collision.flag(npc.getX(), npc.getY(), npc.heightLevel, Collision.GROUND_TILE_BLOCKED);
		CastleWars.getBarricadeLocations()[npc.getX()][npc.getY()] = true;
	}

	public void destroy() {
		Collision.flag(npc.getX(), npc.getY(), npc.heightLevel, originalTileClipping);
		CastleWars.getBarricadeLocations()[npc.getX()][npc.getY()] = false;

		npc.applyDead = true;
		npc.isDead = true;
		npc.spawnedBy = 1;
	}

	public NPC getNpc() {
		return npc;
	}

	public void setNpc(NPC npc) {
		this.npc = npc;
	}

	public boolean isBurning() {
		return burning;
	}

	public void setBurning(boolean burning) {
		if (burning && npc.type == 1532) {

		}

		this.burning = burning;
	}

	public int getOriginalTileClipping() {
		return originalTileClipping;
	}

	public int getBurningTicks() {
		return burningTicks;
	}

	public void setBurningTicks(int burningTicks) {
		this.burningTicks = burningTicks;
	}

	public Team getTeam() {
		return team;
	}

}