package cs310MRAK.rackcity.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rackfavouritesservice")
public interface RackFavouritesService extends RemoteService {

	void addFavourite(String rackID);
	ArrayList<String> getFavourites();
	void removeFavourite(String rackID);
	
}
