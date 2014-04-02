package cs310MRAK.rackcity.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import cs310MRAK.rackcity.client.RackFavouritesService;
import cs310MRAK.rackcity.shared.FavoriteRack;

public class RackFavouritesServiceImpl extends RemoteServiceServlet implements RackFavouritesService{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -932652595470237815L;

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(RackFavouritesServiceImpl.class.getName());
	 
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
		
	private PersistenceManager getPersistenceManager() {
		// TODO Auto-generated method stub
		return PMF.getPersistenceManager();
	}


	@Override
	public ArrayList<String[]> getFavorite(String uid) 
	{
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		ArrayList<String[]> fin = new ArrayList<String[]>();
		try
		{	
			String query = "select from " + FavoriteRack.class.getName();
			@SuppressWarnings("unchecked")
			List<FavoriteRack> FavoriteRacks = (List<FavoriteRack>) pm.newQuery(query).execute();
			for (FavoriteRack r: FavoriteRacks)
			{
				// ====== Basic Info ========
				if (r.getUid().equals(uid))
				{
					String addr = r.getAddress();
					String LL = r.getPosition();
					String[] temp = {LL, addr};
					fin.add(temp);
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
	public void removeFavorite(String uid, String cooridinate) 
	{
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " +FavoriteRack.class.getName();
			@SuppressWarnings("unchecked")
			List<FavoriteRack> FavoriteRacks = (List<FavoriteRack>) pm.newQuery(query).execute();
			for (FavoriteRack r: FavoriteRacks)
			{
				if (r.getUid().equals(uid) && r.getPosition().equals(cooridinate))
				{
					pm.deletePersistent(r);
				}
			}
		}
		finally
		{
			pm.close();
		}
	}


	@Override
	public void addToFavorite(String uid, String address, String cooridinate) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try
		{
			pm.makePersistent(new FavoriteRack(uid, address, cooridinate));
		}
		finally
		{
			pm.close();
		}
	}


}
