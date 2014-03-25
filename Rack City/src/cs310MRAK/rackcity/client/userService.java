package cs310MRAK.rackcity.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("userinfo")
public interface userService extends RemoteService {
	
	// adder
	public void addUser(String id, String name,  String email, String gender, Boolean isPlus, String propic);
	void addUserSearchHistoryInstance(String userID, String searchAddress,
			String searchAddressLatLong, String radius, String crimeScore);
	
	// checker 
	public Boolean hasUser(String id);
	
	// updater
	public void changeUserName(String id, String newName);
	public void changeUserEmail(String id, String newEmail);
	public void changeUserGender(String id, String newGender);
	public void changeUserPlus(String id, Boolean newStatus);
	public void cahngeUserPic(String id, String newURL);
	
	// remover
	public void removeUser(String id);


}
