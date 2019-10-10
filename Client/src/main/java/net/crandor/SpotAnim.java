package net.crandor;

final class SpotAnim {

	static SpotAnim cache[];
	static MRUNodes modelCache = new MRUNodes(30);
	private final int[] originalModelColours;
	private final int[] modifiedModelColours;
	Animation sequence;
	int resizeXY;
	int resizeZ;
	int rotation;
	int modelBrightness;
	int modelShadow;
	int animationID;
	private int id;
	private int modelID;

	private SpotAnim() {
		animationID = -1;
		originalModelColours = new int[6];
		modifiedModelColours = new int[6];
		resizeXY = 128;
		resizeZ = 128;
	}

	static void unpackConfig(StreamLoader streamLoader) {
		Stream stream = new Stream(streamLoader.getDataForName("spotanim.dat"));
		int length = stream.readUnsignedWord();
		System.out.println("634 Graphics Amount: " + length);
		if (cache == null)
			//cache = new SpotAnim[length];
			cache = new SpotAnim[3136];
		for (int j = 0; j < length; j++) {
			if (cache[j] == null)
				cache[j] = new SpotAnim();
			cache[j].id = j;
			cache[j].readValues(stream, false);
		}

		cache[3098] = new SpotAnim();
		cache[3098].modelID = 69608;
		cache[3098].animationID = 16706;
		cache[3098].sequence = Animation.anims[16706];

		cache[3099] = new SpotAnim();
		cache[3099].modelID = 69608;
		cache[3099].animationID = 16706;
		cache[3099].sequence = Animation.anims[16706];
		int[] original = new int[]{5559, 5665, 5784};
		int[] modified = new int[]{6697, 5660, 5908};
		for (int i = 0; i < original.length; i++) {
			cache[3099].originalModelColours[i] = original[i];
			cache[3099].modifiedModelColours[i] = modified[i];
		}

		cache[3100] = new SpotAnim();
		cache[3100].modelID = 69608;
		cache[3100].animationID = 16706;
		cache[3100].sequence = Animation.anims[16706];
		original = new int[]{5559, 5665, 5784};
		modified = new int[]{2487, 3875, 3994};
		for (int i = 0; i < original.length; i++) {
			cache[3100].originalModelColours[i] = original[i];
			cache[3100].modifiedModelColours[i] = modified[i];
		}

		cache[3101] = new SpotAnim();
		cache[3101].modelID = 69608;
		cache[3101].animationID = 16706;
		cache[3101].sequence = Animation.anims[16706];
		original = new int[]{5559, 5665, 5784};
		modified = new int[]{11701, 10772, 11148};
		for (int i = 0; i < original.length; i++) {
			cache[3101].originalModelColours[i] = original[i];
			cache[3101].modifiedModelColours[i] = modified[i];
		}

		cache[3111] = new SpotAnim();
		cache[3111].modelID = 69608;
		cache[3111].animationID = 16706;
		cache[3111].sequence = Animation.anims[16706];
		original = new int[]{5665, 5784, 5559};
		modified = new int[]{6034, 5786, 5786};
		for (int i = 0; i < original.length; i++) {
			cache[3111].originalModelColours[i] = original[i];
			cache[3111].modifiedModelColours[i] = modified[i];
		}

		cache[3135] = new SpotAnim();
		cache[3135].modelID = 69604;
		cache[3135].animationID = 16707;
		cache[3135].sequence = Animation.anims[16707];
	}

	static void unpackOsrsConfig(StreamLoader streamLoader) {
		Stream stream = new Stream(streamLoader.getDataForName("osrsspotanim.dat"));
		int length = stream.readUnsignedWord();
		System.out.println("OSRS Graphics Amount: " + length);
		if (cache == null)
			cache = new SpotAnim[length];
		for (int j = 0; j < length; j++) {
			SpotAnim spotAnim = new SpotAnim();
			spotAnim.id = j;
			spotAnim.readValues(stream, true);

			if (j >= 1044 && j <= 1047) { // zulrah toxic cloud gfx
				cache[j] = spotAnim;
			}
			if (j == 1043) { // blowpipe gfx special
				cache[j] = spotAnim;
			}
			if (j == 1242) { // projectile 1 cerb
				cache[j] = spotAnim;
			}
			if (j == 1245) { // projectile 2 cerb
				cache[j] = spotAnim;
			}
			if (j == 1246) { // floor lava
				cache[j] = spotAnim;
			}
			if (j == 1247) { // floor lava exploding
				cache[j] = spotAnim;
			}
			if (j == 1248) { // ghost projectile
				cache[j] = spotAnim;
			}
			if (j == 1292) { // d hammer spc gfx
				cache[j] = spotAnim;
			}

			if (j == 1251 || j == 1252 || j == 1253) { // trident of the seas
				cache[j] = spotAnim;
			}

			if (j == 719 || j == 665 || j == 1040 || j == 1042) { // trident of the swamp
				cache[j] = spotAnim;
			}

		}
	}

	private void readValues(Stream stream, boolean osrs) {
		do {
			int i = stream.readUnsignedByte();
			if (i == 0)
				return;
			if (i == 1)
				modelID = stream.readUnsignedWord();
			else if (i == 2) {
				animationID = stream.readUnsignedWord();
				if (Animation.anims != null && animationID < Animation.anims.length)
					sequence = Animation.anims[animationID];
			} else if (i == 4)
				resizeXY = stream.readUnsignedWord();
			else if (i == 5)
				resizeZ = stream.readUnsignedWord();
			else if (i == 6)
				rotation = stream.readUnsignedWord();
			else if (i == 7)
				modelBrightness = osrs ? stream.readUnsignedWord() : stream.readUnsignedByte();
			else if (i == 8)
				modelShadow = osrs ? stream.readUnsignedWord() : stream.readUnsignedByte();
			else if (i == 40) {
				int j = stream.readUnsignedByte();
				for (int k = 0; k < j; k++) {
					originalModelColours[k] = stream.readUnsignedWord();
					modifiedModelColours[k] = stream.readUnsignedWord();
				}
			} else if (i == 41) {
				System.out.println("lol");
			} else
				System.out.println("Error unrecognised spotanim config code: " + i);
		} while (true);
	}

	public Model getModel(int frame, int nextFrame, int frameDelay) {
		Model model = (Model) modelCache.get(id);
		if (model == null) {
			model = Model.getModel(modelID);
			if (model == null) {
				return null;
			}

			for (int id = 0; id < 6; id++) {
				if (originalModelColours[id] != 0) {
					model.setColor(originalModelColours[id], modifiedModelColours[id]);
				}
			}

			model.createBones();
			model.setLighting(64 + modelBrightness, 850 + modelShadow, -30, -50, -30, true, true, true);
			modelCache.put(model, id);
		}

		Model transformedModel;

		if (animationID == -1 || frame == -1) {
			transformedModel = model.copy(Model.spotAnimModel, true);
		} else {
			int delay = sequence.delays[frame];
			frame = sequence.frames[frame];
			if (nextFrame == -1) {
				transformedModel = model.copy(Model.spotAnimModel, !FrameReader.hasAlpha(frame));
			} else {
				nextFrame = sequence.frames[nextFrame];
				transformedModel = model.copy(Model.spotAnimModel, !FrameReader.hasAlpha(frame) & !FrameReader.hasAlpha(nextFrame));
			}
			transformedModel.interpolateFrames(frame, nextFrame, frameDelay - 1, delay, null, false);
		}

		if (resizeXY != 128 || resizeZ != 128)
			transformedModel.scale(resizeXY, resizeZ, resizeXY);
		if (rotation != 0) {
			if (rotation == 90) {
				transformedModel.rotateBy90();
			}
			if (rotation == 180) {
				transformedModel.rotateBy180();
			}
			if (rotation == 270) {
				transformedModel.rotateBy270();
			}
		}
		return transformedModel;
	}

}