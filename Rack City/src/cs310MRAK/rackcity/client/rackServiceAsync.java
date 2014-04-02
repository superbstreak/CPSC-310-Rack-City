package cs310MRAK.rackcity.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface rackServiceAsync {
	
	// latlng are not serialized, all to string
	void addRack(String addr, String p, int rnum, int stolen, double cs, double rate, AsyncCallback<Void> callback);
	void updateCS(String p, double score, AsyncCallback<Void> callback);
	void updateStolen(String p, int steal, AsyncCallback<Void> callback);
	void updateRate(String p, double rating, AsyncCallback<Void> callback);
	void addBikeRackTimeHit(String pos, AsyncCallback<Void> callback);

	
	// getters
	void getAddress(String p, AsyncCallback<String> callback);
	void getRackNum(String p, AsyncCallback<Integer> callback);
	void getStolen(String p, AsyncCallback<Integer> callback);
	void getCS(String p, AsyncCallback<Double> callback);
	void getRate(String p, AsyncCallback<Double> callback);
	
	// parser
	void getRacks(AsyncCallback<ArrayList<String[]>> callback);
	
	//remover
	void removeRack(String pos, AsyncCallback<Void> callback);
}
