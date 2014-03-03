package com.mrakconsultinggruwp.rackcity.client;

import com.google.appengine.api.search.GeoPoint;

public class CrimeGeoPoint {

	public GeoPoint crimeGeoPoint;
	public String addess;
	
	double getLatitude(){
		return crimeGeoPoint.getLatitude();
	}
	
	double getLongitude(){
		return crimeGeoPoint.getLongitude();

	}
	
	
	/**
	 * This is where our getAddress() would go. 
	 * But I think I'm using the wrong GeoPoint import. 
	 */
	
}
