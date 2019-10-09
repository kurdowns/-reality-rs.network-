package com.zionscape.server.net.packets;

import com.zionscape.server.ServerEvents;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.Location;
import com.zionscape.server.model.content.Resting;
import com.zionscape.server.model.content.minigames.DuelArena;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.skills.agility.Agility.Obstacle;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;
import com.zionscape.server.world.shops.Shops;

/**
 * Walking packet
 */
public class Walking implements PacketType {

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(Player c, int packetType, int packetSize, Stream stream) {


		if (!c.getData().completedTutorial || c.attributeExists("doing_tutorial")) {
			return;
		}

		if (c.blockWalking) {
			if(c.rights >= 3) {
				c.sendMessage("c.blockWalking: true");
			}
			return;
		}

		c.nextChat = -1;
		Obstacle obstacle = (Obstacle) c.getAttribute("obstacle");
		if (obstacle != null) {
			return;
		}
		if (c.dialogueAction == 5004) {
			return;
		}
		if (System.currentTimeMillis() - c.lastSkillEmote < 7_500) {
			return;
		}
		/*
		 * if(c.puzzle != null) { c.puzzle = null; }
		 */

		// reset the dialogues stage we moved
		if (c.getDialogueOwner() != null) {
			c.resetDialogue();
		}

		GameCycleTaskHandler.stopEventsUponWalking(c);
		c.removeAttribute("ironman_unoting");
		c.removeAttribute("pending_duo_partner");
		c.dialogueAction = 0;
		c.teleAction = 0;
		c.dialogueId = 0;
		c.usingRingOfWealth = false;
		c.fishing = false;
		c.isBanking = false;
		c.storingFamiliarItems = false;
		c.skilling = false;
		Resting.stop(c);
		Shops.close(c);


		WalkingType type = WalkingType.CLICKING;
		if (packetType == 248) {
			type = WalkingType.CLICKING_MAP;
		} else if (packetType == 98) {
			type = WalkingType.WALKING_TO;
		}

		if (ServerEvents.onPlayerWalk(c, type)) {
			return;
		}

		if (c.isCooking) {
			return;
		}
		if (c.isDoingTutorial()) {
			return;
		}
		if (packetType == 248 || packetType == 164) {
			c.faceUpdate(0);
			c.npcIndex = 0;
			c.playerIndex = 0;
			if (c.followId > 0 || c.followId2 > 0) {
				c.getPA().resetFollow();
			}
		}
		if (c.inTrade) {
			c.getTradeAndDuel().declineTrade();
		}

		boolean closeInterfaces = true;

		c.duelRequestId = -1;
		if (c.getDuel() != null && (c.getDuel().getStage() == DuelArena.Stage.STAKING || c.getDuel().getStage() == DuelArena.Stage.CONFIRMING_STAKE)) {
			if (packetType != 98) {
				DuelArena.declineDuel(c);
			}
			closeInterfaces = false;
		}


		c.stopPlayerSkill = true;
		if (closeInterfaces)
			c.getPA().removeAllWindows();
		//c.startAnimation(65535);
		if (c.freezeTimer > 0) {
			if (PlayerHandler.players[c.playerIndex] != null) {
				if (c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[c.playerIndex].getX(),
						PlayerHandler.players[c.playerIndex].getY(), 1) && packetType != 98) {
					c.playerIndex = 0;
					return;
				}
			}
			if (packetType != 98) {
				c.sendMessage("A magical force stops you from moving.");
				c.playerIndex = 0;
			}
			return;
		}
		if (packetType != 98 && c.getDuel() != null && c.getDuel().getStage() == DuelArena.Stage.FIGHTING) {
			if (!c.getDuel().isStarted()) {
				c.sendMessage("The duel has not started yet!");
				c.playerIndex = 0;
				return;
			}
			if (c.getDuel().getRules()[PlayerConstants.DUEL_MOVEMENT]) {
				c.sendMessage("Walking has been disabled in this duel !");
				c.playerIndex = 0;
				return;
			}
		}

		if (System.currentTimeMillis() - c.lastSpear < 4000) {
			c.sendMessage("You have been stunned.");
			c.playerIndex = 0;
			return;
		}
		if (packetType == 98) {
			c.mageAllowed = true;
		}
		if (c.respawnTimer > 3) {
			return;
		}
		if (c.inTrade) {
			return;
		}
		if (packetType == 248) {
			packetSize -= 14;
		}

		final int steps = (packetSize - 5) / 2;
		final int[][] path = new int[steps][2];

		final int firstX = stream.readSignedWordBigEndianA();
		for (int i = 0; i < steps; i++) {
			path[i][0] = stream.readSignedByte();
			path[i][1] = stream.readSignedByte();
		}
		final int firstY = stream.readSignedWordBigEndian();
		final boolean runSteps = stream.readSignedByteC() == 1;

		c.getWalkingQueue().setRunningQueue(runSteps);

		final Location[] positions = new Location[steps + 1];
		positions[0] = Location.create(firstX, firstY, c.getLocation().getZ());

		for (int i = 0; i < steps; i++) {
			positions[i + 1] = Location.create(path[i][0] + firstX, path[i][1] + firstY, c.getLocation().getZ());
		}

		if (c.getWalkingQueue().addFirstStep(positions[0])) {
			for (int i = 1; i < positions.length; i++) {
				if (!c.getWalkingQueue().addStep(positions[i], true)) {
					return;
				}
			}
		}
	}

	public enum WalkingType {
		CLICKING, CLICKING_MAP, WALKING_TO
	}

}