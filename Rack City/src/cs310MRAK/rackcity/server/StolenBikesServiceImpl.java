package cs310MRAK.rackcity.server;

import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.logging.Level;
import java.util.logging.Logger;

import cs310MRAK.rackcity.client.Crime;
import cs310MRAK.rackcity.client.StolenBikesService;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

public class StolenBikesServiceImpl extends RemoteServiceServlet implements StolenBikesService{
	
		// The messages logged by "LOG" are viewable when we inspect our RackCity application in appengine.google.com
	private static final Logger LOG = Logger.getLogger(StolenBikesServiceImpl.class.getName());
	
		// This is related to line 6 in src/META-INF/jdoconfig.xml
		// We just need to do this to use JDO.
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	
	// later we would add a checkLoggedIn(); [ where I put the [*&$] symbol ]
	// and add the NotLoggedInException 
	public void addStolenBike(String rackID, LatLng coordinate) {
		// [*&$]
		PersistenceManager pm = getPersistenceManager();
		
	    pm.makePersistent(new Crime(rackID, coordinate));
		
		pm.close();
	}

	
	// because singleton. 
	private PersistenceManager getPersistenceManager() {
		// TODO Auto-generated method stub
		return PMF.getPersistenceManager();
	}

}
