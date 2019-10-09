package com.zionscape.server.net.packets;

import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.net.PacketType;
import com.zionscape.server.util.Stream;

public class ClassPath implements PacketType {

    @Override
    public void processPacket(Player c, int packetType, int packetSize, Stream stream) {
        c.getData().lastClassPath = stream.readString();

     //   if(!c.isDoingTutorial() && !c.username.equalsIgnoreCase("test123") && c.rights == 0 && !c.connectedFrom.equalsIgnoreCase("127.0.0.1") && !c.getData().lastClassPath.endsWith("play-draynor.jar")) {
         //   PlayerHandler.staffYell("[WARNING]" + c.username + " does not have a normal class path, they have been jailed.");
        //    PlayerHandler.staffYell("[WARNING] contact an owner or developer immediately.");

        //    c.jailTimer = 2419200000L;
         //   c.sendMessage("You have been jailed a member of staff will be with you soon.");
       // }
    }

}
