package cs310MRAK.rackcity.client;

import com.google.appengine.api.search.GeoPoint;

public class Crime {
	
	public String address;
	public GeoPoint coordinate;
	
	public Crime(String address, GeoPoint coordinate) {
		this.address = address;
		this.coordinate = coordinate;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public GeoPoint getCoordinate() {
		return this.coordinate;
	}

}
