package tytan.mapa;

import com.jfoenix.controls.JFXSlider;
import javafx.fxml.FXML;

public class MapaSettingsController {

    @FXML
    private JFXSlider slider;
    private MapaController mapa;

    public void setMapa(MapaController mapa) {
        this.mapa = mapa;
    }
}
