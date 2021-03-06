package test;

import com.generallycloud.nio.common.CloseUtil;
import com.generallycloud.nio.component.protocol.nio.future.NIOReadFuture;
import com.generallycloud.nio.connector.TCPConnector;
import com.generallycloud.nio.extend.FixedSession;
import com.generallycloud.nio.extend.IOConnectorUtil;
import com.generallycloud.nio.extend.SimpleIOEventHandle;
import com.generallycloud.nio.extend.implementation.SYSTEMStopServerServlet;

public class TestStopServer {

	public static void main(String[] args) throws Exception {
		String serviceKey = SYSTEMStopServerServlet.SERVICE_NAME;

		SimpleIOEventHandle eventHandle = new SimpleIOEventHandle();

		TCPConnector connector = IOConnectorUtil.getTCPConnector(eventHandle);

		FixedSession session = eventHandle.getFixedSession();

		connector.connect();

		session.login("admin", "admin100");

		NIOReadFuture future = session.request(serviceKey, null);
		System.out.println(future.getText());

		CloseUtil.close(connector);

	}
}
