package tytan;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {


    @FXML
    private BorderPane root;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resources);
            Pane anchorPane = loader.load(getClass().getResource("/fxml/Menu.fxml").openStream());
            root.setCenter(anchorPane);
            MenuController controller = loader.getController();
            controller.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
