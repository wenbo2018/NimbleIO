package com.generallycloud.nio.component.protocol.nio;

import java.io.IOException;

import com.generallycloud.nio.buffer.ByteBuf;
import com.generallycloud.nio.component.NIOContext;
import com.generallycloud.nio.component.Session;
import com.generallycloud.nio.component.protocol.IOReadFuture;
import com.generallycloud.nio.component.protocol.ProtocolDecoderAdapter;
import com.generallycloud.nio.component.protocol.nio.future.NIOReadFutureImpl;

/**
 * <pre>
 *  B0 - B10:
 * 
 *  B0：
 *  +-----------------------------------+
 *  |                 B0                |
 *  +   -   -   -   -   -   -   -   -   +
 *  |   0   1   2   3   4   5   6   7   | 
 *  |   -   -   -   -   -   -   -   -   + 
 *  |  T Y P E|      Service  Name      |
 *  +-----------------------------------+
 *  
 *  Type:高两位，类型 [0=RESERVED，1=RESERVED，2=RESERVED, 3=BEAT]
 *  ServiceName:低六位，service name的长度
 *  
 *  B1 - B3 ：future id
 *  B4 - B5 ：text content的长度
 *  B6 - B8 ：binary content的长度
 * 
 * </pre>
 */
public class NIOProtocolDecoder extends ProtocolDecoderAdapter {
	
	public static final byte	TYPE_BEAT					= 3;
	public static final int	PROTOCOL_HADER				= 9;
	public static final int	FUTURE_ID_BEGIN_INDEX			= 1;
	public static final int	BINARY_BEGIN_INDEX			= 6;
	public static final int	TEXT_BEGIN_INDEX				= 4;

	protected ByteBuf allocate(NIOContext context) {
		return context.getHeapByteBufferPool().allocate(PROTOCOL_HADER);
	}

	protected IOReadFuture fetchFuture(Session session, ByteBuf buffer) throws IOException {
		
		byte _type = buffer.get(0);
		
		int type = (_type & 0xff) >> 6;
		
		if (type == TYPE_BEAT) {
			return new NIOReadFutureImpl(session,true);
		}
		
		return new NIOReadFutureImpl(session, buffer);
	}

}
