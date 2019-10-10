package net.crandor;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

class SpriteLoader {

    static boolean dumpImages = false;
    static Sprite[] sprites = null;
    private int id;
    private int drawOffsetX;
    private int drawOffsetY;
    private byte[] spriteData;

    /**
     * Sets the default values.
     */
    public SpriteLoader() {
        id = -1;
        drawOffsetX = 0;
        drawOffsetY = 0;
        spriteData = null;
    }

    /**
     * Loads the sprite data and index files from the cache location. This can
     * be edited to use an archive such as config or media to load from the
     * cache.
     *
     * @param archive
     */
	public static void loadSprites(StreamLoader streamLoader) {
        byte[] index = streamLoader.getDataForName("sprites.idx");
        byte[] data = streamLoader.getDataForName("sprites.dat");
		try (DataInputStream indexFile = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(index))); DataInputStream dataFile = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(data)))) {
			int totalSprites = indexFile.readInt();
			sprites = new Sprite[totalSprites];
			SpriteLoader spriteLoader = new SpriteLoader();
			for (int i = 0; i < totalSprites; i++) {
				indexFile.readInt();
				spriteLoader.readValues(indexFile, dataFile);
				createSprite(spriteLoader);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * Creates a sprite out of the spriteData.
     *
     * @param sprite
     */
    private static void createSprite(SpriteLoader sprite) {
        if (dumpImages) {
            File directory = new File(signlink.findcachedir() + "dump/");
            if (!directory.exists()) {
                directory.mkdir();
            }
            Utility.writeFile(new File(directory.getAbsolutePath() + System.getProperty("file.separator") + sprite.id + ".png"), sprite.spriteData);
        }
        sprites[sprite.id] = new Sprite(sprite.spriteData);
        sprites[sprite.id].offsetX = sprite.drawOffsetX;
        sprites[sprite.id].offsetY = sprite.drawOffsetY;
    }

    /**
     * Reads the information from the index and data files.
     *
     * @param index holds the sprite indices
     * @param data  holds the sprite data per index
     * @throws IOException
     */
    private void readValues(DataInputStream index, DataInputStream data)
            throws IOException {
        do {
            int opCode = data.readByte();
            if (opCode == 0) {
                break;
            }
            if (opCode == 1) {
                id = data.readShort();
            } else if (opCode == 2) {
            	data.readUTF().intern();
            } else if (opCode == 3) {
                drawOffsetX = data.readShort();
            } else if (opCode == 4) {
                drawOffsetY = data.readShort();
            } else if (opCode == 5) {
                int indexLength = index.readInt();
                byte[] dataread = new byte[indexLength];
                data.readFully(dataread);
                spriteData = dataread;
            }
        } while (true);
    }
}
