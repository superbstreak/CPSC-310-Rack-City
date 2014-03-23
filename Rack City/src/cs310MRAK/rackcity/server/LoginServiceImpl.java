package cs310MRAK.rackcity.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import cs310MRAK.rackcity.client.LoginService;
import cs310MRAK.rackcity.shared.LoginInfo;

import java.net.URL;
import java.util.logging.Level;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


public class LoginServiceImpl extends RemoteServiceServlet implements LoginService 
{
	private static final long serialVersionUID = 5734642814593515932L;


	public LoginInfo login(String requestUri)
	{
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		LoginInfo loginInfo = new LoginInfo();

		if (user != null) 
		{
			loginInfo.setLoggedIn(true);
			loginInfo.setEmailAddress(user.getEmail());
			loginInfo.setNickname(user.getNickname());
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
		} 
		else 
		{
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
		return loginInfo;
	}

	@Override
	public String getUserEmail(String token) 
	{
		final UserService userService = UserServiceFactory.getUserService();
		final User user = userService.getCurrentUser();
		if (null != user)
		{
			return user.getEmail();
		} 
		else 
		{
			return "noreply@unknown.com";
		}
	}

	@Override
	public LoginInfo loginProfile(String token) 
	{
		if (token != null && !token.isEmpty())
		{
			
		}
		return null;
	}

}