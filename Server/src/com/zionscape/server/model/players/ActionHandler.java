package com.zionscape.server.model.players;

import com.zionscape.server.Config;
import com.zionscape.server.Server;
import com.zionscape.server.ServerEvents;
import com.zionscape.server.cache.Collision;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.Tanner;
import com.zionscape.server.model.content.grandexchange.GrandExchange;
import com.zionscape.server.model.content.minigames.Barrows;
import com.zionscape.server.model.content.minigames.DuelArena;
import com.zionscape.server.model.content.minigames.GodWarsDungeon;
import com.zionscape.server.model.content.minigames.PestControl;
import com.zionscape.server.model.content.minigames.christmas.BaubleMaking;
import com.zionscape.server.model.content.minigames.christmas.ChristmasEvent;
import com.zionscape.server.model.content.minigames.christmas.MarionetteMaking;
import com.zionscape.server.model.content.treasuretrails.TreasureTrails;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.objects.GameObject;
import com.zionscape.server.model.players.skills.Fishing;
import com.zionscape.server.model.players.skills.agility.Agility;
import com.zionscape.server.model.players.skills.agility.AgilityHandler;
import com.zionscape.server.model.players.skills.summoning.Summoning;
import com.zionscape.server.scripting.Scripting;
import com.zionscape.server.scripting.messages.impl.*;
import com.zionscape.server.util.Misc;
import com.zionscape.server.world.LadderHandler;
import com.zionscape.server.world.doors.DoorHandler;
import com.zionscape.server.world.shops.Shops;

public class ActionHandler {

	private Player player;

	public ActionHandler(Player Player) {
		this.player = Player;
	}

	public void wildyditch() {
		if (player.absY <= player.objectY) {
			player.startAnimation(6132);
			player.getPA().walkTo(0, +3);
		} else if (player.objectY < player.absY) {
			player.startAnimation(6132);
			player.getPA().walkTo(0, -3);
		}
	}

	@SuppressWarnings("static-access")
	public void firstClickObject(int objectType, int obX, int obY) {
		player.clickObjectType = 0;
		Location objectLocation = Location.create(obX, obY, player.heightLevel);

		if (ServerEvents.onObjectClick(player, objectType, objectLocation, 1)) {
			return;
		}

		if (Scripting.handleMessage(player, new FirstObjectActionMessage(objectType, objectLocation))) {
			return;
		}
		if (DoorHandler.handleDoor(player, objectType, objectLocation))
			return;
		if (GodWarsDungeon.isObject(player, objectType, obX, obY))
			return;// test
		if (TreasureTrails.clickingObject(player, objectType, objectLocation))
			return;
		if (DuelArena.clickingObject(player, objectType))
			return;
		if (LadderHandler.onFirstClickObject(player, objectType, objectLocation))
			return;
		if (PestControl.onObjectClicked(player, objectType, objectLocation)) {
			return;
		}

		AgilityHandler.completeObstacle(player, objectType);

		if (objectType == 1738 && obX == 2839 && obY == 3537) {
			player.getPA().movePlayer(player.absX, player.absY, player.heightLevel + 2);
		}
		if (objectType == 15638 && obX == 2840 && obY == 3538) {
			player.getPA().movePlayer(player.absX, player.absY, player.heightLevel - 2);
		}
		if (Agility.handleObjectClick(player, objectType, obX, obY)) {
			return;
		}

		player.getRunecrafting().altars(objectType);

		switch (objectType) {
		case 190:
			if (player.getY() <= 3382) {
				player.getPA().movePlayer(2460, 3385, 0);
			} else {
				player.getPA().movePlayer(2461, 3382, 0);
			}
			break;
		case 35542:
		case 35544:
		case 35543:
		case 35400: // shantay pass
			if (player.getAbsY() >= 3117) {
				player.getPA().movePlayer(3304, 3115, 0);
			} else {
				player.getPA().movePlayer(3304, 3117, 0);
			}
			break;

		case 28714: // summoning ladder tele
			player.getPA().startTeleport(Location.create(Config.START_LOCATION_X, Config.START_LOCATION_Y, 0));
			break;

		case 51: // gruber woods fence
			if (player.absX >= 2662) {
				player.getPA().movePlayer(player.absX - 1, player.absY);
			} else {
				player.getPA().movePlayer(player.absX + 1, player.absY);
			}
			break;

		case 32048:
			player.getPA().movePlayer(3045, 3927);
			break;

		case 1738:
			if (objectLocation.equals(3994, 4022, 0)) { // donor zone stairs

				if (!player.getData().respectedMember) {
					player.sendMessage("You must be a Respected donator to go up there!");
					return;
				}

				player.getPA().movePlayer(3996, 4022, 1);
			}
			if (objectLocation.equals(3994, 4026, 1)) { // donor zone stairs

				if (!player.getData().respectedMember) {
					player.sendMessage("You must be a Respected donator to go up there!");
					return;
				}

				player.getPA().movePlayer(3996, 4026, 2);
			}
			break;
		case 1740:
			if (objectLocation.equals(3995, 4022, 1)) { // donor zone stairs

				if (!player.getData().respectedMember) {
					player.sendMessage("You must be a Respected donator to go up there!");
					return;
				}

				player.getPA().movePlayer(3996, 4022, 0);
			}
			if (objectLocation.equals(3995, 4026, 2)) { // donor zone stairs

				if (!player.getData().respectedMember) {
					player.sendMessage("You must be a Respected donator to go up there!");
					return;
				}

				player.getPA().movePlayer(3996, 4026, 1);
			}
			break;
		case 18342: // rev south cave exits
			player.getPA().movePlayer(Location.create(3182, 3656, 0));
			break;

		case 3735:
			if (objectLocation.equals(3206, 3762, 0)) {// rev cave north entrance
				// player.getPA().movePlayer(Location.create(3037, 10171, 0));
			}
			if (objectLocation.equals(3181, 3657, 0)) { // rev cave south entrance
				// player.getPA().movePlayer(Location.create(3077, 10058, 0));
			}
			break;
		case 18341: // rev cave north exit
			player.getPA().movePlayer(Location.create(3206, 3760, 0));
			break;
		case 33173:
			if (player.getY() == 9562) {
				player.getPA().movePlayer(3056, 9555);
			}
			break;
		case 33174:
			if (player.getY() == 9555) {
				player.getPA().movePlayer(3056, 9562);
			}
			break;
		case 47233:
			if (player.getY() == 5294) {
				player.getPA().movePlayer(1633, 5292);
			}
			if (player.getY() == 5292) {
				player.getPA().movePlayer(1633, 5294);
			}
			break;
		case 47237:
			if (player.objectX == 1641 && player.objectY == 5267) {
				player.getPA().movePlayer(1641, 5260, 0);
			}
			if (player.objectX == 1641 && player.objectY == 5261) {
				player.getPA().movePlayer(1641, 5268, 0);
			}
			break;
		case 47236:
			if (player.objectX == 1625 && player.objectY == 5302) {
				if (player.getX() == 1626) {
					player.getPA().walkTo(-1, 0);
				} else if (player.getX() == 1625) {
					player.getPA().walkTo(1, 0);
				}
			}

			if (player.objectX == 1605 && player.objectY == 5265) {
				if (player.getY() == 5264) {
					player.getPA().walkTo(0, 1);
				} else if (player.getY() == 5265) {
					player.getPA().walkTo(0, -1);
				}
			}
			if (player.objectX == 1610 && player.objectY == 5289) {
				if (player.getY() == 5288) {
					player.getPA().walkTo(0, 1);
				} else if (player.getY() == 5289) {
					player.getPA().walkTo(0, -1);
				}
			}
			if (player.objectX == 1634 && player.objectY == 5253) {
				if (player.getX() == 1635) {
					player.getPA().walkTo(-1, 0);
				} else if (player.getX() == 1634) {
					player.getPA().walkTo(1, 0);
				}
			}
			if (player.objectX == 1651 && player.objectY == 5281) {
				if (player.getY() == 5280) {
					player.getPA().walkTo(0, 1);
				} else if (player.getY() == 5281) {
					player.getPA().walkTo(0, -1);
				}
			}
			if (player.objectX == 1649 && player.objectY == 5302) {
				if (player.getX() == 1650) {
					player.getPA().walkTo(-1, 0);
				} else if (player.getX() == 1649) {
					player.getPA().walkTo(1, 0);
				}
			}
			break;
		case 31412:
			if (player.objectX == 3340 && player.objectY == 9426) {
				player.getPA().movePlayer(3276, 4368, 0);
				break;
			}
			if (player.objectX == 3374 && player.objectY == 9426) {
				player.getPA().movePlayer(3321, 4365, 0);
				break;
			}
			if (player.objectX == 3377 && player.objectY == 9367) {
				player.getPA().movePlayer(3322, 4340, 0);
				break;
			}
			if (player.objectX == 3338 && player.objectY == 9368) {
				player.getPA().movePlayer(3270, 4340, 0);
				break;
			}
			break;
		case 31417:
			if (player.objectX == 3277 && player.objectY == 4367) {
				player.getPA().movePlayer(3344, 9427, 0);
				break;
			}
			if (player.objectX == 3317 && player.objectY == 4364) {
				player.getPA().movePlayer(3373, 9427, 0);
				break;
			}
			if (player.objectX == 3271 && player.objectY == 4339) {
				player.getPA().movePlayer(3342, 9368, 0);
				break;
			}
			if (player.objectX == 3318 && player.objectY == 4339) {
				player.getPA().movePlayer(3376, 9368, 0);
				break;
			}
			break;
		case 31435:

			if (player.objectX == 3368 && player.objectY == 9420) {
				player.getPA().walkTo(player.getAbsX() <= 3367 ? +2 : 2, 0);
				break;
			}

			if (player.objectX == 3349 && player.objectY == 9420) {
				player.getPA().walkTo(player.getAbsX() >= 3350 ? -2 : 2, 0);
				break;
			}

			if (player.objectX == 3347 && player.objectY == 9374) {
				player.getPA().walkTo(player.getAbsX() >= 3348 ? -2 : 2, 0);
				break;
			}
			if (player.objectX == 3370 && player.objectY == 9374) {
				player.getPA().walkTo(player.getAbsX() <= 3369 ? 2 : -2, 0);
				break;
			}
			break;
		case 31436:
			player.getPA().walkTo(0, player.getAbsY() <= 4333 ? 2 : -2);
			break;

		// Chaos tunnels
		case 28779:
			if (player.objectX == 3254 && player.objectY == 5451) {
				player.getPA().movePlayer(3250, 5448, 0);
			}
			if (player.objectX == 3250 && player.objectY == 5448) {
				player.getPA().movePlayer(3254, 5451, 0);
			}
			if (player.objectX == 3241 && player.objectY == 5445) {
				player.getPA().movePlayer(3233, 5445, 0);
			}
			if (player.objectX == 3233 && player.objectY == 5445) {
				player.getPA().movePlayer(3241, 5445, 0);
			}
			if (player.objectX == 3259 && player.objectY == 5446) {
				player.getPA().movePlayer(3265, 5491, 0);
			}
			if (player.objectX == 3265 && player.objectY == 5491) {
				player.getPA().movePlayer(3259, 5446, 0);
			}
			if (player.objectX == 3260 && player.objectY == 5491) {
				player.getPA().movePlayer(3266, 5446, 0);
			}
			if (player.objectX == 3266 && player.objectY == 5446) {
				player.getPA().movePlayer(3260, 5491, 0);
			}
			if (player.objectX == 3241 && player.objectY == 5469) {
				player.getPA().movePlayer(3233, 5470, 0);
			}
			if (player.objectX == 3233 && player.objectY == 5470) {
				player.getPA().movePlayer(3241, 5469, 0);
			}
			if (player.objectX == 3235 && player.objectY == 5457) {
				player.getPA().movePlayer(3229, 5454, 0);
			}
			if (player.objectX == 3229 && player.objectY == 5454) {
				player.getPA().movePlayer(3235, 5457, 0);
			}
			if (player.objectX == 3280 && player.objectY == 5460) {
				player.getPA().movePlayer(3273, 5460, 0);
			}
			if (player.objectX == 3273 && player.objectY == 5460) {
				player.getPA().movePlayer(3280, 5460, 0);
			}
			if (player.objectX == 3283 && player.objectY == 5448) {
				player.getPA().movePlayer(3287, 5448, 0);
			}
			if (player.objectX == 3287 && player.objectY == 5448) {
				player.getPA().movePlayer(3283, 5448, 0);
			}
			if (player.objectX == 3244 && player.objectY == 5495) {
				player.getPA().movePlayer(3239, 5498, 0);
			}
			if (player.objectX == 3239 && player.objectY == 5498) {
				player.getPA().movePlayer(3244, 5495, 0);
			}
			if (player.objectX == 3232 && player.objectY == 5501) {
				player.getPA().movePlayer(3238, 5507, 0);
			}
			if (player.objectX == 3238 && player.objectY == 5507) {
				player.getPA().movePlayer(3232, 5501, 0);
			}
			if (player.objectX == 3218 && player.objectY == 5497) {
				player.getPA().movePlayer(3222, 5488, 0);
			}
			if (player.objectX == 3222 && player.objectY == 5488) {
				player.getPA().movePlayer(3218, 5497, 0);
			}
			if (player.objectX == 3218 && player.objectY == 5478) {
				player.getPA().movePlayer(3215, 5475, 0);
			}
			if (player.objectX == 3215 && player.objectY == 5475) {
				player.getPA().movePlayer(3218, 5478, 0);
			}
			if (player.objectX == 3224 && player.objectY == 5479) {
				player.getPA().movePlayer(3222, 5474, 0);
			}
			if (player.objectX == 3222 && player.objectY == 5474) {
				player.getPA().movePlayer(3224, 5479, 0);
			}
			if (player.objectX == 3208 && player.objectY == 5471) {
				player.getPA().movePlayer(3210, 5477, 0);
			}
			if (player.objectX == 3210 && player.objectY == 5477) {
				player.getPA().movePlayer(3208, 5471, 0);
			}
			if (player.objectX == 3214 && player.objectY == 5456) {
				player.getPA().movePlayer(3212, 5452, 0);
			}
			if (player.objectX == 3212 && player.objectY == 5452) {
				player.getPA().movePlayer(3214, 5456, 0);
			}
			if (player.objectX == 3204 && player.objectY == 5445) {
				player.getPA().movePlayer(3197, 5448, 0);
			}
			if (player.objectX == 3197 && player.objectY == 5448) {
				player.getPA().movePlayer(3204, 5445, 0);
			}
			if (player.objectX == 3189 && player.objectY == 5444) {
				player.getPA().movePlayer(3187, 5460, 0);
			}
			if (player.objectX == 3187 && player.objectY == 5460) {
				player.getPA().movePlayer(3189, 5444, 0);
			}
			if (player.objectX == 3192 && player.objectY == 5472) {
				player.getPA().movePlayer(3186, 5472, 0);
			}
			if (player.objectX == 3186 && player.objectY == 5472) {
				player.getPA().movePlayer(3192, 5472, 0);
			}
			if (player.objectX == 3185 && player.objectY == 5478) {
				player.getPA().movePlayer(3191, 5482, 0);
			}
			if (player.objectX == 3191 && player.objectY == 5482) {
				player.getPA().movePlayer(3185, 5478, 0);
			}
			if (player.objectX == 3171 && player.objectY == 5473) {
				player.getPA().movePlayer(3167, 5471, 0);
			}
			if (player.objectX == 3167 && player.objectY == 5471) {
				player.getPA().movePlayer(3171, 5473, 0);
			}
			if (player.objectX == 3171 && player.objectY == 5478) {
				player.getPA().movePlayer(3167, 5478, 0);
			}
			if (player.objectX == 3167 && player.objectY == 5478) {
				player.getPA().movePlayer(3171, 5478, 0);
			}
			if (player.objectX == 3168 && player.objectY == 5456) {
				player.getPA().movePlayer(3178, 5460, 0);
			}
			if (player.objectX == 3178 && player.objectY == 5460) {
				player.getPA().movePlayer(3168, 5456, 0);
			}
			if (player.objectX == 3191 && player.objectY == 5495) {
				player.getPA().movePlayer(3194, 5490, 0);
			}
			if (player.objectX == 3194 && player.objectY == 5490) {
				player.getPA().movePlayer(3191, 5495, 0);
			}
			if (player.objectX == 3141 && player.objectY == 5480) {
				player.getPA().movePlayer(3142, 5489, 0);
			}
			if (player.objectX == 3142 && player.objectY == 5489) {
				player.getPA().movePlayer(3141, 5480, 0);
			}
			if (player.objectX == 3142 && player.objectY == 5462) {
				player.getPA().movePlayer(3154, 5462, 0);
			}
			if (player.objectX == 3154 && player.objectY == 5462) {
				player.getPA().movePlayer(3142, 5462, 0);
			}
			if (player.objectX == 3143 && player.objectY == 5443) {
				player.getPA().movePlayer(3155, 5449, 0);
			}
			if (player.objectX == 3155 && player.objectY == 5449) {
				player.getPA().movePlayer(3143, 5443, 0);
			}
			if (player.objectX == 3307 && player.objectY == 5496) {
				player.getPA().movePlayer(3317, 5496, 0);
			}
			if (player.objectX == 3317 && player.objectY == 5496) {
				player.getPA().movePlayer(3307, 5496, 0);
			}
			if (player.objectX == 3318 && player.objectY == 5481) {
				player.getPA().movePlayer(3322, 5480, 0);
			}
			if (player.objectX == 3322 && player.objectY == 5480) {
				player.getPA().movePlayer(3318, 5481, 0);
			}
			if (player.objectX == 3299 && player.objectY == 5484) {
				player.getPA().movePlayer(3303, 5477, 0);
			}
			if (player.objectX == 3303 && player.objectY == 5477) {
				player.getPA().movePlayer(3299, 5484, 0);
			}
			if (player.objectX == 3286 && player.objectY == 5470) {
				player.getPA().movePlayer(3285, 5474, 0);
			}
			if (player.objectX == 3285 && player.objectY == 5474) {
				player.getPA().movePlayer(3286, 5470, 0);
			}
			if (player.objectX == 3290 && player.objectY == 5463) {
				player.getPA().movePlayer(3302, 5469, 0);
			}
			if (player.objectX == 3302 && player.objectY == 5469) {
				player.getPA().movePlayer(3290, 5463, 0);
			}
			if (player.objectX == 3296 && player.objectY == 5455) {
				player.getPA().movePlayer(3299, 5450, 0);
			}
			if (player.objectX == 3299 && player.objectY == 5450) {
				player.getPA().movePlayer(3296, 5455, 0);
			}
			if (player.objectX == 3280 && player.objectY == 5501) {
				player.getPA().movePlayer(3285, 5508, 0);
			}
			if (player.objectX == 3285 && player.objectY == 5508) {
				player.getPA().movePlayer(3280, 5501, 0);
			}
			if (player.objectX == 3300 && player.objectY == 5514) {
				player.getPA().movePlayer(3297, 5510, 0);
			}
			if (player.objectX == 3297 && player.objectY == 5510) {
				player.getPA().movePlayer(3300, 5514, 0);
			}
			if (player.objectX == 3289 && player.objectY == 5533) {
				player.getPA().movePlayer(3288, 5536, 0);
			}
			if (player.objectX == 3288 && player.objectY == 5536) {
				player.getPA().movePlayer(3289, 5533, 0);
			}
			if (player.objectX == 3285 && player.objectY == 5527) {
				player.getPA().movePlayer(3282, 5531, 0);
			}
			if (player.objectX == 3282 && player.objectY == 5531) {
				player.getPA().movePlayer(3285, 5527, 0);
			}
			if (player.objectX == 3325 && player.objectY == 5518) {
				player.getPA().movePlayer(3323, 5531, 0);
			}
			if (player.objectX == 3323 && player.objectY == 5531) {
				player.getPA().movePlayer(3325, 5518, 0);
			}
			if (player.objectX == 3299 && player.objectY == 5533) {
				player.getPA().movePlayer(3297, 5536, 0);
			}
			if (player.objectX == 3297 && player.objectY == 5536) {
				player.getPA().movePlayer(3299, 5533, 0);
			}
			if (player.objectX == 3321 && player.objectY == 5554) {
				player.getPA().movePlayer(3315, 5552, 0);
			}
			if (player.objectX == 3315 && player.objectY == 5552) {
				player.getPA().movePlayer(3321, 5554, 0);
			}
			if (player.objectX == 3291 && player.objectY == 5555) {
				player.getPA().movePlayer(3285, 5556, 0);
			}
			if (player.objectX == 3285 && player.objectY == 5556) {
				player.getPA().movePlayer(3291, 5555, 0);
			}
			if (player.objectX == 3266 && player.objectY == 5552) {
				player.getPA().movePlayer(3262, 5552, 0);
			}
			if (player.objectX == 3262 && player.objectY == 5552) {
				player.getPA().movePlayer(3266, 5552, 0);
			}
			if (player.objectX == 3256 && player.objectY == 5561) {
				player.getPA().movePlayer(3253, 5561, 0);
			}
			if (player.objectX == 3253 && player.objectY == 5561) {
				player.getPA().movePlayer(3256, 5561, 0);
			}
			if (player.objectX == 3249 && player.objectY == 5546) {
				player.getPA().movePlayer(3252, 5543, 0);
			}
			if (player.objectX == 3252 && player.objectY == 5543) {
				player.getPA().movePlayer(3249, 5546, 0);
			}
			if (player.objectX == 3261 && player.objectY == 5536) {
				player.getPA().movePlayer(3268, 5534, 0);
			}
			if (player.objectX == 3268 && player.objectY == 5534) {
				player.getPA().movePlayer(3261, 5536, 0);
			}
			if (player.objectX == 3243 && player.objectY == 5526) {
				player.getPA().movePlayer(3241, 5529, 0);
			}
			if (player.objectX == 3241 && player.objectY == 5529) {
				player.getPA().movePlayer(3243, 5526, 0);
			}
			if (player.objectX == 3230 && player.objectY == 5547) {
				player.getPA().movePlayer(3226, 5553, 0);
			}
			if (player.objectX == 3226 && player.objectY == 5553) {
				player.getPA().movePlayer(3230, 5547, 0);
			}
			if (player.objectX == 3206 && player.objectY == 5553) {
				player.getPA().movePlayer(3204, 5546, 0);
			}
			if (player.objectX == 3204 && player.objectY == 5546) {
				player.getPA().movePlayer(3206, 5553, 0);
			}
			if (player.objectX == 3211 && player.objectY == 5533) {
				player.getPA().movePlayer(3214, 5533, 0);
			}
			if (player.objectX == 3214 && player.objectY == 5533) {
				player.getPA().movePlayer(3211, 5533, 0);
			}
			if (player.objectX == 3208 && player.objectY == 5527) {
				player.getPA().movePlayer(3211, 5523, 0);
			}
			if (player.objectX == 3211 && player.objectY == 5523) {
				player.getPA().movePlayer(3208, 5527, 0);
			}
			if (player.objectX == 3201 && player.objectY == 5531) {
				player.getPA().movePlayer(3197, 5529, 0);
			}
			if (player.objectX == 3197 && player.objectY == 5529) {
				player.getPA().movePlayer(3201, 5531, 0);
			}
			if (player.objectX == 3202 && player.objectY == 5515) {
				player.getPA().movePlayer(3196, 5512, 0);
			}
			if (player.objectX == 3196 && player.objectY == 5512) {
				player.getPA().movePlayer(3202, 5515, 0);
			}
			if (player.objectX == 3190 && player.objectY == 5515) {
				player.getPA().movePlayer(3190, 5519, 0);
			}
			if (player.objectX == 3190 && player.objectY == 5519) {
				player.getPA().movePlayer(3190, 5515, 0);
			}
			if (player.objectX == 3185 && player.objectY == 5518) {
				player.getPA().movePlayer(3181, 5517, 0);
			}
			if (player.objectX == 3181 && player.objectY == 5517) {
				player.getPA().movePlayer(3185, 5518, 0);
			}
			if (player.objectX == 3187 && player.objectY == 5531) {
				player.getPA().movePlayer(3182, 5530, 0);
			}
			if (player.objectX == 3182 && player.objectY == 5530) {
				player.getPA().movePlayer(3187, 5531, 0);
			}
			if (player.objectX == 3169 && player.objectY == 5510) {
				player.getPA().movePlayer(3159, 5501, 0);
			}
			if (player.objectX == 3159 && player.objectY == 5501) {
				player.getPA().movePlayer(3169, 5510, 0);
			}
			if (player.objectX == 3165 && player.objectY == 5515) {
				player.getPA().movePlayer(3173, 5530, 0);
			}
			if (player.objectX == 3173 && player.objectY == 5530) {
				player.getPA().movePlayer(3165, 5515, 0);
			}
			if (player.objectX == 3156 && player.objectY == 5523) {
				player.getPA().movePlayer(3152, 5520, 0);
			}
			if (player.objectX == 3152 && player.objectY == 5520) {
				player.getPA().movePlayer(3156, 5523, 0);
			}
			if (player.objectX == 3148 && player.objectY == 5533) {
				player.getPA().movePlayer(3153, 5537, 0);
			}
			if (player.objectX == 3153 && player.objectY == 5537) {
				player.getPA().movePlayer(3148, 5533, 0);
			}
			if (player.objectX == 3143 && player.objectY == 5535) {
				player.getPA().movePlayer(3147, 5541, 0);
			}
			if (player.objectX == 3147 && player.objectY == 5541) {
				player.getPA().movePlayer(3143, 5535, 0);
			}
			if (player.objectX == 3168 && player.objectY == 5541) {
				player.getPA().movePlayer(3171, 5542, 0);
			}
			if (player.objectX == 3171 && player.objectY == 5542) {
				player.getPA().movePlayer(3168, 5541, 0);
			}
			if (player.objectX == 3190 && player.objectY == 5549) {
				player.getPA().movePlayer(3190, 5554, 0);
			}
			if (player.objectX == 3190 && player.objectY == 5554) {
				player.getPA().movePlayer(3190, 5549, 0);
			}
			if (player.objectX == 3180 && player.objectY == 5557) {
				player.getPA().movePlayer(3174, 5558, 0);
			}
			if (player.objectX == 3174 && player.objectY == 5558) {
				player.getPA().movePlayer(3180, 5557, 0);
			}
			if (player.objectX == 3162 && player.objectY == 5557) {
				player.getPA().movePlayer(3158, 5561, 0);
			}
			if (player.objectX == 3158 && player.objectY == 5561) {
				player.getPA().movePlayer(3162, 5557, 0);
			}
			if (player.objectX == 3166 && player.objectY == 5553) {
				player.getPA().movePlayer(3162, 5545, 0);
			}
			if (player.objectX == 3162 && player.objectY == 5545) {
				player.getPA().movePlayer(3166, 5553, 0);
			}
			if (player.objectX == 3142 && player.objectY == 5545) {
				// player.getPA().movePlayer(3115, 5528, 0);
			}
			break;
		// Borks portal
		case 29537:
			if (player.objectX == 3115 && player.objectY == 5528) {
				player.getPA().movePlayer(3142, 5545, 0);
			}
			break;
		// Chaos tunnel exits
		case 28782:
			if (player.objectX == 3183 && player.objectY == 5470) {
				player.getPA().movePlayer(3059, 3549, 0);
			}
			if (player.objectX == 3248 && player.objectY == 5490) {
				player.getPA().movePlayer(3120, 3571, 0);
			}
			if (player.objectX == 3292 && player.objectY == 5479) {
				player.getPA().movePlayer(3166, 3561, 0);
			}
			if (player.objectX == 3291 && player.objectY == 5538) {
				player.getPA().movePlayer(3166, 3618, 0);
			}
			if (player.objectX == 3234 && player.objectY == 5559) {
				player.getPA().movePlayer(3107, 3640, 0);
			}
			break;
		case 2296:
			if (player.absX == 2603 && player.absY == 3477) {
				player.getPA().walkTo(-5, 0);
			}
			if (player.absX == 2598 && player.absY == 3477) {
				player.getPA().walkTo(5, 0);
			}
			break;
		case 4615:
			player.getPA().walkTo(+2, 0);
			return;
		case 4616:
			player.getPA().walkTo(-2, 0);
			return;
		case 10687:
		case 10691:
		case 10695:
		case 10697:
		case 10689:
		case 10693:
		case 10688:
		case 10692:
		case 10696:
		case 10686:
		case 10690:
		case 10694:
			MarionetteMaking.handleObjectClicking(player, objectType);
			break;
		case 10679:
		case 10680:
		case 10681:
		case 10682:
		case 10683:
		case 10684:
		case 10685:
			BaubleMaking.handleBaubleSearching(player);
			break;
		case 10673:
		case 10674:
		case 10675:
			BaubleMaking.handleBaublePaintingInterface(player);
			break;
		case 10708:
		case 10707:
			if (player.christmasEvent < 2) {
				player.sendMessage("It's probably best I speak with the head pixie Rosie so I know what I'm doing!");
				return;
			}
		case 10804:
			if (player.getItems().freeInventorySlots() < 28 && player.christmasEvent == 0) {
				player.sendMessage("It's probably a good idea you don't bring anything in your inventory.");
				return;
			}
		case 492:
			player.getPA().movePlayer(2856, 9570, 0);
			break;
		case 1764:
			player.getPA().movePlayer(2857, 3170, 0);
			break;
		case 10699:
			ChristmasEvent.handleObjectClicking(player, objectType);
			break;
		case 10672:
			player.startAnimation(832);
			ChristmasEvent.grabMarionettes(player);
			break;
		case 5096:
			player.getPA().movePlayer(2649, 9591, 0);
			break;
		case 5094:
			player.getPA().movePlayer(2643, 9594, 2);
			break;
		case 16736:
			player.getPA().movePlayer(1764, 5365, 1);
			break;
		case 2354:
			player.getPA().startTeleport(2734, 5084, 0, "modern");
			break;
		case 25338:
			player.getPA().movePlayer(1772, 5366, 0);
			break;
		case 25336:
			player.getPA().movePlayer(1768, 5366, 1);
			break;
		case 25339:
			player.getPA().movePlayer(1778, 5343, 1);
			break;
		case 25340:
			player.getPA().movePlayer(1778, 5346, 0);
			break;
		case 8744:
			player.getPA().movePlayer(2141, 3944, 0);
			break;
		case 5100:
		case 5101:
		case 5102:
		case 5103:
		case 5104:
		case 5105:
		case 5106:
		case 5107:
		case 5108:
		case 5109:
			if (player.absX < player.objectX) {
				player.getPA().movePlayer(player.absX + 2, player.absY, player.heightLevel);
			}
			if (player.absX > player.objectX) {
				player.getPA().movePlayer(player.absX - 2, player.absY, player.heightLevel);
			}
			if (player.absY < player.objectY) {
				player.getPA().movePlayer(player.absX, player.absY + 2, player.heightLevel);
			}
			if (player.absY > player.objectY) {
				player.getPA().movePlayer(player.absX, player.absY - 2, player.heightLevel);
			}
			break; // this was why lmao

		case 23271:
			wildyditch();
			break;

		case 5097:
			player.getPA().movePlayer(2636, 9510, 2);
			break;
		case 5098:
			player.getPA().movePlayer(2636, 9517, 0);
			break;
		case 5088:
			player.getPA().movePlayer(2687, 9506, player.heightLevel);
			break;
		case 5090:
			player.getPA().movePlayer(2682, 9506, player.heightLevel);
			break;
		case 5110:
			player.getPA().movePlayer(2647, 9557, 0);
			break;
		case 5111:
			player.getPA().movePlayer(2649, 9562, 0);
			break;
		case 30141:
			if (player.canLeaveDeath) {
				player.getPA().movePlayer(1663, 5692, 0);
				player.canLeaveDeath = false;
			} else {
				player.sendMessage("You need to wait 10 seconds before you can leave the death chamber.");
			}
			break;

		/*
		 * case 4247: player.sendMessage("type: " + player.clickObjectType +
		 * " objectId: " + player.objectId + "."); player.sendMessage("yo."); break;
		 */
		case 27663:
		case 21301:
		case 26972:
		case 25808:
		case 11402:
		case 35647:
		case 34752:

			player.getPA().openUpBank();
			break;
		case 21512:// netiznot ladder up
			player.getPA().movePlayer(2364, 3799, 2);
			break;
		case 21513:// netiznot ladder down
			player.getPA().movePlayer(2362, 3799, 0);
			break;
		case 21396:// across netiznot ladder down
			player.getPA().movePlayer(2372, 3800, 0);
			break;
		case 21395:// across netiznot ladder up
			player.getPA().movePlayer(2372, 3800, 2);
			break;
		case 21306:// neitiznot bridges
			player.getPA().movePlayer(2317, 3832, 0);
			break;
		case 21307:// neitiznot bridges
			player.getPA().movePlayer(2317, 3823, 0);
			break;
		case 21310:// neitiznot bridges
			player.getPA().movePlayer(2314, 3848, 0);
			break;
		case 21311:// neitiznot bridges
			player.getPA().movePlayer(2314, 3839, 0);
			break;
		case 21312:// neitiznot bridges
			player.getPA().movePlayer(2355, 3848, 0);
			break;
		case 21313:// neitiznot bridges
			player.getPA().movePlayer(2355, 3839, 0);
			break;
		case 21309:// neitiznot bridges
			player.getPA().movePlayer(2343, 3820, 0);
			break;
		case 21308:// neitiznot bridges
			player.getPA().movePlayer(2343, 3829, 0);
			break;
		case 9293:// bridge requires agility to rune ore
			/*
			 * if (player.absX < player.objectX) {
			 * 
			 * player.getPA().movePlayer(2892, 9799, 0); } else {
			 * player.getPA().movePlayer(2886, 9799, 0); }
			 */
			if (player.level[16] < 55) {
				player.sendMessage("You need an agility level of 55 to squeeze through this tunnel");
			} else {
				player.getPA().movePlayer(2892, 9799, 0);
			}
			break;
		case 21315:// across agility bridge from ore
			player.getPA().movePlayer(2378, 3839, 0);
			break;
		case 32015:
			player.getPA().movePlayer(2546, 3551, 0);
			break;
		case 37995:
			player.getPA().movePlayer(3670, 3497, 0);
			break;
		case 30224:
			player.currentClass = 0;
			player.getPA().sendFrame126("Current Class: None", 49107);
			player.getPA().sendFrame126("Class Level: 0", 49108);
			player.getPA().sendFrame126("Level Potential: " + player.lvlPotential + "%", 49109);
			player.getPA().showInterface(49100);
			break;
		case 13641:
			player.getDH().sendDialogues(131, 0);
			break;
		case 4031:
			player.getPA().movePlayer(3304, 3115, 0);
			// player.sendMessage("go pass");
			break;
		case 6282:

			if (player.getItems().isWearingItems() || player.getItems().hasInventoryItems()) {
				player.sendMessage("You may not enter this portal with items equipped or in your inventory.");
				return;
			}

			player.getPA().movePlayer(2464, 4782, 0);
			break;
		case 5227:
			player.getPA().movePlayer(player.absX <= 3670 ? 3671 : 3670, 3497, player.heightLevel);
			break;
		case 2273: // sea troll
			// player.getDH().sendDialogues(200, player.type);
			player.sendMessage("Sea Troll Queen lies north in the water... Beware!");
			player.getPA().spellTeleport(2526, 4579, 0);
			break;
		case 2492:
			if (player.killCount >= 20 || player.isMember > 0) {
				player.getDH().sendOption4("Armadyl", "Bandos", "Saradomin", "Zamorak");
				player.dialogueAction = 20;
			}
			if (player.killCount < 20) {
				player.sendMessage("You need to have a killcount of 20 before entering the Boss Chamber!");
			}
			break;
		case 12230:
			player.getPA().movePlayer(3094 + Misc.random(2), 3471 + Misc.random(2), 0);
			break;
		/*
		 * case ManHunt.PORTAL_TO_LOBBY: if(player.clanId > -1) {
		 * if(Server.clanChat.isOwner(player)) {
		 * player.sendMessage("You invited your clanmates to a war");
		 * player.stopMovement();
		 * player.getPA().clanQuery("Would you like to enter clan wars? [safe Minigame]"
		 * , "No thank you"); } else {
		 * player.sendMessage("You aren't the owner of the clan!"); player.clanAction =
		 * -1;
		 * 
		 * } } else { player.sendMessage("You must be in a clan!"); player.clanAction =
		 * -1; }
		 * 
		 * break;
		 */
		case 11356:
			player.getPA().movePlayer(3094 + Misc.random(2), 3471 + Misc.random(2), 0);
			break;
		case 411:
			if (player.objectX == 3239 && player.objectY == 3608) {
				if (System.currentTimeMillis() - player.lastPray > 30000) {
					if (player.level[5] < player.getPA().getLevelForXP(player.xp[5])) {
						player.startAnimation(645);
						player.level[5] = player.getPA().getLevelForXP(player.xp[5]);
						player.sendMessage("You recharge your prayer points.");
						player.lastPray = System.currentTimeMillis();
						player.getPA().refreshSkill(5);
					} else {
						player.sendMessage("You already have full prayer points.");
					}
				} else {
					player.sendMessage("You need to wait 30 seconds before recharging prayer again!");
				}
			} else if (player.playerPrayerBook == 0) {
				player.setSidebarInterface(5, 22500);
				player.startAnimation(12563);
				player.sendMessage("You feel the power of Zaros as curses flit through your mind");
				player.playerPrayerBook = 1;
				player.getCombat().resetAllPrayers();
				switch (Misc.random(4) + 1) {
				case 1:
					player.forcedChat("Split my soul Zaros!");
					break;
				case 2:
					player.forcedChat("I feel your wrath...");
					break;
				case 3:
					player.forcedChat("AWGHHHHHHHH!");
					break;
				case 4:
					player.forcedChat("I shall use this to steal my enemies' powers!");
					break;
				}
			} else {
				player.setSidebarInterface(5, 5608);
				player.startAnimation(12563);
				player.sendMessage("You feel drained as prayers enter your mind again");
				player.playerPrayerBook = 0;
				player.getCombat().resetAllPrayers();
				switch (Misc.random(4) + 1) {
				case 1:
					player.forcedChat("Praise Saradomin!");
					break;
				case 2:
					player.forcedChat("I feel the power, and shall boost myself!");
					break;
				case 3:
					player.forcedChat("Wow these prayers are boring");
					break;
				case 4:
					player.forcedChat("I feel so powerless now...:'(");
					break;
				}
			}
			break;
		case 1765:
			player.getPA().movePlayer(2271, 4680, 0);
			break;
		case 2882:
		case 2883:
			if (player.objectX == 3268) {
				if (player.absX < player.objectX) {
					player.getPA().walkTo(1, 0);
				} else {
					player.getPA().walkTo(-1, 0);
				}
			}
			break;
		case 82:
			player.sendMessage("under construction.");
			break;
		case 272:
			player.getPA().movePlayer(player.absX, player.absY, 1);
			break;
		case 273:
			player.getPA().movePlayer(player.absX, player.absY, 0);
			break;
		case 246:
			player.getPA().movePlayer(player.absX, player.absY - 2, 1);
			break;
		case 1766:
			player.getPA().movePlayer(3016, 3849, 0);
			break;
		case 6552:
			if (System.currentTimeMillis() - player.lastSwitch > 30000) {
				player.getDH().sendOption4("Lunar Magic", "Ancient Magic", "Modern Magic", "");
				player.switchAction = 1;
				player.teleAction = 50;
				// player.lastSwitch = System.currentTimeMillis();
			} else {
				player.sendMessage("You have to wait 30 seconds before using the altar.");
			}
			player.getItems().sendWeapon(player.equipment[player.playerWeapon],
					player.getItems().getItemName(player.equipment[player.playerWeapon]));
			break;
		case 1816:
			player.getPA().startTeleport(2271, 4680, 0);
			break;
		case 1817:
			player.getPA().startTeleport(3067, 10253, 0, "modern");
			break;
		case 1814:
			// ardy lever
			player.getPA().startTeleport(3153, 3923, 0, "modern");
			break;
		case 1733:
			player.getPA().movePlayer(player.absX, player.absY + 6393, 0);
			break;
		case 1734:
			player.getPA().movePlayer(player.absX, player.absY - 6396, 0);
			break;
		case 2646:
			player.getItems().addItem(1779, 1);
			player.startAnimation(827);
			break;
		case 8959: // dag door
			if (player.getX() == 2490 && (player.getY() == 10146 || player.getY() == 10148)) {
				if (player.getPA().checkForPlayer(2490, player.getY() == 10146 ? 10148 : 10146)) {
					new GameObject(6951, player.objectX, player.objectY, player.heightLevel, 1, 10, 8959, 15);
				}
			}
			break;
		case 2213:
		case 14367:
		case 11758:
		case 3193:
			player.getPA().openUpBank();
			break;
		case 2996:
			if (player.getItems().playerHasItem(989, 1) && player.getItems().freeInventorySlots() >= 3) {
				player.getItems().addItem(player.getPA().randomCrystal(), Misc.random(150) + 1);
				player.getItems().deleteItem(989, 1);
				player.getDH().sendDialogues(38, 945);
				// }
				if (Misc.random(2) == 1) {
					player.getItems().addItem(player.getPA().randomCrystal(), 1);
				}
			} else {
				player.getDH().sendDialogues(37, 945);
			}
			break;
		case 10177:
			player.getPA().movePlayer(1890, 4407, 0);
			break;
		case 27668:
			player.getPA().movePlayer(Config.RESPAWN_X, Config.RESPAWN_Y, 0);
			break;
		case 10230:
			player.getPA().movePlayer(2900, 4449, 0);
			break;
		case 10229:
			player.getPA().movePlayer(1912, 4367, 0);
			break;
		case 2623:
			if (player.absX >= player.objectX) {
				player.getPA().walkTo(-1, 0);
			} else {
				player.getPA().walkTo(1, 0);
			}
			break;
		// pc boat
		case 14315:
			if (player.combatLevel < 30) {
				player.sendMessage("You must be at least 30 to enter this boat.");
				return;
			}
			player.getPA().movePlayer(2661, 2639, 0);
			break;
		case 14314:
			player.getPA().movePlayer(2657, 2639, 0);
			break;
		case 14235:
		case 14233:
			if (player.objectX == 2670) {
				if (player.absX <= 2670) {
					player.absX = 2671;
				} else {
					player.absX = 2670;
				}
			}
			if (player.objectX == 2643) {
				if (player.absX >= 2643) {
					player.absX = 2642;
				} else {
					player.absX = 2643;
				}
			}
			if ((player.objectX == 2656 || player.objectX == 2657) && player.objectY == 2585) {
				if (player.absY >= 2585) {
					player.absY = 2584;
				} else {
					player.absY = 2585;
				}
			} else if (player.absX <= 2585) {
				player.absY += 1;
			} else {
				player.absY -= 1;
			}
			player.getPA().movePlayer(player.absX, player.absY, 0);
			break;
		case 4387:
			// Server.castleWars.joinWait(player,1);
			break;
		case 4388:
			// Server.castleWars.joinWait(player,2);
			break;
		case 4408:
			// Server.castleWars.joinWait(player,3);
			break;
		case 9369: // Pits wait
			if (player.getY() > 5175) {
				player.sendMessage("Welcome to Fight Pits, a safe minigame.");
				player.getPA().movePlayer(2399, 5175, 0);
			} else {
				player.getPA().movePlayer(2399, 5177, 0);
			}
			break;
		case 9368: // Pits game
			if (player.getY() < 5169) {
				player.getPA().movePlayer(2399, 5169, 0);
			}
			break;

		/*
		 * case 2286: case 154: case 4058: case 2295: case 2285: case 2313: case 2312:
		 * case 2314: player.getAgility().handleGnomeCourse(objectType,obX,obY); break;
		 */
		// barrows
		// Chest
		case 10284:
			// case 170:
			if (player.barrowsKillCount < 5) {
				player.sendMessage("You haven't killed all the brothers");
			}
			if (player.barrowsKillCount == 5 && player.barrowsNpcs[player.randomCoffin][1] == 1) {
				player.sendMessage("I have already summoned this npc.");
			}
			if (player.barrowsNpcs[player.randomCoffin][1] == 0 && player.barrowsKillCount >= 5) {
				NPCHandler.spawnNpc(player, player.barrowsNpcs[player.randomCoffin][0], 3551, 9694 - 1, 0, 0, 120, 30,
						200, 200, true, true);
				player.barrowsNpcs[player.randomCoffin][1] = 1;
			}
			if ((player.barrowsKillCount > 5 || player.barrowsNpcs[player.randomCoffin][1] == 2)
					&& player.getItems().freeInventorySlots() >= 2) {
				player.getPA().resetBarrows();
				Barrows.BarrowsLoot(player);
				/*
				 * player.getItems().addItem(player.getPA().randomRunes(), Misc.random(150) +
				 * 100); if (Misc.random(2) == 1)
				 * player.getItems().addItem(player.getPA().randomBarrows(), 1);
				 * player.getPA().startTeleport(3564, 3288, 0, "modern");
				 */
			} else if (player.barrowsKillCount > 5 && player.getItems().freeInventorySlots() <= 1) {
				player.sendMessage("You need at least 2 inventory slot opened.");
			}
			break;
		// doors
		case 6749:
			if (obX == 3562 && obY == 9678) {
				player.getPA().object(3562, 9678, 6749, -3, 0);
				player.getPA().object(3562, 9677, 6730, -1, 0);
			} else if (obX == 3558 && obY == 9677) {
				player.getPA().object(3558, 9677, 6749, -1, 0);
				player.getPA().object(3558, 9678, 6730, -3, 0);
			}
			break;
		case 6730:
			if (obX == 3558 && obY == 9677) {
				player.getPA().object(3562, 9678, 6749, -3, 0);
				player.getPA().object(3562, 9677, 6730, -1, 0);
			} else if (obX == 3558 && obY == 9678) {
				player.getPA().object(3558, 9677, 6749, -1, 0);
				player.getPA().object(3558, 9678, 6730, -3, 0);
			}
			break;
		case 6727:
			if (obX == 3551 && obY == 9684) {
				player.sendMessage("You cant open this door..");
			}
			break;
		case 6746:
			if (obX == 3552 && obY == 9684) {
				player.sendMessage("You cant open this door..");
			}
			break;
		case 6748:
			if (obX == 3545 && obY == 9678) {
				player.getPA().object(3545, 9678, 6748, -3, 0);
				player.getPA().object(3545, 9677, 6729, -1, 0);
			} else if (obX == 3541 && obY == 9677) {
				player.getPA().object(3541, 9677, 6748, -1, 0);
				player.getPA().object(3541, 9678, 6729, -3, 0);
			}
			break;
		case 6729:
			if (obX == 3545 && obY == 9677) {
				player.getPA().object(3545, 9678, 6748, -3, 0);
				player.getPA().object(3545, 9677, 6729, -1, 0);
			} else if (obX == 3541 && obY == 9678) {
				player.getPA().object(3541, 9677, 6748, -1, 0);
				player.getPA().object(3541, 9678, 6729, -3, 0);
			}
			break;
		case 6726:
			if (obX == 3534 && obY == 9684) {
				player.getPA().object(3534, 9684, 6726, -4, 0);
				player.getPA().object(3535, 9684, 6745, -2, 0);
			} else if (obX == 3535 && obY == 9688) {
				player.getPA().object(3535, 9688, 6726, -2, 0);
				player.getPA().object(3534, 9688, 6745, -4, 0);
			}
			break;
		case 6745:
			if (obX == 3535 && obY == 9684) {
				player.getPA().object(3534, 9684, 6726, -4, 0);
				player.getPA().object(3535, 9684, 6745, -2, 0);
			} else if (obX == 3534 && obY == 9688) {
				player.getPA().object(3535, 9688, 6726, -2, 0);
				player.getPA().object(3534, 9688, 6745, -4, 0);
			}
			break;
		case 6743:
			if (obX == 3545 && obY == 9695) {
				player.getPA().object(3545, 9694, 6724, -1, 0);
				player.getPA().object(3545, 9695, 6743, -3, 0);
			} else if (obX == 3541 && obY == 9694) {
				player.getPA().object(3541, 9694, 6724, -1, 0);
				player.getPA().object(3541, 9695, 6743, -3, 0);
			}
			break;
		case 6724:
			if (obX == 3545 && obY == 9694) {
				player.getPA().object(3545, 9694, 6724, -1, 0);
				player.getPA().object(3545, 9695, 6743, -3, 0);
			} else if (obX == 3541 && obY == 9695) {
				player.getPA().object(3541, 9694, 6724, -1, 0);
				player.getPA().object(3541, 9695, 6743, -3, 0);
			}
			break;
		// end doors
		// coffins
		case 6707: // verac
			player.getPA().movePlayer(3556, 3298, 0);
			break;
		case 6823:
			if (Barrows.selectCoffin(player, objectType)) {
				return;
			}
			if (player.barrowsNpcs[0][1] == 0) {
				NPCHandler.spawnNpc(player, 2030, player.getX(), player.getY(), 3, 0, 100, 25, 90, 90, true, true);
				player.barrowsNpcs[0][1] = 1;
			} else {
				player.sendMessage("You have already searched in this sarcophagus.");
			}
			break;
		case 6706: // torag
			player.getPA().movePlayer(3553, 3283, 0);
			break;
		case 6772:
			if (Barrows.selectCoffin(player, objectType)) {
				return;
			}
			if (player.barrowsNpcs[1][1] == 0) {
				NPCHandler.spawnNpc(player, 2029, player.getX(), player.getY(), 3, 0, 100, 20, 90, 90, true, true);
				player.barrowsNpcs[1][1] = 1;
			} else {
				player.sendMessage("You have already searched in this sarcophagus.");
			}
			break;
		case 6705: // karil stairs
			player.getPA().movePlayer(3565, 3276, 0);
			break;
		case 6822:
			if (Barrows.selectCoffin(player, objectType)) {
				return;
			}
			if (player.barrowsNpcs[2][1] == 0) {
				NPCHandler.spawnNpc(player, 2028, player.getX(), player.getY(), 3, 0, 90, 17, 90, 75, true, true);
				player.barrowsNpcs[2][1] = 1;
			} else {
				player.sendMessage("You have already searched in this sarcophagus.");
			}
			break;
		case 6704: // guthan stairs
			player.getPA().movePlayer(3578, 3284, 0);
			break;
		case 6773:
			if (Barrows.selectCoffin(player, objectType)) {
				return;
			}
			if (player.barrowsNpcs[3][1] == 0) {
				NPCHandler.spawnNpc(player, 2027, player.getX(), player.getY(), 3, 0, 100, 23, 90, 90, true, true);
				player.barrowsNpcs[3][1] = 1;
			} else {
				player.sendMessage("You have already searched in this sarcophagus.");
			}
			break;
		case 6703: // dharok stairs
			player.getPA().movePlayer(3574, 3298, 0);
			break;
		case 6771:
			if (Barrows.selectCoffin(player, objectType)) {
				return;
			}
			if (player.barrowsNpcs[4][1] == 0) {
				NPCHandler.spawnNpc(player, 2026, player.getX(), player.getY(), 3, 0, 110, 45, 120, 120, true, true);
				player.barrowsNpcs[4][1] = 1;
			} else {
				player.sendMessage("You have already searched in this sarcophagus.");
			}
			break;
		case 6702: // ahrim stairs
			player.getPA().movePlayer(3565, 3290, 0);
			break;
		case 6821:
			if (Barrows.selectCoffin(player, objectType)) {
				return;
			}
			if (player.barrowsNpcs[5][1] == 0) {
				NPCHandler.spawnNpc(player, 2025, player.getX(), player.getY(), 3, 0, 90, 19, 90, 75, true, true);
				player.barrowsNpcs[5][1] = 1;
			} else {
				player.sendMessage("You have already searched in this sarcophagus.");
			}
			break;
		/*
		 * case 2491: // rune ess player.mining[0] = 7936; player.mining[1] = 15;
		 * player.mining[2] = 30; player.getMining().startMining(player.mining[0],
		 * player.mining[1], player.mining[2]); break;
		 */
		// DOORS
		/*
		 * case 1516: case 1519: if (player.objectY == 9698) { if (player.absY >=
		 * player.objectY) { player.getPA().walkTo(0, -1); } else {
		 * player.getPA().walkTo(0, 1); } break; } case 1530: case 1531: case 1533: case
		 * 1534: case 11712: case 11711: case 11707: case 11708: case 6725: case 3198:
		 * case 3197: Server.objectHandler.doorHandling(objectType, player.objectX,
		 * player.objectY, 0); break;
		 */
		case 9319:
			if (player.heightLevel == 0) {
				player.getPA().movePlayer(player.absX, player.absY, 1);
			} else if (player.heightLevel == 1) {
				player.getPA().movePlayer(player.absX, player.absY, 2);
			}
			break;
		case 9320:
			if (player.heightLevel == 1) {
				player.getPA().movePlayer(player.absX, player.absY, 0);
			} else if (player.heightLevel == 2) {
				player.getPA().movePlayer(player.absX, player.absY, 1);
			}
			break;
		case 4496:
		case 4494:
			if (player.heightLevel == 2) {
				player.getPA().movePlayer(player.absX - 5, player.absY, 1);
			} else if (player.heightLevel == 1) {
				player.getPA().movePlayer(player.absX + 5, player.absY, 0);
			}
			break;
		case 4493:
			if (player.heightLevel == 0) {
				player.getPA().movePlayer(player.absX - 5, player.absY, 1);
			} else if (player.heightLevel == 1) {
				player.getPA().movePlayer(player.absX + 5, player.absY, 2);
			}
			break;
		case 4495:
			if (player.heightLevel == 1) {
				player.getPA().movePlayer(player.absX + 5, player.absY, 2);
			}
			break;
		case 5126:
			if (player.absY == 3554) {
				player.getPA().walkTo(0, 1);
			} else {
				player.getPA().walkTo(0, -1);
			}
			break;
		case 10167:
			player.getPA().movePlayer(3094 + Misc.random(2), 3471 + Misc.random(2), 0);
			break;
		case 1755:
			if (player.objectX == 2884 && player.objectY == 9797) {
				player.getPA().movePlayer(player.absX, player.absY - 6400, 0);
			} else if (player.objectX == 3008 && player.objectY == 9550) {
				player.getPA().movePlayer(3094 + Misc.random(2), 3471 + Misc.random(2), 0);
			}
			break;
		case 1759:
			if (player.objectX == 2884 && player.objectY == 3397) {
				player.getPA().movePlayer(player.absX, player.absY + 6400, 0);
			}
			break;
		case 26444:
			break;
		case 26289:
		case 26288:
		case 26287:
		case 26286:
			if (player.gwdAltarTimer > 0) {
				player.sendMessage("You have to wait for " + player.gwdAltarTimer / 100
						+ " more minutes before doing this action.");
				return;
			}
			player.gwdAltarTimer += (10 * 60 * 1000 / 600);
			if (player.level[5] < player.getPA().getLevelForXP(player.xp[5])) {
				player.startAnimation(645);
				player.level[5] = player.getPA().getLevelForXP(player.xp[5]);
				player.sendMessage("You recharge your prayer points.");
				player.getPA().refreshSkill(5);
			} else {
				player.sendMessage("You already have full prayer points.");
			}
			break;
		case 409:
		case 10638:
		case 61:
			if (player.level[5] < player.getPA().getLevelForXP(player.xp[5])) {
				player.startAnimation(645);
				player.level[5] = player.getPA().getLevelForXP(player.xp[5]);
				player.sendMessage("You recharge your prayer points.");
				player.getPA().refreshSkill(5);
			} else {
				player.sendMessage("You already have full prayer points.");
			}
			break;
		case 28089:
			GrandExchange.openGrandExchange(player);
			break;
		case 2873:
			if (player.mageState == 5) {
				if (!player.getItems().ownsCape()) {
					player.startAnimation(645);
					player.sendMessage("Saradomin blesses you with a cape.");
					player.getItems().addItem(2412, 1);
				}
			} else {
				player.sendMessage("You must have defeated all forms of Kolodion before doing this!");
			}
			break;
		case 2875:
			if (player.mageState == 5) {
				if (!player.getItems().ownsCape()) {
					player.startAnimation(645);
					player.sendMessage("Guthix blesses you with a cape.");
					player.getItems().addItem(2413, 1);
				}
			} else {
				player.sendMessage("You must have defeated all forms of Kolodion before doing this!");
			}
			break;
		case 2874:
			if (player.mageState == 5) {
				if (!player.getItems().ownsCape()) {
					player.startAnimation(645);
					player.sendMessage("Zamorak blesses you with a cape.");
					player.getItems().addItem(2414, 1);
				}
			} else {
				player.sendMessage("You must have defeated all forms of Kolodion before doing this!");
			}
			break;
		case 2879:
			player.getPA().movePlayer(2538, 4716, 0);
			break;
		case 2878:
			player.getPA().movePlayer(2509, 4689, 0);
			break;
		case 5960:
			player.getPA().startTeleport(3090, 3956, 0);
			break;
		case 1815:
			player.getPA().startTeleport(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0);
			break;
		case 9706:
			player.getPA().startTeleport(3105, 3951, 0);
			break;
		case 9707:
			player.getPA().startTeleport(3105, 3956, 0);
			break;
		case 5959:
			player.getPA().startTeleport(2539, 4712, 0);
			break;
		case 2558:
			player.sendMessage("This door is locked.");
			break;
		case 9294:
			if (player.absX < player.objectX) {
				player.getPA().movePlayer(player.objectX + 1, player.absY, 0);
			} else if (player.absX > player.objectX) {
				player.getPA().movePlayer(player.objectX - 1, player.absY, 0);
			}
			break;
		case 2561:
			player.getThieving().stealFromStall(1897, 500, 1);
			break;
		case 2560:
			player.getThieving().stealFromStall(950, 1350, 50);
			break;
		case 2562:
			player.getThieving().stealFromStall(1635, 2500, 75);
			break;

		case 4876: // respected donor zone stall

			if (objectLocation.equals(2753, 2775)) {
				return;
			}

			if (objectLocation.equals(3047, 4384)) {
				player.getThieving().stealFromStall(19665, 3000, 1);
			} else {
				player.getThieving().stealFromStall(5982, 5700, 1);
			}
			break;

		/**
		 * case 2563: player.getThieving().stealFromStall(7650, 100, 75); break;
		 */
		case 2564:
			player.getThieving().stealFromStall(1613, 2850, 90);
			break;

		case 10529:
		case 10527:
			if (player.absY <= player.objectY) {
				player.getPA().walkTo(0, 1);
			} else {
				player.getPA().walkTo(0, -1);
			}
			break;
		case 4188:
		case 3044:
		case 21303:
		case 26814:
			player.getSmithing().sendSmelting();
			break;
		case 733:
			if (player.equipment[Player.playerWeapon] == -1 && !player.getItems().playerHasItem(946)) {
				player.sendMessage("You need a weapon or a knife to slash the web.");
				return;
			}
			player.startAnimation(451);
			if (Misc.random(1) == 1) {
				int rotation = 3;
				int type = 10;
				if (player.objectY == 3957 && (player.objectX == 3095 || player.objectX == 3092)) {
					type = 0;
					rotation = 0;
				}
				if (player.objectX == 3092 && player.objectY == 3957) {
					rotation = 2;
				}
				if (player.objectX == 3158 && player.objectY == 3951) {
					rotation = 1;
				}

				Collision.removeObjectClipping(objectType, objectLocation, rotation, type);
				Server.objectManager.removeObject(objectLocation, type, false);
				new GameObject(-1, player.objectX, player.objectY, player.heightLevel, rotation, type, 733, 50);
				
				player.sendMessage("You slash the web.");
			} else {
				player.sendMessage("You fail to slash the webs.");
			}
			break;
		/*
		 * case 733: player.startAnimation(451);
		 * 
		 * // if (Misc.random(1) == 1) { // player.getPA().removeObject(player.objectX,
		 * player.objectY); // player.sendMessage("You slash the web."); //} else { //
		 * player.sendMessage("You fail to slash the webs."); }
		 * 
		 * if (player.objectX == 3158 && player.objectY == 3951) { new GameObject(734,
		 * player.objectX, player.objectY, player.heightLevel, 1, 10, 733, 50); } else {
		 * //TODO: Fix webs for realZ new GameObject(734, player.objectX,
		 * player.objectY, player.heightLevel, 0, 10, 733, 50); } break;
		 */
		default:
			break;
		}
	}

	public void secondClickObject(int objectType, int obX, int obY) {
		player.clickObjectType = 0;

		Location objectLocation = Location.create(obX, obY, player.heightLevel);

		if (ServerEvents.onObjectClick(player, objectType, objectLocation, 2)) {
			return;
		}

		if (Scripting.handleMessage(player, new SecondObjectActionMessage(objectType, objectLocation))) {
			return;
		}

		int xDiff = Math.abs(player.absX - obX);
		int yDiff = Math.abs(player.absY - obY);
		if (objectType == 38012 && obX == 2839 && obY == 3537 && xDiff <= 2 && yDiff <= 2) {
			player.getPA().movePlayer(player.absX, player.absY, player.heightLevel + 1);
		}
		// player.sendMessage("GameObject type: " + objectType);
		switch (objectType) {
		case 28089:
			GrandExchange.openGrandExchange(player);
			break;
		case 11666:
		case 21303:
		case 3044:
		case 26814:
		case 4188:
			player.getSmithing().sendSmelting();
			break;
		case 2561:
			player.getThieving().stealFromStall(1897, 500, 1);
			break;
		case 2560:
			player.getThieving().stealFromStall(950, 1350, 50);
			break;
		case 2562:
			player.getThieving().stealFromStall(1635, 2300, 75);
			break;

		case 4876: // respected donor zone stall
			if (objectLocation.equals(2753, 2775)) {
				return;
			}

			if (objectLocation.equals(3047, 4384)) {
				player.getThieving().stealFromStall(19665, 3000, 1);
			} else {
				player.getThieving().stealFromStall(5982, 5700, 1);
			}
			break;
		/**
		 * case 2563: player.getThieving().stealFromStall(7650, 100, 75); break;
		 */
		case 2564:
			player.getThieving().stealFromStall(1613, 2750, 90);
			break;
		case 2213:
		case 14367:
		case 11758:
		case 27663:
		case 26972:
			player.getPA().openUpBank();
			break;
		case 2558:
			if (System.currentTimeMillis() - player.lastLockPick < 3000 || player.freezeTimer > 0) {
				break;
			}
			if (player.getItems().playerHasItem(1523, 1)) {
				player.lastLockPick = System.currentTimeMillis();
				if (Misc.random(10) <= 3) {
					player.sendMessage("You fail to pick the lock.");
					break;
				}
				if (player.objectX == 3044 && player.objectY == 3956) {
					if (player.absX == 3045) {
						player.getPA().walkTo2(-1, 0);
					} else if (player.absX == 3044) {
						player.getPA().walkTo2(1, 0);
					}
				} else if (player.objectX == 3038 && player.objectY == 3956) {
					if (player.absX == 3037) {
						player.getPA().walkTo2(1, 0);
					} else if (player.absX == 3038) {
						player.getPA().walkTo2(-1, 0);
					}
				} else if (player.objectX == 3041 && player.objectY == 3959) {
					if (player.absY == 3960) {
						player.getPA().walkTo2(0, -1);
					} else if (player.absY == 3959) {
						player.getPA().walkTo2(0, 1);
					}
				}
			} else {
				player.sendMessage("I need a lockpick to pick this lock.");
			}
			break;
		default:
			break;
		}
	}

	public void thirdClickObject(int objectType, int obX, int obY) {
		player.clickObjectType = 0;

		Location objectLocation = Location.create(obX, obY, player.heightLevel);
		if (ServerEvents.onObjectClick(player, objectType, objectLocation, 1)) {
			return;
		}
		if (Scripting.handleMessage(player, new ThirdObjectActionMessage(objectType, objectLocation))) {
			return;
		}

		switch (objectType) {
		case 35647:
		case 28089:
		case 2213:
		case 14367:
		case 11758:
		case 27663:
		case 26972:
			GrandExchange.openGrandExchange(player);
			break;
		default:
			break;
		}
	}

	public void firstClickNpc(int npcType) {
		NPC npc = NPCHandler.npcs[player.npcClickIndex];

		if (player.rights == 3) {
			// System.out.println("First Click Npc : " + npcType);
		}

		player.clickNpcType = 0;
		player.npcClickIndex = 0;

		if (Scripting.handleMessage(player, new FirstNpcActionMessage(npc))) {
			return;
		}
		if (ServerEvents.onNpcClick(player, npc, 1)) {
			return;
		}
		if (Summoning.onNpcClick(player, npc, 1)) {
			return;
		}

		if (!player.playerIsFishing) {
			Fishing.fishingNPC(player, 1, npcType);
		}

		if (TreasureTrails.clickingNpc(player, npc)) {
			return;
		}

		switch (npcType) {

		case 564:// TODO
			if (player.getTotalDonated() >= 50)
				Shops.open(player, 29);
			break;

		case 5266:
			if (player.getTotalDonated() >= 750)
				Shops.open(player, 28);
			break;

		case 230: // stardust shop
			Shops.open(player, 16);
			break;

		case 614: // dougs tools
			Shops.open(player, 19);
			break;

		case 6390: // grim reaper
			Shops.open(player, 999);
			break;

		case 274: // master cape shop
			Shops.open(player, 14);
			break;

		// case 5623: // Zombie shop
		// Shops.open(player, 69);
		// player.sendMessage("You have @red@" + player.zombiePoints + "@bla@ zombie
		// points");
		// break;

		case 418: // Leprechaun / Farming shop
			Shops.open(player, 140);
			break;
		case 648:
			Shops.open(player, 139);
			break;
		case 578:
			Shops.open(player, 22);
			break;
		case 2262:
			player.getPA().addPouches();
			break;
		case 9462:
		case 9464:
		case 9466:
			NPCHandler.stompStrykewyrm(player, npc);
			break;
		case 9085:
			if (player.combatLevel < 85 || player.level[18] < 50) {
				player.sendMessage("You need a combat level of 85 and a Slayer level of 50 to speak with Kuradal.");
				return;
			}
			player.getDH().sendDialogues(1592, npcType);
			break;
		case 1526:
			Shops.open(player, 44);
			break;
		case 804:
			Tanner.sendHideExchangeInterface(player);
			break;
		case 1552:
			if (player.christmasEvent == 0) {
				player.getDH().sendDialogues(1572, 1552);
				return;
			}
			if (player.christmasEvent == 1) {
				player.getDH().sendStatement("It appears you still have work left to do in the workshop!");
				return;
			}
			if (player.christmasEvent == 2 && player.marionetteMaking == 0) {// || player.baubleMaking == 0) {
				player.getDH().sendStatement("It appears you still have work left to do in the workshop!");
				return;
			}
			if (player.christmasEvent == 2 && player.marionetteMaking == 1 && player.baubleMaking == 0) {
				player.getDH().sendDialogues(1582, 1552);
				return;
			}
			if (player.christmasEvent == 3 || player.christmasEvent == 4) {
				player.getDH().sendDialogues(1587, 1552);
				return;
			}
			break;
		case 970:
			if (player.christmasEvent == 0) {
				player.getDH().sendDialogues(1550, 970);
				return;
			}
			if (player.christmasEvent == 1) {
				player.getDH().sendStatement("You need to speak with the head pixie Rosie in the workshop.");
				return;
			}
			if (player.christmasEvent == 2 && player.marionetteMaking == 0) {
				player.getDH().sendStatement("It appears you still have work left to do in the workshop!");
				return;
			}
			if (player.christmasEvent == 2 && player.marionetteMaking == 1 && player.baubleMaking == 0) {
				player.getDH().sendStatement("I believe it would be best to speak with santa first.");
				return;
			}
			if (player.christmasEvent == 3) {
				player.getDH().sendDialogues(1588, 970);
				return;
			}
			if (player.christmasEvent == 4) {
				player.getDH().sendDialogues(1590, 970);
				return;
			}
			break;
		case 3082:
			if (player.getItems().playerHasItem(6854)) {
				if (player.marionetteMaking == 1) {
					player.sendMessage("You've already given Rosie all the required completed puppet boxes!");
					return;
				}
				player.getDH().sendStatement("You hand Rosie 1 completed puppet box.");
				MarionetteMaking.completePuppetBox(player);
				return;
			}
			if (player.getItems().playerHasItem(6855)) {
				if (player.baubleMaking == 1) {
					player.sendMessage("You've already given Rosie all the required completed bauble boxes!");
					return;
				}
				player.getDH().sendStatement("You hand Rosie 1 completed bauble box.");
				BaubleMaking.completeBaubleBox(player);
				return;
			}
			if (player.christmasEvent == 1) {
				player.getDH().sendDialogues(1557, 3082);
			}
			if (player.christmasEvent == 2) {
				player.getDH().sendDialogues(1565, 3082);
			}
			if (player.christmasEvent == 3 || player.christmasEvent == 4 && player.giftAmount != 5) {
				player.getDH().sendDialogues(1565, 3082);
			} else if (player.christmasEvent == 3 || player.christmasEvent == 4 && player.giftAmount == 5) {
				player.getDH().sendDialogues(1591, 3082);
			}
			break;
		case 580:
			Shops.open(player, 125);
			break;
		case 3083:
			player.getDH().sendDialogues(1576, 3083);
			break;
		case 3084:
			player.getDH().sendDialogues(1577, 3084);
			break;
		case 3085:
			player.getDH().sendDialogues(1578, 3085);
			break;
		case 3086:
			player.getDH().sendDialogues(1579, 3086);
			break;
		case 3087:
			player.getDH().sendDialogues(1580, 3087);
			break;
		case 3088:
			player.getDH().sendDialogues(1581, 3088);
			break;
		case 6139:
			player.getDH().sendDialogues(5021, npcType);
			break;
		case 1888:
			player.getDH().sendDialogues(102, npcType);
			break;
		case 6537:
			player.getDH().sendDialogues(5002, 8725);
			break;
		case 251:
			player.getPA().showInterface(65400);// loyalty title interface
			break;
		case 2998:
			if (player.rights < 1) {
				player.getDH().sendDialogues(97, npcType);
				// player.getPA().sendFrame126("Current fund: ", 11003);
				// player.getPA().sendFrame126("Last winner: ", 11005);
				// player.getPA().showInterface(11000);
			} else {
				player.sendMessage("Your rank is too high to participate in the lottery!");
			}
			break;

		case 2622:// tokul shop
			Shops.open(player, 32);
			break;
		case 523:// general store
			Shops.open(player, 1);
			break;
		case 461:// mage
			Shops.open(player, 2);
			break;

		case 683:// range
			Shops.open(player, 3);
			break;

		case 549:// horvik
			Shops.open(player, 4);
			break;
		case 2538:// gile
			Shops.open(player, 6);
			break;

		case 519:// bob
			Shops.open(player, 8);
			break;

		case 541:// zeke
			Shops.open(player, 5);
			break;
		case 726:
			player.getDH().sendDialogues(3626, npcType);
			break;
		case 546:
			Shops.open(player, 2);
			// //player.sendMessage("@red@All items in this shop are free!");
			// player.sendMessage("@red@All items in this shop are free!");
			break;
		case 884:
			player.getDH().sendDialogues(5020, npcType);
			break;
		/*
		 * Shops.open(player, 50); player.sendMessage("You have " + player.pkPoints +
		 * " Pk Points."); break;
		 */
		case 553:
			Shops.open(player, 99);
			// player.sendMessage("@red@All items in this shop are free!");
			// player.sendMessage("@red@All items in this shop are free!");
			break;
		case 1294:
			// Shops.open(player, 23);
			// player.sendMessage("@red@All items in this shop are free!");
			// player.sendMessage("@red@All items in this shop are free!");
			break;
		case 2830:
			Shops.open(player, 21);
			// player.sendMessage("@red@All items in this shop are free!");
			// player.sendMessage("@red@All items in this shop are free!");
			break;
		case 6187:
			Shops.open(player, 30);
			break;
		case 2270:
			Shops.open(player, 20);
			// player.sendMessage("@red@All items in this shop are free!");
			// player.sendMessage("@red@All items in this shop are free!");
			break;
		case 650:
			Shops.open(player, 69);
			player.sendMessage("You have @red@" + player.zombiePoints + "@bla@ Zombie points");
			player.sendMessage("More rewards our on their way!");
			break;
		case 401:
			player.getPA().spellTeleport(2721, 2748, 0);
			player.sendMessage("Are you ready to take on the mighty jungle demon?");
			player.sendMessage("*Jungle demon is still being worked on, and will be improved even more*");
			break;
		case 6524:
			player.getDH().sendDialogues(42, npcType);
			break;
		case 554:// fancydress
			Shops.open(player, 77);
			player.sendMessage(
					"You have @red@" + player.donatorPoint + "@bla@ donator points. Type ::donate to buy more.");
			player.sendMessage("Remember to check out the other donator shop right next to me!");
			break;
		case 4361:// fancydan
			Shops.open(player, 78);
			player.sendMessage(
					"You have @red@" + player.donatorPoint + "@bla@ donator points. Type ::donate to buy more.");
			player.sendMessage("Remember to check out the other donator shop right next to me!");
			break;
		/*
		 * case 1552://santa Shops.open(player, 9); break;
		 */
		case 1917:// bandit shop
			Shops.open(player, 76);
			break;
		// case 9085:
		// if (player.slayerTask <= 0) {
		// player.getDH().sendDialogues(11, type);
		// } else {
		// player.getDH().sendDialogues(13, type);
		// }
		// break;
		case 3050:
			player.getDH().sendDialogues(39, npcType);
			break;
		// case 2244: //lumby guide
		// player.getPA().showInterface(23600);
		// break;
		case 706:
			player.getDH().sendDialogues(9, npcType);
			break;
		case 2258:
			player.startAnimation(1818);
			player.getPA().movePlayer(3040, 4841, 0);
			player.gfx0(343);
			break;
		/*
		 * case 1552: player.getDH().sendDialogues(24, type); break;
		 */
		case 6138:
			player.getDH().sendDialogues(24, npcType);
			break;
		case 2288:
			// player.getPA().showInterface(21356);
			Shops.open(player, 31);
			player.sendMessage("@red@You can earn 4 vote points every 12 hours, and a free 4m each vote! Type ::vote");
			player.sendMessage("You have @red@" + player.votePoints + "@bla@ vote points");
			break;
		case 33:
			if (player.isMember == 1) {
				Shops.open(player, 19);
			} else if (player.isMember == 2) {
				Shops.open(player, 20);
			} else if (player.isMember == 3) {
				Shops.open(player, 21);
			}
			// player.getDH().sendDialogues(20,type);
			break;
		case 384:
			player.getDH().sendDialogues(22, npcType);
			break;
		/*
		 * case 541://old zs Shops.open(player, 5); break;
		 */
		case 1835:
			Shops.open(player, 75);
			break;
		case 239:
			Shops.open(player, 13);
			player.sendMessage("You have @red@" + player.getData().pkPoints + "@bla@ pk points");
			break;

		/*
		 * case 549: //old zs Shops.open(player, 4); break;
		 */

		case 1282:
			Shops.open(player, 7);
			break;
		case 219:
			Shops.open(player, 97);
			break;
		case 1152:
			player.getDH().sendDialogues(16, npcType);
			break;
		case 494:
			player.getPA().openUpBank();
			break;
		case 2253:
			Shops.open(player, 9);
			// player.getShops().openSkillCape();
			break;
		case 3789:
			// player.sendMessage("You currently have " + player.pcPoints +
			// " pest control points.");
			player.getDH().sendOption4("Attack (10 pts)", "Strength (10 pts)", "Defence (10 pts)", "More");
			player.pestAction = 1;
			break;
		case 3788:
			// player.getShops().openVoid();
			player.sendMessage("You currently have " + player.pcPoints + " pest control points.");
			break;
		case 905:
			if (player.mageState == 5)
				player.getDH().sendDialogues(5, npcType);
			else
				player.getDH().sendDialogues(3500, npcType);
			break;
		case 462:
			player.getDH().sendDialogues(7, npcType);
			break;
		case 1700:
			Shops.open(player, 22);
			break;
		case 522:
			Shops.open(player, 1);
			break;
		case 599:
			player.getPA().showInterface(3559);
			player.canChangeAppearance = true;
			break;
		case 904:
			if (player.mageState != 5) {
				player.sendMessage("In order to earn points, you must defeat kolodion.");
			}
			player.sendMessage("@red@You have " + player.magePoints + " points.");
			break;
		}
	}

	public void secondClickNpc(int npcType) {
		NPC npc = NPCHandler.npcs[player.npcClickIndex];

		player.clickNpcType = 0;
		player.npcClickIndex = 0;

		if (player.rights == 3) {
			System.out.println("Second Click Npc : " + npcType);
		}

		if (Scripting.handleMessage(player, new SecondNpcActionMessage(npc))) {
			return;
		}

		if (ServerEvents.onNpcClick(player, npc, 2)) {
			return;
		}

		if (Summoning.onNpcClick(player, npc, 2)) {
			return;
		}

		if (!player.playerIsFishing) {
			Fishing.fishingNPC(player, 2, npcType);
		}
		switch (npcType) {
		case 2262:
			player.getPA().repairPouches();
			break;
		case 804:
			Tanner.sendHideExchangeInterface(player);
			break;
		case 1304:
			player.getPA().spellTeleport(2571, 3896, 0);
			break;
		case 1282:
			Shops.open(player, 7);
			break;
		case 3788:
			// player.getShops().openVoid();
			break;
		case 494:
			player.getPA().openUpBank();
			break;
		case 904:
			Shops.open(player, 17);
			player.sendMessage("@red@You have " + player.magePoints + " points.");
			break;
		case 522:
		case 523:
			Shops.open(player, 1);
			break;
		/*
		 * case 541: //old zs Shops.open(player, 5); break;
		 */
		case 578:
			Shops.open(player, 22);
			break;
		case 461:
			Shops.open(player, 2);
			break;
		case 549:
			Shops.open(player, 4);
			break;
		case 2538:
			Shops.open(player, 6);
			break;
		case 519:
			Shops.open(player, 8);
			break;
		case 3789:
			Shops.open(player, 18);
			break;
		case 1699:
			Shops.open(player, 9);
			break;
		case 6970:
			Shops.open(player, 27);
			break;
		case 3416:
			player.getDH().sendDialogues(45, npcType);
			break;
		// case 9085:
		// if (player.slayerTask <= 0) {
		// player.getDH().sendDialogues(11, type);
		// } else {
		// player.getDH().sendDialogues(13, type);
		// }
		// break;
		case 1:
		case 9:
		case 18:
		case 20:
		case 26:
		case 21:
			if (npc.isDead()) {
				return;
			}
			player.getThieving().stealFromNPC(npcType);
			break;
		}
	}

	public void thirdClickNpc(int npcType) {

		NPC npc = NPCHandler.npcs[player.npcClickIndex];

		player.clickNpcType = 0;
		player.npcClickIndex = 0;

		if (player.rights == 3) {
			System.out.println("Third Click NPC : " + npcType);
		}

		if (Scripting.handleMessage(player, new ThirdNpcActionMessage(npc))) {
			return;
		}

		if (ServerEvents.onNpcClick(player, npc, 3)) {
			return;
		}
		if (Summoning.onNpcClick(player, npc, 3)) {
			return;
		}

		switch (npcType) {
		case 2259:
			player.getPA().startTeleport(3040, 4844, 0, "modern");
			break;
		case 553:
			// player.teleTimer = 11;
			player.getPA().movePlayer(2898, 4844, 0);
			player.gfx0(110);
			break;

		/*
		 * switch (Misc.random(4) + 1) { case 1: player.getPA().spellTeleport(2898,
		 * 4844, 0); break; case 2: player.getPA().spellTeleport(2920, 4842, 0); break;
		 * case 3: player.getPA().spellTeleport(2911, 4832, 0); break; case 4:
		 * player.getPA().spellTeleport(2922, 4820, 0); break; case 5:
		 * player.getPA().spellTeleport(2900, 4821, 0); break; } break;
		 */
		}
	}

	public void fourthClickNpc(int npcType) {
		NPC npc = NPCHandler.npcs[player.npcClickIndex];

		player.clickNpcType = 0;
		player.npcClickIndex = 0;

		if (ServerEvents.onNpcClick(player, npc, 4)) {
			return;
		}
	}

}
