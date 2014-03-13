package cs310MRAK.rackcity.client;

import 	com.google.gwt.user.client.rpc.AsyncCallback;

public interface RackFavouritesServiceAsync {

	
	void addFavourite(String rackID, AsyncCallback callback);
	
	// it's okay to say void even though it's a string array return type, apparently
	// youtube.com/watch?v=dBZyWq13AQg
	void getFavourites(AsyncCallback callback);
	
	
	void removeFavourite(String rackID, AsyncCallback callback);
}
