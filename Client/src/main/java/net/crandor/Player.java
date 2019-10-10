package net.crandor;

public final class Player extends Mobile {

	static MRUNodes modelCache = new MRUNodes(260);
	public final int[] equipment;
	final int[] appearanceColors;
	public int npcId = -1;
	public int team;
	public String name;
	public String titleText = null;
	public int combatLevel;
	public int summoning;
	public int headIcon;
	public int skullIcon;
	public int hintIcon;
	public int anInt1707;
	public int privilage;
	public boolean elite;
	boolean aBoolean1699;
	int playerIndex;
	int anInt1708;
	int drawHeight;
	boolean visible;
	int anInt1711;
	int anInt1712;
	int anInt1713;
	Model aModel_1714;
	int anInt1719;
	int anInt1720;
	int anInt1721;
	int anInt1722;
	int skill;
	CustomizedItem[] customizedItems = new CustomizedItem[12];
	int gender;
    private long lastUsedHash;
    private long appearanceHash;

	Player() {
		lastUsedHash = -1L;
		aBoolean1699 = false;
		appearanceColors = new int[5];
		visible = false;
		equipment = new int[12];
	}

	@Override
	public Model getSpotAnimModel() {
		if (!aBoolean1699 && super.spotAnimId != -1 && super.spotAnimFrame != -1) {
			SpotAnim spotAnim = SpotAnim.cache[super.spotAnimId];
			Model model_2 = spotAnim.getModel(super.spotAnimFrame, super.spotAnimNextFrame, super.spotAnimFrameDelay);
			if (model_2 != null) {
				model_2.translate(0, -super.anInt1524, 0);
			}
			return model_2;
		}
		return null;
	}

	@Override
	public Model getRotatedModel() {
		if (!visible)
			return null;
		Model animatedModel = getAnimatedModel();
		if (animatedModel == null)
			return null;
		animatedModel.calculateDiagonals();
		super.height = animatedModel.highestY;
		if (!aBoolean1699 && aModel_1714 != null) {
			if (Client.loopCycle >= anInt1708)
				aModel_1714 = null;
			if (Client.loopCycle >= anInt1707 && Client.loopCycle < anInt1708) {
				Model model_1 = aModel_1714;
				model_1.translate(anInt1711 - super.x, anInt1712 - drawHeight, anInt1713 - super.y);
				if (super.turnDirection == 512) {
					model_1.rotateBy270();
				} else if (super.turnDirection == 1024) {
					model_1.rotateBy180();
				} else if (super.turnDirection == 1536)
					model_1.rotateBy90();
				Model aclass30_sub2_sub4_sub6s[] = { animatedModel, model_1 };
				animatedModel = new Model(aclass30_sub2_sub4_sub6s);
				if (super.turnDirection == 512)
					model_1.rotateBy90();
				else if (super.turnDirection == 1024) {
					model_1.rotateBy180();
				} else if (super.turnDirection == 1536) {
					model_1.rotateBy270();
				}
				model_1.translate(super.x - anInt1711, drawHeight - anInt1712, super.y - anInt1713);
			}
		}
		animatedModel.rendersWithinOneTile = true;
		return animatedModel;
	}

	public void updatePlayer(Stream stream) {
		stream.currentOffset = 0;
		playerIndex = stream.readUnsignedWordA();

		int bitPacked = stream.readUnsignedByte();
		gender = (bitPacked & 0x1);
		boolean hasRecolors = (bitPacked & 0x2) != 0;
		headIcon = stream.readUnsignedByte();
		skullIcon = stream.readUnsignedByte();
		npcId = -1;
		team = 0;

		for (int index = 0; index < 12; index++) {

			int k = stream.readUnsignedByte();
			if (k == 0) {
				equipment[index] = 0;
				continue;
			}

			int i1 = stream.readUnsignedByte();
			int data = (k << 8) + i1;

			if (index == 0 && data == 65535) {
				npcId = stream.readUnsignedWord();
				break;
			}

			equipment[index] = data;
			if (data >= 32768) {
				int l1 = ItemDefinition.forID(data - 32768).team;
				if (l1 != 0) {
					team = l1;
				}
			}

		}

		if (hasRecolors) {
			int mask = stream.readUnsignedWord();

			for (int i = 0; i < 12; i++) {
				if ((mask & 1 << i) != 0) {
					int itemId = stream.readUnsignedWord();
					customizedItems[i] = CustomizedItem.decode(ItemDefinition.forID(itemId), stream);
				}
			}
		}
		for (int l = 0; l < 5; l++) {
			int j1 = stream.readUnsignedByte();
			if (j1 < 0 || j1 >= Client.anIntArrayArray1003[l].length)
				j1 = 0;
			appearanceColors[l] = j1;
		}

		super.standAnimIndex = stream.readUnsignedWord();
		if (super.standAnimIndex == 65535)
			super.standAnimIndex = -1;
		super.standTurnAnimIndex = stream.readUnsignedWord();
		if (super.standTurnAnimIndex == 65535)
			super.standTurnAnimIndex = -1;
		super.walkAnimIndex = stream.readUnsignedWord();
		if (super.walkAnimIndex == 65535)
			super.walkAnimIndex = -1;
		super.turn180AnimIndex = stream.readUnsignedWord();
		if (super.turn180AnimIndex == 65535)
			super.turn180AnimIndex = -1;
		super.turn90CWAnimIndex = stream.readUnsignedWord();
		if (super.turn90CWAnimIndex == 65535)
			super.turn90CWAnimIndex = -1;
		super.turn90CCWAnimIndex = stream.readUnsignedWord();
		if (super.turn90CCWAnimIndex == 65535)
			super.turn90CCWAnimIndex = -1;
		super.runAnimIndex = stream.readUnsignedWord();
		if (super.runAnimIndex == 65535)
			super.runAnimIndex = -1;

		name = TextClass.fixName(TextClass.nameForLong(stream.readQWord()));
		String title = TextClass.fixName(TextClass.nameForLong(stream.readQWord()));
		String titleColor = TextClass.fixName(TextClass.nameForLong(stream.readQWord()));
		if (!title.equalsIgnoreCase("None") || !titleColor.equalsIgnoreCase("None")) {
			titleText = "<col=" + titleColor + ">" + title + "</col>";
		} else {
			titleText = null;
		}

		elite = stream.readUnsignedByte() == 1;

		combatLevel = stream.readUnsignedByte();
		summoning = stream.readUnsignedByte();
		skill = stream.readUnsignedWord();
		visible = true;

		updateHash();
		title = null;
		titleColor = null;
	}

	public void updateHash() {
		long[] crcTable = Stream.CRC_64;
		appearanceHash = -1;
		for (int id : equipment) {
			appearanceHash = (appearanceHash >>> 8 ^ crcTable[(int) ((appearanceHash ^ id >> 24) & 0xffL)]);
			appearanceHash = (appearanceHash >>> 8 ^ crcTable[(int) ((appearanceHash ^ id >> 16) & 0xffL)]);
			appearanceHash = (appearanceHash >>> 8 ^ crcTable[(int) ((appearanceHash ^ id >> 8) & 0xffL)]);
			appearanceHash = (appearanceHash >>> 8 ^ crcTable[(int) ((appearanceHash ^ id) & 0xffL)]);
		}

		if (customizedItems != null) {
			for (CustomizedItem customizedItem : customizedItems) {
				if (customizedItem != null) {
					if (customizedItem.newModelColors != null) {
						for (int id = 0; id < customizedItem.newModelColors.length; id++) {
							appearanceHash = (appearanceHash >>> 8 ^ crcTable[(int) ((appearanceHash ^ customizedItem.newModelColors[id] >> 8) & 0xffL)]);
							appearanceHash = (appearanceHash >>> 8 ^ crcTable[(int) ((appearanceHash ^ customizedItem.newModelColors[id]) & 0xffL)]);
						}
					}
				}
			}
		}

		for (int color : appearanceColors) {
			appearanceHash = (appearanceHash >>> 8 ^ crcTable[(int) ((appearanceHash ^ color) & 0xffL)]);
		}

		appearanceHash = (appearanceHash >>> 8 ^ crcTable[(int) ((appearanceHash ^ gender) & 0xffL)]);
	}

	public Model getAnimatedModel() {
		long l = appearanceHash;
		int currentFrame = -1;
		int nextFrame = -1;
		int currrentFrameDelay = 0;
		int currentDelay = 0;
		int idleAnimFrame = -1;
		int idleAnimNextFrame = -1;
		int idleAnimDelay = 0;
		int idleAnimFrameDelay = 0;
		int shieldId = -1;
		int weaponId = -1;
		boolean[] stopLabels = null;
		if (npcId != -1) {
			if (super.animId >= 0 && super.animDelay == 0) {
				final Animation seq = Animation.anims[super.animId];
				stopLabels = seq.anIntArray357;
				currentFrame = seq.frames[super.animFrame];
				if (seq.tween && Settings.tweening && super.animNextFrame != -1) {
					nextFrame = seq.frames[super.animNextFrame];
					currrentFrameDelay = seq.delays[super.animFrame];
					currentDelay = super.animFrameDelay;
				}
				if (super.idleAnimId >= 0 && super.idleAnimId != super.standAnimIndex) {
					Animation idleSeq = Animation.anims[super.idleAnimId];
					idleAnimFrame = idleSeq.frames[super.idleAnimFrame];
					if (idleSeq.tween && Settings.tweening && super.idleAnimNextFrame != -1) {
						idleAnimNextFrame = idleSeq.frames[super.idleAnimNextFrame];
						idleAnimDelay = super.idleAnimFrameDelay;
						idleAnimFrameDelay = idleSeq.delays[super.idleAnimFrame];
					}
				}
			} else if (super.idleAnimId >= 0) {
				final Animation seq = Animation.anims[super.idleAnimId];
				currentFrame = seq.frames[super.idleAnimFrame];
				if (seq.tween && Settings.tweening && super.idleAnimNextFrame != -1) {
					nextFrame = seq.frames[super.idleAnimNextFrame];
					currrentFrameDelay = seq.delays[super.idleAnimFrame];
					currentDelay = super.idleAnimFrameDelay;
				}
			}
			return NpcDefinition.forID(npcId).getAnimatedModel(idleAnimFrame, idleAnimNextFrame, idleAnimDelay, idleAnimFrameDelay, currentFrame, nextFrame, currrentFrameDelay, currentDelay, stopLabels);
		}
		if (super.animId >= 0 && super.animDelay == 0) {
			final Animation animation = Animation.anims[super.animId];
			stopLabels = animation.anIntArray357;
			currentFrame = animation.frames[super.animFrame];
			if (animation.tween && Settings.tweening && super.animNextFrame != -1) {
				nextFrame = animation.frames[super.animNextFrame];
				currrentFrameDelay = animation.delays[super.animFrame];
				currentDelay = super.animFrameDelay;
			}
			if (super.idleAnimId >= 0 && super.idleAnimId != super.standAnimIndex) {
				Animation idleSeq = Animation.anims[super.idleAnimId];
				idleAnimFrame = idleSeq.frames[super.idleAnimFrame];
				if (idleSeq.tween && Settings.tweening && super.idleAnimNextFrame != -1) {
					idleAnimNextFrame = idleSeq.frames[super.idleAnimNextFrame];
					idleAnimDelay = super.idleAnimFrameDelay;
					idleAnimFrameDelay = idleSeq.delays[super.idleAnimFrame];
				}
			}
			if (animation.anInt360 >= 0) {
				shieldId = animation.anInt360;
				l += shieldId - equipment[5] << 40;
			}
			if (animation.anInt361 >= 0) {
				weaponId = animation.anInt361;
				l += weaponId - equipment[3] << 48;
			}
		} else if (super.idleAnimId >= 0) {
			Animation seq = Animation.anims[super.idleAnimId];
			currentFrame = seq.frames[super.idleAnimFrame];
			if (seq.tween && Settings.tweening && super.idleAnimNextFrame != -1) {
				nextFrame = seq.frames[super.idleAnimNextFrame];
				currrentFrameDelay = seq.delays[super.idleAnimFrame];
				currentDelay = super.idleAnimFrameDelay;
			}
		}
		Model model_1 = (Model) modelCache.get(l);
		if (model_1 == null) {
			boolean flag = false;
			for (int i2 = 0; i2 < 12; i2++) {
				int k2 = equipment[i2];
				if (i2 == 3 && weaponId != -1) {
					if (weaponId == 65535) {
						k2 = 0;
					} else {
						k2 = weaponId + 32768;
					}
				}
				if (i2 == 5 && shieldId != -1) {
					if (shieldId == 65535) {
						k2 = 0;
					} else {
						k2 = shieldId + 32768;
					}
				}
				if (k2 >= 256 && k2 < 32768 && !IdentityKit.cache[k2 - 256].isBodyDownloaded()) {
					flag = true;
				}

				// zulrah blowpipe anim fix
				if (k2 == 45694) {
					k2 = 57808;
				}

				if (k2 >= 32768 && !ItemDefinition.forID(k2 - 32768).isEquippedModelCached(gender)) {
					flag = true;
				}
			}
			if (flag) {
				if (lastUsedHash != -1L) {
					model_1 = (Model) modelCache.get(lastUsedHash);
				}
				if (model_1 == null) {
					return null;
				}
			}
		}
		if (model_1 == null) {
			Model aclass30_sub2_sub4_sub6s[] = new Model[13];
			int j2 = 0;
			for (int l2 = 0; l2 < 12; l2++) {
				int i3 = equipment[l2];
				CustomizedItem customizedItem = null;
				if (l2 == 3 && weaponId != -1) {
					if (weaponId == 65535) {
						i3 = 0;
					} else {
						i3 = weaponId + 32768;
					}
				}
				if (l2 == 5 && shieldId != -1) {
					if (shieldId == 65535) {
						i3 = 0;
					} else {
						i3 = shieldId + 32768;
					}
				}
				if (i3 >= 256 && i3 < 32768) {
					Model model_3 = IdentityKit.cache[i3 - 256].getBodyModel();
					if (model_3 != null)
						aclass30_sub2_sub4_sub6s[j2++] = model_3;
				}

				// zulrah blowpipe anim fix
				if (i3 == 45694) {
					i3 = 57808;
				}

				if (i3 >= 32768) {
					if (customizedItems != null && customizedItems[l2] != null) {
						customizedItem = customizedItems[l2];
					}
					Model model_4 = ItemDefinition.forID(i3 - 32768).getEquippedModel(gender, customizedItem);
					if (model_4 != null)
						aclass30_sub2_sub4_sub6s[j2++] = model_4;
				}
			}
			if (Settings.playerShadow)
				aclass30_sub2_sub4_sub6s[j2++] = Model.getModel(9996);
			model_1 = new Model(aclass30_sub2_sub4_sub6s, j2);
			for (int j3 = 0; j3 < 5; j3++) {
				if (appearanceColors[j3] != 0) {
					model_1.setColor(Client.anIntArrayArray1003[j3][0], Client.anIntArrayArray1003[j3][appearanceColors[j3]]);
		            if (j3 == 4) {
		            	model_1.setColor(Client.skinColor2[0], Client.skinColor2[appearanceColors[j3]]);
                    }
					if (j3 == 1) {
						model_1.setColor(Client.anIntArray1204[0], Client.anIntArray1204[appearanceColors[j3]]);
					}
				}
			}
			model_1.createBones();
			model_1.setLighting(84, 1000, -90, -580, -90, true, true, true);
			modelCache.put(model_1, l);
			lastUsedHash = l;
		}
		if (aBoolean1699) {
			return model_1;
		}
		Model model_2 = Model.npcModel;
		model_2.method464(model_1, FrameReader.isNullFrame(currentFrame) & FrameReader.isNullFrame(idleAnimFrame));
		if (currentFrame != -1 && idleAnimFrame != -1) {
			model_2.method471(stopLabels, idleAnimFrame, idleAnimNextFrame, idleAnimDelay - 1, idleAnimFrameDelay, currentFrame, nextFrame, currentDelay - 1, currrentFrameDelay);
		} else if (currentFrame != -1) {
			if (Settings.tweening) {
				model_2.interpolateFrames(currentFrame, nextFrame, currentDelay - 1, currrentFrameDelay, null, false);
			} else {
				model_2.applyTransform(currentFrame);
			}
		}
		model_2.triangleSkin = null;
		model_2.vertexSkin = null;
		return model_2;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	public Model getHeadModel() {
		if (!visible) {
			return null;
		}

		if (npcId != -1) {
			return NpcDefinition.forID(npcId).getHeadModel();
		}

		boolean flag = false;

		for (int i = 0; i < 12; i++) {
			int j = equipment[i];
			if (j >= 256 && j < 32768 && !IdentityKit.cache[j - 256].isHeadDownloaded())
				flag = true;
			if (j >= 32768 && !ItemDefinition.forID(j - 32768).isDialogueModelCached(gender))
				flag = true;
		}

		if (flag)
			return null;

		Model aclass30_sub2_sub4_sub6s[] = new Model[12];
		int k = 0;
		for (int l = 0; l < 12; l++) {
			int i1 = equipment[l];
			if (i1 >= 256 && i1 < 32768) {
				Model model_1 = IdentityKit.cache[i1 - 256].getHeadModel();
				if (model_1 != null)
					aclass30_sub2_sub4_sub6s[k++] = model_1;
			}
			if (i1 >= 32768) {
				Model model_2 = ItemDefinition.forID(i1 - 32768).getChatModel(gender);
				if (model_2 != null)
					aclass30_sub2_sub4_sub6s[k++] = model_2;
			}
		}

		Model model = new Model(aclass30_sub2_sub4_sub6s, k);
		for (int j1 = 0; j1 < 5; j1++) {
			if (appearanceColors[j1] != 0) {
				model.setColor(Client.anIntArrayArray1003[j1][0], Client.anIntArrayArray1003[j1][appearanceColors[j1]]);
				if (j1 == 4) {
					model.setColor(Client.skinColor2[0], Client.skinColor2[appearanceColors[j1]]);
				}
				if (j1 == 1) {
					model.setColor(Client.anIntArray1204[0], Client.anIntArray1204[appearanceColors[j1]]);
				}
			}
		}
		return model;
	}

}
