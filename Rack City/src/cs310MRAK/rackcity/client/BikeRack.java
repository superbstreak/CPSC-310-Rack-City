package cs310MRAK.rackcity.client;

import com.google.gwt.maps.client.geom.LatLng;


/**
 * 
 * @author aprad1124
 * 
 * BikeRack object class
 *
 */
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class BikeRack {
	@Persistent
	public String address;
	@PrimaryKey
	public LatLng coordinate;
	@Persistent
	public double rating;
	@Persistent
	public int rackCount;
	@Persistent
	public int crimeScore;
	private int numberStolenBikes;

	
	// TODO: will need to add User object to the constructor as well
	// TODO: make @persistent
	
	public BikeRack(String address, LatLng coordinate, double rating,
			int rackCount, int crimeScore, int numberStolenBikes){
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
	}
	

}
