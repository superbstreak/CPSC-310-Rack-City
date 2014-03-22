package cs310MRAK.rackcity.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("crime")
public interface crimeService extends RemoteService {
	
	// latlng are not serialized, all latlng convert to string
	public void addCrime (int year, String pos);
	
	// get
	public int getYear (String pos);
	public ArrayList<String[]> getCrimes();
	
	// remove
	public void removeCrime (String pos);

}
