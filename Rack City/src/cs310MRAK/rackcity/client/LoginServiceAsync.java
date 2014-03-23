package cs310MRAK.rackcity.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cs310MRAK.rackcity.shared.LoginInfo;

public interface LoginServiceAsync {
	
	
  void login(String requestUri, AsyncCallback<LoginInfo> async);
  
  void getUserEmail(String token, AsyncCallback<String> callback);
  
  void loginProfile(String token, AsyncCallback<LoginInfo> callback);
  
  // ast in
}
