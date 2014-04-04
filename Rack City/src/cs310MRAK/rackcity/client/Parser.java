package cs310MRAK.rackcity.client;

import java.util.ArrayList;
import java.util.List;

import cs310MRAK.rackcity.server.URLserviceImpl;

public class Parser {

	private List<BikeRack> rackList;
	private List<Crime> crimesList;

	private List<BikeRack> xmlRackList;
	private List<Crime> xmlCrimesList;

	public Parser() {

		this.rackList = new ArrayList<BikeRack>();
		this.crimesList = new ArrayList<Crime>();
		this.xmlRackList = new ArrayList<BikeRack>();
		this.xmlCrimesList = new ArrayList<Crime>();
	}



	public List<BikeRack> getRackList() {

		// this.parseBikeRacks();
		this.rackList = Rack_City.getRackData();

		return this.rackList;
	}

	public List<Crime> getCrimesList() {

		// this.parseCrimes();
		this.crimesList = Rack_City.getCrimeData();

		return this.crimesList;
	}

	public List<BikeRack> getXMLRackList() {

		// this.parseBikeRacks();
		//this.rackList = Rack_City.getRackData();

		return this.xmlRackList;
	}

	public List<Crime> getXMLCrimesList() {

		// this.parseCrimes();
		//this.crimesList = Rack_City.getCrimeData();

		return this.xmlCrimesList;
	}

	public void parseBikeRacks(StringBuffer stringOfRacks) {
		if(stringOfRacks.substring(1, 5).equals("?xml")) {
			stringOfRacks.delete(0, stringOfRacks.indexOf("<Records>"));

			if (stringOfRacks.substring(stringOfRacks.indexOf("Address=")).contains("pullthis")) {
				stringOfRacks.delete(0, (stringOfRacks.indexOf("</Record>")) + 9);
				this.xmlRackList.clear();
				this.xmlRackList = new ArrayList<BikeRack>();
			} 
			else {
				return;
			}
		}

		String address = null;
		String latlng = null;
		double rating = 0;
		int rackCount = 0;
		double crimeScore = 0;
		int numStolenBikes = 0;

		if (stringOfRacks.substring(0, 10).contains("<Record>")) {
			address = stringOfRacks.substring(stringOfRacks.indexOf("Address=") + 9, stringOfRacks.indexOf("LatLong") - 7);			
			latlng = stringOfRacks.substring(stringOfRacks.indexOf("LatLong=") + 9, stringOfRacks.indexOf("RackNumber") - 7);
			rackCount = Integer.parseInt(stringOfRacks.substring(stringOfRacks.indexOf("RackNumber=") + 12, stringOfRacks.indexOf("StolenNumber") - 7));
			numStolenBikes = Integer.parseInt(stringOfRacks.substring(stringOfRacks.indexOf("StolenNumber=") + 14, stringOfRacks.indexOf("CrimeScore") - 7));
			crimeScore = Double.parseDouble(stringOfRacks.substring(stringOfRacks.indexOf("CrimeScore=") + 12, stringOfRacks.indexOf("Rating") - 7));
			rating = Double.parseDouble(stringOfRacks.substring(stringOfRacks.indexOf("Rating=") + 8, stringOfRacks.indexOf("/>") - 5));

			BikeRack br = new BikeRack(address, latlng, rating, rackCount, crimeScore, numStolenBikes);
			this.xmlRackList.add(br);

			stringOfRacks.delete(0, stringOfRacks.indexOf("</Record>") + 9);

			parseBikeRacks(stringOfRacks);
		}

		return;
	}

	public void parseCrimes(StringBuffer stringOfCrimes) {
		if(stringOfCrimes.substring(1, 5).equals("?xml")) {
			stringOfCrimes.delete(0, stringOfCrimes.indexOf("<Records>"));

			stringOfCrimes.delete(0, (stringOfCrimes.indexOf("</Record>")) + 9);

			if (stringOfCrimes.substring(stringOfCrimes.indexOf("A=")).contains("pullthis")) {
				stringOfCrimes.delete(0, (stringOfCrimes.indexOf("</Record>")) + 9);
				this.xmlCrimesList.clear();
				this.xmlCrimesList = new ArrayList<Crime>();
			} 
			else {
				return;
			}
		}

		String year = null;
		String address = null;

		if (stringOfCrimes.substring(0, 10).contains("<Record>")) {
			year = stringOfCrimes.substring(stringOfCrimes.indexOf("A=") + 3, stringOfCrimes.indexOf("A=") + 7);
			address = stringOfCrimes.substring(stringOfCrimes.indexOf("B=") + 3, stringOfCrimes.indexOf("/>") - 5);

			Crime c = new Crime(year, address, "");
			this.xmlCrimesList.add(c);

			stringOfCrimes.delete(0, stringOfCrimes.indexOf("</Record>") + 9);

			if (xmlCrimesList.size() < 50) {
				parseCrimes(stringOfCrimes);
			}
		}

		return;
	}
}