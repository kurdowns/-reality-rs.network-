package org.apollo.jagcached;

import org.apollo.jagcached.dispatch.RequestWorkerPool;
import org.apollo.jagcached.net.*;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The core class of the file server.
 *
 * @author Graham
 */
public final class FileServer {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(FileServer.class.getName());
	/**
	 * The executor service.
	 */
	private final ExecutorService service = Executors.newCachedThreadPool();
	/**
	 * The request worker pool.
	 */
	private final RequestWorkerPool pool = new RequestWorkerPool();
	/**
	 * The file server event handler.
	 */
	private final FileServerHandler handler = new FileServerHandler();
	/**
	 * The timer used for idle checking.
	 */
	private final Timer timer = new HashedWheelTimer();

	/**
	 * The entry point of the application.
	 *
	 * @param args The command-line arguments.
	 */
	public static void main(String[] args) {
		try {
			new FileServer().start();
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Error starting server.", t);
		}
	}

	/**
	 * Starts the file server.
	 *
	 * @throws Exception if an error occurs.
	 */
	public void start() throws Exception {

		String[] fileNames = {
				"main_file_cache.dat",
				"main_file_cache.idx0",
				"main_file_cache.idx1",
				"main_file_cache.idx2",
				"main_file_cache.idx3",
				"main_file_cache.idx4",
				"main_file_cache.idx5"
		};

		logger.info("Compressing cache");
		try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream("./data/www/cache.zip"))) {
			for (String s : fileNames) {
				try (FileInputStream in = new FileInputStream(new File("./data/cache/" + s))) {
					ZipEntry entry = new ZipEntry(s);
					out.putNextEntry(entry);

					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = in.read(buffer)) != -1) {
						out.write(buffer, 0, bytesRead);
					}
				}
			}
		}

		logger.info("Starting workers...");
		pool.start();

		logger.info("Starting services...");
		try {
			start("HTTP", new HttpPipelineFactory(handler, timer), NetworkConstants.HTTP_PORT);
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Failed to start HTTP service.", t);
			logger.warning("HTTP will be unavailable. JAGGRAB will be used as a fallback by clients but this isn't reccomended!");
		}
		start("JAGGRAB", new JagGrabPipelineFactory(handler, timer), NetworkConstants.JAGGRAB_PORT);
		start("ondemand", new OnDemandPipelineFactory(handler, timer), NetworkConstants.SERVICE_PORT);

		logger.info("Ready for connections.");
	}

	/**
	 * Starts the specified service.
	 *
	 * @param name            The name of the service.
	 * @param pipelineFactory The pipeline factory.
	 * @param port            The port.
	 */
	private void start(String name, ChannelPipelineFactory pipelineFactory, int port) {
		SocketAddress address = new InetSocketAddress(port);

		logger.info("Binding " + name + " service to " + address + "...");

		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.setFactory(new NioServerSocketChannelFactory(service, service));
		bootstrap.setPipelineFactory(pipelineFactory);
		bootstrap.bind(address);
	}

}