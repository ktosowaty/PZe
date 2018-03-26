package tytan.client.model;

import tytan.client.beans.Message;
import tytan.client.connection.AbstractConnection;

public class ReceiveDataModel extends AbstractModel implements Runnable {

	private AbstractConnection connectionModel;

	public ReceiveDataModel(AbstractConnection connectionModel) {
		this.connectionModel = connectionModel;
	}

	@Override
	public void run() {
		do {
			Message message = (Message) connectionModel.readMessage();
			if (message == null)
				break;
			this.firePropertyChange("newMessageFromReceiveDataModel", null, message);

		} while (true);
	}
}
