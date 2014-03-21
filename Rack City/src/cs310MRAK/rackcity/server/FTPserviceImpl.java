package cs310MRAK.rackcity.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cs310MRAK.rackcity.client.FTPservice;

public class FTPserviceImpl extends RemoteServiceServlet implements FTPservice{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7068432272258660457L;

	@Override
	public void adminConnection() {

		 URL url = null;
			try {
				url = new URL("ftp://webftp.vancouver.ca/opendata/bike_rack/2012BikeRackData.csv");
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
		        
		        // ==================================    
		        // *** CSV TO XML CONVERTER ***
		        // ==================================
		        
//		        BufferedReader reader = new BufferedReader(new InputStreamReader(
//		        		Csv2Xml.class.getResourceAsStream("test.csv")));
//		        StringBuilder xml = new StringBuilder();
//		        String lineBreak = System.getProperty("line.separator");
//		        String line = null;
//		        List<String> headers = new ArrayList<String>();
//		        boolean isHeader = true;
//		        int count = 0;
//		        int entryCount = 1;
//		        xml.append("<root>");
//		        xml.append(lineBreak);
//		        while ((line = reader.readLine()) != null) {
//		        	StringTokenizer tokenizer = new StringTokenizer(line, ",");
//		        	if (isHeader) {
//		        		isHeader = false;
//		        		while (tokenizer.hasMoreTokens()) {
//		        			headers.add(tokenizer.nextToken());
//		        		}
//		        	} else {
//		        		count = 0;
//		        		xml.append("\t<entry id=\"");
//		        		xml.append(entryCount);
//		        		xml.append("\">");
//		        		xml.append(lineBreak);
//		        		while (tokenizer.hasMoreTokens()) {
//		        			xml.append("\t\t<");
//		        			xml.append(headers.get(count));
//		        			xml.append(">");
//		        			xml.append(tokenizer.nextToken());
//		        			xml.append("</");
//		        			xml.append(headers.get(count));
//		        			xml.append(">");
//		        			xml.append(lineBreak);
//		        			count++;
//		        		}
//		        		xml.append("\t</entry>");
//		        		xml.append(lineBreak);
//		        		entryCount++;
//		        	}
//		        }
//		        xml.append("</root>");
//		        System.out.println(xml.toString());
			      
	}

}
