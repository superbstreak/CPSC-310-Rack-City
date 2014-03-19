package cs310MRAK.rackcity.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cs310MRAK.rackcity.client.crimeService;



public class crimeServiceImpl extends RemoteServiceServlet implements crimeService{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1261023406071941548L;

	/**
	 *  make sure pm is singleton
	 */
	

	//private static final long serialVersionUID = 1L;
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
			long deleteCount = 0;	
			String query = "select from " + Crime.class.getName();
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

}
