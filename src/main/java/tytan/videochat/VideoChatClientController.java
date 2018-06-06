package tytan.videochat;

import com.github.sarxos.webcam.util.ImageUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.events.JFXDrawerEvent;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.util.Callback;
import jdk.internal.util.xml.impl.Input;
import org.json.JSONObject;
import tytan.config.Config;
import tytan.data.rdp.entity.Client;
import tytan.data.rdp.manager.StreamManager;
import tytan.data.rdp.repository.web_socket.ClientWebSocketRepository;
import tytan.data.rdp.specification.web_socket.ConnectAndGetListOfClientsSpecification;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VideoChatClientController implements Initializable, StreamManager.StreamDataListener {

    private Disposable clientsListDisposable = null;

    @FXML
    private JFXListView<Client> personListView;
    @FXML
    private Button settingsButton;
    @FXML
    private JFXDrawer drawer;
    private AnchorPane menuPane;

    @FXML
    private Canvas displayStreamCanvas;

    @FXML
    private ImageView displayStreamImage;

    private StreamManager streamManager;

    @FXML
    private JFXButton connectClientButton;

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
        personListView.setOnMouseClicked(event -> {
            Client selectedClient = personListView.getSelectionModel().getSelectedItem();
            initializeStreamManagerForClientId(selectedClient.getName());
        });
    }

    private void initializeStreamManagerForClientId(String clientId) {
        streamManager = new StreamManager
                .Builder()
                .setURI(URI
                        .create(Config.getInstance().getApiUrl() + "/clients/list/" + clientId + "/true"
                        )
                )
                .setStreamListener(this)
                .setClientId(clientId).build();
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

    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();

    @Override
    public void onStreamReceived(String stream) {
        JSONObject jsonObject = new JSONObject(stream);
        String video = jsonObject.getString("video");
        Charset charset = Charset.forName("ISO-8859-1");
        ByteBuffer byteBuffer = StreamManager.string2ByteBuffer(video, charset);
        System.out.println("Stream Byte array length: " + byteBuffer.array().length + ", " + new Date().toString());
//        Image img = new Image(new ByteArrayInputStream(byteBuffer.array()));
//        Image img = getJavaFXImage(byteBuffer.array(), 300, 300);
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    Image img = new Image(new ByteArrayInputStream(byteBuffer.array()));
                    System.out.println("After byte buffer");
                    displayStreamCanvas.getGraphicsContext2D().drawImage(img, 0, 0, 200, 200);
                    System.out.println("After set graphics");
                    imageProperty.set(img);
                    System.out.println("After setImage");
                    System.out.println("StreamDataListener: onMessageReceived " + stream);
                });
                return null;
            }

        };
        Thread webCamStream = new Thread(task);
        webCamStream.setDaemon(true);
        webCamStream.start();
        displayStreamImage.imageProperty().bind(imageProperty);

    }

    public void connectClient(ActionEvent event) {
        clientsListDisposable = new ClientWebSocketRepository().streamQuery(new ConnectAndGetListOfClientsSpecification())
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .subscribe(onNext -> {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            personListView.setItems(FXCollections.observableList(onNext));
                            // uncomment this to enable initialization to first item
//                            if (onNext.size() > 0) {
//                                initializeStreamManagerForClientId(onNext.get(0).getName());
//                            }
                        }
                    });
                }, error -> {
                    error.printStackTrace();
                    System.out.println("Getting exception: " + error.getMessage());
                });
    }
}
