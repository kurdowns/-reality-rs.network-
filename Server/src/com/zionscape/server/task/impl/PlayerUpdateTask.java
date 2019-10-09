package com.zionscape.server.task.impl;

import com.zionscape.server.Server;
import com.zionscape.server.model.content.Food;
import com.zionscape.server.model.items.ItemUtility;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.skills.summoning.Summoning;
import com.zionscape.server.net.Packet;
import com.zionscape.server.net.PacketBuilder;
import com.zionscape.server.task.Task;
import com.zionscape.server.util.Misc;
import com.zionscape.server.world.ItemDrops;

import java.util.BitSet;
import java.util.Iterator;
import java.util.Map;

/**
 * A task which creates and sends the player update block.
 *
 * @author Graham Edgecombe
 */
public class PlayerUpdateTask implements Task {

	/**
	 * The player.
	 */
	private Player player;

	/**
	 * Creates an update task.
	 *
	 * @param player The player.
	 */
	public PlayerUpdateTask(Player player) {
		this.player = player;
	}

	private static void appendHit2Update(final Player p, final PacketBuilder updateBlock) {
		if (p.level[3] <= 0) {
			p.level[3] = 0;


			if (p.underAttackByNpcID > 0) {
				p.getData().timedKilledByNpc++;
			}

			p.isDead = true;
		}

		updateBlock.putShortA(p.getHitDiff2()); // What the perseon got 'hit' for

		if (p.poisonMask == 2) {
			updateBlock.putShortA(2);
		} else if (p.getHitDiff2() > p.hitMax2 * 0.90) {
			updateBlock.putShortA(1); // crits
		} else {
			updateBlock.putShortA(0);
		}

		updateBlock.put((byte) (p.hitIcon2 - 1));// icon
		// str.writeByte(hitFocus);//focus
		updateBlock.putShortA(p.level[3]); // Their current hp, for HP bar
		updateBlock.putShortA((p.getPA().getLevelForXP(p.xp[3]) + Food.getHpBoost(p))); // Their max hp, for HP bar
	}

	private static void appendHitUpdate(final Player p, final PacketBuilder updateBlock) {
		if (p.level[3] <= 0) {
			p.level[3] = 0;

			if (p.underAttackByNpcID > 0) {
				p.getData().timedKilledByNpc++;
			}

			p.isDead = true;
		}

		updateBlock.putShortA(p.getHitDiff()); // What the perseon got 'hit' for

		if (p.poisonMask == 1) {
			updateBlock.putShortA(2);
		} else if (p.getHitDiff2() > p.hitMax2 * 0.90) {
			updateBlock.putShortA(1); // crits
		} else {
			updateBlock.putShortA(0);
		}

		updateBlock.put((byte) (p.hitIcon - 1));// icon

		// str.writeByte(hitFocus);//focus

		updateBlock.putShortA(p.level[3]); // Their current hp, for HP bar
		updateBlock.putShortA((p.getPA().getLevelForXP(p.xp[3]) + Food.getHpBoost(p))); // Their max hp, for HP bar
	}

	private static void appendForceMovementMask(final Player p, final PacketBuilder updateBlock) {
		updateBlock.putByteS((byte) p.forceMovementStartX); // X where were starting from
		updateBlock.putByteS((byte) p.forceMovementStartY); // Y where were starting from
		updateBlock.putByteS((byte) p.forceMovementEndX);  // X1 Where were moving to
		updateBlock.putByteS((byte) p.forceMovementEndY); // Y1 where were moving to
		updateBlock.putLEShortA(p.forceMovementSpeed1); // speed to get to the first cord 0 - 1000
		updateBlock.putShortA(p.forceMovementSpeed2);  // speed2 the time to get from the start to the destination 0-1000
		updateBlock.putByteS((byte) p.forceMovementDirection); // the direct the player faces 0-3
	}

	@Override
	public void execute() {

		// update timer
		if (PlayerHandler.updateRunning && !PlayerHandler.updateAnnounced) {
			player.getOutStream().createFrame(114);
			player.getOutStream().writeWordBigEndian(PlayerHandler.updateSeconds * 50 / 30);
			player.flushOutStream();
		}

        /*
		 * If the map region changed send the new one.
		 * We do this immediately as the client can begin loading it before the
		 * actual packet is received.
		 */
		if (player.mapRegionDidChange) {
			player.getPA().sendMapRegion();
		}

		/*
		 * The update block packet holds update blocks and is send after the
		 * main packet.
		 */
		PacketBuilder updateBlock = new PacketBuilder();

		/*
		 * The main packet is written in bits instead of bytes and holds
		 * information about the local list, players to add and remove,
		 * movement and which updates are required.
		 */
		PacketBuilder packet = new PacketBuilder(81, Packet.Type.VARIABLE_SHORT);
		packet.startBitAccess();

		/*
		 * Updates this player.
		 */
		updateThisPlayerMovement(packet);
		updatePlayer(updateBlock, player, false, true);

		/*
		 * Write the current size of the player list.
		 */
		packet.putBits(8, player.getLocalPlayers().size());

		/*
		 * Iterate through the local player list.
		 */
		for (Iterator<Player> it$ = player.getLocalPlayers().iterator(); it$.hasNext(); ) {
			/*
			 * Get the next player.
			 */
			Player otherPlayer = it$.next();

			/*
			 * If the player should still be in our list.
			 */
			if (PlayerHandler.isPlayerOnline(otherPlayer) && !otherPlayer.didTeleport && player.getLocation().isWithinDistance(otherPlayer.getLocation()) && !otherPlayer.attributeExists("hide_me")) {

				/*
				 * Update the movement.
				 */
				updatePlayerMovement(packet, otherPlayer);

				/*
				 * Check if an update is required, and if so, send the update.
				 */
				if (otherPlayer.updateRequired) {
					updatePlayer(updateBlock, otherPlayer, false, false);
				}
			} else {
				/*
				 * Otherwise, remove the player from the list.
				 */
				it$.remove();

				/*
				 * Tell the client to remove the player from the list.
				 */
				packet.putBits(1, 1);
				packet.putBits(2, 3);
			}
		}

		/*
		 * Loop through every player.
		 */
		for (Player otherPlayer : PlayerHandler.getLocalPlayers(player)) {
			/*
			 * Check if there is room left in the local list.
			 */
			if (player.getLocalPlayers().size() >= 75) {
				/*
				 * There is no more room left in the local list. We cannot add
				 * more players, so we just ignore the extra ones. They will be
				 * added as other players get removed.
				 */
				break;
			}

			/*
			 * If they should not be added ignore them.
			 */
			if (otherPlayer == player || player.getLocalPlayers().contains(otherPlayer) || otherPlayer.isHidden()) {
				continue;
			}

			/*
			 * Add the player to the local list if it is within distance.
			 */
			player.getLocalPlayers().add(otherPlayer);

			/*
			 * Add the player in the packet.
			 */
			addNewPlayer(packet, otherPlayer);

			/*
			 * Update the player, forcing the appearance flag.
			 */
			updatePlayer(updateBlock, otherPlayer, true, false);
		}

		/*
		 * Check if the update block is not empty.
		 */
		if (!updateBlock.isEmpty()) {
			/*
			 * Write a magic id indicating an update block follows.
			 */
			packet.putBits(11, 2047);
			packet.finishBitAccess();

			/*
			 * Add the update block at the end of this packet.
			 */
			packet.put(updateBlock.toPacket().getPayload());
		} else {
			/*
			 * Terminate the packet normally.
			 */
			packet.finishBitAccess();
		}

		/*
		 * Write the packet.
		 */
		Packet p = packet.toPacket();
		if (p.getLength() > 10000) {
			System.out.println("WARNING PLAYER UPDATING OVER 10000 BYTES DROPPING PACKET");
		} else {
			(player).writePacket(packet.toPacket());
		}

		// send items and objects
		if (player.mapRegionDidChange) {
			ItemDrops.reloadItems(player);
			Server.objectManager.loadObjects(player);
			//Server.itemHandler.reloadItems(player);
			//Server.objectManager.loadObjects(player);
		}
	}

	/**
	 * Updates a non-this player's movement.
	 *
	 * @param packet      The packet.
	 * @param otherPlayer The player.
	 */
	public void updatePlayerMovement(PacketBuilder packet, Player otherPlayer) {
		/*
		 * Check which type of movement took place.
		 */
		if (otherPlayer.getSprites().getPrimarySprite() == -1) {
			/*
			 * If no movement did, check if an update is required.
			 */
			if (otherPlayer.updateRequired) {
				/*
				 * Signify that an update happened.
				 */
				packet.putBits(1, 1);

				/*
				 * Signify that there was no movement.
				 */
				packet.putBits(2, 0);
			} else {
				/*
				 * Signify that nothing changed.
				 */
				packet.putBits(1, 0);
			}
		} else if (otherPlayer.getSprites().getSecondarySprite() == -1) {
			/*
			 * The player moved but didn't run. Signify that an update is
			 * required.
			 */
			packet.putBits(1, 1);

			/*
			 * Signify we moved one tile.
			 */
			packet.putBits(2, 1);

			/*
			 * Write the primary sprite (i.e. walk direction).
			 */
			packet.putBits(3, otherPlayer.getSprites().getPrimarySprite());

			/*
			 * Write a flag indicating if a block update happened.
			 */
			packet.putBits(1, otherPlayer.updateRequired ? 1 : 0);
		} else {
			/*
			 * The player ran. Signify that an update happened.
			 */
			packet.putBits(1, 1);

			/*
			 * Signify that we moved two tiles.
			 */
			packet.putBits(2, 2);

			/*
			 * Write the primary sprite (i.e. walk direction).
			 */
			packet.putBits(3, otherPlayer.getSprites().getPrimarySprite());

			/*
			 * Write the secondary sprite (i.e. run direction).
			 */
			packet.putBits(3, otherPlayer.getSprites().getSecondarySprite());

			/*
			 * Write a flag indicating if a block update happened.
			 */
			packet.putBits(1, otherPlayer.updateRequired ? 1 : 0);
		}
	}

	/**
	 * Adds a new player.
	 *
	 * @param packet      The packet.
	 * @param otherPlayer The player.
	 */
	public void addNewPlayer(PacketBuilder packet, Player otherPlayer) {

		otherPlayer.appearanceUpdateRequired = true;
		otherPlayer.updateRequired = true;

		/*
		 * Write the player index.
		 */
		packet.putBits(11, otherPlayer.playerId);

		/*
		 * Write two flags here: the first indicates an update is required
		 * (this is always true as we add the appearance after adding a player)
		 * and the second to indicate we should discard client-side walk
		 * queues.
		 */
		packet.putBits(1, 1);
		packet.putBits(1, 1);

		/*
		 * Calculate the x and y offsets.
		 */
		int yPos = otherPlayer.getLocation().getY() - player.getLocation().getY();
		int xPos = otherPlayer.getLocation().getX() - player.getLocation().getX();

		/*
		 * Write the x and y offsets.
		 */
		packet.putBits(5, yPos);
		packet.putBits(5, xPos);
	}

	/**
	 * Updates a player.
	 *
	 * @param packet          The packet.
	 * @param otherPlayer     The other player.
	 * @param forceAppearance The force appearance flag.
	 * @param noChat          Indicates chat should not be relayed to this player.
	 */
	public void updatePlayer(PacketBuilder packet, Player otherPlayer, boolean forceAppearance, boolean noChat) {
		/*
		 * If no update is required and we don't have to force an appearance
		 * update, don't write anything.
		 */
		if (!otherPlayer.updateRequired && !forceAppearance && !otherPlayer.isChatTextUpdateRequired()) {
			return;
		}

		/*
		 * We can used the cached update block!
		 */
		//synchronized (otherPlayer) {
		//if (otherPlayer.hasCachedUpdateBlock() && otherPlayer != player && !forceAppearance && !noChat) {
		//     packet.put(otherPlayer.getCachedUpdateBlock().getPayload());
		//     return;
		//  }

			/*
			 * We have to construct and cache our own block.
			 */
		PacketBuilder block = new PacketBuilder();

			/*
			 * Calculate the bitmask.
			 */
		int mask = 0;

		// TODO mask 0x400
		if(otherPlayer.forceMovementUpdate && player.equals(otherPlayer)) {
			mask |= 0x400;
		}
		if (otherPlayer.mask100update) {
			mask |= 0x100;
		}
		if (otherPlayer.animationRequest != -1) {
			mask |= 0x8;
		}
		if (otherPlayer.forcedChatUpdateRequired) {
			mask |= 0x4;
		}
		if (otherPlayer.isChatTextUpdateRequired() && !noChat) {
			mask |= 0x80;
		}
		if (otherPlayer.faceUpdateRequired) {
			mask |= 0x1;
		}
		if (otherPlayer.isAppearanceUpdateRequired()) {
			mask |= 0x10;
		}
		if (otherPlayer.FocusPointX != -1) {
			mask |= 0x2;
		}
		if (otherPlayer.isHitUpdateRequired()) {
			mask |= 0x20;
		}
		if (otherPlayer.hitUpdateRequired2) {
			mask |= 0x200;
		}

			/*
			 * Check if the bitmask would overflow a byte.
			 */
		if (mask >= 0x100) {
				/*
				 * Write it as a short and indicate we have done so.
				 */
			mask |= 0x40;
			block.put((byte) (mask & 0xFF));
			block.put((byte) (mask >> 8));
		} else {
				/*
				 * Write it as a byte.
				 */
			block.put((byte) (mask));
		}

			/*
			 * Append the appropriate updates.
			 */
		if(otherPlayer.forceMovementUpdate && player.equals(otherPlayer)) {
			appendForceMovementMask(otherPlayer, block);
		}
		if (otherPlayer.mask100update) {
			appendGraphicsUpdate(block, otherPlayer);
		}
		if (otherPlayer.animationRequest != -1) {
			appendAnimationUpdate(block, otherPlayer);
		}
		if (otherPlayer.forcedChatUpdateRequired) {
			block.putRS2String(otherPlayer.forcedText);
		}
		if (otherPlayer.isChatTextUpdateRequired() && !noChat) {
			appendChatUpdate(block, otherPlayer);
		}
		if (otherPlayer.faceUpdateRequired) {
			block.putLEShort(otherPlayer.face);
		}
		if (otherPlayer.isAppearanceUpdateRequired()) {
			appendPlayerAppearanceUpdate(block, otherPlayer);
		}
		if (otherPlayer.FocusPointX != -1) {
			block.putLEShortA(otherPlayer.FocusPointX);
			block.putLEShort(otherPlayer.FocusPointY);
		}
		if (otherPlayer.isHitUpdateRequired()) {
			appendHitUpdate(otherPlayer, block);
		}
		if (otherPlayer.hitUpdateRequired2) {
			appendHit2Update(otherPlayer, block);
		}

			/*
			 * Convert the block builder to a packet.
			 */
		Packet blockPacket = block.toPacket();

			/*
			 * Now it is over, cache the block if we can.
			 */
		// if (otherPlayer != player && !forceAppearance && !noChat) {
		//     otherPlayer.setCachedUpdateBlock(blockPacket);
		//  }

			/*
			 * And finally append the block at the end.
			 */
		packet.put(blockPacket.getPayload());
		// }
	}

	/**
	 * Appends an animation update.
	 *
	 * @param block       The update block.
	 * @param otherPlayer The player.
	 */
	private void appendAnimationUpdate(PacketBuilder block, Player otherPlayer) {
		block.putLEShort(otherPlayer.animationRequest == -1 ? 65535 : otherPlayer.animationRequest);
		block.putByteC(otherPlayer.animationWaitCycles);
	}

	/**
	 * Appends a graphics update.
	 *
	 * @param block       The update block.
	 * @param otherPlayer The player.
	 */
	private void appendGraphicsUpdate(PacketBuilder block, Player otherPlayer) {
		block.putLEShort(otherPlayer.mask100var1);
		block.putInt(otherPlayer.mask100var2);
	}

	/**
	 * Appends a chat text update.
	 *
	 * @param packet      The packet.
	 * @param otherPlayer The player.
	 */
	private void appendChatUpdate(PacketBuilder packet, Player otherPlayer) {
		packet.putLEShort(((otherPlayer.getChatTextColor() & 0xFF) << 8) + (otherPlayer.getChatTextEffects() & 0xFF));
		packet.put((byte) Misc.getChatIcon(otherPlayer));
		packet.putByteC(otherPlayer.getChatTextSize());

		for (int ptr = otherPlayer.getChatText().length - 1; ptr >= 0; ptr--) {
			packet.put(otherPlayer.getChatText()[ptr]);
			//System.out.println(otherPlayer.getChatText()[ptr]);
		}
	}

	/**
	 * Appends an appearance update.
	 *
	 * @param packet      The packet.
	 * @param otherPlayer The player.
	 */
	private void appendPlayerAppearanceUpdate(PacketBuilder packet, Player otherPlayer) {
		PacketBuilder playerProps = new PacketBuilder();

		playerProps.putShort(otherPlayer.playerIndex);

		int genderColors = 0;
		genderColors = genderColors | (otherPlayer.appearance[0] << 0);

		boolean hasColors = false;

		for(int i = 0; i < otherPlayer.equipment.length; i++) {
			if(otherPlayer.getData().itemColors.containsKey(otherPlayer.equipment[i])) {
				hasColors = true;
				break;
			}
		}

		genderColors = genderColors | ((hasColors ? 1 : 0) << 1);

		playerProps.put((byte)genderColors); // gender and color
		playerProps.put((byte) otherPlayer.headIcon); // skull icon
		playerProps.put((byte) otherPlayer.headIconPk); // skull icon

		if (!otherPlayer.isNpc) {

			boolean fullHead = false;
			if (otherPlayer.equipment[PlayerConstants.HAT] > 1)
				fullHead = ItemUtility.showFullMask(otherPlayer.equipment[otherPlayer.playerHat]);

			boolean fullBody = false;
			if (otherPlayer.equipment[PlayerConstants.CHEST] > 1)
				fullBody = ItemUtility.showFullMask(otherPlayer.equipment[otherPlayer.playerChest]);


			// if (pNpcId < 1) {
			if (otherPlayer.equipment[PlayerConstants.HAT] > 1) {
				playerProps.putShort(32768 + otherPlayer.equipment[PlayerConstants.HAT]);
			} else {
				playerProps.put((byte) 0);
			}

			if (otherPlayer.equipment[PlayerConstants.CAPE] > 1) {
				playerProps.putShort(32768 + otherPlayer.equipment[PlayerConstants.CAPE]);
			} else {
				playerProps.put((byte) 0);
			}

			if (otherPlayer.equipment[PlayerConstants.AMULET] > 1) {
				playerProps.putShort(32768 + otherPlayer.equipment[PlayerConstants.AMULET]);
			} else {
				playerProps.put((byte) 0);
			}

			if (otherPlayer.equipment[PlayerConstants.WEAPON] > 1) {
				playerProps.putShort(32768 + otherPlayer.equipment[PlayerConstants.WEAPON]);
			} else {
				playerProps.put((byte) 0);
			}

			if (otherPlayer.equipment[PlayerConstants.CHEST] > 1) {
				playerProps.putShort(32768 + otherPlayer.equipment[PlayerConstants.CHEST]);
			} else {
				playerProps.putShort(256 + otherPlayer.appearance[2]);
			}

			if (otherPlayer.equipment[PlayerConstants.SHIELD] > 1) {
				playerProps.putShort(32768 + otherPlayer.equipment[PlayerConstants.SHIELD]);
			} else {
				playerProps.put((byte) 0);
			}

			if (!fullBody) {
				playerProps.putShort(256 + otherPlayer.appearance[3]);
			} else {
				playerProps.put((byte) 0);
			}

			if (otherPlayer.equipment[PlayerConstants.LEGS] > 1) {
				playerProps.putShort(32768 + otherPlayer.equipment[PlayerConstants.LEGS]);
			} else {
				playerProps.putShort(256 + otherPlayer.appearance[5]);
			}

			if (!fullHead) {
				playerProps.putShort(256 + otherPlayer.appearance[1]);
			} else {
				playerProps.put((byte) 0);
			}

			if (otherPlayer.equipment[PlayerConstants.HANDS] > 1) {
				playerProps.putShort(32768 + otherPlayer.equipment[PlayerConstants.HANDS]);
			} else {
				playerProps.putShort(256 + otherPlayer.appearance[4]);
			}

			if (otherPlayer.equipment[PlayerConstants.FEET] > 1) {
				playerProps.putShort(32768 + otherPlayer.equipment[PlayerConstants.FEET]);
			} else {
				playerProps.putShort(256 + otherPlayer.appearance[6]);
			}

			boolean showBeard = true;
			if (otherPlayer.equipment[PlayerConstants.HAT] > 1) {
				showBeard = ItemUtility.showBeard(otherPlayer.equipment[otherPlayer.playerHat]);
			}

			if (otherPlayer.appearance[0] != 1 && showBeard) {
				playerProps.putShort(0x100 + otherPlayer.appearance[7]);
			} else {
				playerProps.put((byte) 0);
			}
		} else {
			// playerProps.putShort(-1);
			// playerProps.putShort(pNpcId);
			playerProps.putShort(-1);
			playerProps.putShort(otherPlayer.npcId2);
		}

		if(hasColors) {
			int slots = 0;
			for(int i = 0; i < otherPlayer.equipment.length; i++) {
				if (otherPlayer.getData().itemColors.containsKey(otherPlayer.equipment[i])) {
					slots = slots | (1 << i);
				}
			}

			playerProps.putShort(slots);

			for(int i = 0; i < otherPlayer.equipment.length; i++) {
				if (otherPlayer.getData().itemColors.containsKey(otherPlayer.equipment[i])) {
					playerProps.putShort(otherPlayer.equipment[i]);
					playerProps.put((byte)1);

					Map<Integer, Integer> colors = otherPlayer.getData().itemColors.get(otherPlayer.equipment[i]);

					playerProps.put((byte)colors.size());
					for (Map.Entry<Integer, Integer> entry : colors.entrySet()) {
						playerProps.put((byte)((int)entry.getKey()));
						playerProps.putShort(entry.getValue());
					}
				}
			}
		}

		playerProps.put((byte) otherPlayer.appearance[8]); // hairc
		playerProps.put((byte) otherPlayer.appearance[9]); // torsoc
		playerProps.put((byte) otherPlayer.appearance[10]); // legc
		playerProps.put((byte) otherPlayer.appearance[11]); // feetc
		playerProps.put((byte) otherPlayer.appearance[12]); // skinc

		playerProps.putShort((short) otherPlayer.playerStandIndex); // stand
		playerProps.putShort((short) otherPlayer.playerTurnIndex); // stand turn
		playerProps.putShort((short) otherPlayer.playerWalkIndex); // walk
		playerProps.putShort((short) otherPlayer.playerTurn180Index); // turn 180
		playerProps.putShort((short) otherPlayer.playerTurn90CWIndex); // turn 90 cw
		playerProps.putShort((short) otherPlayer.playerTurn90CCWIndex); // turn 90 ccw
		playerProps.putShort((short) otherPlayer.playerRunIndex); // run

		playerProps.putLong(Misc.playerNameToInt64(otherPlayer.username));
		playerProps.putLong(Misc.playerNameToInt64(otherPlayer.getData().title));
		playerProps.putLong(Misc.playerNameToInt64(otherPlayer.getData().titleHexColor));
		playerProps.put((byte)(otherPlayer.elite ? 1 : 0));

		int level = otherPlayer.combatLevel;
		int summoningLevel = 0;
		if (otherPlayer.inWild()) {
			summoningLevel = otherPlayer.getSummoningCombat();

			if (otherPlayer.getFamiliar() == null && !Summoning.hasPoucheInInventory(otherPlayer)) {
				level -= summoningLevel;
			} else {
				// we have a familiar or pouches in our inventory show full combat level
				summoningLevel = 0;
			}
		}

		playerProps.put((byte) level);
		playerProps.put((byte) summoningLevel);
		playerProps.putShort(0);

		Packet propsPacket = playerProps.toPacket();
		packet.putByteC(propsPacket.getLength());
		packet.put(propsPacket.getPayload());
	}

	/**
	 * Updates this player's movement.
	 *
	 * @param packet The packet.
	 */
	private void updateThisPlayerMovement(PacketBuilder packet) {
		/*
		 * Check if the player is teleporting.
		 */
		if (player.didTeleport || player.mapRegionDidChange) {
			/*
			 * They are, so an update is required.
			 */
			packet.putBits(1, 1);

			/*
			 * This value indicates the player teleported.
			 */
			packet.putBits(2, 3);

			/*
			 * This is the new player height.
			 */
			packet.putBits(2, player.getLocation().getZ());

			/*
			 * This indicates that the client should discard the walking queue.
			 */
			packet.putBits(1, player.didTeleport ? 1 : 0);

			/*
			 * This flag indicates if an update block is appended.
			 */
			packet.putBits(1, player.updateRequired ? 1 : 0);

			/*
			 * These are the positions.
			 */
			packet.putBits(7, player.getLocation().getLocalY(player.getLastKnownRegion()));
			packet.putBits(7, player.getLocation().getLocalX(player.getLastKnownRegion()));
		} else {
			/*
			 * Otherwise, check if the player moved.
			 */
			if (player.getSprites().getPrimarySprite() == -1) {
				/*
				 * The player didn't move. Check if an update is required.
				 */
				if (player.updateRequired) {
					/*
					 * Signifies an update is required.
					 */
					packet.putBits(1, 1);

					/*
					 * But signifies that we didn't move.
					 */
					packet.putBits(2, 0);
				} else {
					/*
					 * Signifies that nothing changed.
					 */
					packet.putBits(1, 0);
				}
			} else {
				/*
				 * Check if the player was running.
				 */
				if (player.getSprites().getSecondarySprite() == -1) {
					/*
					 * The player walked, an update is required.
					 */
					packet.putBits(1, 1);

					/*
					 * This indicates the player only walked.
					 */
					packet.putBits(2, 1);

					/*
					 * This is the player's walking direction.
					 */
					packet.putBits(3, player.getSprites().getPrimarySprite());

					/*
					 * This flag indicates an update block is appended.
					 */
					packet.putBits(1, player.updateRequired ? 1 : 0);
				} else {
					/*
					 * The player ran, so an update is required.
					 */
					packet.putBits(1, 1);

					/*
					 * This indicates the player ran.
					 */
					packet.putBits(2, 2);

					/*
					 * This is the walking direction.
					 */
					packet.putBits(3, player.getSprites().getPrimarySprite());

					/*
					 * And this is the running direction.
					 */
					packet.putBits(3, player.getSprites().getSecondarySprite());

					/*
					 * And this flag indicates an update block is appended.
					 */
					packet.putBits(1, player.updateRequired ? 1 : 0);
				}
			}
		}
	}

}

