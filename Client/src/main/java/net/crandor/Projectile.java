package net.crandor;

final class Projectile extends Entity {

	public final int delayTime;
	public final int speedTime;
	public final int engHeight;

	@Override
	public Model getRotatedModel() {
		Model model = spotAnim.getModel(currentFrame, nextFrame, currentFrameDelay);
		if (model == null)
			return null;
		model.method474(anInt1596);
		return model;
	}

	public final int lockOn;
	public final int plane;
	private final int anInt1580;
	private final int anInt1581;
	private final int anInt1582;
	private final int slope;
	private final int radius;
	private final SpotAnim spotAnim;
	public double aDouble1585;
	public double aDouble1586;
	public double aDouble1587;
	public int anInt1595;
	private double aDouble1574;
	private double aDouble1575;
	private double aDouble1576;
	private double aDouble1577;
	private double aDouble1578;
	private boolean aBoolean1579;
	private int currentFrame;
	private int nextFrame;
	private int currentFrameDelay;
	private int anInt1596;

	public Projectile(int i, int j, int l, int i1, int j1, int k1, int l1, int i2, int j2, int k2, int l2) {
		aBoolean1579 = false;
		spotAnim = SpotAnim.cache[l2];
		plane = k1;
		anInt1580 = j2;
		anInt1581 = i2;
		anInt1582 = l1;
		delayTime = l;
		speedTime = i1;
		slope = i;
		radius = j1;
		lockOn = k2;
		engHeight = j;
		aBoolean1579 = false;
	}

	public void method455(int currentTime, int offsetX, int drawHeight, int offsetY) {
		if (!aBoolean1579) {
			double d = offsetY - anInt1580;
			double d2 = offsetX - anInt1581;
			double d3 = Math.sqrt(d * d + d2 * d2);
			aDouble1585 = anInt1580 + (d * radius) / d3;
			aDouble1586 = anInt1581 + (d2 * radius) / d3;
			aDouble1587 = anInt1582;
		}
		double d1 = (speedTime + 1) - currentTime;
		aDouble1574 = (offsetY - aDouble1585) / d1;
		aDouble1575 = (offsetX - aDouble1586) / d1;
		aDouble1576 = Math.sqrt(aDouble1574 * aDouble1574 + aDouble1575 * aDouble1575);
		if (!aBoolean1579)
			aDouble1577 = -aDouble1576 * Math.tan(slope * 0.02454369);
		aDouble1578 = (2D * (drawHeight - aDouble1587 - aDouble1577 * d1)) / (d1 * d1);
	}

	public void method456(int timePassed) {
		aBoolean1579 = true;
		aDouble1585 += aDouble1574 * timePassed;
		aDouble1586 += aDouble1575 * timePassed;
		aDouble1587 += aDouble1577 * timePassed + 0.5D * aDouble1578 * timePassed * timePassed;
		aDouble1577 += aDouble1578 * timePassed;
		anInt1595 = (int) (Math.atan2(aDouble1574, aDouble1575) * 2607.5945876176133) + 8192 & 0x3fff;
		anInt1596 = (int) (Math.atan2(aDouble1577, aDouble1576) * 2607.5945876176133) & 0x3fff;
		if (spotAnim.sequence != null) {
			currentFrameDelay += timePassed;
			while (currentFrameDelay > spotAnim.sequence.delays[currentFrame]) {
				currentFrameDelay -= spotAnim.sequence.delays[currentFrame];
				currentFrame++;
				if (currentFrame >= spotAnim.sequence.frameCount) {
					currentFrame -= spotAnim.sequence.padding;
					if (currentFrame < 0 || currentFrame >= spotAnim.sequence.frameCount) {
						currentFrame = 0;
					}
				}
				nextFrame = currentFrame + 1;
				if (nextFrame >= spotAnim.sequence.frameCount) {
					nextFrame -= spotAnim.sequence.padding;
					if (nextFrame < 0 || nextFrame >= spotAnim.sequence.frameCount) {
						nextFrame = -1;
					}
				}
			}
		}
	}
}
