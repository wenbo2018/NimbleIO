package com.gifisan.nio.plugin.rtp.server;

import com.gifisan.nio.component.future.ServerReadFuture;
import com.gifisan.nio.server.IOSession;
import com.gifisan.nio.server.service.NIOServlet;

public abstract class RTPServlet extends NIOServlet {

	private RTPContext	context	= RTPContextFactory.getRTPContext();

	public RTPContext getRTPContext() {
		return context;
	}

	public void accept(IOSession session, ServerReadFuture future) throws Exception {

		RTPSessionAttachment attachment = (RTPSessionAttachment) session.getAttachment(context);

		if (attachment == null) {

			attachment = new RTPSessionAttachment(context);

			session.setAttachment(context, attachment);
		}

		this.accept(session, future, attachment);
	}

	public abstract void accept(IOSession session, ServerReadFuture future, RTPSessionAttachment attachment)
			throws Exception;

}