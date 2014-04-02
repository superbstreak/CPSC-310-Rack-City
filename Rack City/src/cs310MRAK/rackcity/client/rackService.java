package cs310MRAK.rackcity.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rack")
public interface rackService extends RemoteService {
	
	// latlng are not serialized, all latlng convert to string
	public void addRack(String addr, String p, int rnum, int stolen, double cs, double rate);
	public void updateCS(String p, double score);
	public void updateStolen(String p, int steal);
	public void updateRate(String p, double rating);
	
	// get
	public String getAddress(String p);
	public int getRackNum(String p);
	public int getStolen(String p);
	public double getCS(String p);
	public double getRate(String p);
	public String getRackTimeHits(String p);

	
	// parser
	public ArrayList<String[]> getRacks();
	
	// remove
	public void removeRack(String pos);
	public void addBikeRackTimeHit(String pos);
	public void updateBikeRackTimeHit(String pos, int timeHits, String whichOne);
}
