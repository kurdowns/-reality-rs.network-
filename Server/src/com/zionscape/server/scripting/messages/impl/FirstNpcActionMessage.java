package com.zionscape.server.scripting.messages.impl;

import com.zionscape.server.model.npcs.NPC;

/**
 * @author Stuart
 */
public final class FirstNpcActionMessage extends NpcActionMessage {

	public FirstNpcActionMessage(NPC npc) {
		super(npc, 1);
	}

}