package com.zionscape.server.scripting.messages.impl;

import com.zionscape.server.model.npcs.NPC;

/**
 * @author Stuart
 */
public final class ThirdNpcActionMessage extends NpcActionMessage {

	public ThirdNpcActionMessage(NPC npc) {
		super(npc, 3);
	}

}