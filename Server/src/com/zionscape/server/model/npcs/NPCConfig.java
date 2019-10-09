package com.zionscape.server.model.npcs;

import com.zionscape.server.model.npcs.combat.*;
import com.zionscape.server.model.npcs.combat.kraken.Kraken;
import com.zionscape.server.model.npcs.combat.kraken.LargeWhirlpool;
import com.zionscape.server.model.npcs.combat.kraken.SmallWhirlpool;
import com.zionscape.server.model.npcs.combat.kraken.Tentacle;
import com.zionscape.server.model.npcs.combat.zulrah.Snakeling;
import com.zionscape.server.model.npcs.combat.zulrah.Zulrah;

public class NPCConfig {

	public static NPCCombat getCombatScript(NPC npc) {
		if (npc.type >= 13465 && npc.type <= 13481) {
			return new Revenant(npc);
		}
		switch (npc.type) {

			case 14383:
				return new Callisto(npc);

			case 14385:
				return new Thermo(npc);

			case 14386:
				return new Vetion(npc);

			case 14384:
				return new Venenatis(npc);

			case 492:
				return new SmallWhirlpool(npc);

			case 490:
				return new LargeWhirlpool(npc);

			case 491:
				return new Tentacle(npc);

			case 493:
				return new Kraken(npc);


			case 5862:
				return new Cerberus(npc);

			case 2045:
				return new Snakeling(npc);

			case 2042:
			case 2043:
			case 2044:
				return new Zulrah(npc);


			case 13447:
			case 13448:
			case 13449:
			case 13450:
				return new Nex(npc);
			case 9462://ice
			case 9464://desert
			case 9466://jungle
				return new Strykewyrm(npc);
			case 14301:
				return new Glacor(npc);
			case 8528:
				return new Nomad(npc);
		}
		return null;
	}

	public static int getMaxHit(NPC npc) {
		if (npc.getCombat() != null) {
			return npc.getCombat().getDamage();
		}
		if (npc.maxHit > 0) {
			return npc.maxHit;
		}
		return NPCHandler.getNpcDefinition(npc.type).getMaxDamage();
	}

	public static boolean canAttack(int type) {
		switch (type) {

			case 6142: // pest control portals
			case 6143:
			case 6144:
			case 6145:
			case 6146:
			case 6147:
			case 6148:
			case 6149:

			case 1532: // barricades
			case 1533:
				return true;
		}
		return false;
	}

	/**
	 * Hit delays
	 */
	public static int getHitDelay(NPC npc) {
		if (npc.getCombat() != null) {
			return npc.getCombat().getHitDelay();
		}

		NpcDefinition record = NPCHandler.getNpcDefinition(npc.type);
		if (record.getHitDelay() > 0) {
			return record.getHitDelay();
		}

		switch (npc.type) {
			case 3847:
				if (npc.attackType == 1 || npc.attackType == 2) {
					return 3;
				} else {
					return 2;// I think thats it o.o
				}
			case 2881:
			case 2882:
			case 3200:
			case 2892:
			case 2894:
			case 1456:
			case 8349:
			case 8133:
			case 3340:
				return 3;
			case 907:
			case 908:
			case 909:
			case 910:
			case 911:
				return 4;
			case 2743:
			case 2631:
			case 6222:
			case 6223:
			case 6225:
				return 3;
			case 2745:
				return 5;
			case 2025:
			case 50:
				return 4;
			case 2028:
			case 1158:
			case 1160:
				return 3;
			default:
				return 2;
		}
	}

	/**
	 * Attack delays
	 */
	public static int getNpcAttackDelay(NPC npc) {
		if (npc.getCombat() != null) {
			return npc.getCombat().getAttackDelay();
		}

		NpcDefinition record = NPCHandler.getNpcDefinition(npc.type);
		if (record.getAttackDelay() > 0) {
			return record.getAttackDelay();
		}

		switch (npc.type) {
			case 3847:
				return 8;
			case 2025:
			case 2028:
				return 7;
			case 2745:
				return 8;
			case 8349:
				return 6;
			case 6222:
			case 495:
			case 6223:
			case 6225:
			case 6227:
			case 6260:
			case 8133:
			case 3340:
				return 6;
			// saradomin gw boss
			case 6247:
				// case 8133:
				return 2;
			default:
				return 5;
		}
	}

	/**
	 * Npc respawn time
	 */
	public static int getRespawnTime(NPC npc) {
		NpcDefinition record = NPCHandler.getNpcDefinition(npc.type);
		if (record.getRespawnTime() > 0) {
			return record.getRespawnTime();
		}

		switch (npc.type) {

			case 2042:
			case 2043:
			case 2044:
				return 90;

			case 5363: // ok where can we find death/attk emotes
			case 5362:
			case 11773:
				return 150;
			case 50:
			case 8596:
			case 7133:
			case 8133:
			case 8349:
			case 2881:
			case 2882:
			case 2883:
			case 5666:
			case 3847:
			case 1472:
				return 150;
			case 1158:
				return 1;
			case 1160:
				return 150;
			case 6222:
			case 6223:
			case 6225:
			case 6227:
			case 6247:
			case 6248:
			case 6250:
			case 6260:
			case 6261:
			case 6263:
			case 6265:
				return 150;
			case 10773:
				return 70;
			case 3777:
			case 3778:
			case 3779:
			case 3780:
			case 6142:
			case 6143:
			case 6144:
			case 6145:
				return 500;
			case 9462:
			case 9464:
			case 9466:
				return 100;
			default:
				return 25;
		}
	}

	public static boolean overridesPrayerCompletely(int type) {
		return type == 491;
	}

	public static boolean isAggressive(NPC npc) {
		if (NPCHandler.isFightCaveNpc(npc)) {
			return true;
		}

		NpcDefinition record = NPCHandler.getNpcDefinition(npc.type);
		if (record.isAggressive()) {
			return true;
		}

		// attack = 9277
		// death = 9278
		// block should be around those numbers, probably 9276 or 9279 ok
		switch (npc.type) {

			case 491: // tentacle
			case 493: // kraken
				return true;

			case 2045:
			case 2042:
			case 2043:
			case 2044:

			case 3740:
			case 3775:


			case 5452:
			case 8596:
			case 8133:
			case 6204:
			case 6203:
			case 6208:
			case 6206:
			case 2745:
			case 3847:// seatrollqueen
			case 6260:
			case 6261:
			case 50:
			case 1456:
			case 6263:
			case 6265:
			case 6222:
			case 6223:
			case 6225:
			case 6227:
			case 6247:
			case 6248:
			case 6250:
			case 6252:
			case 2892:
			case 2894:
			case 2881:
			case 2882:
			case 2883:
			case 5666:
			case 1158:
			case 7135:
			case 7133:
			case 1160:
				return true;
		}
		if (npc.inWild() && npc.maxHP > 0 && npc.getOwnerId() == -1) {
			if (npc.type == 3340 || npc.type >= 13465 && npc.type <= 13481) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	public static boolean multiAttacks(NPC npc) {
		switch (npc.type) {
			case 13447:
				if (npc.attackType == 2) {
					return true;
				}
				return false;
			case 1158:
			case 1160:
				return true;
			case 8133:
				if ((npc.attackType == 1) || (npc.attackType == 2) || (npc.attackType == 3)) {
					return true;
				}
			case 6222:
				return true;
			case 5666:
				if (npc.attackType == 1) {
					return true;
				}
			case 6247:
				if (npc.attackType == 2) {
					return true;
				}
			case 6260:
				if (npc.attackType == 1) {
					return true;
				}
			default:
				return false;
		}
	}

	public static int offset(NPC npc) {
		switch (npc.type) {

			case 2042: // zulrah
			case 2043:
			case 2044:
				return 2;

			case 50:
				return 2;
			case 2881:
			case 2882:
				return 1;
			case 2745:
			case 2743:
				return 1;
		}
		return 0;
	}

	public static boolean retaliates(int npcType) {
		return npcType < 6142 || npcType > 6145 && !(npcType >= 2440 && npcType <= 2446) || npcType == 4630;
	}

	public static boolean switchesAttackers(NPC npc) {
		switch (npc.type) {
			case 13447: // nex

			case 5452:
			case 6204:
			case 6203:
			case 6208:
			case 6206:
			case 7133:
			case 6261:
			case 6263:
			case 6265:
			case 6223:
			case 6225:
			case 6227:
			case 6248:
			case 6250:
			case 6252:
			case 2892:
			case 2894:
			case 1456:
			case 50:
			case 1472:
			case 10773:
			case 8596:
				return true;
		}
		return false;
	}

}
