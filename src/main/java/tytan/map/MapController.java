package tytan.map;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.events.JFXDrawerEvent;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.object.LatLong;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import tytan.Main;
import tytan.client.ClientMVC;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MapController implements Initializable {

    public MapModel mapModel;
    @FXML
    public JFXDrawer drawer;
    @FXML
    private GoogleMapView googleMapView;
    @FXML
    private Button settingsButton;
    private ClientMVC client;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            client = Main.getClient();
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resources);
            ScrollPane vBox = loader.load(getClass().getResource("/fxml/map/MapSettings.fxml").openStream());
            drawer.setSidePane(vBox);
            MapSettingsController settings = loader.getController();
            settings.setMap(this);
            mapModel = new MapModel(googleMapView);
            client.getController().setMapController(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openSettings(MouseEvent mouseEvent) {
        drawer.toggle();
    }

    @FXML
    public void setMapVisible(boolean visible) {
        googleMapView.setVisible(visible);
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

    public void addLocationMarker(LatLong latLong) {
        mapModel.addLocationMarker(latLong);
    }
}
