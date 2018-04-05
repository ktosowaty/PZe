package tytan.videochat;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sun.plugin2.ipc.windows.WindowsEvent;

import java.beans.EventHandler;
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
    private Stage secondStage = new Stage();

    private VideoChatOperatorController mVideoChatOperatorController = null;
    private VideoChatClientController mVideoChatClientController = null;

    public void initialize(URL location, ResourceBundle resources) {
        loadClientPane(resources);
        loadOperatorPane(resources);
    }

    private void loadClientPane(ResourceBundle resources) {
        try {
            FXMLLoader clientLoader = new FXMLLoader();
            clientLoader.setResources(resources);
            menuPane.toFront();

            AnchorPane clientAnchorPane = clientLoader.load(getClass().getResource("/fxml/videochat/VideoChatClient.fxml").openStream());
            clientPane.setCenter(clientAnchorPane);
            mVideoChatClientController = clientLoader.getController();
            mVideoChatClientController.setMenuPane(menuPane);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void loadOperatorPane(ResourceBundle resources) {
        try {
            FXMLLoader operatorLoader = new FXMLLoader();
            operatorLoader.setResources(resources);
            AnchorPane operatorAnchorPane = operatorLoader.load(getClass().getResource("/fxml/videochat/VideoChatOperator.fxml").openStream());
            //operatorPane.setCenter(operatorAnchorPane);
            secondStage.setScene(new Scene(operatorAnchorPane));
            mVideoChatOperatorController = operatorLoader.getController();
            mVideoChatOperatorController.setMenuPane(menuPane);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void openOperatorPanel(MouseEvent mouseEvent) {
        System.out.println("openOperator");
        //operatorPane.toFront();
        secondStage.show();
    }

    @FXML
    private void openClientPanel(MouseEvent mouseEvent) {
        System.out.println("openClient");
        clientPane.toFront();
    }
}
