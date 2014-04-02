package cs310MRAK.rackcity.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cs310MRAK.rackcity.client.userService;
import cs310MRAK.rackcity.shared.UserInfo;
import cs310MRAK.rackcity.shared.UserSearchHistoryInstance;
import cs310MRAK.rackcity.shared.rackStarRatings;

public class userServiceImpl  extends RemoteServiceServlet implements userService{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8733888600485828222L;
			 
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
			@SuppressWarnings("unused")
			long deleteCount = 0;	
			String query = "select from " + UserInfo.class.getName();
			@SuppressWarnings("unchecked")
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
			@SuppressWarnings("unchecked")
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
	public void addUserSearchHistoryInstance(String key, String userID, String searchAddress, int radius, int crimeScore, int rate) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try
		{
			pm.makePersistent(new UserSearchHistoryInstance(key, userID, searchAddress, radius, crimeScore, rate));
		}
		finally
		{
			pm.close();
		}
	}

	@Override
	public ArrayList<UserSearchHistoryInstance> getHistory(String uid) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		ArrayList<UserSearchHistoryInstance> fin = new ArrayList<UserSearchHistoryInstance>();
		try
		{
			String query = "select from " +  UserSearchHistoryInstance.class.getName();
			@SuppressWarnings("unchecked")
			List<UserSearchHistoryInstance> UserSearchHistoryInstances = (List<UserSearchHistoryInstance>) pm.newQuery(query).execute();
			for (UserSearchHistoryInstance r: UserSearchHistoryInstances)
			{
				if (r.getUserID().equals(uid))
				{
					fin.add(r);
				}
			}
		}
		finally
		{
			pm.close();
		}
		return fin;
	}

	@Override
	public void addStarRating(String userID, String addr, String pos, int starratings) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try
		{
			pm.makePersistent(new rackStarRatings(userID, addr, pos, starratings));
		}
		finally
		{
			pm.close();
		}
		
	}

	@Override
	public ArrayList<rackStarRatings> getStarRating(String userID, String addr, String pos, int type) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		ArrayList<rackStarRatings> fin = new ArrayList<rackStarRatings>();
		try
		{
			String query = "select from " +  rackStarRatings.class.getName();
			@SuppressWarnings("unchecked")
			List<rackStarRatings> StarRatings = (List<rackStarRatings>) pm.newQuery(query).execute();
			String primarykey = addr+userID;
			if (type == 1)				// get user rating on all position
			{
				for (rackStarRatings r: StarRatings)
				{
					if (r.getUid().equals(userID))
					{
						fin.add(r);
					}
				}
			}
			else if (type == 2)			// get all ratings on this address
			{
				for (rackStarRatings r: StarRatings)
				{
					if (r.getAddress().equals(addr))
					{
						fin.add(r);
					}
				}
			}
			else if (type == 3)			// get user rating on this postion
			{
				for (rackStarRatings r: StarRatings)
				{
					if (r.getUidAddress().equals(primarykey))
					{
						fin.add(r);
					}
				}
			}
		}
		finally
		{
			pm.close();
		}
		return fin;
	}
	
}
