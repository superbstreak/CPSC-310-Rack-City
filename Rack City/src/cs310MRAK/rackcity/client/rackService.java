package cs310MRAK.rackcity.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rack")
public interface rackService extends RemoteService {
	
	public void addRack(String addr, String p, int rnum, int stolen, double cs, double rate);
	public void updateCS(String p, double score);
	public void updateStolen(String p, int steal);
	public void updateRate(String p, double rating);
	
	public String getAddress(String p);
	public int getRackNum(String p);
	public int getStolen(String p);
	public double getCS(String p);
	public double getRate(String p);
}
