package com.zionscape.server.model.players.commands.impl;

import com.zionscape.server.Server;
import com.zionscape.server.model.clan.Clan;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.commands.Command;
import com.zionscape.server.util.ISAACRandomGen;

public class Bot implements Command {

	@Override
	public void execute(Player client, String message) {

		if (client.rights < 3) {
			return;
		}

		for (int i = 0; i < 100; i++) {
			Player cl = new Player(null, -1, true);
			cl.username = "hi " + i;
			cl.isActive = true;
			cl.absX = client.absX;
			cl.absY = client.absY;
			cl.heightLevel = client.heightLevel;
			cl.outStream.packetEncryption = new ISAACRandomGen(new int[10]);

			PlayerHandler.newPlayerClient(cl);

			Clan clan = Server.clanManager.getClan("stuart1");
			long start = System.currentTimeMillis();
			if (clan != null) {
				clan.addMember(cl);
			}
			System.out.println((System.currentTimeMillis() - start) + " ms");
		}
	}

	@Override
	public String getCommandString() {
		return "bot";
	}

}
