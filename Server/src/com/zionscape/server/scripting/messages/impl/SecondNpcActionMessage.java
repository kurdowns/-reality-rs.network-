package com.zionscape.server.scripting.messages.impl;

import com.zionscape.server.model.npcs.NPC;

/**
 * @author Stuart
 */
public final class SecondNpcActionMessage extends NpcActionMessage {

	public SecondNpcActionMessage(NPC npc) {
		super(npc, 2);
	}
}
