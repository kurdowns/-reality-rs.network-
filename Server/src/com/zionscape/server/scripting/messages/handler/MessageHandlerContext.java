package com.zionscape.server.scripting.messages.handler;

/**
 * Provides operations specific to a {@link MessageHandler} in a {@link MessageHandlerChain}.
 *
 * @author Graham
 */
public abstract class MessageHandlerContext {

	/**
	 * Breaks the handler chain.
	 */
	public abstract void breakHandlerChain();

}