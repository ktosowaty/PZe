package tytan.map;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.service.directions.*;
import com.lynden.gmapsfx.shapes.Circle;
import com.lynden.gmapsfx.shapes.CircleOptions;
import com.lynden.gmapsfx.shapes.Polyline;
import com.lynden.gmapsfx.shapes.PolylineOptions;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import netscape.javascript.JSObject;

import java.util.ArrayList;

public class MapModel implements DirectionsServiceCallback {
    public GoogleMap googleMap;
    private GoogleMapView gmv;
    private ArrayList<Marker> locationMarkers;
    private ArrayList<Marker> placeMarkers;
    private ArrayList<Circle> circles;
    private ArrayList<Polyline> fireLines;
    private boolean locationMarkersVisible;
    private boolean placeMarkersVisible;
    private DirectionsService directionsService;
    private DirectionsPane directionsPane;
    private ContextMenu contextMenu;

    public MapModel(GoogleMapView googleMapView) {
        googleMapView.addMapInializedListener(() -> configureMap(googleMapView));
        locationMarkersVisible = true;
        placeMarkersVisible = true;
        locationMarkers = new ArrayList<>();
        placeMarkers = new ArrayList<>();
        fireLines = new ArrayList<>();
        circles = new ArrayList<>();
        contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(new MenuItem("bhgjh"), new MenuItem("sdf"));
    }

    public void setLocationVisible(boolean visible) {
        if (!locationMarkers.isEmpty()) {
            locationMarkersVisible = visible;
            for (Marker m : locationMarkers) {
                m.setVisible(visible);
            }
        }
    }

    public void setPlaceVisible(boolean visible) {
        placeMarkersVisible = visible;
        if (!placeMarkers.isEmpty()) {
            for (Marker m : placeMarkers) {
                m.setVisible(visible);
            }
        }
    }

    public void addRoute(String from, String to) {
        directionsService = new DirectionsService();
        directionsPane = gmv.getDirec();
        DirectionsRequest request = new DirectionsRequest(from, to, TravelModes.DRIVING);
        DirectionsRenderer directionsRenderer = new DirectionsRenderer(true, gmv.getMap(), directionsPane);
        directionsService.getRoute(request, this, directionsRenderer);
    }

    public void addLocationMarker(LatLong latLong) {
        Marker marker = new Marker(new MarkerOptions()
                .position(latLong)
                .animation(Animation.DROP)
                .icon("https://raw.githubusercontent.com/Concept211/Google-Maps-Markers/master/images/marker_blueL.png")
                .visible(locationMarkersVisible));
        googleMap.addMarker(marker);
        locationMarkers.add(marker);
        Circle circle = addCircleArea(latLong,300);
        googleMap.addUIEventHandler(marker, UIEventType.mouseover, (JSObject obj) -> circle.setVisible(true));
        googleMap.addUIEventHandler(marker, UIEventType.mouseout, (JSObject obj) -> {
            circle.setVisible(false);
        });
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

    public Circle addCircleArea(LatLong latLong, int radius) {
        CircleOptions circleOptions = new CircleOptions().center(latLong).radius(radius).visible(false).fillOpacity(0.5);
        Circle circle = new Circle(circleOptions);
        circles.add(circle);
        googleMap.addMapShape(circle);
        return circle;
    }

    public void addFireLine(LatLong l1, LatLong l2) {
        PolylineOptions line_opt;
        Polyline line;

        LatLong[] path = {l1, l2};
        line_opt = new PolylineOptions();
        line_opt.path(new MVCArray(path))
                .clickable(false)
                .draggable(false)
                .editable(false)
                .strokeColor("#c0392b")
                .strokeWeight(2)
                .visible(true);

        line = new Polyline(line_opt);
        fireLines.add(line);
        googleMap.addMapShape(line);
    }

    public void setFireLinesVisible(boolean visible) {
        if (!fireLines.isEmpty()) {
            for (Polyline p : fireLines) {
                p.setVisible(visible);
            }
        }
    }

    private void configureMap(GoogleMapView googleMapView) {
        gmv = googleMapView;
        MapOptions mapOptions = new MapOptions();
        mapOptions.center(new LatLong(52.13, 21.00))
                .mapType(MapTypeIdEnum.ROADMAP)
                .zoom(10)
                .overviewMapControl(false)
                .panControl(false)
                .streetViewControl(false)
                .mapType(MapTypeIdEnum.TERRAIN);
        googleMapView.setKey("AIzaSyC5U5zdeCQ-i0JjG6TV1lbiWiA98LASJ2E");
        googleMap = googleMapView.createMap(mapOptions, false);
//        googleMap.addMouseEventHandler(UIEventType.dblclick, (GMapMouseEvent event) -> {
//            LatLong latLong = event.getLatLong();
//            addLocationMarker(latLong);
//        });
//        googleMap.addMouseEventHandler(UIEventType.rightclick, (GMapMouseEvent event) -> {
//            LatLong latLong = event.getLatLong();
//            addPlaceMarker(latLong);
//        });
        addLocationMarker(new LatLong(52.13, 21.00));
        addPlaceMarker(new LatLong(52.20, 21.10));
        addFireLine(new LatLong(52.13, 21.00), new LatLong(52.20, 21.10));
        addRoute("Warszawa, Wojskowa Akademia Techniczna", "Warszawa, Dworzec Zachodni");
    }

    @Override
    public void directionsReceived(DirectionsResult results, DirectionStatus status) {

    }
}

