package tytan.client.controller;

import com.lynden.gmapsfx.javascript.object.LatLong;
import javafx.application.Platform;
import tytan.client.ClientMVC;
import tytan.client.model.AbstractModel;
import tytan.client.model.SendDataModel;
import tytan.map.MapController;
import tytan.map.MapModel;
import tytan.meldunki.MeldunkiController;
import tytan.meldunki.MeldunkiType;
import tytan.serwer.beans.Message;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

public class Controller extends AbstractController {
	private final String usernick;
	public double lat, lng, radius;

	private final static Logger LOGGER = Logger.getLogger(ClientMVC.class.getName());
	private Map<String, AbstractModel> modelsMap;
	private MeldunkiController meldunkiController;
	private MapController mapController;

	public void setMeldunkiController(MeldunkiController meldunkiController) {
		this.meldunkiController = meldunkiController;
	}

	public void setMapController(MapController mapController) {
		this.mapController = mapController;
	}

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
			LOGGER.info("Recived message " + messageContent);

			String[] messageSplit = messageContent.split(";");
			try {
				MeldunkiType meldunkiType = MeldunkiType.valueOf(messageSplit[0]);

				switch (meldunkiType) {
				case FireSupport:
					lat = Double.parseDouble(messageSplit[1]);
					lng = Double.parseDouble(messageSplit[2]);
					LOGGER.info("Placing requirement of fire support");
					Platform.runLater(() -> mapController.addLocationMarker(new LatLong(lat, lng)));

					break;
				case PersonalLocation:
					lat = Double.parseDouble(messageSplit[1]);
					lng = Double.parseDouble(messageSplit[2]);
					Platform.runLater(() -> MapModel.addFriendlyLocationMarker(new LatLong(lat, lng)));

					break;
				case MedicalHelp:
					lat = Double.parseDouble(messageSplit[1]);
					lng = Double.parseDouble(messageSplit[2]);
					Platform.runLater(() -> MapModel.addMedicalHelpMarker(new LatLong(lat, lng)));
					break;
				case EnemyForce:
					lat = Double.parseDouble(messageSplit[1]);
					lng = Double.parseDouble(messageSplit[2]);
					radius = Double.parseDouble(messageSplit[3]);
					Platform.runLater(() -> MapModel.addCircleArea(new LatLong(lat, lng), radius));
					break;
				}
			} catch (Exception e) {
				LOGGER.warning("Wrong format of message");
				// e.printStackTrace();
			}

		}
	}

}
