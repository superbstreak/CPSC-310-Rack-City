package cs310MRAK.rackcity.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.maps.client.geom.LatLng;


public class Filter {

	private Parser parser;

	List<BikeRack> unfilteredList;
	List<BikeRack> filteredList;

	List<Crime> unfilteredCrimeList;
	List<Crime> filteredCrimeList;

	public Filter() {
		this.parser = new Parser();
		this.unfilteredList = new ArrayList<BikeRack>();
		this.filteredList = new ArrayList<BikeRack>();

	}

	/**
	 * 
	 * Filters the list of bike racks according to the distance
	 * between the user's location and the bike racks
	 * 	- if the distance from the user to a bike rack is less than
	 * 	  or equal to the radius specified by the user, the bike
	 * 	  rack is added to a new list of bike racks that is then returned
	 * @param radius
	 * @param myLocation
	 * @return
	 */
	private List<BikeRack> filteredDistanceList(double radius, LatLng myLocation,
			List<BikeRack> list) {

		for(BikeRack rack : list) {
			double distanceX = (rack.getCoordinate().getLongitude()
					- myLocation.getLongitude());
			double distanceY = (rack.getCoordinate().getLatitude()
					- myLocation.getLatitude());
			double distance = Math.sqrt((distanceX)*(distanceX)
					+ (distanceY)*(distanceY));

			if (distance <= radius) {
				this.filteredList.add(rack);
			}
		}

		return this.filteredList;
	}
	
	private List<Crime> filteredCrimeDistanceList(double radius, LatLng myLocation,
			List<Crime> list) {
		
		for (Crime crime : list) {
			double distanceX = (crime.getCoordinate().getLongitude()
					- myLocation.getLongitude());
			double distanceY = (crime.getCoordinate().getLatitude()
					- myLocation.getLatitude());
			double distance = Math.sqrt((distanceX)*(distanceX)
					+ (distanceY)*(distanceY));
			
			if (distance <= radius) {
				this.filteredCrimeList.add(crime);
			}
		}
		
		return this.filteredCrimeList;
	}

	/**
	 * 
	 * Filters the list of bike racks according to rating
	 * 	- returns a list of racks with rating greater than
	 * 	  or equivalent to the rating demanded by the user
	 * @param rating
	 * @return
	 */
	private List<BikeRack> filteredRatingList(double rating,
			List<BikeRack> list) {

		for(BikeRack rack : list) {			
			if (rack.getRating() >= rating) {
				this.filteredList.add(rack);
			}
		}

		return this.filteredList;
	}

	/**
	 * 
	 * Filters the list of bike racks according to the
	 * crime score (lower is better)
	 * 	- returns a list of racks with crimeScore less
	 * 	  than or equal to the crimeScore demanded by
	 * 	  the user 
	 * @param crimeScore
	 * @return
	 */
	private List<BikeRack> filteredCrimeScoreList(int crimeScore,
			List<BikeRack> list) {

		for(BikeRack rack : list) {
			if (rack.getCrimeScore() <= crimeScore) {
				this.filteredList.add(rack);
			}
		}

		return this.filteredList;
	}
	
	/**
	 * 
	 * @param myLocation
	 * @param radius
	 * @param rating
	 * @param crimeScore
	 * @return
	 */
	public List<BikeRack> getFilteredRackList(LatLng myLocation, double radius,
			double rating, int crimeScore) {
		
		this.filteredList = this.filteredDistanceList(radius, myLocation, this.unfilteredList());
		
		this.filteredList = this.filteredRatingList(rating, this.filteredList);
		
		this.filteredList = this.filteredCrimeScoreList(crimeScore, this.filteredList);
		
		return this.filteredList;
		
	}
	
	/**
	 * 
	 * @param myLocation
	 * @param radius
	 * @return
	 */
	public List<Crime> getFilteredCrimeList(LatLng myLocation, double radius) {
		
		this.filteredCrimeList = this.filteredCrimeDistanceList(radius, myLocation,
				this.unfilteredCrimeList);
		
		return this.filteredCrimeList;
	}

	/**
	 * 
	 * Returns an unfiltered list from the parse. The
	 * purpose of this method is to eliminate the dependency
	 * between the RackCity class and the Parser class
	 * @return
	 */
	private List<BikeRack> unfilteredList() {
		this.unfilteredList = parser.getRackList();
		return this.unfilteredList;
	}
	
	private List<Crime> unfilteredCrimeList() {
		this.unfilteredCrimeList = parser.getCrimesList();
		return this.unfilteredCrimeList;
	}

}
