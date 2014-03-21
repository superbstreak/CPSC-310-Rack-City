package cs310MRAK.rackcity.client;

import com.google.gwt.maps.client.geom.LatLng;



/**
 * 
 * @author aprad1124
 * 
 * BikeRack object class
 *
 */

public class BikeRack {
	
	public String address;
	public String coordinate;
	public double rating;
	public int rackCount;
	public int crimeScore;
	private int numberStolenBikes;


	// TODO: will need to add User object to the constructor as well
	// TODO: make @persistent

	public BikeRack(String address, String coordinate, double rating, int rackCount, int crimeScore, int numberStolenBikes){
		this.address = address;
		this.coordinate = coordinate;
		this.rating = rating;
		this.rackCount = rackCount;
		this.crimeScore = crimeScore;
		this.numberStolenBikes = numberStolenBikes;
	}

	public String getAddress() {
		return this.address;
	}

	public LatLng getCoordinate() {
		String c = this.coordinate;
		// ====== Process LL ========
		String[] results = c.split( "," );
		String Lat = results[0];
		String Lon = results[1];
		Lat = Lat.substring(1);
		Lon = Lon.substring(0, Lon.length() - 1);
		double LatVal = Double.parseDouble(Lat);
		double LonVal = Double.parseDouble(Lon);
		LatLng pos = LatLng.newInstance(LatVal, LonVal);
		return pos;
	}

	public double getRating() {
		return this.rating;
	}

	public int getRackCount() {
		return this.rackCount;
	}

	public int getCrimeScore() {
		return this.crimeScore;
	}

	public int getNumberStolenBikes(){
		return numberStolenBikes;
	}

	public void setRating(double newRating) {
		this.rating = newRating;
	}

	public void setCrimeScore(int newCrimeScore) {
		this.crimeScore = newCrimeScore;
	}

	public void addStolenBike() {
		this.numberStolenBikes++;

		int currNumStolenBikes = this.numberStolenBikes;
		int currCrimeScore = this.getCrimeScore();
		int mod = 10;

		if (currNumStolenBikes % mod == 0
				&& currCrimeScore < 5) {
			this.setCrimeScore(currCrimeScore + 1);
		}
	}


}
