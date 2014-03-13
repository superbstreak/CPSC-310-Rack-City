package cs310MRAK.rackcity.client;

import com.google.appengine.api.search.GeoPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StolenBikesServiceAsync {

	void addStolenBike(String rackID, GeoPoint coordinate, AsyncCallback callback);

}
