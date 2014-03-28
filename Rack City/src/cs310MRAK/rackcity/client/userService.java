package cs310MRAK.rackcity.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cs310MRAK.rackcity.shared.UserSearchHistoryInstance;

@RemoteServiceRelativePath("userinfo")
public interface userService extends RemoteService {
	
	// adder
	public void addUser(String id, String name,  String email, String gender, Boolean isPlus, String propic);
	public void addUserSearchHistoryInstance(String userID, String searchAddress, int radius, int crimeScore, int rate);
	
	// checker 
	public Boolean hasUser(String id);
	
	// getter
	public ArrayList<UserSearchHistoryInstance> getHistory(String uid);
	
	// updater
	public void changeUserName(String id, String newName);
	public void changeUserEmail(String id, String newEmail);
	public void changeUserGender(String id, String newGender);
	public void changeUserPlus(String id, Boolean newStatus);
	public void cahngeUserPic(String id, String newURL);
	
	// remover
	public void removeUser(String id);


}
