package tytan.client.controller;

import tytan.client.ClientMVC;
import tytan.client.model.AbstractModel;
import tytan.client.model.SendDataModel;
import tytan.client.model.UsersListModel;
import tytan.meldunki.MeldunkiController;
import tytan.serwer.beans.Message;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

public class Controller extends AbstractController {
	private final String usernick;
	
	private final static Logger LOGGER = Logger.getLogger(ClientMVC.class.getName());
	private Map<String, AbstractModel> modelsMap;

	public void setMeldunkiHandler(MeldunkiController meldunkiHandler) {
		this.meldunkiHandler = meldunkiHandler;
	}

	private MeldunkiController meldunkiHandler;


	public Controller() {
		Random rand = new Random();
		modelsMap = new HashMap<String, AbstractModel>();		
		usernick = new Integer(rand.nextInt(10000)).toString();
	}

	@Override
	public void addModel(String name, AbstractModel model) {
		modelsMap.put(name, model);
		model.addPropertyChangeListener(this);
	}
	
	public void sendBrodcastMessage(Object messageContent) {
		LOGGER.info("Sending brodcast message");
		Message message = new Message("broadcast", usernick, messageContent);
		((SendDataModel) modelsMap.get("sendDataModel")).sendData(message);
	}
	
	
	public void sendMessageTo(Object messageContent, String messageRecipient) {

		Message message = new Message(messageRecipient, usernick, messageContent);
		LOGGER.info("Sending message to " + messageRecipient);
		((SendDataModel) modelsMap.get("sendDataModel")).sendData(message);

	}

	public void registerUser() {
		Message message = new Message("empty", usernick, "empty");
		LOGGER.info("Sending register message from " + usernick);
		((SendDataModel) modelsMap.get("sendDataModel")).sendData(message);
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();

		if (propertyName.equals("newMessageFromReceiveDataModel")) {

			Message message = (Message) evt.getNewValue();
			String nickTo = message.getNickTo();
			String messageContent = (String) message.getMessage();
			meldunkiHandler.printMessage("Reviced message from " + nickTo);
			if (nickTo.equals("addNewUser")) {
				LOGGER.info("Adding new user");
				((UsersListModel) modelsMap.get("userListModel")).addNewUser(message);

			} else if (messageContent.equals("uniqueNick")) {
				LOGGER.info("User register successfully");
				String username = message.getNickFrom();


			} else if (messageContent.equals("wrongNick")) {
				LOGGER.info("User nick is not unique");

			} else if (messageContent.equals("removeUserFromList")) {
				LOGGER.info("User removed from list model");
				((UsersListModel) modelsMap.get("userListModel")).removeUser(message);

			} else if (!nickTo.equals("empty") && !message.getNickFrom().equals("empty")) {
				LOGGER.info("Showing message from " + message.getNickFrom());

			}
		} else if (propertyName.equals("removeUserFromList")) {
			String username = (String) evt.getNewValue();
			LOGGER.info("User removed from list");

		} else if (propertyName.equals("addNewUserToList")) {
			String username = (String) evt.getNewValue();
			LOGGER.info("Adding new user to list " + username);
		} 
	}

	public void receivedLoginData(String username) {
		Message message = new Message(username, "empty", "empty");
		((SendDataModel) modelsMap.get("sendDataModel")).sendData(message);
	}

}
