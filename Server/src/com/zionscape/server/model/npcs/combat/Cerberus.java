package com.zionscape.server.model.npcs.combat;

import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCCombat;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.util.Misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Cerberus extends NPCCombat {

	private List<NPC> ghosts = new ArrayList<>();
	private List<Location> lavaLocations = new ArrayList<>();

	public Cerberus(NPC npc) {
		super(npc);
	}

	@Override
	public void attackPlayer(Player player) {
		super.attackPlayer(player);

		// lava traps
		if (lavaLocations.size() == 0 && Misc.random(0, 6) == 0 && getNpc().getHP() <= 200) {
			getNpc().forceChat("Grrrrrr");
			getNpc().startAnimation(4494);
			Location loc = player.getLocation();
			lavaLocations.add(loc);

			player.dealDamage(10);
			player.handleHitMask(10, 0, 3, 0);
			player.getPA().refreshSkill(PlayerConstants.HITPOINTS);

			player.getPA().createPlayersStillGfx(1246, loc.getX(), loc.getY(), loc.getZ(), 0);
			GameCycleTaskHandler.addEvent(getNpc(), container -> {

				if (player.getLocation().equals(loc)) {

					GameCycleTaskHandler.addEvent(player, new GameCycleTask() {
						int timesRan = 0;

						@Override
						public void execute(GameCycleTaskContainer container) {
							if (!player.getLocation().equals(loc) || player.isDead) {
								container.stop();
								return;
							}

							player.dealDamage(15);
							player.handleHitMask(15, 0, 3, 0);
							player.getPA().refreshSkill(PlayerConstants.HITPOINTS);

							if (timesRan++ >= 2) {
								container.stop();
							}
						}
					}, 1);

					player.dealDamage(15);
					player.handleHitMask(15, 0, 3, 0);
					player.getPA().refreshSkill(PlayerConstants.HITPOINTS);
				}

				player.getPA().createPlayersStillGfx(1247, loc.getX(), loc.getY(), loc.getZ(), 0);
				lavaLocations.remove(loc);
				container.stop();
			}, 4);

			getNpc().setSkipCombatTurn(true);
			return;
		}

		// ghosts
		if (ghosts.size() == 0 && Misc.random(0, 8) == 0) {

			getNpc().forceChat("Aoorrrooooooo");
			getNpc().startAnimation(4494);

			List<Integer> ids = Arrays.asList(5867, 5868, 5869);
			Collections.shuffle(ids);

			for (int i = 0; i < ids.size(); i++) {
				final int index = i;

				int id = ids.get(i);
				NPC npc = NPCHandler.spawnNpc2(id, 1239 + i, 1258, getNpc().heightLevel, 0, 0, 0, 0, 0);


				// inner class inception woops

				GameCycleTaskHandler.addEvent(npc, new GameCycleTask() {
					int moved = 0;

					@Override
					public void execute(GameCycleTaskContainer container) {
						npc.moveX = 0;
						npc.moveY -= 1;

						npc.getNextNPCMovement();
						npc.updateRequired = true;
						npc.moveX = 0;
						npc.moveY = 0;

						if (moved++ >= 1) {
							container.stop();

							GameCycleTaskHandler.addEvent(npc, new GameCycleTask() {
								@Override
								public void execute(GameCycleTaskContainer container) {

									GameCycleTaskHandler.addEvent(npc, new GameCycleTask() {

										@Override
										public void execute(GameCycleTaskContainer container) {
											if (player.getLocation().isWithinDistance(npc.getLocation())) {
												//send attacks

												npc.startAnimation(npc.type == 5869 ? 4504 : 4503);

												int npcX = npc.absX;
												int npcY = npc.absY;

												int offY = ((npcX - player.absX) * -1);
												int offX = ((npcY - player.absY) * -1);

												int projectile = 1123; // range
												if (npc.type == 5868) { // magic
													projectile = 2731;
												}
												if (npc.type == 5869) { // melee
													projectile = 1248;
												}

												player.getPA().createPlayersProjectile(npcX, npcY, offX, offY, 50, 60, projectile, 75, 0, -player.getId() - 1, 30);

												GameCycleTaskHandler.addEvent(npc, container1 -> {

													int type = 0;
													if (npc.getType() == 5868) {
														type = 2;
													}
													if (npc.getType() == 5867) {
														type = 1;
													}


													int maxDamage = 30;

													if (player.equipment[PlayerConstants.SHIELD] == 13744) {
														maxDamage = 15;
													}

													// drain prayer
													if (npc.getType() == 5869 && (player.prayerActive[18] || player.curseActive[9]) || npc.getType() == 5868 && (player.prayerActive[16] || player.curseActive[7]) || npc.getType() == 5867 && (player.prayerActive[17] || player.curseActive[8])) {


														if (player.level[PlayerConstants.PRAYER] <= maxDamage) {
															int damage = maxDamage - player.level[PlayerConstants.PRAYER];

															player.level[PlayerConstants.PRAYER] = 0;
															player.getPA().refreshSkill(PlayerConstants.PRAYER);
															player.getCombat().resetAllPrayers();

															if (damage > 0) {
																player.dealDamage(damage);
																player.handleHitMask(damage, 0, type + 1, 0);
																player.getPA().refreshSkill(PlayerConstants.HITPOINTS);
															}

														} else {
															player.level[PlayerConstants.PRAYER] -= maxDamage;
															player.getPA().refreshSkill(PlayerConstants.PRAYER);
														}

														// drain prayer
													} else {
														player.dealDamage(maxDamage);
														player.handleHitMask(maxDamage, 0, type + 1, 0);
														player.getPA().refreshSkill(PlayerConstants.HITPOINTS);
													}

													container1.stop();

												}, 2);

											}

											container.stop();
										}

									}, index * 4);

									GameCycleTaskHandler.addEvent(npc, new GameCycleTask() {
										@Override
										public void execute(GameCycleTaskContainer container) {

											GameCycleTaskHandler.addEvent(npc, new GameCycleTask() {
												int moved = 0;

												@Override
												public void execute(GameCycleTaskContainer container) {
													npc.moveX = 0;
													npc.moveY += 1;

													npc.getNextNPCMovement();
													npc.updateRequired = true;
													npc.moveX = 0;
													npc.moveY = 0;

													if (moved++ >= 2) {
														ghosts.remove(npc);
														npc.setAttribute("hidden", true);
														npc.isDead = true;
														npc.setRespawn(false);
														container.stop();
													}
												}
											}, 1);
										}
									}, 13);
									container.stop();
								}
							}, 1);

						}
					}
				}, 1);

				ghosts.add(npc);
			}

			getNpc().setSkipCombatTurn(true);

			return;
		}

		// the player is within melee distance
		if (Location.isWithinDistance(player.getLocation(), getNpc().getLocation(), 3)) {
			getNpc().attackType = Misc.random(0, 2);
		} else {
			getNpc().attackType = Misc.random(1, 2);
		}

		getNpc().projectileId = -1;
		if (getNpc().attackType == 1) {
			getNpc().projectileId = 1242;
		} else if (getNpc().attackType == 2) {
			getNpc().projectileId = 1245;
		}

	}

	@Override
	public int getAttackAnimation() {
		return 4492;
	}
}
