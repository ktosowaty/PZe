package tytan.client.connection;

import tytan.client.beans.Message;

public interface AbstractConnection {
	
	public Message readMessage();
	public void writeMessage(Message message);
}
