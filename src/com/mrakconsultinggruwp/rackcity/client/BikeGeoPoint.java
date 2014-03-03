package com.mrakconsultinggruwp.rackcity.client;

import com.google.appengine.api.search.GeoPoint;

public class BikeGeoPoint {

	public int UID;
	public GeoPoint crimeGeoPoint;
	public String addess;
	public int crimeScore;
	public double rating;
	public CrimeGeoList crimeGeoList;
	
	
	public int getUID() {
		return UID;
	}

	public void setUID(int uID) {
		UID = uID;
	}

	public String getAddess() {
		return addess;
	}
	
	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public double getLatitude(){
		return crimeGeoPoint.getLatitude();
	}
	
	public double getLongitude(){
		return crimeGeoPoint.getLongitude();
	}
	
	public void setCrimeScore(int crimeScore) {
		// each BikeGeoPoint iterates through the CrimeGeoList and calculates its score accordingly.
		this.crimeScore = crimeScore;
	}
	
	public int getCrimeScore(){
		return crimeScore;
		
	}
	
	
	
}
