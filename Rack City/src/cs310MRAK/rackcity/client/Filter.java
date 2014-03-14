package cs310MRAK.rackcity.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.maps.client.geom.LatLng;


public class Filter {

	private Parser parser;

	List<BikeRack> unfilteredRackList;
	List<Crime> unfilteredCrimeList;

	public Filter() {
		this.parser = new Parser();
		unfilteredRackList = parser.getRackList();
		unfilteredCrimeList = parser.getCrimesList();
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
		
		List<BikeRack> filteredRackList = new ArrayList<BikeRack>();
		
		for(BikeRack rack : unfilteredRackList) {
			if (calcLatLngDistance(rack.getCoordinate(), myLocation) <= radius && rack.getRating() >= rating && rack.getCrimeScore() <= crimeScore) {
				System.out.println("Rack Coordinate: "  + rack.getCoordinate());
				System.out.println("Distance: "  + calcLatLngDistance(rack.getCoordinate(), myLocation));
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
		
		List<Crime> filteredCrimeList = new ArrayList<Crime>();
		
		for (Crime crime : unfilteredCrimeList) {
			if (calcLatLngDistance(crime.getCoordinate(), myLocation) <= radius) {
				filteredCrimeList.add(crime);
			}
		}
		
		return filteredCrimeList;
	}
	
	/**
	 * Code adapted from http://stackoverflow.com/questions/837872/calculate-distance-in-meters-when-you-know-longitude-and-latitude-in-java
	 * Takes in a LatLng point and calculates the distance away from the current searched location
	 * @param rackPoint
	 * @return
	 */
	private double calcLatLngDistance(LatLng point1, LatLng point2){
		
		double lat1, lng1, lat2, lng2;
		lat1 = point1.getLatitude();
		lng1 = point1.getLongitude();
		lat2 = point2.getLatitude();
		lng2 = point2.getLongitude();
		
		
		double earthRadius = 3958.75;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    int meterConversion = 1609;

	    return (double) (dist * meterConversion)/1000;
	}

}
