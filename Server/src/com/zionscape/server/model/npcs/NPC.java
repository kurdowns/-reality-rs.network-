package com.zionscape.server.model.npcs;

import com.zionscape.server.cache.Collision;
import com.zionscape.server.model.Direction;
import com.zionscape.server.model.Entity;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.minigames.zombies.Zombies;
import com.zionscape.server.model.npcs.combat.zulrah.Zulrah;
import com.zionscape.server.model.players.skills.hunter.Hunter;
import com.zionscape.server.util.Misc;
import com.zionscape.server.util.Stream;

import java.awt.*;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NPC extends Entity {

	public Direction faceDirection = Direction.NONE;
	public int poisonMask;
	public long lastPoison;
	public int poisonDamage;

	public long lastForcedText;
	public boolean damageImmune = false;
	public boolean smartPathfinding = false;
	public int overrideDamage = 0;
	public boolean doSpecial = false;
	public boolean secondHit = false;
	public int attackingNpcId;
	public int id, tries;
	public boolean transformUpdateRequired = false;
	public int transformId;
	public int type;
	public int npcAttacking = 0;
	public boolean npcHadHeal = false;
	public int makeX, makeY, maxHit, defence, attack, moveX, moveY, direction, walkingType;
	public int viewX, viewY, kbdAttack;
	public Optional<Location> destination = Optional.empty();
	public Optional<Deque<Location>> path = Optional.empty();
	public int attackType, projectileId, endGfx, spawnedBy, hitDelayTimer, maxHP, hitDiff, hitIcon, hitFocus,
			hitMax, animNumber, actionTimer, enemyX, enemyY;
	public boolean applyDead, isDead, needRespawn;
	public boolean walkingHome, underAttack;
	public int freezeTimer, attackTimer, killerId, killedBy, oldIndex, underAttackBy;
	public long lastDamageTaken;
	public boolean randomWalk;
	public boolean dirUpdateRequired;
	public boolean animUpdateRequired;
	public boolean hitUpdateRequired;
	public boolean updateRequired;
	public boolean forcedChatRequired;
	public boolean faceToUpdateRequired;
	public int firstAttacker;
	public String forcedText;
	public int bork, kQueen, kalphRespawn = -1;
	public NPC jad;
	public long lastJadHeal;
	/**
	 * Graphics
	 */
	public int mask80var1 = 0;
	public int mask80var2 = 0;
	/**
	 * Face
	 */
	public int FocusPointX = -1, FocusPointY = -1;
	public int face = 0;
	public int hitDiff2 = 0;

	private int hitMax2;
	private int hitIcon2;
	private int hitFocus2;

	public boolean hitUpdateRequired2 = false;
	public int slot = -1;
	protected boolean mask80update = false;
	/**
	 * attackType: 0 = melee, 1 = range, 2 = mage
	 */

	private int HP;
	private Map<Integer, Integer> playerDamage = new HashMap<>();
	private boolean respawn = true;
	private boolean skipCombatTurn;
	private NpcDefinition definition;
	private NPCCombat combat;
	private int ownerId = -1;

	public NPC(int id, int _npcType) {
		this.id = id;
		type = _npcType;
		direction = -1;
		isDead = false;
		applyDead = false;
		actionTimer = 0;
		randomWalk = true;

		definition = NPCHandler.getNpcDefinition(type);
	}

	public static int getFreeIndex() {
		for (int i = 0; i < NPCHandler.npcs.length; i++) {
			if (NPCHandler.npcs[i] == null)
				return i;
		}
		return -1;
	}

	public static int getCurrentHP(int i, int i1, int i2) {
		double x = (double) i / (double) i1;
		return (int) Math.round(x * i2);
	}

	public boolean faceEntityUpdatRequired() {
		return dirUpdateRequired;
	}

	public void requestTransform(int Id) {
		transformId = Id;
		transformUpdateRequired = true;
		updateRequired = true;
	}

	public void appendTransformUpdate(Stream str) {
		str.writeWordBigEndianA(transformId);
	}

	public void faceNpc(int i) {
		face = i;
		dirUpdateRequired = true;
		updateRequired = true;
	}

	/**
	 * Checks if the position is within distance of another.
	 *
	 * @param otherX   The other position x.
	 * @param distance The distance.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isWithinDistance(int otherX, int otherY, int distance) {
		int deltaX = Math.abs(absX - otherX);
		int deltaY = Math.abs(absY - otherY);
		return deltaX <= distance && deltaY <= distance;
	}

	public void updateNPCMovement(Stream str) {
		if (direction == -1) {
			if (updateRequired) {
				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
		} else {
			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.XLATE_DIRECTION_TO_CLIENT[direction]);
			if (updateRequired) {
				str.writeBits(1, 1);
			} else {
				str.writeBits(1, 0);
			}
		}
	}

	/**
	 * Text update
	 */
	public void forceChat(String text) {
		forcedText = text;
		forcedChatRequired = true;
		updateRequired = true;
	}

	public void appendMask80Update(Stream str) {
		str.writeWord(mask80var1);
		str.writeDWord(mask80var2);
	}

	public void gfx100(int gfx) {

		// block all cerberous gfx
		if (type == 5862) {
			return;
		}

		mask80var1 = gfx;
		mask80var2 = 6553600;
		mask80update = true;
		updateRequired = true;
	}

	public void gfx0(int gfx) {

		// block all cerberous gfx
		if (type == 5862) {
			return;
		}

		mask80var1 = gfx;
		mask80var2 = 65536;
		mask80update = true;
		updateRequired = true;
	}

	public void appendAnimUpdate(Stream str) {
		str.writeWordBigEndian(animNumber);
		str.writeByte(1);
	}

	private void appendSetFocusDestination(Stream str) {
		str.writeWordBigEndian(FocusPointX);
		str.writeWordBigEndian(FocusPointY);
	}

	public void turnNpc(int i, int j) {
		FocusPointX = 2 * i + 1;
		FocusPointY = 2 * j + 1;
		updateRequired = true;

		face = -1;
		dirUpdateRequired = true;
	}

	public void appendFaceEntity(Stream str) {
		str.writeWord(face);
	}

	public void facePlayer(int player) {
		// barricades
		if (type == 1532) {
			return;
		}

		if (ownerId > -1) {
			if (player != ownerId) {
				return;
			}
		}

		if (attributeExists("block_face_player")) {
			return;
		}

		// don't face if we're set to face a certain location
		if (FocusPointX > -1 || FocusPointY > -1) {
			return;
		}

		if (player + 32768 == face) {
			//return;
		}

		face = player + 32768;
		dirUpdateRequired = true;
		updateRequired = true;
	}

	public void appendFaceToUpdate(Stream str) {
		str.writeWordBigEndian(viewX);
		str.writeWordBigEndian(viewY);
	}

	public boolean checkWalk(int dirX, int dirY) {
		if (!canMove(dirX, dirY)) {
			return false;
		}
        /*if (walkIntoNpc(dirX, dirY)) {
    		return false;
    	}*/
		return true;
	}

	public boolean goodDistanceEntity(Entity other, int distance) {
		int size = getDefinition().getSize();
		Rectangle thisArea = new Rectangle(absX - distance, absY - distance, 2 * distance + size, 2 * distance + size);
		Rectangle otherArea = new Rectangle(other.getX(), other.getY(), 1, 1);
		return thisArea.intersects(otherArea);
	}

	public boolean canMove(int x, int y) {
		return Collision.canMove(absX, absY, absX + x, absY + y, heightLevel, getDefinition().getSize(), getDefinition().getSize());
	}

	public void appendNPCUpdateBlock(Stream str) {
		if (!updateRequired) return;
		int updateMask = 0;
		if (animUpdateRequired) updateMask |= 0x10;
		if (hitUpdateRequired2) updateMask |= 8;
		if (mask80update) updateMask |= 0x80;
		if (faceEntityUpdatRequired()) updateMask |= 0x20;
		if (forcedChatRequired) updateMask |= 1;
		if (hitUpdateRequired) updateMask |= 0x40;
		if (transformUpdateRequired) updateMask |= 2;
		if (FocusPointX != -1) updateMask |= 4;

		str.writeByte(updateMask);

		if (animUpdateRequired) appendAnimUpdate(str);
		if (hitUpdateRequired2) appendHitUpdate2(str);
		if (mask80update) appendMask80Update(str);
		if (faceEntityUpdatRequired()) appendFaceEntity(str);
		if (forcedChatRequired) {
			str.writeString(forcedText);
		}
		if (hitUpdateRequired) appendHitUpdate(str);
		if (transformUpdateRequired) appendTransformUpdate(str);
		if (FocusPointX != -1) appendSetFocusDestination(str);

	}

	public void clearUpdateFlags() {
		updateRequired = false;
		forcedChatRequired = false;
		hitUpdateRequired = false;
		hitUpdateRequired2 = false;
		animUpdateRequired = false;
		dirUpdateRequired = false;
		transformUpdateRequired = false;
		mask80update = false;
		forcedText = null;
		moveX = 0;
		moveY = 0;
		direction = -1;
		FocusPointX = -1;
		FocusPointY = -1;
		poisonMask = 0;
	}

	public int getNextWalkingDirection() {
		int dir;
		dir = Misc.direction(absX, absY, absX + moveX, absY + moveY);
		if (dir == -1) {
			return -1;
		}
		dir >>= 1;
		absX += moveX;
		absY += moveY;

		Hunter.npcRandomMoved(this);

		return dir;
	}

	public void getNextNPCMovement() {

		// stop zulrah moving
		if (type == 2042 || type == 2043 || type == 2044) {
			return;
		}

		// cerb
		if (type == 5862) {
			return;
		}

		direction = -1;
		if (freezeTimer == 0) {
			direction = this.getNextWalkingDirection();
		}
	}

	public void startAnimation(int animId) {

		// block all cerberous anims which are not death or attack anims
		if (type == 5862 && animId < 4494 && animId > 4495) {
			return;
		}

		animNumber = animId;
		animUpdateRequired = true;
		updateRequired = true;
	}

	public void appendHitUpdate(Stream str) {
		if (getHP() <= 0) {
			isDead = true;
		}

		str.writeWordA(hitDiff);

		if (poisonMask == 1) {
			str.writeByte(2);
		} else if (hitDiff > 0 && hitMax > 0 && hitDiff > (hitMax * 0.90)) {
			str.writeByte(1);
		} else {
			str.writeByte(0);
		}

		// type absorption,hit,poison,critical,yellow(trouble brewing),dungeeoneering
		str.writeByte(hitIcon - 1);// icon

		// str.writeByte(hitFocus);//focus

		str.writeWordA(getHP());
		str.writeWordA(maxHP);
	}

	// public void handleHitMask(int damage, int icon, int focus) {
	// handleHitMask(damage,damage,icon,focus);
	// }

	public void appendHitUpdate2(Stream str) {
		if (getHP() <= 0) {
			isDead = true;
		}

		str.writeWordA(hitDiff2);

		if (poisonMask == 2) {
			str.writeByte(2);
		} else if (hitDiff2 > 0 && hitMax2 > 0 && hitDiff2 > (hitMax2 * 0.90)) {
			str.writeByte(1);
		} else {
			str.writeByte(0);
		}
		// type absorption,hit,poison,critical,yellow(trouble brewing),dungeeoneering
		str.writeByte(hitIcon2 - 1);// icon
		// str.writeByte(hitFocus);//focus
		str.writeWordA(getHP());
		str.writeWordA(maxHP);
	}

	public void handleHitMask(int damage, int max, int icon, int focus) {
		if (!hitUpdateRequired) {
			hitUpdateRequired = true;
			hitDiff = damage;
			hitIcon = icon;
			hitFocus = focus;
			hitMax = max;
		} else if (!hitUpdateRequired2) {
			hitUpdateRequired2 = true;
			hitDiff2 = damage;
			hitIcon2 = icon;
			hitFocus2 = focus;
			hitMax2 = max;
		}
		updateRequired = true;
	}

	@Override
	public int getX() {
		return absX;
	}

	@Override
	public int getY() {
		return absY;
	}

	public boolean inWild() {
		if (absX > 2941 && absX < 3392 && absY > 3518 && absY < 3966 || absX > 2941 && absX < 3392 && absY > 9918
				&& absY < 10366) {
			return true;
		}
		return false;
	}

	public void forceAnim(int number) {
		animNumber = number;
		animUpdateRequired = true;
		updateRequired = true;
	}

	public int getType() {
		return type;
	}

	public Location getLocation() {
		return Location.create(absX, absY, heightLevel);
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public NPCCombat getCombat() {
		return combat;
	}

	public void setCombat(NPCCombat combat) {
		this.combat = combat;
	}

	public NpcDefinition getDefinition() {
		return definition;
	}

	public boolean skipCombatTurn() {
		return skipCombatTurn;
	}

	public void setSkipCombatTurn(boolean skipCombatTurn) {
		this.skipCombatTurn = skipCombatTurn;
	}

	public boolean isRespawn() {
		return respawn;
	}

	public void setRespawn(boolean respawn) {
		this.respawn = respawn;
	}

	@Override
	public boolean isDead() {
		return isDead || getHP() <= 0;
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.NPC;
	}

	public Map<Integer, Integer> getPlayerDamage() {
		return playerDamage;
	}

	public void dealdamage(int playerId, int damage) {
		if (playerId > -1) {
			getPlayerDamage().put(playerId, getPlayerDamage().getOrDefault(playerId, 0) + damage);
		}
		setHP(getHP() - damage);
	}

	public int getHP() {
		return HP;
	}

	public void setHP(int HP) {
		this.HP = HP;
	}

	public void incrementHP(int increment) {
		this.HP += increment;

		if (this.HP > maxHP) {
			HP = maxHP;
		}
	}

	public void appendPoison(int damage) {
		if (poisonDamage <= 0) {
			poisonDamage = damage;
		}
	}

	public void process() {
		if (System.currentTimeMillis() - lastPoison > 20000 && poisonDamage > 0) {
			int damage = poisonDamage / 2;
			if (damage > 0) {
				if (!hitUpdateRequired) {
					hitUpdateRequired = true;
					hitDiff = damage;
					hitIcon = 0;
					poisonMask = 1;
				} else if (!hitUpdateRequired2) {
					hitUpdateRequired2 = true;
					hitDiff2 = damage;
					hitIcon = 0;
					poisonMask = 2;
				}
				updateRequired = true;

				lastPoison = System.currentTimeMillis();
				poisonDamage--;
				dealdamage(-1, damage);
			} else {
				poisonDamage = -1;
			}
		}
	}


}
