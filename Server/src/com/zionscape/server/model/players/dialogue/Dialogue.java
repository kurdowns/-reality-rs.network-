package com.zionscape.server.model.players.dialogue;

public class Dialogue {

	private int id;
	private DialogueType type;
	private int npcId;
	private int next;
	private String message;
	private String[] options;
	private DialogueAnimations animation = DialogueAnimations.TALKING;
	private Dialogue nextDialogue;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DialogueType getType() {
		return type;
	}

	public void setType(DialogueType type) {
		this.type = type;
	}

	public int getNpcId() {
		return npcId;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String[] getOptions() {
		return options;
	}

	public void setOptions(String[] options) {
		this.options = options;
	}

	public DialogueAnimations getAnimation() {
		return animation;
	}

	public void setAnimation(DialogueAnimations animation) {
		this.animation = animation;
	}

	public Dialogue getNextDialogue() {
		return nextDialogue;
	}

	public void setNextDialogue(Dialogue nextDialogue) {
		this.nextDialogue = nextDialogue;
	}
}
