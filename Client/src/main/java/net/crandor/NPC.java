package net.crandor;

final class NPC extends Mobile {

	NpcDefinition desc;

	private Model method450() {
		int currAnimFrame = -1;
		int nextAnimFrame = -1;
		int currCycle = -1;
		int nextCycle = -1;
		if (super.animId >= 0 && super.animDelay == 0) {
			final Animation seq = Animation.anims[super.animId];
			if (seq.tween && Settings.tweening && super.animNextFrame != -1) {
				nextAnimFrame = seq.frames[super.animNextFrame];
				currCycle = seq.delays[super.animFrame];
				nextCycle = super.animFrameDelay;
			}
			currAnimFrame = seq.frames[super.animFrame];
			int idleAnimFrame = -1;
			int idleAnimNextFrame = -1;
			int idleAnimDelay = -1;
			int idleAnimFrameDelay = 0;
			if (super.idleAnimId >= 0 && super.idleAnimId != super.standAnimIndex) {
				Animation idleSeq = Animation.anims[super.idleAnimId];
				idleAnimFrame = idleSeq.frames[super.idleAnimFrame];
				if (idleSeq.tween && Settings.tweening && super.idleAnimNextFrame != -1) {
					idleAnimNextFrame = idleSeq.frames[super.idleAnimNextFrame];
					idleAnimDelay = super.idleAnimFrameDelay;
					idleAnimFrameDelay = idleSeq.frames[super.idleAnimFrame];
				}
			}

			return desc.getAnimatedModel(idleAnimFrame, idleAnimNextFrame, idleAnimDelay, idleAnimFrameDelay, currAnimFrame, nextAnimFrame, currCycle, nextCycle, seq.anIntArray357);
		}
		if (super.idleAnimId >= 0) {
			final Animation seq = Animation.anims[super.idleAnimId];
			currAnimFrame = seq.frames[super.idleAnimFrame];
			if (seq.tween && Settings.tweening && super.idleAnimNextFrame != -1) {
				nextAnimFrame = seq.frames[super.idleAnimNextFrame];
				currCycle = seq.delays[super.idleAnimFrame];
				nextCycle = super.idleAnimFrameDelay;
			}
		}

		return desc.getAnimatedModel(-1, -1, -1, -1, currAnimFrame, nextAnimFrame, currCycle, nextCycle, null);
	}

	@Override
	public Model getSpotAnimModel() {
		if (super.spotAnimId != -1 && super.spotAnimFrame != -1) {
			SpotAnim spotAnim = SpotAnim.cache[super.spotAnimId];
			Model model_1 = spotAnim.getModel(super.spotAnimFrame, super.spotAnimNextFrame, super.spotAnimFrameDelay);
			if (model_1 != null) {
				model_1.translate(0, -super.anInt1524, 0);
			}
			return model_1;
		}
		return null;
	}

	@Override
	public Model getRotatedModel() {
		if (desc == null)
			return null;
		Model model = method450();
		if (model == null)
			return null;
		model.calculateDiagonals();
		super.height = model.highestY;
		if (desc.npcSize == 1)
			model.rendersWithinOneTile = true;
		return model;
	}

	@Override
	public boolean isVisible() {
		return desc != null;
	}
}
