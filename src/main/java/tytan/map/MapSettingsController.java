package tytan.map;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MapSettingsController {

    public JFXToggleButton fireLinesBtn;
    @FXML
    private JFXToggleButton visibleButton;
    @FXML
    private JFXToggleButton visibleLocationsBtn;
    @FXML
    private JFXToggleButton visiblePlacesBtn;
    private MapController map;

    public void setMap(MapController map) {
        this.map = map;
    }

    public MapSettingsController() {

    }

    @FXML
    private void setVisibleMap(ActionEvent actionEvent) {
        if (visibleButton.isSelected()) {
            map.setMapVisible(true);
        } else {
            map.setMapVisible(false);
        }
    }

    @FXML
    private void setVisibleLocations(ActionEvent actionEvent) {
        boolean selected = visibleLocationsBtn.isSelected();
        map.mapModel.setLocationVisible(!selected);
    }

    @FXML
    private void setVisiblePlaces(ActionEvent actionEvent) {
        boolean selected = visiblePlacesBtn.isSelected();
        map.mapModel.setPlaceVisible(!selected);
    }

    @FXML
    public void changeFireVisible(ActionEvent actionEvent) {
        boolean selected = fireLinesBtn.isSelected();
        map.mapModel.setFireLinesVisible(!selected);
    }
}
