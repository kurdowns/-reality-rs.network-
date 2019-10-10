package net.crandor;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by stuart on 13/04/14.
 */
public class CacheDownloader {

	private static final String URL_LOCATION = "http://" + Constants.SERVER_IP + ":43597/cache.zip";
	private static final String CACHE_DIR = signlink.findcachedir() + "/";

	public static void start(Client c) throws Exception {
		if (Client.preloadedCache) {
			return;
		}

		c.drawLoadingText(0, "Downloading full cache...");

		URL url = new URL(URL_LOCATION);
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
		int fileSize = conn.getContentLength();
		/**
		 * Download the file
		 */
		long startedAt = System.currentTimeMillis();
		long lastSpeedUpdate = 0;
		int totalRead = 0;
		int available;
		byte[] buff = new byte[1024];

		ByteArrayOutputStream bais = new ByteArrayOutputStream();
		InputStream is = conn.getInputStream();

		while ((available = is.read(buff)) > 0) {
			bais.write(buff, 0, available);
			totalRead += available;

			if (System.currentTimeMillis() - lastSpeedUpdate > 1000) {
				int speed = (int) (totalRead / 1024
						/ (1L + (System.currentTimeMillis() - startedAt) / 1000L));
				int percentage = (int) (((double) totalRead / (double) fileSize) * 100D);

				c.drawLoadingText(percentage, "Downloading cache - " + speed + "Kb/s");

				lastSpeedUpdate = System.currentTimeMillis();
			}
		}

		/**
		 * Unzip it
		 */
		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(bais.toByteArray()));
		ZipEntry entry;
		while ((entry = zis.getNextEntry()) != null) {
			FileOutputStream out = new FileOutputStream(CACHE_DIR + entry.getName());

			while ((available = zis.read(buff)) != -1) {
				out.write(buff, 0, available);
			}

			out.close();
			zis.closeEntry();
		}

		/**
		 * clean up
		 */
		is.close();
		zis.close();

		Client.preloadedCache = true;
		Settings.write();

	}

}
