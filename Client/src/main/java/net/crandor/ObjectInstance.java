package net.crandor;

final class ObjectInstance extends Entity {

	private final int[] alternativeIDS;

	@Override
	public Model getRotatedModel() {
		if (sequence != null) {
			int i = Client.loopCycle - oldCycle;
			if (i > 100 && sequence.padding > 0) {
				i = 100;
			}
			while (i > sequence.delays[currentFrame]) {
				i -= sequence.delays[currentFrame];
				currentFrame++;
				if (currentFrame >= sequence.frameCount) {
					currentFrame -= sequence.padding;
					if (currentFrame < 0 || currentFrame >= sequence.frameCount) {
						sequence = null;
						break;
					}
				}
				nextFrame = currentFrame + 1;
				if (nextFrame >= sequence.frameCount) {
					nextFrame -= sequence.padding;
					if (nextFrame < 0 || nextFrame >= sequence.frameCount) {
						nextFrame = -1;
					}
				}
			}
			delay = i;
			oldCycle = Client.loopCycle - i;
		}
		ObjectDefinition class46 = ObjectDefinition.forID(objectId);
		if (alternativeIDS != null) {
			class46 = method457();
		}
		if (class46 == null) {
			return null;
		}
		return class46.generateModel(anInt1611, anInt1612, anInt1603, anInt1604, anInt1605, anInt1606, currentFrame, nextFrame, delay, sequence);
	}

	private final int varBitId;
	private final int varpId;
	private final int anInt1603;
	private final int anInt1604;
	private final int anInt1605;
	private final int anInt1606;
	private final int objectId;
	private final int anInt1611;
	private final int anInt1612;
	private int currentFrame;
	private int nextFrame;
	private Animation sequence;
	private int oldCycle;
	private int delay;

	public ObjectInstance(int i, int j, int k, int l, int i1, int j1, int k1, int animationId, boolean flag) {
		objectId = i;
		anInt1611 = k;
		anInt1612 = j;
		anInt1603 = j1;
		anInt1604 = l;
		anInt1605 = i1;
		anInt1606 = k1;
		if (animationId != -1) {
			sequence = Animation.anims[animationId];
			if (flag && sequence.padding != -1) {
				currentFrame = (int) (Math.random() * sequence.frameCount);
				delay = (int) (Math.random() * sequence.delays[currentFrame]) + 1;
			} else {
				currentFrame = 0;
				delay = 1;
			}
			nextFrame = currentFrame + 1;
			if (nextFrame < 0 || nextFrame >= sequence.frameCount) {
				nextFrame = -1;
			}
			oldCycle = Client.loopCycle - delay;
		}
		ObjectDefinition class46 = ObjectDefinition.forID(objectId);
		varBitId = class46.varBitId;
		varpId = class46.varpId;
		alternativeIDS = class46.alternativeIDS;
	}

	private ObjectDefinition method457() {
		int i = -1;
		if (varBitId != -1) {
			i = VarBit.getValue(varBitId);
		} else if (varpId != -1)
			i = Client.playerVariables[varpId];
		if (i < 0 || i >= alternativeIDS.length || alternativeIDS[i] == -1)
			return null;
		else
			return ObjectDefinition.forID(alternativeIDS[i]);
	}
}
