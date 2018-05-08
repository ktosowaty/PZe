package tytan.client;

import tytan.client.connection.AbstractConnection;
import tytan.client.connection.EstablishConnectionSupport;
import tytan.client.controller.Controller;
import tytan.client.model.ReceiveDataModel;
import tytan.client.model.SendDataModel;
import tytan.client.model.UsersListModel;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class ClientMVC {
	private final static Logger LOGGER = Logger.getLogger(ClientMVC.class.getName());
	private final static String host = "localhost";
	private final static int port = 8000;
	private SendDataModel sendDataModel;
	public ReceiveDataModel receiveDataModel;
	private Controller controller;
	private UsersListModel userListModel;
	
	public ClientMVC() {

		try {
			FileHandler fileHandler = new FileHandler("log/client%g.log", 5242880, 5, true);
			LOGGER.addHandler(fileHandler);

			AbstractConnection connection = new EstablishConnectionSupport(host, port);
			controller = new Controller();

			sendDataModel = new SendDataModel(connection);
			controller.addModel("sendDataModel", sendDataModel);

			userListModel = new UsersListModel();
			controller.addModel("userListModel", userListModel);

			receiveDataModel = new ReceiveDataModel(connection);
			controller.addModel("receiveDataModel", receiveDataModel);
			Thread thread = new Thread(receiveDataModel);
			thread.start();
			controller.registerUser();

        } catch (Exception e) {
            LOGGER.warning("Failed to start client");
            e.printStackTrace();
        }
    }


	public Controller getController() {
		return controller;
	}

	
}
