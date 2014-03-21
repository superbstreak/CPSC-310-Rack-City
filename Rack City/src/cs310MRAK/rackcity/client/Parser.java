package cs310MRAK.rackcity.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.maps.client.geom.LatLng;

public class Parser {
	
	private List<BikeRack> rackList;
	private List<Crime> crimesList;
	
//	public Parser() {
//		this.rackList = new LinkedList<BikeRack>();
//		this.crimesList = new LinkedList<Crime>();
//		rackList.add(new BikeRack("", LatLng.newInstance(49.264453, -123.100939), 1.0, 2, 0, 1));
//		rackList.add(new BikeRack("", LatLng.newInstance(49.260227, -123.101088), 1.2, 2, 5, 10));
//		
//		crimesList.add(new Crime("", LatLng.newInstance(49.262509, -123.094469)));
//		crimesList.add(new Crime("", LatLng.newInstance(49.263049, -123.106903)));
//	}
//	
//	public List<BikeRack> getRackList() {
//		
//		
//		return this.rackList;
//	}
//	
//	public List<Crime> getCrimesList() {
//		
//		return this.crimesList;
//	}
//	
//	public void parseBikeRacks(String input) {
//		
//	}
//	
//	public void parseCrimes(String input) {
//		
//	}
//
//}

	
	public Parser() {
		
		this.rackList = new ArrayList<BikeRack>();
		this.crimesList = new ArrayList<Crime>();
		// rackList.add(new BikeRack("", LatLng.newInstance(49.264453, -123.100939), 1.0, 2, 0, 1));
		// rackList.add(new BikeRack("", LatLng.newInstance(49.260227, -123.101088), 1.2, 2, 5, 10));
		
		// crimesList.add(new Crime("", LatLng.newInstance(49.262509, -123.094469)));
		// crimesList.add(new Crime("", LatLng.newInstance(49.263049, -123.106903)));
		// rackList = Rack_City.getRackData();
		// crimesList = Rack_City.getCrimeData();
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
	
	public void parseBikeRacks() {
		
//		BikeRack rack1 = new BikeRack("134 East Abbott St, Vancouver, BC", LatLng.newInstance(49.284176,-123.106037), 3, 1, 1, 1);
//	    BikeRack rack2 = new BikeRack("216 East Abbott St, Vancouver, BC", LatLng.newInstance(49.283429,-123.106404), 4, 2, 1, 1);
//	    BikeRack rack3 = new BikeRack("1600 North Alberni St, Vancouver, BC", LatLng.newInstance(49.2902,-123.131333), 3, 1, 3, 1);
//	    BikeRack rack4 = new BikeRack("90 South Alexander, Vancouver, BC", LatLng.newInstance(49.283638,-123.102348), 5, 1, 3, 1);
//	    BikeRack rack5 = new BikeRack("55 North Alexander, Vancouver, BC", LatLng.newInstance(49.283613,-123.103), 2, 2, 1, 1);
//	    BikeRack rack6 = new BikeRack("200 North Alexander, Vancouver, BC", LatLng.newInstance(49.284083,-123.099423), 2, 2, 3, 1);
//	    BikeRack rack7 = new BikeRack("2083 West Alma, Vancouver, BC", LatLng.newInstance(49.267958,-123.185798), 4, 3, 3, 1);
//	    BikeRack rack8 = new BikeRack("5704 East Balsam, Vancouver, BC", LatLng.newInstance(49.234669,-123.161332), 2, 1, 3, 1);
//	    
//	    rackList.add(rack1);
//	    rackList.add(rack2);
//	    rackList.add(rack3);
//	    rackList.add(rack4);
//	    rackList.add(rack5);
//	    rackList.add(rack6);
//	    rackList.add(rack7);
//	    rackList.add(rack8);
	    
	}
	
	public void parseCrimes() {
		
	    Crime crime1 = new Crime("305 North Alexander St, Vancouver, BC", LatLng.newInstance(49.284449,-123.097679));
	    Crime crime2 = new Crime("475 North Alexander St, Vancouver, BC", LatLng.newInstance(49.284113,-123.093897));
		
	    crimesList.add(crime1);
	    crimesList.add(crime2);
	    
	}

}