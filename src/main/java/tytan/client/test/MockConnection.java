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

	public MockConnection() {
		buffer = new ByteArrayOutputStream();
		try {
			out = new ObjectOutputStream(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(Message m) {
		try {
			out.writeObject(m);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Message readMessage() {
		ByteArrayInputStream bis = new ByteArrayInputStream(buffer.toByteArray());
		Message m = null;
		try {
			ObjectInput in = new ObjectInputStream(bis);
			m = (Message) in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return m;

	}

	@Override
	public void writeMessage(Message message) {
		inputMessage = message;

	}

}
