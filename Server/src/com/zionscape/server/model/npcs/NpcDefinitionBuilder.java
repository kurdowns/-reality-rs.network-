package com.zionscape.server.model.npcs;

import com.zionscape.server.model.players.combat.Weaknesses;

public class NpcDefinitionBuilder {
    private int id;
    private String name;
    private int size;
    private int combatLevel;
    private int standAnim;
    private int walkAnim;
    private int health;
    private int maxDamage;
    private int attack;
    private int defence;
    private int attackAnimation;
    private int deathAnimation;
    private int blockAnimation;
    private int projectileId;
    private int endGfxId;
    private NpcDefinition.CombatType combatType;
    private boolean aggressive;
    private int hitDelay;
    private int attackDelay;
    private int respawnTime;
    private boolean instancedDrops;
    private Weaknesses weakness;

    public NpcDefinitionBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public NpcDefinitionBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public NpcDefinitionBuilder setSize(int size) {
        this.size = size;
        return this;
    }

    public NpcDefinitionBuilder setCombatLevel(int combatLevel) {
        this.combatLevel = combatLevel;
        return this;
    }

    public NpcDefinitionBuilder setStandAnim(int standAnim) {
        this.standAnim = standAnim;
        return this;
    }

    public NpcDefinitionBuilder setWalkAnim(int walkAnim) {
        this.walkAnim = walkAnim;
        return this;
    }

    public NpcDefinitionBuilder setHealth(int health) {
        this.health = health;
        return this;
    }

    public NpcDefinitionBuilder setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
        return this;
    }

    public NpcDefinitionBuilder setAttack(int attack) {
        this.attack = attack;
        return this;
    }

    public NpcDefinitionBuilder setDefence(int defence) {
        this.defence = defence;
        return this;
    }

    public NpcDefinitionBuilder setAttackAnimation(int attackAnimation) {
        this.attackAnimation = attackAnimation;
        return this;
    }

    public NpcDefinitionBuilder setDeathAnimation(int deathAnimation) {
        this.deathAnimation = deathAnimation;
        return this;
    }

    public NpcDefinitionBuilder setBlockAnimation(int blockAnimation) {
        this.blockAnimation = blockAnimation;
        return this;
    }

    public NpcDefinitionBuilder setProjectileId(int projectileId) {
        this.projectileId = projectileId;
        return this;
    }

    public NpcDefinitionBuilder setEndGfxId(int endGfxId) {
        this.endGfxId = endGfxId;
        return this;
    }

    public NpcDefinitionBuilder setCombatType(NpcDefinition.CombatType combatType) {
        this.combatType = combatType;
        return this;
    }

    public NpcDefinitionBuilder setAggressive(boolean aggressive) {
        this.aggressive = aggressive;
        return this;
    }

    public NpcDefinitionBuilder setHitDelay(int hitDelay) {
        this.hitDelay = hitDelay;
        return this;
    }

    public NpcDefinitionBuilder setAttackDelay(int attackDelay) {
        this.attackDelay = attackDelay;
        return this;
    }

    public NpcDefinitionBuilder setRespawnTime(int respawnTime) {
        this.respawnTime = respawnTime;
        return this;
    }

    public NpcDefinitionBuilder setInstancedDrops(boolean instancedDrops) {
        this.instancedDrops = instancedDrops;
        return this;
    }

    public NpcDefinitionBuilder setWeakness(Weaknesses weakness) {
        this.weakness = weakness;
        return this;
    }

    public NpcDefinition createNpcDefinition() {
        return new NpcDefinition(id, name, size, combatLevel, standAnim, walkAnim, health, maxDamage, attack, defence, attackAnimation, deathAnimation, blockAnimation, projectileId, endGfxId, combatType, aggressive, hitDelay, attackDelay, respawnTime, instancedDrops, weakness);
    }
}