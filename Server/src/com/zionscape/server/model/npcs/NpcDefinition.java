package com.zionscape.server.model.npcs;

import com.zionscape.server.model.players.combat.Weaknesses;

public class NpcDefinition {

	private final int id;
	private final String name;
	private final int size;
	private final int combatLevel;
	private final int standAnim;
	private final int walkAnim;
	private final int health;
	private final int maxDamage;
	private final int attack;
	private final int defence;
	private final int attackAnimation;
	private final int deathAnimation;
	private final int blockAnimation;
	private final int projectileId;
	private final int endGfxId;
	private final CombatType combatType;
	private final boolean aggressive;
	private final int hitDelay;
	private final int attackDelay;
	private final int respawnTime;
	private final boolean instancedDrops;
	private final Weaknesses weakness;

	public enum CombatType {
		MELEE, RANGE, MAGIC
	}

	public NpcDefinition(int id, String name, int size, int combatLevel, int standAnim, int walkAnim, int health, int maxDamage, int attack, int defence, int attackAnimation, int deathAnimation, int blockAnimation, int projectileId, int endGfxId, CombatType combatType, boolean aggressive, int hitDelay, int attackDelay, int respawnTime, boolean instancedDrops, Weaknesses weakness) {
		this.id = id;
		this.name = name;
		this.size = size;
		this.combatLevel = combatLevel;
		this.standAnim = standAnim;
		this.walkAnim = walkAnim;
		this.health = health;
		this.maxDamage = maxDamage;
		this.attack = attack;
		this.defence = defence;
		this.attackAnimation = attackAnimation;
		this.deathAnimation = deathAnimation;
		this.blockAnimation = blockAnimation;
		this.projectileId = projectileId;
		this.endGfxId = endGfxId;
		this.combatType = combatType;
		this.aggressive = aggressive;
		this.hitDelay = hitDelay;
		this.attackDelay = attackDelay;
		this.respawnTime = respawnTime;
		this.instancedDrops = instancedDrops;
		this.weakness = weakness;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getSize() {
		return size;
	}

	public int getCombatLevel() {
		return combatLevel;
	}

	public int getStandAnim() {
		return standAnim;
	}

	public int getWalkAnim() {
		return walkAnim;
	}

	public int getHealth() {
		return health;
	}

	public int getMaxDamage() {
		return maxDamage;
	}

	public int getAttack() {
		return attack;
	}

	public int getDefence() {
		return defence;
	}

	public int getAttackAnimation() {
		return attackAnimation;
	}

	public int getDeathAnimation() {
		return deathAnimation;
	}

	public int getBlockAnimation() {
		return blockAnimation;
	}

	public int getProjectileId() {
		return projectileId;
	}

	public int getEndGfxId() {
		return endGfxId;
	}

	public CombatType getCombatType() {
		return combatType;
	}

	public boolean isAggressive() {
		return aggressive;
	}

	public int getHitDelay() {
		return hitDelay;
	}

	public int getAttackDelay() {
		return attackDelay;
	}

	public int getRespawnTime() {
		return respawnTime;
	}

	public boolean isInstancedDrops() {
		return instancedDrops;
	}

	public Weaknesses getWeakness() {
		return weakness;
	}
}
