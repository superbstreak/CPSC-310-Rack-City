package cs310MRAK.rackcity.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.maps.client.geom.LatLng;


public class Filter {

	private Parser parser;

	List<BikeRack> unfilteredRackList;
	List<BikeRack> filteredRackList;

	List<Crime> unfilteredCrimeList;
	List<Crime> filteredCrimeList;

	public Filter() {
		this.parser = new Parser();
		filteredRackList = new ArrayList<BikeRack>();
		filteredCrimeList = new ArrayList<Crime>();
		unfilteredRackList = parser.getRackList();
		unfilteredCrimeList = parser.getCrimesList();
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
	private List<BikeRack> filteredDistanceList(double radius, LatLng myLocation, List<BikeRack> list) {

		for(BikeRack rack : list) {
			double distanceX = (rack.getCoordinate().getLongitude()
					- myLocation.getLongitude());
			double distanceY = (rack.getCoordinate().getLatitude()
					- myLocation.getLatitude());
			double distance = Math.sqrt((distanceX)*(distanceX)
					+ (distanceY)*(distanceY));

			if (distance <= radius) {
				this.filteredRackList.add(rack);
			}
		}

		return this.filteredRackList;
	}
	
	/**
	 * Filters the list of crime locations based on distance from a given location
	 * and a user-defined radius
	 * 	- if there has been a crime within the given radius, this method will add
	 * 	  that crime's attributes to a filtered list
	 * @param radius
	 * @param myLocation
	 * @param list
	 * @return
	 */
	private List<Crime> filteredCrimeDistanceList(double radius, LatLng myLocation) {
		
		for (Crime crime : unfilteredCrimeList) {
			double distanceX = (crime.getCoordinate().getLongitude() - myLocation.getLongitude());
			double distanceY = (crime.getCoordinate().getLatitude() - myLocation.getLatitude());
			double distance = Math.sqrt((distanceX)*(distanceX) + (distanceY)*(distanceY));
			
			if (distance <= radius) {
				filteredCrimeList.add(crime);
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
				this.filteredRackList.add(rack);
			}
		}

		return this.filteredRackList;
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
				this.filteredRackList.add(rack);
			}
		}

		return this.filteredRackList;
	}
	
	/**
	 * Takes an unfiltered rack list and provides a single filtered list after running
	 * through several filters including distance, rating, and crimeScore
	 * @param myLocation
	 * @param radius
	 * @param rating
	 * @param crimeScore
	 * @return
	 */
	public List<BikeRack> getFilteredRackList(LatLng myLocation, double radius, double rating, int crimeScore) {
		
		for(BikeRack rack : unfilteredRackList) {
			double distanceX = (rack.getCoordinate().getLongitude() - myLocation.getLongitude());
			double distanceY = (rack.getCoordinate().getLatitude() - myLocation.getLatitude());
			double distance = Math.sqrt((distanceX)*(distanceX) + (distanceY)*(distanceY));
			System.out.println("Distance: " + distance);
			if (distance <= radius && rack.getRating() >= rating && rack.getCrimeScore() <= crimeScore) {
				System.out.println(rack.getCoordinate());
				filteredRackList.add(rack);
			}
		}
		
		return filteredRackList;
		
	}
	
	/**
	 * Takes an unfiltered crimes list from the parser and provides a single
	 * filtered list based on radius from a bike rack's location
	 * @param myLocation
	 * @param radius
	 * @return
	 */
	public List<Crime> getFilteredCrimeList(LatLng myLocation, double radius) {
		
		filteredCrimeList = filteredCrimeDistanceList(radius, myLocation);
		
		return filteredCrimeList;
	}

}
