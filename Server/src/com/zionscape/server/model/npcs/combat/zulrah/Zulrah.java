package com.zionscape.server.model.npcs.combat.zulrah;

import com.zionscape.server.Server;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Area;
import com.zionscape.server.model.Entity;
import com.zionscape.server.model.InstancesArea;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCCombat;
import com.zionscape.server.model.npcs.NPCConfig;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.objects.GameObject;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.util.Misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


// // TODO: 01/05/2016  snakelings, weaknesses, fix poison, add blow pipe server and client


public class Zulrah extends NPCCombat {

	public static final int ATTACK = 350;
	public static final int DEFENCE = 360;
	private static final int MAX_HIT = 41;

	private static final Location NORTH_SPAWN = Location.create(2266, 3072);
	private static final Location SOUTH_SPAWN = Location.create(2266, 3062);
	private static final Location EAST_SPAWN = Location.create(2275, 3073);
	private static final Location WEST_SPAWN = Location.create(2257, 3071);
	private static final Rotation[][] ROTATIONS = {
			// first rotation
			{
					new Rotation(NORTH_SPAWN, Type.RANGE), //1
					new Rotation(NORTH_SPAWN, Type.MELEE), //2
					new Rotation(NORTH_SPAWN, Type.MAGIC), //3
					new Rotation(SOUTH_SPAWN, Type.RANGE), //4
					new Rotation(NORTH_SPAWN, Type.MELEE), //5
					new Rotation(WEST_SPAWN, Type.MAGIC), // 6
					new Rotation(SOUTH_SPAWN, Type.RANGE), // 7
					new Rotation(SOUTH_SPAWN, Type.MAGIC), // 8
					new Rotation(EAST_SPAWN, Type.JAD), // 9 // jad
					new Rotation(NORTH_SPAWN, Type.MELEE) // 10
			},
			// second rotation
			{
					new Rotation(NORTH_SPAWN, Type.RANGE), // 1
					new Rotation(NORTH_SPAWN, Type.MELEE), // 2
					new Rotation(NORTH_SPAWN, Type.MAGIC), // 3
					new Rotation(WEST_SPAWN, Type.RANGE), // 4
					new Rotation(SOUTH_SPAWN, Type.MAGIC), // 5
					new Rotation(NORTH_SPAWN, Type.MELEE), // 6
					new Rotation(EAST_SPAWN, Type.RANGE), // 7
					new Rotation(SOUTH_SPAWN, Type.MAGIC), // 8
					new Rotation(EAST_SPAWN, Type.JAD), // 9 jad
					new Rotation(NORTH_SPAWN, Type.MELEE) // 10
			},
			// third rotation
			{
					new Rotation(NORTH_SPAWN, Type.RANGE), // 1
					new Rotation(EAST_SPAWN, Type.RANGE), // 2
					new Rotation(NORTH_SPAWN, Type.MELEE), // 3
					new Rotation(WEST_SPAWN, Type.MAGIC), // 4
					new Rotation(SOUTH_SPAWN, Type.RANGE), // 5
					new Rotation(EAST_SPAWN, Type.MAGIC), // 6
					new Rotation(NORTH_SPAWN, Type.RANGE), // 7
					new Rotation(WEST_SPAWN, Type.RANGE), // 8
					new Rotation(NORTH_SPAWN, Type.MAGIC), // 9
					new Rotation(EAST_SPAWN, Type.JAD) // jad
			},
			// fourth rotation
			{
					new Rotation(NORTH_SPAWN, Type.RANGE), // 1
					new Rotation(EAST_SPAWN, Type.MAGIC), // 2
					new Rotation(SOUTH_SPAWN, Type.RANGE), // 3
					new Rotation(WEST_SPAWN, Type.MAGIC), // 4
					new Rotation(NORTH_SPAWN, Type.MELEE), // 5
					new Rotation(EAST_SPAWN, Type.RANGE), // 6
					new Rotation(SOUTH_SPAWN, Type.RANGE), // 7
					new Rotation(WEST_SPAWN, Type.MAGIC), // 8
					new Rotation(NORTH_SPAWN, Type.RANGE), // 9
					new Rotation(EAST_SPAWN, Type.JAD) // jad 10
			}
	};
	private static Location[] TOXIC_SNAKE_LOCATIONS = {
			Location.create(2273, 3075),
			Location.create(2273, 3072),
			Location.create(2272, 3070),
			Location.create(2270, 3069),
			Location.create(2267, 3069),
			Location.create(2264, 3070),
			Location.create(2263, 3072),
			Location.create(2263, 3074),
	};
	private List<ToxicCloud> toxicClouds = new ArrayList<>();
	private List<NPC> snakelings = new ArrayList<>();
	private Type type;
	private Rotation[] rotations;
	private int stage = 0;
	private int cloudsSent = 0;

	public Zulrah(NPC npc) {
		super(npc);

		rotations = ROTATIONS[Misc.random(ROTATIONS.length - 1)];
		type = rotations[stage].getType();
	}

	public static boolean inArea(Entity entity) {
		return new Area(2250, 3050, 2280, 3090, -1).inArea(entity.getLocation());
	}

	@Override
	public void attackPlayer(Player player) {
		if (getNpc().attributeExists("hidden")) {
			getNpc().setSkipCombatTurn(true);
			return;
		}

		int tempStage = (int) Math.abs(Math.ceil(getNpc().getHP() / (double) 50 - (double) 10));

		if (tempStage != stage) {
			player.getCombat().resetPlayerAttack();
			getNpc().setSkipCombatTurn(true);
			changeStatus(tempStage);
			return;
		}

		if (type.equals(Type.MELEE)) {
			getNpc().attackType = 0;
			getNpc().projectileId = -1;
		}

		if (type.equals(Type.RANGE)) {
			getNpc().attackType = 1;
			getNpc().projectileId = 1044;
		}

		if (type.equals(Type.MAGIC)) {
			getNpc().attackType = 2;
			getNpc().projectileId = 1046;
		}

		if (type.equals(Type.JAD)) {
			getNpc().attackType = Misc.random(1, 2);
			if (getNpc().attackType == 2) {
				getNpc().projectileId = 1046;
			} else {
				getNpc().projectileId = 1047;
			}
		}

		getNpc().facePlayer(player.playerId);

		if (cloudsSent < 5 && stage == 0 || type.equals(Type.RANGE) && toxicClouds.size() < 5 && stage > 0 && Misc.random(3) == 0) {

			getNpc().setSkipCombatTurn(true);
			getNpc().attackType = 2;

			Location location = TOXIC_SNAKE_LOCATIONS[(int) (Math.random() * TOXIC_SNAKE_LOCATIONS.length - 1)];
			Optional<ToxicCloud> optionalCloud = toxicClouds.stream().filter(x -> x.getLocation().equals(location)).findAny();
			if (!optionalCloud.isPresent()) {

				ToxicCloud cloud = new ToxicCloud(location);
				toxicClouds.add(cloud);

				int npcX = getNpc().absX + NPCConfig.offset(getNpc());
				int npcY = getNpc().absY + NPCConfig.offset(getNpc());

				int offY = ((npcX - location.getX()) * -1);
				int offX = ((npcY - location.getY()) * -1);

				getNpc().startAnimation(5068);

				player.getPA().createPlayersProjectile(npcX, npcY, offX, offY, 50, 60, 1045, 75, 0, -1, 30);

				getNpc().turnNpc(location.getX(), location.getY());

				GameCycleTaskHandler.addEvent(getNpc(), container -> {
					cloud.setActive(true);

					// -1 off x and y because of the object offset
					GameObject gameObject = new GameObject(11700, location.getX() - 1, location.getY() - 1, player.heightLevel, 0, 10, -1, -1);

					GameCycleTaskHandler.addEvent(getNpc(), container1 -> {

						if (getNpc().attributeExists("instanced_area")) {
							((InstancesArea) getNpc().getAttribute("instanced_area")).getGameObjects().remove(gameObject);
						}

						toxicClouds.remove(cloud);
						Server.objectManager.removeObject(Location.create(gameObject.objectX, gameObject.objectY, gameObject.height), gameObject.type, true);
						container1.stop();
					}, (30 * 1000) / 600);

					if (getNpc().attributeExists("instanced_area")) {
						((InstancesArea) getNpc().getAttribute("instanced_area")).getGameObjects().add(gameObject);
					}

					container.stop();
				}, 2);

				getNpc().attackTimer += 4;
				getNpc().setAttribute("block_face_player", true);
				GameCycleTaskHandler.addEvent(getNpc(), container -> {
					getNpc().removeAttribute("block_face_player");
				}, 4);


			}
			cloudsSent++;

			return;
		}

		if ((type.equals(Type.RANGE) || type.equals(Type.MAGIC)) && snakelings.size() < 3 && Misc.random(3) == 0) {

			getNpc().setSkipCombatTurn(true);
			getNpc().attackType = 2;

			Location location = TOXIC_SNAKE_LOCATIONS[(int) (Math.random() * TOXIC_SNAKE_LOCATIONS.length - 1)];

			int npcX = getNpc().absX + NPCConfig.offset(getNpc());
			int npcY = getNpc().absY + NPCConfig.offset(getNpc());

			int offY = ((npcX - location.getX()) * -1);
			int offX = ((npcY - location.getY()) * -1);

			getNpc().startAnimation(5068);

			player.getPA().createPlayersProjectile(npcX, npcY, offX, offY, 50, 60, 1047, 75, 0, -1, 30);

			getNpc().turnNpc(location.getX(), location.getY());

			GameCycleTaskHandler.addEvent(getNpc(), container -> {
				// spawn zee snake

				NPC npc = NPCHandler.spawnNpc2(2045, location.getX(), location.getY(), player.getId() * 4, 1, 1, 1, 300, 1);

				npc.setAttribute("zulrah", getNpc());
				npc.setRespawn(false);
				npc.underAttack = true;
				npc.underAttackBy = player.getId();

				snakelings.add(npc);


				if (getNpc().attributeExists("instanced_area")) {
					InstancesArea instancesArea = (InstancesArea) getNpc().getAttribute("instanced_area");
					instancesArea.getNpcs().add(npc);

					((InstancesArea) getNpc().getAttribute("instanced_area")).getNpcs().add(npc);
				}

				container.stop();
			}, 2);

			getNpc().attackTimer += 4;
			getNpc().setAttribute("block_face_player", true);
			GameCycleTaskHandler.addEvent(getNpc(), container -> {
				getNpc().removeAttribute("block_face_player");
			}, 4);
		}

	}

	public void changeStatus(int stage) {
		this.stage = stage;
		getNpc().startAnimation(5072);

		GameCycleTaskHandler.addEvent(getNpc(), container -> {

			getNpc().setAttribute("hidden", true);

			Location location = rotations[stage].getLocation();
			type = rotations[stage].getType();

			int npc = 2042;
			if (type.equals(Type.MELEE)) {
				npc = 2043;
			} else if (type.equals(Type.MAGIC)) {
				npc = 2044;
			}

			if (getNpc().getType() != npc) {
				getNpc().type = npc;
			}

			getNpc().absX = location.getX();
			getNpc().absY = location.getY();

			GameCycleTaskHandler.addEvent(getNpc(), container1 -> {
				getNpc().removeAttribute("hidden");
				getNpc().startAnimation(5073);
				container1.stop();
			}, 2);


			container.stop();
		}, 2);
	}

	@Override
	public void onDeath() {
		super.onDeath();

		snakelings.clear();
		toxicClouds.clear();

		if (getNpc().attributeExists("instanced_area")) {
			InstancesArea area = (InstancesArea) getNpc().getAttribute("instanced_area");

			for (NPC npc : area.getNpcs()) {
				if (npc == null || npc.equals(getNpc())) {
					continue;
				}

				if (!npc.isDead) {
					npc.isDead = true;
				}

				npc.setAttribute("dont_drop", true);
				npc.setRespawn(false);
			}

			for (GameObject gameObject : area.getGameObjects()) {

				if (gameObject == null) {
					continue;
				}

				Server.objectManager.removeObject(Location.create(gameObject.objectX, gameObject.objectY, gameObject.height), gameObject.type, true);
			}
		}

	}

	@Override
	public int damageDealtByPlayer(Player player, int damage) {
		return super.damageDealtByPlayer(player, damage);
	}

	@Override
	public int getDamage() {
		return MAX_HIT;
	}

	@Override
	public int getDistanceRequired() {
		return super.getDistanceRequired();
	}

	@Override
	public int getAttackDelay() {
		return super.getAttackDelay();
	}

	@Override
	public int getAttackAnimation() {
		if (type.equals(Type.RANGE)) {
			return 5069;
		}


		if (type.equals(Type.MAGIC)) {
			return 5068;
		}

		return 5807;
	}

	@Override
	public boolean overrideProtectionPrayers() {
		return false;
	}

	@Override
	public void process() {
		super.process();

		if (!getNpc().attributeExists("instanced_area")) {
			return;
		}

		InstancesArea instancesArea = (InstancesArea) getNpc().getAttribute("instanced_area");
		Player player = instancesArea.getPlayer();

		for (ToxicCloud cloud : toxicClouds) {
			if (cloud.getLocation().equals(player.getX(), player.getY())) {
				int damage = Misc.random(1, 8);

				if (damage > player.level[PlayerConstants.HITPOINTS]) {
					damage = player.level[PlayerConstants.HITPOINTS];
				}

				player.getPA().appendPoison(damage);

				if (!player.getHitUpdateRequired()) {
					player.setHitUpdateRequired(true);
					player.setHitDiff(damage);
					player.updateRequired = true;
					player.poisonMask = 1;
					player.hitIcon = 0;
				} else if (!player.getHitUpdateRequired2()) {
					player.setHitUpdateRequired2(true);
					player.setHitDiff2(damage);
					player.updateRequired = true;
					player.poisonMask = 2;
					player.hitIcon = 0;
				}

				player.dealDamage(damage);
				player.getPA().refreshSkill(PlayerConstants.HITPOINTS);
			}
		}
	}

	public void removeSnake(NPC snake) {
		InstancesArea instancesArea = (InstancesArea) getNpc().getAttribute("instanced_area");
		instancesArea.getNpcs().remove(snake);

		snakelings.remove(snake);
	}

}