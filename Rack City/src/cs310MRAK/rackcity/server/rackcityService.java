package cs310MRAK.rackcity.server;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;

public class rackcityService extends HttpServlet {
	
	public void doGet (HttpServletRequest request, HttpServletResponse result) throws IOException
	{
		// tutorial
		Entity e = new Entity("BikeRack");
		e.setProperty("Address", "400 N 23RD AVE");
		e.setProperty("LatLng", "49.27417,-123.13098");
		e.setProperty("NumberofRack", 3);
		// entiy with attrs
		
		result.setContentType("text/plain");
		result.getWriter().println("Test Print Values");
		

	}
	
}
