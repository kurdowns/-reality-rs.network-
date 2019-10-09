package com.zionscape.server.model.npcs.combat;

import com.zionscape.server.gamecycle.GameCycleTask;
import com.zionscape.server.gamecycle.GameCycleTaskContainer;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCCombat;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.util.Misc;

public class Nex extends NPCCombat {

	private boolean started = false;
	private Phase phase = null;
	private NPC minion = null;
	private boolean healing = false;

	public Nex(NPC npc) {
		super(npc);
	}

	@Override
	public void attackPlayer(Player player) {

		if (!player.getLocation().isWithinDistance(getNpc().getLocation())) {
			getNpc().setSkipCombatTurn(true);
			return;
		}

		if (healing) {
			healing = false;
		}
		// phase changing
		int hpDivided = getNpc().maxHP / 5;

		if (phase == null) {
			started = true;
			phase = Phase.SMOKE;
			minion = NPCHandler.spawnNpc(13451, 2913, 5191, 0, 0, 100, 10, 100, 100);
			minion.setRespawn(false);
		} else if (!phase.equals(Phase.FINAL) && minion.isDead && getNpc().getHP() < hpDivided) {
			phase = Phase.FINAL;
		} else if (!phase.equals(Phase.ICE) && minion.isDead && getNpc().getHP() >= hpDivided && getNpc().getHP() < hpDivided * 2) {
			phase = Phase.ICE;
			minion = NPCHandler.spawnNpc(13454, 2936, 5215, 0, 0, 100, 10, 100, 100);
			minion.setRespawn(false);
		} else if (!phase.equals(Phase.BLOOD) && minion.isDead && getNpc().getHP() >= hpDivided * 2 && getNpc().getHP() < hpDivided * 3) {
			phase = Phase.BLOOD;
			minion = NPCHandler.spawnNpc(13453, 2936, 5192, 0, 0, 100, 10, 100, 100);
			minion.setRespawn(false);
		} else if (!phase.equals(Phase.SHADOW) && minion.isDead && getNpc().getHP() >= hpDivided * 3 && getNpc().getHP() < hpDivided * 4) {
			phase = Phase.SHADOW;
			minion = NPCHandler.spawnNpc(13452, 2914, 5215, 0, 0, 100, 10, 100, 100);
			minion.setRespawn(false);
		}

		if (phase.equals(Phase.SMOKE)) {
			if (player.getAttribute("nex_virus") == null && Misc.random(3) == 0) {
				getNpc().forceChat("Let the virus flow through you!");
				player.setAttribute("nex_virus", true);
				GameCycleTaskHandler.addEvent(player, new GameCycleTask() {
					int cycles = 0;

					@Override
					public void execute(GameCycleTaskContainer container) {

						if(player.isDead()) {
							container.stop();
							return;
						}

						player.forcedChat("Cough");

						for (int i = 0; i < player.level.length; i++) {
							player.level[i] -= Misc.random(1, 10);
							if (player.level[i] < 1) {
								player.level[i] = 1;
							}
							player.getPA().refreshSkill(i);
						}

						cycles++;
						if (cycles >= 6) {
							container.stop();
							player.setAttribute("nex_virus", false);
						}
					}
				}, 17);
			}

			getNpc().attackType = 2;
			getNpc().projectileId = -1;
			getNpc().endGfx = 391;
		}

		if (phase.equals(Phase.SHADOW)) {
			getNpc().attackType = 1;
			getNpc().projectileId = -1;
			getNpc().endGfx = -1;
		}

		if (phase.equals(Phase.BLOOD)) {
			getNpc().attackType = 2;
			getNpc().projectileId = -1;
			getNpc().endGfx = 377;
			// healing
			if (Misc.random(5) == 0) {
				getNpc().forceChat("A siphon will solve this!");
				getNpc().setSkipCombatTurn(true);
				healing = true;
				getNpc().startAnimation(6948);
				return;
			}
			if (player.getAttribute("nex_sacrifice") == null && Misc.random(5) == 0) {
				getNpc().forceChat("I demand a blood sacrifice!");
				player.sendMessage("You are being sacrificed. RUN RUN RUN!");
				player.setAttribute("nex_sacrifice", true);
				GameCycleTaskHandler.addEvent(player, new GameCycleTask() {
					@Override
					public void execute(GameCycleTaskContainer container) {
						if (getNpc().getLocation().getDistance(player.getLocation()) < 5) {
							getNpc().incrementHP(player.level[PlayerConstants.HITPOINTS]);


							player.dealDamage((int) (player.getPA().getActualLevel(PlayerConstants.HITPOINTS) * 0.10));
							player.appearanceUpdateRequired = true;
							player.updateRequired = true;
						}
						player.setAttribute("nex_sacrifice", null);
						container.stop();
					}
				}, 15);
				return;
			}
		}
		if (phase.equals(Phase.ICE)) {
			getNpc().attackType = 2;
			getNpc().projectileId = -1;
			getNpc().endGfx = 360;
			if (Misc.random(6) == 0) {
				getNpc().forceChat("Die now, in a prison of ice!");
				player.gfx0(369);
				int damage = Misc.random(1, 30);

				if (damage > player.level[3]) {
					damage = player.level[3];
				}

				player.dealDamage(damage);
				player.handleHitMask(damage, 100, 2, player.playerId);
				player.getCombat().resetAllPrayers();
				getNpc().setSkipCombatTurn(true);
			}
		}
		if (phase.equals(Phase.FINAL)) {
			if (Misc.random(5) < 2) {
				getNpc().attackType = 0;
				getNpc().projectileId = -1;
				getNpc().endGfx = -1;
			} else {
				getNpc().attackType = 2;
				getNpc().projectileId = -1;
				getNpc().endGfx = -1;
			}
			if (Misc.random(6) == 0 && !getNpc().attributeExists("zaros_heal")) {
				getNpc().forceChat("NOW, THE POWER OF ZAROS!");
				getNpc().incrementHP(hpDivided * 2);
				getNpc().setAttribute("zaros_heal", true);
			}
		}
	}

	@Override
	public void onDeath() {
		getNpc().removeAttribute("zaros_heal");
		getNpc().forceChat("Taste my wrath!");
		for (Player player : PlayerHandler.players) {
			if (player == null) {
				continue;
			}
			if (player.getLocation().getDistance(getNpc().getLocation()) < 5) {

				int damage = 30;

				if (player.level[3] - damage < 0) {
					damage = player.level[3];
				}

				player.dealDamage(damage);
				player.handleHitMask(damage, 30, 2, player.playerId);
			}
		}
		super.onDeath();
	}

	@Override
	public int damageDealtByPlayer(Player player, int damage) {
		if (!started || minion != null && damage > 0 && !minion.isDead) {
			player.sendMessage("A mystical Nex follower stops you from dealing damage.");
			return 0;
		}

		if (healing) {
			getNpc().incrementHP(damage);
			player.sendMessage("Your damage heals Nex.");
		}

		return super.damageDealtByPlayer(player, damage);
	}

	public boolean minionSpawned() {
		return minion != null && !minion.isDead;
	}

	@Override
	public int getDamage() {
		if (phase.equals(Phase.SHADOW)) {
			return 30;
		} else if (phase.equals(Phase.BLOOD)) {
			return 30;
		} else if (phase.equals(Phase.ICE)) {
			return 35;
		} else if (phase.equals(Phase.FINAL)) {
			return 50;
		}
		return 25;
	}

	@Override
	public int getDistanceRequired() {
		if (phase == null) {
			return 6;
		}
		if (phase.equals(Phase.SMOKE)) {
			return 6;
		}
		return super.getDistanceRequired();
	}


	@Override
	public int getAttackDelay() {
		if (healing) {
			return 8;
		}
		return super.getAttackDelay();
	}

	@Override
	public int getAttackAnimation() {

		if (getNpc().attackType == 2) {
			return 6326;
		}

		if (getNpc().attackType == 0) {
			return 6354;
		}

		return 6987;
	}

	/*@Override
	public boolean overrideProtectionPrayers() {
		if (phase.equals(Phase.SHADOW) || phase.equals(Phase.FINAL)) {
			return true;
		}
		return false;
	}*/

	private enum Phase {
		SMOKE, SHADOW, BLOOD, ICE, FINAL
	}

}