package tytan.client;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import client.connection.AbstractConnection;
import client.connection.EstablishConnectionSupport;
import client.controller.AbstractController;
import client.controller.Controller;
import client.model.ReceiveDataModel;
import client.model.SendDataModel;
import client.model.UsersListModel;
import client.view.TextDemo;

public class ClientMVC {
	private final static Logger LOGGER = Logger.getLogger(ClientMVC.class.getName());
	private final static String host = "localhost";
	private final static int port = 8000;

	public ClientMVC(AbstractConnection connection, AbstractController controller) {

		try {
			AbstractConnection establishConnectionModel = connection;

			SendDataModel sendDataModel = new SendDataModel(establishConnectionModel);
			controller.addModel("sendDataModel", sendDataModel);

			ReceiveDataModel receiveDataModel = new ReceiveDataModel(establishConnectionModel);
			controller.addModel("receiveDataModel", receiveDataModel);
			Thread thread = new Thread(receiveDataModel);
			thread.start();

			UsersListModel userListModel = new UsersListModel();
			controller.addModel("userListModel", userListModel);

			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					TextDemo.createAndShowGUI(controller);
				}
			};
			EventQueue.invokeLater(runnable);
			LOGGER.info("Starting client program");

		} catch (Exception e) {
			LOGGER.warning("Failed to start client");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			FileHandler fileHandler = new FileHandler("log/client%g.log", 5242880, 5, true);
			LOGGER.addHandler(fileHandler);
			AbstractConnection connection = new EstablishConnectionSupport(host, port);
			AbstractController controller = new Controller();
			
			ClientMVC clientMVC = new ClientMVC(connection, controller);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
