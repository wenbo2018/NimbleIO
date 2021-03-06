package test.http11;

import com.generallycloud.nio.common.CloseUtil;
import com.generallycloud.nio.common.test.ITest;
import com.generallycloud.nio.common.test.ITestHandle;
import com.generallycloud.nio.component.ReadFutureFactory;
import com.generallycloud.nio.component.Session;
import com.generallycloud.nio.component.protocol.http11.ClientHTTPProtocolFactory;
import com.generallycloud.nio.component.protocol.http11.HttpClient;
import com.generallycloud.nio.component.protocol.http11.HttpIOEventHandle;
import com.generallycloud.nio.component.protocol.http11.future.HttpRequestFuture;
import com.generallycloud.nio.connector.TCPConnector;
import com.generallycloud.nio.extend.IOConnectorUtil;

public class TestHttpLoad {

	public static void main(String[] args) throws Exception {

		HttpIOEventHandle eventHandleAdaptor = new HttpIOEventHandle();

		TCPConnector connector = IOConnectorUtil.getTCPConnector(eventHandleAdaptor);
		
		eventHandleAdaptor.setTCPConnector(connector);

		connector.getContext().setProtocolFactory(new ClientHTTPProtocolFactory());
		
		connector.connect();

		final Session session = connector.getSession();

		final HttpClient client = eventHandleAdaptor.getHttpClient();
		
		ITestHandle.doTest(new ITest() {
			
			public void test() throws Exception {
				
				HttpRequestFuture future = ReadFutureFactory.createHttpReadFuture(session, "/test");
				
				client.request(session, future, 3000);
				
			}
		}, 2000, "test-http");
		

		CloseUtil.close(connector);

	}
}
