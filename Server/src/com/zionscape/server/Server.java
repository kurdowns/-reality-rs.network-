package com.zionscape.server;

import com.google.common.eventbus.EventBus;
import com.zionscape.server.cache.CacheLoader;
import com.zionscape.server.gamecycle.GameCycleTaskHandler;
import com.zionscape.server.model.clan.ClanManager;
import com.zionscape.server.model.content.cannon.DwarfCannon;
import com.zionscape.server.model.content.grandexchange.GrandExchange;
import com.zionscape.server.model.content.minigames.DuelArena;
import com.zionscape.server.model.content.minigames.FightPits;
import com.zionscape.server.model.content.minigames.Lottery;
import com.zionscape.server.model.content.minigames.PestControl;
import com.zionscape.server.model.items.Item;
import com.zionscape.server.model.items.ItemDefinition;
import com.zionscape.server.model.npcs.NPC;
import com.zionscape.server.model.npcs.NPCHandler;
import com.zionscape.server.model.npcs.drops.NPCDropsHandler;
import com.zionscape.server.model.players.Player;
import com.zionscape.server.model.players.PlayerHandler;
import com.zionscape.server.model.players.PlayerSave;
import com.zionscape.server.model.players.skills.agility.AgilityHandler;
import com.zionscape.server.model.players.skills.hunter.Hunter;
import com.zionscape.server.model.players.skills.summoning.Summoning;
import com.zionscape.server.model.region.RegionRepository;
import com.zionscape.server.net.LoginLimitFilter;
import com.zionscape.server.net.PipelineFactory;
import com.zionscape.server.plugin.PluginLoader;
import com.zionscape.server.plugin.impl.npcs.SirElitePlugin;
import com.zionscape.server.scripting.Scripting;
import com.zionscape.server.scripting.messages.handler.MessageHandlerChainGroup;
import com.zionscape.server.tick.Tick;
import com.zionscape.server.tick.TickManager;
import com.zionscape.server.util.DatabaseUtil;
import com.zionscape.server.util.LoggingOutputStream;
import com.zionscape.server.util.StdOutErrLevel;
import com.zionscape.server.util.StopWatch;
import com.zionscape.server.world.ItemDrops;
import com.zionscape.server.world.ObjectManager;
import com.zionscape.server.world.shops.Shops;
import org.apollo.jagcached.FileServer;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Server.java
 *
 * @author Tyler - Head developer
 * @author Tyler - Eximinus Founder & Owner
 * @author Stuart
 * @version 1.10
 */
public final class Server {

	/**
	 *
	 */
	public static final long SERVER_STARTED_AT = System.currentTimeMillis();
	private static final int CYCLE_RATE = 600;
	/**
	 *
	 */
	private static final ExecutorService workService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
	/**
	 * This world's {@link RegionRepository}.
	 */
	private static final RegionRepository regions = RegionRepository.immutable();
	private static final StopWatch stopWatch = new StopWatch();
	// all this shit below here needs cleaning
	public static Lottery lottery = new Lottery();
	public static boolean UpdateServer = false;
	public static boolean shutdownServer = false;
	public static ClanManager clanManager = new ClanManager();
	public static ObjectManager objectManager = new ObjectManager();
	public static FightPits fightPits = new FightPits();
	public static int yes, no;
	public static Server server;
	private static EventBus eventBus = new EventBus();
	/**
	 * The {@link MessageHandlerChainGroup}.
	 */
	private static MessageHandlerChainGroup chainGroup = new MessageHandlerChainGroup(new HashMap<>());
	/**
	 *
	 */
	private static Properties settings = new Properties();
	private static TickManager tickManager = new TickManager();
	private static SimpleDateFormat format = new SimpleDateFormat("M-d");
	private static String date = null;

	public static void loadSettings() throws IOException {
		settings.load(new FileInputStream("settings.ini"));
	}

	public static TickManager getTickManager() {
		return tickManager;
	}

	public static void main(String[] argv) {
		// initialise logging to file
		setLogger();

		System.out.println("Launching Varrock.org Game Server");

		GameCycleTaskHandler.addEvent(null, container -> {
			PlayerHandler.yell("Double xp has been finished!!");
			Config.DOUBLE_XP = false;
			container.stop();
		}, (24 * 60 * 60 * 1000) / 600); // 12 hours 12 * 60 * 60 * 1000 / 600

		try {
			shutdownServer = false;
			settings.load(new FileInputStream("settings.ini"));

			DatabaseUtil.init();

			CountDownLatch latch = new CountDownLatch(4);

			new Thread(() -> {
				try {
					new FileServer().start();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					latch.countDown();
				}
			}).start();

			new Thread(() -> {
				try {
					CacheLoader.load();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					latch.countDown();
				}
			}).start();

			new Thread(() -> {
				try {
					PluginLoader.load();
					GrandExchange.loadOffers();
					Item.load();
					ItemDefinition.load();
					NPCHandler.load();
					GrandExchange.init();
					NPCDropsHandler.loadDrops();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					latch.countDown();
				}
			}).start();

			new Thread(() -> {
				try {
					ItemDrops.load();
					Summoning.loadConfiguration();
					objectManager.loadRemovedObjects();
					Connection.initialize();
					Shops.load();
					Scripting.load();
					AgilityHandler.loadObstacles();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					latch.countDown();
				}
			}).start();

			latch.await();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		ServerBootstrap serverBootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		serverBootstrap.setPipelineFactory(new PipelineFactory(new HashedWheelTimer()));
		serverBootstrap.bind(new InetSocketAddress(43594));

		/**
		 * Server Successfully Loaded
		 */
		System.out.println("Server listening on port 0.0.0.0:43594");

		while (true) {
			// see if we need a new logger when the date changes, certainly not the best way to do it but will do for now
			setLogger();

			LoginLimitFilter.getInstance().process();

			try {
				stopWatch.reset();
				long start = System.currentTimeMillis();
				Iterator<Tick> tickIt$ = Server.getTickManager().getTickables().iterator();
				while (tickIt$.hasNext()) {
					Tick t = tickIt$.next();
					try {
						t.cycle();
					} catch (Exception e) {

						t.stop();

						System.out.println(e.getMessage());
					}
				}

				//  System.out.println("cycle events");

				stopWatch.start();
				GameCycleTaskHandler.process();
				stopWatch.stop("cycle events");

				GrandExchange.process(false);

				stopWatch.start();
				NPCHandler.process();
				stopWatch.stop("npc process");

				// System.out.println("player process");

				stopWatch.start();
				PlayerHandler.process();
				stopWatch.stop("player process");

				// clear update flags
				for (NPC npc : NPCHandler.npcs) {
					if (npc == null) {
						continue;
					}
					npc.clearUpdateFlags();
				}
				for (Player player : PlayerHandler.players) {
					if (player == null) {
						continue;
					}
					player.clearUpdateFlags();
				}

				stopWatch.start();
				Shops.process();
				stopWatch.stop("shop process");

				stopWatch.start();
				Server.objectManager.process();
				stopWatch.stop("object manager process");

				stopWatch.start();
				ItemDrops.process();
				stopWatch.stop("item drops");

				stopWatch.start();
				Server.lottery.process();
				stopWatch.stop("lottery");

				stopWatch.start();
				Server.fightPits.process();
				stopWatch.stop("fight pits");

				PestControl.process();
				Hunter.process();


				long taken = System.currentTimeMillis() - start;
				if (taken > 600) {
					System.out.println("Server lag: " + taken + "ms");

					DatabaseUtil.printDebug();

					stopWatch.print();
					PlayerHandler.stopWatch.print();
				}
				if (taken < CYCLE_RATE) {
					Thread.sleep(CYCLE_RATE - taken);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void shutdown() {
		System.out.println("Saving all players.");

		GrandExchange.process(true);

		for (Player p : PlayerHandler.players) {
			if (p == null) {
				continue;
			}
			if (p.inTrade) {
				p.getTradeAndDuel().declineTrade();
			}
			if (p.getDuel() != null) {
				DuelArena.serverClosed(p);
			}
			if (p.getCannon() != null) {
				DwarfCannon.pickupCannon(p, -1, null, true);
			}

			ServerEvents.onPlayerLoggedOut(p);
			GameCycleTaskHandler.stopEvents(p);

			PlayerSave.saveGame(p);
		}

		System.out.println("saving complete");

		System.exit(0);
	}

	/**
	 * Gets a setting stored in settings.ini
	 *
	 * @param key The setting key name
	 * @return The value for the setting
	 */
	public static String getSetting(String key) {
		return settings.getProperty(key);
	}

	/**
	 * @param runnable
	 */
	public static void submitWork(Runnable runnable) {
		workService.submit(runnable);
	}

	/**
	 * @return
	 */
	public static MessageHandlerChainGroup getMessageHandlerChains() {
		return chainGroup;
	}

	/**
	 * Gets this world's {@link RegionRepository}.
	 *
	 * @return The RegionRepository.
	 */
	public static RegionRepository getRegionRepository() {
		return regions;
	}

	/**
	 * @param tick
	 */
	public static void submit(final Tick tick) {
		tickManager.submit(tick);
	}

	private static void setLogger() {
		String dateNow = format.format(Calendar.getInstance().getTime());
		if (date == null || !date.equalsIgnoreCase(dateNow)) {
			date = dateNow;
			try {
				Handler fileHandler = new FileHandler("./logs/" + date + ".log", true);
				fileHandler.setFormatter(new SimpleFormatter());
				Logger.getLogger("").addHandler(fileHandler);

				// now rebind stdout/stderr to logger
				Logger logger;
				LoggingOutputStream los;

				logger = Logger.getLogger("stdout");
				los = new LoggingOutputStream(logger, StdOutErrLevel.STDOUT);
				System.setOut(new PrintStream(los, true));

				logger = Logger.getLogger("stderr");
				los = new LoggingOutputStream(logger, StdOutErrLevel.STDERR);
				System.setErr(new PrintStream(los, true));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static EventBus getEventBus() {
		return eventBus;
	}
}