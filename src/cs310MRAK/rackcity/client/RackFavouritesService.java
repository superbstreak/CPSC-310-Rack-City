package cs310MRAK.rackcity.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rackfavouritesservice")
public interface RackFavouritesService extends RemoteService {
	
//	// > implying address is a sufficient unique identifier which we will call "rackID"
//	void addFavourite(String rackID, GeoPoint coordinate,
//			double rating, int rackCount, int crimeScore);
//	ArrayList<BikeRack> getFavourites();
//	void removeFavourite(String rackID, GeoPoint coordinate,
//			double rating, int rackCount, int crimeScore);
	
}
