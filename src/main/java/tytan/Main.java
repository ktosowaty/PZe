package tytan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {
    private ResourceBundle rb;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        rb = ResourceBundle.getBundle("LangBundle", new Locale("pl", "PL"));
        loader.setResources(rb);
        loader.setLocation(getClass().getResource("/fxml/RootLayout.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Tytan");
        primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("/images/logo.png"))));
        Scene scene = new Scene(root, 720, 480);
        scene.getStylesheets().add(getClass().getResource("/css/my-theme.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
