package tytan.videochat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VideoChatController implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private BorderPane operatorPane;
    @FXML
    private BorderPane clientPane;
    @FXML
    private AnchorPane menuPane;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resources);
            menuPane.toFront();

            AnchorPane pane1 = loader.load(getClass().getResource("/fxml/videochat/VideoChatClient.fxml").openStream());
            clientPane.setCenter(pane1);
            VideoChatClientController vcc1 = loader.getController();
            vcc1.setMenuPane(menuPane);

            FXMLLoader loader2 = new FXMLLoader();
            loader2.setResources(resources);
            AnchorPane pane2 = loader2.load(getClass().getResource("/fxml/videochat/VideoChatOperator.fxml").openStream());
            operatorPane.setCenter(pane2);
            VideoChatOperatorController vcc2 = loader2.getController();
            vcc2.setMenuPane(menuPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openOperatorPanel(MouseEvent mouseEvent) {
        System.out.println("openOperator");
        operatorPane.toFront();
    }

    @FXML
    private void openClientPanel(MouseEvent mouseEvent) {
        System.out.println("openClient");
        clientPane.toFront();
    }
}
