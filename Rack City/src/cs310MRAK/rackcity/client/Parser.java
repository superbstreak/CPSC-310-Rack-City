package cs310MRAK.rackcity.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.maps.client.geom.LatLng;

public class Parser {
	
	private List<BikeRack> rackList;
	private List<Crime> crimesList;
	
	public Parser() {
		this.rackList = new LinkedList<BikeRack>();
		this.crimesList = new LinkedList<Crime>();
		rackList.add(new BikeRack("", LatLng.newInstance(49.264453, -123.100939), 1.0, 2, 0, 1));
		rackList.add(new BikeRack("", LatLng.newInstance(49.260227, -123.101088), 1.2, 2, 5, 10));
		
		crimesList.add(new Crime("", LatLng.newInstance(49.262509, -123.094469)));
		crimesList.add(new Crime("", LatLng.newInstance(49.263049, -123.106903)));
	}
	
	public List<BikeRack> getRackList() {
		
		
		return this.rackList;
	}
	
	public List<Crime> getCrimesList() {
		
		return this.crimesList;
	}
	
	public void parseBikeRacks(String input) {
		
	}
	
	public void parseCrimes(String input) {
		
	}

}
