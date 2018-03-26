package tytan.client.model;

import client.beans.Message;
import client.connection.AbstractConnection;

public class SendDataModel extends AbstractModel{

	private AbstractConnection connectionModel;

	public SendDataModel(AbstractConnection connectionModel) {
		this.connectionModel = connectionModel;
	}
	
	public void sendData(Message msg){
			connectionModel.writeMessage(msg);
		
	}
}
