package cs310MRAK.rackcity.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("stolenbikesservice")
public interface StolenBikesService extends RemoteService{
	
		void addStolenBike(String rackID);
		
	}


