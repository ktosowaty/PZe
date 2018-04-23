package tytan.videochat;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.events.JFXDrawerEvent;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import tytan.data.rdp.entity.Client;
import tytan.data.rdp.repository.web_socket.ClientWebSocketRepository;
import tytan.data.rdp.specification.web_socket.ConnectAndGetListOfClientsSpecification;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VideoChatClientController implements Initializable {

    private Disposable clientsListDisposable = null;

    @FXML
    private JFXListView<Client> personListView;
    @FXML
    private Button settingsButton;
    @FXML
    private JFXDrawer drawer;
    private AnchorPane menuPane;

    public void subscribe() {

    }

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

        clientsListDisposable = new ClientWebSocketRepository().streamQuery(new ConnectAndGetListOfClientsSpecification())
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .subscribe(onNext -> {
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            personListView.setItems(FXCollections.observableList(onNext));
                        }
                    });
                }, error -> {
                    error.printStackTrace();
                    System.out.println("Getting exception: " + error.getMessage());
                });

        personListView.setCellFactory(new Callback<ListView<Client>, ListCell<Client>>() {
            @Override
            public ListCell<Client> call(ListView<Client> param) {
                return new JFXListCell<Client>() {
                    @Override
                    protected void updateItem(Client item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(null);
                        if (item != null) {
                            VBox vBox = new VBox(new Text(item.getName()), new Text("dostepny"));
                            setGraphic(vBox);
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
