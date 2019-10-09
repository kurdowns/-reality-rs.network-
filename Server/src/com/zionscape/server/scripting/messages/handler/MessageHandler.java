package com.zionscape.server.scripting.messages.handler;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.scripting.messages.Message;

/**
 * Handles messages received from the client.
 *
 * @param <M> The type of message handled by this class.
 * @author Graham
 */
public abstract class MessageHandler<M extends Message> {

	/**
	 * Handles a message.
	 *
	 * @param ctx     The context.
	 * @param player  The player.
	 * @param message The message.
	 */
	public abstract void handle(MessageHandlerContext ctx, Player player, M message);

}