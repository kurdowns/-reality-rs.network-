package net.crandor;

public class ChatMessageGroup {

	private ChatMessage[] messages = new ChatMessage[100];
	private int pos;

	ChatMessage append(int type, String message, String title, String name) {
		ChatMessage last = messages[99];

		for (int i = pos; i > 0; i--) {
			if (i != 100) {
				messages[i] = messages[i - 1];
			}
		}

		if (last == null) {
			last = new ChatMessage(type, message, title, name);
		} else {
			last.unlink();
			last.unlinkSub();
			last.set(type, message, title, name);
		}

		messages[0] = last;
		if (pos < 100) {
			pos++;
		}
		return last;
	}

	int getPos() {
		return pos;
	}

	ChatMessage getMessage(int id) {
		return id >= 0 && id < pos ? messages[id] : null;
	}

}
