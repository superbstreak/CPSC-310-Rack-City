package cs310MRAK.rackcity.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cs310MRAK.rackcity.client.rackService;



public class rackServiceImpl extends RemoteServiceServlet implements rackService{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1205162297094536429L;

	//private static final long serialVersionUID = 1L;
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
	
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

}
