package com.zionscape.server.net;

import org.jboss.netty.channel.*;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LoginLimitFilter extends SimpleChannelHandler {

	private static LoginLimitFilter instance = new LoginLimitFilter();

	private final List<Attempt> attempts = new ArrayList<Attempt>();

	public static LoginLimitFilter getInstance() {
		return instance;
	}

	public void process() {
		synchronized (attempts) {
			final Iterator<Attempt> itr = attempts.iterator();
			while (itr.hasNext()) {
				final Attempt attempt = itr.next();
				if (System.currentTimeMillis() - attempt.time > (60 * 1000)) {
					itr.remove();
					continue;
				}
			}
		}
	}

	private int getAttempts(final String address) {
		synchronized (attempts) {
			int count = 0;
			for (final Attempt attempt : attempts)
				if (attempt.address.equalsIgnoreCase(address))
					count++;
			return count;
		}
	}

	public void addAttempt(final Channel channel) {
		synchronized (attempts) {
			attempts.add(new Attempt(channelToIp(channel)));
		}
	}

	private String channelToIp(final Channel channel) {
		return ((InetSocketAddress) channel.getRemoteAddress()).getAddress().getHostAddress();
	}

	@Override
	public void channelConnected(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception {
		if (getAttempts(channelToIp(ctx.getChannel())) >= 5) {
			sendReturnCode(ctx.getChannel(), 16);
			return;
		}
		super.channelConnected(ctx, e);
	}

	public void sendReturnCode(final Channel channel, final int code) {
		channel.write(new PacketBuilder().putLong(0).put((byte) code).toPacket()).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(final ChannelFuture arg0) throws Exception {
				arg0.getChannel().close();
			}
		});
	}

	private class Attempt {

		public String address;
		public long time;

		public Attempt(final String address) {
			this.address = address;
			this.time = System.currentTimeMillis();
		}
	}

}
