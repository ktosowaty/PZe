package tytan.client.connection;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface AbstractConnection {

	public ObjectInputStream getIn();

	public ObjectOutputStream getOut();

	public void closeConnection();
}
