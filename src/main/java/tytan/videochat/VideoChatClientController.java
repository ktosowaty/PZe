package tytan.videochat;

import com.github.sarxos.webcam.util.ImageUtils;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.events.JFXDrawerEvent;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
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
            displayStreamCanvas.getGraphicsContext2D().fillRect(10, 10, 60, 60);
            streamManager = new StreamManager
                    .Builder()
                    .setURI(URI
                            .create(Config.getInstance().getApiUrl() + "/clients/list/Client_3/true"
                            )
                    )
                    .setStreamListener(this)
                    .setClientId("Client_19").build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        clientsListDisposable = new ClientWebSocketRepository().streamQuery(new ConnectAndGetListOfClientsSpecification())
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .subscribe(onNext -> {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
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

    @Override
    public void onStreamReceived(String stream) {
        JSONObject jsonObject = new JSONObject(stream);
        String video = jsonObject.getString("video");
        ByteBuffer byteBuffer = StreamManager.string2ByteBuffer(video, Charset.forName("UTF-8"));

        System.out.println("Byte array length: " + byteBuffer.array().length);
//        Image img = new Image(new ByteArrayInputStream(byteBuffer.array()));
        Image img = getJavaFXImage(byteBuffer.array(), 300, 300);
        displayStreamCanvas.getGraphicsContext2D().drawImage(img, 0, 0, 200, 200);
        displayStreamImage.setImage(img);
        System.out.println("StreamDataListener: onMessageReceived " + stream);
    }

    public Image getJavaFXImage(byte[] rawPixels, int width, int height) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write((RenderedImage) createBufferedImage(rawPixels, width, height), "jpg", out);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(ImageUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        return new javafx.scene.image.Image(in);
    }

    private BufferedImage createBufferedImage(byte[] pixels, int width, int height) {
        SampleModel sm = getIndexSampleModel(width, height);
        DataBuffer db = new DataBufferByte(pixels, width * height, 0);
        WritableRaster raster = Raster.createWritableRaster(sm, db, null);
        IndexColorModel cm = getDefaultColorModel();
        BufferedImage image = new BufferedImage(cm, raster, false, null);
        return image;
    }

    private SampleModel getIndexSampleModel(int width, int height) {
        IndexColorModel icm = getDefaultColorModel();
        WritableRaster wr = icm.createCompatibleWritableRaster(1, 1);
        SampleModel sampleModel = wr.getSampleModel();
        sampleModel = sampleModel.createCompatibleSampleModel(width, height);
        return sampleModel;
    }

    private IndexColorModel getDefaultColorModel() {
        byte[] r = new byte[256];
        byte[] g = new byte[256];
        byte[] b = new byte[256];
        for (int i = 0; i < 256; i++) {
            r[i] = (byte) i;
            g[i] = (byte) i;
            b[i] = (byte) i;
        }
        IndexColorModel defaultColorModel = new IndexColorModel(8, 256, r, g, b);
        return defaultColorModel;
    }
}
