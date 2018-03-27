package tytan.client.model;

import java.io.IOException;
import java.io.ObjectOutputStream;

import tytan.client.connection.AbstractConnection;
import tytan.serwer.beans.Message;

public class SendDataModel extends AbstractModel{

	private AbstractConnection connectionModel;
	private ObjectOutputStream out;
	
	public SendDataModel(AbstractConnection connectionModel) {
		this.connectionModel = connectionModel;
		this.out = connectionModel.getOut();
	}
	
	public void sendData(Message msg){
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			connectionModel.closeConnection();  
		}
		
	}
}
