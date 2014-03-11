package cs310MRAK.rackcity.client;

import com.google.appengine.api.search.GeoPoint;

/**
 * 
 * @author aprad1124
 * 
 * BikeRack object class
 *
 */
public class BikeRack {
	
	public String address;
	public GeoPoint coordinate;
	public double rating;
	public int rackCount;
	public int crimeScore;
	
	public BikeRack(String address, GeoPoint coordinate,
			double rating, int rackCount, int crimeScore) {
		this.address = address;
		this.coordinate = coordinate;
		this.rating = rating;
		this.rackCount = rackCount;
		this.crimeScore = crimeScore;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public GeoPoint getCoordinate() {
		return this.coordinate;
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
	
	public void setRating(double newRating) {
		this.rating = newRating;
	}
	
	public void setCrimeScore(int newCrimeScore) {
		this.crimeScore = newCrimeScore;
	}

}
