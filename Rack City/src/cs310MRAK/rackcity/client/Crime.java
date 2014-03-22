package cs310MRAK.rackcity.client;

import com.google.gwt.maps.client.geom.LatLng;


public class Crime {

	public String address;
	public String coordinate;

	// TODO: will need to add User object to the constructor as well
	// TODO: make @persistent
	public Crime(String address, String coordinate){
		this.address = address;
		this.coordinate = coordinate;
	}

	public String getAddress() {
		return this.address;
	}

	public LatLng getCoordinate() {
		String c = this.coordinate;
		// ====== Process LL ========
		String[] results = c.split( "," );
		String Lat = results[0];
		String Lon = results[1];
		Lat = Lat.substring(1);
		Lon = Lon.substring(0, Lon.length() - 1);
		double LatVal = Double.parseDouble(Lat);
		double LonVal = Double.parseDouble(Lon);
		LatLng pos = LatLng.newInstance(LatVal, LonVal);
		return pos;
	}

}
