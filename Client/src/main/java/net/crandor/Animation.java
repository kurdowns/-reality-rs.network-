package net.crandor;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * we're actually using 667 animations on 634 characters, to get round this the 634 labels have been used on the 667 animations
 */
final class Animation {

    private static final int[] OSRS_ANIMATIONS = {
            5070, // zulrah stand
            3989, // kraken stand
            4488, // cerberous stand
            5068, // zulrah toxic cloud
            5069, // zulrah range anim
            5072, // zulrah go up
            5073, // zulrah go down
            5806, // zulrah melee target
            5807, // zulrah melee target
            //  493, // toxic cloud anim
            1721, // snakeling stand
            2405, // snakeling walk
            2406, // snakeleing attack
            2407, // snakeling block
            2408, // snakling death
            85, // projectile anim
            5061, // blowpipe anim
            876, // blowpipe gfx anim,
            621,
            619,
            620,
            622,
            4517, // soul devourer cerb map
            494, // cerb map fire
            4484, // cerb stand
            4505, // ghost stand
            4497, // cerb gfx
            4500, // cerb gfx
            4502, // cerb gfx
            4501, // cerb gfx
            4505, // cerb gfx
            4494, // cerb attack
            4492, // cerb attack 2
            4495, // cerb death
            4504, // ghost
            4503, // ghost
            1378, // d hammer attack
            2261, // d hammer special anim
            618, // harpoonW

            5805, // stand jad jr
            2650, // walk jad jr

            7177, // stand beaver
            7178, // walk beaver
            719, // spell anim


            4923,
            4919,
            5317,
            5318,
            1828,
            1829,
            5497,
            5505,
            5319,
            5320,
            5321,
            5322,
            5499,
            5500,
            5501,
            5503,
            6581,
            6576,
            5325,
            5326,

            // new pet anims
            7304,
            7310,
            7313,
            7312,
            7316,
            7306,
            7307,
            7309,
            7315,
            6809,
            6808
    };

    static Animation anims[];
    int frameCount;
    int frames[];
    int anIntArray354[];
    int[] delays;
    int padding;
    boolean anIntArray357[];
    boolean aBoolean358;
    int anInt359;
    int anInt360;
    int anInt361;
    int anInt362;
    int anInt363;
    int anInt364;
    int anInt365;
    boolean tween;
    private int id;

    private Animation() {
        padding = -1;
        aBoolean358 = false;
        anInt359 = 5;
        anInt360 = -1;
        anInt361 = -1;
        anInt362 = 99;
        anInt363 = -1;
        anInt364 = -1;
        anInt365 = 2;
    }

    static void unpackConfig(StreamLoader streamLoader) {
        Stream stream = new Stream(streamLoader.getDataForName("seq.dat"));
        int length = stream.readUnsignedWord();
        System.out.println("Animation Amount: " + length);
        if (anims == null)
            anims = new Animation[16708];
        //  anims = new Animation[length];
        for (int j = 0; j < length; j++) {
            if (anims[j] == null) {
                anims[j] = new Animation();
                anims[j].id = j;
            }
            anims[j].readValues(stream);
        }

        // bonfire anim
        anims[16703] = new Animation();
        anims[16703].id = 16703;
        // anims[16703].fileId = 3988;
        anims[16703].frameCount = 26;
        //anims[16703].priority = 8;
        // anims[16703].rightHandEquip = 0;
        // anims[16703].leftHandEquip = 0;
        anims[16703].delays = new int[]{3, 3, 3, 3, 3, 3, 9, 4, 4, 4, 4, 4, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2};
        anims[16703].frames = new int[]{261357573, 261357763, 261357653, 261357674, 261357791, 261357796, 261357591, 261357569, 261357687, 261357634, 261357572, 261357600, 261357711, 261357658, 261357625, 261357607, 261357621, 261357688, 261357682, 261357643, 261357806, 261357652, 261357750, 261357812, 261357662, 261357696};


        anims[16706] = new Animation();
        anims[16706].id = 16706;
        //anims[16706].fileId = 3990;
        anims[16706].frameCount = 15;
        // anims[16706].loopDelay = 1;
        anims[16706].delays = new int[]{20, 4, 4, 4, 4, 3, 3, 3, 4, 4, 3, 3, 3, 3, 25};
        anims[16706].frames = new int[]{261488649, 261488668, 261488646, 261488662, 261488656, 261488654, 261488663, 261488640, 261488658, 261488643, 261488642, 261488661, 261488648, 261488666, 261488664};

        anims[16707] = new Animation();
        anims[16707].id = 16707;
        //anims[16707].fileId = 3990;
        anims[16707].frameCount = 15;
        // anims[16707].loopDelay = 1;
        anims[16707].delays = new int[]{27, 4, 4, 4, 4, 4, 5, 3, 3, 3, 3, 3, 3, 3, 17};
        anims[16707].frames = new int[]{261488651, 261488641, 261488655, 261488652, 261488647, 261488665, 261488660, 261488657, 261488650, 261488659, 261488669, 261488644, 261488653, 261488645, 261488667};

        // note(stuart): used to dump animations back to seq.dat
        /*try {
            reencode();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    static void unpackOsrs(StreamLoader streamLoader) {
    	Stream stream = new Stream(FileOperations.ReadFile(signlink.findcachedir() + "osrsseq.dat"));

        int length = stream.readUnsignedWord();
        System.out.println("osrs Animation Amount: " + length);
        for (int j = 0; j < length; j++) {
            Animation anim = new Animation();
            anim.readValues(stream);
            if (anim.anInt360 > 0) {
            	anim.anInt360 -= 512;
            }
            if (anim.anInt361 > 0) {
            	anim.anInt361 -= 512;
            }

            // List<Integer> files = new ArrayList<>();

            for (int index = 0; index < OSRS_ANIMATIONS.length; index++) {
                if (OSRS_ANIMATIONS[index] == j) {
                    anims[j] = anim;
                  /*  int group = anim.frames[0] >> 16;
                    if(OSRS_ANIMATIONS[index] == 6966) {
                        System.out.println("group: " + group);
                    }*/
                    /*for(int id : anim.frames) {
                        int group = id >> 16;

                        System.out.println("anim file: " + group);

                        if(!files.contains(group)) {
                            files.add(group);
                            try {
                                Files.copy(Paths.get("./anim_dump/" + group + ".gz"), Paths.get("./anims/" + group + ".gz"), REPLACE_EXISTING);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }*/
                }
            }
        }
    }

    private static void reencode() throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("seq.dat"))) {
            dos.writeShort(anims.length);
            for (int i = 0; i < anims.length; i++) {

                Animation animation = anims[i];

                if (animation.frames != null) {
                    dos.writeByte(1);
                    dos.writeShort(animation.frames.length);
                    for (int index = 0; index < animation.frames.length; index++) {
                        dos.writeInt(animation.frames[index]);
                    }
                    for (int index = 0; index < animation.frames.length; index++) {
                        dos.writeByte(animation.delays[index]);
                    }
                }
                if (animation.padding != -1) {
                    dos.writeByte(2);
                    dos.writeShort(animation.padding);
                }
                if (animation.anIntArray357 != null) {
                    dos.writeByte(3);
                    List<Integer> ids = new ArrayList<>();
                    for (int index = 0; index < animation.anIntArray357.length; index++) {
                        if (animation.anIntArray357[index]) {
                            ids.add(index);
                        }
                    }
                    if (ids.size() > 0) {
                        dos.writeByte(ids.size());
                        for (Integer id : ids) {
                            dos.writeByte(id);
                        }
                    }
                }
                if (animation.aBoolean358) {
                    dos.writeByte(4);
                }
                if (animation.anInt359 != 5) {
                    dos.writeByte(5);
                    dos.writeByte(animation.anInt359);
                }
                if (animation.anInt360 != -1) {
                    dos.writeByte(6);
                    dos.writeShort(animation.anInt360);
                }
                if (animation.anInt361 != -1) {
                    dos.writeByte(7);
                    dos.writeShort(animation.anInt361);
                }
                if (animation.anInt362 != 99) {
                    dos.writeByte(8);
                    dos.writeByte(animation.anInt362);
                }
                if (animation.anInt363 != -1) {
                    dos.writeByte(9);
                    dos.writeByte(animation.anInt363);
                }
                if (animation.anInt364 != -1) {
                    dos.writeByte(10);
                    dos.writeByte(animation.anInt364);
                }
                if (animation.anInt365 != 2) {
                    dos.writeByte(11);
                    dos.writeByte(animation.anInt365);
                }
                if (animation.tween) {
                    dos.writeByte(13);
                }
                //if(animation.interfaceFrames != null) {
                //	dos.writeByte(12);
                //	dos.writeInt(0);
                //}
                dos.writeByte(0);
            }
            dos.close();
        }
    }

	final Model animateObjectModel(Model model, int direction, int frame, int nextFrame, int cycle) {
		int i = delays[frame];
		frame = frames[frame];
		direction &= 0x3;
		if (Settings.tweening && nextFrame != -1 && nextFrame < frames.length) {
			nextFrame = frames[nextFrame];
		} else {
			nextFrame = -1;
		}
		Model model_;
		if (nextFrame == -1) {
			model_ = model.copy(Model.objectModel, !FrameReader.hasAlpha(frame));
		} else {
			model_ = model.copy(Model.objectModel, !FrameReader.hasAlpha(frame) & !FrameReader.hasAlpha(nextFrame));
		}
		if (direction == 1)
			model_.rotateBy270();
		else if (direction == 2)
			model_.rotateBy180();
		else if (direction == 3)
			model_.rotateBy90();
		model_.interpolateFrames(frame, nextFrame, cycle - 1, i, null, false);
		if (direction == 1)
			model_.rotateBy90();
		else if (direction == 2)
			model_.rotateBy180();
		else if (direction == 3)
			model_.rotateBy270();
		return model_;
	}

    private void readValues(Stream stream) {
        do {
            int i = stream.readUnsignedByte();
            if (i == 0)
                break;
            if (i == 1) {
                frameCount = stream.readUnsignedWord();

                frames = new int[frameCount];
                anIntArray354 = new int[frameCount];
                delays = new int[frameCount];
                for (int j = 0; j < frameCount; j++) {
                    frames[j] = stream.readDWord();
                    anIntArray354[j] = -1;
                }
                for (int j = 0; j < frameCount; j++)
                    delays[j] = stream.readUnsignedByte();
            } else if (i == 2)
                padding = stream.readUnsignedWord();
            else if (i == 3) {
                int k = stream.readUnsignedByte();
                anIntArray357 = new boolean[256];
                for (int l = 0; l < k; l++)
                    anIntArray357[stream.readUnsignedByte()] = true;
            } else if (i == 4)
                aBoolean358 = true;
            else if (i == 5)
                anInt359 = stream.readUnsignedByte();
            else if (i == 6)
                anInt360 = stream.readUnsignedWord();
            else if (i == 7)
                anInt361 = stream.readUnsignedWord();
            else if (i == 8)
                anInt362 = stream.readUnsignedByte();
            else if (i == 9)
                anInt363 = stream.readUnsignedByte();
            else if (i == 10)
                anInt364 = stream.readUnsignedByte();
            else if (i == 11)
                anInt365 = stream.readUnsignedByte();
            else if (i == 12)
                stream.readDWord();
            else if (i == 13)
                tween = true;
            else
                System.out.println("Error unrecognised seq config code: " + i);
        } while (true);
        if (frameCount == 0) {
            frameCount = 1;
            frames = new int[1];
            frames[0] = -1;
            anIntArray354 = new int[1];
            anIntArray354[0] = -1;
            delays = new int[1];
            delays[0] = -1;
        }
        if (anInt363 == -1)
            if (anIntArray357 != null)
                anInt363 = 2;
            else
                anInt363 = 0;
        if (anInt364 == -1) {
            if (anIntArray357 != null) {
                anInt364 = 2;
                return;
            }
            anInt364 = 0;
        }
    }

}