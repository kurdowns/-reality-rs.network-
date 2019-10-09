package com.zionscape.server.net.packets;

import com.google.common.base.MoreObjects;
import com.zionscape.server.Config;
import com.zionscape.server.model.content.Resting;
import com.zionscape.server.model.content.achievements.Achievements;
import com.zionscape.server.model.items.Blowpipe;
import com.zionscape.server.model.items.TridentOfTheSeas;
import com.zionscape.server.model.items.TridentOfTheSwamp;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.combat.CombatHelper;
import com.zionscape.server.model.players.skills.hunter.Hunter;
import com.zionscape.server.model.players.skills.slayer.Slayer;
import com.zionscape.server.model.players.skills.slayer.Tasks;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

/**
 * Click NPC
 */
public class ClickNPC implements PacketType {


	public static final int ATTACK_NPC = 72, MAGE_NPC = 131, FIRST_CLICK = 155, SECOND_CLICK = 17, THIRD_CLICK = 21,
			FOURTH_CLICK = 18;

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
		if (c.isDoingTutorial())
			return;
		c.npcIndex = 0;
		c.npcClickIndex = 0;
		c.playerIndex = 0;
		c.clickNpcType = 0;
		c.lampVfy = false;
		c.getPA().resetFollow();
		Resting.stop(c);
		c.resetDialogue();

		switch (packetType) {
			/**
			 * Attack npc melee or range
			 **/
			case ATTACK_NPC:

				// don't know what this did but it caused the i can't reach that bug
				/*if (!c.mageAllowed) {
					c.mageAllowed = true;
                    c.sendMessage("I can't reach that.");
                    break;
                }*/

				c.npcIndex = stream.readUnsignedWordA();
				if (c.npcIndex > NPCHandler.npcs.length) {
					break;
				}
				if (NPCHandler.npcs[c.npcIndex] == null) {
					c.npcIndex = 0;
					break;
				}
				// 5th option hack on kuradel
				if (NPCHandler.npcs[c.npcIndex].type == 9085) {
					Slayer.onNpcClick(c, NPCHandler.npcs[c.npcIndex], 5);
					c.npcIndex = 0;
					break;
				}
				if (NPCHandler.npcs[c.npcIndex].maxHP == 0) {
					c.npcIndex = 0;
					break;
				}

				if (c.rights >= 3) {
					NPC npc = NPCHandler.npcs[c.npcIndex];
					c.sendMessage(MoreObjects.toStringHelper(ClickNPC.class).add("type", npc.type).add("index", c.npcIndex).add("loc", npc.getLocation()).toString());
				}

                /*NPC npc1 = NPCHandler.npcs[c.npcIndex];
				if (!Collision.canMove(c.absX, c.absY, npc1.absX, npc1.absY, c.heightLevel, 1, 1)) {
                    c.sendMessage("I can't reach that.");
                    break;
                }*/

				NPC npc1 = NPCHandler.npcs[c.npcIndex];
				int requiredSlayer = Tasks.getRequiredSlayerLevel(npc1.type);
				if (c.getPA().getActualLevel(PlayerConstants.SLAYER) < requiredSlayer) {
					c.sendMessage("A Slayer level requirement of " + requiredSlayer + " is required to attack this NPC.");
					c.npcIndex = 0;
					break;
				}

				if (npc1.getOwnerId() > -1 && !npc1.inWild()) {
					c.sendMessage("You cannot attack this npc.");
					c.npcIndex = 0;
					break;
				}

				if (!NPCHandler.canPlayerAttackNPC(c, NPCHandler.npcs[c.npcIndex], true)) {
					c.npcIndex = 0;
					break;
				}

				if(c.equipment[PlayerConstants.WEAPON] == TridentOfTheSeas.CHARGED_TRIDENT) {
					c.autocastId = 57;
					c.autocasting = true;

					if(c.getData().tridentSeaCharges <= 0) {
						c.sendMessage("Your staff has no charges left.");
						c.npcIndex = 0;
						return;
					}
				}

				if(c.equipment[PlayerConstants.WEAPON] == TridentOfTheSwamp.CHARGED_TRIDENT) {
					c.autocastId = 58;
					c.autocasting = true;

					if(c.getData().tridentSwampCharges <= 0) {
						c.sendMessage("Your staff has no charges left.");
						c.npcIndex = 0;
						return;
					}
				}

				if (c.autocastId > 0) {
					c.autocasting = true;
				}
				if (!c.autocasting && c.spellId > 0) {
					c.spellId = 0;
				}
				c.faceUpdate(c.npcIndex);
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

				if (c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[c.npcIndex].getX(), NPCHandler.npcs[c.npcIndex].getY(), CombatHelper.getRequiredDistance(c))) {
					c.stopMovement();
				}

				c.usingBow = usingBow;
				c.usingRangeWeapon = usingOtherRangeWeapons;

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

				if (c.followId > 0) {
					c.getPA().resetFollow();
				}
				if (c.attackTimer <= 0) {
					c.getCombat().attackNpc(c.npcIndex);
					c.attackTimer++;
				}
				break;
			/**
			 * Attack npc with magic
			 **/
			case MAGE_NPC:
				// c.usingSpecial = false;
				// c.getItems().updateSpecialBar();
				c.npcIndex = stream.readSignedWordBigEndianA();
				int castingSpellId = stream.readDWord();

				c.usingMagic = false;
				if (c.npcIndex > NPCHandler.maxNPCs) {
					return;
				}
				if (NPCHandler.npcs[c.npcIndex] == null) {
					break;
				}

				if (c.rights >= 3)
					c.sendMessage("attacking npc " + NPCHandler.npcs[c.npcIndex].type);

				if (NPCHandler.npcs[c.npcIndex].maxHP == 0 || NPCHandler.npcs[c.npcIndex].type == 944) {
					c.sendMessage("You can't attack this npc.");
					break;
				}

				requiredSlayer = Tasks.getRequiredSlayerLevel(NPCHandler.npcs[c.npcIndex].type);
				if (c.getPA().getActualLevel(PlayerConstants.SLAYER) < requiredSlayer) {
					c.sendMessage("A Slayer level requirement of " + requiredSlayer + " is required to attack this NPC.");
					c.npcIndex = 0;
					break;
				}

				if (NPCHandler.npcs[c.npcIndex].getOwnerId() > -1 && !NPCHandler.npcs[c.npcIndex].inWild()) {
					c.sendMessage("You cannot attack this npc.");
					c.npcIndex = 0;
					break;
				}

				if (!NPCHandler.canPlayerAttackNPC(c, NPCHandler.npcs[c.npcIndex], true)) {
					c.npcIndex = 0;
					break;
				}

                /*npc1 = NPCHandler.npcs[c.npcIndex];
				if (!Region.canMove(c.absX, c.absY, npc1.absX, npc1.absY, c.heightLevel, 1, 1)) {
                    c.sendMessage("I can't reach that.");
                    break;
                }*/

				for (int i = 0; i < c.MAGIC_SPELLS.length; i++) {
					if (castingSpellId == c.MAGIC_SPELLS[i][0]) {
						c.spellId = i;
						c.usingMagic = true;
						break;
					}
				}

				if (castingSpellId >= 1190 && castingSpellId <= 1192) {
					Achievements.progressMade(c, Achievements.Types.CAST_EACH_GOD_SPELL, castingSpellId);
				}

				if (castingSpellId == 1171) { // crumble undead
					for (int npc : Config.UNDEAD_NPCS) {
						if (NPCHandler.npcs[c.npcIndex].type != npc) {
							c.sendMessage("You can only attack undead monsters with this spell.");
							c.usingMagic = false;
							c.stopMovement();
							break;
						}
					}
				}
            /*
             * if(!c.getCombat().checkMagicReqs(c.spellId)) { c.stopMovement(); break; }
			 */
				if (c.autocasting) {
					c.getPA().resetAutocast();
				}
				if (c.usingMagic) {
					if (c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[c.npcIndex].getX(), NPCHandler.npcs[c.npcIndex].getY(), CombatHelper.getRequiredDistance(c))) {
						c.stopMovement();
					}
					if (c.attackTimer <= 0) {
						c.getCombat().attackNpc(c.npcIndex);
						c.attackTimer++;
					}
				}
				break;
			case FIRST_CLICK:
				c.npcClickIndex = stream.readSignedWordBigEndian();
				if (c.npcClickIndex < 0 || c.npcClickIndex > NPCHandler.maxNPCs) {
					c.npcClickIndex = 0;
					return;
				}

				NPC npc = NPCHandler.npcs[c.npcClickIndex];
				c.npcType = npc.type;

				if (c.rights >= 3) {
					c.sendMessage(MoreObjects.toStringHelper(ClickNPC.class).add("type", c.npcType).add("option", 1).toString());
				}

				int distance = 1;

				if (Hunter.isImp(npc)) {
					distance = 2;
				}

				if (c.goodDistance(npc.getX(), npc.getY(), c.getX(), c.getY(), distance)) {
					c.turnPlayerTo(npc.getX(), npc.getY());
					npc.facePlayer(c.playerId);
					c.getActions().firstClickNpc(c.npcType);
				} else {
					c.clickNpcType = 1;
				}
				break;
			case SECOND_CLICK:
				c.npcClickIndex = stream.readUnsignedWordBigEndianA();
				if (c.npcClickIndex < 0 || c.npcClickIndex > NPCHandler.maxNPCs) {
					c.npcClickIndex = 0;
					return;
				}

				npc = NPCHandler.npcs[c.npcClickIndex];
				c.npcType = npc.type;

				if (c.rights >= 3) {
					c.sendMessage(MoreObjects.toStringHelper(ClickNPC.class).add("type", c.npcType).add("option", 2).toString());
				}

				if (c.goodDistance(npc.getX(), npc.getY(), c.getX(), c.getY(), 1)) {
					c.turnPlayerTo(npc.getX(), npc.getY());
					npc.facePlayer(c.playerId);
					c.getActions().secondClickNpc(c.npcType);
				} else {
					c.clickNpcType = 2;
				}
				break;
			case THIRD_CLICK:
				c.npcClickIndex = stream.readSignedWord();
				if (c.npcClickIndex < 0 || c.npcClickIndex > NPCHandler.maxNPCs) {
					c.npcClickIndex = 0;
					return;
				}

				npc = NPCHandler.npcs[c.npcClickIndex];
				c.npcType = NPCHandler.npcs[c.npcClickIndex].type;

				if (c.rights >= 3) {
					c.sendMessage(MoreObjects.toStringHelper(ClickNPC.class).add("type", c.npcType).add("option", 3).toString());
				}

				if (c.goodDistance(npc.getX(), npc.getY(), c.getX(), c.getY(), 1)) {
					c.turnPlayerTo(npc.getX(), npc.getY());
					npc.facePlayer(c.playerId);
					c.getActions().thirdClickNpc(c.npcType);
				} else {
					c.clickNpcType = 3;
				}
				break;
			case FOURTH_CLICK:
				c.npcClickIndex = stream.readUnsignedWordBigEndian();
				if (c.npcClickIndex < 0 || c.npcClickIndex > NPCHandler.maxNPCs) {
					c.npcClickIndex = 0;
					return;
				}
				npc = NPCHandler.npcs[c.npcClickIndex];
				c.npcType = NPCHandler.npcs[c.npcClickIndex].type;

				if (c.rights >= 3) {
					c.sendMessage(MoreObjects.toStringHelper(ClickNPC.class).add("type", c.npcType).add("option", 4).toString());
				}

				if (c.goodDistance(npc.getX(), npc.getY(), c.getX(), c.getY(), 1)) {
					c.turnPlayerTo(npc.getX(), npc.getY());
					npc.facePlayer(c.playerId);
					c.getActions().fourthClickNpc(c.npcType);
				} else {
					c.clickNpcType = 4;
				}
				break;
		}
	}
}
