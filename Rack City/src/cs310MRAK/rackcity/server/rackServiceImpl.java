package cs310MRAK.rackcity.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import cs310MRAK.rackcity.client.rackService;
import cs310MRAK.rackcity.shared.BikeRackTimeHits;
import cs310MRAK.rackcity.shared.UserSearchHistoryInstance;



public class rackServiceImpl extends RemoteServiceServlet implements rackService{
	
	/**
	 *  make sure pm is singleton
	 */
	private static final long serialVersionUID = 1205162297094536429L;

	//private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(rackServiceImpl.class.getName());
	 
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	@Override
	public void addRack(String addr, String p, int rnum, int stolen, double cs,
			double rate) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try
		{
			pm.makePersistent(new Rack(addr, p, rnum, stolen, cs, rate));
		}
		finally
		{
			pm.close();
		}
	}

	@Override
	public void updateCS(String p, double score) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try
		{
			
			Rack r = pm.getObjectById(Rack.class, p);
			r.setCS(score);
			/*String query = "select from " + Rack.class.getName();
			List<Rack> Racks = (List<Rack>) pm.newQuery(query).execute();
			for (Rack r: Racks)
			{
				if (r.getLL().equals(p))
				{
					r.setCS(score);
					pm.makePersistent(r);
				}
			}*/
		}
		finally
		{
			pm.close();
		}
	}

	@Override
	public void updateStolen(String p, int steal) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try
		{
			Rack r = pm.getObjectById(Rack.class, p);
			r.setStolen(steal);
		}
		finally
		{
			pm.close();
		}
	}

	@Override
	public void updateRate(String p, double rating) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try
		{
			Rack r = pm.getObjectById(Rack.class, p);
			r.setRating(rating);
		}
		finally
		{
			pm.close();
		}
	}

	@Override
	public String getAddress(String p) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		Rack fin = null;
		try
		{
			String query = "select from " + Rack.class.getName();
			@SuppressWarnings("unchecked")
			List<Rack> Racks = (List<Rack>) pm.newQuery(query).execute();
			for (Rack r: Racks)
			{
				if (r.getLL().equals(p))
				{
					fin = r;
				}
			}
		}
		finally
		{
			pm.close();
		}
		return fin.getAddr();
	}

	@Override
	public int getRackNum(String p) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		Rack fin = null;
		try
		{
			String query = "select from " + Rack.class.getName();
			@SuppressWarnings("unchecked")
			List<Rack> Racks = (List<Rack>) pm.newQuery(query).execute();
			for (Rack r: Racks)
			{
				if (r.getLL().equals(p))
				{
					fin = r;
				}
			}
		}
		finally
		{
			pm.close();
		}
		return fin.getRnum();
	}

	@Override
	public int getStolen(String p) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		Rack fin = null;
		try
		{
			String query = "select from " + Rack.class.getName();
			@SuppressWarnings("unchecked")
			List<Rack> Racks = (List<Rack>) pm.newQuery(query).execute();
			for (Rack r: Racks)
			{
				if (r.getLL().equals(p))
				{
					fin = r;
				}
			}
		}
		finally
		{
			pm.close();
		}
		return fin.getStolen();
	}

	@Override
	public double getCS(String p) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		Rack fin = null;
		try
		{
			String query = "select from " + Rack.class.getName();
			@SuppressWarnings("unchecked")
			List<Rack> Racks = (List<Rack>) pm.newQuery(query).execute();
			for (Rack r: Racks)
			{
				if (r.getLL().equals(p))
				{
					fin = r;
				}
			}
		}
		finally
		{
			pm.close();
		}
		return fin.getCS();
	}

	@Override
	public double getRate(String p) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		Rack fin = null;
		try
		{
			String query = "select from " + Rack.class.getName();
			@SuppressWarnings("unchecked")
			List<Rack> Racks = (List<Rack>) pm.newQuery(query).execute();
			for (Rack r: Racks)
			{
				if (r.getLL().equals(p))
				{
					fin = r;
				}
			}
		}
		finally
		{
			pm.close();
		}
		return fin.getRating();
	}
	
	@Override
	public void removeRack(String pos) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try {
			@SuppressWarnings("unused")
			long deleteCount = 0;	
			String query = "select from " + Rack.class.getName();
			@SuppressWarnings("unchecked")
			List<Rack> Racks = (List<Rack>) pm.newQuery(query).execute();
			for (Rack r: Racks)
			{
				if (r.getLL().equals(pos))
				{
					deleteCount++;
					pm.deletePersistent(r);
				}
			}
			//if (deleteCount != 1) { LOG.log(Level.WARNING, "removeRack deleted " + deleteCount + " Racks");}
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
	public ArrayList<String[]> getRacks() {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		ArrayList<String[]> fin = new ArrayList<String[]>();
		try
		{	
			String query = "select from " + Rack.class.getName();
			@SuppressWarnings("unchecked")
			List<Rack> Racks = (List<Rack>) pm.newQuery(query).execute();
			for (Rack r: Racks)
			{
				// ====== Basic Info ========
				String addr = r.getAddr();
				String LL = r.getLL();
				double rate = r.getRating();
				int rackN = r.getRnum();
				double cs = r.getCS();
				int stolenN = r.getStolen();				
				
				String[] temp = {addr, LL, String.valueOf(rate), String.valueOf(rackN), String.valueOf(cs), String.valueOf(stolenN)};
				fin.add(temp);
			}
			
		}
		finally
		{
			pm.close();
		}
		return fin;
	}

	
	
	@Override
	public void addBikeRackTimeHit(String pos) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try
		{
			pm.makePersistent(new BikeRackTimeHits(pos, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0));
		}
		finally
		{
			pm.close();
		}
	}
	

	
	
	
	
	
	
	
	
	

}
