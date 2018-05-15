package tytan.meldunki;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.events.JFXDrawerEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import tytan.MenuController;
import tytan.client.ClientMVC;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MeldunkiController implements Initializable {

    @FXML
    private Button settingsButton;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private Label meldunkiLabel;
    @FXML
    private TextArea messageField;
    @FXML
    private ChoiceBox reportChoice;

    private ClientMVC client;

    public void initialize(URL location, ResourceBundle resources) {
        reportChoice.getItems().addAll("Mapa", "Chat", "Meldunki");
        reportChoice.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    MenuController.tabPaneController.setSelection(newValue.toString());
                }
        );

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resources);
            Pane vBox = loader.load(getClass().getResource("/fxml/meldunki/MeldunkiSettings.fxml").openStream());
            drawer.setSidePane(vBox);
            MeldunkiSettingsController settings = loader.getController();
            settings.setMeldunki(this);
            client = new ClientMVC();
            client.getController().setMeldunkiHandler(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openSettings(MouseEvent mouseEvent) {
        drawer.toggle();
    }

    @FXML
    private void drawerClosed(JFXDrawerEvent jfxDrawerEvent) {
        drawer.toBack();
    }

    @FXML
    private void drawerOpening(JFXDrawerEvent jfxDrawerEvent) {
        drawer.toFront();
        settingsButton.toFront();
    }

    @FXML
    private void sendMessage() {

        client.getController().sendBrodcastMessage("test");
    }

    public void printMessage(String text) {
        messageField.setText(text + "\n" + messageField.getText());
    }
}
