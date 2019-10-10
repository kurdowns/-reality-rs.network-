package net.crandor;

import java.util.HashMap;
import java.util.Map;

public class ChatMessageManager {
	
	static Map<Integer, ChatMessageGroup> messageGroups = new HashMap<>();
	static NodeSubList messageQueue = new NodeSubList();
	static NodeCache messageHashtable = new NodeCache();
	static int messageCount = 0;
	
	static int getPrevMessageId(int id) {
		ChatMessage chatMessage = (ChatMessage) messageHashtable.findNodeByID((long) id);
		if (chatMessage == null) {
			return -1;
		}
		if (messageQueue.head == chatMessage.nextNodeSub) {
			return -1;
		}
		return ((ChatMessage) chatMessage.nextNodeSub).uid;
	}
	
	static void clear() {
		messageGroups.clear();
		messageQueue.clear();
		messageHashtable.clear();
		messageCount = 0;
	}

	static void appendMessage(int type, String message, String title, String name) {
		ChatMessageGroup group = messageGroups.get(type);
		if (group == null) {
			group = new ChatMessageGroup();
			messageGroups.put(type, group);
		}

		ChatMessage chatMessage = group.append(type, message, title, name);
		messageHashtable.removeFromCache(chatMessage, chatMessage.uid);
		messageQueue.insertHead(chatMessage);
	}
	
	static int getHighestId() {
		int highestId = -1;
		for (int type = 0; type < 10; type++) {
			ChatMessageGroup messageGroup = messageGroups.get(type);
			if (messageGroup != null && messageGroup.getPos() > 0) {
				ChatMessage chatMessage = messageGroup.getMessage(0);
				int id = -1;
				if (chatMessage != null) {
					id = chatMessage.uid;
				}
				if (id > highestId) {
					highestId = id;
				}
			}
		}
		return highestId;
	}

}
