package net.crandor;

final class NpcDefinition extends NodeSub {

    static MRUNodes modelCache = new MRUNodes(30);
    static MRUNodes recentUse = new MRUNodes(64);
    private static Stream stream;
    private static int[] streamIndices;
    public int[] aditionalModels;
    int turn90CCWAnimIndex;
    int turn180AnimIndex;
    int combatLevel;
    String name;
    String actions[];
    int walkAnim;
    byte npcSize;
    int headIcon;
    int standAnim;
    long type;
    int degreesToTurn;
    int turn90CWAnimIndex;
    boolean clickable;
    boolean drawMinimapDot;
    int childrenIDs[];
    byte description[];
    boolean aBoolean93;
    private int varBitId;
    private int varpId;
    private short[] recolourTarget;
    private short[] recolourOriginal;
    private int lightModifier;
    private int scaleY;
    private int scaleXZ;
    private int translateX;
    private int translateY;
    private int translateZ;
    private int shadowModifier;
    private int[] npcModels;

    NpcDefinition() {
        turn90CCWAnimIndex = -1;
        varBitId = -1;
        turn180AnimIndex = -1;
        varpId = -1;
        combatLevel = -1;
        walkAnim = -1;
        npcSize = 1;
        headIcon = -1;
        standAnim = -1;
        type = -1L;
        degreesToTurn = 256;
        turn90CWAnimIndex = -1;
        clickable = true;
        scaleY = 128;
        drawMinimapDot = true;
        scaleXZ = 128;
        aBoolean93 = false;
    }

    static NpcDefinition forID(int i) {
        NpcDefinition entityDef = (NpcDefinition) recentUse.get(i);

        if (entityDef != null) {
            return entityDef;
        }

        entityDef = new NpcDefinition();
        entityDef.type = i;

        stream.currentOffset = streamIndices[i];
        entityDef.readValues(stream);

        if (i == 2862) {
            entityDef.scaleXZ = (entityDef.scaleXZ / 2) + 15;
            entityDef.scaleY = (entityDef.scaleY / 2) + 15;
            entityDef.actions = new String[]{"Talk-to", null, "Pick-up", null, null};
        } else if (i == 614) {
            entityDef.name = "Doug";
            entityDef.actions = new String[5];
            entityDef.actions[0] = "Open Shop";
        } else if (i == 350) {
            entityDef.actions = new String[5];
            entityDef.actions[0] = "Talk-to";
            entityDef.name = "Boric";
        } else if (i == 3815) {
            entityDef.name = "Greebo Gnome Engineer";
            entityDef.actions = new String[5];
            entityDef.actions[0] = "Talk-to";
        } else  if (i == 6611) {
            entityDef.actions = new String[5];
            entityDef.actions[0] = "Talk-to";
            entityDef.actions[2] = "Open Shop";
        } else if (i == 490) {
			entityDef.translateY = 66;
		} else if (i == 492) {
			entityDef.translateY = 22;
		}

        recentUse.put(entityDef, i);
        return entityDef;
    }

    static void unpackConfig(StreamLoader streamLoader) {
        stream = new Stream(FileOperations.ReadFile(signlink.findcachedir() + "npcNew.dat"));
        Stream stream2 = new Stream(FileOperations.ReadFile(signlink.findcachedir() + "npcNew.idx"));
        int totalNPCs = stream2.readUnsignedWord();
        System.out.println("NPC Amount: " + totalNPCs);
        streamIndices = new int[totalNPCs];
        int i = 2;
        for (int j = 0; j < totalNPCs; j++) {
            streamIndices[j] = i;
            i += stream2.readUnsignedWord();
        }

        NpcDefinition def = forID(5557);
        System.out.println(def.standAnim);
    }

    static void nullLoader() {
        modelCache = null;
        streamIndices = null;
        stream = null;
    }

    Model getHeadModel() {
        if (childrenIDs != null) {
            NpcDefinition entityDef = morph();
            if (entityDef == null)
                return null;
            else
                return entityDef.getHeadModel();
        }
        if (aditionalModels == null)
            return null;
        boolean modelMissing = false;
        for (int i = 0; i < aditionalModels.length; i++)
            if (!Model.isCached(aditionalModels[i]))
                modelMissing = true;
        if (modelMissing)
            return null;
        Model additionalModels[] = new Model[aditionalModels.length];
        for (int j = 0; j < aditionalModels.length; j++)
            additionalModels[j] = Model.getModel(aditionalModels[j]);
        Model model;
        if (additionalModels.length == 1)
            model = additionalModels[0];
        else
            model = new Model(additionalModels, additionalModels.length);
        if (recolourOriginal != null) {
            for (int k = 0; k < recolourOriginal.length; k++)
                model.setColor(recolourOriginal[k], recolourTarget[k]);
        }
        return model;
    }

    NpcDefinition morph() {
        try {
            int j = -1;
            if (varBitId != -1) {
                j = VarBit.getValue(varBitId);
            } else if (varpId != -1) {
                j = Client.playerVariables[varpId];
            }

            if (j < 0 || j >= childrenIDs.length || childrenIDs[j] == -1) {
                return null;
            }

            return forID(childrenIDs[j]);
        } catch (Exception e) {
            return null;
        }
    }

    Model getAnimatedModel(int idleAnimFrame, int idleAnimNextFrame, int idleAnimCycle, int idleAnimFrameCycle, int currAnimFrame, int nextAnimFrame, int currCycle, int nextCycle, boolean labelsStop[]) {
        if (childrenIDs != null) {
            final NpcDefinition type = morph();
            if (type == null) {
                return null;
            }
            return type.getAnimatedModel(idleAnimFrame, idleAnimNextFrame, idleAnimCycle, idleAnimFrameCycle, currAnimFrame, nextAnimFrame, currCycle, nextCycle, labelsStop);
        }
        Model model = (Model) modelCache.get(type);
        if (model == null) {
            boolean flag = false;
            for (int i1 = 0; i1 < npcModels.length; i1++) {
                if (!Model.isCached(npcModels[i1])) {
                    flag = true;
                }
            }
            if (flag) {
                return null;
            }
            final Model[] parts = new Model[npcModels.length];
            for (int j1 = 0; j1 < npcModels.length; j1++) {
                parts[j1] = Model.getModel(npcModels[j1]);
            }
            if (parts.length == 1) {
                model = parts[0];
            } else {
                model = new Model(parts, parts.length);
            }
            if (recolourOriginal != null) {
                for (int k1 = 0; k1 < recolourOriginal.length; k1++) {
                    model.setColor(recolourOriginal[k1], recolourTarget[k1]);
                }
            }
            if (type == 7339 || type == 5361) {//geyser titan, waterfiend
            	model.face_texture = null;
            }
            model.createBones();
            model.setLighting(lightModifier + 64, shadowModifier + 850, -30, -50, -30, true, true, true);
            modelCache.put(model, type);
        }
		if (currAnimFrame == -1 && idleAnimFrame == -1) {
			Model model_1 = model.copy(Model.npcModel, true);
			if (scaleXZ != 128 || scaleY != 128) {
				model_1.scale(scaleXZ, scaleY, scaleXZ);
			}
			if (translateX != 0 || translateY != 0 || translateZ != 0) {
				model_1.translate(translateX, -translateY, translateZ);
			}
			return model_1;
		}
		Model model_1;
		if (currAnimFrame != -1 && idleAnimFrame != -1) {
			model_1 = model.copy(Model.npcModel, !(FrameReader.hasAlpha(currAnimFrame) | FrameReader.hasAlpha(idleAnimFrame)));
			model_1.method471(labelsStop, idleAnimFrame, idleAnimNextFrame, idleAnimCycle - 1, idleAnimFrameCycle, currAnimFrame, nextAnimFrame, nextCycle - 1, currCycle);
		} else if (currAnimFrame != -1 && nextAnimFrame != -1) {
			model_1 = model.copy(Model.npcModel, !(FrameReader.hasAlpha(currAnimFrame) | FrameReader.hasAlpha(nextAnimFrame)));
			model_1.interpolateFrames(currAnimFrame, nextAnimFrame, nextCycle - 1, currCycle, null, false);
		} else {
			model_1 = model.copy(Model.npcModel, !FrameReader.hasAlpha(currAnimFrame));
			model_1.applyTransform(currAnimFrame);
		}

        if (scaleXZ != 128 || scaleY != 128) {
            model_1.scale(scaleXZ, scaleY, scaleXZ);
        }
		if (translateX != 0 || translateY != 0 || translateZ != 0) {
			model_1.translate(translateX, -translateY, translateZ);
		}
        return model_1;
    }

    private void readValues(Stream stream) {
        do {
            int i = stream.readUnsignedByte();
            if (i == 0)
                return;
            if (i == 1) {
                int j = stream.readUnsignedByte();
                npcModels = new int[j];
                for (int j1 = 0; j1 < j; j1++)
                    npcModels[j1] = stream.readUnsignedWord();

            } else if (i == 2)
                name = stream.readString();
            else if (i == 3)
                description = stream.readBytes();
            else if (i == 12)
                npcSize = stream.readSignedByte();
            else if (i == 13)
                standAnim = stream.readUnsignedWord();
            else if (i == 14)
                walkAnim = stream.readUnsignedWord();
            else if (i == 17) {
                walkAnim = stream.readUnsignedWord();
                turn180AnimIndex = stream.readUnsignedWord();
                turn90CWAnimIndex = stream.readUnsignedWord();
                turn90CCWAnimIndex = stream.readUnsignedWord();
            } else if (i >= 30 && i < 35) {
                if (actions == null)
                    actions = new String[5];
                actions[i - 30] = stream.readString();
                if (actions[i - 30].equalsIgnoreCase("hidden"))
                    actions[i - 30] = null;
            } else if (i == 40) {
                int k = stream.readUnsignedByte();
                recolourOriginal = new short[k];
                recolourTarget = new short[k];
                for (int k1 = 0; k1 < k; k1++) {
                    recolourOriginal[k1] = (short) stream.readUnsignedWord();
                    recolourTarget[k1] = (short) stream.readUnsignedWord();
                }
            } else if (i == 60) {
                int l = stream.readUnsignedByte();
                aditionalModels = new int[l];
                for (int l1 = 0; l1 < l; l1++)
                    aditionalModels[l1] = stream.readUnsignedWord();

            } else if (i == 90)
                stream.readUnsignedWord();
            else if (i == 91)
                stream.readUnsignedWord();
            else if (i == 92)
                stream.readUnsignedWord();
            else if (i == 93)
                drawMinimapDot = false;
            else if (i == 95)
                combatLevel = stream.readUnsignedWord();
            else if (i == 97)
                scaleXZ = stream.readUnsignedWord();
            else if (i == 98)
                scaleY = stream.readUnsignedWord();
            else if (i == 99)
                aBoolean93 = true;
            else if (i == 100)
                lightModifier = stream.readSignedByte();
            else if (i == 101)
                shadowModifier = stream.readSignedByte() * 5;
            else if (i == 102)
                headIcon = stream.readUnsignedWord();
            else if (i == 103)
                degreesToTurn = stream.readUnsignedWord() * 8;
            else if (i == 106) {
                varBitId = stream.readUnsignedWord();
                if (varBitId == 65535)
                    varBitId = -1;
                varpId = stream.readUnsignedWord();
                if (varpId == 65535)
                    varpId = -1;
                int i1 = stream.readUnsignedByte();
                childrenIDs = new int[i1 + 1];
                for (int i2 = 0; i2 <= i1; i2++) {
                    childrenIDs[i2] = stream.readUnsignedWord();
                    if (childrenIDs[i2] == 65535)
                        childrenIDs[i2] = -1;
                }

            } else if (i == 107)
                clickable = false;
        } while (true);
    }

}
