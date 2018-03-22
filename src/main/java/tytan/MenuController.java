package tytan;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    private BorderPane root;
    private ResourceBundle rb;


    @FXML
    private ImageView menuButton;
    @FXML
    private ImageView chatButton;
    @FXML
    private JFXButton videochatButton;
    @FXML
    private ImageView meldunkiButton;

    public void initialize(URL location, ResourceBundle resources) {
        rb = resources;
    }

    @FXML
    private void click(MouseEvent mouseEvent) {
        try {
            JFXButton button = (JFXButton) mouseEvent.getSource();
            String name = button.getText();
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(rb);
            AnchorPane anchorPane = loader.load(getClass().getResource("/fxml/TabPaneLayout.fxml").openStream());
            root.setCenter(anchorPane);
            TabPaneController controller = loader.getController();
            controller.setSelection(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRoot(BorderPane root) {
        this.root = root;
    }
}
