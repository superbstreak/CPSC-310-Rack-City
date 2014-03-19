package cs310MRAK.rackcity.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("crime")
public interface crimeService extends RemoteService {
	
	// latlng are not serialized, all latlng convert to string
	public void addCrime (int year, String pos);
	
	// get
	public int getYear (String pos);
	
	// remove
	public void removeCrime (String pos);

}
