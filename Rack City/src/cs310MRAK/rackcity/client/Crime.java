package cs310MRAK.rackcity.client;


public class Crime {
	
	public String address;
	//public GeoPoint coordinate;
	
	// TODO: will need to add User object to the constructor as well
	// TODO: make @persistent
	public Crime(String address	){//, GeoPoint coordinate) {
		this.address = address;
//		this.coordinate = coordinate;
	}
	
	public String getAddress() {
		return this.address;
	}
	
//	public GeoPoint getCoordinate() {
//		return this.coordinate;
//	}

}
