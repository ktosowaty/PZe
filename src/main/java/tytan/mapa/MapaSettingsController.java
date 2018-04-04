package tytan.mapa;

import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MapaSettingsController {

    @FXML
    private JFXToggleButton visibilityButton;
    private MapaController mapa;

    public void setMapa(MapaController mapa) {
        this.mapa = mapa;
    }

    @FXML
    private void setVisibilityMap(ActionEvent actionEvent) {
        if (visibilityButton.isSelected()) {
            mapa.setMapVisible(true);
        } else {
            mapa.setMapVisible(false);
        }
    }
}
