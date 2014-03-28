package cs310MRAK.rackcity.server;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cs310MRAK.rackcity.client.userService;
import cs310MRAK.rackcity.shared.UserInfo;
import cs310MRAK.rackcity.shared.UserSearchHistoryInstance;

public class userServiceImpl  extends RemoteServiceServlet implements userService{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8733888600485828222L;
	
	//private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(userServiceImpl.class.getName());
		 
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	@Override
	public void addUser( String id, String name, String email, String gender, Boolean isPlus, String propic) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try
		{
			pm.makePersistent(new UserInfo(id, name, email, gender, isPlus, propic));
		}
	
		finally
		{
			pm.close();
		}
	}

	@Override
	public void changeUserName(String id, String newName) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try
		{
			UserInfo u = pm.getObjectById(UserInfo.class, id);
			u.setName(newName);
		}
		finally
		{
			pm.close();
		}
	}

	@Override
	public void changeUserEmail(String id, String newEmail) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try
		{
			UserInfo u = pm.getObjectById(UserInfo.class, id);
			u.setEmail(newEmail);
		}
		finally
		{
			pm.close();
		}
	}

	@Override
	public void changeUserGender(String id, String newGender) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try
		{
			UserInfo u = pm.getObjectById(UserInfo.class, id);
			u.setGender(newGender);
		}
		finally
		{
			pm.close();
		}
	}

	@Override
	public void changeUserPlus(String id, Boolean newStatus) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try
		{
			UserInfo u = pm.getObjectById(UserInfo.class, id);
			u.setisPlus(newStatus);
		}
		finally
		{
			pm.close();
		}
	}

	@Override
	public void cahngeUserPic(String id, String newURL) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try
		{
			UserInfo u = pm.getObjectById(UserInfo.class, id);
			u.setProfilePicURL(newURL);
		}
		finally
		{
			pm.close();
		}
	}

	@Override
	public void removeUser(String id) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try {
			long deleteCount = 0;	
			String query = "select from " + UserInfo.class.getName();
			List<UserInfo> Users = (List<UserInfo>) pm.newQuery(query).execute();
			for (UserInfo r: Users)
			{
				if (r.getId().equals(id))
				{
					deleteCount++;
					pm.deletePersistent(r);
				}
			}
		}
		finally
		{
			pm.close();
		}
	}
	
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

	@Override
	public Boolean hasUser(String id) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		Boolean output = false;
		try {	
			String query = "select from " + UserInfo.class.getName();
			List<UserInfo> Users = (List<UserInfo>) pm.newQuery(query).execute();
			for (UserInfo r: Users)
			{
				if (r.getId().equals(id))
				{
					output = true;
				}
			}
		}
		finally
		{
			pm.close();
		}
		return output;
	}
	
	@Override
	public void addUserSearchHistoryInstance(String userID, String searchAddress, int radius, int crimeScore, int rate) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try
		{
			pm.makePersistent(new UserSearchHistoryInstance(userID, searchAddress, radius, crimeScore, rate));
		}
		finally
		{
			pm.close();
		}
	}
	
}