package tytan.videochat;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.events.JFXDrawerEvent;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import tytan.data.rdp.repository.web_socket.ClientWebSocketRepository;
import tytan.data.rdp.repository.web_socket.StreamWebSocketRepository;
import tytan.data.rdp.specification.web_socket.CreateClientConnectionSpecification;
import tytan.data.rdp.specification.web_socket.StreamVideoSpecification;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

//TODO NEEDS REFACTOR
public class VideoChatOperatorController implements Initializable {

    private StreamWebSocketRepository streamWebSocketRepository = new StreamWebSocketRepository();
    private ClientWebSocketRepository clientWebSocketRepository = new ClientWebSocketRepository();

    private AnchorPane menuPane;
    private boolean isStreaming = false;


    @FXML
    private Button settingsButton;
    @FXML
    private JFXDrawer drawer;

    @FXML
    BorderPane bpWebCamPaneHolder;

    @FXML
    ImageView imgWebCamCapturedImage;

    @FXML
    Button streamingButton;

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

    public void back(MouseEvent mouseEvent) {
        if (selWebCam != null) {
            selWebCam.close();
        }
        menuPane.toFront();
    }

    public void setMenuPane(AnchorPane menuPane) {
        this.menuPane = menuPane;
    }

    private BufferedImage grabbedImage;
    private Webcam selWebCam = null;
    private boolean stopCamera = false;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();
    private StreamVideoSpecification streamVideoSpecification;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resources);
            Pane vBox = loader.load(getClass().getResource("/fxml/videochat/VideoChatOperatorSettings.fxml").openStream());
            drawer.setSidePane(vBox);
            VideoChatOperatorSettingsController settings = loader.getController();
            settings.setVideoChat(this);
            clientWebSocketRepository.streamQuery(new CreateClientConnectionSpecification()).subscribe(onNext -> {
                streamVideoSpecification = new StreamVideoSpecification("Klient1");
            }, error -> {
                error.printStackTrace();
                System.out.println("Getting exception: " + error.getMessage());
            });
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
//                setImageViewSize();
                    initializeWebCam();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void initializeWebCam() {

        Task<Void> webCamIntilizer = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                if (selWebCam == null) {
                    selWebCam = Webcam.getDefault(10, TimeUnit.SECONDS);
                    selWebCam.addWebcamListener(new WebcamListener() {
                        @Override
                        public void webcamOpen(WebcamEvent webcamEvent) {

                        }

                        @Override
                        public void webcamClosed(WebcamEvent webcamEvent) {

                        }

                        @Override
                        public void webcamDisposed(WebcamEvent webcamEvent) {

                        }

                        @Override
                        public void webcamImageObtained(WebcamEvent webcamEvent) {
                            if (isStreaming) {
                                try {
                                    BufferedImage image = webcamEvent.getImage();
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    ImageIO.write(image, "jpg", baos);
                                    byte[] bytes = baos.toByteArray();
                                    ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
//                                    streamWebSocketRepository.streamQuery().subscribe();
                                    if (streamVideoSpecification != null)
                                        streamVideoSpecification.sendMessage(byteBuffer);
                                    System.out.println("Bytes length: " + bytes.length);
                                } catch (IOException exception) {
                                    exception.printStackTrace();
                                }
                            }
                        }
                    });
                    selWebCam.open();
                }
                startWebCamStream();
                return null;
            }

        };

        new Thread(webCamIntilizer).start();
    }

    protected void startWebCamStream() {

        stopCamera = false;
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                while (!stopCamera) {
                    try {
                        if ((grabbedImage = selWebCam.getImage()) != null) {

                            Platform.runLater(new Runnable() {

                                @Override
                                public void run() {
                                    final Image mainiamge = SwingFXUtils
                                            .toFXImage(grabbedImage, null);
                                    imageProperty.set(mainiamge);
                                }
                            });

                            grabbedImage.flush();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }

        };
        Thread webCamStream = new Thread(task);
        webCamStream.setDaemon(true);
        webCamStream.start();
        imgWebCamCapturedImage.imageProperty().bind(imageProperty);

    }

    public void startCamera(ActionEvent event) {
        if (isStreaming) {
            streamingButton.setText("Start stream");
        } else {
            streamingButton.setText("Stop stream");
        }
        isStreaming = !isStreaming;
    }

}
