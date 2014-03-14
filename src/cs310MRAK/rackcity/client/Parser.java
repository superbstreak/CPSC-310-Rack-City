package cs310MRAK.rackcity.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Parser {
	
	public List rackList;
	public List crimesList;
	
	public Parser() {
		this.rackList = new ArrayList<BikeRack>();
		this.crimesList = new ArrayList<Crime>();
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
