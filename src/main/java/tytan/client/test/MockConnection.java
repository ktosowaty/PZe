package tytan.client.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import tytan.client.beans.Message;
import tytan.client.connection.AbstractConnection;

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
