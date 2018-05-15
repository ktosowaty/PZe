package tytan.meldunki;

import com.lynden.gmapsfx.javascript.object.GoogleMap;

import tytan.map.MapModel;

public class MeldunkiPersonalLocation {
	public static boolean personal=false;

	public static void setPersonalLocation(GoogleMap gMap) {
		
		//gMap.clearMarkers();
		if(personal==true) {
		MapModel.addPersonalLocationMarker(MapModel.latLong);
		personal=false;
		}

	}
}
