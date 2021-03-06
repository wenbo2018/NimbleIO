package com.generallycloud.nio.component;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

public interface UDPEndPoint extends EndPoint {

	public abstract void sendPacket(ByteBuffer buffer, SocketAddress socketAddress) throws IOException;

	public abstract void sendPacket(ByteBuffer buffer) throws IOException;

	public abstract void setSession(Session session);
}
