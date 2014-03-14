package cs310MRAK.rackcity.server;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import cs310MRAK.rackcity.client.BikeRack;
import cs310MRAK.rackcity.client.RackFavouritesService;

public class RackFavouritesServiceImpl extends RemoteServiceServlet implements RackFavouritesService{

	// The messages logged by "LOG" are viewable when we inspect our RackCity application in appengine.google.com
		private static final Logger LOG = Logger.getLogger(StolenBikesServiceImpl.class.getName());
		
			// This is related to line 6 in src/META-INF/jdoconfig.xml
			// We just need to do this to use JDO.
		private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
		


}
