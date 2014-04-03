package cs310MRAK.rackcity.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cs310MRAK.rackcity.shared.UserInfo;
import cs310MRAK.rackcity.shared.UserSearchHistoryInstance;
import cs310MRAK.rackcity.shared.rackStarRatings;

@RemoteServiceRelativePath("userinfo")
public interface userService extends RemoteService {
	
	// adder
	public void addUser(String id, String name,  String email, String gender, Boolean isPlus, String propic, String favbike, String bikename, String bcolor);
	public void addUserSearchHistoryInstance(String key, String userID, String searchAddress, int radius, int crimeScore, int rate);
	public void addStarRating(String userID, String addr, String pos, int starratings);
	
	// checker 
	public Boolean hasUser(String id);
	
	// getter
	public ArrayList<UserInfo> getUser(String uid);
	public ArrayList<UserSearchHistoryInstance> getHistory(String uid);
	public ArrayList<rackStarRatings> getStarRating(String userID , String addr, String pos, int type);
	
	// updater
	public void changeUserName(String id, String newName);
	public void changeUserEmail(String id, String newEmail);
	public void changeUserGender(String id, String newGender);
	public void changeUserPlus(String id, Boolean newStatus);
	public void cahngeUserPic(String id, String newURL);
	
	// remover
	public void removeUser(String id);


}
