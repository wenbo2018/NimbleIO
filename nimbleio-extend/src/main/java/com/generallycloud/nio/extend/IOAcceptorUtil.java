package com.generallycloud.nio.extend;

import com.generallycloud.nio.acceptor.TCPAcceptor;
import com.generallycloud.nio.common.Logger;
import com.generallycloud.nio.common.LoggerFactory;
import com.generallycloud.nio.common.SharedBundle;
import com.generallycloud.nio.component.DefaultNIOContext;
import com.generallycloud.nio.component.IOEventHandleAdaptor;
import com.generallycloud.nio.component.LoggerSEListener;
import com.generallycloud.nio.component.NIOContext;
import com.generallycloud.nio.component.concurrent.EventLoopGroup;
import com.generallycloud.nio.component.concurrent.SingleEventLoopGroup;
import com.generallycloud.nio.configuration.PropertiesSCLoader;
import com.generallycloud.nio.configuration.ServerConfiguration;

public class IOAcceptorUtil {

	private static Logger	logger	= LoggerFactory.getLogger(IOAcceptorUtil.class);

	public static TCPAcceptor getTCPAcceptor(IOEventHandleAdaptor ioEventHandleAdaptor) throws Exception {

		return getTCPAcceptor(ioEventHandleAdaptor, null);
	}

	public static TCPAcceptor getTCPAcceptor(IOEventHandleAdaptor ioEventHandleAdaptor,
			ServerConfiguration configuration) throws Exception {
		
		if (configuration == null) {
			PropertiesSCLoader loader = new PropertiesSCLoader();
			configuration = loader.loadConfiguration(SharedBundle.instance());
		}

		TCPAcceptor acceptor = new TCPAcceptor();

		try {
			
			configuration.setSERVER_IS_ACCEPT_BEAT(true);

			EventLoopGroup eventLoopGroup = new SingleEventLoopGroup(
					"IOEvent", 
					configuration.getSERVER_CHANNEL_QUEUE_SIZE(),
					configuration.getSERVER_CORE_SIZE());

			NIOContext context = new DefaultNIOContext(configuration,eventLoopGroup);

			context.setIOEventHandleAdaptor(ioEventHandleAdaptor);

			context.addSessionEventListener(new LoggerSEListener());

//			context.addSessionEventListener(new SessionAliveSEListener());
			
			acceptor.setContext(context);

		} catch (Throwable e) {

			logger.error(e.getMessage(), e);

			acceptor.unbind();

			throw new RuntimeException(e);
		}

		return acceptor;
	}

}
