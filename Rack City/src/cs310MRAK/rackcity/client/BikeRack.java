package cs310MRAK.rackcity.client;


/**
 * 
 * @author aprad1124
 * 
 * BikeRack object class
 *
 */


public class BikeRack {
	
	public String address;
	public double rating;
	public int rackCount;
	public int crimeScore;
	
	// TODO: will need to add User object to the constructor as well
	// TODO: make @persistent
	public BikeRack(String address){//, GeoPoint coordinate,
		this.address = address;
	
	}
	
	public String getAddress() {
		return this.address;
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
