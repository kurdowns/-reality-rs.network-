package com.zionscape.server.cache;

import com.zionscape.server.Config;
import com.zionscape.server.cache.clientref.*;
import com.zionscape.server.util.Stream;

import java.io.*;
import java.nio.file.Files;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;

public class CacheLoader {

	/**
	 * Decompresses a byte array
	 *
	 * @param compressedByteArray
	 * @return
	 * @throws java.io.IOException
	 */
	private static byte[] decompressGzipByteArray(byte[] compressedByteArray) throws IOException {
		ByteArrayOutputStream uncompressedStream = new ByteArrayOutputStream();
		GZIPInputStream compressedStream = new GZIPInputStream(new ByteArrayInputStream(compressedByteArray));
		byte[] buffer = new byte[1024];
		int index = -1;
		while ((index = compressedStream.read(buffer)) != -1) {
			uncompressedStream.write(buffer, 0, index);
		}
		return uncompressedStream.toByteArray();
	}

	/**
	 * Loads the map data from the cache
	 */
	@SuppressWarnings("resource")
	public static void load() {
		try {
			/**
			 * Load the cache data file
			 */
			RandomAccessFile cacheData = new RandomAccessFile("./data/cache/main_file_cache.dat", "rw");

			/**
			 * Load the index files for each archive in the data file
			 */
			RandomAccessFile[] cacheIndex = new RandomAccessFile[7];
			for (int i = 0; i < 7; i++) {
				cacheIndex[i] = new RandomAccessFile("./data/cache/main_file_cache.idx" + i, "rw");
			}

			/**
			 * Stores each of the archives
			 */
			Decompressor[] archives = new Decompressor[7];

			/**
			 * Decompress each archive
			 */
			for (int i = 0; i < 7; i++) {
				archives[i] = new Decompressor(cacheData, cacheIndex[i], i + 1);
			}


			/**
			 * For checking login crcs
			 */
			CRC32 crc = new CRC32();
			for (int i = 1; i < 9; i++) {
				crc.reset();
				crc.update(archives[0].decompress(i));
				Config.EXPECTED_CRCS[i] = (int) crc.getValue();
			}

			String[] names = {"models", "anims", "music", "maps", "textures", "osrs"};

			// cache packing
			for (int i = 0; i < names.length; i++) {
				File directory = new File("./data/cache/" + names[i]);
				if (directory.listFiles() != null && directory.listFiles().length > 0) {
					for (File f : directory.listFiles()) {
						byte[] buf = Files.readAllBytes(f.toPath());
						int id = Integer.parseInt(f.getName().substring(0, f.getName().indexOf(".")));
						archives[i + 1].pack(buf.length, buf, id);

						System.out.println("packed " + id + " into index " + (i + 1));
					}
				}
			}

			/**
			 * Get the configuration archive in index 0
			 */
			StreamLoader configArchive = new StreamLoader(archives[0].decompress(2));

			/**
			 * Load the object definitions
			 */
			ObjectDef.unpackConfig(configArchive);

			/**
			 * Load item definitions
			 */
			ItemDef.unpackConfig(configArchive);

			/**
			 * Load idk
			 */
			IdentityKit.unpackConfig(configArchive);

			/**
			 * Get the version archive in index 0
			 */
			StreamLoader versionArchive = new StreamLoader(archives[0].decompress(5));

			/**
			 * Load the map index from the version archive
			 */
			byte[] mapIndex = versionArchive.getDataForName("map_index");
			Stream stream = new Stream(mapIndex);

			/**
			 * The amount of regions
			 */
			int size = stream.readUnsignedWord();
			System.out.println("Map Amount: " + size);

			/**
			 * Load the data from the map_index
			 */
			for (int i = 0; i < size; i++) {
				int regionId = stream.readUnsignedWord();
				int mapObjectsId = stream.readUnsignedWord();
				int mapLandscapeId = stream.readUnsignedWord();
				try {
					load(cacheData, cacheIndex[4], regionId, mapLandscapeId, mapObjectsId);
				} catch (Exception e) {
					//System.err.println("Error loading 530 region " + regionId + " " + e.toString());
				}
			}

			/**
			 * Close all resources
			 */
			cacheData.close();
			for (int i = 0; i < 5; i++) {
				cacheIndex[i].close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void load(RandomAccessFile cacheData, RandomAccessFile indexFile, int regionId, int mapObjectId, int landscapeId) throws Exception {
		Decompressor archive = new Decompressor(cacheData, indexFile, 5);

		Stream objStream = new Stream(decompressGzipByteArray(archive.decompress(mapObjectId)));
		Stream mapStream = new Stream(decompressGzipByteArray(archive.decompress(landscapeId)));

		Collision.loadMaps(regionId, objStream, mapStream);
	}

}