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
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUp() throws Exception {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
		
	}

	@Test
	public void test() {
		connection = new MockConnection();
		connection.sendMessage(new Message("1", "2", "Eloszka"));
		controller = new MockController();
		clientMVC = new ClientMVC(connection, controller);
		
		assertEquals("newMessageFromReceiveDataModel", outContent.toString());
	}

}
