package tytan.client;

import tytan.client.connection.AbstractConnection;
import tytan.client.connection.EstablishConnectionSupport;
import tytan.client.controller.AbstractController;
import tytan.client.controller.Controller;
import tytan.client.model.ReceiveDataModel;
import tytan.client.model.SendDataModel;
import tytan.client.model.UsersListModel;
import tytan.client.view.TextDemo;

import java.awt.*;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class ClientMVC {
    private final static Logger LOGGER = Logger.getLogger(ClientMVC.class.getName());
    private final static String host = "localhost";
    private final static int port = 8000;
    public SendDataModel sendDataModel;

    public ClientMVC(AbstractConnection connection, AbstractController controller) {

        try {

            sendDataModel = new SendDataModel(connection);
            controller.addModel("sendDataModel", sendDataModel);

            ReceiveDataModel receiveDataModel = new ReceiveDataModel(connection);
            controller.addModel("receiveDataModel", receiveDataModel);
            Thread thread = new Thread(receiveDataModel);
            thread.start();

            UsersListModel userListModel = new UsersListModel();
            controller.addModel("userListModel", userListModel);

            Runnable runnable = () -> TextDemo.createAndShowGUI(controller);
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
