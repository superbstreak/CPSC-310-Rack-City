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

	public static StringBuffer sb = new StringBuffer();

	@Override
	public void adminConnection(String urlz) {

		URL url = null;
		try {
			url = new URL(urlz);
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
		URLConnection con = null;
		try {
			con = url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String inputLine;
		final String link = urlz;
		try {
			sb.setLength(0);
			while ((inputLine = in.readLine()) != null) {
				//System.out.println(inputLine);
				sb.append(inputLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//System.out.println("hey what's up");
	}
}