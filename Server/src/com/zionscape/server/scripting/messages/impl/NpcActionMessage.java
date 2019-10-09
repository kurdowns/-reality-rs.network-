package com.zionscape.server.scripting.messages.impl;

import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.scripting.messages.Message;

/**
 * @author Stuart
 */
public abstract class NpcActionMessage extends Message {

	private final NPC npc;
	private final int option;

	public NpcActionMessage(NPC npc, int option) {
		this.npc = npc;
		this.option = option;
	}

	public NPC getNpc() {
		return npc;
	}

	public int getOption() {
		return option;
	}

}