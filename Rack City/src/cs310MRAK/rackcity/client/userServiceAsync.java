package cs310MRAK.rackcity.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cs310MRAK.rackcity.shared.UserSearchHistoryInstance;

public interface userServiceAsync {
		
		// adder
		void addUser(String id, String name,  String email, String gender, Boolean isPlus, String propic, AsyncCallback<Void> callback);
		void addUserSearchHistoryInstance(String key, String userID, String searchAddress, int radius, int crimeScore, int rate, AsyncCallback<Void> callback);
		
		// checker
		void hasUser(String id, AsyncCallback<Boolean> callback);
		
		// getter
		void getHistory (String uid, AsyncCallback<ArrayList<UserSearchHistoryInstance>> callback);
		
		// updater
		void changeUserName(String id, String newName, AsyncCallback<Void> callback);
		void changeUserEmail(String id, String newEmail, AsyncCallback<Void> callback);
		void changeUserGender(String id, String newGender, AsyncCallback<Void> callback);
		void changeUserPlus(String id, Boolean newStatus, AsyncCallback<Void> callback);
		void cahngeUserPic(String id, String newURL, AsyncCallback<Void> callback);
		
		// remover
		void removeUser(String id, AsyncCallback<Void> callback);
}
