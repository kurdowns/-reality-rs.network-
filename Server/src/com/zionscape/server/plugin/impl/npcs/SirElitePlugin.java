package com.zionscape.server.plugin.impl.npcs;

import com.zionscape.server.Server;
import com.zionscape.server.events.Listener;
import com.zionscape.server.events.impl.DialogueContinueEvent;
import com.zionscape.server.events.impl.DialogueOptionEvent;
import com.zionscape.server.events.impl.NpcClickedEvent;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerConstants;
import com.zionscape.server.model.players.dialogue.CloseDialogue;
import com.zionscape.server.plugin.Plugin;
import com.zionscape.server.util.DatabaseUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class SirElitePlugin implements Listener, Plugin {

    private static final int ID = 605;

    @Override
    public void onDialogueContinue(DialogueContinueEvent event) {
        Player player = event.getPlayer();

        if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(SirElitePlugin.class)) {
            return;
        }

        event.setHandled(true);

        switch (player.getCurrentDialogueId()) {
            case 1:
                player.getPA().sendOptions(Optional.of("Would you like to switch to elite mode?"), "Yes", "No", "");
                player.setCurrentDialogueId(2);
                break;
            case 3:
                player.getPA().sendOptions(Optional.of("Would you like to switch to normal mode?"), "Yes", "No", "");
                player.setCurrentDialogueId(4);
                break;
        }
    }

    @Override
    public void onDialogueOption(DialogueOptionEvent event) {
        Player player = event.getPlayer();

        if (player.getDialogueOwner() == null || !player.getDialogueOwner().equals(SirElitePlugin.class)) {
            return;
        }

        event.setHandled(true);

        if(player.getCurrentDialogueId() == 2) {
            switch (event.getOption()) {
                case 1:
                    if (player.elite) {
                        player.setDialogueOwner(CloseDialogue.class);
                        player.getPA().sendNpcChat(ID, "You already are an elite user!");
                        return;
                    }
                    for (int i : player.equipment) {
                        if (i > 0) {
                            player.getPA().sendNpcChat(ID, "You must remove all equipped items to do this.");
                            player.setDialogueOwner(CloseDialogue.class);
                            return;
                        }
                    }

                    if (!player.getItems().playerHasItem(995, 20_000_000)) {
                        player.getPA().sendNpcChat(ID, "You do not have 20M gp.");
                        player.setDialogueOwner(CloseDialogue.class);
                        return;
                    }

                    player.getItems().deleteItem(995, 20_000_000);

                    for (int i = 0; i < player.level.length; i++) {
                        player.level[i] = 1;
                        player.xp[i] = 0;
                    }

                    player.level[PlayerConstants.HITPOINTS] = 10;
                    player.xp[PlayerConstants.HITPOINTS] = 1300;

                    for (int i = 0; i < player.level.length; i++) {
                        player.getPA().refreshSkill(i);
                    }
                    player.getPA().sendFrame126("Total Level: " + player.getPA().getTotalLevel(), 3984);

                    player.elite = true;

                    Server.submitWork(() -> {
                        try (java.sql.Connection connection = DatabaseUtil.getConnection()) {
                            try (PreparedStatement ps = connection.prepareStatement("UPDATE players SET elite = ? WHERE id = ?")) {
                                ps.setBoolean(1, player.elite);
                                ps.setInt(2, player.getDatabaseId());
                                ps.execute();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });

                    player.getPA().sendNpcChat(ID, "I have reset your stats and set your account to elite mode.");
                    player.setDialogueOwner(CloseDialogue.class);
                    break;
                case 2:
                case 3:
                    player.resetDialogue();
                    break;
            }
        } else if(player.getCurrentDialogueId() == 4) {
            switch (event.getOption()) {
                case 1:
                    if (!player.elite) {
                        player.setDialogueOwner(CloseDialogue.class);
                        player.getPA().sendNpcChat(ID, "You already are a normal user!");
                        return;
                    }

                    if (!player.getItems().playerHasItem(995, 20_000_000)) {
                        player.getPA().sendNpcChat(ID, "You do not have 20M gp.");
                        player.setDialogueOwner(CloseDialogue.class);
                        return;
                    }

                    player.getItems().deleteItem(995, 20_000_000);

                    player.elite = false;

                    Server.submitWork(() -> {
                        try (java.sql.Connection connection = DatabaseUtil.getConnection()) {
                            try (PreparedStatement ps = connection.prepareStatement("UPDATE players SET elite = ? WHERE id = ?")) {
                                ps.setBoolean(1, player.elite);
                                ps.setInt(2, player.getDatabaseId());
                                ps.execute();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });

                    player.getPA().sendNpcChat(ID, "I have set your account to normal mode.");
                    player.setDialogueOwner(CloseDialogue.class);
                    break;
                case 2:
                case 3:
                    player.resetDialogue();
                    break;
            }
        }
    }

    @Override
    public void onNpcClicked(NpcClickedEvent event) {
        Player player = event.getPlayer();
        NPC npc = event.getNpc();
        if (npc.type == ID) {
            event.setHandled(true);
            if(!player.elite) {
                player.setDialogueOwner(SirElitePlugin.class);
                player.setCurrentDialogueId(1);
                player.getPA().sendNpcChat(ID, "Hello " + player.username + ", I can reset your stats and change your account mode to elite for 20M.");
            } else {
                player.setDialogueOwner(SirElitePlugin.class);
                player.setCurrentDialogueId(3);
                player.getPA().sendNpcChat(ID, "Hello " + player.username + ", I can set your account to normal mode for 20m.");
            }
        }
    }

}
