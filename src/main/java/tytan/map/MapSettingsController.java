package tytan.map;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MapSettingsController {

    public JFXToggleButton fireLinesBtn;
    public Button btnRoute;
    public JFXTextField tfTo;
    public JFXTextField tfFrom;
    public JFXToggleButton btnShowRoute;
    @FXML
    private JFXToggleButton visibleButton;
    @FXML
    private JFXToggleButton visibleLocationsBtn;
    @FXML
    private JFXToggleButton visiblePlacesBtn;
    @FXML
    private MapController map;

    public MapSettingsController() {

    }

    public void setMap(MapController map) {
        this.map = map;
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
        map.mapModel.setLocationVisible(selected);
    }

    @FXML
    private void setVisiblePlaces(ActionEvent actionEvent) {
        boolean selected = visiblePlacesBtn.isSelected();
        map.mapModel.setPlaceVisible(selected);
    }

    @FXML
    public void changeFireVisible(ActionEvent actionEvent) {
        boolean selected = fireLinesBtn.isSelected();
        map.mapModel.setAreaVisible(selected);
    }

    public void drawRoute(ActionEvent actionEvent) {
        map.mapModel.addRoute(tfFrom.getText(), tfTo.getText());
        map.drawer.toBack();
    }

    public void showRouteInfo(ActionEvent actionEvent) {
        boolean selected = btnShowRoute.isSelected();
        if (selected)
            map.mapModel.googleMap.showDirectionsPane();
        else map.mapModel.googleMap.hideDirectionsPane();
    }
}
