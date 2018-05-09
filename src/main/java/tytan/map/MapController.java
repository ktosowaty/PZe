package tytan.map;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.events.JFXDrawerEvent;
import com.lynden.gmapsfx.GoogleMapView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MapController implements Initializable {

    MapModel mapModel;
    @FXML
    private GoogleMapView googleMapView;
    @FXML
    private Button settingsButton;
    @FXML
    private JFXDrawer drawer;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resources);
            Pane vBox = loader.load(getClass().getResource("/fxml/map/MapSettings.fxml").openStream());
            drawer.setSidePane(vBox);
            MapSettingsController settings = loader.getController();
            settings.setMap(this);
            mapModel = new MapModel(googleMapView);
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
}
