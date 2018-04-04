package tytan.videochat;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.events.JFXDrawerEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VideoChatClientController implements Initializable {

    @FXML
    private JFXListView<Person> personListView;
    @FXML
    private Button settingsButton;
    @FXML
    private JFXDrawer drawer;
    private AnchorPane menuPane;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resources);
            Pane vBox = loader.load(getClass().getResource("/fxml/videochat/VideoChatClientSettings.fxml").openStream());
            drawer.setSidePane(vBox);
            VideoChatClientSettingsController settings = loader.getController();
            settings.setVideoChat(this);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //TODO USUN_TO
        ObservableList<Person> items = FXCollections.observableArrayList(
                new Person("Mirek"), new Person("Andrzej"), new Person("Włodek"), new Person("Janusz"));
        personListView.setItems(items);

        personListView.setCellFactory(new Callback<ListView<Person>, ListCell<Person>>() {
            @Override
            public ListCell<Person> call(ListView<Person> param) {
                return new JFXListCell<Person>() {
                    @Override
                    protected void updateItem(Person item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(null);
                        if (item != null) {
                            VBox vBox = new VBox(new Text(item.getName()), new Text("dostepny"));
                            HBox hBox = new HBox(new ImageView(item.getIcon()), vBox);
                            hBox.setSpacing(10);
                            setGraphic(hBox);
                        }
                    }
                };
            }
        });
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

    public void setMenuPane(AnchorPane menuPane) {
        this.menuPane = menuPane;
    }

    @FXML
    private void back(MouseEvent mouseEvent) {
        menuPane.toFront();
    }
}
