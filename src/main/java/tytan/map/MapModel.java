package tytan.map;

import com.lynden.gmapsfx.GoogleMapView;
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

import tytan.Main;
import tytan.meldunki.MeldunkiMedicalHelpLocation;
import tytan.meldunki.MeldunkiPersonalLocation;


import java.util.ArrayList;

public class MapModel implements DirectionsServiceCallback {
    public static GoogleMap googleMap;
    private GoogleMapView gmv;
    private static ArrayList<Marker> locationMarkers;
    private ArrayList<Marker> placeMarkers;
    private ArrayList<Circle> circles;
    private ArrayList<Polyline> fireLines;
    private static boolean locationMarkersVisible;
    private boolean placeMarkersVisible;
    private DirectionsService directionsService;
    private DirectionsPane directionsPane;
    private ContextMenu contextMenu;
    public static LatLong latLong;
    public static Marker personalMarker;
    public static Marker medicalHelpMarker;

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
    public static void addPersonalLocationMarker(LatLong latLong) {
    	if(personalMarker != null) googleMap.removeMarker(personalMarker);
    	
        personalMarker = new Marker(new MarkerOptions()
                .position(latLong)
                .animation(Animation.DROP)
                .icon("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAADeSURBVEhL3dU7CsJAGATgrQTFAyh4HhVP4+sAdjYWnsGj+ajs1U5nIIFlmcFk3TQOfCAy/78hhhj+Lgu4wjsTZ+dgcwE12MYZbNRADhtVpgcsYVRZwxNUl2xUmVaQhoeoLtmoMvGq04xBdclGlYnL0hQ9QN2iLagu2agy8UfmPedV0wZeoLpko8o5bFQ5h01avMMBZjCBfoWfp7CHG6RzNnHpBEP4lh4cIZ61iUtNltcZQDxrE5d+YaPKOWw6f13zz4IFNdgEZ/nEtc4O0mX8rmjiQ4ovr8PFnS3PSAgf2xsPkja7LTgAAAAASUVORK5CYII=")
                .visible(locationMarkersVisible)
                .title("Moja pozycja"));
        googleMap.addMarker(personalMarker);
        locationMarkers.add(personalMarker);
    }
    public static void addMedicalHelpMarker(LatLong latLong) {
    	if(medicalHelpMarker != null) googleMap.removeMarker(personalMarker);
    	
        medicalHelpMarker = new Marker(new MarkerOptions()
                .position(latLong)
                .animation(Animation.DROP)
                .icon("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAABUSURBVDhPY6AWaAXin0D8nwgMUtcCxCgAJCgIYRIEIHUg9SgAZDIpAEM9LgOIFh9cBoDYuDAMILPBAEMACogWH7wG4AIY6ilOiaTmhSYgHhSAgQEAVJA67J3A5PkAAAAASUVORK5CYII=")
                .visible(locationMarkersVisible)
                .title("Pomoc medyczna"));
        googleMap.addMarker(medicalHelpMarker);
        locationMarkers.add(medicalHelpMarker);
    }
    public static void addFriendlyLocationMarker(LatLong latLong) {
        Marker marker = new Marker(new MarkerOptions()
                .position(latLong)
                .animation(Animation.DROP)
                .icon("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeCAYAAAA7MK6iAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAANgSURBVEhLvVZLTxNRFCbxlajRuHJr3LlmS2xop+20QGKgpSLlkRASE1hAAggCiTw2vIP+CWNijC50YQSUQB9UwqNEDEYWNAUUaWmnBY3mer7JxbTTsdPW4pd8CZ17zne45557zi3IFH67/ey8IFR6LJYXHlEMugThO4i/F0TxOdZgw83zA4/RaPeaTNvLdnsk0NrKQgMDLDY6KjM0OMjwbcVmO/CYTEGXwVDB3XLHE7v9lNtsfugrKZG+9vSww4mJtIQNbN2iOAlfLpM9PGbzo+XyckkaGlINpEZpeJjBhzI0yWWyA9K7YLXGpJER1QDpiOALtPM5vf4Wl8sMKBI6r5293t4U0Z3OTrbqdDIqKpmrzhr5m9IOaUdd+AoLz3BZbVCBOJZttohS7HNTE/PZbCww/Y6FwzGZgZlZtninWl5T2i+Rxrxeb+ey2sCVQaUmimBXCBr6FmGRHyyJ+IY15c6h4TWbn3FZbeBa4MokiiC92Kky6DEDU29lm0QfaECLy2rDLQhHyqLCeYYP4qpBQaTdY7Um+UADWlxWG3TGP2NjY0kiuQSGBrS4rDZQ0ft9fUkiqzU1ciGpBQW33sykpBoaqGwuqw2vKE5tt7cniaBwUL3pimu3qyvJJ9jWxqiPv+ay2qDmf+9jff1hogh4fJ2wO/k6hSQ5C+9vV7HN5uYkW3C9ri7u1us7uKw2vAbDdWqXMaUQ+KeB0HmCfmogyp2C8fFxpDnu0umucdnMQOn2f+nuThHMlPClNC9xucxB8/Wu3+GQ1EQz4YrDEaUja+RymWNap7voNhqjYZq3asLpiMbhNpnCvtLS81wuO9CEerBeW5tSZFr84HTGXCZTJ5fJHj5BuEwCkrJ9puN+fz+jf/hgyWi8wGVyA7W8+2tVVVG1IGpcqayMkE8Ld88dL0XxHJ1XUO3KKLnT0YErtEn1cZq7/xvcBkOJ12KJ4XGnFhDE84iuIBpGMXfLD2iuvtpobPxroVERxukWPObm+YNLr7/qMhrDyuEBollQp9ul1+Ulbp5fUIVXLJaVRRJHJmYuPQqj1HB03OxkQHP56UZDw9Fx4LXq6jiewXz55DBbVHSFUr6HYbHV0vKLKv4TKp8vnyzmiotvYnrRgwH9+Ab//H9AFdyORz//mSUKCn4DQp7pmFaRNZYAAAAASUVORK5CYII=")
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

        googleMap.addMouseEventHandler(UIEventType.click, (me) -> {
            Main.getClient().getController().sendBrodcastMessage(me.getLatLong().toString());
            if(MeldunkiPersonalLocation.personal==true) {
      	   latLong = me.getLatLong();

      	   System.out.println("Latitude: " + latLong.getLatitude());
      	   System.out.println("Longitude: " + latLong.getLongitude());
      	   MeldunkiPersonalLocation.setPersonalLocation(googleMap);

        	}
            else  if(MeldunkiMedicalHelpLocation.medHelp==true) {
           	   latLong = me.getLatLong();

           	   System.out.println("Latitude: " + latLong.getLatitude());
           	   System.out.println("Longitude: " + latLong.getLongitude());
           	   MeldunkiMedicalHelpLocation.setMedicalHelpLocation(googleMap);

             	}
      	   
      	});

        addLocationMarker(new LatLong(52.13, 21.00));
        addPlaceMarker(new LatLong(52.20, 21.10));
        addFireLine(new LatLong(52.13, 21.00), new LatLong(52.20, 21.10));
        addRoute("Warszawa, Wojskowa Akademia Techniczna", "Warszawa, Dworzec Zachodni");
    }

    @Override
    public void directionsReceived(DirectionsResult results, DirectionStatus status) {

    }
}

