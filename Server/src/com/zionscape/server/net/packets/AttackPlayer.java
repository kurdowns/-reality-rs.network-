package com.zionscape.server.net.packets;

import com.zionscape.server.Config;
import com.zionscape.server.ServerEvents;
import com.zionscape.server.model.content.Resting;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.content.minigames.DuelArena;
import com.zionscape.server.model.content.minigames.gambling.Gambling;
import com.zionscape.server.model.items.Blowpipe;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.combat.CombatHelper;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

/**
 * Attack Player
 */
public class AttackPlayer implements PacketType {

	public static final int ATTACK_PLAYER = 73, MAGE_PLAYER = 249;

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {

		Resting.stop(c);

		c.playerIndex = 0;
		c.npcIndex = 0;
		switch (packetType) {
			/**
			 * Attack player
			 **/
			case ATTACK_PLAYER:
				c.playerIndex = stream.readSignedWordBigEndian();
				if (c.playerIndex > (Config.MAX_PLAYERS - 1) || c.playerIndex < 0) {
					return;
				}
				if (PlayerHandler.players[c.playerIndex] == null) {
					break;
				}
				if (c.respawnTimer > 0) {
					break;
				}

				Player target = PlayerHandler.players[c.playerIndex];


				if(Gambling.inArea(target) && Gambling.inArea(c)) {
					Gambling.request(c, target);
					c.playerIndex = 0;
					return;
				}

               /* if(c.ironman) {
					c.sendMessage("You cannot do this on an ironman account.");
                    return;
                }*/

				if (PlayerHandler.players[c.playerIndex].inDuelArena()) {
					if (c.getDuel() == null || c.getDuel().getStage() == DuelArena.Stage.REQUESTING) {
						if (c.getLocation().isWithinInteractionDistance(PlayerHandler.players[c.playerIndex].getLocation())) {
							DuelArena.requestDuel(c, c.playerIndex);
						} else {
							c.duelRequestId = c.playerIndex;
						}
						c.playerIndex = 0;
						return;
					}
				}

				if (ServerEvents.onAttackPlayer(c, PlayerHandler.players[c.playerIndex])) {
					c.playerIndex = 0;
					return;
				}

				if (c.autocastId > 0) {
					c.autocasting = true;
				}
				if (!c.autocasting && c.spellId > 0) {
					c.spellId = 0;
				}
				c.mageFollow = false;
				c.spellId = 0;
				c.usingMagic = false;

				boolean usingBow = false;
				boolean usingOtherRangeWeapons = false;
				boolean usingCross = c.getCombat().usingCrossbow(c.equipment[PlayerConstants.WEAPON]);

				for (int bowId : c.BOWS) {
					if (c.equipment[c.playerWeapon] == bowId) {
						usingBow = true;
						break;
					}
				}
				for (int otherRangeId : c.OTHER_RANGE_WEAPONS) {
					if (c.equipment[c.playerWeapon] == otherRangeId) {
						usingOtherRangeWeapons = true;
						break;
					}
				}

				if(c.equipment[PlayerConstants.WEAPON] == 25096) {
					c.sendMessage("You cannot use Trident of the seas in PvP.");
					return;
				}

				if (c.getDuel() != null && c.getDuel().getStage() == DuelArena.Stage.FIGHTING) {
					if (!c.getDuel().isStarted()) {
						c.sendMessage("The duel hasn't started yet!");
						c.playerIndex = 0;
						return;
					}
					if (c.getDuel().getRules()[PlayerConstants.DUEL_RANGE] && (usingBow || usingOtherRangeWeapons)) {
						c.sendMessage("Range has been disabled in this duel!");
						return;
					}
					if (c.getDuel().getRules()[PlayerConstants.DUEL_MELEE] && (!usingBow && !usingOtherRangeWeapons)) {
						c.sendMessage("Melee has been disabled in this duel!");
						return;
					}
				}

				c.usingRangeWeapon = usingOtherRangeWeapons;
				c.usingBow = usingBow;

				if (c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[c.playerIndex].getX(), PlayerHandler.players[c.playerIndex].getY(), CombatHelper.getRequiredDistance(c))) {
					c.stopMovement();
				}

				if (usingBow || usingCross) {
					if (c.equipment[PlayerConstants.WEAPON] != Blowpipe.BLOWPIPE && !(c.equipment[c.playerWeapon] >= 4212 && c.equipment[c.playerWeapon] <= 4223)
							&& c.equipment[c.playerArrows] == -1 && c.equipment[Player.playerWeapon] != 20173) {
						c.sendMessage("You do not have any ammo for this weapon!");
						c.stopMovement();
						c.getCombat().resetPlayerAttack();
						return;
					}
					if (c.equipment[PlayerConstants.WEAPON] == Blowpipe.BLOWPIPE && c.getData().blowpipeAmmo == 0) {
						if (c.getData().blowpipeAmmo == 0) {
							c.sendMessage("You do not have any ammo for this weapon!");
							c.stopMovement();
							c.getCombat().resetPlayerAttack();
							return;
						}
						if (c.getData().blowpipeCharges == 0) {
							c.sendMessage("You do not have any Toxic blowpipe charges.");
							c.stopMovement();
							c.getCombat().resetPlayerAttack();
							return;
						}
					}
					if (!CombatHelper.correctBowAndArrow(c)) {
						c.sendMessage("You can't use "
								+ c.getItems().getItemName(c.equipment[c.playerArrows]).toLowerCase() + "s with a "
								+ c.getItems().getItemName(c.equipment[c.playerWeapon]).toLowerCase() + ".");
						c.stopMovement();
						c.getCombat().resetPlayerAttack();
						return;
					}
				}
				if (c.getCombat().checkReqs()) {
					c.followId = c.playerIndex;
					if (!c.usingMagic && !usingBow && !usingOtherRangeWeapons) {
						c.getPA().follow();
					}
					if (c.attackTimer <= 0) {
						// c.sendMessage("Tried to attack...");
						// c.getCombat().attackPlayer(c.playerIndex);
						// c.attackTimer++;
					}
				}
				break;
			/**
			 * Attack player with magic
			 **/
			case MAGE_PLAYER:
				if (!c.mageAllowed) {
					c.mageAllowed = true;
					break;
				}
				// c.usingSpecial = false;
				// c.getItems().updateSpecialBar();
				c.playerIndex = stream.readSignedWordA();
				int castingSpellId = stream.readDWord();
				c.usingMagic = false;
				if (c.playerIndex > PlayerHandler.players.length || c.playerIndex < 0) {
					return;
				}
				if (PlayerHandler.players[c.playerIndex] == null) {
					break;
				}
				if (c.respawnTimer > 0) {
					break;
				}

                /*if(c.ironman) {
					c.sendMessage("You cannot do this on an ironman account.");
                    return;
                }*/

				if (ServerEvents.onAttackPlayer(c, PlayerHandler.players[c.playerIndex])) {
					c.playerIndex = 0;
					return;
				}

				if (c.getDuel() != null) {
					if (!c.getDuel().isStarted()) {
						c.sendMessage("The duel hasn't started yet!");
						c.playerIndex = 0;
						return;
					}
					if (c.getDuel().getRules()[PlayerConstants.DUEL_MAGE]) {
						c.sendMessage("Magic has been disabled in this duel!");
						return;
					}
				}

				if (castingSpellId >= 1190 && castingSpellId <= 1192) {
					Achievements.progressMade(c, Achievements.Types.CAST_EACH_GOD_SPELL, castingSpellId);
				}

				Player castOnPlayer = PlayerHandler.players[c.playerIndex];
				/*if (castingSpellId == 30282) {
					castOnPlayer = PlayerHandler.players[c.playerIndex];
					double specLeft;
					c.playerIndex = 0;
					if (castOnPlayer != null) {
						c.startAnimation(4411);
						c.stopMovement();
						if (c.specAmount > 10) {
							specLeft = 10;
							c.specAmount = -10;
							castOnPlayer.specAmount += specLeft;
							if (castOnPlayer.specAmount > 10) {
								castOnPlayer.specAmount = 10;
							}
						} else {
							specLeft = c.specAmount;
							c.specAmount -= specLeft;
							castOnPlayer.specAmount += specLeft;
							if (castOnPlayer.specAmount > 10) {
								castOnPlayer.specAmount = 10;
							}
						}
						c.gfx100(727);
						castOnPlayer.gfx100(733);
						c.getItems().updateSpecialBar();
						castOnPlayer.getItems().updateSpecialBar();
						castOnPlayer.sendMessage("Your special attack has been filled by " + specLeft * 10 + "%!");
					}
				}*/
				if (castingSpellId == 30298) {
					castOnPlayer = PlayerHandler.players[c.playerIndex];
					if (castOnPlayer != null) {
						c.playerIndex = 0;
						if (castOnPlayer.getDuel() != null) {
							break;
						}
						if (c.level[6] < 93) {
							c.sendMessage("You need a magic level of 93 to cast this spell.");
							break;
						}
						if (c.level[1] < 40) {
							c.sendMessage("You need a defence level of 40 to cast this spell.");
							c.stopMovement();
							break;
						}
						if (!c.getItems().playerHasItem(9075, 4) || !c.getItems().playerHasItem(557, 10)
								|| !c.getItems().playerHasItem(560, 2)) {
							c.sendMessage("You don't have the required runes to cast this spell.");
							c.stopMovement();
							break;
						}
						if (System.currentTimeMillis() - c.lastVeng < 30000) {
							c.sendMessage("You can only cast vengeance every 30 seconds.");
							c.stopMovement();
							break;
						}
						if (castOnPlayer.vengOn) {
							c.sendMessage("This player already has vengeance casted on them.");
							c.stopMovement();
							break;
						}
						c.startAnimation(4411);
						c.getItems().deleteItem(9075, 4);
						c.getItems().deleteItem(557, 10);
						c.getItems().deleteItem(560, 2);
						c.getPA().addSkillXP(108, PlayerConstants.MAGIC);
						castOnPlayer.vengOn = true;
						castOnPlayer.gfx100(725);
						c.usingMagic = true;
						c.stopMovement();
						c.lastVeng = System.currentTimeMillis();
						castOnPlayer.sendMessage("You have been vengeanced by " + c.username + "!");
						break;
					}
				}
				for (int i = 0; i < c.MAGIC_SPELLS.length; i++) {
					if (castingSpellId == c.MAGIC_SPELLS[i][0]) {
						c.spellId = i;
						c.usingMagic = true;
						break;
					}
				}
				if (c.autocasting) {
					c.getPA().resetAutocast();
				}
				if (!c.getCombat().checkReqs()) {
					break;
				}
				for (int r = 0; r < c.REDUCE_SPELLS.length; r++) { // reducing spells, confuse etc
					if (PlayerHandler.players[c.playerIndex].REDUCE_SPELLS[r] == c.MAGIC_SPELLS[c.spellId][0]) {
						if ((System.currentTimeMillis() - PlayerHandler.players[c.playerIndex].reduceSpellDelay[r]) < PlayerHandler.players[c.playerIndex].REDUCE_SPELL_TIME[r]) {
							c.sendMessage("That player is currently immune to this spell.");
							c.usingMagic = false;
							c.stopMovement();
							c.getCombat().resetPlayerAttack();
						}
						break;
					}
				}
				if (System.currentTimeMillis() - PlayerHandler.players[c.playerIndex].teleBlockDelay < PlayerHandler.players[c.playerIndex].teleBlockLength
						&& c.MAGIC_SPELLS[c.spellId][0] == 12445) {
					c.sendMessage("That player is already affected by this spell.");
					c.usingMagic = false;
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
				}

			/*
			 * if(!c.getCombat().checkMagicReqs(c.spellId)) { c.stopMovement(); c.getCombat().resetPlayerAttack();
			 * break; }
			 */
				if (c.usingMagic) {

					if (c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[c.playerIndex].getX(),
							PlayerHandler.players[c.playerIndex].getY(), CombatHelper.getRequiredDistance(c))) {
						c.stopMovement();
					}
					if (c.getCombat().checkReqs()) {
						c.followId = c.playerIndex;
						c.mageFollow = true;
						if (c.attackTimer <= 0) {
							// c.getCombat().attackPlayer(c.playerIndex);
							// c.attackTimer++;
						}
					}
				}
				break;
		}
	}
}