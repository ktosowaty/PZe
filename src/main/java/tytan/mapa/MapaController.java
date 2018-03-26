package tytan.mapa;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.events.JFXDrawerEvent;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MapaController implements Initializable {

    @FXML
    private GoogleMapView googleMapView;
    private GoogleMap googleMap;
    @FXML
    private Button settingsButton;
    @FXML
    private JFXDrawer drawer;
    //    @FXML
//    private WebView webView;
    @FXML
    private Label mapaLabel;


    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resources);
            Pane vBox = loader.load(getClass().getResource("/fxml/mapa/MapaSettings.fxml").openStream());
            drawer.setSidePane(vBox);
            MapaSettingsController settings = loader.getController();
            settings.setMapa(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //webView.getEngine().load("https://www.google.pl/maps/@52.2534883,20.8954443,15z?hl=pl");
        //webView.getEngine().load(String.valueOf(getClass().getResource("/html/myMap.html")));


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

}
