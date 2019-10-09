package com.zionscape.server.model.players.dialogue;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zionscape.server.model.players.Player;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DialogueHelper {

	protected static final short SEND_NO_EMOTE = -1;
	protected static final short CONFUSED = 9827;
	protected static final short CALM = 9760;
	protected static final short CRYING = 9765;
	protected static final short SHY = 9770;
	protected static final short SAD = 9775;
	protected static final short SCARED = 9780;
	protected static final short MAD = 9785;
	protected static final short ANGRY = 9790;
	protected static final short CRAZY = 9795;
	protected static final short CRAZY_2 = 9800;
	protected static final short SAYS_NOTHING = 9805;
	protected static final short JUST_TALKS_NO_ANIMATION = 9810;
	protected static final short YEAH = 9815;
	protected static final short DISGUSTED = 9820;
	protected static final short NOWAY = 9823;
	protected static final short DRUNK = 9835;
	protected static final short LAUGH = 9840;
	protected static final short HEAD_SWAY_TALK = 9845;
	protected static final short HAPPY_TALK = 9850;
	protected static final short STIFF = 9855;
	protected static final short STIFF_EYES_MOVE = 9860;
	protected static final short PRIDEFULL = 9865;
	protected static final short DEMENTED = 9870;
	private static final Gson gson = new GsonBuilder().create();

	@SuppressWarnings("serial")
	public static List<Dialogue> getDialogues(String fileName) throws IOException {
		return gson.fromJson(FileUtils.readFileToString(new File("./config/dialogues/" + fileName + ".json")),
				new TypeToken<List<Dialogue>>() {
				}.getType());
	}

	public static void sendDialogue(Player player, Dialogue dialogue) {
		if (dialogue.getType() == DialogueType.NPC) {
			player.getPA().sendNpcChat(dialogue.getNpcId(), dialogue.getAnimation().getId(), dialogue.getMessage().replace("*username*", player.username));
		} else if (dialogue.getType() == DialogueType.PLAYER) {
			player.getPA().sendPlayerChat(dialogue.getAnimation().getId(), dialogue.getMessage().replace("*username*", player.username));
		} else if (dialogue.getType() == DialogueType.OPTION) {
			player.getPA().sendOptions(dialogue.getOptions());
		} else if (dialogue.getType() == DialogueType.STATEMENT) {
			player.getPA().sendStatement(dialogue.getMessage());
		}

		player.setCurrentDialogueId(dialogue.getId());
		player.setNextDialogueId(dialogue.getNext());
	}

}