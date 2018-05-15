package tytan.meldunki;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.events.JFXDrawerEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import tytan.Main;
import tytan.MenuController;
import tytan.client.ClientMVC;
import tytan.map.MapModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MeldunkiController implements Initializable {

    @FXML
    private Button settingsButton;
    @FXML
    private JFXDrawer drawer;

    private ClientMVC client;

    public static MeldunkiType meldunkiType;

    public MeldunkiController() {
        client = Main.getClient();
        client.getController().setMeldunkiController(this);
    }

    public void initialize(URL location, ResourceBundle resources) {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resources);
            Pane vBox = loader.load(getClass().getResource("/fxml/meldunki/MeldunkiSettings.fxml").openStream());
            drawer.setSidePane(vBox);
            MeldunkiSettingsController settings = loader.getController();
            settings.setMeldunki(this);

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
    private void personalLocation() {
        meldunkiType = MeldunkiType.PersonalLocation;
        MenuController.tabPaneController.setSelection("Map");

    }

    @FXML
    private void fireSupport() {
        meldunkiType = MeldunkiType.FireSupport;
        MenuController.tabPaneController.setSelection("Map");
    }

    @FXML
    private void medicalHelp() {
        meldunkiType = MeldunkiType.MedicalHelp;
        MenuController.tabPaneController.setSelection("Map");
        //MapModel.googleMap.removeMarkers(MapModel.medicalHelpMarkerList);
        //MapModel.medicalHelpMarkerList.clear();
    }

    @FXML
    private void enemyForce() {
        meldunkiType = MeldunkiType.EnemyForce;
        MenuController.tabPaneController.setSelection("Map");
    }

}
