package tytan;

import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TabPaneController implements Initializable {

    @FXML
    private JFXTabPane tabPane;
    @FXML
    private Tab tabMapa;
    @FXML
    private Tab tabChat;
    @FXML
    private Tab tabVideoChat;
    @FXML
    private Tab tabMeldunki;

    public void initialize(URL location, ResourceBundle resources) {
        show(resources, tabMapa, "Mapa");
        show(resources, tabChat, "Chat");
        show(resources, tabVideoChat, "VideoChat");
        show(resources, tabMeldunki, "Meldunki");
    }

    private void show(ResourceBundle rb, Tab tab, String name) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(rb);
            Pane anchorPane = (AnchorPane) loader.load(this.getClass().getResource("/fxml/" + name.toLowerCase() + "/" + name + ".fxml").openStream());
            tab.setContent(anchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSelection(String name) {
        int x = 0;
        switch (name) {
            case "Mapa":
                x = 0;
                break;
            case "Chat":
                x = 1;
                break;
            case "VideoChat":
                x = 2;
                break;
            case "Meldunki":
                x = 3;
                break;
        }
        tabPane.getSelectionModel().select(x);
    }
}
