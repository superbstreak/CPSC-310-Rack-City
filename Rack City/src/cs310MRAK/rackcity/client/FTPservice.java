package cs310MRAK.rackcity.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("grabData")
public interface FTPservice extends RemoteService{

	public void adminConnection();
	
}