package tytan.client.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;

import tytan.client.connection.AbstractConnection;
import tytan.serwer.beans.Message;

public class MockConnection implements AbstractConnection {

	public Message outputMessage;
	public Message inputMessage;
	ByteArrayOutputStream buffer;
	ObjectOutputStream out;
	ByteArrayInputStream bis;
	ObjectInputStream in;

	public MockConnection(Message messageToSend) {
		buffer = new ByteArrayOutputStream();
		try {
			PipedInputStream pipeInput = new PipedInputStream();
			
			out = new ObjectOutputStream(buffer);
			out.writeObject(messageToSend);
			out.flush();

			bis = new ByteArrayInputStream(buffer.toByteArray());
			in = new ObjectInputStream(bis);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ObjectInputStream getIn() {
		return in;
	}

	@Override
	public ObjectOutputStream getOut() {
		return out;
	}

	@Override
	public void closeConnection() {

	}

}
