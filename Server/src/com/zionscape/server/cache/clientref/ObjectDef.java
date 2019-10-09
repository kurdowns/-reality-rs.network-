package com.zionscape.server.cache.clientref;

/*******************************************************************************
 * Copyright 2014 Aeresan LTD
 *
 * @author Poesy700
 * @author Lost Valentino
 * @author Wolfs Darker
 * <p>
 * No redistribution allowed in any way shape or form other than a obfuscated client executable for usage by players.
 ******************************************************************************/

import com.zionscape.server.util.Stream;

@SuppressWarnings("unused")
public final class ObjectDef {

	public static boolean lowMem;
	static int totalObjects;
	private static int[] streamIndices2;
	private static Stream stream2;
	private static Stream stream;
	private static int[] streamIndices;
	private static int cacheIndex;
	private static ObjectDef[] cache;
	public boolean groundObstructive;
	public String name;
	public int width;
	public int mapFunctionID;
	public int variableID;
	public int type;
	public boolean impenetrable;
	public int mapSceneID;
	public int alternativeIDS[];
	public int height;
	public boolean adjustToTerrain;
	public boolean occludes;
	public boolean isUnwalkable;
	public int surroundings;
	public int variableIDBitfield;
	public int decorDisplacement;
	public byte description[];
	public boolean hasActions;
	public boolean castsShadow;
	public int animationId;
	public String actions[];
	public int[] objectModelIDS;
	private byte brightness;
	private int offsetX;
	private int modelSizeY;
	private byte contrast;
	private int offsetH;
	private int[] originalModelColors;
	private int modelSizeX;
	private boolean inverted;
	private int supportItems;
	private boolean ethereal;
	private boolean nonFlatShading;
	private int modelSizeH;
	private int[] objectModelType;
	private int offsetY;
	private int[] modifiedModelColors;
	private short[] modifiedTextureFace;
	private short[] originalTextureFace;

	private ObjectDef() {
		type = -1;
	}

	/**
	 * todo get rid of this
	 *
	 * @param id
	 * @return
	 */
	private static boolean useNewDefinition(int id) {
		switch (id) {

			// rev exit
			case 18341:
				return true;

			/**
			 * dung maps
			 */
			case 49257:
			case 49272:
			case 49273:
			case 49462:
			case 49465:
			case 49504:
			case 49507:
			case 49609:
			case 49615:
			case 49616:
			case 49617:
			case 49618:
			case 49623:
			case 49624:
			case 49702:
			case 49765:
			case 49922:
			case 50342:
			case 50346:
			case 50569:
			case 50574:
			case 50575:
			case 50576:
			case 51100:
			case 51101:
			case 51149:
			case 51157:
			case 51158:
			case 51159:
			case 51163:
			case 51164:
			case 51171:
			case 51172:
			case 51173:
			case 51174:
			case 51175:
			case 51177:
			case 51178:
			case 51179:
			case 51180:
			case 51181:
			case 51182:
			case 51183:
			case 51184:
			case 51186:
			case 51187:
			case 51188:
			case 51190:
			case 51191:
			case 51192:
			case 51193:
			case 51195:
			case 51196:
			case 51198:
			case 51200:
			case 51207:
			case 51213:
			case 51214:
			case 51227:
			case 51228:
			case 51230:
			case 51231:
			case 51232:
			case 51233:
			case 51280:
			case 51313:
			case 51315:
			case 51316:
			case 51317:
			case 51318:
			case 51320:
			case 51321:
			case 51322:
			case 51323:
			case 51325:
			case 51326:
			case 51327:
			case 51358:
			case 51360:
			case 51362:
			case 51363:
			case 51364:
			case 51365:
			case 51367:
			case 51368:
			case 51370:
			case 51371:
			case 51396:
			case 51401:
			case 51406:
			case 51426:
			case 51427:
			case 51435:
			case 51440:
			case 51445:
			case 51450:
			case 51473:
			case 51475:
			case 51476:
			case 51478:
			case 51483:
			case 51484:
			case 51485:
			case 51498:
			case 51514:
			case 51516:
			case 51518:
			case 51519:
			case 51539:
			case 51540:
			case 51542:
			case 51543:
			case 52206:
			case 52222:
			case 52242:
			case 52263:

				/**
				 * kuradel dungeon
				 */
			case 2745:
			case 25297:
			case 25332:
			case 25333:
			case 25334:
			case 25335:
			case 25359:
			case 25360:
			case 25361:
			case 39440:
			case 39467:
			case 47232:
			case 4451:
			case 16449:
			case 25296:
			case 25301:
			case 25302:
			case 25303:
			case 25311:
			case 25314:
			case 25318:
			case 25321:
			case 25326:
			case 25327:
			case 25328:
			case 25329:
			case 25330:
			case 25342:
			case 25343:
			case 25345:
			case 25352:
			case 25354:
			case 25356:
			case 25357:
			case 25363:
			case 25364:
			case 29958:
			case 29992:
			case 37024:
			case 37090:
			case 39450:
			case 39451:
			case 47223:
			case 47224:
			case 47225:
			case 47226:
			case 47227:
			case 47228:
			case 47229:
			case 47230:
			case 47231:
			case 47233:
			case 47234:
			case 47235:
			case 47236:
			case 47237:
			case 52838:
			case 52839:
			case 25351:

				/**
				 * smoke dungeon
				 */
				//case 32766:

				/**
				 * jadinko
				 */
			case 12284:
			case 12287:
			case 12289:
			case 12290:
			case 12291:
			case 12320:
			case 12321:
			case 12327:
			case 12333:
			case 12413:
			case 12414:
			case 12415:
			case 12421:
			case 12423:
			case 12424:
			case 12426:
			case 12427:
			case 12452:
			case 12456:
			case 12458:
			case 12484:
			case 12485:
			case 12496:
			case 12504:
			case 12525:
			case 12530:
			case 12541:
			case 12569:
			case 12572:
			case 12588:
			case 12589:
			case 12603:
			case 12604:
			case 12605:
			case 12638:
			case 12639:
			case 12640:
			case 12642:
			case 12643:
			case 12644:
			case 12645:
			case 12646:
			case 12647:
			case 12648:
			case 12650:
			case 44097:
			case 11124:
			case 11129:
			case 11130:
			case 11132:
			case 11133:
			case 11137:
			case 11142:
			case 11143:
			case 11144:
			case 11146:
			case 11147:
			case 11149:
			case 11155:
			case 11376:
			case 11377:

			case 26408:
			case 57225:
			case 39221:
			case 39222:
			case 39223:
			case 40717:
			case 40736:
			case 40741:
			case 40742:
			case 40744:
			case 40745:
			case 40746:
			case 40748:
			case 40750:
			case 40782:
			case 40783:
			case 40784:
			case 40785:
			case 40786:
			case 40801:
			case 40803:
			case 40804:
			case 40805:
			case 40806:
			case 41069:
			case 41276:
			case 41378:
			case 32767:
			case 32768:
			case 33823:
			case 33824:
			case 33825:
			case 33826:
			case 33827:
			case 33828:
			case 33829:
			case 33830:
			case 33831:
			case 33832:
			case 33833:
			case 33834:
			case 33835:
			case 33836:
			case 33837:
			case 33838:
			case 34262:
			case 34268:
			case 34269:
			case 34281:
			case 34282:
			case 34293:
			case 34295:
			case 34296:
			case 34327:
			case 34328:
			case 34329:
			case 34330:
			case 34331:
			case 34342:
			case 34343:
			case 34344:
			case 34345:
			case 34346:
			case 34348:
			case 34349:
			case 34350:
			case 34364:
			case 34365:
			case 34826:
			case 34827:
			case 34841:
			case 34842:
			case 34843:
			case 34923:
			case 14390:
			case 14391:
			case 14392:
			case 14393:
			case 14394:
			case 18303:
			case 18445:
			case 18446:
			case 18447:
			case 18448:
			case 18452:
			case 18456:
			case 18457:
			case 18458:
			case 18459:
			case 20490:
			case 20504:
			case 20525:
			case 20529:
			case 20533:
			case 20545:
			case 20549:
			case 20576:
			case 20577:
			case 20578:
			case 20579:
			case 20583:
			case 20584:
			case 20596:
			case 20597:
			case 20598:
			case 57105:
			case 57558:
			case 84:
			case 18342:
			case 18343:
			case 18344:
			case 18345:
			case 18346:
			case 18361:
			case 20468:
			case 20500:
			case 20530:
			case 20532:
			case 25636:
			case 57557:
			case 57560:
			case 57570:
			case 10370:
			case 53365:
			case 53366:
			case 53368:
			case 53378:
			case 53379:
			case 32157:
				return true;
		}
		return false;
	}


	public static ObjectDef forID(int i) {

		for (int j = 0; j < 20; j++)
			if (cache[j].type == i)
				return cache[j];

		cacheIndex = (cacheIndex + 1) % 20;
		ObjectDef objectDef = cache[cacheIndex];
		try {
			if (useNewDefinition(i) || i > totalObjects) {
				stream2.currentOffset = streamIndices2[i];
			} else {
				stream.currentOffset = streamIndices[i];
			}
		} catch (Exception e) {
		}

		boolean newDefinition = false;
		objectDef.type = i;
		objectDef.setDefaults();
		try {
			Stream str = stream;
			if (useNewDefinition(i) || i > totalObjects) {
				str = stream2;
				newDefinition = true;
			}
			objectDef.readValues(str, i, newDefinition);
		} catch (Exception e) {
			System.out.println("" + i);
			e.printStackTrace();
		}
		if (i == 23271) {
			objectDef.hasActions = false;
			objectDef.isUnwalkable = false;
			objectDef.actions = null;
		}
		if (i == 13830) {
			objectDef.objectModelIDS = null;
			objectDef.objectModelIDS = new int[]{13264};
		}
		/*objectDef.hasActions = true;
		objectDef.actions = new String[5];
        objectDef.actions[3] = i + " " + objectDef.name + " " + (objectDef.objectModelIDS != null ? objectDef.objectModelIDS[0] : -1) + "";*/

		// range path finding fix
		//if(objectDef.height <= 1) {
		//    objectDef.impenetrable = false;
		//}

		return objectDef; //13830 = window, 13098 = wall
	}

	public static void unpackConfig(StreamLoader streamLoader) {
		stream = new Stream(streamLoader.getDataForName("loc.dat"));
		Stream stream = new Stream(streamLoader.getDataForName("loc.idx"));
		totalObjects = stream.readUnsignedWord();

		System.out.println("530 Object Amount: " + totalObjects);

		streamIndices = new int[totalObjects];
		int i = 2;
		for (int j = 0; j < totalObjects; j++) {
			streamIndices[j] = i;
			i += stream.readUnsignedWord();
		}

		stream2 = new Stream(streamLoader.getDataForName("loc667.dat"));
		Stream stream3 = new Stream(streamLoader.getDataForName("loc667.idx"));
		int total = stream3.readUnsignedWord();

		System.out.println("667 Object Amount: " + total);

		streamIndices2 = new int[total];
		i = 2;
		for (int j = 0; j < total; j++) {
			streamIndices2[j] = i;
			i += stream3.readUnsignedWord();
		}

		cache = new ObjectDef[20];
		for (int k = 0; k < 20; k++)
			cache[k] = new ObjectDef();
	}

	public boolean actionsContain(String s) {
		if (actions == null) {
			return false;
		}
		for (String action : actions) {
			if (s == null || action == null) {
				continue;
			}
			if (s.toLowerCase().contains(action.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	private void readValues(Stream stream, int id, boolean latest) {
		int i = -1;
		label0:
		do {
			int j;
			do {
				j = stream.readUnsignedByte();
				if (j == 0)
					break label0;
				if (j == 1) {
					int k = stream.readUnsignedByte();
					if (k > 0)
						if (objectModelIDS == null || lowMem) {
							objectModelType = new int[k];
							objectModelIDS = new int[k];
							for (int k1 = 0; k1 < k; k1++) {
								objectModelIDS[k1] = stream.readUnsignedWord();
								objectModelType[k1] = stream.readUnsignedByte();
							}

						} else {
							stream.currentOffset += k * 3;
						}
				} else if (j == 2)
					name = stream.readString();
				else if (j == 3)
					description = stream.readBytes();
				else if (j == 5) {
					int l = stream.readUnsignedByte();
					if (l > 0)
						if (objectModelIDS == null || lowMem) {
							objectModelType = null;
							objectModelIDS = new int[l];
							for (int l1 = 0; l1 < l; l1++)
								objectModelIDS[l1] = stream.readUnsignedWord();

						} else {
							if (!latest) {
								stream.currentOffset += l * 2;
							}
						}
				} else if (j == 14)
					width = stream.readUnsignedByte();
				else if (j == 15)
					height = stream.readUnsignedByte();
				else if (j == 17)
					isUnwalkable = false;
				else if (j == 18)
					impenetrable = false;
				else if (j == 19) {
					i = stream.readUnsignedByte();
					if (i == 1)
						hasActions = true;
				} else if (j == 21)
					adjustToTerrain = true;
				else if (j == 22)
					nonFlatShading = false;
				else if (j == 23)
					occludes = true;
				else if (j == 24) {
					animationId = stream.readUnsignedWord();
					if (animationId == 65535)
						animationId = -1;
				} else if (j == 28)
					decorDisplacement = stream.readUnsignedByte();
				else if (j == 29)
					brightness = stream.readSignedByte();
				else if (j == 39)
					contrast = stream.readSignedByte();
				else if (j >= 30 && j < 39) {
					if (actions == null)
						actions = new String[5];
					actions[j - 30] = stream.readString();
					if (actions[j - 30].equalsIgnoreCase("hidden"))
						actions[j - 30] = null;
				} else if (j == 40) {
					int i1 = stream.readUnsignedByte();
					modifiedModelColors = new int[i1];
					originalModelColors = new int[i1];
					for (int i2 = 0; i2 < i1; i2++) {
						modifiedModelColors[i2] = stream.readUnsignedWord();
						originalModelColors[i2] = stream.readUnsignedWord();
					}

				} else if (j == 60)
					mapFunctionID = stream.readUnsignedWord();
				else if (j == 62)
					inverted = true;
				else if (j == 64)
					castsShadow = false;
				else if (j == 65)
					modelSizeX = stream.readUnsignedWord();
				else if (j == 66)
					modelSizeH = stream.readUnsignedWord();
				else if (j == 67)
					modelSizeY = stream.readUnsignedWord();
				else if (j == 68)
					mapSceneID = stream.readUnsignedWord();
				else if (j == 69)
					surroundings = stream.readUnsignedByte();
				else if (j == 70)
					offsetX = stream.readSignedWord();
				else if (j == 71) {
					offsetH = stream.readSignedWord();
				} else if (j == 72)
					offsetY = stream.readSignedWord();
				else if (j == 73)
					groundObstructive = true;
				else if (j == 74) {
					ethereal = true;
				} else if (j == 80) {
					int len = stream.readUnsignedByte();
					modifiedTextureFace = new short[len];
					originalTextureFace = new short[len];
					for (int i2 = 0; i2 < len; i2++) {
						modifiedTextureFace[i2] = (short) stream.readUnsignedWord();
						originalTextureFace[i2] = (short) stream.readUnsignedWord();
					}
				} else {
					if (j != 75)
						continue;
					supportItems = stream.readUnsignedByte();
				}
				continue label0;
			} while (j != 77);
			variableIDBitfield = stream.readUnsignedWord();
			if (variableIDBitfield == 65535)
				variableIDBitfield = -1;
			variableID = stream.readUnsignedWord();
			if (variableID == 65535)
				variableID = -1;
			int j1 = stream.readUnsignedByte();
			alternativeIDS = new int[j1 + 1];
			for (int j2 = 0; j2 <= j1; j2++) {
				alternativeIDS[j2] = stream.readUnsignedWord();
				if (alternativeIDS[j2] == 65535)
					alternativeIDS[j2] = -1;
			}

		} while (true);
		if (ethereal) {
			isUnwalkable = false;
			impenetrable = false;
		}
		if (supportItems == -1)
			supportItems = isUnwalkable ? 1 : 0;
	}

	private void setDefaults() {
		objectModelIDS = null;
		objectModelType = null;
		name = null;
		description = null;
		modifiedModelColors = null;
		originalModelColors = null;
		modifiedTextureFace = null;
		originalTextureFace = null;
		width = 1;
		height = 1;
		isUnwalkable = true;
		impenetrable = true;
		hasActions = false;
		adjustToTerrain = false;
		nonFlatShading = false;
		occludes = false;
		animationId = -1;
		decorDisplacement = 16;
		brightness = 0;
		contrast = 0;
		actions = null;
		mapFunctionID = -1;
		mapSceneID = -1;
		inverted = false;
		castsShadow = true;
		modelSizeX = 128;
		modelSizeH = 128;
		modelSizeY = 128;
		surroundings = 0;
		offsetX = 0;
		offsetH = 0;
		offsetY = 0;
		groundObstructive = false;
		ethereal = false;
		supportItems = -1;
		variableIDBitfield = -1;
		variableID = -1;
		alternativeIDS = null;
	}

}
