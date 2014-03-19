package cs310MRAK.rackcity.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface crimeServiceAsync {
	
	// latlng are not serialized, all to string
	void addCrime(int year, String pos, AsyncCallback<Void> callback);
	
	// getters
	void getYear(String pos, AsyncCallback<Integer> callback);
	
	//remover
	void removeCrime (String pos, AsyncCallback<Void> callback);
}
