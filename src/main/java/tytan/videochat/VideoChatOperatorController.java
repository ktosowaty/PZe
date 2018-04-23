package tytan.videochat;

import com.github.sarxos.webcam.Webcam;
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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class VideoChatOperatorController implements Initializable {

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

    private AnchorPane menuPane;
    private boolean isStreaming = false;

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

    @FXML
    private void startStreaming(MouseEvent mouseEvent) {
        //TODO
        System.out.println("startStreaming");
    }

    @FXML
    private void stopStreaming(MouseEvent mouseEvent) {
        //TODO
        System.out.println("stopStreaming");
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

    private String cameraListPromptText = "Choose Camera";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resources);
            Pane vBox = loader.load(getClass().getResource("/fxml/videochat/VideoChatOperatorSettings.fxml").openStream());
            drawer.setSidePane(vBox);
            VideoChatOperatorSettingsController settings = loader.getController();
            settings.setVideoChat(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
//                setImageViewSize();
                initializeWebCam();
            }
        });

    }

    protected void initializeWebCam() {

        Task<Void> webCamIntilizer = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                if (selWebCam == null) {
                    selWebCam = Webcam.getDefault(10, TimeUnit.SECONDS);
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
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        imgWebCamCapturedImage.imageProperty().bind(imageProperty);

    }

    private void closeCamera() {
        if (selWebCam != null) {
            selWebCam.close();
        }
    }

    public void stopCamera(ActionEvent event) {
        stopCamera = true;
//        btnStartCamera.setDisable(false);
//        btnStopCamera.setDisable(true);
    }

    public void startCamera(ActionEvent event) {
        if (isStreaming) {
            streamingButton.setText("Start stream");
        } else {
            streamingButton.setText("stop stream");
        }
        isStreaming = !isStreaming;
//        btnStartCamera.setDisable(true);
//        btnStopCamera.setDisable(false);
    }

}
