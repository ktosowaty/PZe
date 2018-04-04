package tytan.mapa;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.object.*;

import java.util.ArrayList;

public class MapaModel {
    public GoogleMap googleMap;
    private ArrayList<Marker> locationMarkers;
    private ArrayList<Marker> placeMarkers;
    private boolean locationMarkersVisible;
    private boolean placeMarkersVisible;

    public MapaModel(GoogleMapView googleMapView) {
        googleMapView.addMapInializedListener(() -> {
            configureMap(googleMapView);
        });
        locationMarkersVisible = true;
        placeMarkersVisible = true;
    }

    public void setLocationVisable(boolean visible) {
        locationMarkersVisible = visible;
        for (Marker m : locationMarkers) {
            m.setVisible(visible);
        }
    }

    public void setPlaceVisable(boolean visable) {
        placeMarkersVisible = visable;
        for (Marker m : placeMarkers) {
            m.setVisible(visable);
        }
    }

    public void addLocationMarker(LatLong latLong) {
        Marker marker = new Marker(new MarkerOptions()
                .position(latLong)
                .animation(Animation.DROP)
                .icon("https://raw.githubusercontent.com/Concept211/Google-Maps-Markers/master/images/marker_blueL.png")
                .visible(locationMarkersVisible));
        googleMap.addMarker(marker);
        locationMarkers.add(marker);
    }

    public void addPlaceMarker(LatLong latLong) {
        Marker marker = new Marker(new MarkerOptions()
                .position(latLong)
                .animation(Animation.DROP)
                .icon("https://raw.githubusercontent.com/Concept211/Google-Maps-Markers/master/images/marker_greenP.png")
                .visible(placeMarkersVisible));
        googleMap.addMarker(marker);
        placeMarkers.add(marker);

    }

    private void configureMap(GoogleMapView googleMapView) {
        MapOptions mapOptions = new MapOptions();
        mapOptions.center(new LatLong(52.13, 21.00))
                .mapType(MapTypeIdEnum.ROADMAP)
                .zoom(10)
                .streetViewControl(false);
        googleMapView.setKey("AIzaSyC5U5zdeCQ-i0JjG6TV1lbiWiA98LASJ2E");
        googleMap = googleMapView.createMap(mapOptions, false);
        addLocationMarker(new LatLong(52.13, 21.00));
        addPlaceMarker(new LatLong(52.20, 21.10));
    }

}
