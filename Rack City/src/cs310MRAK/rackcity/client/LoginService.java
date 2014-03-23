package cs310MRAK.rackcity.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cs310MRAK.rackcity.shared.LoginInfo;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	
  public LoginInfo login(String requestUri);
  
  public String getUserEmail(String token);
  
  public LoginInfo loginProfile(String token);
  
  // requests (pull to +)
}