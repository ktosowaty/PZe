package tytan.client.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import tytan.client.ClientMVC;
import tytan.client.beans.Message;

public class ConnectionTest {
	MockConnection connection;
	MockController controller;
	ClientMVC clientMVC;

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void test() {
		connection = new MockConnection(new Message("1", "2", "Eloszka"));
		controller = new MockController();
		clientMVC = new ClientMVC(connection, controller);
		
		assertEquals("newMessageFromReceiveDataModel", controller.propertyList.get(0));
		assertEquals(1, controller.propertyList.size());
	}

}
