package com.generallycloud.nio.component.protocol.fixedlength.future;

import java.io.IOException;

import com.generallycloud.nio.buffer.ByteBuf;
import com.generallycloud.nio.common.ReleaseUtil;
import com.generallycloud.nio.component.Session;
import com.generallycloud.nio.component.protocol.AbstractIOReadFuture;
import com.generallycloud.nio.component.protocol.ProtocolException;
import com.generallycloud.nio.component.protocol.fixedlength.FixedLengthProtocolDecoder;

public class FixedLengthReadFutureImpl extends AbstractIOReadFuture implements FixedLengthReadFuture {

	private ByteBuf	buffer;

	private String		text;

	private int		length;

	private boolean	header_complete;

	private boolean	body_complete;

	private byte[]	byteArray;

	private int		limit	= 1024 * 1024;

	public FixedLengthReadFutureImpl(Session session, ByteBuf buf) {
		
		super(session);
		
		this.buffer = buf;

		if (!isHeaderReadComplete(buf)) {
			doHeaderComplete(buf);
		}
	}

	public FixedLengthReadFutureImpl(Session session) {
		super(session);
	}
	
	protected FixedLengthReadFutureImpl(Session session,boolean isBeatPacket) {
		super(session);
		this.isBeatPacket = isBeatPacket;
	}
	
	private boolean isHeaderReadComplete(ByteBuf buf){
		return buf.position() > FixedLengthProtocolDecoder.PROTOCOL_HADER;
	}

	private void doHeaderComplete(ByteBuf buffer) {

		header_complete = true;
		
		int length = buffer.getInt(0);
		
		this.length = length;
		
		if (length < 1) {
			
			if (length == -1) {
			
				isBeatPacket = true;
				
				body_complete = true;
				
				return;
			}
			
			throw new ProtocolException("illegal length:" + length);
			
		}else if (length > limit) {
			
			ReleaseUtil.release(buffer);
			
			throw new ProtocolException("max 1M ,length:" + length);
			
		}else if(length > buffer.capacity()){
			
			ReleaseUtil.release(buffer);
			
			this.buffer = endPoint.getContext().getDirectByteBufferPool().allocate(length);
			
		}else{
			
			buffer.limit(length);
		}
	}

	public boolean read() throws IOException {

		ByteBuf buffer = this.buffer;
		
		if (!header_complete) {

			buffer.read(endPoint);

			if (!isHeaderReadComplete(buffer)) {
				return false;
			}

			doHeaderComplete(buffer);
		}

		if (!body_complete) {

			buffer.read(endPoint);

			if (buffer.hasRemaining()) {
				return false;
			}

			doBodyComplete(buffer);
		}
		
		return true;
	}

	private void doBodyComplete(ByteBuf buf) {

		body_complete = true;

		//FIXME 
		byteArray = new byte[buf.limit()];
		
		buf.flip();
		
		buf.get(byteArray);
	}

	public String getServiceName() {
		return null;
	}

	public String getText() {

		if (text == null) {
			text = new String(byteArray, session.getContext().getEncoding());
		}

		return text;
	}
	
	public int getLength() {
		return length;
	}

	public byte[] getByteArray() {
		return byteArray;
	}

	public void release() {
		ReleaseUtil.release(buffer);
	}
	
}
