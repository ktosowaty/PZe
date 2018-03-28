package tytan.client.model;

import java.io.ObjectInputStream;

import tytan.client.connection.AbstractConnection;
import tytan.serwer.beans.Message;

public class ReceiveDataModel extends AbstractModel implements Runnable {

	private AbstractConnection connectionModel;
	private ObjectInputStream in;
	
	public ReceiveDataModel(AbstractConnection connectionModel) {
		this.connectionModel = connectionModel;
		this.in = connectionModel.getIn();
	}

	@Override
	public void run() {
		do{
			try {
				Message message = (Message) in.readObject();                             
				this.firePropertyChange("newMessageFromReceiveDataModel", null, message);  
			} catch (Exception e){ 
				connectionModel.closeConnection();
				break;                                                                   
			}
		}while (true);
	}
}
