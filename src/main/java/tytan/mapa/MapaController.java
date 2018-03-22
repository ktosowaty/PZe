package tytan.mapa;

import com.jfoenix.controls.JFXDrawer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MapaController implements Initializable {

    @FXML
    private JFXDrawer drawer;
    @FXML
    private WebView web;
    @FXML
    private Label mapaLabel;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resources);
            Pane vBox = loader.load(getClass().getResource("/fxml/mapa/MapaSettings.fxml").openStream());
            drawer.setSidePane(vBox);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //web.getEngine().load("https://www.google.pl/maps/@52.2534883,20.8954443,15z?hl=pl");

    }

    @FXML
    private void openSettings(MouseEvent mouseEvent) {
        if (drawer.isShown()) drawer.close();
        else drawer.open();
    }
}
