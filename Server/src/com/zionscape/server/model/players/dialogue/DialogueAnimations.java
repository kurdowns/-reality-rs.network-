package com.zionscape.server.model.players.dialogue;

public enum DialogueAnimations {

	NONE(-1), CONFUSED(9827), CALM(9760), CRYING(9765), SHY(9770), SAD(9775), SCARED(9780), MAD(9785), ANGRY(9790), CRAZY(
			9695), CRAZY_2(9800), SAYS_NOTHING(9805), TALKING(9810), YEAH(9815), DISGUSTED(9820), NOWAY(9823), DRUNK(
			9835), LAUGH(9840), HEAD_SWAY_TALK(9845), HAPPY_TALK(9850), STIFF(9855), STIFF_EYES_MOVE(9860), PROUD(9865), DEMENTED(
			9870);

	private final int id;

	DialogueAnimations(int animationId) {
		this.id = animationId;
	}

	public int getId() {
		return id;
	}

}
