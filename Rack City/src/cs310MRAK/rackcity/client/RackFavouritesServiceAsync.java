package cs310MRAK.rackcity.client;

import java.util.ArrayList;
import 	com.google.gwt.user.client.rpc.AsyncCallback;

public interface RackFavouritesServiceAsync {

	
	void addToFavorite(String uid, String address, String cooridinate, AsyncCallback<Void> callback);
	
	void getFavorite(String uid, AsyncCallback<ArrayList<String[]>> callback);
	

	void removeFavorite(String uid, String cooridinate, AsyncCallback<Void> callback);
}
