package com.zionscape.server.net;

import com.zionscape.server.model.players.Player;
import org.jboss.netty.channel.Channel;

public class Session {

	private final Channel channel;
	private Player client;

	public Session(Channel channel) {
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

	public Player getClient() {
		return client;
	}

	public void setClient(Player client) {
		this.client = client;
	}
}
