package cs310MRAK.rackcity.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import cs310MRAK.rackcity.client.rackService;
import cs310MRAK.rackcity.shared.BikeRackTimeHit;
import cs310MRAK.rackcity.shared.Rack;



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
	public String getRackTimeHits(String p) {
		PersistenceManager pm = getPersistenceManager();
		String rackTimeHits = "";
		try
		{
			String query = "select from " + BikeRackTimeHit.class.getName() + "where pos ==  p";
			@SuppressWarnings("unchecked")
			BikeRackTimeHit brth = (BikeRackTimeHit) pm.newQuery(query).execute();
			rackTimeHits = "[ "+brth.getInterval0to1()+", "
									  +brth.getInterval1to2()+", "
									  +brth.getInterval2to3()+", "
									  +brth.getInterval3to4()+", "
									  +brth.getInterval4to5()+", "
									  +brth.getInterval5to6()+", "
									  +brth.getInterval6to7()+", "
									  +brth.getInterval7to8()+", "
									  +brth.getInterval8to9()+", "
									  +brth.getInterval9to10()+", "
									  +brth.getInterval10to11()+", "
									  +brth.getInterval11to12()+", "
									  +brth.getInterval12to13()+", "
									  +brth.getInterval13to14()+", "
									  +brth.getInterval14to15()+", "
									  +brth.getInterval15to16()+", "
									  +brth.getInterval16to17()+", "
									  +brth.getInterval17to18()+", "
									  +brth.getInterval18to19()+", "
									  +brth.getInterval19to20()+", "
									  +brth.getInterval20to21()+", "
									  +brth.getInterval21to22()+", "
									  +brth.getInterval22to23()+", "
									  +brth.getInterval23to24()+" ]";
									  
		}
		finally
		{
			pm.close();
		}
		return rackTimeHits;
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
			pm.makePersistent(new BikeRackTimeHit(pos, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0));
		}
		finally
		{
			pm.close();
		}
	}
	
	@Override
	public void updateBikeRackTimeHit(String pos, int timeHits, String whichOne) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try
		{
			BikeRackTimeHit brth = pm.getObjectById(BikeRackTimeHit.class, pos);
			if(whichOne.equals("0to1")) brth.setInterval0to1(timeHits);
			if(whichOne.equals("1to2")) brth.setInterval1to2(timeHits);
			if(whichOne.equals("2to3")) brth.setInterval2to3(timeHits);
			if(whichOne.equals("3to4")) brth.setInterval3to4(timeHits);
			if(whichOne.equals("4to5")) brth.setInterval4to5(timeHits);
			if(whichOne.equals("5to6")) brth.setInterval5to6(timeHits);
			if(whichOne.equals("6to7")) brth.setInterval6to7(timeHits);
			if(whichOne.equals("7to8")) brth.setInterval7to8(timeHits);
			if(whichOne.equals("8to9")) brth.setInterval8to9(timeHits);
			if(whichOne.equals("9to10")) brth.setInterval9to10(timeHits);
			if(whichOne.equals("10to11")) brth.setInterval10to11(timeHits);
			if(whichOne.equals("11to12")) brth.setInterval11to12(timeHits);
			if(whichOne.equals("12to13")) brth.setInterval12to13(timeHits);
			if(whichOne.equals("13to14")) brth.setInterval13to14(timeHits);
			if(whichOne.equals("14to15")) brth.setInterval14to15(timeHits);
			if(whichOne.equals("15to16")) brth.setInterval15to16(timeHits);
			if(whichOne.equals("16to17")) brth.setInterval16to17(timeHits);
			if(whichOne.equals("17to18")) brth.setInterval17to18(timeHits);
			if(whichOne.equals("18to19")) brth.setInterval18to19(timeHits);
			if(whichOne.equals("19to20")) brth.setInterval19to20(timeHits);
			if(whichOne.equals("20to21")) brth.setInterval20to21(timeHits);
			if(whichOne.equals("21to22")) brth.setInterval21to22(timeHits);
			if(whichOne.equals("22to23")) brth.setInterval22to23(timeHits);
			if(whichOne.equals("23to24")) brth.setInterval23to24(timeHits);

		}
		finally
		{
			pm.close();
		}
	}
	

	
	
	
	
	
	
	
	
	

}
