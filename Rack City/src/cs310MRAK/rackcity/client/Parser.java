package cs310MRAK.rackcity.client;

import java.util.ArrayList;
import java.util.List;

public class Parser {

	private List<BikeRack> rackList;
	private List<Crime> crimesList;

	public Parser() {

		this.rackList = new ArrayList<BikeRack>();
		this.crimesList = new ArrayList<Crime>();
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

	}

	public void parseCrimes() {

	}

}