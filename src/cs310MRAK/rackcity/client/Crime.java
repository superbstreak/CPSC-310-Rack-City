package cs310MRAK.rackcity.client;

import com.google.gwt.maps.client.geom.LatLng;


public class Crime {

	public String address;
	public LatLng coordinate;

	// TODO: will need to add User object to the constructor as well
	// TODO: make @persistent
	public Crime(String address, LatLng coordinate){
		this.address = address;
		this.coordinate = coordinate;
	}

	public String getAddress() {
		return this.address;
	}

	public LatLng getCoordinate() {
		return this.coordinate;
	}

}
