package cs310MRAK.rackcity.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rackfavouritesservice")
public interface RackFavouritesService extends RemoteService {
	
	//LatLng cannot be serialized
	
	
	// > implying address is a sufficient unique identifier which we will call "rackID"
	public void addToFavorite(String uid, String address, String cooridinate);
	
	
	public ArrayList<String[]> getFavorite(String uid);
	

	public void removeFavorite(String uid, String cooridinate);
	
}
