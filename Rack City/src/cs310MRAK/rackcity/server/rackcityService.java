package cs310MRAK.rackcity.server;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class rackcityService extends HttpServlet {

	// Okay wait, this method is accessing the datastore using the low-level Datastore API. 
	// I think using the JDO is simpler in the long-run.
	// 1. have the StockWatcher Tutorial to reference as guidance with a JDO implementation
	// 2. it's higher level stuff. Look at how easily we can view the Datastore this way:
		// http://appengine.google.com   -> Datastore Viewer
	
	// But switching to JDO wouldn't mean we've wasted our time watching the youtube video: http://www.youtube.com/watch?v=_P1wcF_XBlE
	// I think it was good to watch because it mainly described the concept of the Datastore, which is still useful to know.
public Key rackKEY;
public Key crimeKEY;
	
	public void doGet (HttpServletRequest request, HttpServletResponse result) throws IOException
	{
		// tutorial
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		// use this to create different data
		
		//================================ UPLOAD ===================================
		
		// loop this for entry
		newRack("400 N 24th AVEujkm ", "49.27417,-123.13098", 5, 0 , 0, ds); //create new entry]
		System.out.println(rackKEY);
		
		//------- DRAFT ---------
		// entiy with attrs, group these
		//Entity e = new Entity("BikeRack");					// identifier (have uid)
		//e.setProperty("Address", "400 N 23RD AVE");			// attribute values
		//e.setProperty("LatLng", "49.27417,-123.13098");		// attribute values	
		//e.setProperty("NumberofRack", 3);					// attribute values
		
		//upload entity
		//ds.put(e);
		
		
		//=============================== DOWNLOAD ==================================
		Key k = rackKEY;
		
		/*
		try 													// try to get entity with this key from server
		{
			Entity e = ds.get(k);
			
		} 
		catch (EntityNotFoundException e) 						// failed, error message! SHOULD NOT HAPPEN IN FINAL PRODUCT!
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		//===========================================================================
		
		result.setContentType("text/plain");
		result.getWriter().println("Test Print Values");
	}
	
	
	//====================== NOT SURE
	// Retrieve racks from server
	public void getRacks (Key k, DatastoreService db)
	{
		
		try 
		{
			Entity e = db.get(k);
			String addr = (String) e.getProperty("Address");
			String LL = (String) e.getProperty("LatLng");
			int nR = (int) e.getProperty("NoR");
			int avgR = (int) e.getProperty("Rating");
			int avgC = (int) e.getProperty("CrimeScore");
			
		} 
		catch (EntityNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void getCrime (Key k, DatastoreService db)
	{
		
		try 
		{
			Entity e = db.get(k);
			String addr = (String) e.getProperty("Address");
			String LL = (String) e.getProperty("LatLng");
			int nR = (int) e.getProperty("NoC");
			
		} 
		catch (EntityNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//push new Crime data onto server
	public void newCrime (String address, String LL, int numCrime, DatastoreService db)
	{
		Entity e = new Entity("Crime");
		crimeKEY = e.getKey();
		e.setProperty("Address", address);
		e.setProperty("LatLng", LL);
		e.setProperty("NoC", numCrime);
		db.put(e);
	}
	
	//push new bikeData onto server
	public void newRack (String address, String LL, int Racknum, int AvgRating, int AvgCrime, DatastoreService db)
	{
		Entity e = new Entity("Rack");
		rackKEY = e.getKey();
		e.setProperty("Address", address);
		e.setProperty("LatLng", LL);
		e.setProperty("NoR", Racknum);
		e.setProperty("Rating", AvgRating);
		e.setProperty("CrimeScore", AvgCrime);
		db.put(e);
	}
	
}
