package com.zionscape.server.net;

import com.zionscape.server.net.codec.RS2LoginProtocol;
import com.zionscape.server.net.codec.RS2ProtocolEncoder;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.Timer;

public class PipelineFactory implements ChannelPipelineFactory {

	private final Timer timer;

	public PipelineFactory(Timer timer) {
		this.timer = timer;
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		final ChannelPipeline pipeline = new DefaultChannelPipeline();
		pipeline.addFirst("loginlimit", LoginLimitFilter.getInstance());
		pipeline.addLast("timeout", new ReadTimeoutHandler(timer, 30));
		pipeline.addLast("encoder", new RS2ProtocolEncoder());
		pipeline.addLast("decoder", new RS2LoginProtocol());
		pipeline.addLast("handler", new ChannelHandler());
		return pipeline;
	}
}
