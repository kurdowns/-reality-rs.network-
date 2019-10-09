package com.zionscape.server.model.players.chat;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.util.Misc;
import com.zionscape.server.util.Stream;

/**
 * @author Omicron
 */
public final class PrivateMessaging {

	private final Player player;
	public Status status = Status.LOADING;

	public PrivateMessaging(Player player) {
		this.player = player;
	}

	void updateWorld(long encodedName, int world) {
		if (!player.getChat().hasFriend(encodedName)) {
			return;
		}
		if (world != 0 && player.rights == 0) {
			Player player2 = PlayerHandler.getPlayer(encodedName);
			Chat.Display display = player2.getChat().getPrivateDisplay();
			if (player2.getChat().hasIgnore(player.encodedName)) {
				world = 0;
			} else {
				if (display == Chat.Display.FRIENDS) {
					if (!player2.getChat().hasFriend(player.encodedName)) {
						world = 0;
					}
				} else if (display == Chat.Display.OFF) {
					world = 0;
				}
			}
		}
		if (world != 0) {
			world += 9;
		}
		// world += 1;
		Stream stream = player.getOutStream();
		stream.createFrame(50);
		stream.writeQWord(encodedName);
		stream.writeByte(world);
		player.flushOutStream();
	}

	public void sendMessage(long encodedName, byte[] message) {
		Player player2 = PlayerHandler.getPlayer(encodedName);
		if (player2 == null) {
			player.sendMessage("That player is currently offline.");
			return;
		}
		if (player.rights == 0) {
			if (player2.getChat().getPrivateDisplay() == Chat.Display.FRIENDS) {
				if (!player2.getChat().hasFriend(player.encodedName)) {
					player.sendMessage("That player is currently offline.");
					return;
				}
			} else if (player2.getChat().getPrivateDisplay() == Chat.Display.OFF) {
				player.sendMessage("That player is currently offline.");
				return;
			}
		}
		Stream stream = player2.getOutStream();
		stream.createFrameVarSize(196);
		stream.writeQWord(player.encodedName);
		stream.writeDWord(0);
		stream.writeByte(Misc.getChatIcon(player));
		stream.writeByte(player.elite ? 1 : 0);
		stream.writeBytes(message, message.length, 0);
		stream.endFrameVarSize();
		player2.flushOutStream();
	}

	public void updateStatus(Status status) {
		this.status = status;
		Stream stream = player.getOutStream();
		stream.createFrame(221);
		stream.writeByte(status.ordinal());
		player.flushOutStream();
	}

	public enum Status {
		LOADING, CONNECTING, CONNECTED
	}
}
