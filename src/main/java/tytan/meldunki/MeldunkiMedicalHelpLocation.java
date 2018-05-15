package tytan.meldunki;

import com.lynden.gmapsfx.javascript.object.GoogleMap;

import tytan.map.MapModel;

public class MeldunkiMedicalHelpLocation {
	public static boolean medHelp=false;

	public static void setMedicalHelpLocation(GoogleMap gMap) {
		
		//gMap.clearMarkers();
		if(medHelp==true) {
		MapModel.addMedicalHelpMarker(MapModel.latLong);
		//medHelp=false;
		}

	}
}
