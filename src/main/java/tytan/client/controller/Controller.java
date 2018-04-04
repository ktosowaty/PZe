package tytan.client.controller;

import tytan.client.ClientMVC;
import tytan.client.model.AbstractModel;
import tytan.client.model.SendDataModel;
import tytan.client.model.UsersListModel;
import tytan.client.view.TextDemo;
import tytan.serwer.beans.Message;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

public class Controller extends AbstractController {
    private final static Logger LOGGER = Logger.getLogger(ClientMVC.class.getName());
    private final String usernick;
    private Map<String, AbstractModel> modelsMap;
    private TextDemo demoView;

    public Controller() {
        Random rand = new Random();
        modelsMap = new HashMap<>();
        usernick = new Integer(rand.nextInt(10000)).toString();
    }

    @Override
    public void addModel(String name, AbstractModel model) {
        modelsMap.put(name, model);
        model.addPropertyChangeListener(this);
    }

    public void sendMessageTo(Object messageContent, String messageRecipient) {

        Message message = new Message(messageRecipient, usernick, messageContent);

        try {
            demoView.printMessage("Sending message to " + messageRecipient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((SendDataModel) modelsMap.get("sendDataModel")).sendData(message);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();

        switch (propertyName) {
            case "newMessageFromReceiveDataModel":

                Message message = (Message) evt.getNewValue();
                String nickTo = message.getNickTo();
                String messageContent = (String) message.getMessage();

                if (nickTo.equals("addNewUser")) {
                    LOGGER.info("Adding new user");
                    ((UsersListModel) modelsMap.get("userListModel")).addNewUser(message);

                } else if (messageContent.equals("uniqueNick")) {
                    LOGGER.info("User register successfully");
                    String username = message.getNickFrom();
                    demoView.printMessage("New user registered " + username);

                } else if (messageContent.equals("wrongNick")) {
                    LOGGER.info("User nick is not unique");
                    demoView.printMessage("Nickname already in use");

                } else if (messageContent.equals("removeUserFromList")) {
                    LOGGER.info("User removed from list model");
                    ((UsersListModel) modelsMap.get("userListModel")).removeUser(message);

                } else if (!nickTo.equals("empty") && !message.getNickFrom().equals("empty")) {
                    LOGGER.info("Showing message from " + message.getNickFrom());
                    demoView.printMessage(
                            String.format("Revived message from %s to %s", message.getNickFrom(), message.getNickTo()));
                }
                break;
            case "removeUserFromList": {
                String username = (String) evt.getNewValue();
                LOGGER.info("User removed from list");
                demoView.printMessage("Removing user " + username);

                break;
            }
            case "addNewUserToList": {
                String username = (String) evt.getNewValue();
                LOGGER.info("Adding new user to list " + username);
                demoView.printMessage("Adding user " + username);
                break;
            }
        }
    }

    public void receivedLoginData(String username) {
        Message message = new Message(username, "empty", "empty");
        ((SendDataModel) modelsMap.get("sendDataModel")).sendData(message);
    }

    public TextDemo getDemoView() {
        return demoView;
    }

    public void setDemoView(TextDemo demoView) {
        this.demoView = demoView;
    }
}
