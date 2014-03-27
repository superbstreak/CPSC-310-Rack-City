package cs310MRAK.rackcity.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cs310MRAK.rackcity.client.URLservice;

public class URLserviceImpl extends RemoteServiceServlet implements URLservice{

	/**
	 *
	 */
	private static final long serialVersionUID = 7068432272258660457L;

	@Override
	public void adminConnection() {

		URL url = null;
		try {
			url = new URL("https://dl.dropboxusercontent.com/u/280882377/Book1.xml");
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		URLConnection con = null;
		try {
			con = url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String inputLine;
		try {
			while ((inputLine = in.readLine()) != null)
				System.out.println(inputLine);
		} catch (IOException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println("hey what's up");
	}
}