package cs310MRAK.rackcity.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import cs310MRAK.rackcity.client.crimeService;
import cs310MRAK.rackcity.shared.Crime;



public class crimeServiceImpl extends RemoteServiceServlet implements crimeService{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1261023406071941548L;

	/**
	 *  make sure pm is singleton
	 */
	

	//private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(crimeServiceImpl.class.getName());
	 
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	
	
	
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}




	@Override
	public void addCrime(int year, String pos) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try
		{
			pm.makePersistent(new Crime(year, pos));
		}
		finally
		{
			pm.close();
		}
	}




	@Override
	public int getYear (String pos) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		Crime fin = null;
		try
		{
			String query = "select from " + Crime.class.getName();
			@SuppressWarnings("unchecked")
			List<Crime> Crimes = (List<Crime>) pm.newQuery(query).execute();
			for (Crime r: Crimes)
			{
				if (r.getLL().equals(pos))
				{
					fin = r;
				}
			}
		}
		finally
		{
			pm.close();
		}
		return fin.getYear();
	}




	@Override
	public void removeCrime(String pos) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		try {
			@SuppressWarnings("unused")
			long deleteCount = 0;	
			String query = "select from " + Crime.class.getName();
			@SuppressWarnings("unchecked")
			List<Crime> Crimes = (List<Crime>) pm.newQuery(query).execute();
			for (Crime r: Crimes)
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




	@Override
	public ArrayList<String[]> getCrimes() {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPersistenceManager();
		ArrayList<String[]> fin = new ArrayList<String[]>();
		try
		{	
			String query = "select from " + Crime.class.getName();
			@SuppressWarnings("unchecked")
			List<Crime> Crimes = (List<Crime>) pm.newQuery(query).execute();
			for (Crime c: Crimes)
			{
				// ====== Basic Info ========
				String addr = "Record Year: "+c.getYear();
				String LL = c.getLL();			
				
				String[] temp = {addr, LL};
				fin.add(temp);
			}
			
		}
		finally
		{
			pm.close();
		}
		return fin;
	}

}
