package net.crandor;

final class StillGraphic extends Entity {

	public final int plane;
	public final int x;
	public final int y;

	@Override
	public Model getRotatedModel() {
		Model model;
		if (!finishedAnimating) {
			model = spotAnim.getModel(currentFrame, nextFrame, currentFrameDelay);
		} else {
			model = spotAnim.getModel(-1, -1, -1);
		}
		if (model == null)
			return null;
		return model;
	}

	public final int anInt1563;
	public final int anInt1564;
	private final SpotAnim spotAnim;
	public boolean finishedAnimating;
	private int currentFrame;
	private int currentFrameDelay;
	private int nextFrame;

	public StillGraphic(int i, int j, int l, int i1, int j1, int k1, int l1) {
		spotAnim = SpotAnim.cache[i1];
		plane = i;
		x = l1;
		y = k1;
		anInt1563 = j1;
		anInt1564 = j + l;
		finishedAnimating = spotAnim.sequence == null;
	}

	public void animationStep(int timePassed) {
		for (currentFrameDelay += timePassed; currentFrameDelay > spotAnim.sequence.delays[currentFrame];) {
			currentFrameDelay -= spotAnim.sequence.delays[currentFrame] + 1;
			currentFrame++;
			if (currentFrame >= spotAnim.sequence.frameCount && (currentFrame < 0 || currentFrame >= spotAnim.sequence.frameCount)) {
				currentFrame = 0;
				finishedAnimating = true;
			}
			nextFrame = currentFrame + 1;
			if (nextFrame >= spotAnim.sequence.frameCount) {
				nextFrame = -1;
			}
		}
	}
}
