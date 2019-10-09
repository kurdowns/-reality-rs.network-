package com.zionscape.server.model.players.chat;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.PlayerSave;
import com.zionscape.server.util.Misc;
import com.zionscape.server.util.Stream;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Omicron
 */
public final class Chat {

	private final Player player;
	private final PrivateMessaging privateMessaging;
	private final List<Long> friends = new ArrayList<>();
	private final List<Long> ignores = new ArrayList<>();
	private Display publicDisplay = Display.ON;
	private Display privateDisplay = Display.ON;
	private Display tradeDisplay = Display.ON;

	public Chat(Player player) {
		this.player = player;
		this.privateMessaging = new PrivateMessaging(player);
	}

	public void addFriend(long encodedName) {

		if (friends.size() >= 200) {
			player.sendMessage("Your friends list is full.");
			return;
		}

		if (friends.contains(encodedName)) {
			player.sendMessage(Misc.longToString(encodedName) + " is already on your friends list.");
			return;
		}
		if (friends.add(encodedName)) {
			privateMessaging.updateWorld(encodedName, PlayerHandler.getPlayer(encodedName) != null ? 1 : 0);
		}
	}

	public void removeFriend(long encodedName) {
		if (!friends.remove(encodedName)) {
			return;
		}
		Player player2 = PlayerHandler.getPlayer(encodedName);
		if (player2 != null) {
			player2.getChat().getPrivateMessaging().updateWorld(player.encodedName, 1);
		}
	}

	public void addIgnore(long encodedName) {
		if (ignores.size() >= 100) {
			player.sendMessage("Your ignores list is full.");
			return;
		}
		if (friends.contains(encodedName)) {
			player.sendMessage("Please remove the player from your friends list first.");
			return;
		}
		if (ignores.contains(encodedName)) {
			player.sendMessage("This player is already on your ignore list.");
			return;
		}
		if (ignores.add(encodedName)) {
			Player player2 = PlayerHandler.getPlayer(encodedName);
			if (player2 != null) {
				player2.getChat().getPrivateMessaging().updateWorld(player.encodedName, 1);
			}
		}
	}

	public void removeIgnore(long encodedName) {
		if (!ignores.remove(encodedName)) {
			return;
		}
		Player player2 = PlayerHandler.getPlayer(encodedName);
		if (player2 != null) {
			player2.getChat().getPrivateMessaging().updateWorld(player.encodedName, 1);
		}
	}

	private void updateChatOptions() {
		Stream stream = player.getOutStream();
		stream.createFrame(206);
		stream.writeByte(publicDisplay.ordinal());
		stream.writeByte(privateDisplay.ordinal());
		stream.writeByte(tradeDisplay.ordinal());
		player.flushOutStream();
	}

	public boolean hasIgnore(long encodedName) {
		return ignores.contains(encodedName);
	}

	public boolean hasFriend(long encodedName) {
		return friends.contains(encodedName);
	}

	public void initialize() {
		this.updateChatOptions();
		for (int i = 0; i < friends.size(); i++) {
			long friend = friends.get(i);
			Player player2 = PlayerHandler.getPlayer(friend);
			privateMessaging.updateWorld(friend, player2 != null ? 1 : 0);
		}
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			Player player2 = PlayerHandler.players[i];
			if (player2 == null) {
				continue;
			}
			player2.getChat().getPrivateMessaging().updateWorld(player.encodedName, 1);
		}
		Stream stream = player.getOutStream();
		stream.createFrameVarSizeWord(214);
		for (int i = 0; i < ignores.size(); i++) {
			stream.writeQWord(ignores.get(i));
		}
		stream.endFrameVarSizeWord();
		player.flushOutStream();
	}

	public void logout() {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			Player player2 = PlayerHandler.players[i];
			if (player2 == null) {
				continue;
			}
			player2.getChat().getPrivateMessaging().updateWorld(player.encodedName, 0);
		}
	}

	public void deserialize(String token, String token2, String[] token3) {
		if (token.equals("public")) {
			publicDisplay = Display.values()[Integer.parseInt(token2)];
		} else if (token.equals("private")) {
			privateDisplay = Display.values()[Integer.parseInt(token2)];
		} else if (token.equals("trade")) {
			tradeDisplay = Display.values()[Integer.parseInt(token2)];
		} else if (token.equals("friend")) {
			friends.add(Long.parseLong(token2));
		} else if (token.equals("ignore")) {
			ignores.add(Long.parseLong(token2));
		}
	}

	public void serialize(PlayerSave.PlayerSaveWriter writer) {
		writer.write("public = ");
		writer.write(Integer.toString(publicDisplay.ordinal()));
		writer.newLine();
		writer.write("private = ");
		writer.write(Integer.toString(privateDisplay.ordinal()));
		writer.newLine();
		writer.write("trade = ");
		writer.write(Integer.toString(tradeDisplay.ordinal()));
		writer.newLine();
		for (int i = 0; i < friends.size(); i++) {
			writer.write("friend = ");
			writer.write(String.valueOf(friends.get(i)));
			writer.newLine();
		}
		for (int i = 0; i < ignores.size(); i++) {
			writer.write("ignore = ");
			writer.write(String.valueOf(ignores.get(i)));
			writer.newLine();
		}
	}

	public boolean hasFriend(String name) {
		return friends.contains(Misc.playerNameToInt64(name));
	}

	public PrivateMessaging getPrivateMessaging() {
		return privateMessaging;
	}

	public Display getPublicDisplay() {
		return publicDisplay;
	}

	public void setPublicDisplay(Display publicDisplay) {
		this.publicDisplay = publicDisplay;
	}

	public Display getPrivateDisplay() {
		return privateDisplay;
	}

	public void setPrivateDisplay(Display privateDisplay) {
		this.privateDisplay = privateDisplay;
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			Player player2 = PlayerHandler.players[i];
			if (player2 == null) {
				continue;
			}
			player2.getChat().getPrivateMessaging().updateWorld(player.encodedName, 1);
		}
	}

	public Display getTradeDisplay() {
		return tradeDisplay;
	}

	public void setTradeDisplay(Display tradeDisplay) {
		this.tradeDisplay = tradeDisplay;
	}

	public enum Display {
		ON, FRIENDS, OFF, HIDDEN
	}

}
