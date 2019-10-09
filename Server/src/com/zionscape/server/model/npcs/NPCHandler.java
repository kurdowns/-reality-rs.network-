package com.zionscape.server.model.npcs;

import com.zionscape.server.Config;
import com.zionscape.server.Server;
import com.zionscape.server.ServerEvents;
import com.zionscape.server.cache.Collision;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Direction;
import com.zionscape.server.model.InstancesArea;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.GreeGree;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.content.minigames.Barrows;
import com.zionscape.server.model.content.minigames.FightCaves;
import com.zionscape.server.model.content.minigames.PestControl;
import com.zionscape.server.model.content.minigames.castlewars.CastleWars;
import com.zionscape.server.model.content.minigames.zombies.Zombies;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.npcs.drops.Drop;
import com.zionscape.server.model.npcs.drops.NPCDropsHandler;
import com.zionscape.server.model.players.*;
import com.zionscape.server.model.players.combat.Weaknesses;
import com.zionscape.server.model.players.skills.hunter.Hunter;
import com.zionscape.server.model.players.skills.slayer.Slayer;
import com.zionscape.server.model.players.skills.slayer.Tasks;
import com.zionscape.server.tick.Tick;
import com.zionscape.server.util.DatabaseUtil;
import com.zionscape.server.util.Misc;
import com.zionscape.server.util.pathfinding.CollisionMap;
import com.zionscape.server.util.pathfinding.NpcPathFinder;
import com.zionscape.server.world.ItemDrops;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class NPCHandler {

	static final int[] dragons = { 50, 53, 54, 55, 941, 1590, 1591, 1592 };
	private static final int BANDOS = 6260, ARMADYL = 6222, SARADOMIN = 6247, ZAMORAK = 6203;
	public static int maxNPCs = 4000;
	public static NPC[] npcs = new NPC[maxNPCs];
	private static Map<Integer, NpcDefinition> npcDefinitions = new HashMap<>();
	private static Logger logger = Logger.getLogger(NPCHandler.class.getName());
	private static AtomicInteger atomicInteger = new AtomicInteger();

	public static void load() throws Exception {
		try (Connection connection = DatabaseUtil.getConnection()) {
			logger.info("loading npc definitions");

			try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM npc_defs")) {
				try (ResultSet results = ps.executeQuery()) {
					while (results.next()) {

						NpcDefinitionBuilder builder = new NpcDefinitionBuilder();

						builder.setId(results.getInt("id"));
						builder.setName(results.getString("name"));
						builder.setSize(results.getInt("size"));
						builder.setCombatLevel(results.getInt("combat_level"));
						builder.setStandAnim(results.getInt("stand_anim"));
						builder.setWalkAnim(results.getInt("walk_anim"));
						builder.setHealth(results.getInt("health"));
						builder.setMaxDamage(results.getInt("max_damage"));
						builder.setAttack(results.getInt("attack"));
						builder.setDefence(results.getInt("defence"));
						builder.setAttackAnimation(results.getInt("attack_animation"));
						builder.setDeathAnimation(results.getInt("death_animation"));
						builder.setBlockAnimation(results.getInt("block_animation"));
						builder.setProjectileId(results.getInt("projectile_id"));
						builder.setEndGfxId(results.getInt("end_gfx_id"));
						builder.setAggressive(results.getBoolean("aggressive"));
						builder.setHitDelay(results.getInt("hit_delay"));
						builder.setAttackDelay(results.getInt("attack_delay"));
						builder.setRespawnTime(results.getInt("respawn_time"));
						builder.setWeakness(Weaknesses.stringToWeakness(results.getString("weakness")));

						String combatType = results.getString("combat_type");
						if (combatType != null) {
							switch (combatType) {
							case "MELEE":
								builder.setCombatType(NpcDefinition.CombatType.MELEE);
								break;
							case "RANGE":
								builder.setCombatType(NpcDefinition.CombatType.RANGE);
								break;
							case "MAGIC":
								builder.setCombatType(NpcDefinition.CombatType.MAGIC);
								break;
							}
						}

						npcDefinitions.put(results.getInt("id"), builder.createNpcDefinition());
					}
				}
			}

			logger.info(String.format("loaded %d npc definitions", npcDefinitions.size()));

			logger.info("loading npc spawns");

			int count = 0;
			try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM npc_spawns")) {
				try (ResultSet results = ps.executeQuery()) {
					while (results.next()) {
						NpcDefinition definition = getNpcDefinition(results.getInt("npc_id"));
						newNPC(results.getInt("npc_id"), results.getInt("x"), results.getInt("y"), results.getInt("z"),
								results.getInt("walk"), definition == null ? 0 : definition.getHealth(),
								definition == null ? 0 : definition.getMaxDamage(),
								definition == null ? 0 : definition.getAttack(),
								definition == null ? 0 : definition.getDefence(),
								Direction.getDirection(results.getString("face")));
						count++;
					}
				}
			}
			logger.info(String.format("loaded %d npc spawns", count));
		}

		Hunter.spawnInitialImps();
	}

	public static boolean canRemove(Player player, NPC npc) {
		if (player == null) {
			return true;
		}
		if (player.heightLevel != npc.heightLevel) {
			return true;
		}
		if (FightCaves.isFightCaveNpc(npc)) {
			return false;
		}
		if (player.respawnTimer > 0) {
			return true;
		}
		if (!player.goodDistance(npc.getX(), npc.getY(), player.getX(), player.getY(), 20)) {
			return true;
		}
		return false;
	}

	/**
	 * Npc Follow Player
	 */
	public static int getMove(int place1, int place2) {
		if ((place1 - place2) == 0) {
			return 0;
		} else if ((place1 - place2) < 0) {
			return 1;
		} else if ((place1 - place2) > 0) {
			return -1;
		}
		return 0;
	}

	public static boolean isMinion(NPC npc, int god) {
		switch (god) {
		case BANDOS:
			return npc.type == 9184 || npc.type >= 6268 && npc.type <= 6283;
		case ARMADYL:
			return npc.type >= 6229 && npc.type <= 6246;
		case SARADOMIN:
			return npc.type >= 6254 && npc.type <= 6259;
		case ZAMORAK:
			return npc.type == 49 || npc.type == 3406 || npc.type >= 6210 && npc.type <= 6221;
		}
		return false;
	}

	public static void stompStrykewyrm(Player player, NPC npc) {
		if (npc == null) {
			return;
		}
		// final int[][] strykewyrm = {{9462, 9463, 93}, {9464, 9465, 77}, {9466, 9467,
		// 73}};
		final int[][] strykewyrm = { { 9462, 9463, 93 }, { 9464, 9465, 77 }, { 9466, 9467, 73 } };

		Server.getTickManager().submit(new Tick(1) {
			int timer = 2;

			public void execute() {
				if (timer == 2) {
					player.startAnimation(4278);
				} else if (timer == 0) {
					for (int j = 0; j < strykewyrm.length; j++) {
						if (npc.type == strykewyrm[j][0])
							npc.requestTransform(strykewyrm[j][1]);
					}
					npc.killerId = player.playerId;
					npc.facePlayer(player.playerId);
					stop();
					return;
				}
				if (timer > 0) {
					timer--;
				}
			}

		});
	}

	public static void appendKillCount(NPC npc) {
		Player player = PlayerHandler.players[npc.killedBy];
		if (player == null)
			return;
		if (isMinion(npc, BANDOS)) {
			player.bandosKills++;
		} else if (isMinion(npc, ARMADYL)) {
			player.armadylKills++;
		} else if (isMinion(npc, SARADOMIN)) {
			player.saradominKills++;
		} else if (isMinion(npc, ZAMORAK)) {
			player.zamorakKills++;
		}
	}

	public static void faceDirection(NPC npc, Direction direction) {
		if (direction.equals(Direction.WEST)) {
			npc.turnNpc(npc.absX - 1, npc.absY);
		}
		if (direction.equals(Direction.EAST)) {
			npc.turnNpc(npc.absX + 1, npc.absY);
		}
		if (direction.equals(Direction.SOUTH)) {
			npc.turnNpc(npc.absX, npc.absY - 1);
		}
		if (direction.equals(Direction.NORTH)) {
			npc.turnNpc(npc.absX, npc.absY + 1);
		}
	}

	/**
	 * Slayer Experience
	 */
	// public static void appendSlayerExperience(int i) {
	// Player c = PlayerHandler.players[npcs[i].killedBy];
	// if (c != null) {
	// if (c.slayerTask == npcs[i].type) {
	// c.taskAmount--;
	// c.getPA().addSkillXP(npcs[i].maxHP * Config.SLAYER_EXPERIENCE, 18);
	// if (c.taskAmount <= 0) {
	// if (npcs[i].type == 1645 || npcs[i].type == 1618 || npcs[i].type == 1643
	// || npcs[i].type == 941 || npcs[i].type == 119 || npcs[i].type == 82
	// || npcs[i].type == 52 || npcs[i].type == 1612 || npcs[i].type == 117
	// || npcs[i].type == 1265 || npcs[i].type == 112 || npcs[i].type == 125) {
	// c.getPA().addSkillXP(npcs[i].maxHP * 10 * Config.SLAYER_EXPERIENCE, 18);
	// int points = Misc.random(5) + 5;
	// c.slayerPoints += points;
	// c.slayerTask = -1;
	// c.sendMessage("You completed your MEDIUM slayer task. Please see a slayer
	// master to get a new one.");
	// if (!c.getPA().wearing2xRing()) {
	// c.sendMessage("You have received " + points + " slayer points for You now
	// have "
	// + c.slayerPoints + " points");
	// } else {
	// c.sendMessage("You have received " + points * 2 + " as it is doubled. You now
	// have "
	// + c.slayerPoints + " points");
	// c.slayerPoints += points;
	// }
	// }
	// if (npcs[i].type == 1624 || npcs[i].type == 1610 || npcs[i].type == 1613
	// || npcs[i].type == 1615 || npcs[i].type == 53 || npcs[i].type == 84
	// || npcs[i].type == 49 || npcs[i].type == 1618 || npcs[i].type == 941
	// || npcs[i].type == 82 || npcs[i].type == 2783 || npcs[i].type == 1341) {
	// c.getPA().addSkillXP(npcs[i].maxHP * 12 * Config.SLAYER_EXPERIENCE, 18);
	// int points = Misc.random(10) + 10;
	// c.slayerPoints += points;
	// c.slayerTask = -1;
	// c.sendMessage("You completed your HARD slayer task. Please see a slayer
	// master to get a new one.");
	// if (!c.getPA().wearing2xRing()) {
	// c.sendMessage("You have received " + points + " slayer points for You now
	// have "
	// + c.slayerPoints + " points");
	// } else {
	// c.sendMessage("You have received " + points * 2 + " as it is doubled. You now
	// have "
	// + c.slayerPoints + " points");
	// c.slayerPoints += points;
	// }
	// }
	// if (npcs[i].type == 1648 || npcs[i].type == 117 || npcs[i].type == 1265
	// || npcs[i].type == 90 || npcs[i].type == 103 || npcs[i].type == 78
	// || npcs[i].type == 119 || npcs[i].type == 18 || npcs[i].type == 101
	// || npcs[i].type == 1265 || npcs[i].type == 181 || npcs[i].type == 55) {
	// c.getPA().addSkillXP(npcs[i].maxHP * 8 * Config.SLAYER_EXPERIENCE, 18);
	// int points = Misc.random(2) + 1;
	// c.slayerPoints += points;
	// c.slayerTask = -1;
	// c.sendMessage("You completed your EASY slayer task. Please see a slayer
	// master to get a new one.");
	// if (!c.getPA().wearing2xRing()) {
	// c.sendMessage("You have received " + points + " slayer points for You now
	// have "
	// + c.slayerPoints + " points");
	// } else {
	// c.sendMessage("You have received " + points * 2 + " as it is doubled. You now
	// have "
	// + c.slayerPoints + " points");
	// c.slayerPoints += points;
	// }
	// }
	// }
	// }
	// }
	// }
	@SuppressWarnings("unused")
	public static void applyDamage(NPC npc) {

		if (npc == null) {
			return;
		}
		if (PlayerHandler.players[npc.oldIndex] == null) {
			return;
		}
		if (npc.isDead) {
			return;
		}
		Player player = PlayerHandler.players[npc.oldIndex];

		/*
		 * if(c.rights > 3) { return; }
		 */

		if (NPCConfig.multiAttacks(npc)) {
			multiAttackDamage(npc);
			return;
		}
		/*
		 * if (player.playerIndex <= 0 && player.npcIndex <= 0) { && DONT CLOSE if
		 * (player.autoRet == 1) { player.npcIndex = npc.id; player.faceUpdate(npc.id);
		 * } }
		 */
		if (player.attackTimer <= 3 || player.attackTimer == 0 && player.npcIndex == 0 && player.oldNpcIndex == 0) {
			player.startAnimation(player.getCombat().getBlockEmote());
		}

		if (player.respawnTimer <= 0) {

			if (npc.doSpecial) {
				Player plr = PlayerHandler.players[npc.getOwnerId()];

				if (plr != null) {
					plr.getFamiliar().getSpecial().onFamiliarDamageGiven(player);
				}
			}

			int damage = 0;
			int combatType = 0;
			if (npc.attackType == 0) { // melee

				damage = Misc.random(NPCConfig.getMaxHit(npc));
				if (10 + Misc.random(player.getCombat().calculateMeleeDefence(npc)) > Misc.random(npc.attack)) {
					damage = 0;
				} else if (player.prayerActive[18]) { // protect from melee
					if (npc.type == 8596 || npc.getCombat() != null && npc.getCombat().overrideProtectionPrayers()) // sae
																													// as
																													// the
																													// prayer?
						damage *= 0.75;
					else
						damage = 0;

				} else if (player.curseActive[9]) {
					if (npc.type == 8596 || npc.getCombat() != null && npc.getCombat().overrideProtectionPrayers())
						damage *= 0.75; // 1/4 damange less
					else
						damage = 0;
				}

				// slayer
				if (Slayer.isFightingSlaverNPC(player, npc, Tasks.COCKATRICE)) {
					if (player.equipment[PlayerConstants.SHIELD] != 4156) {
						damage = (int) Math.floor(player.getPA().getLevelForXP(PlayerConstants.HITPOINTS) * 0.10);
					}
				}
				if (Slayer.isFightingSlaverNPC(player, npc, Tasks.BASILISK)) {
					if (player.equipment[PlayerConstants.SHIELD] != 4156) {
						damage = (int) Math.floor(player.getPA().getLevelForXP(PlayerConstants.HITPOINTS) * 0.10);
					}
				}

				combatType = 1;
			}
			if (npc.attackType == 1) { // range
				damage = Misc.random(NPCConfig.getMaxHit(npc));
				if (10 + Misc.random(player.getCombat().calculateRangeDefence()) > Misc.random(npc.attack)) {
					damage = 0;
				} else if (player.prayerActive[17] || player.curseActive[8]) { // range protection
					if (npc.getCombat() != null && npc.getCombat().overrideProtectionPrayers()) {
						damage *= 0.75;
					} else {
						damage = 0;
					}
				}

				if (Slayer.isFightingSlaverNPC(player, npc, Tasks.DUST_DEVIL)) {
					if (player.equipment[PlayerConstants.HAT] != 4164) {
						damage += (int) Math.floor(player.getPA().getLevelForXP(PlayerConstants.HITPOINTS) * 0.10);
					}
				}
				if (Slayer.isFightingSlaverNPC(player, npc, Tasks.COCKATRICE)) {
					if (player.equipment[PlayerConstants.SHIELD] != 4156) {
						damage = (int) Math.floor(player.getPA().getLevelForXP(PlayerConstants.HITPOINTS) * 0.10);
					}
				}
				if (Slayer.isFightingSlaverNPC(player, npc, Tasks.BASILISK)) {
					if (player.equipment[PlayerConstants.SHIELD] != 4156) {
						damage = (int) Math.floor(player.getPA().getLevelForXP(PlayerConstants.HITPOINTS) * 0.10);
					}
				}

				if (player.equipment[PlayerConstants.SHIELD] == 9731 && damage > 0) {
					damage = (int) Math.floor((double) damage * 0.85);
				}

				combatType = 2;
			}

			if (npc.attackType == 2) { // magic

				damage = Misc.random(NPCConfig.getMaxHit(npc));
				boolean magicFailed = false;
				if (10 + Misc.random(player.getCombat().mageDef()) > Misc.random(npc.attack)) {
					damage = 0;
					magicFailed = true;
				} else if (!NPCConfig.overridesPrayerCompletely(npc.type)
						&& (player.prayerActive[16] || player.curseActive[7])) { // magic protection
					if (npc.getCombat() != null && npc.getCombat().overrideProtectionPrayers()) {
						damage *= 0.75;
					} else {
						damage = 0;
					}

					if (damage == 0) {
						magicFailed = true;
					}
				}

				if (Slayer.isFightingSlaverNPC(player, npc, Tasks.ABERRANT_SPECTRE)) {
					if (player.equipment[PlayerConstants.HAT] != 4168) {
						magicFailed = false;
						damage = (int) Math.floor(player.getPA().getLevelForXP(PlayerConstants.HITPOINTS) * 0.10);
					}
				}
				if (Slayer.isFightingSlaverNPC(player, npc, Tasks.BANSHEE)) {
					if (player.equipment[PlayerConstants.HAT] != 4166) {
						magicFailed = false;
						damage = (int) Math.floor(player.getPA().getLevelForXP(PlayerConstants.HITPOINTS) * 0.10);
					}
				}
				if (Slayer.isFightingSlaverNPC(player, npc, Tasks.COCKATRICE)) {
					if (player.equipment[PlayerConstants.SHIELD] != 4156) {
						damage = (int) Math.floor(player.getPA().getLevelForXP(PlayerConstants.HITPOINTS) * 0.10);
					}
				}
				if (Slayer.isFightingSlaverNPC(player, npc, Tasks.BASILISK)) {
					if (player.equipment[PlayerConstants.SHIELD] != 4156) {
						damage = (int) Math.floor(player.getPA().getLevelForXP(PlayerConstants.HITPOINTS) * 0.10);
					}
				}

				if (npc.endGfx > 0 && (!magicFailed || isFightCaveNpc(npc)) && npc.type != 6247) {
					player.gfx100(npc.endGfx);
				} else {
					player.gfx100(85);
				}
				if (npc.type == 6247) {
					if (npc.endGfx > 0 && (!magicFailed || isFightCaveNpc(npc))) {
						player.gfx0(npc.endGfx);
					}
				}
				combatType = 3;
			}
			if (npc.type == 8596) { // avatar
				final int random = Misc.random(15);// lower # = MORE COMMon prayer drain rite yeh ok
				switch (random) {
				case 0:
					for (Player plr : PlayerHandler.players) {
						if (plr != null && npc.isWithinDistance(plr.absX, plr.absY, 15)) {// and the 4 is the
							// number of
							// squares?yh
							plr.level[5] = 0;
							plr.getPA().refreshSkill(5);
							plr.sendMessage("The Avatar has drained your prayer!");
							plr.gfx100(1000);
						}
					}
					break;
				}
			}
			if (npc.type == 5452) { // wizards for avatar
				final int random = Misc.random(0);// lower number = higher chance of freezing
				if (random == 0) {
					final Player victim = PlayerHandler.players[npc.killerId];
					if (victim != null) {
						victim.freezeTimer = 8; // second though
						victim.sendMessage("You have been frozen by the Icelord!");
						victim.gfx100(363);
					}
				}
			}
			if (npc.type == 7133) {// bork
				damage = Misc.random(npc.maxHit);
				if (10 + Misc.random(player.getCombat().calculateMeleeDefence(npcs)) > Misc.random(npc.attack)) {
					damage = 0;
				} else if (player.prayerActive[18]) { // protect from melee
					// damage = 0;
					damage = Misc.random(20);
				} else if (player.curseActive[9]) {
					damage = Misc.random(20);
					// damage = 0;
				}
				combatType = 1;
			}
			/*
			 * if (npc.type == FightCaves.TZTOK_JAD && player.jadHealers == null &&
			 * npc.getHP() <= (npc.maxHP / 2)) { player.jadHealers = new NPC[4]; for (int j
			 * = 0; j < player.jadHealers.length; j++) { final NPC healer = spawnNpc(player,
			 * FightCaves.YT_HUR_KOT, npc.absX, npc.absY - (3 + j), npc.heightLevel, 0, 250,
			 * 7, 100, 100, false, false); healer.jad = npc; player.jadHealers[j] = healer;
			 * } }
			 */
			// if (npcs[i].type == 941) {//bork minions
			// damage = Misc.random(20);
			// }
			if (npc.type == 7135) {// bork minions
				damage = Misc.random(npc.maxHit);
				if (10 + Misc.random(player.getCombat().calculateMeleeDefence(npc)) > Misc.random(npc.attack)) {
					damage = 0;
				} else if (player.prayerActive[18]) { // protect from melee
					// damage = 0;
					damage = Misc.random(5);
				} else if (player.curseActive[9]) {
					damage = Misc.random(5);
					// damage = 0;
				}
				combatType = 1;
			}
			if (npc.type == 13478) {// revs
				damage = Misc.random(npc.maxHit);
				if (10 + Misc.random(player.getCombat().calculateMeleeDefence(npc)) > Misc.random(npc.attack)) {
					damage = 0;
				} else if (player.prayerActive[18]) { // protect from melee
					// damage = 0;
					damage = Misc.random(10);
				} else if (player.curseActive[9]) {
					damage = Misc.random(10);
					// damage = 0;
				}
				combatType = 1;
			}

			if (npc.attackType == 8) { // skeletal wyvern fire attack
				double anti = player.getPA().antiFire();
				if (anti == 0) {
					damage = Misc.random(30) + 1;
					player.sendMessage("You are badly burnt by the wyvern's ice attack!");
				} else if (anti == 1) {
					damage = 0;
				} else {
					damage = Misc.random(30) + 1;
					damage = (int) Math.round((double) damage * anti);
				}
				player.gfx100(npc.endGfx);
				combatType = 3;
			}

			if (npc.attackType == 3) { // fire breath
				double anti = player.getPA().antiFire();
				if (anti == 0) {
					damage = Misc.random(30) + 1;
					player.sendMessage("You are badly burnt by the dragon fire!");
				} else if (anti == 1) {
					damage = 0;
				} else {
					damage = Misc.random(30) + 1;
					damage = (int) Math.round((double) damage * anti);
				}
				player.gfx100(npc.endGfx);
				combatType = 3;
				if (npc.type == 50) {
					switch (npc.kbdAttack) {
					case 3:
						for (int i1 = 0; i1 < 6; i1++) {
							if (player.level[i1] > 0) {
								player.level[i1]--;
								player.getPA().refreshSkill(i1);
							}
						}
						player.sendMessage("You have been shocked!");
						break;
					case 2:
						if (player.freezeTimer < 0) {
							player.sendMessage("You have been frozen!");
							player.freezeTimer = 10;
						}
						break;
					case 1:
						if (Misc.random(100) <= 30) {
							if (player.poisonDelay == -1 && player.poisonDamage == 0) {
								player.poisonDelay = 36;
								player.poisonDamage = 8;
								player.sendMessage("You have been poisoned!");
							}
						}
						break;
					}
				}
			}

			if (npc.attackType == 4) { // corp beast mage
				damage = Misc.random(npc.maxHit);
				boolean magicFailed = false;
				if (player.prayerActive[16]) { // protect from magic
					damage = Misc.random(10);
				} else if (player.curseActive[7]) {
					damage = Misc.random(10);
				} else if (10 + Misc.random(player.getCombat().mageDef()) > Misc.random(npc.attack)) {
					damage = Misc.random(10);
				}
				player.gfx0(npc.endGfx);
				combatType = 3;
			}
			if (npc.attackType == 5) { // Torm Mage
				damage = Misc.random(npc.maxHit);
				boolean magicFailed = false;
				if (player.prayerActive[16]) { // protect from magic
					damage = Misc.random(10);
				} else if (player.curseActive[7]) {
					damage = Misc.random(10);
				} else if (10 + Misc.random(player.getCombat().mageDef()) > Misc.random(npc.attack)) {
					damage = Misc.random(10);
				}
				player.gfx0(npc.endGfx);
				combatType = 3;
			}
			if (npc.attackType == 6) { // Torm Range
				damage = Misc.random(npc.maxHit);
				if (10 + Misc.random(player.getCombat().calculateRangeDefence()) > Misc.random(npc.attack)) {
					damage = Misc.random(5);
				} else if (player.prayerActive[17]) { // protect from range
					damage = Misc.random(5);
				} else if (player.curseActive[8]) {
					damage = Misc.random(5);
				}
				combatType = 2;
			}
			if (npc.attackType == 7) { // Giant mole
				damage = Misc.random(npc.maxHit);
				boolean magicFailed = false;
				if (10 + Misc.random(player.getCombat().mageDef()) > Misc.random(npc.attack)) {
					damage = 0;
				} else if (player.prayerActive[16]) { // protect from magic
					damage = Misc.random(5);
				} else if (player.curseActive[7]) {
					damage = Misc.random(5);
				}
				player.gfx0(npc.endGfx);
				combatType = 3;
			}
			if (player.equipment[Player.playerWeapon] == 15486 && damage >= 1 && player.SolProtect >= 1
					&& npc.attackType == 0) {
				damage = (int) damage / 2;
				player.gfx0(1958);
			}

			if (npc.overrideDamage > 0) {
				damage = npc.overrideDamage;

				npc.overrideDamage = 0;
			}

			if (player.level[3] - damage < 0) {
				damage = player.level[3];
			}

			// ring of recoil
			if (player.equipment[PlayerConstants.RING] == 2550) {
				int recoilDamage = (int) Math.round((double) damage * 0.10);
				if (recoilDamage > 0) {
					npc.handleHitMask(recoilDamage, damage, 1, 0);
					npc.dealdamage(player.playerIndex, recoilDamage);
				}
			}

			if (player.equipment[player.playerShield] == 13740) {
				player.level[5] -= damage * 0.15;
				if (player.level[5] >= 1) {
					damage = (int) damage * 70 / 100;
				}
				player.getPA().refreshSkill(5);
				if (player.level[5] <= 0) {
					player.level[5] = 0;
				}
			}
			if (player.equipment[player.playerShield] == 13742) {
				int random = Misc.random(9);
				if (random >= 0 && random <= 6) {
					damage = (int) damage * 75 / 100;
				}
			}

			handleSpecialEffects(player, npc, damage);
			// FightCaves.tzKihEffect(c, i, damage);
			player.logoutDelay = System.currentTimeMillis(); // logout delay

			// c.setHitDiff(damage);
			player.handleHitMask(damage, npc.maxHit, combatType, npc.oldIndex);
			player.level[3] -= damage;
			player.getPA().refreshSkill(3);
			player.updateRequired = true;
			// c.setHitUpdateRequired(true);
		}
	}

	public static void applyNpcDamage(NPC npc, NPC other) {

		/**
		 * checks before applying damage
		 */

		/**
		 * attacking checks
		 */
		if (npc == null || other == null) {
			return;
		}
		if (npc.isDead || other.isDead) {
			return;
		}

		int distanceRequired = distanceRequired(npc);
		if (!goodDistance(npc.getX(), npc.getY(), other.getX(), other.getY(), distanceRequired)) {
			return;
		}

		if (!Collision.canProjectileMove(npc.absX, npc.absY, other.absX, other.absY, other.heightLevel, 1, 1)) {
			return;
		}

		if (npc.doSpecial) {
			Player player = PlayerHandler.players[npc.getOwnerId()];

			if (player != null) {
				player.getFamiliar().getSpecial().onFamiliarDamageGiven(other);
			}
		}

		int combatType = 0;
		int damage = npc.overrideDamage > 0 ? npc.overrideDamage : Misc.random(npc.maxHit);

		switch (npc.attackType) {
		case 0: // melee
			if (npc.overrideDamage == 0 && Misc.random(npc.attack) < Misc.random(other.defence)) {
				damage = 0;
			}
			combatType = 1;
			break;
		case 1: // range
			if (npc.overrideDamage == 0 && Misc.random(npc.attack) < Misc.random(other.defence)) {
				damage = 0;
			}
			combatType = 2;
			break;
		case 2: // magic
			boolean magicFailed = false;
			if (npc.overrideDamage == 0 && Misc.random(npc.attack) < Misc.random(other.defence)) {
				damage = 0;

				magicFailed = true;
			}

			if (npc.endGfx > 0 && !magicFailed) {
				other.gfx100(npc.endGfx);
			} else {
				other.gfx100(85);
			}
			combatType = 3;
			break;
		case 3: // fire breath
			damage = Misc.random(30) + 10;
			// other.gfx100(npc.endGfx);
			combatType = 3;
			break;
		}

		if (damage > other.getHP()) {
			damage = other.getHP();
		}

		other.handleHitMask(damage, npc.maxHit, combatType, other.id);
		other.dealdamage(-1, damage);
		other.updateRequired = true;
		npc.overrideDamage = 0;
	}

	public static void attackNpc(NPC npc, NPC other) {

		// do our little checks here

		/**
		 * attacking checks
		 */
		if (npc == null || other == null) {
			npc.underAttack = false;
			npc.attackingNpcId = 0;
			return;
		}
		if (npc.isDead || other.isDead) {
			npc.underAttack = false;
			npc.attackingNpcId = 0;
			return;
		}

		int distanceRequired = distanceRequired(npc);
		if (!goodDistance(npc.getX(), npc.getY(), other.getX(), other.getY(), distanceRequired)) {
			return;
		}

		if (!Collision.canProjectileMove(npc.absX, npc.absY, other.absX, other.absY, other.heightLevel, 1, 1)) {
			return;
		}

		/**
		 * Attacking code
		 */
		npc.attackTimer = NPCConfig.getNpcAttackDelay(npc);
		npc.hitDelayTimer = NPCConfig.getHitDelay(npc);

		npc.attackType = 0;

		// Summoning attack hook

		/**
		 * todo figure out if we need magic / range attacks for npc vs npc
		 */
		loadSpell(npc);

		if (npc.attackType == 3) {
			npc.hitDelayTimer += 2;
		}
		if (npc.attackType == 7) {
			npc.hitDelayTimer += 2;
		}
		if (npc.attackType == 4) {
			npc.hitDelayTimer += 1;
		}

		if (npc.doSpecial) {
			Player plr = PlayerHandler.players[npc.getOwnerId()];

			if (plr != null) {
				plr.getFamiliar().getSpecial().onFamiliarAttack(other);
			}
		}

		if (npc.projectileId > 0) {
			List<Player> closePlayers = Arrays.asList(PlayerHandler.players).stream()
					.filter(x -> x != null && x.getLocation().isViewableFrom(npc.getLocation()))
					.collect(Collectors.toList());

			int nX = npc.getX() + NPCConfig.offset(npc);
			int nY = npc.getY() + NPCConfig.offset(npc);
			int pX = other.getX();
			int pY = other.getY();
			int offX = (nY - pY) * -1;
			int offY = (nX - pX) * -1;
			int projectileSpeed = getProjectileSpeed(npc.id);

			for (Player player : closePlayers) {
				player.getPA().createProjectile(nX, nY, offX, offY, 50, projectileSpeed, npc.projectileId, 43, 31,
						npc.attackingNpcId + 1, 65);
			}
		}

		startAnimation(NPCAnimations.getAttackEmote(npc.id), npc.id);
	}

	/**
	 * NPC Attacking Player
	 */
	public static void attackPlayer(Player player, NPC npc) {
		if (npc == null) {
			return;
		}
		if (npc.type >= 6142 && npc.type <= 6145) {
			return;
		}
		if (npc.isDead) {
			return;
		}
		if (player.isDead || player.level[PlayerConstants.HITPOINTS] <= 0) {
			npc.killerId = 0;
			return;
		}
		if (npc.getOwnerId() == -1 && !Areas.inMulti(npc) && npc.underAttackBy > 0
				&& npc.underAttackBy != player.playerId) {
			npc.killerId = 0;
			return;
		}
		if (npc.getOwnerId() == -1 && !Areas.inMulti(npc) && (player.underAttackBy > 0
				|| (player.underAttackByNpcID > 0 && player.underAttackByNpcID != npc.id))) {
			npc.killerId = 0;
			return;
		}
		if (npc.heightLevel != player.heightLevel) {
			npc.killerId = 0;
			return;
		}
		npc.facePlayer(player.playerId);

		// load combat definition
		if (npc.getDefinition() != null && npc.getDefinition().getCombatType() != null && npc.getCombat() == null) {
			NpcDefinition.CombatType type = npc.getDefinition().getCombatType();
			if (type.equals(NpcDefinition.CombatType.MELEE)) {
				npc.attackType = 0;
			} else if (type.equals(NpcDefinition.CombatType.RANGE)) {
				npc.attackType = 1;
			} else if (type.equals(NpcDefinition.CombatType.MAGIC)) {
				npc.attackType = 2;
			}

			npc.projectileId = npc.getDefinition().getProjectileId();
			npc.endGfx = npc.getDefinition().getEndGfxId();
		}

		npc.attackType = 0;

		loadSpell(npc);

		if (npc.getCombat() != null) {
			npc.getCombat().attackPlayer(player);
		}

		if (npc.skipCombatTurn()) {
			npc.setSkipCombatTurn(false);
			return;
		}

		int distanceRequired = distanceRequired(npc);
		if (!Zombies.inGameArea(npc.getLocation()) && !isFightCaveNpc(npc)) {
			double distance = npc.getLocation().getDistance(Location.create(npc.makeX, npc.makeY));

			// were at the max we can walk
			if (distance >= Config.NPC_FOLLOW_DISTANCE
					&& !goodDistance(npc.getX(), npc.getY(), player.getX(), player.getY(), distanceRequired)) {
				npc.walkingHome = true;
				npc.randomWalk = true;
				return;
			}

			npc.randomWalk = false;
			npc.walkingHome = false;
		}
		if (goodDistance(npc.getX(), npc.getY(), player.getX(), player.getY(), distanceRequired)) {

			// if (distanceRequired > 1) {

			boolean checkClipping = true;

			if (npc.type == 8528) {
				checkClipping = false;
			}
			if (npc.type == 5862) { // cerb
				checkClipping = false;
			}
			// zulrah hit the player in the safe spots when using projectiles
			if (npc.type >= 2042 && npc.type <= 2045 && npc.attackType > 0) {
				if (player.absX == 2273 && player.absY == 3071 || player.absX == 2263 && player.absY == 3071) {
					checkClipping = false;
				}
			}

			if (checkClipping && !Collision.canProjectileMove(npc.absX + NPCConfig.offset(npc),
					npc.absY + NPCConfig.offset(npc), player.absX, player.absY, player.heightLevel, 1, 1)) {
				if (npc.type == 14205 && Zombies.inGameArea(npc.getLocation()) && npc.absX == 4132
						&& (npc.absY == 3993 || npc.absY == 3994)) {
					if (!((player.absX + 1) == npc.absX && (player.absY == npc.absY || player.absY - 1 == npc.absY
							|| player.absY + 1 == npc.absY))) {
						return;
					}
				}
			}
			// }
			/**
			 * if(npc.type == 2745){ FightCave.proccesJad(c, i); return; } else
			 * if(FightCave.isCaveNpcs(i)){ FightCave.proccesNPCS(c, i); return; }
			 */
			if (player.respawnTimer <= 0) {

				if (npc.type == 7133) { // Bork
					try {
						if (npc.getHP() < (npc.maxHP / 4 + npc.maxHP / 2) && npc.bork == 0) {
							if (Misc.random(5) >= 3) {
								npc.forceChat("Come to my aid, brothers!");
								// c.sendMessage("send chat");
								int r = Misc.random(9);
								for (int m = 0; m < r; m++) {
									spawnNpc(player, 7135, 2698 + Misc.random(4), 9999 + Misc.random(4), 0, 1, 100, 10,
											175, 70, true, false);
									// c.getPA().spellTeleport(3565+Misc.random(2), 3314+Misc.random(2), 0);
									// spawnNpc(c, 7135, npc.absX+Misc.random(4), npc.absX+Misc.random(4),
									// 0, 1, 100, 10, 70, 40, true, false);
									// spawnNpc(c, 7135, 3436, 3142, 0, 1, 100, 10, 70, 40, true, false);
									// player.sendMessage("sent brothers");
								}
								npc.bork = 1;
								// return;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				npc.facePlayer(player.playerId);
				npc.attackTimer = NPCConfig.getNpcAttackDelay(npc);
				npc.hitDelayTimer = NPCConfig.getHitDelay(npc);
				if (npc.attackType == 3) {
					npc.hitDelayTimer += 2;
				}
				if (npc.attackType == 7) {
					npc.hitDelayTimer += 2;
				}
				if (npc.attackType == 4) {
					npc.hitDelayTimer += 1;
				}

				if (npc.doSpecial) {
					Player plr = PlayerHandler.players[npc.getOwnerId()];

					if (plr != null) {
						plr.getFamiliar().getSpecial().onFamiliarAttack(player);
					}
				}

				if (NPCConfig.multiAttacks(npc)) {
					multiAttackGfx(npc.id, npc.projectileId);
					startAnimation(NPCAnimations.getAttackEmote(npc.id), npc.id);
					npc.oldIndex = player.playerId;
					return;
				}
				if (npc.projectileId > 0) {
					int nX = npc.getX() + NPCConfig.offset(npc);
					int nY = npc.getY() + NPCConfig.offset(npc);
					int pX = player.getX();
					int pY = player.getY();
					int offX = (nY - pY) * -1;
					int offY = (nX - pX) * -1;
					player.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, getProjectileSpeed(npc.id),
							npc.projectileId, 43, 31, -player.getId() - 1, 65);
				}
				player.underAttackByNpcID = npc.id;
				player.singleCombatDelay2 = System.currentTimeMillis();
				npc.oldIndex = player.playerId;
				startAnimation(NPCAnimations.getAttackEmote(npc.id), npc.id);
				player.getPA().removeAllWindows();
			}
		}
	}

	private static void appendKolodion(NPC npc) {
		Player c = PlayerHandler.players[npc.spawnedBy];
		if (npc.type == 907) {
			c.mageState = 1;
			NPCHandler.spawnNpc(c, 908, c.absX + 1, c.absY, c.heightLevel, 1, 100, 20, 500, 120, true, false);
		} else if (npc.type == 908) {
			c.mageState = 2;
			NPCHandler.spawnNpc(c, 64, c.absX + 1, c.absY, c.heightLevel, 1, 100, 20, 500, 120, true, false);
		} else if (npc.type == 64) {
			c.mageState = 3;
			NPCHandler.spawnNpc(c, 1549, c.absX + 1, c.absY, c.heightLevel, 1, 100, 20, 500, 120, true, false);
		} else if (npc.type == 1549) {
			c.mageState = 4;
			NPCHandler.spawnNpc(c, 911, c.absX + 1, c.absY, c.heightLevel, 1, 100, 20, 500, 120, true, false);
		} else if (npc.type == 911) {
			c.mageState = 5;
			c.getPA().startTeleport(2540, 4714, 0);

			Achievements.progressMade(c, Achievements.Types.COMPLETE_KOLODION_MINIGAME);
		}

	}

	private static int distance(int x, int y, int dx, int dy) {
		int xdiff = x - dx;
		int ydiff = y - dy;
		return (int) Math.sqrt(xdiff * xdiff + ydiff * ydiff);
	}

	/**
	 * Distanced required to attack
	 */
	public static int distanceRequired(NPC npc) {

		if (npc.type == 491 || npc.type == 493) {
			return 10;
		}

		if (npc.type == 5862) {
			if (npc.attackType == 0) {
				return 3;
			}
			return 12;
		}

		// so jad constantly tries to stomp you
		if (npc.type == 2745) {

			if (npc.attackType == 0) {
				return 1;
			} else {
				return 8;
			}
		}

		// snakelings
		if (npc.type == 2045) {
			return 3;
		}

		// zulrah
		if (npc.type >= 2042 && npc.type <= 2044) {
			return 8;
		}

		if (npc.attackType == 0) {
			return 1;
		}

		return 8;

		/*
		 * NpcDefinition record = npc.getDefinition();
		 * 
		 * if (record.getCombatType() != null && npc.getCombat() == null) { if
		 * (record.getCombatType().equals(NpcDefinition.CombatType.MELEE)) { return 1; }
		 * else { return 6; } }
		 * 
		 * if (npc.getCombat() != null) { return npc.getCombat().getDistanceRequired();
		 * }
		 * 
		 * switch (npc.type) { case 14301: return 10; case 3847: return 10; case 1472:
		 * return 8; case 8349: case 50: return 8; case 10773: case 7133: return 2; case
		 * 8133: if (npc.attackType == 2) // mage { return 6; } else if (npc.attackType
		 * == 1) // range { return 6; } else { return 3; } case 2025: case 2028: case
		 * 1158: case 1160: return 6; case 6247: return 2; case 2881:// dag kings case
		 * 2882: case 3200:// chaos ele case 2739: case 2740: case 2743: case 2744: case
		 * 2631: return 8; case 2741: case 2742: return 4; case 2745: return 15; case
		 * 2883:// rex return 1; case 6263: case 6265: case 2556: case 2557: case 6222:
		 * case 6223: case 6225: case 6250: case 6252: return 9; case 9462: case 9464:
		 * case 9466: return 10; // things around dags case 2892: case 2894: case 1456:
		 * return 10; default: return 1; }
		 */
	}

	@SuppressWarnings("unused")
	public static void dropItems(Player player, NPC npc) {

		if (npc.attributeExists("dont_drop")) {
			return;
		}

		if (player.mageState == 5) {
			if (npc.type == 912 || npc.type == 913 || npc.type == 914) {
				player.magePoints += 1;
				Achievements.progressMade(player, Achievements.Types.EARN_100_MAGE_POINTS);
			}
		}
		if (npc.type >= 907 && npc.type <= 911 || npc.type == 64 || npc.type == 1549) {
			appendKolodion(npc);
		}

		// Only loop through potential dragons if we are wearing a DFS
		if (player.equipment[player.playerShield] == 11283 || player.equipment[player.playerShield] == 11284) {
			for (int dragon : dragons) {
				if (dragon != npc.type) {
					continue;
				}

				if (player.dfsCharges < 50) {
					player.dfsCharges++;
					player.sendMessage("You currently have " + player.dfsCharges + " DFS charges.");
				} else {
					player.sendMessage("You cannot store any more DFS charges.");
				}

				break;
			}
		}

		/**
		 * new system
		 */

		boolean hasBoneCrusher = player.getItems().playerHasItem(18337);

		int type = npc.type;

		if (npc.type >= 2042 && npc.type <= 2044) {
			type = npc.type = 2042;
		}

		List<Drop> drops = NPCDropsHandler.getRandomDrops(player, type, 1);
		if (drops != null) {
			for (Drop drop : drops) {

				if (ItemUtility.getName(drop.getItemId()).toLowerCase().contains("clue scroll")
						&& player.getItems().hasClueScroll()) {
					return;
				}

				if (hasBoneCrusher) {
					int xp = player.getPrayer().getExp(drop.getItemId());
					if (xp > 0) {

						Achievements.progressMade(player, Achievements.Types.BURY_100_BONES);

						if (drop.getItemId() == 532) {
							Achievements.progressMade(player, Achievements.Types.BURY_250_BIG_BONES);
						}
						if (drop.getItemId() == 536) {
							Achievements.progressMade(player, Achievements.Types.BURY_500_DRAGON_BONES);
						}
						if (drop.getItemId() == 18830) {
							Achievements.progressMade(player, Achievements.Types.BURY_500_FROST_BONES);
							Achievements.progressMade(player, Achievements.Types.BURY_1000_FROST_BONES);
						}

						player.getPA().addSkillXP(xp * 1, 5);
						player.sendMessage("You gain prayer XP from the Bonecrusher.");
						continue;
					}
				}

				Location location = npc.getLocation();

				// zulrah drops
				if (npc.type >= 2042 && npc.type <= 2044) {
					location = Location.create(2268, 3069);
				}

				// kraken drops
				if (npc.type == 493) {
					location = Location.create(3698, 5807);
				}

				if (drop.getItemId() == 8851) {
					for (int i = 0; i < drop.getDropAmount(); i++) {
						Achievements.progressMade(player, Achievements.Types.EARN_1000_WARRIOR_TOKENS);
					}
				}

				ItemDrops.createGroundItem(player, drop.getItemId(), location.getX(), location.getY(),
						drop.getDropAmount() * (Config.DOUBLE_DROPS ? 2 : 1), player.playerId, false);
			}
		}
		// System.out.println("Took: " + (System.currentTimeMillis() - start));
	}

	private static final CollisionMap nexCollisionMap = new CollisionMap(Location.create(2910, 5185),
			Location.create(2945, 5225), 0);

	public static void follow(NPC npc, Player player) {
		if (npc == null || player == null) {
			return;
		}
		if (player.respawnTimer > 0) {
			npc.facePlayer(0);
			npc.randomWalk = true;
			npc.underAttack = false;
			return;
		}
		if (!followPlayer(npc)) {
			npc.facePlayer(player.playerId);
			return;
		}

		// zulrah
		if (npc.getType() >= 2042 && npc.getType() <= 2044) {
			return;
		}

		int playerX = player.absX;
		int playerY = player.absY;
		int playerHeight = player.heightLevel;

		// cheap fix
		if (player.inMageArena()) {
			playerHeight = 1;
		}

		Location location = Location.create(npc.absX, npc.absY, playerHeight);
		int npcSize = npc.getDefinition().getSize();

		npc.randomWalk = false;// this is where he added the nc
		if (npc.type >= 6142 && npc.type <= 6145) {
			return;
		}

		if (npc.heightLevel != player.heightLevel) {
			return;
		}

		int distance = distanceRequired(npc);

		// makes jad always try and get into melee distance
		if (npc.type == 2745) {
			distance = 1;
		}
		
		//Temp zombies fix
		if (npc.type == 14205 && Zombies.inGameArea(npc.getLocation()) && npc.absX == 4131
				&& (npc.absY == 3993 || npc.absY == 3994)) 
				npc.moveX = 1;
		
		if (goodDistance(npc.getX(), npc.getY(), playerX, playerY, distance)) {
			return;
		}

		if (npc.type == 13447 || npc.type == 14205 && Zombies.inGameArea(npc.getLocation()) && npc.smartPathfinding) {

			Optional<CollisionMap> map;

			if (npc.type == 13447) {
				map = Optional.of(nexCollisionMap);
			} else {
				map = Zombies.getCollisionMap(npc.heightLevel);
			}

			if (map.isPresent()) {

				boolean cached = false;
				Deque<Location> path = null;

				/*
				 * if(npc.destination.isPresent() && npc.path.isPresent()) { Location dest =
				 * npc.destination.get(); if(dest.equals(player.getLocation())) { path =
				 * npc.path.get(); cached = true; } else { npc.destination = Optional.empty();
				 * npc.path = Optional.empty(); } }
				 */

				if (path == null) {

					atomicInteger.incrementAndGet();
					Server.submitWork(new Runnable() {
						@Override
						public void run() {

							Deque<Location> path = NpcPathFinder.findPath(npc.getLocation(), player.getLocation(),
									map.get());
							if (path != null) {
								Location walkLocation = path.poll();

								if (walkLocation == null) {
									return;
								}

								npc.path = Optional.of(path);
								npc.destination = Optional.of(player.getLocation());

								npc.moveX = walkLocation.getX() - npc.absX;
								npc.moveY = walkLocation.getY() - npc.absY;

								npc.getNextNPCMovement();
								npc.facePlayer(player.playerId);
								npc.updateRequired = true;
							}
							atomicInteger.decrementAndGet();
						}
					});
				}
			}

			return;
		}

		// if((npcs[i].spawnedBy > 0) || ((npcs[i].absX < npcs[i].makeX +
		// Config.NPC_FOLLOW_DISTANCE) && (npcs[i].absX >
		// npcs[i].makeX - Config.NPC_FOLLOW_DISTANCE) && (npcs[i].absY < npcs[i].makeY
		// + Config.NPC_FOLLOW_DISTANCE) &&
		// (npcs[i].absY > npcs[i].makeY - Config.NPC_FOLLOW_DISTANCE))) {
		if (npc.getOwnerId() > -1 || npc.type == 14205 && Zombies.inGameArea(npc.getLocation()) || (npc.spawnedBy > 0)
				|| isFightCaveNpc(npc)
				|| ((npc.absX < npc.makeX + Config.NPC_FOLLOW_DISTANCE)
						&& (npc.absX > npc.makeX - Config.NPC_FOLLOW_DISTANCE)
						&& (npc.absY < npc.makeY + Config.NPC_FOLLOW_DISTANCE)
						&& (npc.absY > npc.makeY - Config.NPC_FOLLOW_DISTANCE))) {

			followMethod(npc, player);

			if (playerX == npc.absX && playerY == npc.absY) {
				int o = Misc.random(4);
				switch (o) {
				case 0:
					if (NpcPathFinder.canMove(location, NpcPathFinder.Move.NORTH, npcSize)) {
						npc.moveX = 0;
						npc.moveY = 1;
					}
					break;
				case 1:
					if (NpcPathFinder.canMove(location, NpcPathFinder.Move.SOUTH, npcSize)) {
						npc.moveX = 0;
						npc.moveY = -1;
					}
					break;
				case 2:
					if (NpcPathFinder.canMove(location, NpcPathFinder.Move.EAST, npcSize)) {
						npc.moveX = 1;
						npc.moveY = 0;
					}
					break;
				case 3:
					if (NpcPathFinder.canMove(location, NpcPathFinder.Move.WEST, npcSize)) {
						npc.moveX = -1;
						npc.moveY = 0;
					}
					break;
				}
			}
			npc.getNextNPCMovement();
			npc.facePlayer(player.playerId);
			npc.updateRequired = true;
		} else {
			npc.facePlayer(0);
			npc.randomWalk = true;
			npc.underAttack = false;
		}
	}

	/**
	 * Npc Following
	 */
	public static void followMethod(NPC npc, Player leader) {
		int followX = leader.getLocation().getX(), followY = leader.getLocation().getY();
		int moveX = 0, moveY = 0;
		if (followX != npc.getLocation().getX() && followY != npc.getLocation().getY()) {
			if (followX > npc.getLocation().getX() && followY > npc.getLocation().getY()) {
				if (npc.checkWalk(1, 1) && !npc.goodDistanceEntity(leader, 1)) {
					moveX = 1;
					moveY = 1;
				} else if (npc.checkWalk(1, 0)) {
					moveX = 1;
					moveY = 0;
				} else if (npc.checkWalk(0, 1)) {
					moveX = 0;
					moveY = 1;
				}
			} else if (followX > npc.getLocation().getX() && followY < npc.getLocation().getY()) {
				if (npc.checkWalk(1, -1) && !npc.goodDistanceEntity(leader, 1)) {
					moveX = 1;
					moveY = -1;
				} else if (npc.checkWalk(0, -1)) {
					moveX = 0;
					moveY = -1;
				} else if (npc.checkWalk(1, 0)) {
					moveX = 1;
					moveY = 0;
				}
			} else if (followX < npc.getLocation().getX() && followY > npc.getLocation().getY()) {
				if (npc.checkWalk(-1, 1) && !npc.goodDistanceEntity(leader, 1)) {
					moveX = -1;
					moveY = 1;
				} else if (npc.checkWalk(-1, 0)) {
					moveX = -1;
					moveY = 0;
				} else if (npc.checkWalk(0, 1)) {
					moveX = 0;
					moveY = 1;
				}
			} else if (followX < npc.getLocation().getX() && followY < npc.getLocation().getY()) {
				if (npc.checkWalk(-1, -1) && !npc.goodDistanceEntity(leader, 1)) {
					moveX = -1;
					moveY = -1;
				} else if (npc.checkWalk(0, -1)) {
					moveX = 0;
					moveY = -1;
				} else if (npc.checkWalk(-1, 0)) {
					moveX = -1;
					moveY = 0;
				}
			}
		} else if (followX != npc.getLocation().getX()) {
			if (followX > npc.getLocation().getX()) {
				if (npc.checkWalk(1, 0)) {
					moveX = 1;
					moveY = 0;
				}
			} else if (followX < npc.getLocation().getX()) {
				if (npc.checkWalk(-1, 0)) {
					moveX = -1;
					moveY = 0;
				}
			}
		} else if (followY != npc.getLocation().getY()) {
			if (followY > npc.getLocation().getY()) {
				if (npc.checkWalk(0, 1)) {
					moveX = 0;
					moveY = 1;
				}
			} else if (followY < npc.getLocation().getY()) {
				if (npc.checkWalk(0, -1)) {
					moveX = 0;
					moveY = -1;
				}
			}
		}
		if (moveX != 0 || moveY != 0) {
			npc.moveX = moveX;
			npc.moveY = moveY;
		}
	}

	public static int followDistance(int i) {
		switch (npcs[i].type) {
		case 13447:
			return 10;

		case 6260:
		case 6261:
		case 6247:
		case 6248:
			return 8;
		case 2883:
			return 4;
		case 2881:
		case 2882:
			return 1;
		case 2739:
		case 2740:
		case 2743:
		case 2744:
		case 2745:
			return 8;
		}
		return 0;
	}

	public static void followNpc(NPC npc, NPC other) {

		// some simple checks
		if (npc.isDead || other.isDead) {
			return;
		}

		if (npc.getHP() == 0 || other.getHP() == 0) {
			return;
		}

		if (!npc.getLocation().isViewableFrom(other.getLocation())) {
			return;
		}

		if (Misc.distanceToPoint(npc.getX(), npc.getY(), other.getX(), other.getY()) < 2) {
			return;
		}

		int[] path = NPCHelper.findPath(npc.getLocation(), other.getLocation());

		if (path[0] != 0 || path[1] != 0) {
			npc.moveX = path[0];
			npc.moveY = path[1];

			npc.getNextNPCMovement();
			npc.faceNpc(other.id);
			npc.updateRequired = true;
		}
	}

	public static boolean followPlayer(NPC npc) {
		switch (npc.type) {
		case 13451: // nex minion
		case 13452:
		case 13453:
		case 13454:
		case 2892:
		case 2894:
		case 1456:
			return false;
		}
		return true;
	}

	public static int getCloseRandomPlayer(NPC npc) {
		ArrayList<Integer> players = new ArrayList<>();

		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player player = PlayerHandler.players[j];
				if (goodDistance(player.absX, player.absY, npc.absX, npc.absY,
						2 + distanceRequired(npc) + followDistance(npc.id)) || isFightCaveNpc(npc)
						|| npc.type == 14205 && Zombies.inGameArea(npc.getLocation())) {
					if (player.underAttackBy <= 0 && player.underAttackByNpcID <= 0 || Areas.inMulti(player)) {
						if (player.heightLevel == npc.heightLevel) {

							// note(stuart): monkeys which attack you at ape atoll shouldnt attack you when
							// in greegree form
							if (npc.type == 1456 && GreeGree.isGreeGree(player)) {
								continue;
							}

							// note(stuart): stop nex attacking players in bank area
							if (npc.type == 13447 && player.absX < 2911) {
								continue;
							}

							// note stops General Graardor attacking players outside the room
							if (npc.type == 6260 && player.absX <= 2963) {
								continue;
							}
							if (npc.type == 6261 && player.absX <= 2963) {
								continue;
							}

							if (!NPCHandler.canPlayerAttackNPC(player, npc, false)) {
								continue;
							}

							players.add(j);
						}
					}
				}
			}
		}
		if (!players.isEmpty()) {
			return players.get(Misc.random(players.size() - 1));
		} else {
			return 0;
		}
	}

	public static NpcDefinition getNPCDefinition(int npcId) {
		return npcDefinitions.get(npcId);
	}

	/**
	 * Npc killer id?
	 */
	private static int getNpcKillerId(int npcId) {

		int damage = 0;
		int killerId = 0;

		NPC npc = NPCHandler.npcs[npcId];

		for (Map.Entry<Integer, Integer> entry : npc.getPlayerDamage().entrySet()) {
			Player player = PlayerHandler.getPlayerByDatabaseId(entry.getKey());
			if (player == null) {
				continue;
			}
			if (entry.getValue() > damage) {
				damage = entry.getValue();
				killerId = player.playerId;
			}
		}

		return killerId;
	}

	public static int getNpcListHP(int npcId) {
		NpcDefinition definition = getNPCDefinition(npcId);
		return definition != null ? definition.getHealth() : 0;
	}

	public static String getNpcListName(int npcId) {
		NpcDefinition definition = getNPCDefinition(npcId);

		return definition != null ? definition.getName() : "No Name";
	}

	public static int getProjectileSpeed(int i) {
		switch (npcs[i].type) {
		case 908:
		case 909:
		case 910:
		case 911:
			return 4;
		case 3847:
			return 85;
		case 1158:
		case 1160:
			return 85;
		case 2881:
		case 2882:
		case 3200:
		case 3340:
			return 85;
		case 2745:
			return 130;
		case 50:
			return 90;
		case 10773:
			return 90;
		case 2025:
			return 85;
		case 2028:
			return 80;
		default:
			return 85;
		}
	}

	public static boolean getsPulled(int i) {
		switch (npcs[i].type) {
		case 6260:
			if (npcs[i].firstAttacker > 0) {
				return false;
			}
			break;
		}
		return true;
	}

	public static boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		for (int i = 0; i <= distance; i++) {
			for (int j = 0; j <= distance; j++) {
				if ((objectX + i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if ((objectX - i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if (objectX == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				}
			}
		}
		return false;
	}

	public static void handleSpecialEffects(Player player, NPC npc, int damage) {
		if (npc.type == 2892 || npc.type == 2894) {
			if (damage > 0) {
				if (player != null) {
					if (player.level[5] > 0) {
						player.level[5]--;
						player.getPA().refreshSkill(5);
						player.getPA().appendPoison(12);
					}
				}
			}
		}
	}

	/**
	 * Checks if something is a fight cave npc.
	 *
	 * @param npc The npc.
	 * @return Whether or not it is a fight caves npc.
	 */
	public static boolean isFightCaveNpc(NPC npc) {
		if (npc == null) {
			return false;
		}
		switch (npc.type) {
		case 2629:
		case FightCaves.TZ_KEK_SPAWN:
		case FightCaves.TZ_KIH:
		case FightCaves.TZ_KEK:
		case FightCaves.TOK_XIL:
		case FightCaves.YT_MEJKOT:
		case FightCaves.KET_ZEK:
		case FightCaves.TZTOK_JAD:
			return true;
		}
		return false;
	}

	/*
	 * public boolean isFightCaveNpc(int i) { if (npcs[i] == null) { return false; }
	 * switch (npcs[i].type) { case FightCaves.TZ_KEK_SPAWN: { return true; } case
	 * FightCaves.TZ_KIH: case FightCaves.TZ_KEK: case FightCaves.TOK_XIL:
	 * npcs[i].attackType = 1; npcs[i].projectileId = 443; return true; case
	 * FightCaves.YT_MEJKOT: case FightCaves.KET_ZEK: npcs[i].attackType = 2;
	 * npcs[i].projectileId = 445; npcs[i].endGfx = 446; return true; case
	 * FightCaves.TZTOK_JAD: int r3 = 0; if
	 * (PlayerHandler.players[npcs[i].spawnedBy] == null) { return false; } if
	 * (goodDistance(npcs[i].absX, npcs[i].absY,
	 * PlayerHandler.players[npcs[i].spawnedBy].absX,
	 * PlayerHandler.players[npcs[i].spawnedBy].absY, 1)) { r3 = Misc.random(2); }
	 * else { r3 = Misc.random(1); } if (r3 == 0) { npcs[i].attackType = 2;
	 * npcs[i].endGfx = 157; npcs[i].projectileId = 448; } else if (r3 == 1) {
	 * npcs[i].attackType = 1; npcs[i].endGfx = 451; npcs[i].projectileId = -1; }
	 * else if (r3 == 2) { npcs[i].attackType = 0; npcs[i].projectileId = -1; }
	 * return true; } return false; }
	 */

	/**
	 *
	 */
	private static void killedBarrow(int i) {
		Player c = PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			for (int o = 0; o < c.barrowsNpcs.length; o++) {
				if (npcs[i].type == c.barrowsNpcs[o][0]) {
					c.barrowsNpcs[o][1] = 2; // 2 for dead
					c.barrowsKillCount++;
				}
			}
		}
	}

	@SuppressWarnings("incomplete-switch")
	public static void loadSpell(NPC npc) {
		int random = 0;
		int r = Misc.random(3);
		switch (npc.type) {
		/*
		 * case 941: npc.projectileId = 393; //red npc.endGfx = 430; break;
		 */
		case 3847: // sea troll
			int stq1 = 0;
			stq1 = Misc.random(1);
			if (stq1 == 0) { // mage
				npc.projectileId = 162;// no delay? idk, should i have a delyy one sec let me check haven't worked
				// with pi in a while
				npc.endGfx = 163;// and heres the endgfx
				npc.attackType = 2;// keep it at 3it wouldnt have the delay I dont think..try it.
				npc.gfx100(163);// thats true keep it like that thren.
			} else if (stq1 == 1) {
				npc.attackType = 1; // range
				npc.endGfx = 552;
				npc.projectileId = 551;
				npc.forceChat("You cannot defeat me!");
				npc.forceChat("You will die!");// thats why thats magic I think -.- umm right? xD
			} // whats the id?
			break;

		case 909:
		case 908:
			npc.attackType = 2;
			npc.endGfx = 78;
			break;

		case 911:
		case 910:
			int ran1 = Misc.random(5);
			npc.attackType = 2;
			if (ran1 == 0) {
				npc.attackType = 2;
				npc.endGfx = 77;
			}
			if (ran1 == 1) {
				npc.attackType = 2;
				npc.endGfx = 76;
			} else {
				npc.attackType = 2;
				npc.endGfx = 78;
			}
			break;
		case 2745:

			double distance = npc.getLocation().getDistance(PlayerHandler.players[npc.killerId].getLocation());

			if (distance > 8) {
				break;
			}

			int r4 = 0;
			if (distance <= 4) {
				r4 = Misc.random(2);
			} else {
				r4 = Misc.random(1) == 1 ? 1 : 2;
			}

			switch (r4) {
			case 0:
				npc.attackType = 0;
				startAttack(npc, -1, -1, 0, -1);
				break;
			case 1:
				npc.attackType = 1;
				npc.projectileId = -1;
				npc.endGfx = 451;
				// PlayerHandler.players[npc.killerId].gfx0(451);
				break;
			case 2:
				npc.attackType = 2;
				npc.projectileId = 448;
				npc.endGfx = 157;
				break;
			}

			break;

		case 2739: // tok-xil
		case 2740:
			// int r = Misc.random(3);
			if (goodDistance(npc.absX, npc.absY, PlayerHandler.players[npc.killerId].absX,
					PlayerHandler.players[npc.killerId].absY, 2)) {
				r = 0;
			} else {
				r = 1;
			}
			switch (r) {
			case 0:
				startAttack(npc, -1, -1, 0, -1);
				break;
			case 1:
				startAttack(npc, -1, 442, 1, -1);
				break;
			}
			break;
		case 2741:
		case 2742:
			// int r = Misc.random(3);
			List<Integer> inHealingRange = new ArrayList<Integer>();
			/*
			 * for(Iterator<Integer> npcInCaves =
			 * c.getFightCaves().inCaveMonsters.iterator(); npcInCaves.hasNext(); ) {
			 * inHealingRange.add(npcInCaves.next()); }
			 */
			if (goodDistance(npc.absX, npc.absY, PlayerHandler.players[npc.killerId].absX,
					PlayerHandler.players[npc.killerId].absY, 2)) {
				r = Misc.random(1);
			} else {
				r = 0;
			}
			switch (r) {
			case 1: // healing random npc within 1 square away
				if (inHealingRange.isEmpty()) {
					return;
				}
				int lol = Misc.random(inHealingRange.size()) - 1;
				if (lol == -1) {
					lol = 0;
				}
				if (npcs[inHealingRange.get(lol)] != null) {
					npcs[inHealingRange.get(lol)].gfx0(444);
					int amttoheal = Misc.random(15);
					if (npcs[inHealingRange.get(lol)].maxHP > npcs[inHealingRange.get(lol)].getHP() + amttoheal) {
						npcs[inHealingRange.get(lol)].incrementHP(amttoheal);
						npcs[inHealingRange.get(lol)].updateRequired = true;
					} else {
						npcs[inHealingRange.get(lol)].setHP(npcs[inHealingRange.get(lol)].maxHP);
						npcs[inHealingRange.get(lol)].updateRequired = true;
					}
				}
				break;
			case 0: // melee claw slash
				startAttack(npc, -1, -1, 0, -1);
				break;
			}
			break;
		case 2743: // ket-zek
		case 2744:

			// public static void startAttack(int i, int gfx, int proj, int att, int endgfx)
			// {

			// int r = Misc.random(3);
			if (goodDistance(npc.absX, npc.absY, PlayerHandler.players[npc.killerId].absX,
					PlayerHandler.players[npc.killerId].absY, 2)) {
				r = 0;
			} else {
				r = 1;
			}
			switch (r) {
			case 0:
				startAttack(npc, -1, -1, 0, -1);
				break;
			case 1:
				startAttack(npc, -1, 445, 2, 446);
				break;
			}
			break;

		/*
		 * case 3847: //sea troll int stq1 = 0; stq1 = Misc.random(1);
		 *
		 * if (stq1 == 0) { //mage npc.projectileId = 162;// no delay? idk, should i
		 * have a delyy one sec let me check haven't worked with pi in a while
		 * npc.endGfx = 163;//and heres the endgfx npc.attackType = 2;//keep it at 3it
		 * wouldnt have the delay I dont think..try it. npc.gfx100(163);//thats true
		 * keep it like that thren. } else if (stq1 == 1) { npc.attackType = 1; // range
		 * npc.endGfx = 552; npc.projectileId = 551;
		 * npc.forceChat("You cannot defeat me!"); npc.forceChat("You will die!");//
		 * thats why thats magic I think -.- umm right? xD }//whats the id? break;
		 */
		case 1472:// jungle demon switch attacks
			int jgld = 0;
			jgld = Misc.random(3);
			if (jgld == 3) { // melee
				// npc.projectileId = 162;// no delay? idk, should i have a delyy one sec let me
				// check haven't
				// worked with pi in a while
				// npc.endGfx = 163;//and heres the endgfx
				npc.attackType = 3;// keep it at 3it wouldnt have the delay I dont think..try it.
				npc.forceChat("I will eat your flesh!");
				// damage = 25; //that is, if you want it to do 0
				// npc.gfx100(163);//thats true keep it like that thren.
			} else if (jgld == 1) {
				// damage = 25; //that is, if you want it to do 0
				npc.attackType = 1; // range
				npc.endGfx = 552;
				npc.projectileId = 551;
				npc.forceChat("Leave my jungle!");// thats why thats magic I think -.- umm right? xD
			}
			break;
		case 1592:
		case 3590:
		case 53:
		case 54:
		case 55:
		case 56:
		case 1590:
		case 1591:
		case 5363:
		case 941:
		case 10773:
			Player player = PlayerHandler.players[npc.killerId];
			if (player == null) {
				return; // wtf happened here?
			}
			if (goodDistance(npc.absX, npc.absY, player.absX, player.absY, 2)) {
				if (Misc.random(5) == 0) {
					npc.projectileId = 393;
					npc.attackType = 3;
				} else {
					npc.attackType = 0;
				}
			} else {
				npc.projectileId = 393;
				npc.attackType = 3;
			}
			break;

		case 3068:
			player = PlayerHandler.players[npc.killerId];
			if (player == null) {
				return; // wtf happened here?
			}
			if (goodDistance(npc.absX, npc.absY, player.absX, player.absY, 2)) {
				if (Misc.random(5) == 0) {
					npc.projectileId = 395;
					npc.attackType = 8;
				} else {
					npc.attackType = 0;
				}
			} else {
				npc.projectileId = 395;
				npc.attackType = 8;
			}
			break;

		/*
		 * case 10773: npc.attackType = 3; npc.kbdAttack = Misc.random(4); switch
		 * (npc.kbdAttack) { case 0: npc.projectileId = 393; break; case 1:
		 * npc.projectileId = 394; break; case 2: npc.projectileId = 395; break; case 3:
		 * npc.projectileId = 396; break; }
		 */
		case 5362:
			npc.attackType = 2;
			npc.projectileId = 393;
			break;
		case 50:
			npc.attackType = 3;
			switch (Misc.random(4)) {
			case 0:
				npc.projectileId = 393;
				break;
			case 1:
				npc.projectileId = 394;
				break;
			case 2:
				npc.projectileId = 395;
				break;
			case 3:
				npc.projectileId = 396;
				break;
			case 4: // melee
				npc.projectileId = -1;
				npc.endGfx = -1;
				npc.attackType = 0;
				break;
			}
			break;
		case 1158:
			npc.kQueen = Misc.random(1);
			switch (npc.kQueen) {
			case 0:
				npc.attackType = 1;
				npc.projectileId = 288;
				npc.endGfx = -1;
				break;
			case 1:
				npc.gfx0(278);
				npc.attackType = 2;
				npc.projectileId = 280;
				npc.endGfx = 281;
				break;
			}
			break;
		case 1160:
			npc.kQueen = Misc.random(1);
			switch (npc.kQueen) {
			case 0:
				npc.attackType = 1;
				npc.projectileId = 289;
				npc.endGfx = -1;
				break;
			case 1:
				npc.gfx100(279);
				npc.attackType = 2;
				npc.projectileId = 280;
				npc.endGfx = 281;
				break;
			}
			break;
		case 8349:
			int tormDemon = 0;
			if (npc.getHP() < 75) {
				tormDemon = Misc.random(30);
			} else {
				tormDemon = Misc.random(20);
			}
			if (tormDemon > 20) {
				npc.gfx100(1885);
				npc.incrementHP(Misc.random(25));
				npc.attackType = 5;
			} else if (tormDemon <= 10) {
				npc.projectileId = 1884;
				npc.attackType = 6;
			} else if (tormDemon > 10 && tormDemon <= 20) {
				npc.gfx0(1886);
				npc.attackType = 0;
			}
			break;
		case 3340:
			int Mole = Misc.random(1);
			if (Mole == 1) {
				npc.projectileId = 156;
				npc.endGfx = 157;
				npc.attackType = 7;
			} else if (npc.attackType == 0) {
				npc.attackType = 0;
				npc.projectileId = -1; // melee
				npc.endGfx = -1;
			}
			break;
		case 7133: // bork
			int lll = Misc.random(1);
			if (lll == 0) {
				npc.attackType = 8;
				npc.projectileId = -1; // melee
				npc.endGfx = -1;
			}
		case 5666: // mah barrelchest bruh
			int rrr = Misc.random(1);
			if (rrr == 0) {
				npc.attackType = 0;
				npc.projectileId = -1; // melee
				npc.endGfx = -1;
			} else {
				npc.attackType = 1;
				npc.projectileId = -1;
				npc.projectileId = -1;
			}
			break;
		case 2892:
			npc.projectileId = 94;
			npc.attackType = 2;
			npc.endGfx = 95;
			break;
		case 2894:
			npc.projectileId = 298;
			npc.attackType = 1;
			break;
		case 1456:
			npc.projectileId = 298;
			npc.attackType = 1;
			break;
		case 2496:
			int random1 = Misc.random(4);
			if (random1 == 0) {
				npc.animNumber = 10917;
				npc.gfx100(1885);
				npc.attackType = 2;
			}
			break;
		/*
		 * case 8133: int corpBeast = Misc.random(1); if (corpBeast == 0) { npc.endGfx =
		 * 1739; npc.attackType = 4; } else if (corpBeast == 1) npc.attackType = 0;
		 * break;
		 */
		// arma npcs
		case 6227:
			npc.attackType = 0;
			break;
		case 6225:
			npc.attackType = 1;
			npc.projectileId = 1190;
			break;
		case 6223:
			npc.attackType = 2;
			npc.projectileId = 1203;
			break;
		case 6222:
			random = Misc.random(1);
			npc.attackType = 1 + random;
			if (npc.attackType == 1) {
				npc.projectileId = 1197;
			} else {
				npc.attackType = 2;
				npc.projectileId = 1198;
			}
			break;
		// sara npcs
		case 6247: // sara
			random = Misc.random(1);
			if (random == 0) {
				npc.attackType = 2;
				npc.endGfx = 1224;
				npc.projectileId = -1;
			} else if (random == 1) {
				npc.attackType = 0;
			}
			break;
		case 6248: // star
			npc.attackType = 0;
			break;
		case 6250: // growler
			npc.attackType = 2;
			npc.projectileId = 1203;
			break;
		case 6252: // bree
			npc.attackType = 1;
			npc.projectileId = 9;
			break;
		// bandos npcs
		case 6260:
			random = Misc.random(2);
			if (random == 0 || random == 1) {
				npc.attackType = 0;
			} else {
				npc.attackType = 1;
				npc.endGfx = 1211;
				npc.projectileId = 288;
			}
			break;
		case 6261:
			npc.attackType = 0;
			break;
		case 6263:
			npc.attackType = 2;
			npc.projectileId = 1203;
			break;
		case 6265:
			npc.attackType = 1;
			npc.projectileId = 1206;
			break;
		case 2025:
			npc.attackType = 2;
			int bsp = Misc.random(3);
			if (bsp == 0) {
				npc.gfx100(158);
				npc.projectileId = 159;
				npc.endGfx = 160;
			}
			if (bsp == 1) {
				npc.gfx100(161);
				npc.projectileId = 162;
				npc.endGfx = 163;
			}
			if (bsp == 2) {
				npc.gfx100(164);
				npc.projectileId = 165;
				npc.endGfx = 166;
			}
			if (bsp == 3) {
				npc.gfx100(155);
				npc.projectileId = 156;
			}
			break;
		case 2881:// supreme
			npc.attackType = 1;
			npc.projectileId = 298;
			break;
		case 2882:// prime
			npc.attackType = 2;
			npc.projectileId = 162;
			npc.endGfx = 477;
			break;
		case 2028:
			npc.attackType = 1;
			npc.projectileId = 27;
			break;
		case 3200:
			int r2 = Misc.random(1);
			if (r2 == 0) {
				npc.attackType = 1;
				npc.gfx100(550);
				npc.projectileId = 551;
				npc.endGfx = 552;
			} else {
				npc.attackType = 2;
				npc.gfx100(553);
				npc.projectileId = 554;
				npc.endGfx = 555;
			}
			break;
		case 8133: // corp
			int r5 = 0;
			r5 = Misc.random(3);
			if (r5 == 0) {
				npc.attackType = 2; // mage
				npc.endGfx = 1739;
				npc.projectileId = -1;
			} else if (r5 == 1) { // range
				npc.attackType = 1;
				npc.endGfx = -1;
				npc.projectileId = 1741;
			} else if (r5 == 2) {
				npc.attackType = 0; // melee
				npc.projectileId = -1;
				npc.endGfx = -1;
			} else {
				npc.attackType = 0; // melee
				npc.projectileId = -1;
				npc.endGfx = -1;
			}
			break;
		case 2631:
			npc.attackType = 1;
			npc.projectileId = 443;
			break;
		}
	}

	public static void multiAttackDamage(NPC npc) {
		int max = NPCConfig.getMaxHit(npc);
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = PlayerHandler.players[j];
				if (c.isDead || c.heightLevel != npc.heightLevel) {
					continue;
				}
				if (PlayerHandler.players[j].goodDistance(c.absX, c.absY, npc.absX, npc.absY, 15)) {

					// note(stuart): stop nex attacking players in bank area
					if (npc.type == 13447 && c.absX < 2911) {
						continue;
					}

					if (npc.attackType == 2) {
						if (!c.prayerActive[16]) {
							if (Misc.random(500) + 200 > Misc.random(c.getCombat().mageDef())) {
								int dam = Misc.random(max);

								// ring of recoil
								if (c.equipment[PlayerConstants.RING] == 2550) {
									int recoilDamage = (int) Math.round(dam * 0.10);
									if (recoilDamage > 0) {
										npc.handleHitMask(recoilDamage, dam, 1, 0);
										npc.dealdamage(c.playerIndex, recoilDamage);
									}
								}

								c.dealDamage(dam);
								c.handleHitMask(dam, max, npc.attackType + 1, j);
							} else {
								c.dealDamage(0);
								c.handleHitMask(0, max, npc.attackType + 1, j);
							}
						} else {
							c.dealDamage(0);
							c.handleHitMask(0, max, npc.attackType + 1, j);
						}
					} else if (npc.attackType == 1) {
						if (!c.prayerActive[17]) {
							int dam = Misc.random(max);
							if (Misc.random(500) + 200 > Misc.random(c.getCombat().calculateRangeDefence())) {

								// ring of recoil
								if (c.equipment[PlayerConstants.RING] == 2550) {
									int recoilDamage = (int) Math.round(dam * 0.10);
									if (recoilDamage > 0) {
										npc.handleHitMask(recoilDamage, dam, 1, 0);
										npc.dealdamage(c.playerIndex, recoilDamage);
									}
								}

								c.dealDamage(dam);
								c.handleHitMask(dam, max, npc.attackType + 1, j);
							} else {
								c.dealDamage(0);
								c.handleHitMask(0, max, npc.attackType + 1, j);
							}
						} else {
							c.dealDamage(0);
							c.handleHitMask(0, max, npc.attackType + 1, j);
						}
					}
					if (npc.endGfx > 0) {
						c.gfx0(npc.endGfx);
					}
				}
				c.getPA().refreshSkill(3);
			}
		}
	}

	public static void multiAttackGfx(int i, int gfx) {
		if (npcs[i].projectileId < 0) {
			return;
		}
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = PlayerHandler.players[j];
				if (c.heightLevel != npcs[i].heightLevel) {
					continue;
				}
				if (PlayerHandler.players[j].goodDistance(c.absX, c.absY, npcs[i].absX, npcs[i].absY, 15)) {

					// note(stuart): stop nex attacking players in bank area
					if (npcs[i].type == 13447 && c.absX < 2911) {
						continue;
					}

					int nX = npcs[i].getX() + NPCConfig.offset(npcs[i]);
					int nY = npcs[i].getY() + NPCConfig.offset(npcs[i]);
					int pX = c.getX();
					int pY = c.getY();
					int offX = (nY - pY) * -1;
					int offY = (nX - pX) * -1;
					c.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, getProjectileSpeed(i),
							npcs[i].projectileId, 43, 31, -c.getId() - 1, 65);
				}
			}
		}
	}

	public static NPC newNPC(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence, Direction direction) {

		NpcDefinition definition = getNPCDefinition(npcType);
		if (definition == null) {
			System.out.println("npc definition for " + npcType + " does not exist");
			return null;
		}

		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			return null;
		}
		npcs[slot] = new NPC(slot, npcType);
		npcs[slot].absX = x;
		npcs[slot].absY = y;
		npcs[slot].makeX = x;
		npcs[slot].faceDirection = direction;
		npcs[slot].makeY = y;
		npcs[slot].heightLevel = heightLevel;
		npcs[slot].walkingType = WalkingType;
		npcs[slot].setHP(HP);
		npcs[slot].maxHP = HP;
		npcs[slot].maxHit = maxHit;
		npcs[slot].attack = attack;
		npcs[slot].defence = defence;
		npcs[slot] = npcs[slot];
		npcs[slot].slot = slot;

		npcs[slot].setCombat(NPCConfig.getCombatScript(npcs[slot]));
		if (npcs[slot].getCombat() != null) {
			npcs[slot].getCombat().npcSpawned();
		}

		if (npcType == 1160) {
			npcs[slot].actionTimer = 5;
			npcs[slot].startAnimation(6237);
		}

		return npcs[slot];
	}

	public static void removeNpc(int i) {

		GameCycleTaskHandler.stopEvents(npcs[i]);

		npcs[i].absX = 0;
		npcs[i].absY = 0;
		npcs[i] = null;
	}

	/**
	 * Resets players in combat
	 */
	public static void resetPlayersInCombat(int i) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].underAttackByNpcID == i) {
					PlayerHandler.players[j].underAttackByNpcID = 0;
				}
			}
		}
	}

	public static NPC spawnNpc(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence) {

		NpcDefinition definition = getNPCDefinition(npcType);
		if (definition == null) {
			System.out.println("npc definition for " + npcType + " does not exist");
			return null;
		}

		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return null; // no free slot found
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.setHP(HP);
		newNPC.maxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
		npcs[slot].setCombat(NPCConfig.getCombatScript(npcs[slot]));
		newNPC.slot = slot;
		if (newNPC.type == 907) {
			newNPC.forceChat("You must prove yourself... Now!");
			newNPC.forcedChatRequired = true;
		} else if (newNPC.type == 908) {
			newNPC.forceChat("This is only the beginning, you can't beat me!");
			newNPC.forcedChatRequired = true;
		} else if (newNPC.type == 909) {
			newNPC.forceChat("Foolish mortal, I am unstoppable.");
			newNPC.forcedChatRequired = true;
		} else if (newNPC.type == 910) {
			newNPC.forceChat("Now you feel it... The dark energy.");
			newNPC.forcedChatRequired = true;
		} else if (newNPC.type == 911) {
			newNPC.forceChat("Aoooaaarrgghhhh! The power!");
			newNPC.forcedChatRequired = true;
		}
		return newNPC;
	}

	public static NPC spawnNpc(Player c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP,
			int maxHit, int attack, int defence, boolean attackPlayer, boolean headIcon) {

		NpcDefinition definition = getNPCDefinition(npcType);
		if (definition == null) {
			System.out.println("npc definition for " + npcType + " does not exist");
			return null;
		}

		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			return null;
		}
		npcs[slot] = new NPC(slot, npcType);
		// NPC newNPC = new NPC(slot, type);
		npcs[slot].absX = x;
		npcs[slot].absY = y;
		npcs[slot].makeX = x;
		npcs[slot].makeY = y;
		npcs[slot].heightLevel = heightLevel;
		npcs[slot].walkingType = WalkingType;
		npcs[slot].setHP(HP);
		npcs[slot].maxHP = HP;
		npcs[slot].maxHit = maxHit;
		npcs[slot].attack = attack;
		npcs[slot].defence = defence;
		npcs[slot].spawnedBy = c.playerId;
		npcs[slot].setCombat(NPCConfig.getCombatScript(npcs[slot]));
		if (headIcon) {
			c.getPA().drawHeadicon(PlayerAssistant.HintType.NPC, slot, null, 0);
		}
		if (attackPlayer) {
			npcs[slot].underAttack = true;
			if (c != null) {
				if (Barrows.COFFIN_AND_BROTHERS[c.randomCoffin][1] != npcs[slot].type) {
					if (npcs[slot].type == 2025 || npcs[slot].type == 2026 || npcs[slot].type == 2027
							|| npcs[slot].type == 2028 || npcs[slot].type == 2029 || npcs[slot].type == 2030) {
						npcs[slot].forceChat("You dare disturb my rest!");
					}
				}
				if (Barrows.COFFIN_AND_BROTHERS[c.randomCoffin][1] == npcs[slot].type) {
					npcs[slot].forceChat("You dare steal from us!");
				}
				npcs[slot].killerId = c.playerId;
			}
		}
		// npcs[slot] = newNPC;
		npcs[slot].slot = slot;
		return npcs[slot];
	}

	public static NPC spawnNpc2(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence) {

		NpcDefinition definition = getNPCDefinition(npcType);
		if (definition == null) {
			System.out.println("npc definition for " + npcType + " does not exist");
			return null;
		}

		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return null; // no free slot found
		}
		npcs[slot] = new NPC(slot, npcType);
		// NPC newNPC = new NPC(slot, type);
		npcs[slot].absX = x;
		npcs[slot].absY = y;
		npcs[slot].makeX = x;
		npcs[slot].makeY = y;
		npcs[slot].heightLevel = heightLevel;
		npcs[slot].walkingType = WalkingType;
		npcs[slot].randomWalk = WalkingType > 0;
		npcs[slot].setHP(HP);
		npcs[slot].maxHP = HP;
		npcs[slot].maxHit = maxHit;
		npcs[slot].attack = attack;
		npcs[slot].defence = defence;
		npcs[slot].setCombat(NPCConfig.getCombatScript(npcs[slot]));
		// npcs[slot] = newNPC;
		npcs[slot].slot = slot;
		return npcs[slot];
	}

	public static void spawnPet(Player c, int npcType, int x, int y, int heightLevel) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return; // no free slot found
		}
		npcs[slot] = new NPC(slot, npcType);
		// NPC newNPC = new NPC(slot, type);
		npcs[slot].absX = x;
		npcs[slot].absY = y;
		npcs[slot].makeX = x;
		npcs[slot].makeY = y;
		npcs[slot].heightLevel = heightLevel;
		npcs[slot].walkingType = 100;
		npcs[slot].setHP(0);
		npcs[slot].maxHP = 0;
		npcs[slot].maxHit = 0;
		npcs[slot].attack = 0;
		npcs[slot].defence = 0;
		npcs[slot].spawnedBy = c.playerId;
		npcs[slot].setCombat(NPCConfig.getCombatScript(npcs[slot]));
		// npcs[slot] = newNPC;
		npcs[slot].slot = slot;
	}

	public static void startAnimation(int animId, int i) {
		npcs[i].animNumber = animId;
		npcs[i].animUpdateRequired = true;
		npcs[i].updateRequired = true;
	}

	public static void startAttack(NPC npc, int gfx, int proj, int att, int endgfx) {
		npc.gfx100(gfx);
		npc.projectileId = proj;
		npc.attackType = att;
		npc.endGfx = endgfx;
	}

	public static void process() {
		for (int i = 0; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				continue;
			}
			if (npcs[i].actionTimer > 0) {
				npcs[i].actionTimer--;
			}

			if (npcs[i].attributeExists("update_face")) {
				NPCHandler.faceDirection(npcs[i], npcs[i].faceDirection);
				npcs[i].removeAttribute("update_face");
			}

			if (npcs[i].type == 1282 && System.currentTimeMillis() - npcs[i].lastForcedText > 5000) {
				npcs[i].updateRequired = true;
				npcs[i].lastForcedText = System.currentTimeMillis();
				npcs[i].forceChat("Sell me your thieving supplies! Also try ::starter");
			}

			if (npcs[i].type == 1552) {
				if (Misc.random(50) <= 3) {
					npcs[i].updateRequired = true;
					npcs[i].forceChat("Ho, ho, ho, Merry Christmas!");
					int randomEmote = Misc.random(2);
					if (randomEmote == 0) {
						npcs[i].animNumber = 861;
					} else if (randomEmote == 1) {
						npcs[i].animNumber = 866;
					} else if (randomEmote == 2) {
						npcs[i].animNumber = 862;
					}
					npcs[i].animUpdateRequired = true;
				}
			}
			if (npcs[i].type == 3082) {
				if (Misc.random(50) <= 3) {
					npcs[i].updateRequired = true;
					npcs[i].forceChat("Workity, workity, work, Hrmph!");
				}
			}
			if (npcs[i].type == 3083) {
				if (Misc.random2(50) <= 3) {
					npcs[i].updateRequired = true;
					npcs[i].forceChat("Search the decoration boxes! Find those baubles!");
				}
			}
			if (npcs[i].type == 3084) {
				if (Misc.random2(50) <= 3) {
					npcs[i].updateRequired = true;
					npcs[i].forceChat("Search the decoration boxes! Find those baubles!");
				}
			}
			if (npcs[i].type == 3085) {
				if (Misc.random2(50) <= 3) {
					npcs[i].updateRequired = true;
					npcs[i].forceChat("Get those baubles painted!");
				}
			}
			if (npcs[i].type == 3086) {
				if (Misc.random2(50) <= 3) {
					npcs[i].updateRequired = true;
					npcs[i].forceChat("Get those baubles painted!");
				}
			}
			if (npcs[i].type == 3087) {
				if (Misc.random2(50) <= 3) {
					npcs[i].updateRequired = true;
					npcs[i].forceChat("Build them marionette pieces together!");
				}
			}
			if (npcs[i].type == 3088) {
				if (Misc.random2(50) <= 3) {
					npcs[i].updateRequired = true;
					npcs[i].forceChat("Find those marionette pieces!");
				}
			}
			if (npcs[i].freezeTimer > 0) {
				npcs[i].freezeTimer--;
			}
			if (npcs[i].hitDelayTimer > 0) {
				npcs[i].hitDelayTimer--;
			}
			if (npcs[i].walkingType == 100) {
				follow(npcs[i], PlayerHandler.players[npcs[i].spawnedBy]);
			}
		}

		// wait for path finding
		while (atomicInteger.get() != 0) {

		}

		for (int i = 0; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				continue;
			}

			if (npcs[i].hitDelayTimer == 1) {
				npcs[i].hitDelayTimer = 0;

				if (npcs[i].attackingNpcId > 0) {
					applyNpcDamage(npcs[i], npcs[npcs[i].attackingNpcId]);

					if (npcs[i].secondHit) {
						applyNpcDamage(npcs[i], npcs[npcs[i].attackingNpcId]);

						npcs[i].secondHit = false;
					}
				} else {
					applyDamage(npcs[i]);

					if (npcs[i].secondHit) {

						applyDamage(npcs[i]);
						npcs[i].secondHit = false;
					}
				}
			}

		}

		for (int i = 0; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				continue;
			}

			if (npcs[i].attackTimer > 0) {
				npcs[i].attackTimer--;
			}

			/**
			 * npcs created by a player (this is not related to summoning)
			 */
			if (npcs[i].spawnedBy > 0) {
				if (NPCHandler.canRemove(PlayerHandler.players[npcs[i].spawnedBy], npcs[i])) {
					if (PlayerHandler.players[npcs[i].spawnedBy] != null) {
						for (int o = 0; o < PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs.length; o++) {
							if (npcs[i].type == PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][0]) {
								if (PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][1] == 1) {
									PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][1] = 0;
								}
							}
						}
					}
					npcs[i].slot = -1;
					removeNpc(i);
					continue;
				}
			}

			/**
			 * npc aggression
			 */

			if (npcs[i].type == 14205 && Zombies.inGameArea(npcs[i].getLocation()) && npcs[i].killerId == 0) {
				npcs[i].killerId = getCloseRandomPlayer(npcs[i]);
			}

			if (NPCConfig.isAggressive(npcs[i]) && !npcs[i].underAttack && !npcs[i].isDead
					&& !NPCConfig.switchesAttackers(npcs[i])) {
				npcs[i].killerId = getCloseRandomPlayer(npcs[i]);
			} else if (NPCConfig.isAggressive(npcs[i]) && !npcs[i].underAttack && !npcs[i].isDead
					&& NPCConfig.switchesAttackers(npcs[i])) {
				npcs[i].killerId = getCloseRandomPlayer(npcs[i]);
			}

			/**
			 * resets who the npc is under attack by
			 */
			if (System.currentTimeMillis() - npcs[i].lastDamageTaken > 5000) {
				npcs[i].underAttackBy = 0;
			}
		}

		for (int i = 0; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				continue;
			}

			npcs[i].process();

			/**
			 * player attacking
			 */
			if ((npcs[i].killerId > 0 || npcs[i].underAttack || npcs[i].attackingNpcId > 0) && !npcs[i].walkingHome
					&& NPCConfig.retaliates(npcs[i].type)) {
				if (!npcs[i].isDead) {

					boolean validOpponent = false;

					if (npcs[i].attackingNpcId > 0) {

						NPC npc = npcs[npcs[i].attackingNpcId];

						if (npc != null) {
							// follow and attack
							followNpc(npcs[i], npc);

							if (npcs[i].attackTimer == 0)
								attackNpc(npcs[i], npc);

							validOpponent = true;
						}
					} else if (npcs[i].killerId > 0 && !NPCConfig.canAttack(npcs[i].type)) {
						Player player = PlayerHandler.players[npcs[i].killerId];

						// were a summoning companion, if the owner cannot attack neither can the
						// familiar
						if (npcs[i].getOwnerId() > 0) {
							Player owner = PlayerHandler.players[npcs[i].getOwnerId()];
							// this should happen but just incase
							if (owner != null) {
								if (!owner.inWild()) {
									npcs[i].killerId = 0;
									npcs[i].underAttackBy = 0;
									continue;
								}
							}
						}

						if (player != null) {
							follow(npcs[i], player);
							if (npcs[i].attackTimer == 0) {
								attackPlayer(player, npcs[i]);
							}
							if (npcs[i].getCombat() != null) {
								npcs[i].getCombat().process();
							}
							validOpponent = true;
						}
					}
					if (!validOpponent) {
						npcs[i].attackingNpcId = 0;
						npcs[i].killerId = 0;
						npcs[i].underAttack = false;
						npcs[i].facePlayer(0);
					}
				}
			}

			/**
			 * Random walking and walking home
			 **/
			if (npcs[i].getOwnerId() == -1 && (!npcs[i].underAttack || npcs[i].walkingHome) && npcs[i].randomWalk
					&& !npcs[i].isDead) {
				npcs[i].facePlayer(0);
				npcs[i].killerId = 0;
				/*
				 * if (npcs[i].spawnedBy == 0 && !Zombies.inGameArea(npcs[i].getLocation()) &&
				 * !Hunter.isImp(npcs[i]) && !PestControl.isShifter(npcs[i].type)) { if
				 * ((npcs[i].absX > npcs[i].makeX + Config.NPC_RANDOM_WALK_DISTANCE) ||
				 * (npcs[i].absX < npcs[i].makeX - Config.NPC_RANDOM_WALK_DISTANCE) ||
				 * (npcs[i].absY > npcs[i].makeY + Config.NPC_RANDOM_WALK_DISTANCE) ||
				 * (npcs[i].absY < npcs[i].makeY - Config.NPC_RANDOM_WALK_DISTANCE)) {
				 * npcs[i].walkingHome = true; } }
				 */
				if (npcs[i].walkingHome && npcs[i].absX == npcs[i].makeX && npcs[i].absY == npcs[i].makeY) {
					npcs[i].walkingHome = false;
				} else if (npcs[i].walkingHome) {
					npcs[i].moveX = NPCHandler.getMove(npcs[i].absX, npcs[i].makeX);
					npcs[i].moveY = NPCHandler.getMove(npcs[i].absY, npcs[i].makeY);
					npcs[i].getNextNPCMovement();
					npcs[i].updateRequired = true;
				}
				if (npcs[i].walkingType == 1) {
					if (Hunter.isImp(npcs[i]) || Hunter.isHunterNpc(npcs[i]) && Misc.random(1) == 0
							|| Misc.random(6) == 0 && !npcs[i].walkingHome) {
						int walk = Misc.random(4);

						if (Hunter.isImp(npcs[i])) {
							if (!npcs[i].attributeExists("last_turn")) {
								npcs[i].setAttribute("last_turn", walk);
							} else {
								if (Misc.random(3) == 0) {
									npcs[i].setAttribute("last_turn", walk);
								} else {
									walk = (Integer) npcs[i].getAttribute("last_turn");
								}
							}
						}

						switch (walk) {
						case 0: // North

							if (Hunter.isImp(npcs[i])) {
								npcs[i].moveX = 0;
								npcs[i].moveY = 1;
								break;
							}

							if ((npcs[i].absY + 1) > (npcs[i].makeY + Config.NPC_RANDOM_WALK_DISTANCE)) {
								break;
							}
							if (NpcPathFinder.canMove(npcs[i].getLocation(), NpcPathFinder.Move.NORTH,
									npcs[i].getDefinition().getSize())) {
								npcs[i].moveX = 0;
								npcs[i].moveY = 1;
							}
							break;
						case 1: // South

							if (Hunter.isImp(npcs[i])) {
								npcs[i].moveX = 0;
								npcs[i].moveY = -1;
								break;
							}

							if ((npcs[i].absY - 1) < (npcs[i].makeY - Config.NPC_RANDOM_WALK_DISTANCE)) {
								break;
							}
							if (NpcPathFinder.canMove(npcs[i].getLocation(), NpcPathFinder.Move.SOUTH,
									npcs[i].getDefinition().getSize())) {
								npcs[i].moveX = 0;
								npcs[i].moveY = -1;
							}
							break;
						case 2: // West

							if (Hunter.isImp(npcs[i])) {
								npcs[i].moveX = -1;
								npcs[i].moveY = 0;
								break;
							}

							if ((npcs[i].absX - 1) < (npcs[i].makeX - Config.NPC_RANDOM_WALK_DISTANCE)) {
								break;
							}
							if (NpcPathFinder.canMove(npcs[i].getLocation(), NpcPathFinder.Move.WEST,
									npcs[i].getDefinition().getSize())) {
								npcs[i].moveX = -1;
								npcs[i].moveY = 0;
							}
							break;
						case 3: // East

							if (Hunter.isImp(npcs[i])) {
								npcs[i].moveX = 1;
								npcs[i].moveY = 0;
								break;
							}

							if ((npcs[i].absX + 1) > (npcs[i].makeX + Config.NPC_RANDOM_WALK_DISTANCE)) {
								break;
							}
							if (NpcPathFinder.canMove(npcs[i].getLocation(), NpcPathFinder.Move.EAST,
									npcs[i].getDefinition().getSize())) {
								npcs[i].moveX = 1;
								npcs[i].moveY = 0;
							}
							break;
						}

						if (Hunter.isImp(npcs[i])) {
							int x = npcs[i].absX + npcs[i].moveX;
							int y = npcs[i].absY + npcs[i].moveY;

							if (x > 2621 || x < 2562 || y < 4290 || y > 4349) {
								npcs[i].moveX = 0;
								npcs[i].moveY = 0;
							}
						}

						if (npcs[i].moveX != 0 || npcs[i].moveY != 0) {
							npcs[i].getNextNPCMovement();
							npcs[i].updateRequired = true;
							npcs[i].moveX = 0;
							npcs[i].moveY = 0;
						}
					}
				}
			}

			/**
			 * dying and respawning of npcs
			 */
			if (npcs[i].isDead) {

				// barricade respawning fix
				if (npcs[i].type == 1532 || npcs[i].type == 1533) {
					if (npcs[i].spawnedBy != 1) {
						CastleWars.removeBarricade(npcs[i]);
					}
				}

				if (npcs[i].actionTimer == 0 && npcs[i].applyDead == false && npcs[i].needRespawn == false) {

					if (npcs[i].getCombat() != null) {
						npcs[i].getCombat().onDeath();
					}

					npcs[i].updateRequired = true;
					npcs[i].facePlayer(0);
					npcs[i].killedBy = getNpcKillerId(i);
					npcs[i].animNumber = NPCAnimations.getDeadEmote(i); // dead emote
					npcs[i].animUpdateRequired = true;
					npcs[i].freezeTimer = 0;
					npcs[i].applyDead = true;
					killedBarrow(i);
					npcs[i].actionTimer = 4; // delete time
					resetPlayersInCombat(i);
					// npcs[i].slot = -1;
				} else if (npcs[i].actionTimer == 0 && npcs[i].applyDead == true && npcs[i].needRespawn == false) {
					npcs[i].needRespawn = true;
					npcs[i].actionTimer = NPCConfig.getRespawnTime(npcs[i]); // respawn time

					PestControl.npcDied(npcs[i]);

					Player c = PlayerHandler.players[npcs[i].killedBy];

					if (c != null) {
						ServerEvents.onNpcKilled(c, npcs[i]);
						dropItems(c, npcs[i]); // npc drops items!
					}

					appendKillCount(npcs[i]);

					/**
					 * if(npcs[i].type == 2745) { Player c =
					 * (Player)PlayerHandler.players[npcs[i].killedBy]; //c.getPA().movePlayer(2438,
					 * 5168, 0); //c.sendMessage("Well done!, you have killed TzTok-Jad");
					 * //c.sendMessage("Take this as an reward!"); //c.getItems().addItem(6570,1);
					 * //c.getItems().addItem(6529,16384);
					 * if(FightCaves.isFightCaveNpc(npcs[i].type)) {
					 * System.out.println(npcs[i].type); c.getFightCaves().killedTzhaar(i, c); }
					 *
					 * for (int j = 0; j < Server.playerHandler.players.length; j++) { if
					 * (Server.playerHandler.players[j] != null && j != c.playerId) { Player c2 =
					 * (Player)Server.playerHandler.players[j]; c2.sendMessage(c.username+" Has just
					 * recieved his Fire Cape!"); } } }
					 */
					npcs[i].absX = npcs[i].makeX;
					npcs[i].absY = npcs[i].makeY;
					npcs[i].setHP(npcs[i].maxHP);
					npcs[i].animNumber = 0x328;
					npcs[i].updateRequired = true;
					npcs[i].animUpdateRequired = true;
					/**
					 * if(FightCave.isCaveNpcs(i)){ Player c =
					 * (Player)PlayerHandler.players[npcs[i].killedBy];
					 * FightCave.processWaveKill(c,npcs[i].type); FightCave.processWaves(c); }
					 */

					/*
					 * if(FightCaves.isFightCaveNpc(npcs[i].type)) {
					 * //System.out.println(npcs[i].type); if(c != null)
					 * c.getFightCaves().killedTzhaar(i, c); }
					 */
					if (npcs[i].type >= 2440 && npcs[i].type <= 2446) {
						Server.objectManager.removeObject(npcs[i].absX, npcs[i].absY, npcs[i].heightLevel);
					}
				} else if (npcs[i].actionTimer == 0 && npcs[i].needRespawn) {

					// void knight portals
					if (npcs[i].type >= 6142 && npcs[i].type <= 6149
							|| npcs[i].type == 3782/* || npc.type >= 3732 && npc.type >= 3736 */) {
						continue;
					}

					PestControl.npcRespawn(npcs[i]);

					npcs[i].getPlayerDamage().clear();

					if (!npcs[i].isRespawn() || npcs[i].getOwnerId() > -1 || npcs[i].spawnedBy > 0) {
						npcs[i].slot = -1;
						removeNpc(i);
					} else {

						if (Hunter.isImp(npcs[i])) {
							npcs[i].makeX = 2562 + Misc.random(59);
							npcs[i].makeY = 4290 + Misc.random(59);
						}

						int old1 = npcs[i].type;
						int old2 = npcs[i].makeX;
						int old3 = npcs[i].makeY;
						int old4 = npcs[i].heightLevel;
						int old5 = npcs[i].walkingType;
						int old6 = npcs[i].maxHP;
						int old7 = npcs[i].maxHit;
						int old8 = npcs[i].attack;
						int old9 = npcs[i].defence;
						Direction direction = npcs[i].faceDirection;

						if (Hunter.isHunterNpc(npcs[i])) {
							old5 = 1;
						}

						// newNPC(1158, 3473, 9491, 0, 1, 255, 31, 175, 255);

						switch (npcs[i].type) {

						case 491: // kraken
						case 493:
							NPC npc = newNPC(old1, old2, old3, old4, old5, old6, old7, old8, old9, direction);
							if (npc != null) {
								InstancesArea area = (InstancesArea) npcs[i].getAttribute("instanced_area");
								if (area != null) {
									area.getNpcs().add(npc);
									npc.setAttribute("instanced_area", area);
								}
							}
							break;

						case 2042: // zulrah
						case 2043:
						case 2044:
							npc = newNPC(2042, 2266, 3072, old4, old5, old6, old7, old8, old9, direction);
							if (npc != null) {
								InstancesArea area = (InstancesArea) npcs[i].getAttribute("instanced_area");
								if (area != null) {
									area.getNpcs().add(npc);
									npc.setAttribute("instanced_area", area);
								}
							}
							break;
						case 1158:
							newNPC(1160, old2, old3, 0, 0, 255, 28, 150, 205, direction);
							break;
						case 1160:
							newNPC(1158, old2, old3, 0, 0, 255, 28, 150, 205, direction);
							break;
						default:
							newNPC(old1, old2, old3, old4, old5, old6, old7, old8, old9, direction);
							break;
						}

						npcs[i].slot = -1;
						removeNpc(i);
					}
				}
			}
		}
	}

	public static Map<Integer, NpcDefinition> getNpcDefinitions() {
		return npcDefinitions;
	}

	public static NpcDefinition getNpcDefinition(int npcType) {
		return npcDefinitions.get(npcType);
	}

	public NPC spawnZombieNpc(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return null; // no free slot found
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.setHP(HP);
		newNPC.maxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
		newNPC.slot = slot;
		newNPC.needRespawn = false;
		newNPC.walkingType = 1;
		npcs[slot].setCombat(NPCConfig.getCombatScript(npcs[slot]));
		return newNPC;
	}

	public static boolean canPlayerAttackNPC(Player player, NPC npc, boolean message) {
		if (npc.type == 5862 && player.level[PlayerConstants.SLAYER] < 91) {
			if (message) {
				player.sendMessage("This monster requires level 91 slayer to attack.");
			}
			return false;
		}
		if (npc.type == 8596 && player.combatLevel < 70) {
			if (message) {
				player.sendMessage("This monster requires level 70+ combat level to attack.");
			}
			return false;
		}

		return true;
	}

}
