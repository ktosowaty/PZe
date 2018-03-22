package tytan.meldunki;

import com.jfoenix.controls.JFXDrawer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MeldunkiController implements Initializable {

    @FXML
    private JFXDrawer drawer;
    @FXML
    private Label meldunkiLabel;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resources);
            Pane vBox = loader.load(getClass().getResource("/fxml/meldunki/MeldunkiSettings.fxml").openStream());
            drawer.setSidePane(vBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openSettings(MouseEvent mouseEvent) {
        if (drawer.isShown()) drawer.close();
        else drawer.open();
    }
}
