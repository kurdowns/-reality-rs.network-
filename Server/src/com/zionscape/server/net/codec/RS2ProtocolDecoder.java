package com.zionscape.server.net.codec;

import com.zionscape.server.net.Packet;
import com.zionscape.server.net.Packet.Type;
import com.zionscape.server.util.ISAACRandomGen;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * RS2ProtocolDecoder
 *
 * @author Lmctruck30
 * @version 1.0
 */
public class RS2ProtocolDecoder extends FrameDecoder {

	public static final int[] PACKET_SIZES = {0,0,-1,1,-1,0,0,0,0,0,0,0,0,0,8,0,6,2,2,0,0,2,0,6,0,12,0,0,-1,0,0,0,0,0,0,8,4,0,0,2,2,6,0,6,0,-1,0,0,0,0,0,0,0,12,0,0,0,8,8,12,8,8,0,0,0,0,0,0,0,3,6,0,2,2,8,6,0,-1,0,6,0,0,0,0,0,1,4,6,0,0,0,0,0,0,0,3,0,0,-1,0,0,25,0,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,6,0,0,1,0,6,0,0,0,-1,-1,2,6,0,6,6,8,0,6,0,0,0,2,0,0,0,0,0,6,0,0,0,0,0,0,1,2,0,2,6,0,0,0,0,0,0,0,-1,-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,0,3,0,2,0,0,8,1,0,0,12,0,0,0,0,0,0,0,2,0,0,0,2,8,0,0,4,10,4,0,0,4,11,8,0,0,10,0,0,0,0,0,0,0,-1,0,6,0,1,0,0,0,6,0,6,8,1,0,0,4,0,0,0,0,-1,0,-1,6,0,0,6,6,0,0,0,};
	private final ISAACRandomGen cipher;
	private int opcode = -1;
	private int size = -1;

	public RS2ProtocolDecoder(ISAACRandomGen cipher) {
		this.cipher = cipher;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (opcode == -1) {
			if (buffer.readableBytes() >= 1) {
				opcode = buffer.readByte() & 0xFF;
				opcode = (opcode - cipher.getNextKey()) & 0xFF;
				size = PACKET_SIZES[opcode];
			} else {
				return null;
			}
		}
		if (size == -1) {
			if (buffer.readableBytes() >= 1) {
				size = buffer.readByte() & 0xFF;
			} else {
				return null;
			}
		}
		if (buffer.readableBytes() >= size) {
			final byte[] data = new byte[size];
			buffer.readBytes(data);
			final ChannelBuffer payload = ChannelBuffers.buffer(size);
			payload.writeBytes(data);
			try {
				return new Packet(opcode, Type.FIXED, payload);
			} finally {
				opcode = -1;
				size = -1;
			}
		}
		return null;
	}
}
