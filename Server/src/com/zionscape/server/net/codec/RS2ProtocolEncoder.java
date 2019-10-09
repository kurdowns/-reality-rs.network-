package com.zionscape.server.net.codec;

import com.zionscape.server.net.Packet;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public class RS2ProtocolEncoder extends OneToOneEncoder {

	@Override
	protected Object encode(final ChannelHandlerContext arg0, final Channel arg1, final Object in) throws Exception {
		Packet p = (Packet) in;
		if (p.isRaw()) {
			return p.getPayload();
		} else {
			/*
			* Get the packet attributes.
            */
			int opcode = p.getOpcode();
			Packet.Type type = p.getType();
			int length = p.getLength();

            /*
			 * Compute the required size for the buffer.
             */
			int finalLength = length + 1;
			switch (type) {
				case VARIABLE:
					finalLength += 1;
					break;
				case VARIABLE_SHORT:
					finalLength += 2;
					break;
			}

            /*
             * Create the buffer and write the opcode (and length if the
             * packet is variable-length).
             */
			ChannelBuffer buffer = ChannelBuffers.buffer(finalLength);
			buffer.writeByte((byte) opcode);
			switch (type) {
				case VARIABLE:
					buffer.writeByte((byte) length);
					break;
				case VARIABLE_SHORT:
					buffer.writeShort((short) length);
					break;
			}

            /*
             * Write the payload itself.
             */
			buffer.writeBytes(p.getPayload());

			return buffer;
		}
	}

}