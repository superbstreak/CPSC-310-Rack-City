package cs310MRAK.rackcity.client;

import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StolenBikesServiceAsync {

	void addStolenBike(String rackID, LatLng coordinate, AsyncCallback callback);//, GeoPoint coordinate, AsyncCallback callback);

}
