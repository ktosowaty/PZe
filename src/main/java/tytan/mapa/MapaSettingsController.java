package tytan.mapa;

import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MapaSettingsController {

    @FXML
    private JFXToggleButton visibleButton;
    @FXML
    private JFXToggleButton visibleLocationsBtn;
    @FXML
    private JFXToggleButton visiblePlacesBtn;
    private MapaController mapa;

    public void setMapa(MapaController mapa) {
        this.mapa = mapa;
    }

    @FXML
    private void setVisibleMap(ActionEvent actionEvent) {
        if (visibleButton.isSelected()) {
            mapa.setMapVisible(true);
        } else {
            mapa.setMapVisible(false);
        }
    }

    @FXML
    private void setVisibleLocations(ActionEvent actionEvent) {
        if (visibleLocationsBtn.isSelected()) {
            mapa.mapaModel.setLocationVisable(true);
        } else {
            mapa.mapaModel.setLocationVisable(false);
        }
    }

    @FXML
    private void setVisiblePlaces(ActionEvent actionEvent) {
        if (visiblePlacesBtn.isSelected()) {
            mapa.mapaModel.setPlaceVisable(true);
        } else {
            mapa.mapaModel.setPlaceVisable(false);
        }
    }
}
